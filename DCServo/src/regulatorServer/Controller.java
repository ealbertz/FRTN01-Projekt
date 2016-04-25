package regulatorServer;

import communication.ProcessInput;
import communication.RegulParameters;

public class Controller {

	public static final int OFF = 0, VEL = 1, POS = 2;

	private int mode = OFF;
	private double velRef=0;
	private double posRef=0;
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
	
	public void setRegulParameters(RegulParameters p){
				

		
		if (p.getMode() != mode) {
			setMode(p.getMode());
		}
		velRef = 0.1 * p.getVelRef();
		posRef = 0.1 * p.getPosRef();
		if (p.getDirection()) {
			velRef = Math.abs(velRef);
			posRef = Math.abs(posRef);
		} else {
			velRef = -Math.abs(velRef);
			posRef = -Math.abs(posRef);
		}

		
	}

	public synchronized Double calculateOutput(ProcessInput input) {
		
		
		switch(mode) {

		case OFF:
			u=new Double(0);
			return u;
		case VEL: 
			u=new Double(velRegul.calculateOutput(velRef, input.getVel()));
			
			
			return limit(u,uMin,uMax);
		case POS: 
			u = new Double(posRegul.calculateOutput(posRef, input.getPos()));
			return limit(u,uMin,uMax);
			
		}
		return u;
		

	}
	
	public synchronized void updateState(ProcessInput input){
		this.input=input;
		
		switch(mode) {

		case OFF:
			return;
		case VEL: 
			velRegul.updateState(velRef, input.getVel());
			return;
		case POS: 
			posRegul.updateState(input.getPos());
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
	
	public double getRealTime(){
		return input.getRealTime();
	}
	
	public double getPos(){
		return input.getPos();
	}
	
	public double getVel(){
		return input.getVel();
	}
	
	public double getCtrl(){
		return limit(u,uMin,uMax);
	}

}
