package regulator;
public class PIParameters implements Cloneable {
	double K;
	double Ti;
	double Beta;
	double h;

	
	public Object clone() {
		try {
			return super.clone();
		} catch (CloneNotSupportedException x) {
			return null;
		}
	}
}
