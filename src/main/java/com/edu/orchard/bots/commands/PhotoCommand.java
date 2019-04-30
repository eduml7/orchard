package com.edu.orchard.bots.commands;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.helpCommand.ManCommand;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;

import com.edu.orchard.auth.Securized;
import com.edu.orchard.mqtt.config.MQTTConfiguration.Gateway;

public class PhotoCommand extends ManCommand {

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private Gateway deviceGateway;

	public PhotoCommand(String commandIdentifier, String description, String extendedDescription) {
		super(commandIdentifier, description, extendedDescription);
	}

	@Override
	@Securized
	public void execute(AbsSender absSender, User user, Chat chat, String[] arguments) {
		log.info("Taking a photo ...");
		deviceGateway.sendToMqtt("Photo");
	}

}
