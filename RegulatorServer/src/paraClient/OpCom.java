package paraClient;
import javax.swing.*;
import javax.swing.event.*;

import communication.*;

import java.awt.*;
import java.awt.event.*;
import se.lth.control.*;
import se.lth.control.plot.*;

/** Class that ServerClientcreates and maintains a GUI for the DC servo process. 
	 Uses two internal threads to update plotters */

public class OpCom {   
	
	private int velRef;
	private int posRef;
	private boolean goingLeft;
	private int mode;
	private boolean hasChanged;

	private int priority;
	private PlotterPanel measurementPlotter; // has internal thread
	private PlotterPanel controlPlotter; // has internal thread
	
	// Declaration of main frame.
	private JFrame frame;

	// Declaration of panels.
	private BoxPanel plotterPanel;

	private double range = 10.0; // Range of time axis
	private int divTicks = 5;    // Number of ticks on time axis
	private int divGrid = 5;     // Number of grids on time axis

	private JRadioButton offModeButton;
	private JRadioButton velModeButton;
	private JRadioButton posModeButton;
	private JButton stopButton;

	//addat av Gustav, lokala variabler
	private JPanel buttonPanel;
	private JPanel sliderPanel;
	private JSlider velSlider;
	private JSlider posSlider;
	private JLabel velocityLabel;
	private JLabel positionLabel;
	private JLabel modeLabel;

	private JLabel directionLabel;
	private JRadioButton leftButton;
	private JRadioButton rightButton;
	private JPanel directionPanel;

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


	/** Creates the GUI. Called from Main. */
	public void initializeGUI() {
		// Create main frame.
		frame = new JFrame("DC Servo GUI");

		// Create a panel for the two plotters.
		plotterPanel = new BoxPanel(BoxPanel.VERTICAL);
		// Create plot components and axes, add to plotterPanel.

		measurementPlotter.setYAxis(30, -15, 4, 4);
		measurementPlotter.setXAxis(range, divTicks, divGrid);
		measurementPlotter.setTitle("Position and velocity (V)");
		plotterPanel.add(measurementPlotter);
		plotterPanel.addFixed(10);


		controlPlotter.setYAxis(30, -15, 4, 4);

		controlPlotter.setXAxis(range, divTicks, divGrid);
		controlPlotter.setTitle("Control (V)");
		plotterPanel.add(controlPlotter);

		measurementPlotter.setPreferredSize(new Dimension(600,280));
		controlPlotter.setPreferredSize(new Dimension(600,280));
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



		//----------------- regulator mode buttons------------------------

		modeLabel = new JLabel("Regulator mode:   ");
		modeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

		buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout());
		//buttonPanel.setBorder(BorderFactory.createEtchedBorder());

		offModeButton = new JRadioButton("OFF");
		velModeButton = new JRadioButton("VEL");
		posModeButton = new JRadioButton("POS");
		stopButton = new JButton("STOP");

		ButtonGroup group = new ButtonGroup();
		group.add(offModeButton);
		group.add(velModeButton);
		group.add(posModeButton);

		// Button action listeners.
		offModeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mode = 0;
				hasChanged = true;
			}
		});
		velModeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mode = 1;
				hasChanged = true;
			}
		});
		posModeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mode = 2;
				hasChanged = true;
			}
		});
		stopButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				measurementPlotter.stopThread();
				controlPlotter.stopThread();
				System.exit(0);
			}
		});

		// Add buttons to button panel.
		buttonPanel.add(modeLabel);
		buttonPanel.add(offModeButton, BorderLayout.NORTH);
		buttonPanel.add(velModeButton, BorderLayout.CENTER);
		buttonPanel.add(posModeButton, BorderLayout.SOUTH);


		plotterPanel.add(buttonPanel);
		//plotterPanel.add(stopButton);	

		// Select initial mode.
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

		//-------------optional change of direction-----------
		directionPanel = new JPanel();
		directionPanel.setLayout(new FlowLayout());
		directionLabel = new JLabel("Velocity direction:   ");
		leftButton = new JRadioButton("Left");
		rightButton = new JRadioButton("Right");

		ButtonGroup group2 = new ButtonGroup();
		group2.add(leftButton);
		group2.add(rightButton);

		directionPanel.add(directionLabel);
		directionPanel.add(leftButton);
		directionPanel.add(rightButton);

		plotterPanel.add(directionPanel);

		// Button action listeners.
		leftButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				goingLeft = true;
				hasChanged = true;
				//System.out.println("left");
			}
		});

		rightButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				goingLeft = false;
				hasChanged = true;
				//System.out.println("right");
			}
		});
		
		//set initial setting
		if (goingLeft) {
			leftButton.setSelected(true);
		}
		else {
			rightButton.setSelected(true);
		}


		//----------------------- velocity slider --------------------------
		//initialize components
		sliderPanel = new JPanel();
		sliderPanel.setBorder(BorderFactory.createEtchedBorder());
		velSlider = new JSlider(0,100,0);
		velocityLabel = new JLabel("Velocity");

		//set component parameters
		sliderPanel.setLayout(new BoxLayout(sliderPanel,BoxLayout.PAGE_AXIS));
		velSlider.setPaintTicks(true);
		velSlider.setPaintLabels(true);
		velSlider.setMajorTickSpacing(10);
		velSlider.setMinorTickSpacing(1);
		velSlider.setPreferredSize(new Dimension(600,60));
		velocityLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		//fungerar
		velSlider.addChangeListener(new ChangeListener() {
			public void stateChanged (ChangeEvent e) {
				JSlider source = (JSlider)e.getSource();
				if (!source.getValueIsAdjusting()) {
					int vel = (int)source.getValue();
					velRef = vel;
					//System.out.println(velRef);
					hasChanged = true;
				}
			}
		});

		//add everything together
		sliderPanel.add(velocityLabel);
		sliderPanel.add(velSlider);
		plotterPanel.add(sliderPanel);


		//---------------------position slider -------------------------------
		//initialize components
		positionLabel = new JLabel("Position");
		posSlider = new JSlider(0,100,0);

		//set component parameters
		posSlider.setPaintTicks(true);
		posSlider.setPaintLabels(true);
		posSlider.setMajorTickSpacing(10);
		posSlider.setMinorTickSpacing(1);
		posSlider.setPreferredSize(new Dimension(600,60));
		positionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

		//fungerar
		posSlider.addChangeListener(new ChangeListener() {
			public void stateChanged (ChangeEvent e) {
				JSlider source = (JSlider)e.getSource();
				if (!source.getValueIsAdjusting()) {
					int pos = (int)source.getValue();
					posRef = pos;
					//System.out.println(posRef);
					hasChanged = true;
				}
			}
		});
		//add everyting
		sliderPanel.add(positionLabel);
		sliderPanel.add(posSlider);



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
	
	public RegulParameters getRegulParameters () {
		RegulParameters r = new RegulParameters();
		r.setParameters(goingLeft, mode, velRef, posRef);
		return r;
	}
	
	public boolean hasChanged () {
		try {
			Thread.sleep(100);
		}
		catch (InterruptedException e) {
			
		}
		//System.out.println("inneIhasChanged");
		return hasChanged;
	}
	public void changesPerformed () {
		hasChanged = false;
	}
}
