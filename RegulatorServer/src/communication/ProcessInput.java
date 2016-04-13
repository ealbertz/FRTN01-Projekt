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

	public ProcessInput(double vel, double pos) {

		this.vel = vel;
		this.pos = pos;

	}

//	public ProcessInput(byte[] inputBytes) {
//		ProcessInput p = (ProcessInput)SerializationUtils.deserialize(inputBytes);
//		vel=p.getVel();
//		pos=p.getPos();
//
//	}

	public double getVel() {
		return vel;
	}

	public double getPos() {
		return pos;
	}

	public byte[] getBytes() {
		return SerializationUtils.serialize(this);

	}

}