package com.edu.orchard.config;

import javax.websocket.MessageHandler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.h2.H2ConsoleProperties.Settings;
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

//@Configuration
//@IntegrationComponentScan
@Deprecated
public class MQTTConfiguration {

//	@Autowired
//	private Settings settings;
//	@Autowired
//	private DevMqttMessageListener messageListener;
//
//	@Bean
//	MqttPahoClientFactory mqttClientFactory() {
//		DefaultMqttPahoClientFactory clientFactory = new DefaultMqttPahoClientFactory();
//		clientFactory.setServerURIs(settings.getMqttBrokerUrl());
//		clientFactory.setUserName(settings.getMqttBrokerUser());
//		clientFactory.setPassword(settings.getMqttBrokerPassword());
//		return clientFactory;
//	}
//
//	@Bean
//	MessageChannel mqttOutboundChannel() {
//		return new DirectChannel();
//	}
//
//	@Bean
//	@ServiceActivator(inputChannel = "mqttOutboundChannel")
//	public MessageHandler mqttOutbound() {
//		MqttPahoMessageHandler messageHandler = new MqttPahoMessageHandler("dev-client-outbound", mqttClientFactory());
//		messageHandler.setAsync(true);
//		messageHandler.setDefaultTopic(settings.getMqttPublishTopic());
//		return messageHandler;
//	}
//
//	@MessagingGateway(defaultRequestChannel = "mqttOutboundChannel")
//	public interface DeviceGateway {
//		void sendToMqtt(String payload);
//	}
}
