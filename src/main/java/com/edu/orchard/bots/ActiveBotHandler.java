package com.edu.orchard.bots;

import java.util.Set;

import org.eclipse.paho.client.mqttv3.MqttException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import com.edu.orchard.service.WaterService;

@Component
public class ActiveBotHandler extends TelegramLongPollingBot {

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Value("${telegram.bots.botToken}")
	private String botToken;

	@Value("${telegram.bots.botUserName}")
	private String botUserName;

	@Value("${telegram.bots.users.allowed}")
	private Set<Long> allowedUsers;

	@Autowired
	private WaterService waterService;

	@Override
	public void onUpdateReceived(Update update) {

		try {
			if (allowedUsers.contains(update.getMessage().getChatId())) {
				if (update.getMessage().getText().contains("regar")) {
					waterService.waterOrchard();
					execute(new SendMessage().setChatId(update.getMessage().getChatId())
							.setText("Starting watering the orchard ..."));
				} else {
					execute(new SendMessage().setChatId(update.getMessage().getChatId())
							.setText("Sorry, I don't understand"));
				}
			} else {
				execute(new SendMessage().setChatId(update.getMessage().getChatId())
						.setText("You are not allowed to talk to me, sorry"));
			}
		} catch (TelegramApiException | MqttException e) {
			log.error("Error trying to water the orchard", e);
		}
	}

	@Override
	public String getBotUsername() {
		return botUserName;
	}

	@Override
	public String getBotToken() {
		return botToken;
	}

}
