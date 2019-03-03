package com.edu.orchard;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//@SpringBootApplication
public class OrchardApplication {

	public static void main(String[] args) {
		//SpringApplication.run(OrchardApplication.class, args);
		ArduinoConnection arduinoConnection = new ArduinoConnection();
		
		arduinoConnection.initialize();
		Thread t=new Thread() {
			public void run() {
				//the following line will keep this app alive for 10 seconds,
				//waiting for events to occur and responding to them (printing incoming messages to console).
				try {Thread.sleep(10000);} catch (InterruptedException ie) {}
			}
		};
		t.start();
System.out.println("Started");
	}

}
