package combinedClient;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import javax.swing.SwingUtilities;

import communication.DisplayData;
import communication.PlotData;
import paraClient.OpCom;
//import regulator.PlotData;
import se.lth.control.DoublePoint;

public class DisplayDataClient extends Thread {

	OpCom opcom;
	ObjectOutputStream outToServer;
	ObjectInputStream inFromServer;
	Socket clientSocket;
	DisplayData d;
	PlotData pd;
	DoublePoint dp;

	public void run () {
		try {
			clientSocket = new Socket("94.246.117.31", 1500);
			outToServer = new ObjectOutputStream(clientSocket.getOutputStream());
			inFromServer = new ObjectInputStream(clientSocket.getInputStream());
			try {
				while (true) {
					outToServer.writeObject(new DisplayData());
					d = (DisplayData) inFromServer.readObject();
					
					pd = new PlotData(d.getRealTime(),d.getPos(),d.getVel());
					opcom.putMeasurementDataPoint(pd);
					dp = new DoublePoint(d.getRealTime(),d.getCtrl());
					opcom.putControlDataPoint(dp);
					
					//System.out.println("Server Ms response: " + d.getRealTime());
//					try {
//						Thread.sleep(10);
//					}
//					catch (InterruptedException e) {
//
//					}
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
	public void setOpcom(OpCom op) {
		opcom=op;
	}
}
