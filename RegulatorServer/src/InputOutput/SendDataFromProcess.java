package InputOutput;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import communication.DoubleToByteArray;
import communication.ProcessInput;
import se.lth.control.realtime.AnalogIn;
import se.lth.control.realtime.AnalogOut;
import se.lth.control.realtime.IOChannelException;
import se.lth.control.realtime.Semaphore;

public class SendDataFromProcess extends Thread {

	private boolean WeShouldRun = true;

	private AnalogIn velChan;
	private AnalogIn posChan;
	private AnalogOut ctrlChan;

	private Semaphore mutex;
	private int priority;

	public SendDataFromProcess(int prio) {
		priority = prio;
		mutex = new Semaphore(1);
		try {
			velChan = new AnalogIn(0);
			posChan = new AnalogIn(1);
			ctrlChan = new AnalogOut(0);

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
		setPriority(priority);
		mutex.take();
		while (WeShouldRun) {
			double u = 0;
			synchronized (this) {
				try {
					vel = velChan.get();
					pos = posChan.get();
				} catch (Exception e) {
					System.out.println(e);
				}
				ProcessInput input = new ProcessInput(realTime, vel, pos);
				try {
					u = sendProcessOutput(input);
					// System.out.println("U som returneras från servern: " +
					// u);
					ctrlChan.set(u);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			realTime += ((double) h) / 1000.0;

			t += h;
			duration = t - System.currentTimeMillis();
			if (duration > 0) {
				try {
					sleep(duration);
				} catch (InterruptedException e) {

				}
			} else {
				System.out.println("No sleep");
			}
			//System.out.println(duration);
		}
		
		mutex.give();

	}

	public synchronized void shutDown() {
		WeShouldRun = false;
		mutex.take();
		try {
			ctrlChan.set(0.0);
		} catch (IOChannelException x) {
		}
	}

	// Lägg till medtod sendProcessOnput

	public double sendProcessOutput(ProcessInput input) throws Exception {

		// debugging
		int counter = 0;

		DatagramSocket clientSocket = new DatagramSocket();
		InetAddress IPAddress = InetAddress.getByName("localhost");
		byte[] sendData = new byte[1024];
		byte[] receiveData = new byte[1024];
		sendData = input.getBytes();
		DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 9811);
		clientSocket.send(sendPacket);

		//System.out.println("Vel: " + input.getVel());
		//System.out.println("Pos: " + input.getPos());

		DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
		clientSocket.receive(receivePacket);
		Double reply = DoubleToByteArray.toDouble(receivePacket.getData());
		// System.out.println("Reply signal: " + reply);
		clientSocket.close();

		return reply;
	}

	public static void main(String[] args) throws Exception {
		new SendDataFromProcess(10).start();

	}
}
