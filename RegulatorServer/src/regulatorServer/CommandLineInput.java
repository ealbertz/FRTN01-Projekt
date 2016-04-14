package regulatorServer;

import java.util.Scanner;

public class CommandLineInput extends Thread {

	Controller controller;

	public CommandLineInput(Controller controller,int priority) {
		this.controller = controller;
		setPriority(priority);
	}

	public void run() {

		while (true) {
			Scanner scanner = new Scanner(System.in);
			int mode = scanner.nextInt();
			if(mode==9){
				scanner.close();
				System.exit(0);
			}
			controller.setMode(mode);
		}
	}

}
