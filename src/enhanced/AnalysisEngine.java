package enhanced;

import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.JFrame;

import org.math.plot.Plot2DPanel;

import automata.State;
import automata.fsa.FSAAlphabetRetriever;
import automata.fsa.FSAToRegularExpressionConverter;
import automata.fsa.FiniteStateAutomaton;
import automata.fsa.Minimizer;
import automata.graph.FSAEqualityChecker;

public class AnalysisEngine 
{
	private List<FiniteStateAutomaton> attempts;
	private List<Double> ts;
	private FiniteStateAutomaton result;
	private DFACrossProduct crossProduct;
	private FSAAlphabetRetriever alphabetRetriever;
	Set<String> alphabet;
	private NewMinimizer minimizer;
	private IsFinal instance;
	public AnalysisEngine(String logFilePath) throws Exception
	{
		XMLReader reader = new XMLReader(logFilePath);
		attempts= reader.getAutomatonsFromXML();
		ts = new ArrayList<Double>();
		List<Long> temp = reader.getTimeStamps();
		for(Long t: temp)
		{
			ts.add((double)(t-reader.getStartTime())/ 1000);
		}
		int questionNumber = reader.getQuestionNumberFromXML();
		result = (FiniteStateAutomaton) OpenRepositoryAutomaton.getAutomaton(questionNumber); //need to take care
		crossProduct = new DFACrossProduct();
		alphabetRetriever = new FSAAlphabetRetriever();
		minimizer = new NewMinimizer();
		alphabet = new HashSet<String>();
		for(String alpha : alphabetRetriever.getAlphabet(result))
		{
			alphabet.add(alpha);
		}
		 instance = new UnionOfDifferenceLanguage();
	}
	
	public AnalysisEngine()
	{
		//stub for testing
	}
	
	public double computeCPMetric(FiniteStateAutomaton automaton)
	{
		int score = 0;
		if(automaton.getFinalStates().length == 0)
		{
			return 0;
		}
		
		score += automaton.getStates().length;
		return score;
	}
	
	public double computeNoDistinctStatesMetric(Set<String>alphabet, FiniteStateAutomaton automaton)
	{
		if(automaton.getFinalStates().length == 0)
		{
			return 0;
		}
		VectorGenerator generator = new VectorGenerator(alphabet);
		Set<List<Integer>> distinctVectors = new HashSet<List<Integer>>();
		for(State state: automaton.getStates())
		{
			distinctVectors.add(generator.generateVector(automaton, state));
		}
		return distinctVectors.size();
	}
	
	public double computeVectorDistance(FiniteStateAutomaton attempt)
	{
		Map<List<Integer>,Integer> vectorDictionary = new HashMap<List<Integer>,Integer>();
		Set<String> combinedAlphabet = getCombineAlphabet(attempt);
		VectorGenerator generator = new VectorGenerator(combinedAlphabet);
		//FiniteStateAutomaton minimizedAttempt = getMinimizedAutomaton(attempt);
		//FiniteStateAutomaton minimizedResult = getMinimizedAutomaton(result);
		FiniteStateAutomaton minimizedAttempt = minimizer.getMinimizedAutomaton(attempt);
		FiniteStateAutomaton minimizedResult = minimizer.getMinimizedAutomaton(result);
		for(State state : minimizedResult.getStates())
		{
			List<Integer> vector = generator.generateVector(minimizedResult, state);
			int count = 0;
			if(vectorDictionary.containsKey(vector))
			{
				count = vectorDictionary.get(vector);
			}
			vectorDictionary.put(vector, count+1);
		}
		for(State state : minimizedAttempt.getStates())
		{
			List<Integer> vector = generator.generateVector(minimizedAttempt, state);
			int count = 0;
			if(vectorDictionary.containsKey(vector))
			{
				count = vectorDictionary.get(vector);
			}
			vectorDictionary.put(vector, count-1);
		}
		int n = vectorDictionary.keySet().size();
		int sum = 0;
		for(Integer xi : vectorDictionary.values())
		{
			sum += xi * xi;
		}
		double norm = Math.sqrt(sum);
		return norm;
		
	}
	public List<Double> generateCPMetric()
	{
		
		List<Double> cpMetricList = new ArrayList<Double>();
		for(int i=0;i < attempts.size();i++)
		{
			FiniteStateAutomaton attempt = attempts.get(i);
			Set <String> combinedAlphabet = getCombineAlphabet(attempt);
			FiniteStateAutomaton cp = crossProduct.getDFACrossProduct(result, attempt, combinedAlphabet,instance);
			cpMetricList.add(computeCPMetric(cp));
		}
		return cpMetricList;
	}
	
	public List<Double> generateNoDistinctStatesMetric()
	{
		List<Double> cpMetricList = new ArrayList<Double>();
		for(int i=0;i < attempts.size();i++)
		{
			FiniteStateAutomaton attempt = attempts.get(i);
			Set<String> combinedAlphabet = getCombineAlphabet(attempt);
			FiniteStateAutomaton cp = crossProduct.getDFACrossProduct(result, attempt, combinedAlphabet,instance);
			cpMetricList.add(computeNoDistinctStatesMetric(combinedAlphabet, cp));
		}
		return cpMetricList;
	}
	
	public List<Double> generateVectorDistance()
	{
		List<Double> vectorDistanceList = new ArrayList<Double>();
		for(int i=0;i< attempts.size();i++)
		{
			FiniteStateAutomaton attempt = attempts.get(i);
			vectorDistanceList.add(computeVectorDistance(attempt));
		}
		return vectorDistanceList;
	}
	public void plotMetrics()
	{
		if(attempts.size() <= 0)
		{
			return;
		}
		Visualizer visualizer = new Visualizer();
		JFrame frame = new JFrame("Analysis Panel");
		frame.setLayout(new GridLayout(2, 2));
		List<Double> cpmetrics = generateCPMetric();
		Plot2DPanel plot1 = visualizer.getPlot(ts,cpmetrics, 
				"Size complexity metric", "Attempts", "Number of states in Cross Product");
		
		cpmetrics = generateNoDistinctStatesMetric();
		Plot2DPanel plot2 = visualizer.getPlot(ts,cpmetrics, 
				"Distinct States complexity metric", "Attempts", "Number of distinct states in Cross Product");
		
		cpmetrics = generateVectorDistance();
		Plot2DPanel plot3 = visualizer.getPlot(ts,cpmetrics, 
				"Vector Distance Metric", "Attempts", "Normalized distance");
		
		frame.add(plot1);
		frame.add(plot2);
		frame.add(plot3);
		frame.setVisible(true);
		frame.setSize(1000, 1000);
	}
	
	private Set<String> getCombineAlphabet(FiniteStateAutomaton attempt)
	{
		Set <String> combinedAlphabet = new HashSet<String>(alphabet);
		for(String alpha : alphabetRetriever.getAlphabet(attempt))
		{
			combinedAlphabet.add(alpha);
		}
		return combinedAlphabet;
	}
	
	/*private FiniteStateAutomaton getMinimizedAutomaton(FiniteStateAutomaton automaton)
	{
		minimizer.initializeMinimizer();
		try
		{
			automaton = (FiniteStateAutomaton)minimizer.getMinimizeableAutomaton(automaton);
		}
		catch(NullPointerException e)
		{
			//hack for bad language
			automaton = (FiniteStateAutomaton) minimizer.getMinimizeableAnalysisAutomaton(automaton);
		}
		
		javax.swing.tree.DefaultTreeModel tree = minimizer
				.getDistinguishableGroupsTree(automaton);
		automaton = minimizer.getMinimumDfa(automaton, tree);
		return automaton;
		
	}*/
	
}
