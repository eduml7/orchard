package com.edu.orchard.bots.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.edu.orchard.bots.commands.PhotoCommand;
import com.edu.orchard.bots.commands.StartCommand;
import com.edu.orchard.bots.commands.WaterCommand;

@Configuration
public class BotConfig {

	@Bean
	public WaterCommand waterBotCommand() {
		return new WaterCommand("water", "Water the orchard",
				"With this command, you can activate the relay to turn on the weater pump for 5 secs water flux");
	}
	
	@Bean
	public PhotoCommand photoBotCommand() {
		return new PhotoCommand("photo", "Takes a photo",
				"With this command, you can be a beholder");
	}
	
	@Bean
	public StartCommand startBotCommand() {
		return new StartCommand("start", "Starts bot conversation",
				"No more info, just starts the bot!");
	}
}
