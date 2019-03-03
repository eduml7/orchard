const int sensorPin = A0;

void setup() {
  Serial.begin(9600);
}

void loop()
{
  int humidity = analogRead(sensorPin);

  if (humidity < 500)
  {
    Serial.print(humidity);
    Serial.println(" humidity, water please");
  }
  delay(1000);

  Serial.print(humidity);
  Serial.println(" humidity no water needed");
}
