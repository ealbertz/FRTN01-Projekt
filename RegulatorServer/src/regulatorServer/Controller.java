package regulatorServer;

import communication.ProcessInput;

public class Controller {

	public static final int OFF = 0, VEL = 1, POS = 2;

	private int mode = OFF;
	private double ref=5;
	private double u=0;
	private final double uMax= 10.0;
	private final double uMin= -10.0;
	private ProcessInput input;

	VelRegulator velRegul = new VelRegulator();
	PosRegulator posRegul = new PosRegulator();
	
	
	public synchronized void setMode(int mode){
		this.mode=mode;
		velRegul.reset();
		posRegul.reset();
	}

	public Double calculateOutput(ProcessInput input) {
		
		
		switch(mode) {

		case OFF:
			u=new Double(0);
			return u;
		case VEL: 
			u=new Double(velRegul.calculateOutput(ref, input.getVel()));
			
			//System.out.println("ref: "+ref +" vel: " + input.getVel()+" u: " + u + "limit u: "+ limit(u,uMin,uMax)  );
			
			return limit(u,uMin,uMax);
		case POS: 
			u = new Double(posRegul.calculateOutput(ref, input.getPos()));
			return limit(u,uMin,uMax);
			
		}
		return u;
		

	}
	
	public void updateState(ProcessInput input){
		this.input=input;
		
		switch(mode) {

		case OFF:
			return;
		case VEL: 
			velRegul.updateState(ref, input.getVel());
			return;
		case POS: 
			posRegul.updateState(u, input.getPos());
			return;
			
		}
	}
	
	private double limit(double v, double min, double max) {
		if (v < min) {
			v = min;
		} else if (v > max) {
			v = max;
		}
		return v;
	}

}
