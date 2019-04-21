package com.edu.orchard.bots;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.extensions.bots.commandbot.TelegramLongPollingCommandBot;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.helpCommand.HelpCommand;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import com.edu.orchard.auth.Securized;
import com.edu.orchard.bots.commands.WaterCommand;

@Component
public class CommandBotHandler extends TelegramLongPollingCommandBot {

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Value("${telegram.bots.botToken}")
	private String botToken;

	@Autowired
	private WaterCommand waterCommand;

	public CommandBotHandler(@Value("${telegram.bots.botUserName}") String botUsername) {
		super(botUsername);
	}

	@Override
	@Securized
	public void processNonCommandUpdate(Update update) {
		try {
			execute(new SendMessage().setChatId(update.getMessage().getChatId())
					.setText("The same: " + update.getMessage().getText()));
		} catch (TelegramApiException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.error("ERROR ", e);
		}
	}

	@Override
	public String getBotToken() {
		return botToken;
	}

	@PostConstruct
	public void registerCommands() {
		register(new HelpCommand());
		register(waterCommand);
	}

}
