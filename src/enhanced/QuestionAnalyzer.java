package enhanced;

import java.awt.Color;
import java.awt.GridLayout;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.math.plot.Plot2DPanel;

public class QuestionAnalyzer 
{
	private File repo;
	private Visualizer visualizer;
	private final int START_TIME = 0;
	private final int END_TIME = 1000;
	private final int GRANULARITY = 10;
	private Random rand;
	public QuestionAnalyzer(File repo)
	{
		this.repo = repo;
		this.visualizer = new Visualizer();
		this.rand = new Random();
	}
	
	public void display() throws Exception
	{
	
		Map<String,Map<Double,Double>> questionMap = calculateQuestionMap();
		if(questionMap == null)
		{
			JOptionPane.showMessageDialog(null, "No traces exist. Please check if you have the right repository");
			return;
		}
		JFrame frame = new JFrame("Velocity Curves for Questions");
		frame.setVisible(true);
		frame.setSize(1000, 1000);
		Plot2DPanel plot = visualizer.getLinePlot(new ArrayList<Double>(),new ArrayList<Double>(), 
				"Velocity Curve", "Time", "CombinedMetric");
		
		for(String q : questionMap.keySet())
		{
			Map<Double, Double> timeMetricMap = questionMap.get(q);
			List <Double> x = new ArrayList<Double>();
			List <Double> y = new ArrayList<Double>();
			for(int i=START_TIME;i<=END_TIME;i+=GRANULARITY)
			{	
				List<Double> temp = new ArrayList<Double>();
				
				for(Double key: timeMetricMap.keySet())
				{
					if(key < i)
					{
						temp.add(timeMetricMap.get(key));
					}
				}
				double score = 0;
				for(Double d : temp)
				{
					score+=d;
				}
				if(!temp.isEmpty())
				{
					score = score / temp.size();
				}
				y.add(score);
				x.add((double)i);
			}
			//System.out.println("Adding for "+q+"x:"+x+"y:"+y);
			plot = visualizer.addLinePlot(plot, getRandomColor(), x, y, q);
		}
		frame.add(plot);
	}
	
	public Map<String,Map<Double,Double>>  calculateQuestionMap() throws Exception
	{
		Map<String,Map<Double,Double>> questionMap = new HashMap<String, Map<Double,Double>>();
		List<File> filesToProcess = getUserFiles();
		if(filesToProcess.size() == 0)
		{
			return null;
		}
		for(File f: filesToProcess)
		{
			AnalysisEngine currentEngine = new AnalysisEngine(f.getPath()); 
			List<Double> tsList = currentEngine.getTSList();
			if(tsList.size() == 0)
			{
				continue;
			}
			AggregatorAnalysisEngine aggregatorAnalysisEngine = new AggregatorAnalysisEngine(f);
			List<Double> combinedMetric = aggregatorAnalysisEngine.getCombinedMetric(currentEngine);
			String question_no = f.getName().split("_")[1].trim();
			if(!questionMap.containsKey(question_no))
			{
				HashMap<Double,Double> timeMetricMap = new HashMap<Double,Double>();
				questionMap.put(question_no, timeMetricMap);
			}
			Map<Double, Double> currentTimeMetricMap = questionMap.get(question_no);
			for(int i=0;i<combinedMetric.size();i++)
			{
				double ts = tsList.get(i);
				double cm = combinedMetric.get(i);
				currentTimeMetricMap.put(ts, cm);
			}
			questionMap.put(question_no, currentTimeMetricMap);
		}
		return questionMap;
	}
	private List<File> getUserFiles()
	{
		List<File> filesToProcess = new ArrayList<File>();
		File[] listOfFiles = repo.listFiles();
		for(File f : listOfFiles)
		{
			if(f.isFile())
			{
				filesToProcess.add(f);
			}
		}
		return filesToProcess;
	}
	private Color getRandomColor()
	{
		float r = rand.nextFloat();
		float g = rand.nextFloat();
		float b = rand.nextFloat();
		Color randomColor = new Color(r, g, b);
		return randomColor;
	}
}
