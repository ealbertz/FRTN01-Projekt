/**
 * Variant of LTH Control plot to enable plotting of delays.
 * Takes current time, a maximum allowed delay and an actual
 * realtime delay as input through the putDelayDatapoint method.
 * Author: Gustav Halling, mostly copied from prewritten classes
 */

package InputOutput;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.LinkedList;

import se.lth.control.*;
import se.lth.control.plot.*;

public class DelayPlot {
	
	//sliding average vars
	private double cumAvg = 0;
	private final int samplePeriod = 20;
	private LinkedList list;

	private int priority; //priority for internal PlotterPanel
	private PlotterPanel delayPlotter; // has internal thread

	//Declaration of GUI components
	private JFrame frame;
	private BoxPanel plotterPanel;

	private double range = 10.0; // Range of time axis
	private int divTicks = 10; // Number of ticks on time axis
	private int divGrid = 10; // Number of grids on time axis
	
	/** Constructor. */
	public DelayPlot(int plotterPriority) {
		priority = plotterPriority;
		delayPlotter = new PlotterPanel(2, priority);
		//use other if implementing sliding average
		//delayPlotter = new PlotterPanel(3, priority);
		list = new LinkedList<>();
		for (int i=0; i<10; i++) {
			list.add(0);
		}
	}

	/** Starts the thread(s). */
	public void start() {
		delayPlotter.start();
	}

	/** Stops the thread(s). */
	public void stopThread() {
		delayPlotter.stopThread();

	}

	public void initializeGUI() {
		
		frame = new JFrame("Delay Plot");
		plotterPanel = new BoxPanel(BoxPanel.VERTICAL);

		delayPlotter.setYAxis(100, 0, 4, 4);
		delayPlotter.setXAxis(range, divTicks, divGrid);
		delayPlotter.setTitle("Delay [ms]");

		plotterPanel.add(delayPlotter);
		
		//uncomment to set preffered size
		//delayPlotter.setPreferredSize(new Dimension(800, 400));
		
		frame.add(plotterPanel);

		// WindowListener that exits the system if the main window is closed.
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
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
		frame.setLocation((sd.width - fd.width) / 2, (sd.height - fd.height) / 2);

		frame.setVisible(true);
	}

	public synchronized void putDelayDataPoint (double time, double maxDelay, double realtimeDelay) {
		delayPlotter.putData(time, maxDelay, realtimeDelay);
	}
//	public synchronized void putDelayDataPointAvg (double time, double maxDelay, double realtimeDelay) {
//		delayPlotter.putData(time, maxDelay, realtimeDelay, getSlidingAverage(realtimeDelay));
//	}
	
	/** Implement to enable a sliding average of the delay to plot.
	 * requires change of channel number in plot, some methods.*/
	private double getSlidingAverage (double newValue) {
		double oldValue = (double) list.removeFirst();
		cumAvg = cumAvg + newValue - oldValue; 
		double average = cumAvg/samplePeriod;
		list.addLast(newValue);
		return average;
	}

}
