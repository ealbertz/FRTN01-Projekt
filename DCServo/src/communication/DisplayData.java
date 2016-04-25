package communication;
import java.io.*;
public class DisplayData implements Serializable {

	private static final long serialVersionUID = 2L;
	private double realTime;
	private double vel;
	private double pos;
	private double ctrl;

	public DisplayData () {

	}
	public DisplayData (double time, double vel, double pos, double u) {
		realTime = time;
		this.vel = vel;
		this.pos = pos;
		ctrl = u;
	}

	public void setData (double time, double vel, double pos, double u) {
		realTime = time;
		this.vel = vel;
		this.pos = pos;
		ctrl = u;
	}

	public double getRealTime () {
		return realTime;
	}

	public double getVel () {
		return vel;
	}

	public double getPos () {
		return pos;
	}

	public double getCtrl () {
		return ctrl;
	}
}
