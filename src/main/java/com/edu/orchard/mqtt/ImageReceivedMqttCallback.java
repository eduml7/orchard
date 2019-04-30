package com.edu.orchard.mqtt;

import java.io.ByteArrayInputStream;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import com.edu.orchard.bots.CommandBotHandler;

@Component
public class ImageReceivedMqttCallback {

	@Value("${telegram.bots.users.allowed}")
	private Set<Long> allowedUsers;

	@Autowired
	private CommandBotHandler activeBotHandler;

	@ServiceActivator
	public void messageArrived(@Payload byte[] mqttMessage) {

		allowedUsers.stream().forEach(chatId -> {

			SendPhoto sendPhotoRequest = new SendPhoto();
			sendPhotoRequest.setChatId(chatId);
			sendPhotoRequest.setPhoto(String.valueOf(System.currentTimeMillis()),
					new ByteArrayInputStream(mqttMessage));
			try {
				activeBotHandler.execute(sendPhotoRequest);
			} catch (TelegramApiException e) {
				e.printStackTrace();
			}
		});
	}

}