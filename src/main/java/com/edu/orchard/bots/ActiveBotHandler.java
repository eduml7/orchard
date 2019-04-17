package com.edu.orchard.bots;

import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

//@Component
//public class ActiveBotHandler extends TelegramLongPollingBot {
//
//	private final Logger log = LoggerFactory.getLogger(this.getClass());
//
//	@Value("${telegram.bots.botToken}")
//	private String botToken;
//
//	@Value("${telegram.bots.botUserName}")
//	private String botUserName;
//
//	@Value("${telegram.bots.users.allowed}")
//	private Set<Long> allowedUsers;
//
//	@Override
//	public void onUpdateReceived(Update update) {
//		// TODO: handle this with aspect and annotation @Securize
//		Long user = update.getMessage().getChatId();
//		try {
//			if (allowedUsers.contains(user)) {
//				execute(new SendMessage().setChatId(user).setText("Sorry, I don't understand"));
//			} else {
//				execute(new SendMessage().setChatId(user).setText("You are not allowed to talk to me, sorry"));
//			}
//		} catch (TelegramApiException e) {
//			log.error("Error in active chatting with bot", e);
//		}
//	}
//
//	@Override
//	public String getBotUsername() {
//		return botUserName;
//	}
//
//	@Override
//	public String getBotToken() {
//		return botToken;
//	}
//
//}
