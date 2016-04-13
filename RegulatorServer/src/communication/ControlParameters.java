package communication;

public class ControlParameters implements Cloneable{
	public double phi11;
	public double phi12;
	public double phi21;
	public double phi22;
	public double gamma1;
	public double gamma2;
	public double l1;
	public double l2;
	public double lr;
	public double k1;
	public double k2;
	public double kv;
	public double h;

	
	public Object clone() {
		try {
			return super.clone();
		} catch (CloneNotSupportedException x) {
			return null;
		}
	}
}
