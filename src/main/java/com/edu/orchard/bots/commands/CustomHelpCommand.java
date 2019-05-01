package com.edu.orchard.bots.commands;

import org.telegram.telegrambots.extensions.bots.commandbot.commands.IBotCommand;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.ICommandRegistry;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.helpCommand.HelpCommand;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import com.edu.orchard.auth.Securized;

public class CustomHelpCommand extends HelpCommand {
	
	private static final String PARSE_MODE_HTML = "HTML"; 

	@Override
	@Securized
	public void execute(AbsSender absSender, User user, Chat chat, String[] arguments) {
		if (ICommandRegistry.class.isInstance(absSender)) {
			ICommandRegistry registry = (ICommandRegistry) absSender;
			
			if (arguments.length > 0) {
				IBotCommand command = registry.getRegisteredCommand(arguments[0]);
				String reply = getManText(command);
				try {
					absSender.execute(new SendMessage(chat.getId(), reply).setParseMode(PARSE_MODE_HTML));
				} catch (TelegramApiException e) {
					e.printStackTrace();
				}
			} else {
				String reply = getHelpText(registry);
				try {
					absSender.execute(new SendMessage(chat.getId(), reply).setParseMode(PARSE_MODE_HTML));
				} catch (TelegramApiException e) {
					e.printStackTrace();
				}
			}
		}
	}

}

