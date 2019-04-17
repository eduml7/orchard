package com.edu.orchard.mqtt;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import com.edu.orchard.bots.CommandBotHandler;

@Component
public class WaterResponseMqttCallback implements MqttCallback {

	@Value("${telegram.bots.users.admin}")
	private Long admin;
	
	@Autowired
	@Qualifier("mqttClientWaterResponse")
	private MqttClient client;

	@Autowired
	private CommandBotHandler activeBotHandler;

	@Override
	public void connectionLost(Throwable cause) {
		System.out.println("Connection to MQTT broker lost!");
	}

	@Override
	public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {
		System.out.println("Message received:\n\t" + new String(mqttMessage.getPayload()));
		if (activeBotHandler != null)
			activeBotHandler
					.execute(new SendMessage().setChatId(admin).setText(new String(mqttMessage.getPayload())));
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
			client.subscribe("home/orchard/water/response");
		} catch (MqttException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	@PreDestroy
	private void mqttUnSubscribe() {
		try {
			client.disconnect();
			client.close();
		} catch (MqttException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}