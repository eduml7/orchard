package com.edu.orchard.bots.commands;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.helpCommand.ManCommand;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;

import com.edu.orchard.auth.Securized;

public class StartCommand extends ManCommand {

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	public StartCommand(String commandIdentifier, String description, String extendedDescription) {
		super(commandIdentifier, description, extendedDescription);
	}

	@Override
	@Securized
	public void execute(AbsSender absSender, User user, Chat chat, String[] arguments) {
		log.info("This guy called {} started talking to me!", user.getUserName());
	}

}
