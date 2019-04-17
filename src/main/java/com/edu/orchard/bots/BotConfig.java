package com.edu.orchard.bots;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.edu.orchard.bots.commands.WaterCommand;

@Configuration
public class BotConfig {

	@Bean
	public WaterCommand waterBotCommand() {
		return new WaterCommand("water", "Water the orchard",
				"With this command, you can activate the relay to turn on the weater pump for 5 secs water flux");
	}
}
