package communication;

public class PlotData implements Cloneable { 
	
    public double ref, y; 
    public double x; // holds the current time 
    
    public PlotData(double xIn, double yrefIn, double yIn) {
		ref = yrefIn;
		y = yIn;
		x = xIn;
	}
    public Object clone() { 
        try { 
	    return super.clone(); 
        } catch (Exception e) {return null;} 
    } 
} 
