package InputOutput;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import communication.DoubleToByteArray;
import communication.ProcessInput;
import se.lth.control.realtime.AnalogIn;
import se.lth.control.realtime.IOChannelException;
import se.lth.control.realtime.Semaphore;

public class SendDataFromProcess extends Thread {
	
	private boolean WeShouldRun = true;

	private AnalogIn velChan;
	private AnalogIn posChan;	
	

	// private Semaphore mutex;

	public SendDataFromProcess() {
		// mutex = new Semaphore(1); Vet ej om denna behövs..
		try {
			velChan = new AnalogIn(0);
			posChan = new AnalogIn(1);

		} catch (IOChannelException e) {
			System.out.println("SendDataFromProcess");
			System.out.print("Error:setMode(int newMode) {IOChannelException: ");
			System.out.println(e.getMessage());
		}

	}
	public void run() {
		final long h = 50; 
		long duration;
		long t = System.currentTimeMillis();
		double vel = 0, pos = 0;
		double realTime = 0;
		//mutex.take()
		while(WeShouldRun) {
			try{
				vel = velChan.get();
				pos = posChan.get();
			}catch (Exception e) {
				System.out.println(e);
			} 
			ProcessInput input = new ProcessInput(/*realTime,*/ vel, pos);
			sendProcessOutput(input);
		}
		realTime += ((double) h)/1000.0;

		t += h;
		duration = t - System.currentTimeMillis();
		if (duration > 0) {
			try {
				sleep(duration);
			} catch (InterruptedException e) {
				
			}
		}
		//mutex.give();
	}

	//Lägg till medtod sendProcessOnput
	
	
	public static void main(String[] args) throws Exception {

		DatagramSocket clientSocket = new DatagramSocket();
		InetAddress IPAddress = InetAddress.getByName("localhost");
		byte[] sendData = new byte[1024];
		byte[] receiveData = new byte[1024];
		ProcessInput input = new ProcessInput(1,2);
		sendData = input.getBytes();
		DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 9805);
		clientSocket.send(sendPacket);
		DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
		clientSocket.receive(receivePacket);
		Double reply = DoubleToByteArray.toDouble(receivePacket.getData());
		System.out.println("Reply signal: " + reply);
		clientSocket.close();
	}

}
