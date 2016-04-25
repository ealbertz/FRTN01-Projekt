package regulatorServer;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import org.apache.commons.lang3.SerializationUtils;

import communication.DoubleToByteArray;
import communication.ProcessInput;

public class IOServer extends Thread {

	DatagramSocket serverSocket;
	Controller controller;
	byte[] receiveData = new byte[1024];
	byte[] sendData = new byte[1024];

	public IOServer(Controller controller, int priority) {
		this.controller = controller;
		setPriority(priority);
		try {
			serverSocket = new DatagramSocket(9811);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void run() {

		try {

			while (true) {
				synchronized(this){
				DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
				serverSocket.receive(receivePacket);
				ProcessInput receivedInput = (ProcessInput) SerializationUtils.deserialize(receivePacket.getData());	

				InetAddress IPAddress = receivePacket.getAddress();
				int port = receivePacket.getPort();
				sendData = DoubleToByteArray.toByteArray(controller.calculateOutput(receivedInput));
				DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
				serverSocket.send(sendPacket);
				controller.updateState(receivedInput);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
