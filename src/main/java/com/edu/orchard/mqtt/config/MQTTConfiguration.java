package com.edu.orchard.mqtt.config;

import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;

@Configuration
@IntegrationComponentScan
public class MQTTConfiguration {

	@Bean
	MqttPahoClientFactory mqttClientFactory() {
		String[] conn = { "tcp://localhost:1883" };
		MqttConnectOptions config = new MqttConnectOptions();
		config.setServerURIs(conn);
		DefaultMqttPahoClientFactory clientFactory = new DefaultMqttPahoClientFactory();
		clientFactory.setConnectionOptions(config);
		return clientFactory;
	}

	@Bean
	MessageChannel mqttOutboundChannel() {
		return new DirectChannel();
	}

	@Bean
	@ServiceActivator(inputChannel = "mqttOutboundChannel")
	public MessageHandler mqttOutbound() {
		MqttPahoMessageHandler messageHandler = new MqttPahoMessageHandler("dev-client-outbound", mqttClientFactory());
		messageHandler.setAsync(true);
		messageHandler.setDefaultTopic("home/orchard/water");
		return messageHandler;
	}

	@MessagingGateway(defaultRequestChannel = "mqttOutboundChannel")
	public interface DeviceGateway {
		void sendToMqtt(String payload);
	}
	
	@Bean
	MessageChannel mqttConfigChannell() {
		return new DirectChannel();
	}

	@Bean
	public MessageHandler mqttOutboundConfigChannel() {
		MqttPahoMessageHandler messageHandler = new MqttPahoMessageHandler("mqttConfigChannell", mqttClientFactory());
		messageHandler.setAsync(true);
		messageHandler.setDefaultTopic("home/config/orchard");

		return messageHandler;
	}

}
