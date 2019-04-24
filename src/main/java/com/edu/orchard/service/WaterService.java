package com.edu.orchard.service;

import org.eclipse.paho.client.mqttv3.MqttException;

public interface WaterService {

	public void waterOrchard() throws MqttException;

}
