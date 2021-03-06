package regulator;

public class ControlParameters implements Cloneable{
	double phi11;
	double phi12;
	double phi21;
	double phi22;
	double gamma1;
	double gamma2;
	double l1;
	double l2;
	double lr;
	double k1;
	double k2;
	double kv;
	double h;

	
	public Object clone() {
		try {
			return super.clone();
		} catch (CloneNotSupportedException x) {
			return null;
		}
	}
}
