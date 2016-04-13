package regulatorServer;

import java.io.*;
import java.net.*;
import org.apache.commons.lang3.SerializationUtils;

import communication.DoubleToByteArray;
import communication.ProcessInput;

public class Main {

	public static void main(String[] args) throws Exception {


		DatagramSocket serverSocket = new DatagramSocket(9805);
		byte[] receiveData = new byte[1024];
		byte[] sendData = new byte[1024];
		
		Controller controller = new Controller();
		
		while (true) {
			DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
			serverSocket.receive(receivePacket);
			ProcessInput receivedInput = (ProcessInput)SerializationUtils.deserialize(receivePacket.getData());
			System.out.println("RECEIVED POS: " + receivedInput.getPos()+"\n RECEIVED VEL: "+receivedInput.getVel());
			InetAddress IPAddress = receivePacket.getAddress();
			int port = receivePacket.getPort();
			sendData = DoubleToByteArray.toByteArray(controller.calculateOutput(receivedInput));
			System.out.println("Sending: "+ DoubleToByteArray.toDouble(sendData));
			DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
			serverSocket.send(sendPacket);
		}
	}

}