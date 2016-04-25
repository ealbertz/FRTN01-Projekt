package regulatorServer;

public class Main {
	private static final int IOPrio =10;
	//private static final int CommandLineInputPrio=7;
	private static final int GUIInputPrio=7;
	private static final int DisplayPrio=6;

	public static void main(String[] args)  {

		Controller controller = new Controller();
		
		new IOServer(controller, IOPrio).start();
		new ParametersServer(controller,GUIInputPrio).start();
		new DisplayDataServer(controller, DisplayPrio).start();
		
		
	}

}
