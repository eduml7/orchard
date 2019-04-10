package com.edu.orchard;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.telegram.telegrambots.ApiContextInitializer;

@SpringBootApplication
public class OrchardApplication {

	public static void main(String[] args) {
		ApiContextInitializer.init();
		SpringApplication.run(OrchardApplication.class, args);
	}

	@Bean
	public MqttClient mqttClientConfig() throws MqttException {
		MqttClient client = new MqttClient("tcp://localhost:1883", MqttClient.generateClientId());
		return client;
	}
	
	@Bean
	public MqttClient mqttClientWaterResponse() throws MqttException {
		MqttClient client = new MqttClient("tcp://localhost:1883", MqttClient.generateClientId());
		return client;
	}
	
	//TODO: prototype or factory
	@Bean
	public MqttClient mqttClientWater() throws MqttException {
		MqttClient client = new MqttClient("tcp://localhost:1883", MqttClient.generateClientId());
		return client;
	}

}
