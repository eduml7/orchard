package com.edu.orchard.service;

import javax.annotation.PostConstruct;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import com.edu.orchard.bots.OrchardGangsta;

@Component
public class ConfigMqttCallback implements MqttCallback {

	@Autowired
	@Qualifier("mqttClientConfig")
	private MqttClient client;

	@Autowired
	private OrchardGangsta orchardGangsta;

	@Override
	public void connectionLost(Throwable cause) {
		System.out.println("Connection to MQTT broker lost!");
	}

	@Override
	public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {
		System.out.println("Message received:\n\t" + new String(mqttMessage.getPayload()));
		if (orchardGangsta != null)
			//TODO: resolve chatId Issue 
			orchardGangsta
					.execute(new SendMessage().setChatId("******").setText(new String(mqttMessage.getPayload())));
	}

	@Override
	public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
		// not used in this example
	}

	@PostConstruct
	private void mqttSubscribe() {
		client.setCallback(this);
		try {
			client.connect();
			client.subscribe("home/config/orchard");
		} catch (MqttException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}