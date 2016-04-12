package regulator;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import se.lth.control.*;
import se.lth.control.plot.*;





//class Reader extends Thread {
//
//    private OpCom opcom;
//
//    private boolean doIt = true;
//
//	 AnalogIn velChan, posChan, ctrlChan;
//
//    /** Constructor. Sets initial values of the controller parameters and initial mode. */
//    public Reader(OpCom opcom) {
//
//		  this.opcom = opcom;
//
//    }
//
//    /** Run method. Sends data periodically to Opcom. */
//    public void run() {
//		  final long h = 25; // period (ms)
//		  long duration;
//		  long t = System.currentTimeMillis();
//		  DoublePoint dp;
//		  PlotData pd;
//		  double vel = 0, pos = 0, ctrl = 0;
//		  double realTime = 0;
//
//		  try {
//				velChan = new AnalogIn(0);
//				posChan = new AnalogIn(1);
//				ctrlChan = new AnalogIn(2);
//		  } catch (Exception e) {
//			  System.out.println("Reader.start()");
//				System.out.println(e);
//		  } 
//
//		  setPriority(7);
//
//		  while (doIt) {
//				try {
//					 vel = velChan.get();
//					 pos = posChan.get();
//					 ctrl = ctrlChan.get();
//				} catch (Exception e) {
//					 System.out.println(e);
//				} 
//
//				pd = new PlotData(realTime,pos,vel);
//				opcom.putMeasurementDataPoint(pd);
//	    
//				dp = new DoublePoint(realTime,ctrl);
//				opcom.putControlDataPoint(dp);
//
//				realTime += ((double) h)/1000.0;
//
//				t += h;
//				duration = (int) (t - System.currentTimeMillis());
//				if (duration > 0) {
//					 try {
//						  sleep(duration);
//					 } catch (Exception e) {}
//				}
//		  }
//    }
//
//    /** Stops the thread. */
//    private void stopThread() {
//		  doIt = false;
//    }
//
//    /** Called by Opcom when the Stop button is pressed. */ 
//    public synchronized void shutDown() {
//		  stopThread();
//    } 
//
//}


/** Class that creates and maintains a GUI for the Ball and Beam process. 
	 Uses two internal threads to update plotters */

public class OpCom {   
	
	private Regul regul;
	private int priority;
    private PlotterPanel measurementPlotter; // has internal thread
    private PlotterPanel controlPlotter; // has internal thread
    private int mode;
    // Declaration of main frame.
    private JFrame frame;

    // Declaration of panels.
    private BoxPanel plotterPanel;

    private double range = 10.0; // Range of time axis
    private int divTicks = 5;    // Number of ticks on time axis
    private int divGrid = 5;     // Number of grids on time axis

    //private boolean hChanged = false; 
    
    private JRadioButton offModeButton;
	private JRadioButton velModeButton;
	private JRadioButton posModeButton;
	private JButton stopButton;
	
       
    /** Constructor. Creates the plotter panels. */
    public OpCom(int plotterPriority) {
    	priority = plotterPriority;
		  measurementPlotter = new PlotterPanel(2, priority); // Two channels
		  controlPlotter = new PlotterPanel(1, priority);
    }

    /** Starts the threads. */
    public void start() {
		  measurementPlotter.start();
		  controlPlotter.start();
    }

    /** Stops the threads. */
    public void stopThread() {
		  measurementPlotter.stopThread();
		  controlPlotter.stopThread();
    }
    
    public void setRegul(Regul r){
    	regul =r;
    	
    }

    /** Creates the GUI. Called from Main. */
    public void initializeGUI() {
		  // Create main frame.
		  frame = new JFrame("DC Servo GUI");
	
		  // Create a panel for the two plotters.
		  plotterPanel = new BoxPanel(BoxPanel.VERTICAL);
		  // Create plot components and axes, add to plotterPanel.
		  measurementPlotter.setYAxis(20, -10, 4, 4);
		  measurementPlotter.setXAxis(range, divTicks, divGrid);
		  measurementPlotter.setTitle("Position and velocity (V)");
		  plotterPanel.add(measurementPlotter);
		  plotterPanel.addFixed(10);
		  controlPlotter.setYAxis(20, -10, 4, 4);
		  controlPlotter.setXAxis(range, divTicks, divGrid);
		  controlPlotter.setTitle("Control (V)");
		  plotterPanel.add(controlPlotter);
	
		  frame.add(plotterPanel);
	
		  // WindowListener that exits the system if the main window is closed.
		  frame.addWindowListener(new WindowAdapter() {
					 public void windowClosing(WindowEvent e) {
						  //reader.shutDown();
						  stopThread();
						  System.exit(0);
					 }
				});

		  // Set guiPanel to be content pane of the frame.
		  frame.getContentPane().add(plotterPanel, BorderLayout.CENTER);

		  // Pack the components of the window.
		  frame.pack();

		  // Position the main window at the screen center.
		  Dimension sd = Toolkit.getDefaultToolkit().getScreenSize();
		  Dimension fd = frame.getSize();
		  frame.setLocation((sd.width-fd.width)/2, (sd.height-fd.height)/2);

			// Create panel for the radio buttons.
			JPanel buttonPanel = new JPanel();
			buttonPanel.setLayout(new FlowLayout());
			buttonPanel.setBorder(BorderFactory.createEtchedBorder());
			// Create the buttons.
			offModeButton = new JRadioButton("OFF");
			velModeButton = new JRadioButton("VEL");
			posModeButton = new JRadioButton("POS");
			stopButton = new JButton("STOP");
			
			// Group the radio buttons.
			ButtonGroup regGroup = new ButtonGroup();
			regGroup.add(offModeButton);
			regGroup.add(velModeButton);
			regGroup.add(posModeButton);
			
			// Button action listeners.
			offModeButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					regul.setOFFMode();
				}
			});
			velModeButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					regul.setVELMode();
				}
			});
			posModeButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					regul.setPOSMode();
				}
			});
			stopButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					regul.shutDown();
					measurementPlotter.stopThread();
					controlPlotter.stopThread();
					System.exit(0);
				}
			});

			// Add buttons to button panel.
			buttonPanel.add(offModeButton, BorderLayout.NORTH);
			buttonPanel.add(velModeButton, BorderLayout.CENTER);
			buttonPanel.add(posModeButton, BorderLayout.SOUTH);

	
			plotterPanel.add(buttonPanel);
			//plotterPanel.add(stopButton);	
			
			// Select initial mode.
			mode = regul.getMode();
			switch (mode) {
			case 0:
				offModeButton.setSelected(true);
				break;
			case 1:
				velModeButton.setSelected(true);
				break;
			case 2:
				posModeButton.setSelected(true);
			}
			
//			JPanel velPanel = new JPanel();
//			clockWButton = new JRadioButton("left");
//			counterclockWButton = new JRadioButton("right");
//			velPanel.add(clockWButton);
//			velPanel.add(counterclockWButton);
//			plotterPanel.add(velPanel);
//			
//			ButtonGroup velGroup = new ButtonGroup();
//			velGroup.add(clockWButton);
//			velGroup.add(counterclockWButton);
//			
//			clockWButton.addActionListener(new ActionListener() {
//				public void actionPerformed(ActionEvent e) {
//					regul.changeDirection();
//				}
//			});
//			
//			counterclockWButton.addActionListener(new ActionListener() {
//				public void actionPerformed(ActionEvent e) {
//					regul.changeDirection();
//				}
//			});
			
			 
			frame.setVisible(true);
    }

    /** Called by Reader to put a control signal data point in the buffer. */
    public synchronized void putControlDataPoint(DoublePoint dp) {
		  double x = dp.x;
		  double y = dp.y;
		  controlPlotter.putData(x, y);
    }
    
    /** Called by Reader to put a measurement data point in the buffer. */
    public synchronized void putMeasurementDataPoint(PlotData pd) {
		  double x = pd.x;
		  double ref = pd.ref;
		  double y = pd.y;
		  measurementPlotter.putData(x, ref, y);
    }    
    
}
