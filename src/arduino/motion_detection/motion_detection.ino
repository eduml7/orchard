#include <WiFi.h>
#include <PubSubClient.h>
#include "esp_camera.h"

const char* ssid = "**********";
const char* password = "************";
const char* mqttServer = "*********";
const int mqttPort = 1883;

//ESP32 output pin
int pirPin = 2;
int ledPin = 14;
int flashPin = 4;

//motionDetection command config
bool flash = false;
bool motionDetection = false;

//motiondetection sensor config
long unsigned int transitionTimeFromHighToLow;
long unsigned int millisLowToAssureMotionStop = 5000;
boolean isNewMotionSequence = true;
boolean takeLowTime;

WiFiClient espClient;
PubSubClient client(espClient);

void setup() {

  Serial.begin(115200);
  WiFi.begin(ssid, password);
  pinMode(pirPin, INPUT);
  pinMode(ledPin , OUTPUT);
  pinMode(flashPin , OUTPUT);

  // wifi init
  while (WiFi.status() != WL_CONNECTED) {
    delay(500);
    Serial.println("Connecting to WiFi..");
  }
  Serial.println("Connected to the WiFi network");

  client.setServer(mqttServer, mqttPort);

  while (!client.connected()) {
    Serial.println("Connecting to MQTT...");
    if (client.connect("ESP32Client")) {
      Serial.println("connected");
    } else {
      Serial.print("failed with state ");
      Serial.print(client.state());
      delay(2000);
    }
  }

  char buf[32];
  sprintf(buf, "MOTION DETECTION CAM IP:%d.%d.%d.%d", WiFi.localIP()[0], WiFi.localIP()[1], WiFi.localIP()[2], WiFi.localIP()[3]);
  client.publish("home/config/orchard", buf);

  char motionConfig[300];
  sprintf(motionConfig, "Motion detection config\nEnabled motion detection: %s \nEnabled flash: %s", motionDetection ? "true" : "false", flash ? "true" : "false" );
  client.publish("home/config/orchard", motionConfig);

  // camera init
  camera_config_t config;
  config.ledc_channel = LEDC_CHANNEL_0;
  config.ledc_timer = LEDC_TIMER_0;
  config.pin_d0 = 5;
  config.pin_d1 = 18;
  config.pin_d2 = 19;
  config.pin_d3 = 21;
  config.pin_d4 = 36;
  config.pin_d5 = 39;
  config.pin_d6 = 34;
  config.pin_d7 = 35;
  config.pin_xclk = 0;
  config.pin_pclk = 22;
  config.pin_vsync = 25;
  config.pin_href = 23;
  config.pin_sscb_sda = 26;
  config.pin_sscb_scl = 27;
  config.pin_pwdn = 32;
  config.pin_reset = -1;
  config.xclk_freq_hz = 20000000;
  config.pixel_format = PIXFORMAT_JPEG;
  config.frame_size = FRAMESIZE_SVGA;
  config.jpeg_quality = 12;
  config.fb_count = 1;

  esp_err_t err = esp_camera_init(&config);
  if (err != ESP_OK) {
    Serial.printf("Camera init failed with error 0x%x", err);
    return;
  }

  client.subscribe("home/orchard/motion_detection");
  client.setCallback(motion_detection_callback);
}

/*
    /motiondetection
    command handler, in case of command received if motion detection is enabled, disables it and vice-versa
    flash - can enable the flash on snapshots. If flash param is received, enables or disables it
*/
void motion_detection_callback(char* topic, byte* payload, unsigned int length) {
  if (!strncmp((char *)payload, "flash", length)) {
    flash = !flash;
  } else {
    // enables / disables motion detection
    motionDetection = !motionDetection;
  }
  char buf[300];
  sprintf(buf, "Motion detection config\nEnabled motion detection: %s \nEnabled flash: %s", motionDetection ? "true" : "false", flash ? "true" : "false" );
  client.publish("home/config/orchard", buf);
}

/*
   This method takes an snapshot with the camera and sends the image
   to home/orchard/watcher/photo mqtt topic
*/
esp_err_t take_snapshot() {
  //capture a frame
  camera_fb_t * fb = esp_camera_fb_get();
  if (!fb) {
    Serial.println("Frame buffer could not be acquired");
    ESP_LOGE(TAG, "Frame buffer could not be acquired");
    return ESP_FAIL;
  }

  size_t buf_len = fb->len;
  Serial.println("---Buf Len ");
  Serial.println(buf_len);

  client.beginPublish("home/orchard/watcher/photo",  fb->len, false);

  size_t meison = 0;
  static const size_t bufferSize = 4096;
  static uint8_t buffer[bufferSize] = {0xFF};
  while (buf_len) {
    size_t copy = (buf_len < bufferSize) ? buf_len : bufferSize;
    Serial.println(copy);
    memcpy ( &buffer, &fb->buf[meison], copy );
    client.write(&buffer[0], copy);
    buf_len -= copy;
    meison += copy;
  }

  client.endPublish();
  esp_camera_fb_return(fb);
  return ESP_OK;
}

/**
   Starts detecting motion. If detects it, sends a photo
*/
void detect_motion() {

  // Detected motion
  if (digitalRead(pirPin) == HIGH) {
    if (isNewMotionSequence) {
      isNewMotionSequence = false;
      delay(500);
      digitalWrite(ledPin , HIGH);
      if (flash) {
        Serial.println("Picture with Flash");
        digitalWrite(flashPin , HIGH);
        take_snapshot();
        digitalWrite(flashPin , LOW);
      } else {
        take_snapshot();
      }
    }
    takeLowTime = true;
  }

  // Not detected motion
  if (digitalRead(pirPin) == LOW) {
    if (takeLowTime) {
      transitionTimeFromHighToLow = millis();
      takeLowTime = false;
    }
    // To prevent send images in continuous motion sequences
    if (!isNewMotionSequence && millis() - transitionTimeFromHighToLow > millisLowToAssureMotionStop) {
      isNewMotionSequence = true;
      delay(50);
      digitalWrite(ledPin , LOW);
    }
  }
}

void loop() {
  client.loop();
  if (motionDetection) {
    detect_motion();
  }
}
