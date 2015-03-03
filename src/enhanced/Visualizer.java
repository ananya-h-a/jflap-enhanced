package enhanced;

import java.awt.GridLayout;
import java.util.List;

import javax.swing.JFrame;

import org.math.plot.Plot2DPanel;

public class Visualizer {

	public Plot2DPanel getPlot(List<Double> metricList,String title,String XAxisLabel,String YAxisLabel)
	{
		Plot2DPanel panel = new Plot2DPanel();
		int size = metricList.size(); 
		double [] x = new double[size];
		double [] y = new double[size];
		for(int i = 0; i< size ; i++)
		{
			x[i] = i+1;
			y[i] = metricList.get(i);
		}
		panel.addLinePlot(title, x, y);
		panel.addScatterPlot(title,x,y);
		panel.setAxisLabel (0, XAxisLabel);
		panel.setAxisLabel (1, YAxisLabel);
		panel.includeInBounds(0.0,0.0);
		return panel;
	}
	
}
