package com.edu.orchard;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.telegram.telegrambots.ApiContextInitializer;

@SpringBootApplication
public class OrchardApplication {

	public static void main(String[] args) {
		ApiContextInitializer.init();
		SpringApplication.run(OrchardApplication.class, args);
	}

}
