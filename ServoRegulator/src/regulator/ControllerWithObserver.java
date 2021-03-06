package regulator;

public class ControllerWithObserver {
	private ControlParameters cp;
	private double x1;
	private double x2;
	private double v;
	private double eps;
	private double u;


	public ControllerWithObserver () {
		cp = new ControlParameters();

		cp.phi11 = 0.994;
		cp.phi12 = 0;
		cp.phi21 = 0.2493;
		cp.phi22 = 1;
		cp.gamma1 = 0.1122;
		cp.gamma2 = 0.01403;
		cp.l1 = 3.2898;
		cp.l2 = 1.7831;
		cp.lr = 1.7831;
		cp.k1 = 2.0361;
		cp.k2 = 1.2440;
		cp.kv = 3.2096;
		cp.h = 0.05;

		this.x1 = 0;
		this.x2 = 0;
		this.v = 0;
		this.eps = 0;
		this.u = 0;
	}

	// Calculates the control signal v.
	public synchronized double calculateOutput(double r, double y) {
		u = cp.lr*r-cp.l1*x1-cp.l2*x2-v;
		return u;
	}

	// Updates the controller state.
	public synchronized void updateState(double r, double y) {
		eps=y-x2;
		x1 = cp.phi11*x1+cp.phi12*x2+cp.gamma1*(u+v)+cp.k1*eps;
		x2 = cp.phi21*x1+cp.phi22*x2+cp.gamma2*(u+v)+cp.k2*eps;
		v= v+cp.kv*eps;

	}

	// Returns the sampling interval expressed as a long.
	// Note: Explicit type casting needed
	public synchronized long getHMillis() {
		return (long) (cp.h * 1000.0);
	}

	// For example needed when changing controller mode.
	public synchronized void reset() {
		x1 = 0;
		x2 = 0;
		v = 0;

	}
}
