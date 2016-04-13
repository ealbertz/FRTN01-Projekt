package testClasses;

import java.io.*;
import java.net.*;
import org.apache.commons.lang3.SerializationUtils;

import communication.DoubleToByteArray;
import communication.ProcessInput;

public class IOtest {
	
	// Ska skicka in hastighets och referens värden. 

	public static void main(String[] args) throws Exception {
		System.in.read();
		
		DatagramSocket clientSocket = new DatagramSocket();
		InetAddress IPAddress = InetAddress.getByName("localhost");
		byte[] sendData = new byte[1024];
		byte[] receiveData = new byte[1024];
		ProcessInput input= new ProcessInput(1,2);
		sendData = input.getBytes();
		DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 9805);
		clientSocket.send(sendPacket);
		System.out.println("Sent: vel=1, pos=2");
		DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
		clientSocket.receive(receivePacket);
		Double reply = DoubleToByteArray.toDouble(receivePacket.getData());
		System.out.println("Reply signal: " + reply);
		clientSocket.close();
	}
}
