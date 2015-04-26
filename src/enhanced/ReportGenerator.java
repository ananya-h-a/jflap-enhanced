package enhanced;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.opencsv.CSVWriter;

import automata.Automaton;
import automata.fsa.FiniteStateAutomaton;

public class ReportGenerator
{
	private File repo;
	public ReportGenerator(File repo)
	{
		this.repo = repo;
	}
	
	public void generateReport() throws Exception
	{
		
		Map<String,Map> questionMap = new HashMap<String,Map>(); 
		File[] listOfFiles = repo.listFiles();
		List<AttemptRepresentation> attempts = new ArrayList<AttemptRepresentation>();
		for(File f: listOfFiles)
		{
			String userName = f.getName().split("_")[0].trim();
			String questionNo = f.getName().split("_")[1].trim();
			AnalysisEngine currentEngine = new AnalysisEngine(f.getPath()); 
			XMLReader xmlreader = currentEngine.getXmlReader();
			List<Double> tsList = currentEngine.getTSList();
			if(tsList.size() == 0)
			{
				continue;
			}
			int noOfViewPossibleStrings, noOfTestInputs, noOfAcceptStringClicks, noOfRejectStringClicks;
			List<Double> viewPossibleStringsTsList = xmlreader.getTimeStampsOfElement("viewPossibleStrings");
			List<Double> testInputTsList = xmlreader.getTimeStampsOfElement("testInput");
			List<Double> acceptStringClickTsList = xmlreader.getTimeStampsOfElement("AcceptStringClick");
			List<Double> rejectStringClickTsList = xmlreader.getTimeStampsOfElement("RejectStringClick");
			AggregatorAnalysisEngine aggregatorAnalysisEngine = new AggregatorAnalysisEngine(f);
			List<Double> combinedMetric = aggregatorAnalysisEngine.getCombinedMetric(currentEngine);
			for(int i=0;i<combinedMetric.size();i++)
			{
				double metric = combinedMetric.get(i);
				double timestamp = tsList.get(i);
				double prevTimeStamp = 0;
				if(i!=0)
				{
					prevTimeStamp = tsList.get(i-1);
				}
				noOfViewPossibleStrings = getCount(viewPossibleStringsTsList, prevTimeStamp, timestamp);
				noOfTestInputs = getCount(testInputTsList,prevTimeStamp,timestamp);
				noOfAcceptStringClicks = getCount(acceptStringClickTsList, prevTimeStamp, timestamp);
				noOfRejectStringClicks = getCount(rejectStringClickTsList, prevTimeStamp, timestamp);
				AttemptRepresentation attempt = new AttemptRepresentation(userName, questionNo, timestamp, metric, noOfViewPossibleStrings, noOfTestInputs, noOfAcceptStringClicks, noOfRejectStringClicks);
				attempts.add(attempt);
			}
			
		}
		writeCSV(attempts);
	}
	
	private int  getCount(List<Double> ts, double start,double end)
	{
		int count = 0;
		for(Double d : ts)
		{
			if(d > start && d < end)
			{
				count+=1;
			}
		}
		return count;
	}
	private void writeCSV(List<AttemptRepresentation> attempts) throws IOException
	{
		
		final String FILE_NAME = "Metrics.csv";		
		CSVWriter writer = new CSVWriter(new FileWriter(FILE_NAME));
		String [] headers = "Username#Question#TimeStamp#Metric#ViewPossibleString#TestInput#AcceptString#RejectString".split("#");
		writer.writeNext(headers);
		for(AttemptRepresentation attempt : attempts)
		{
			String data = attempt.username+"#"+attempt.question+"#"+attempt.timestamp+"#"+attempt.metricScore
					+"#"+attempt.noOfViewPossibleStrings+"#"+attempt.noOfTestInputs+"#"+attempt.noOfAcceptStringClicks
					+"#"+attempt.noOfRejectStringClicks;
			writer.writeNext(data.split("#"));
		}
		writer.close();
	}
}
