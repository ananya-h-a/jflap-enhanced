package enhanced;

import gui.environment.FrameFactory;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import automata.fsa.FiniteStateAutomaton;

public class UserAnalyzer 
{
	private String username;
	private File repo;
	public UserAnalyzer(String username,File repo)
	{
		this.username = username;
		this.repo = repo;
	}
	
	public void displayPerformance() throws Exception
	{
		Map<String,String> performanceMap = new HashMap<String, String>();
		List<File> filesToProcess = getUserFiles();
		if(filesToProcess.size() == 0)
		{
			JOptionPane.showMessageDialog(null, "No traces exist for the given username");
			return;
		}
		JFrame frame = new JFrame("Analysis");
		frame.setLayout(new GridLayout(0,1));
		frame.setVisible(true);
		frame.setSize(350, 225);
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
			System.out.println(combinedMetric);
			if(combinedMetric.get(combinedMetric.size()-1) != 0)
			{
				frame.add(createButton(f, "Below Average"));
				continue;
			}
			Map<String,List<List<Double>>> timesAndScoresMap = aggregatorAnalysisEngine.getTimesAndScores();
			List<Double> avgOfAggregates = aggregatorAnalysisEngine.computeAverageOfAggregates(timesAndScoresMap,currentEngine.getTSList());
			
			int below_avg_count = 0;
			for(int i=0;i<combinedMetric.size();i++)
			{
				double userScore = combinedMetric.get(i);
				double averageScore = avgOfAggregates.get(i);
				if(userScore < averageScore)
				{
					if(i>0)
					{
						double prevUserScore = combinedMetric.get(i-1);
						double prevAverageScore = avgOfAggregates.get(i-1);
						if(prevAverageScore == averageScore) // saturation
						{
							if(prevUserScore < prevAverageScore)
							{
								//counting on coincidences here
								below_avg_count+=1;
							}
							//we don't count
						}
						else
						{
							below_avg_count += 1;
						}
					}
					else
					{
						below_avg_count +=1;
					}
				}
			}
			if(below_avg_count == combinedMetric.size())
			{
				frame.add(createButton(f, "Above Average"));
			}
			else
			{
				frame.add(createButton(f, "Average"));
			}
		}
	}
	
	private List<File> getUserFiles()
	{
		List<File> filesToProcess = new ArrayList<File>();
		File[] listOfFiles = repo.listFiles();
		for(File f : listOfFiles)
		{
			if(f.isFile() && f.getName().split("_")[0].equalsIgnoreCase(username))
			{
				filesToProcess.add(f);
			}
		}
		return filesToProcess;
	}
	
	private JButton createButton(final File traceFile, final String message)
	{
		String qno = traceFile.getName().split("_")[1].trim();
		JButton button = new JButton(qno+":"+message);
		button.addActionListener(new ActionListener() 
		{
			
			@Override
			public void actionPerformed(ActionEvent arg0) 
			{
				AggregatorAnalysisEngine engine = null;
				try 
				{
					engine = new AggregatorAnalysisEngine(traceFile);
					engine.plotMetrics();
					
				} 
				catch (Exception e) 
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		return button;
	}
}
