package communication;

import java.io.*;
import org.apache.commons.lang3.SerializationUtils;

public class ProcessInput implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private double vel;
	private double pos;
	private double realTime;

	public ProcessInput(double realTime, double vel, double pos) {
		
		this.realTime=realTime;
		this.vel = vel;
		this.pos = pos;

	}



	public double getVel() {
		return vel;
	}

	public double getPos() {
		return pos;
	}
	
	public double getRealTime(){
		return realTime;
	}

	public byte[] getBytes() {
		return SerializationUtils.serialize(this);

	}

}
