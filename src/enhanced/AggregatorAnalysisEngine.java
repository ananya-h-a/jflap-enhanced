package enhanced;

import java.awt.Color;
import java.awt.GridLayout;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JFrame;

import org.math.plot.Plot2DPanel;

import automata.fsa.FiniteStateAutomaton;

import com.sun.xml.internal.ws.api.pipe.Engine;

public class AggregatorAnalysisEngine 
{
	private String repoPath;
	private String tracePath;
	private double[] weights = {0.33,0.33,0.34 };
	private String qno;
	private List<FiniteStateAutomaton> attempts;
	private File tracefile;
	public AggregatorAnalysisEngine(File tracefile) 
	{
		this.tracefile = tracefile;
		repoPath = tracefile.getParentFile().getPath();
		tracePath = tracefile.getPath();
		XMLReader reader = new XMLReader(tracePath);
		qno = tracefile.getName().split("_")[1];
		attempts = reader.getAutomatonsFromXML();
		
	}
	
	public void plotMetrics() throws Exception
	{
		if(attempts.size() == 0)
		{
			// What should we do ??
			return;
		}
		else
		{
			AnalysisEngine currentEngine = new AnalysisEngine(tracefile.getPath()); 
			Map<String,List<List<Double>>> timesAndScoresMap = getTimesAndScores();
			Map<String,Double> points = computeAggregates(timesAndScoresMap);
			List<Double> combinedMetric = getCombinedMetric(currentEngine);
			Visualizer visualizer = new Visualizer();
			JFrame frame = new JFrame("Velocity Curve");
			frame.setLayout(new GridLayout(2, 1));
			double x = points.get("AverageTime");
			double y = points.get("AverageMetric");
			Plot2DPanel plot1 = visualizer.getPlot(currentEngine.getTSList(),combinedMetric, 
					"Velocity Curve", "Time", "CombinedMetric");
			plot1.addLinePlot("", Color.GREEN, new double [] {x,0,0} , new double [] {0,y,y}); //hack for 2 pt problem
			plot1.addScatterPlot("",  Color.CYAN, new double [] {x,0,0} , new double [] {0,y,y});
			frame.add(plot1);
			
			
			
			frame.setVisible(true);
			frame.setSize(1000, 1000);
			
		}
	}
	private Map<String,Double> computeAggregates(Map<String,List<List<Double>>> timesAndScoresMap) throws Exception
	{
		List<List<Double>> scores;
		List<Double> times = new ArrayList<Double>();
		scores = timesAndScoresMap.get("Scores");
		List<List<Double>> superTimes = timesAndScoresMap.get("Times");
		for(List<Double> t : superTimes)
		{
			double time = 0.0;
			if(t.size() > 0)
			{
				time = t.get(t.size()-1);
			}
			times.add(time);
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
		Map<String,Double> averageTimeAndScoresMap = new HashMap<String,Double>();
		
		averageTimeAndScoresMap.put("AverageMetric",totalMetric/count);
		averageTimeAndScoresMap.put("AverageTime",totalTime/times.size());
		return averageTimeAndScoresMap;
	}
	
	private List<Double> computeAverageOfAggregates(List<Double> ts) throws Exception
	{
		List<List<Double>> scores = new ArrayList<List<Double>>();
		List <Double> times = new ArrayList<Double>();
		File repodir = new File(repoPath);
		File[] listOfFiles = repodir.listFiles();
		for(File f : listOfFiles)
		{
			if(f.isFile() && f.getName().split("_")[1].equals(qno))
			{
				System.out.println(f.getName());
				AnalysisEngine engine = new AnalysisEngine(f.getPath());
			}
		}
		return null;
	}
	private List<Double> getCombinedMetric(AnalysisEngine engine)
	{
		List<Double> metric1 = engine.generateCPMetric();
		List<Double> metric2 = engine.generateNoDistinctStatesMetric();
		List<Double> metric3 = engine.generateVectorDistance();
		List<Double> weightedMetric = new ArrayList<Double>();
		for(int i=0;i< metric1.size();i++)
		{
			weightedMetric.add(weights[0]*metric1.get(i) + weights[1]*metric2.get(i)+ weights[2]*metric3.get(i));
		}
		return weightedMetric;
	}
	
	private Map<String,List<List<Double>>> getTimesAndScores() throws Exception
	{
		List<List<Double>> scores = new ArrayList<List<Double>>();
		List<List<Double>> times = new ArrayList<List<Double>>();
		File repodir = new File(repoPath);
		File[] listOfFiles = repodir.listFiles();
		for(File f : listOfFiles)
		{
			if(f.isFile() && f.getName().split("_")[1].equals(qno))
			{
				System.out.println(f.getName());
				AnalysisEngine engine = new AnalysisEngine(f.getPath());
				
				List<Double> time ;		
				if(engine.getTSList().size() == 0)
				{
					time = new ArrayList<Double>();
				}
				else
				{
					time = engine.getTSList();
				}
				scores.add(getCombinedMetric(engine));
				times.add(time);
				
			}
		}
		Map<String,List<List<Double>>> timesAndScoresMap = new HashMap<String,List<List<Double>>>();
		timesAndScoresMap.put("Times", times);
		timesAndScoresMap.put("Scores", scores);
		return timesAndScoresMap;
	}
}