#include <WiFi.h>
#include <PubSubClient.h>
#include "esp_camera.h"

const char* ssid = "************";
const char* password = "************";
const char* mqttServer = "192.168.0.161";
const int mqttPort = 1883;

#define CAMERA_MODEL_AI_THINKER
#define PWDN_GPIO_NUM     32
#define RESET_GPIO_NUM    -1
#define XCLK_GPIO_NUM      0
#define SIOD_GPIO_NUM     26
#define SIOC_GPIO_NUM     27

#define Y9_GPIO_NUM       35
#define Y8_GPIO_NUM       34
#define Y7_GPIO_NUM       39
#define Y6_GPIO_NUM       36
#define Y5_GPIO_NUM       21
#define Y4_GPIO_NUM       19
#define Y3_GPIO_NUM       18
#define Y2_GPIO_NUM        5
#define VSYNC_GPIO_NUM    25
#define HREF_GPIO_NUM     23
#define PCLK_GPIO_NUM     22
#define MQTT_KEEPALIVE  500
#define MQTT_SOCKET_TIMEOUT  500
#define MQTT_MAX_TRANSFER_SIZE 80
#define MQTT_MAX_PACKET_SIZE 300000

WiFiClient espClient;
PubSubClient client(espClient);

void setup() {

  Serial.begin(115200);

  WiFi.begin(ssid, password);

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
  sprintf(buf, "LIVING ROOM CAM IP:%d.%d.%d.%d", WiFi.localIP()[0], WiFi.localIP()[1], WiFi.localIP()[2], WiFi.localIP()[3] );
  client.publish("home/config/orchard", buf);

  camera_config_t config;
  config.ledc_channel = LEDC_CHANNEL_0;
  config.ledc_timer = LEDC_TIMER_0;
  config.pin_d0 = Y2_GPIO_NUM;
  config.pin_d1 = Y3_GPIO_NUM;
  config.pin_d2 = Y4_GPIO_NUM;
  config.pin_d3 = Y5_GPIO_NUM;
  config.pin_d4 = Y6_GPIO_NUM;
  config.pin_d5 = Y7_GPIO_NUM;
  config.pin_d6 = Y8_GPIO_NUM;
  config.pin_d7 = Y9_GPIO_NUM;
  config.pin_xclk = XCLK_GPIO_NUM;
  config.pin_pclk = PCLK_GPIO_NUM;
  config.pin_vsync = VSYNC_GPIO_NUM;
  config.pin_href = HREF_GPIO_NUM;
  config.pin_sscb_sda = SIOD_GPIO_NUM;
  config.pin_sscb_scl = SIOC_GPIO_NUM;
  config.pin_pwdn = PWDN_GPIO_NUM;
  config.pin_reset = RESET_GPIO_NUM;
  config.xclk_freq_hz = 20000000;
  config.pixel_format = PIXFORMAT_JPEG;
  config.frame_size = FRAMESIZE_SVGA;
  config.jpeg_quality = 12;
  config.fb_count = 1;

  // camera init
  esp_err_t err = esp_camera_init(&config);
  if (err != ESP_OK) {
    Serial.printf("Camera init failed with error 0x%x", err);
    return;
  }
  camera_example_capture();
  client.subscribe("home/orchard/watcher");
  client.setCallback(callback);
}

esp_err_t camera_example_capture() {
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

void callback(char* topic, byte* payload, unsigned int length) {

  Serial.print("Message arrived in topic: ");
  Serial.println(topic);

  Serial.print("Message:");
  for (int i = 0; i < length; i++) {
    Serial.print((char)payload[i]);
  }
  
  camera_example_capture();
}

void loop() {
  client.loop();
}
