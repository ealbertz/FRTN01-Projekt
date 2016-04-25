package UDPForwarding;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class ForwardingApplication extends Thread {
	

	public DatagramPacket forwardProcessOutput(DatagramPacket sendPacket) throws Exception {
		DatagramSocket clientSocket = new DatagramSocket();
		InetAddress IPCloud = InetAddress.getByName("94.246.117.31");
		sendPacket.setAddress(IPCloud);
		sendPacket.setPort(9811);
		System.out.println("Sent to Cloud");
		clientSocket.send(sendPacket);
		byte[] receiveData = new byte[1024];
		DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
		clientSocket.receive(receivePacket);
		System.out.println("Received from Cloud");
		clientSocket.close();
		return receivePacket;
	}
	
	public void run(){
		DatagramSocket serverSocket;
		byte[] dataFromProcess = new byte[1024];			
			
		try{
			serverSocket= new DatagramSocket(9812);
			while(true){
				DatagramPacket receivedFromIO = new DatagramPacket(dataFromProcess, dataFromProcess.length);
				serverSocket.receive(receivedFromIO);
				System.out.println("Packet received: " + receivedFromIO.getLength());
				InetAddress IPStationary = receivedFromIO.getAddress();
				int port = receivedFromIO.getPort();
				DatagramPacket sendToIO = forwardProcessOutput(receivedFromIO);
				sendToIO.setAddress(IPStationary);
				sendToIO.setPort(port);
				serverSocket.send(sendToIO);
				System.out.println("Packet sent: " + sendToIO.toString());
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	
		
		
	}
	public static void main(String[] args){
		new ForwardingApplication().start();
	}
}
