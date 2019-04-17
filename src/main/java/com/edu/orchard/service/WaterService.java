package com.edu.orchard.service;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class WaterService {

	@Autowired
	@Qualifier("mqttClientWater")
	private MqttClient client;
	
	private final Logger log = LoggerFactory.getLogger(this.getClass());

	public void waterOrchard() throws MqttException {
		log.info("Water orchard");
		client.connect();
		MqttMessage message = new MqttMessage();
		message.setPayload("Please, water the orchard".getBytes());
		client.publish("home/orchard/water", message);
		client.disconnect();
	}

}
