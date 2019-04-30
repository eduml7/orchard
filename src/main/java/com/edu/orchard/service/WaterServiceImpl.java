package com.edu.orchard.service;

import org.eclipse.paho.client.mqttv3.MqttException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.edu.orchard.mqtt.config.MQTTConfiguration.DeviceGateway;

@Service
public class WaterServiceImpl implements WaterService {

	@Autowired
	private DeviceGateway deviceGateway;

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	public void waterOrchard() throws MqttException {
		log.info("Water orchard");
		deviceGateway.sendToMqtt("Please, water the orchard");
	}

}
