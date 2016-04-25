package combinedClient;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;


public class RegulParametersClient extends Thread {

	private OpCom opcom;

	public void run () {

		ObjectOutputStream outToServer;
		Socket clientSocket;

		try {
			clientSocket = new Socket("94.246.117.31", 1801);
			outToServer = new ObjectOutputStream(clientSocket.getOutputStream());
			try {
				while (true) {
					if (opcom.hasChanged()) {
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

	public void setOpcom(OpCom op) {
		opcom=op;
	}
}
