package communication;
public class PIParameters implements Cloneable {
	public double K;
	public double Ti;
	public double Beta;
	public double h;

	
	public Object clone() {
		try {
			return super.clone();
		} catch (CloneNotSupportedException x) {
			return null;
		}
	}
}
