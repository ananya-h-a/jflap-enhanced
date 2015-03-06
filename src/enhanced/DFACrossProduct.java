package enhanced;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.tree.DefaultTreeModel;

import automata.Automaton;
import automata.State;
import automata.Transition;
import automata.UselessStatesDetector;
import automata.fsa.FSATransition;
import automata.fsa.FiniteStateAutomaton;
import automata.fsa.Minimizer;

public class DFACrossProduct
{
	public  FiniteStateAutomaton getDFACrossProduct(FiniteStateAutomaton a,
			FiniteStateAutomaton b, Set<String> alphabet)
	{
		FiniteStateAutomaton result = new FiniteStateAutomaton();
		State a0 = a.getInitialState();
		State b0 = b.getInitialState();
		a0.anotherId = Integer.toString(a0.getID());
		b0.anotherId = Integer.toString(b0.getID());
		if(a0==null || b0 == null)
		{
			return null;
		}
		State temp = result.createState(getCombinedStateId(a0,b0));
		result.setInitialState(temp);
		if(isFinal(a0,b0))
		{
			result.addFinalState(temp);
		}
		List<State> toProcess = new ArrayList<State>() ;
		toProcess.add(temp);
		while(!toProcess.isEmpty())
		{
			State stateToProcess = toProcess.remove(0);
			int [] integerIds = getIds(stateToProcess.anotherId);
			int id1 = integerIds[0];
			int id2 = integerIds[1];
			Map<String,Transition> t1 = new HashMap<String,Transition>();
			Map<String,Transition> t2 = new HashMap<String,Transition>();
			if(id1 >= 0)
			{
				a0 = a.getStateWithID(id1);
				Transition [] transitions = a.getTransitionsFromState(a0);
				for(Transition t: transitions)
				{
					FSATransition tempTransition = (FSATransition) t ;
					t1.put(tempTransition.getLabel(),t);
				}
			}
			if(id2 >= 0)
			{
				b0 = b.getStateWithID(id2);
				Transition [] transitions = b.getTransitionsFromState(b0);
				for(Transition t: transitions)
				{
					FSATransition tempTransition = (FSATransition) t ;
					t2.put(tempTransition.getLabel(),t);
				}
			}
			for(String alpha : alphabet)
			{
				Transition tempTransition1 = t1.get(alpha) ;
				State to1 = null;
				if(tempTransition1 != null)
				{
					to1 = tempTransition1.getToState();
				}
				else
				{
					to1 = new State(-1,"-1",result);
				}
				to1.anotherId = Integer.toString(to1.getID());
				
				
				Transition tempTransition2 = t2.get(alpha) ;
				State to2 = null;
				if(tempTransition2 != null)
				{
					to2 = tempTransition2.getToState();
				}
				else
				{
					to2 = new State(-1,"-1",result);
				}
				to2.anotherId = Integer.toString(to2.getID());
				String newId = getCombinedStateId(to1, to2);
				State tempState = getStateWithAnotherId(result, newId);
				if(tempState == null)
				{
					tempState = result.createState(newId);
					toProcess.add(tempState);
				}
				FSATransition fsaTransition= new FSATransition(stateToProcess, tempState, alpha);
				result.addTransition(fsaTransition);
				if(isFinal(to1,to2))
				{
					result.addFinalState(tempState);
				}
					
			}
			
		}
		//result = (FiniteStateAutomaton)  UselessStatesDetector.cleanAutomaton(result);
		/*Minimizer minimizer = new Minimizer();
		minimizer.initializeMinimizer();
		FiniteStateAutomaton tempAutomaton = (FiniteStateAutomaton)minimizer.getMinimizeableAutomaton(result);
		if(tempAutomaton == null)
		{
			//hack for bad language
			System.out.println("Hack");
			result = (FiniteStateAutomaton) minimizer.getMinimizeableAnalysisAutomaton(result);
		}
		else
		{
			result = tempAutomaton;
		}
		javax.swing.tree.DefaultTreeModel tree = minimizer
				.getDistinguishableGroupsTree(result);
		result = minimizer.getMinimumDfa(result, tree);*/
		NewMinimizer minimizer = new NewMinimizer();
		result = minimizer.getMinimizedAutomaton(result);
		return result;
	}
	
	public String getCombinedStateId(State a,State b)
	{
		String combinedId = a.anotherId +":"+b.anotherId;
		return combinedId;
	}
	
	public boolean isFinal(State a,State b)
	{
		boolean condition1, condition2;
		if (a.getAutomaton() == null)
		{
			condition1 = false;
		}
		else
		{
			condition1 = a.getAutomaton().isFinalState(a);
		}
		if(b.getAutomaton() == null)
		{
			condition2 = false;
		}
		else
		{
			condition2 = b.getAutomaton().isFinalState(b);
		}
		return (condition1 && !condition2) || (condition2 & !condition1) ;
	}
	
	public int[] getIds(String id)
	{
		String[] stringIds = id.split(":");
		int [] integerIds = new int[2];
		integerIds[0] = Integer.parseInt(stringIds[0]);
		integerIds[1] = Integer.parseInt(stringIds[1]);
		return integerIds;
	}
	
	public State getStateWithAnotherId(FiniteStateAutomaton automaton ,String id)
	{
		State[] states = automaton.getStates();
		for(State s : states)
		{
			if(s.anotherId.equals(id))
			{
				return s;
			}
		}
		return null;
	}

}
