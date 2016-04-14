package regulatorServer;


public class Main {
	private static final int IOPrio =10;
	private static final int CommandLineInputPrio=7;

	public static void main(String[] args) throws Exception {

	
		
	
		Controller controller = new Controller();
		
		new CommandLineInput(controller,CommandLineInputPrio).start();
		new IOServer(controller, IOPrio).start();
		
		
	}

}
