package enhanced;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import automata.Automaton;
import automata.fsa.FiniteStateAutomaton;

public class ReportGenerator
{
	private File repo;
	public ReportGenerator(File repo)
	{
		this.repo = repo;
	}
	
	public void generateReport()
	{
		Map<String,Map> questionMap = new HashMap<String,Map>(); 
		File[] listOfFiles = repo.listFiles();
		for(File f: listOfFiles)
		{
			String questionNo = f.getName().split("_")[1].trim();
			if(!questionMap.containsKey(questionNo))
			{
				Map emptyMap = new HashMap<String,Double>();
				emptyMap.put("Total Time", 0.0);
				emptyMap.put("Total Attempts", 0.0);
				emptyMap.put("Correct Attempts", 0.0);
				emptyMap.put("Total Hints", 0.0);
				questionMap.put(questionNo, emptyMap);
			}
			Map<String,Double> currentMetrics = questionMap.get(questionNo);
			XMLReader reader = new XMLReader(f.getPath());
			List<FiniteStateAutomaton> attempts = reader.getAutomatonsFromXML(); 
			if(attempts.size() == 0)
			{
				continue;
			}
			double totalTime = currentMetrics.get("Total Time");
			totalTime+= (reader.getEndTime() - reader.getStartTime())/1000.0;
			currentMetrics.put("Total Time", totalTime);
			double totalAttempts = currentMetrics.get("Total Attempts");
			totalAttempts+= attempts.size();
			currentMetrics.put("Total Attempts", totalAttempts);
			if(reader.getIsCorrect())
			{
				double correctAttempts = currentMetrics.get("Correct Attempts");
				correctAttempts+=1;
				currentMetrics.put("Correct Attempts", correctAttempts);
			}
			double hints = reader.getNoOfViewPossibleStrings()  + reader.getNoOfTestInputs();
			if(hints > 0)
			{

				double totalHints = currentMetrics.get("Total Hints");
				totalHints+=hints;
				currentMetrics.put("Total Hints", totalHints);
			}
		}
		writeCSV(questionMap);
	}
	
	private void writeCSV(Map<String,Map> questionMap)
	{
		final String COMMA_DELIMITER = ",";
		final String NEW_LINE_SEPARATOR = "\n";
	    final String FILE_HEADER = "Question No,Total Time,Total Attempts,Correct Attempts,Total Hints";
	    final String FILE_NAME = "Raw Metrics.csv";
	    FileWriter fileWriter = null;
	    try
	    {
	    	fileWriter = new FileWriter(FILE_NAME);
	    	fileWriter.append(FILE_HEADER.toString());
	    	fileWriter.append(NEW_LINE_SEPARATOR);
	    	for(String questionNo : questionMap.keySet())
	    	{
		    	fileWriter.append(questionNo);
                fileWriter.append(COMMA_DELIMITER);
    			Map<String,Double> currentMetrics = questionMap.get(questionNo);
    			fileWriter.append(currentMetrics.get("Total Time").toString());
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(currentMetrics.get("Total Attempts").toString());
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(currentMetrics.get("Correct Attempts").toString());
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(currentMetrics.get("Total Hints").toString());
                fileWriter.append(NEW_LINE_SEPARATOR);

	    	}
	    	fileWriter.flush();
	    	fileWriter.close();

	    }
	    catch(Exception e)
	    {
	    	
	    }
	  
	}
}
