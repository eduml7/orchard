const int sensorPin = A0;

void setup() {
  Serial.begin(9600);
}

void loop()
{
  int humidity = analogRead(sensorPin);
  delay(5000);
  Serial.println(humidity);

}
