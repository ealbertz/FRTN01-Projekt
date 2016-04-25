package combinedClient;


import javax.swing.SwingUtilities;


public class ClientMain {

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
		
		
		DisplayDataClient d = new DisplayDataClient();
		RegulParametersClient p = new RegulParametersClient();
		d.setOpcom(opcom);
		p.setOpcom(opcom);
		d.setPriority(9);
		p.setPriority(8);
		p.start();
		d.start();
	}
	
}
