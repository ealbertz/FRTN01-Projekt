package display;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import javax.swing.SwingUtilities;

import communication.DisplayData;
import paraClient.OpCom;

public class DisplayDataClient {

	public static void main(String[] args) {

		final int plotterPriority = 5;
		final OpCom opcom = new OpCom(plotterPriority);

		Runnable initializeGUI = new Runnable() {
			public void run() {
				opcom.initializeGUI();
				opcom.start();
			}
		};
		try {
			SwingUtilities.invokeAndWait(initializeGUI);
		} catch (Exception e) {
			return;
		}

		ObjectOutputStream outToServer;
		ObjectInputStream inFromServer;
		Socket clientSocket;
		DisplayData d;
		boolean paramsToSend = false;

		try {
			clientSocket = new Socket("localhost", 1601);
			outToServer = new ObjectOutputStream(clientSocket.getOutputStream());
			inFromServer = new ObjectInputStream(clientSocket.getInputStream());
			try {
				while (true) {
					outToServer.writeObject(new DisplayData());
					d = (DisplayData) inFromServer.readObject();
					System.out.println("Server Vel response: " + d.getVel());
					System.out.println("Server Pos response: " + d.getPos());
					System.out.println("Server Ctrl response: " + d.getCtrl());
					System.out.println("Server Ms response: " + d.getRealTime());
					try {
						Thread.sleep(50);
					}
					catch (InterruptedException e) {

					}
				}
			}
			catch (IOException e) {
				System.out.println("IOException2");
			}
			catch (ClassNotFoundException e) {
				System.out.println("ClassNotFoundException");
			}

		} catch (IOException e) {
			System.out.println("IOException1");
			System.exit(1);
		}
	}
}
