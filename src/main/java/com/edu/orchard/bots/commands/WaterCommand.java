package com.edu.orchard.bots.commands;

import org.eclipse.paho.client.mqttv3.MqttException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.helpCommand.ManCommand;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;

import com.edu.orchard.auth.Securized;
import com.edu.orchard.service.WaterService;

public class WaterCommand extends ManCommand {

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private WaterService waterService;

	public WaterCommand(String commandIdentifier, String description, String extendedDescription) {
		super(commandIdentifier, description, extendedDescription);
	}

	@Override
	//@Securized
	public void execute(AbsSender absSender, User user, Chat chat, String[] arguments) {
		try {
			log.info("Watering the orchard ...");
			waterService.waterOrchard();
		} catch (MqttException e) {
			log.error("Error trying to water the orchard", e);
		}

	}

}
