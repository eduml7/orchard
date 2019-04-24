package com.edu.orchard.mqtt;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import com.edu.orchard.bots.CommandBotHandler;

@Component
public class ConfigMqttCallback{

	@Value("${telegram.bots.users.admin}")
	private Long admin;

//	@Autowired
//	@Qualifier("mqttClientConfig")
//	private MqttAsyncClient client;

	@Autowired
	private CommandBotHandler activeBotHandler;

	@ServiceActivator(inputChannel = "mqttConfigChannell")
	public void messageArrived(String topic, MqttMessage mqttMessage) {
		System.out.println("Message received:\n\t" + new String(mqttMessage.getPayload()));
		if (activeBotHandler != null) {
			try {
				activeBotHandler.execute(new SendMessage().setChatId(admin).setText(new String(mqttMessage.getPayload())));
			} catch (TelegramApiException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
//
//	@PostConstruct
//	private void mqttSubscribe() {
//		client.setCallback(this);
//		try {
//			client.connect();
//			client.subscribe("home/config/orchard");
//		} catch (MqttException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//	}
//
//	@PreDestroy
//	private void mqttUnSubscribe() {
//		try {
//			client.disconnect();
//			client.close();
//		} catch (MqttException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//	}
}