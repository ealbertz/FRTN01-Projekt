package paraClient;

import java.io.*;
import java.net.*;

import javax.swing.SwingUtilities;

import communication.RegulParameters;

public class ParameterCommunication {


	public static void main (String[]args) {

		final int plotterPriority = 5;
		final OpCom opcom = new OpCom(plotterPriority);

		Runnable initializeGUI = new Runnable(){
			public void run(){
				opcom.initializeGUI();
				opcom.start();
			}
		};
		try{
			SwingUtilities.invokeAndWait(initializeGUI);
		}catch(Exception e){
			return;
		}

		ObjectOutputStream outToServer;
		ObjectInputStream inFromServer;
		Socket clientSocket;
		RegulParameters p;
		boolean paramsToSend = false;

		try {
			clientSocket = new Socket("localhost", 6820);
			outToServer = new ObjectOutputStream(clientSocket.getOutputStream());
			inFromServer = new ObjectInputStream(clientSocket.getInputStream());
			try {
				while (true) {
					if (opcom.hasChanged()) {
						//System.out.println("inne");
						outToServer.writeObject(opcom.getRegulParameters());
						opcom.changesPerformed();
					}
				}
			}
			catch (IOException e) {
				System.out.println("IOException2");
			}

		}
		catch (IOException e) {
			System.out.println("IOException1");
			System.exit(1);
		}
	}
}
