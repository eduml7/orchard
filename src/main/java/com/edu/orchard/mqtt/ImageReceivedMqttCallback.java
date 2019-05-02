package com.edu.orchard.mqtt;

import java.io.ByteArrayInputStream;
import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import com.edu.orchard.bots.CommandBotHandler;
import com.edu.orchard.bots.messagetimer.TimeToLive;

@Component
public class ImageReceivedMqttCallback {

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Value("${telegram.bots.users.allowed}")
	private Set<Long> allowedUsers;

	@Autowired
	private CommandBotHandler commandBotHandler;

	@ServiceActivator
	@TimeToLive
	public Set<Message> messageArrived(@Payload byte[] mqttMessage) {
		
		Set<Message> messageList = new HashSet<Message>();
		
		allowedUsers.stream().forEach(chatId -> {

			SendPhoto sendPhotoRequest = new SendPhoto();
			sendPhotoRequest.setChatId(chatId);
			sendPhotoRequest.setPhoto(String.valueOf(System.currentTimeMillis()),
					new ByteArrayInputStream(mqttMessage));
			try {
				messageList.add(commandBotHandler.execute(sendPhotoRequest));
			} catch (TelegramApiException e) {
				log.error("Something happened trying to send the image: {}", e);
			}
		});
		
		return messageList;
	}

}