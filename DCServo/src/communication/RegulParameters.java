package communication;
import java.io.*;
public class RegulParameters implements Serializable {
	private static final long serialVersionUID = 1L;
	private boolean direction;
	private int mode;
	private int velRef;
	private int posRef;
	
	public void setParameters (boolean direction, int mode, int velRef, int posRef) {
		this.direction = direction;
		this.mode = mode;
		this.velRef = velRef;
		this.posRef = posRef;
	}
	
	public boolean getDirection () {
		return direction;
	}
	
	public int getMode () {
		return mode;
	}
	
	public int getVelRef () {
		return velRef;
	}
	
	public int getPosRef () {
		return posRef;
	}
	
}
