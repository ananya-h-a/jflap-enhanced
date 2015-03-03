/*
 *  JFLAP - Formal Languages and Automata Package
 * 
 * 
 *  Susan H. Rodger
 *  Computer Science Department
 *  Duke University
 *  August 27, 2009

 *  Copyright (c) 2002-2009
 *  All rights reserved.

 *  JFLAP is open source software. Please see the LICENSE for terms.
 *
 */
package enhanced.action;

import enhanced.action.ConvertFSAToREActionNew;
import enhanced.regular.CustomConvertPane;
import enhanced.CustomUtilities;
import gui.action.NewAction;
import gui.action.NondeterminismAction;
import gui.environment.AutomatonEnvironment;
import gui.environment.Environment;
import gui.environment.Universe;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

import automata.Automaton;
import automata.AutomatonChecker;
import automata.State;
import automata.Transition;
import automata.UselessStatesDetector;
import automata.fsa.FSATransition;
import automata.fsa.FiniteStateAutomaton;
import automata.graph.FSAEqualityChecker;

/**
 * This tests to see if two finite state automatons accept the same language.
 * 
 * 
 */

public class CompareDFAAction {
    public static String ACCEPT_STRINGS = "AcceptStrings";
    public static String REJECT_STRINGS = "RejectStrings";

 
	/**
	 * Instantiates a new <CODE>DFAEqualityAction</CODE>.
	 * 
	 * @param automaton
	 *            the automaton that input will be simulated on
	 * @param environment
	 *            the environment object that we shall add our simulator pane to
	 */
	public CompareDFAAction(FiniteStateAutomaton automaton,
			Environment environment) {
		this.environment = environment;
	}

	
	public void compareDFAAnswer(){
	
		// Set up our automaton.
		Automaton automaton = (Automaton) environment
				.getObject();
		automaton.log += "<TestAgainstSolution>\n<TS>"+System.currentTimeMillis()+
				"</TS>\n<Automaton>"+automaton.toString()+"</Automaton>\n";
		if (automaton.getInitialState() == null) {
			JOptionPane.showMessageDialog(Universe
					.frameForEnvironment(environment),
					"This automaton has no initial state!");
        	Map<String,String> testAnswerFlow = new HashMap<String,String>(); 
			testAnswerFlow.put("Result", "The Automaton has no Initial State");
    		prepareAutomatonString(testAnswerFlow);
    		automaton.flowMap.add(testAnswerFlow);
    		automaton.log += "<Status>-1</Status>\n<Message>No initial state</Message>\n</TestAgainstSolution>\n";
			return;
		}
		if (automaton.getFinalStates().length == 0) {
			JOptionPane.showMessageDialog(Universe
					.frameForEnvironment(environment),
					"Conversion requires at least\n" + "one final state!",
					"No Final States", JOptionPane.ERROR_MESSAGE);
			Map<String,String> testAnswerFlow = new HashMap<String,String>(); 
			testAnswerFlow.put("Result", "The Automaton has no Final State");
    		prepareAutomatonString(testAnswerFlow);
    		automaton.flowMap.add(testAnswerFlow);
    		automaton.log += "<Status>-1</Status>\n<Message>No final state </Message>\n</TestAgainstSolution>\n";
			return ;
		}
		
		if(checkIfNFA())
		{
			automaton.log += "<Status>-1</Status>\n<Message> NFA</Message>\n</TestAgainstSolution>\n";
			return;
		}
		
		if(OpenFileFromRepositoryAction.openedAutomaton == null){
			JOptionPane.showMessageDialog(Universe
					.frameForEnvironment(environment), "No Other FAs Around!. Either You Have Closed the Problem Description or not Quizzed for One!");
			automaton.log += "<Status>-1</Status>\n<Message> No Other FAs Around </Message>\n</TestAgainstSolution>\n";
			return;
		}
		FiniteStateAutomaton other = (FiniteStateAutomaton)OpenFileFromRepositoryAction.openedAutomaton;
		
		other = (FiniteStateAutomaton) UselessStatesDetector
				.cleanAutomaton(other);
		FiniteStateAutomaton cleanedAutomaton = (FiniteStateAutomaton) UselessStatesDetector
				.cleanAutomaton(automaton);
		if(!checker.equals(other, cleanedAutomaton)){
			prepareStringsAndDisplay();
		}
		else{
			ConvertFSAToREActionNew.correctAnswerDrawn = true;
        	final long time = System.currentTimeMillis() - automaton.startTime;
        	int elapsedTimesec =(int)( time/(1000F));	
        	Map<String,String> testAnswerFlow = new HashMap<String,String>(); 
    		testAnswerFlow.put("Time" , new Integer(elapsedTimesec).toString());
    		prepareAutomatonString(testAnswerFlow);
    		automaton.flowMap.add(testAnswerFlow);
			automaton.flowList.add("Correct Automaton was drawn in " + elapsedTimesec + " Seconds\n" );
			String message = customSuccessMessage(testAnswerFlow);
    		testAnswerFlow.put("Result", message);
			automaton.cfrReference.performCloseOperation(null);
			automaton.cfrReference.questionFrame.dispose();
		}
	}
	
	
	private String prepareRE(Automaton automaton){
		
		String computedRE = "";
		ConvertFSAToREActionNew.computedRE = "";
		try{
			
			CustomUtilities.checkAndApplyLamdaTransition(automaton);

			computedRE = "";
			ConvertFSAToREActionNew.customConvertAction = true;

		new CustomConvertPane(new AutomatonEnvironment(automaton));

		ConvertFSAToREActionNew.customConvertAction = false;
		
			
		}
			
			
			
	
		catch(Exception e){
			ConvertFSAToREActionNew.customConvertAction = false;
	

	        if(ConvertFSAToREActionNew.computedRE == ""){
				return null;
	        }
	        computedRE = ConvertFSAToREActionNew.computedRE;
			
		}
		 computedRE = ConvertFSAToREActionNew.computedRE;
        return computedRE;
	}


		
	
	private void prepareStringsAndDisplay(){
		final FiniteStateAutomaton userAutomaton = (FiniteStateAutomaton) environment
				.getObject();
		System.out.println(userAutomaton.toString());
		final Map<String,String> testAnswerFlow = new HashMap<String,String>(); 
		testAnswerFlow.put("Result", "DFA drawn was Incorrect");
		prepareAutomatonString(testAnswerFlow);
		
		 String acceptTheseStrings = "";
		 String rejectTheseStrings = "";
		 // Answer DFA which is stored in OpenAction.openedAutomaton is accessed and its RE is prepared and stored in originalRE
		String originalRE = prepareRE((Automaton)OpenFileFromRepositoryAction.openedAutomaton.clone());
		// Automaton drawn by the DFA is accessed and its RE is stored in userRE.
		String userRE = prepareRE((Automaton)userAutomaton.clone());

		// Xeger Strings is prepared for the answer DFA
		List<String> originalPossibleStrings = CustomUtilities.getXegerList(originalRE);
		
		// Xeger Strings is prepared for the answer DFA
		List<String> userPossibleStrings = CustomUtilities.getXegerList(userRE);
		
		
		/*
		 * acceptTheseStrings is the strings which are there in the original answer (originalPossibleStrings)
		 * but not present in the strings generated from user drawn dfa.
		 */
		for(String str : originalPossibleStrings)
		{
			String testStr = str.replace("\u03BB","");
			if(CustomUtilities.checkInputString(environment,testStr) != 0){
				acceptTheseStrings += (str + "\n");
			}
		}
		
		AutomatonEnvironment answerEnvironment = new AutomatonEnvironment(OpenFileFromRepositoryAction.openedAutomaton);
		/*
		 * rejectTheseStrings is the strings which are not there in the original answer (originalPossibleStrings)
		 * but are present in the strings generated from user drawn dfa. These strings are not needed.
		 */
		for(String str : userPossibleStrings)
		{
			String testStr = str.replace("\u03BB","");
			if(CustomUtilities.checkInputString(answerEnvironment, testStr) != 0){
				if(!str.equals("\u00F8"))
				rejectTheseStrings += (str + "\n");
			}
		}
		
		if(acceptTheseStrings == "" && rejectTheseStrings == "")
		{
			String str = "\u03BB";
			if(CustomUtilities.checkInputString(answerEnvironment,"") != 0)
			{
				rejectTheseStrings += (str + "\n");
			}
			else
			{
				acceptTheseStrings += (str + "\n");
			}
			
		}
		userAutomaton.log += "<Status>0</Status><Message>Incorrect Solution</Message><AcceptStrings>"+acceptTheseStrings+"</AcceptStrings>\n"
				+ "<RejectStrings>"+rejectTheseStrings+"</RejectStrings>\n</TestAgainstSolution>\n";
		JPanel jp = new JPanel();
		JButton jb1 = new JButton("Accept These Strings");
		JButton jb2 = new JButton("Reject These Strings");
		jp.setLayout(new GridBagLayout());
		String s = "\nIncorrect Answer : \n";
		if(acceptTheseStrings == "\u00F8" || acceptTheseStrings == "" || acceptTheseStrings.length() == 0){
			jb1.setEnabled(false);
		}
		else{
			 s += "Accept [" + acceptTheseStrings.replace("\n", ",") + "]\n";
			 testAnswerFlow.put("Accept", acceptTheseStrings);
			
		}
		
		if(rejectTheseStrings == "\u00F8" || rejectTheseStrings == "" || rejectTheseStrings.length() == 0){
			jb2.setEnabled(false);
		}
		else{
				s += "Reject [" + rejectTheseStrings.replace("\n", ",") + "]" ;
				testAnswerFlow.put("Reject", rejectTheseStrings);
	    }
	
		userAutomaton.flowList.add(s + "\n");
		userAutomaton.flowMap.add(testAnswerFlow);
		
		GridBagConstraints gc = new GridBagConstraints();
		
       	JPanel main = new JPanel();
       	main.setLayout(new GridLayout(2,1));
       	main.add(new JLabel("This is not the Right Answer.Please Try Again!"));
		gc = new GridBagConstraints();
		gc.gridx=0;
		gc.gridy=0;
       	gc.insets = new Insets(5,0,0,0); 

		jp.add(jb1,gc);
		gc = new GridBagConstraints();
		gc.gridx=1;
		gc.gridy=0;

       	gc.insets = new Insets(5,5,0,0); 
		jp.add(jb2,gc);
		final String s1 = acceptTheseStrings;
		final String s2 = rejectTheseStrings;
		
		jb1.addActionListener(new ActionListener() {
			
		@Override
		public void actionPerformed(ActionEvent arg0) 
		{
				userAutomaton.log +="<AcceptStringClick><TS>"+System.currentTimeMillis()+"</TS></AcceptStringClick>\n";
				if(testAnswerFlow.get(ACCEPT_STRINGS) != null){
            		String clickCount = testAnswerFlow.get(ACCEPT_STRINGS);
            		int click = Integer.parseInt(clickCount);
            		testAnswerFlow.put(ACCEPT_STRINGS,new Integer(++click).toString());
            	}
            	else
            		testAnswerFlow.put(ACCEPT_STRINGS,"1");
				JTextArea jt = new JTextArea(s1);
				jt.setSize(20, 60);
				JOptionPane.showMessageDialog(null,new JScrollPane(jt));
				
			}
		});
		
		jb2.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				
				userAutomaton.log +="<RejectStringClick><TS>"+System.currentTimeMillis()+"</TS></RejectStringClick>\n";
				if(testAnswerFlow.get(REJECT_STRINGS) != null){
            		String clickCount = testAnswerFlow.get(REJECT_STRINGS);
            		int click = Integer.parseInt(clickCount);
            		testAnswerFlow.put(REJECT_STRINGS,new Integer(++click).toString());
            	}
            	else
            		testAnswerFlow.put(REJECT_STRINGS,"1");
				JTextArea jt = new JTextArea(s2);
				jt.setSize(20, 60);
				JOptionPane.showMessageDialog(null,new JScrollPane(jt));
				
			}
		});
		main.add(jp);
		JOptionPane.showMessageDialog(
				Universe.frameForEnvironment(environment),main,"ERROR",JOptionPane.ERROR_MESSAGE);
		
      }
	
	private String customSuccessMessage(Map<String,String> testAnswerFlow){
		String message = null;
		try
		{	
			int originalNumberOfStates = 0,  usrNumberOfStates = 0;
			FiniteStateAutomaton userAutomaton = (FiniteStateAutomaton) environment
				.getObject();
			if(OpenFileFromRepositoryAction.openedAutomaton != null){
			originalNumberOfStates = OpenFileFromRepositoryAction.openedAutomaton.getStates().length;
			usrNumberOfStates = userAutomaton.getStates().length;
			if(originalNumberOfStates == usrNumberOfStates){
				message = "Smallest DFA was drawn!";
				userAutomaton.log += "<Status>1</Status><Message>Minimal DFA</Message>\n</TestAgainstSolution>\n";
				JOptionPane.showMessageDialog(
						Universe.frameForEnvironment(environment),"Congrats!! You've drawn the smallest DFA","Correct Answer",JOptionPane.INFORMATION_MESSAGE);
				return message;
			}
			else if(originalNumberOfStates < usrNumberOfStates)
			{
				final JPanel shorterAnswerPanel = new JPanel();
				JButton jb = new JButton("View a smaller answer");
				shorterAnswerPanel.setLayout(new GridBagLayout());
				GridBagConstraints c = new GridBagConstraints();
				c.fill = GridBagConstraints.BOTH;
				c.weightx = 0;
				c.gridwidth = GridBagConstraints.REMAINDER;
				int diff = usrNumberOfStates - originalNumberOfStates;
				message = "Correct DFA was drawn. It could have been smaller by " + (usrNumberOfStates - originalNumberOfStates) + ((usrNumberOfStates - originalNumberOfStates)>1? "states" : "state");
				userAutomaton.log += "<Status>1</Status><Message>Not Minimal DFA</Message>\n<Difference>"
						+diff+"</Difference>\n</TestAgainstSolution>\n";
				JLabel shorterAnswer = new JLabel("Your Answer is right. You could have made it smaller by " + (usrNumberOfStates - originalNumberOfStates) + ((usrNumberOfStates - originalNumberOfStates)>1?" states" : " state"));
				shorterAnswerPanel.add(shorterAnswer,c);
				shorterAnswerPanel.add(jb,c);
				
				jb.addActionListener(new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent arg0) {
						
						OpenFileFromRepositoryAction.openedAutomaton.fromShowAnswer = true;
						OpenFileFromRepositoryAction.openedAutomaton.showOnTop = true;
						 Window win = SwingUtilities.getWindowAncestor(shorterAnswerPanel);
						 win.setVisible(false);
						try{
	                	NewAction.createWindow(OpenFileFromRepositoryAction.openedAutomaton);
						}
						catch(Exception e){
							JOptionPane.showInputDialog(e.getStackTrace());
						}
					}
				});
				JOptionPane.showMessageDialog(
						Universe.frameForEnvironment(environment), shorterAnswerPanel, "SUCCESS", JOptionPane.INFORMATION_MESSAGE);
				return message;
			}
			else
			{
				int diff = usrNumberOfStates - originalNumberOfStates;
				userAutomaton.log += "<Status>1</Status><Message>Samller DFA</Message>\n<Difference>"
						+diff+"</Difference>\n</TestAgainstSolution>\n";
				JOptionPane.showMessageDialog(
						Universe.frameForEnvironment(environment),"Congrats !! . You've drawn the Right DFA","Correct Answer",JOptionPane.INFORMATION_MESSAGE);
				return "Correct DFA was drawn!";
			}
		}
		
		}
		catch(Exception e){
		}
		
		JOptionPane.showMessageDialog(
				Universe.frameForEnvironment(environment),"Congrats !! . You've drawn the Right DFA","Correct Answer",JOptionPane.INFORMATION_MESSAGE);
		return "Correct DFA was drawn!";

	}
	private boolean checkIfNFA(){
		final FiniteStateAutomaton automaton = (FiniteStateAutomaton) environment
				.getObject();

		AutomatonChecker ac = new AutomatonChecker();
		if (ac.isNFA(automaton)) {
			JPanel jp = new JPanel();
			JButton jb = new JButton("Highlight Non-Determinism");
			jb.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent arg0) {
					
					 new NondeterminismAction((automata.Automaton) automaton,
								environment).actionPerformed(null);
				}
			});
			jp.setLayout(new GridBagLayout());
			GridBagConstraints c = new GridBagConstraints();
			c.fill = GridBagConstraints.BOTH;
			c.weightx = 0;
			c.gridwidth = GridBagConstraints.REMAINDER;

		
			jp.add(new JLabel("You've drawn an NFA. Please draw a DFA"),c);
			jp.add(jb,c);
			JOptionPane.showMessageDialog(
					Universe.frameForEnvironment(environment), jp, "NFA", JOptionPane.ERROR_MESSAGE);
			automaton.nfaDrawn = true;
			Map<String,String> testAnswerFlow = new HashMap<String,String>();
			testAnswerFlow.put("Result", "An NFA was drawn");
			
    		prepareAutomatonString(testAnswerFlow);
    		automaton.flowMap.add(testAnswerFlow);

			return true;
	    }
		return false;
  }
	
	/** The environment. */
	private Environment environment;

	/** The equality checker. */
	private static FSAEqualityChecker checker = new FSAEqualityChecker();
	private void prepareAutomatonString(Map<String,String> testAnswerFlow){
		FiniteStateAutomaton aut = (FiniteStateAutomaton)environment.getObject();
		String str = "";
		for(State state : aut.getStates()){
			if(str.length() > 1)
				str += "," ;
			str += state.getID() + " " + state.getPoint().x + " " + state.getPoint().y;
			if(aut.isInitialState(state))
				str += " Initial";
			if(aut.isFinalState(state))
				str += " Final";
		}
		testAnswerFlow.put("States", str);
		
		str = "";
		for(Transition t : aut.getTransitions()){
			if(str.length() > 1)
				str += "," ;
			str += t.getFromState().getID() + " " + ((FSATransition)t).getLabel() + " " +  t.getToState().getID();
		}
		testAnswerFlow.put("Transitions", str);

	}
}
