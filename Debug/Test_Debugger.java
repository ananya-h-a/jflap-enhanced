import java.awt.GridLayout;
import java.awt.Point;
import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.JFrame;

import org.math.plot.Plot2DPanel;

import enhanced.AnalysisEngine;
import enhanced.DFACrossProduct;
import enhanced.FinitenessDetector;
import enhanced.OpenRepositoryAutomaton;
import enhanced.VectorGenerator;
import enhanced.XMLReader;
import enhanced.action.OpenFileFromRepositoryAction;
import automata.fsa.FSAAlphabetRetriever;
import automata.fsa.FSATransition;
import automata.fsa.FiniteStateAutomaton;
import automata.State;


public class Test_Debugger 
{
	public static void main(String args[]) throws Exception
	{
		
		/*DFACrossProduct dfa = new DFACrossProduct();
		FiniteStateAutomaton result = new FiniteStateAutomaton();
		FiniteStateAutomaton a = new FiniteStateAutomaton();
		FiniteStateAutomaton b = new FiniteStateAutomaton();*/
		//XMLReader reader = new XMLReader("test.xml");
		//List<FiniteStateAutomaton> automatonList = reader.getAutomatonsFromXML();
		/*for(FiniteStateAutomaton a : automatonList)
		{
			System.out.println(a.toString());
		}*/
		//FiniteStateAutomaton result = (FiniteStateAutomaton) OpenRepositoryAutomaton.getAutomaton(11);
		
		//DFACrossProduct crossProduct = new DFACrossProduct();
		List<String> alphabet = new ArrayList<String>();
		alphabet.add("0");
		alphabet.add("1");
		FSAAlphabetRetriever alphabetRetriever = new FSAAlphabetRetriever();
		//Set<String> alphabet = new HashSet<String>();
		//for(String alpha : alphabetRetriever.getAlphabet(result))
		//{
		//	alphabet.add(alpha);
		//}
		/*for(int i=0;i < automatonList.size();i++)
		{
			FiniteStateAutomaton cp = crossProduct.getDFACrossProduct(result, automatonList.get(i), alphabet);
			FinitenessDetector detector = new FinitenessDetector();
			System.out.println(detector.isFinite(cp));
			System.out.println(cp.toString());
		}*/
		
		
		/*FiniteStateAutomaton cp = crossProduct.getDFACrossProduct(result, result, alphabet);
		FinitenessDetector detector = new FinitenessDetector();
		System.out.println(detector.isFinite(cp));
		System.out.println(cp.toString());*/
		
		FiniteStateAutomaton testAutomaton = new FiniteStateAutomaton();
		State a = testAutomaton.createStateWithId(new Point(), 1);
		State b = testAutomaton.createStateWithId(new Point(), 2);
		State c = testAutomaton.createStateWithId(new Point(), 3);
		testAutomaton.setInitialState(c);
		testAutomaton.addFinalState(c);
		testAutomaton.addTransition(new FSATransition(a, b, "1"));
		testAutomaton.addTransition(new FSATransition(a, c, "0"));
		testAutomaton.addTransition(new FSATransition(b, c, "0"));
		testAutomaton.addTransition(new FSATransition(b, c, "1"));
		testAutomaton.addTransition(new FSATransition(c, c, "0"));
		//FinitenessDetector detector = new FinitenessDetector();
		List<String> alphabet1 = new ArrayList<String>();
		for(String alpha : alphabetRetriever.getAlphabet(testAutomaton))
		{
				alphabet1.add(alpha);
		}
		AnalysisEngine engine = new AnalysisEngine();
		System.out.println(engine.computeNoDistinctStatesMetric(new HashSet<String>(alphabet1), testAutomaton));
		/*VectorGenerator generator = new VectorGenerator(alphabet1);
		System.out.println(generator.generateVector(testAutomaton,testAutomaton.getInitialState()));
		System.out.println(generator.generateVector(testAutomaton,testAutomaton.getStateWithID(2)));
		System.out.println(generator.generateVector(testAutomaton,testAutomaton.getStateWithID(3)));*/
		//System.out.println(detector.isFinite(testAutomaton));
		
		
		Plot2DPanel plot = new  Plot2DPanel();
		double [] x = {1,2,3};
		double [] y = {1,2,3};
		plot.addLinePlot("my plot", x, y);
		x[0] = 4;
		Plot2DPanel plot1 = new  Plot2DPanel();
		plot1.addLinePlot("another plot", x, y);
		JFrame frame = new JFrame("a plot panel");
		frame.setLayout(new GridLayout(3, 2));
		frame.add(plot);
		frame.add(plot1);
		frame.add(plot);
		//frame.setContentPane(plot1);
		frame.setVisible(true);
		
	}
	
	
}
