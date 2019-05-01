package com.edu.orchard.mqtt;

import java.io.ByteArrayInputStream;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import com.edu.orchard.bots.CommandBotHandler;

@Component
public class ImageReceivedMqttCallback {

	private final Logger log = LoggerFactory.getLogger(this.getClass());

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
				Message message = activeBotHandler.execute(sendPhotoRequest);
				// TODO, make this an annotation or something reusable
				// If reusable, make thread pool here,
				//Shoould be reusable, to delte the command too
				ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
				executor.schedule(new Runnable() {
					public void run() {
						try {
							log.info("Executing scheduled deletion for message with id {}", message.getMessageId());
							DeleteMessage deleteMessage = new DeleteMessage();
							deleteMessage.setChatId(message.getChatId().toString());
							deleteMessage.setMessageId(message.getMessageId());
							activeBotHandler.execute(deleteMessage);
						} catch (TelegramApiException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}, 1, TimeUnit.MINUTES);
				executor.shutdown();

			} catch (TelegramApiException e) {
				e.printStackTrace();
			}
		});
	}

}