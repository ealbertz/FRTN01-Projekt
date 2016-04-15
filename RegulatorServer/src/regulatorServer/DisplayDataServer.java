package regulatorServer;

import java.io.*;
import java.net.*;

import communication.DisplayData;

public class DisplayDataServer extends Thread {
	
	private Controller controller;
	public DisplayDataServer(Controller controller, int priority){
		this.controller=controller;
		setPriority(priority);
	}
	
	public void run()  {
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
			try {
				while(true) {
					d = (DisplayData) inFromClient.readObject();
					d.setData(controller.getRealTime(),controller.getVel(),controller.getPos(),controller.getCtrl());
					//d.setParameters(someClass.getData());
					outToClient.writeObject(d);
					//ev lite sleep för att dra mindre process
				} 
			}
			catch (ClassNotFoundException e) {

			}
		}
		catch (IOException e) {
			System.out.println("IOException1");
			System.exit(1);
		}
	}
	
}
