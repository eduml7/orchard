package com.edu.orchard.connection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.TooManyListenersException;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.edu.orchard.service.HumidityService;

import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import gnu.io.UnsupportedCommOperationException;

//@Component
@Deprecated
public class ArduinoSerialConnection implements SerialPortEventListener {

	@Autowired
	private HumidityService humidityService;

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	private BufferedReader input;
	private SerialPort serialPort;
	private final String PORT_NAME = "/dev/ttyACM0";
	private static final int TIME_OUT = 2000;
	private static final int DATA_RATE = 9600;

	@PostConstruct
	public void connect() {
		try {
			CommPortIdentifier port = CommPortIdentifier.getPortIdentifier(PORT_NAME);
			serialPort = (SerialPort) port.open(this.getClass().getName(), TIME_OUT);

			serialPort.setSerialPortParams(DATA_RATE, SerialPort.DATABITS_8, SerialPort.STOPBITS_1,
					SerialPort.PARITY_NONE);

			input = new BufferedReader(new InputStreamReader(serialPort.getInputStream()));
			serialPort.addEventListener(this);
			serialPort.notifyOnDataAvailable(true);

		} catch (NoSuchPortException | PortInUseException | IOException | UnsupportedCommOperationException
				| TooManyListenersException e) {
			log.error("Connection to serial port failed: {}", e.getMessage(), e);
		}
	}

	@PreDestroy
	public void disconnect() {

		log.info("Disconnecting serial port ...");

		if (serialPort != null) {
			serialPort.close();
		}
	}

	@Override
	public synchronized void serialEvent(SerialPortEvent oEvent) {
		if (oEvent.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
			try {

				String inputLine = input.readLine();
				log.info(inputLine);
				humidityService.processHumidityInfo(Integer.parseInt(inputLine));

			} catch (Exception e) {
				log.error("Error getting info from serial port: {}", e);
			}
		}
	}

}