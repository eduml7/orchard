package com.edu.orchard.mqtt.config;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.endpoint.MessageProducerSupport;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;

@Configuration
public class MQTTConfiguration {

	@Bean
	MqttPahoClientFactory mqttClientFactory() {
		String[] conn = { "tcp://localhost:1883" };
		MqttConnectOptions config = new MqttConnectOptions();
		config.setServerURIs(conn);
		DefaultMqttPahoClientFactory clientFactory = new DefaultMqttPahoClientFactory();
		clientFactory.setConnectionOptions(config);
		// To avoid .lck files
		clientFactory.setPersistence(new MemoryPersistence());
		return clientFactory;
	}

	@Bean
	MessageChannel mqttOutboundChannel() {
		return new DirectChannel();
	}

	@Bean
	@ServiceActivator(inputChannel = "mqttOutboundChannel")
	public MessageHandler mqttOutbound() {
		MqttPahoMessageHandler messageHandler = new MqttPahoMessageHandler(MqttClient.generateClientId(),
				mqttClientFactory());
		messageHandler.setAsync(true);
		messageHandler.setDefaultTopic("home/orchard/water");
		return messageHandler;
	}

	@MessagingGateway(defaultRequestChannel = "mqttOutboundChannel")
	public interface DeviceGateway {
		void sendToMqtt(String payload);
	}

	@Bean
	public MessageProducerSupport mqttInboundConfig() {
		MqttPahoMessageDrivenChannelAdapter adapter = new MqttPahoMessageDrivenChannelAdapter(
				MqttClient.generateClientId(), mqttClientFactory(), "home/config/orchard");
		adapter.setCompletionTimeout(5000);
		adapter.setConverter(new DefaultPahoMessageConverter());
		adapter.setQos(1);
		return adapter;
	}

	@Bean
	public IntegrationFlow mqttInFlow() {
		return IntegrationFlows.from(mqttInboundConfig()).transform(p -> p).handle("configMqttCallback", "messageArrived")
				.get();
	}

	@Bean
	public MessageProducerSupport mqttInboundWaterResponse() {
		MqttPahoMessageDrivenChannelAdapter adapter = new MqttPahoMessageDrivenChannelAdapter(
				MqttClient.generateClientId(), mqttClientFactory(), "home/orchard/water/response");
		adapter.setCompletionTimeout(5000);
		adapter.setConverter(new DefaultPahoMessageConverter());
		adapter.setQos(1);
		return adapter;
	}

	@Bean
	public IntegrationFlow mqttInFlowWaterResponse() {
		return IntegrationFlows.from(mqttInboundWaterResponse()).transform(p -> p)
				.handle("waterResponseMqttCallback", "messageArrived").get();
	}
	
	@Bean
	MessageChannel mqttInputChannel() {
		return new DirectChannel();
	}
	//TODO: review if request/response could be handles in one flow
	@Bean
	public MessageProducerSupport inbound() {
		MqttPahoMessageDrivenChannelAdapter adapter = new MqttPahoMessageDrivenChannelAdapter(
				MqttClient.generateClientId(), mqttClientFactory(), "home/orchard/watcher/photo");
		adapter.setCompletionTimeout(5000000);
	    DefaultPahoMessageConverter converter = new DefaultPahoMessageConverter();
	    converter.setPayloadAsBytes(true);
	    adapter.setConverter(converter);
		adapter.setQos(1);

		return adapter;
	}
	
	@Bean
	public IntegrationFlow mqttinbound() {
		return IntegrationFlows.from(inbound())
				.handle("imageReceivedMqttCallback", "messageArrived")
				.nullChannel();
	}
	
	@Bean
	MessageChannel PhotoChannel() {
		return new DirectChannel();
	}

	@Bean
	@ServiceActivator(inputChannel = "photoChannel")
	public MessageHandler mqttImageOutbound() {
		MqttPahoMessageHandler messageHandler = new MqttPahoMessageHandler(MqttClient.generateClientId(),
				mqttClientFactory());
		messageHandler.setAsync(true);
		messageHandler.setDefaultTopic("home/orchard/watcher");
		return messageHandler;
	}

	@MessagingGateway(defaultRequestChannel = "photoChannel")
	public interface Gateway {
		void sendToMqtt(String payload);
	}

}
