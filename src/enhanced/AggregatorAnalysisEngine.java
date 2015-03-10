package enhanced;

import java.awt.Color;
import java.awt.GridLayout;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

import org.math.plot.Plot2DPanel;

import automata.fsa.FiniteStateAutomaton;

import com.sun.xml.internal.ws.api.pipe.Engine;

public class AggregatorAnalysisEngine 
{
	private String repoPath;
	private String tracePath;
	private double[] weights = {0.33,0.33,0.34 };
	public AggregatorAnalysisEngine(File tracefile) throws Exception
	{
		repoPath = tracefile.getParentFile().getPath();
		tracePath = tracefile.getPath();
		XMLReader reader = new XMLReader(tracePath);
		String qno = tracefile.getName().split("_")[1];
		List<FiniteStateAutomaton> attempts = reader.getAutomatonsFromXML();
		if(attempts.size() == 0)
		{
			// What should we do ??
		}
		else
		{
			AnalysisEngine currentEngine = new AnalysisEngine(tracefile.getPath()); 
			double [] points = computeAggregates(qno);
			List<Double> combinedMetric = getCombinedMetric(currentEngine);
			Visualizer visualizer = new Visualizer();
			JFrame frame = new JFrame("Velocity Curve");
			Plot2DPanel plot1 = visualizer.getPlot(currentEngine.getTSList(),combinedMetric, 
					"Velocity Curve", "Time", "CombinedMetric");
			plot1.addLinePlot("", Color.GREEN, new double [] {points[1],0,0} , new double [] {0,points[0],points[0]}); //hack for 2 pt problem
			plot1.addScatterPlot("",  Color.CYAN, new double [] {points[1],0,0} , new double [] {0,points[0],points[0]});
			frame.add(plot1);
			frame.setVisible(true);
			frame.setSize(1000, 1000);
			
		}
	}
	private double[] computeAggregates(String qno) throws Exception
	{
		List<List<Double>> scores = new ArrayList<List<Double>>();
		List<Double> times = new ArrayList<Double>();
		File repodir = new File(repoPath);
		File[] listOfFiles = repodir.listFiles();
		for(File f : listOfFiles)
		{
			if(f.isFile() && f.getName().split("_")[1].equals(qno))
			{
				System.out.println(f.getName());
				AnalysisEngine engine = new AnalysisEngine(f.getPath());
				
				double time ;		
				if(engine.getTSList().size() == 0)
				{
					time = 0;
				}
				else
				{
					time = engine.getTSList().get(engine.getTSList().size()-1);
				}
				scores.add(getCombinedMetric(engine));
				times.add(time);
				
			}
			
		}
		int zeroCount = 0;
		double maxTime = 0;
		double maxMetric = 0;
		double totalTime = 0;
		double totalMetric = 0;
		int count = 0;
		for(int i = 0; i< scores.size();i++)
		{
			List<Double> l = scores.get(i);
			if(l.size() == 0)
			{
				count += 1;
				zeroCount += 1;
			}
			else
			{
				double time = times.get(i);
				totalTime+=time;
				if(time > maxTime)
				{
					maxTime = time;
				}
				for(Double d : l)
				{
					count+=1;
					totalMetric+=d;
					if(d > maxMetric)
					{
						maxMetric = d;
					}
				}
			}
		}
		totalMetric += maxMetric * zeroCount;
		totalTime += maxTime * zeroCount;
		return new double[] {totalMetric/count,totalTime/times.size()};
	}
	private List<Double> getCombinedMetric(AnalysisEngine engine)
	{
		List<Double> metric1 = engine.generateCPMetric();
		List<Double> metric2 = engine.generateNoDistinctStatesMetric();
		List<Double> metric3 = engine.generateVectorDistance();
		List<Double> weightedMetric = new ArrayList<Double>();
		for(int i=0;i< metric1.size();i++)
		{
			weightedMetric.add(weights[0]*metric1.get(i) + weights[1]*metric2.get(i)+ weights[2]*metric2.get(i));
		}
		return weightedMetric;
	}
}
