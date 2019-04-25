package com.edu.orchard.mqtt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import com.edu.orchard.bots.CommandBotHandler;

@Component
public class WaterResponseMqttCallback {

	@Value("${telegram.bots.users.admin}")
	private Long admin;

	@Autowired
	private CommandBotHandler activeBotHandler;

	@ServiceActivator
	public void messageArrived(@Payload String mqttMessage) throws TelegramApiException {
		activeBotHandler.execute(new SendMessage().setChatId(admin).setText(new String(mqttMessage)));
	}

}