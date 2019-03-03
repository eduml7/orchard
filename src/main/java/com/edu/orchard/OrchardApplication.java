package com.edu.orchard;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class OrchardApplication {

	public static void main(String[] args) {
		SpringApplication.run(OrchardApplication.class, args);
		ArduinoConnection arduinoConnection = new ArduinoConnection();

		arduinoConnection.initialize();
		Thread t = new Thread() {
			public void run() {
				try {
					Thread.sleep(10000);
				} catch (InterruptedException ie) {
				}
			}
		};
		t.start();
		System.out.println("Started");
	}

}
