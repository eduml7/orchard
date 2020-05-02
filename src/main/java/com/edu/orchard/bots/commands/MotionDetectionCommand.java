package com.edu.orchard.bots.commands;

import java.util.Arrays;

import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.helpCommand.ManCommand;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;

import com.edu.orchard.auth.Securized;
import com.edu.orchard.mqtt.config.MQTTConfiguration.MotionDetectionGateway;


public class MotionDetectionCommand extends ManCommand {

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	private static final String ENABLE_FLASH = "flash";

	@Autowired
	private MotionDetectionGateway deviceGateway;

	public MotionDetectionCommand(String commandIdentifier, String description, String extendedDescription) {
		super(commandIdentifier, description, extendedDescription);
	}

	@Override
	@Securized
	public void execute(AbsSender absSender, User user, Chat chat, String[] arguments) {
		log.info("MotionDetectionCommand received with params {}", Arrays.toString(arguments));
		deviceGateway.sendToMqtt(setPayload(arguments));
	}

	private String setPayload(String[] arguments) {
		String payload = "MotionDetectionCommand";
		if (!ArrayUtils.isEmpty(arguments)) {
			switch (arguments[0]) {
			case ENABLE_FLASH:
				payload = ENABLE_FLASH;
				break;
			}
		}
		return payload;
	}

}
