package enhanced;

import java.awt.Color;
import java.awt.GridLayout;
import java.util.Arrays;
import java.util.List;

import javax.swing.JFrame;

import org.math.plot.Plot2DPanel;

public class Visualizer 
{

	public Plot2DPanel getPlot(List<Double> timestamps,List<Double> metricList,String title,String XAxisLabel,String YAxisLabel)
	{
		boolean hack = false;
		Plot2DPanel panel = new Plot2DPanel();
		if(timestamps.isEmpty())
		{
			return panel;
		}
		int size = metricList.size();
		if(size == 2)
		{
			hack = true;
			size = size + 1;
		}
		double [] x = new double[size];
		double [] y = new double[size];
		if(hack)
		{
			size -=1;
		}
		for(int i = 0; i< size ; i++)
		{
			x[i] = timestamps.get(i);
			//x[i] = i+1;
			y[i] = metricList.get(i);
		}
		if(hack)
		{
			x[size]  = x[size-1];
			y[size] = y[size-1];
		}
		//System.out.println(Arrays.toString(x));
		//System.out.println(Arrays.toString(y));
		panel.addLinePlot(title, x, y);
		panel.addScatterPlot(title,x,y);
		panel.setAxisLabel (0, XAxisLabel);
		panel.setAxisLabel (1, YAxisLabel);
		panel.includeInBounds(0.0,0.0);
		return panel;
	}
	
	public Plot2DPanel addPlot(Plot2DPanel panel,Color color,List<Double> timestamps,List<Double> metricList,String title)
	{
		boolean hack = false;
		int size = metricList.size();
		if(size == 2)
		{
			hack = true;
			size = size + 1;
		}
		double [] x = new double[size];
		double [] y = new double[size];
		if(hack)
		{
			size -=1;
		}
		for(int i = 0; i< size ; i++)
		{
			x[i] = timestamps.get(i);
			//x[i] = i+1;
			y[i] = metricList.get(i);
		}
		if(hack)
		{
			x[size]  = x[size-1];
			y[size] = y[size-1];
		}
		panel.addLinePlot(title, color,x, y);
		panel.addScatterPlot(title,color,x,y);
		panel.includeInBounds(0.0,0.0);
		return panel;
	}
	public Plot2DPanel getLinePlot(List<Double> timestamps,List<Double> metricList,String title,String XAxisLabel,String YAxisLabel)
	{
		boolean hack = false;
		Plot2DPanel panel = new Plot2DPanel();
		if(timestamps.isEmpty())
		{
			return panel;
		}
		int size = metricList.size();
		if(size == 2)
		{
			hack = true;
			size = size + 1;
		}
		double [] x = new double[size];
		double [] y = new double[size];
		if(hack)
		{
			size -=1;
		}
		for(int i = 0; i< size ; i++)
		{
			x[i] = timestamps.get(i);
			//x[i] = i+1;
			y[i] = metricList.get(i);
		}
		if(hack)
		{
			x[size]  = x[size-1];
			y[size] = y[size-1];
		}
		//System.out.println(Arrays.toString(x));
		//System.out.println(Arrays.toString(y));
		panel.addLinePlot(title, x, y);
		panel.setAxisLabel (0, XAxisLabel);
		panel.setAxisLabel (1, YAxisLabel);
		panel.includeInBounds(0.0,0.0);
		return panel;
	}
	
	public Plot2DPanel addLinePlot(Plot2DPanel panel,Color color,List<Double> timestamps,List<Double> metricList,String title)
	{
		boolean hack = false;
		int size = metricList.size();
		if(size == 2)
		{
			hack = true;
			size = size + 1;
		}
		double [] x = new double[size];
		double [] y = new double[size];
		if(hack)
		{
			size -=1;
		}
		for(int i = 0; i< size ; i++)
		{
			x[i] = timestamps.get(i);
			//x[i] = i+1;
			y[i] = metricList.get(i);
		}
		if(hack)
		{
			x[size]  = x[size-1];
			y[size] = y[size-1];
		}
		panel.addLinePlot(title, color,x, y);
		panel.includeInBounds(0.0,0.0);
		return panel;
	}
}
