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





package gui.environment;

import gui.editor.UndoKeeper;
import automata.Automaton;
import automata.State;
import automata.Transition;
import automata.event.AutomataStateEvent;
import automata.event.AutomataStateListener;
import automata.event.AutomataTransitionEvent;
import automata.event.AutomataTransitionListener;
import automata.event.AutomataNoteEvent;
import automata.event.AutomataNoteListener;
import automata.fsa.FSATransition;
public class AutomatonEnvironment extends Environment {
	/**
	 * Instantiates an <CODE>AutomatonEnvironment</CODE> for the given
	 * automaton. By default this method will set up an environment with an
	 * editor pane for this automaton.
	 * 
	 * @param automaton
	 *            the automaton to set up an environment for
	 * @see gui.editor.EditorPane
	 */
	public AutomatonEnvironment(Automaton automaton) {
		super(automaton);
		Listener listener = new Listener();
		automaton.addStateListener(listener);
		automaton.addTransitionListener(listener);
		automaton.addNoteListener(listener);
		initUndoKeeper();
	}
	private boolean log = false;
	private String logString = "";
	/**
	 * Returns the automaton that this environment manages.
	 * 
	 * @return the automaton that this environment manages
	 */
	public Automaton getAutomaton() {
		return (Automaton) super.getObject();
	}
	
	/*Start undo methods*/
    public UndoKeeper getUndoKeeper(){
        return myKeeper;	
    }
    public void initUndoKeeper(){
        myKeeper = new UndoKeeper(getAutomaton());
    }
    public void saveStatus(){
        myKeeper.saveStatus();	
    }
    public void restoreStatus(){
        myKeeper.restoreStatus();	
    }
    
    public boolean shouldPaint(){
        return myKeeper == null ? true: !myKeeper.sensitive;	
    }
    
    public void setWait(){
    	myKeeper.setWait();
    }

    public void redo(){
        myKeeper.redo();
    }
	// Logging enabled
    public void setLog()
    {
    	this.log = true;
    }
    public String getLog()
    {
    	return logString;
    }
	private UndoKeeper myKeeper;
    /*End undo methods*/

	/**
	 * The transition and state listener for an automaton detects if there are
	 * changes in the environment, and if so, sets the dirty bit.
	 */
	private class Listener implements AutomataStateListener,
			AutomataTransitionListener, AutomataNoteListener {
		public void automataTransitionChange(AutomataTransitionEvent e) {
			if(log)
			{
				printTransition(e);
			}
			setDirty();
		}

		public void automataStateChange(AutomataStateEvent e) {
			if(log)
			{
				printState(e);
			}
			setDirty();
		}

        public void automataNoteChange(AutomataNoteEvent e){
            setDirty();
        }
        public void printTransition(AutomataTransitionEvent e)
        {
        	Transition t = e.getTransition();
        	String label = null;
        	if (e.getTransition() instanceof FSATransition)
        	{
        		FSATransition t1 = (FSATransition) t;
        		label = t1.getLabel();
        	}
        	State from = t.getFromState();
        	State to = t.getToState();
        	if(e.isAdd() == true)
        	{
        		//System.out.println("Transition added "+from.getID()+" to "+ to.getID()+" on "+label);
        		logString +="<AddTransition><TS>"+System.currentTimeMillis()+"</TS><From>"+from.getID()+"</From><To>"+to.getID()+"</To><Label>"+label+"</Label></AddTransition>\n";
        	}
        	else if (e.isDelete() == true)
        	{
        		//System.out.println("Transition deleted "+from.getID()+" to "+ to.getID()+" on "+label);
        		logString +="<DeleteTransition><TS>"+System.currentTimeMillis()+"</TS><From>"+from.getID()+"</From><To>"+to.getID()+"</To><Label>"+label+"</Label></DeleteTransition>\n";
        	}
        	else if(e.isChange())
        	{
            	//System.out.println("Transition changed "+from.getID()+" to "+ to.getID()+" on "+label);
        		logString +="<ChangeTransition><TS>"+System.currentTimeMillis()+"</TS><From>"+from.getID()+"</From><To>"+to.getID()+"</To><Label>"+label+"</Label></ChangeTransition>\n";
        	}
        }
        public void printState(AutomataStateEvent e)
        {
        	State s = e.getState();
        	if(e.isAdd() == true)
        	{
        		//System.out.println("State added "+s.getID());
        		logString+="<AddState><TS>"+System.currentTimeMillis()+"</TS><State>"+s.getID()+"</State></AddState>\n";
        	}
        	else if(e.isDelete() == true)
        	{
        		//System.out.println("State deleted "+s.getID());
        		logString+="<DeleteState><TS>"+System.currentTimeMillis()+"</TS><State>"+s.getID()+"</State></DeleteState>\n";
        	}
        	else if(e.isLabel() == true)
        	{
        		//System.out.println("State labeled "+s.getLabel());
        		
        		if(s.getLabel()==null)
        		{
        			Automaton auto = getAutomaton();
        			int initialState = -1;
        			if(auto.getInitialState() != null)
        			{
        				 initialState = auto.getInitialState().getID();
        			}
        			State [] finalStates = auto.getFinalStates();
        			String finalStateStr = "";
        			if(finalStates.length == 0)
        			{
        				finalStateStr = "-1;";
        			}
        			else
        			{
        				for(State f:finalStates)
        				{
        				finalStateStr+=f.getID()+";";
        				}
        			}
        			finalStateStr = finalStateStr.substring(0,finalStateStr.length()-1);
        			logString+="<LabelState><TS>"+System.currentTimeMillis()+"</TS><State>"+s.getID()+"</State>"
        					+ "<InitialState>"+initialState+"</InitialState><FinalStates>"+finalStateStr
        							+ "</FinalStates></LabelState>\n";
        		}
        		else
        		{
        			logString+="<LabelState><TS>"+System.currentTimeMillis()+"</TS><State>"+s.getID()+"</State><Label>"+s.getLabel() +"</Label></LabelState>\n";
        		}
        	}
        	else if(e.isMove() == true)
        	{
        		//System.out.println("State moved "+s.getID());
        		//logString+="<MoveState>"+s.getID()+"</MoveState>\n"; // Not needed I suppose
        	}
        	
        }
        
	}
}
