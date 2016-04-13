package regulatorServer;

import communication.ProcessInput;

public class Controller {

	public static final int OFF = 0, VEL = 1, POS = 2;

	private int mode = VEL;
	private int ref=0;

	VelRegulator velRegul = new VelRegulator();
	PosRegulator posRegul = new PosRegulator();

	public Double calculateOutput(ProcessInput input) {
		
		switch(mode) {

		case OFF:
			return new Double(0);
		case VEL: 
			return new Double(velRegul.calculateOutput(ref, input.getVel()));
		case POS: 
			return new Double(posRegul.calculateOutput(ref, input.getPos()));
			
		}
		return new Double(0);
		

	}

}