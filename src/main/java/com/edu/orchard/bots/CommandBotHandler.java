package com.edu.orchard.bots;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.extensions.bots.commandbot.TelegramLongPollingCommandBot;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.helpCommand.HelpCommand;
import org.telegram.telegrambots.meta.api.objects.Update;

import com.edu.orchard.bots.commands.WaterCommand;

@Component
@DependsOn(value = "waterCommand")
public class CommandBotHandler extends TelegramLongPollingCommandBot {

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Value("${telegram.bots.botToken}")
	private String botToken;

	@Autowired
	@Qualifier("waterCommand")
	private WaterCommand waterCommand;

	public CommandBotHandler(@Value("${telegram.bots.botUserName}") String botUsername) {
		super(botUsername);
	}

	@Override
	public void processNonCommandUpdate(Update update) {
		// Nothing to do here
		log.info("PRUEBATORIDAS");
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
