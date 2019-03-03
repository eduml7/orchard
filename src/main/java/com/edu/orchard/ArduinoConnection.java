package com.edu.orchard;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Enumeration;

import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;

public class ArduinoConnection implements SerialPortEventListener {

	// private OutputStream Output = null;
	private BufferedReader input = null;
	SerialPort serialPort;
	private final String PORT_NAME = "/dev/ttyACM0";
	private static final int TIME_OUT = 2000;
	private static final int DATA_RATE = 9600;

	public void initialize() {

		CommPortIdentifier portId = null;
		Enumeration<?> portEnum = CommPortIdentifier.getPortIdentifiers();

		while (portEnum.hasMoreElements()) {
			CommPortIdentifier currPortId = (CommPortIdentifier) portEnum.nextElement();

			if (PORT_NAME.equals(currPortId.getName())) {
				portId = currPortId;
				break;
			}
		}

		if (portId == null) {

			System.exit(1);
			return;
		}

		try {

			serialPort = (SerialPort) portId.open(this.getClass().getName(), TIME_OUT);

			serialPort.setSerialPortParams(DATA_RATE, SerialPort.DATABITS_8, SerialPort.STOPBITS_1,
					SerialPort.PARITY_NONE);

			// Output = serialPort.getOutputStream(); // Se prepara a Output
			// //para
			// enviar datos
			//Input = serialPort.getInputStream(); // Se prepara input para
													// //recibir datos
			input = new BufferedReader(new InputStreamReader(serialPort.getInputStream()));
			serialPort.addEventListener(this); // Se agrega un Event //Listener
			serialPort.notifyOnDataAvailable(true); // Se indica que se
													// //notifique al usuario
													// cuando sea que halla
													// datos disponibles en //el
													// puerto serie
		} catch (Exception e) {

			System.exit(1);
		}

	}

	@Override
	public synchronized void serialEvent(SerialPortEvent oEvent) {
		if (oEvent.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
			try {
				String inputLine = input.readLine();
				System.out.println(inputLine);
			} catch (Exception e) {
				System.err.println(e.toString());
			}
		}
	}

}