package regulatorServer;

import java.io.*;
import java.net.*;

import communication.DisplayData;

public class DisplayDataServer extends Thread {

	private Controller controller;

	public DisplayDataServer(Controller controller, int priority) {
		this.controller = controller;
		setPriority(priority);
	}

	public void run() {
		ServerSocket welcomeSocket;
		Socket connectionSocket;
		ObjectInputStream inFromClient;
		ObjectOutputStream outToClient;

		DisplayData d;

		try {
			welcomeSocket = new ServerSocket(1500);
			connectionSocket = welcomeSocket.accept();
			inFromClient = new ObjectInputStream(connectionSocket.getInputStream());
			outToClient = new ObjectOutputStream(connectionSocket.getOutputStream());

			while (true) {
				try {
					d = (DisplayData) inFromClient.readObject();
					d.setData(controller.getRealTime(), controller.getVel(), controller.getPos(), controller.getCtrl());

					outToClient.writeObject(d);
				}

				catch (ClassNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} catch (IOException e) {
			System.out.println("IOException1");

		}
	}

}
