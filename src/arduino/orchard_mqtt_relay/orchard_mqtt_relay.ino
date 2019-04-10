#include <ESP8266WiFi.h>;
#include <PubSubClient.h>

const char* ssid = "*******";
const char* password =  "*******";
const char* mqttServer = "*********";
const int mqttPort = 1883;

#define RELAY 0 // relay connected to  GPIO0

WiFiClient espClient;
PubSubClient client(espClient);

void setup() {

  Serial.begin(115200);
  pinMode(RELAY, OUTPUT);
  digitalWrite(RELAY, LOW);

  WiFi.begin(ssid, password);

  while (WiFi.status() != WL_CONNECTED) {
    delay(500);
    Serial.println("Connecting to WiFi..");
  }
  Serial.println("Connected to the WiFi network");

  client.setServer(mqttServer, mqttPort);
  client.setCallback(callback);

  while (!client.connected()) {
    Serial.println("Connecting to MQTT...");

    if (client.connect("ESP8266Client")) {
      Serial.println("connected");

    } else {

      Serial.print("failed with state ");
      Serial.print(client.state());
      delay(2000);

    }
  }
char buf[22];
sprintf(buf, "RELAY IP:%d.%d.%d.%d", WiFi.localIP()[0], WiFi.localIP()[1], WiFi.localIP()[2], WiFi.localIP()[3] );

  client.publish("home/config/orchard", buf);
  client.subscribe("home/orchard/water");
}

void callback(char* topic, byte* payload, unsigned int length) {

  Serial.print("Message arrived in topic: ");
  Serial.println(topic);

  Serial.print("Message:");
  for (int i = 0; i < length; i++) {
    Serial.print((char)payload[i]);
  }

  Serial.println();
  Serial.println("Watering 5 seconds-----------------------");
  digitalWrite(RELAY, HIGH);
  delay(5000);
  digitalWrite(RELAY, LOW);
  Serial.println("Watered -----------------------");
  client.publish("home/orchard/water/response", "Watered, man");
}

void loop() {
  client.loop();
}
