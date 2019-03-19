package com.edu.orchard.bots;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
public class OrchardGangsta extends TelegramLongPollingBot {

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Value("${telegram.bots.botToken}")
	private String botToken;

	@Value("${telegram.bots.botUserName}")
	private String botUserName;

	@Override
	public void onUpdateReceived(Update update) {

		try {
			execute(new SendMessage().setChatId(update.getMessage().getChatId())
					.setText("I have already watered the orchard"));

		} catch (TelegramApiException e) {
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
