package enhanced;

import gui.environment.Environment;
import gui.environment.Universe;

import java.awt.Component;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import enhanced.action.ConvertFSAToREActionNew;
import enhanced.action.Xeger;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import automata.Automaton;
import automata.AutomatonSimulator;
import automata.Configuration;
import automata.SimulatorFactory;
import automata.State;
import automata.Transition;
import automata.fsa.FSATransition;
import automata.fsa.FiniteStateAutomaton;

public class CustomUtilities {
	
	public static boolean skipStepsForREToDFA;

	public static int checkInputString(Environment environment,String input){
		Configuration[] configs = null;
		FiniteStateAutomaton userAutomaton = (FiniteStateAutomaton) environment
				.getObject();
    	AutomatonSimulator as = SimulatorFactory
				.getSimulator(userAutomaton);
    	//NewAction.createWindow(userAutomaton);
    	configs = as
				.getInitialConfigurations(input);
    	List associated = new ArrayList();
    	try{
    	int result = handleInput(environment,userAutomaton,  as,
				configs, input, associated);
    	
    	return result;
    	}
    	catch(Exception ex){
    	}
    	return 1;
	}
	
	
	public static List<String> getXegerList(String computedRE){
		List<String> stringsAdded = new ArrayList<String>();
		if(computedRE == null || computedRE == "")
			return stringsAdded;
		computedRE = computedRE.replace("λ", "0{0}");
		computedRE = computedRE.replace('+', '|');
		Xeger generator = new Xeger(computedRE);
		List<String> singleLen = new ArrayList<String>();
		List<String> doubleLen = new ArrayList<String>();
		List<String> tripleLen = new ArrayList<String>();
		List<String> fourLen = new ArrayList<String>();
		List<String> fiveLen = new ArrayList<String>();
		List<String> SixLen = new ArrayList<String>();
		List<String> remainingString = new ArrayList<String>();
	


		for(int i=0 ; i<2000 ; i++){
			if(stringsAdded.size() < 15){
		String result = generator.generate();
		if(result.length() == 1 && !stringsAdded.contains(result)){
			singleLen.add(result);
			stringsAdded.add(result);
		}
		else if(result.length() == 2 && !stringsAdded.contains(result)){
			doubleLen.add(result);
			stringsAdded.add(result);
		}
		else if(result.length() == 3 && !stringsAdded.contains(result)){
			tripleLen.add(result);
			stringsAdded.add(result);
		}
		else if(result.length() == 4 && !stringsAdded.contains(result)){
			fourLen.add(result);
			stringsAdded.add(result);
		}
		else if(result.length() == 5 && !stringsAdded.contains(result)){
				fiveLen.add(result);
				stringsAdded.add(result);
		}
		else if(result.length() == 6 && !stringsAdded.contains(result)){
					SixLen.add(result);
					stringsAdded.add(result);
		}
				else{
					if(result.length() > 6 && (remainingString.size()<5) && !stringsAdded.contains(result)){
						remainingString.add(result);
			
					stringsAdded.add(result);	
					}
				}
			}
		}
		Collections.sort(stringsAdded ,new t());
		
		return stringsAdded;

	}
	
	public static class t implements Comparator<String> {
		public int compare(String a, String b) {
			return a.length() - b.length();

		}
	}
	
	public static ArrayList<String> createDFAStr(FiniteStateAutomaton fsa){
		   ArrayList<String> DFAtext = new ArrayList<String>();	       // fsmStr+= "#alphabet\n";
	        List<String> alphs = new ArrayList<String>();
	        String alph = "";
	        for(Transition t: fsa.getTransitions()){
	        	String label = ((FSATransition)t).getLabel();
	        	if(!alphs.contains(label)){
	        		alphs.add(label);
	        		alph += label + " " ;
	        	}
	        }	        
	        DFAtext.add(alph.trim());
	        Map<String,String> m = new HashMap<String,String>();
	        String states = "";
	        for(State s: fsa.getStates()){
	        	int index = Integer.parseInt(Character.toString(s.getName().charAt(1)));
	        	m.put(s.getName(), Character.toString((char)('A' + index)));
	        	states += m.get(s.getName()) + " " ;
	        }
	        DFAtext.add(states.trim());

	        String finalStates = "";
	        for(State s: fsa.getFinalStates()){
	        	finalStates += m.get(s.getName()) + " " ;
	        }
	        DFAtext.add(finalStates.trim());

	        DFAtext.add(m.get(fsa.getInitialState().getName()));

	      
	    
	        for(Transition t: fsa.getTransitions()){
	        	FSATransition ft = ((FSATransition)t);
	        	 DFAtext.add(m.get(ft.getFromState().getName()) + " " + ft.getLabel() + " " + m.get(ft.getToState().getName())) ;
    }
	        
	        
		return DFAtext;
	}
	
	public static boolean checkIfJFLAPAutomaton(Automaton automaton){
		int noOfFinalStates = automaton.getFinalStates().length;
		if(noOfFinalStates > 1)
			return false;
		State initial = automaton.getInitialState();
		for(State s :automaton.getFinalStates()){
			if(s.getName().equals(initial.getName()))
				return false;
		}
		return true;
	}
	
	public static int handleInput(Environment environment,Automaton automaton,
			AutomatonSimulator simulator, Configuration[] configs,
			Object initialInput, List associatedConfigurations) {
		JFrame frame = Universe.frameForEnvironment(environment);
		// How many configurations have we had?
		int numberGenerated = 0;
		// When should the next warning be?
		int warningGenerated = WARNING_STEP;
		Configuration lastConsidered = configs[configs.length - 1];
		while (configs.length > 0) {
			numberGenerated += configs.length;
			// Make sure we should continue.
			if (numberGenerated >= warningGenerated) {
				if (!confirmContinue(numberGenerated, frame)) {
					associatedConfigurations.add(lastConsidered);
					return 2;
				}
				while (numberGenerated >= warningGenerated)
					warningGenerated *= 2;
			}
			// Get the next batch of configurations.
			ArrayList next = new ArrayList();
			for (int i = 0; i < configs.length; i++) {
				lastConsidered = configs[i];
				if (configs[i].isAccept()) {
					associatedConfigurations.add(configs[i]);
					return 0;
				} else {
					next.addAll(simulator.stepConfiguration(configs[i]));
				}
			}
			configs = (Configuration[]) next.toArray(new Configuration[0]);
		}
		associatedConfigurations.add(lastConsidered);
		return 1;
	}
	
	protected static boolean confirmContinue(int generated, Component component) {
		int result = JOptionPane.showConfirmDialog(component, generated
				+ " configurations have been generated.  "
				+ "Should we continue?");
		return result == JOptionPane.YES_OPTION;
	}
	
	public static String getXegerString(String computedRE) {
		computedRE = computedRE.replace("λ", "0{0}");
		computedRE = computedRE.replace('+', '|');
		Xeger generator = new Xeger(computedRE);
		List<String> singleLen = new ArrayList<String>();
		List<String> doubleLen = new ArrayList<String>();
		List<String> tripleLen = new ArrayList<String>();
		List<String> fourLen = new ArrayList<String>();
		List<String> fiveLen = new ArrayList<String>();
		List<String> SixLen = new ArrayList<String>();
		List<String> remainingString = new ArrayList<String>();
		List<String> stringsAdded = new ArrayList<String>();

		for (int i = 0; i < 2000; i++) {
			String result = generator.generate();
			if (result.length() == 1 && !stringsAdded.contains(result)) {
				singleLen.add(result);
				stringsAdded.add(result);
			} else if (result.length() == 2 && !stringsAdded.contains(result)) {
				doubleLen.add(result);
				stringsAdded.add(result);
			} else if (result.length() == 3 && !stringsAdded.contains(result)) {
				tripleLen.add(result);
				stringsAdded.add(result);
			} else if (result.length() == 4 && !stringsAdded.contains(result)) {
				fourLen.add(result);
				stringsAdded.add(result);
			} else if (result.length() == 5 && !stringsAdded.contains(result)) {
				fiveLen.add(result);
				stringsAdded.add(result);
			} else if (result.length() == 6 && !stringsAdded.contains(result)) {
				SixLen.add(result);
				stringsAdded.add(result);
			} else {
				if (result.length() >= 0 && (remainingString.size() < 5)
						&& !stringsAdded.contains(result)) {
					if (result.length() == 0)
						remainingString.add("Empty String");
					else
						remainingString.add(result);

					stringsAdded.add(result);
				}
			}

		}
		Collections.sort(remainingString, new ConvertFSAToREActionNew.t());

		String endResult = "";

		for (String s : singleLen)
			endResult += s + "\n";

		for (String s : doubleLen)
			endResult += s + "\n";

		for (String s : tripleLen)
			endResult += s + "\n";

		for (String s : fourLen)
			endResult += s + "\n";

		for (String s : fiveLen)
			endResult += s + "\n";

		for (String s : SixLen)
			endResult += s + "\n";

		for (String s : remainingString)
			endResult += s + "\n";

		return endResult;
	}
	
	public static String prepareAutomatonString(Document doc, Element automatonElement,
			Automaton automaton) {
		FiniteStateAutomaton aut = (FiniteStateAutomaton) automaton;
		String str = "";
		for (State state : aut.getStates()) {
			if (str.length() > 1)
				str += ",";
			str += state.getID() + " " + state.getPoint().x + " "
					+ state.getPoint().y;
			if (aut.isInitialState(state))
				str += " Initial";
			if (aut.isFinalState(state))
				str += " Final";
		}
		if (str != "") {
			Element States = doc.createElement("States");
			States.appendChild(doc.createTextNode(str));
			automatonElement.appendChild(States);
		}

		str = "";
		FiniteStateAutomaton ft = null;
		for (Transition t : aut.getTransitions()) {
			if (str.length() > 1)
				str += ",";
			str += t.getFromState().getID() + " "
					+ ((FSATransition) t).getLabel() + " "
					+ t.getToState().getID();
		}

		if (str != "") {
			Element Transitions = doc.createElement("Transitions");
			Transitions.appendChild(doc.createTextNode(str));
			automatonElement.appendChild(Transitions);
		}
		return str;

	}
	
	public static void checkAndApplyLamdaTransition(Automaton automaton) {
		boolean useLambdaTransition = false;
		if (automaton.getFinalStates().length > 1)
			useLambdaTransition = true;
		for (State s : automaton.getFinalStates()) {
			if (s.getName().equals(
					automaton.getInitialState().getName())) {
				useLambdaTransition = true;
			}
		}

		if (useLambdaTransition) {

			int x = 0;
			int y = 0;
			Point p = new java.awt.Point(100, 100);
			State s = automaton.createState(p);

			for (State s1 : automaton.getStates()) {
				if (automaton.isFinalState(s1)) {
					FSATransition fst = new FSATransition(s1, s, "");
					automaton.addTransition(fst);

				}
			}

			automaton.finalStates = new HashSet();
			automaton.finalStates.add(s);
		}

	}

	
	protected static final int WARNING_STEP = 500;

}
