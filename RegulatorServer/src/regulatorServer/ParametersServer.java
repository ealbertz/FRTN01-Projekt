package regulatorServer;
import java.io.*;
import java.net.*;

import communication.RegulParameters;

public class ParametersServer extends Thread{
	
	private Controller controller;
	
	public ParametersServer(Controller controller, int priority){
		this.controller=controller;
		setPriority(priority);
	}

	public void run()  {
		ServerSocket welcomeSocket;
		Socket connectionSocket;
		ObjectInputStream inFromClient;
		ObjectOutputStream outToClient;

		RegulParameters p;
		try {
			welcomeSocket = new ServerSocket(1800);
			connectionSocket = welcomeSocket.accept();
			inFromClient = new ObjectInputStream(connectionSocket.getInputStream());
			outToClient = new ObjectOutputStream(connectionSocket.getOutputStream());
			try {
				while(true) {
					p = (RegulParameters) inFromClient.readObject();
					controller.setRegulParameters(p);
					//byts ut mot controller.setRegulParameters
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
