package enhanced;


import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import automata.State;
import automata.Transition;
import automata.UnreachableStatesDetector;
import automata.fsa.FSAAlphabetRetriever;
import automata.fsa.FSATransition;
import automata.fsa.FiniteStateAutomaton;

public class NewMinimizer {
	
	FSAAlphabetRetriever retriever;
	String[] alphabet;
	public NewMinimizer()
	{
		 retriever = new FSAAlphabetRetriever();
	}
	public void getMinimizedAutomaton(FiniteStateAutomaton a)
	{
		alphabet = retriever.getAlphabet(a);
		UnreachableStatesDetector usd = new UnreachableStatesDetector(a);
		State[] unreachableStates = usd.getUnreachableStates();
		for (int k = 0; k < unreachableStates.length; k++) 
		{
			a.removeState(unreachableStates[k]);
		}
		addTrapState(a);
		List<String> mergeableStates = getMergeableStates(a);
		while(mergeableStates.size() > 0)
		{
			String key= mergeableStates.get(0);
			String id[] = key.split(":");
			int id1 = Integer.parseInt(id[0]);
			int id2 = Integer.parseInt(id[1]);
			State s1 = a.getStateWithID(id1);
			State s2 = a.getStateWithID(id2);
			
		}
		
	}
	
	public void addTrapState(FiniteStateAutomaton a)
	{
		List<State> trapStates = getTrapState(a);
		System.out.println("Trap states "+trapStates);
		for(State s : trapStates)
		{
			a.removeState(s);
		}
		if(needsTrapState(a))
		{
			System.out.println("Trap State needed");
			State trapState = a.createState(new Point());
			String [] alphabet = retriever.getAlphabet(a);
			for(State s : a.getStates())
			{
				for(String alpha : alphabet)
				{
					if(!hasTransition(a, s, alpha))
					{
						a.addTransition(new FSATransition(s, trapState, alpha));
					}
				}
			}
			
		}
	}
	public List<State> getTrapState(FiniteStateAutomaton a)
	{
		List<State> trapStates = new ArrayList<State>();
		
		for(State s : a.getStates())
		{
			boolean isTrapState = true;
			if(a.isFinalState(s) || a.isInitialState(s))
			{
				continue;
			}
			Transition[] transitions = a.getTransitionsFromState(s);
			for(Transition  t : transitions)
			{
				if( ! t.getToState().equals(t.getFromState()))
				{
					isTrapState = false;
				}
			}
			if(isTrapState == true)
			{
				trapStates.add(s);
			}
		}
		return trapStates;
	}
	
	public boolean needsTrapState(FiniteStateAutomaton a)
	{
		
		for(State s : a.getStates())
		{
			for(String alpha : alphabet)
			{
				if(!hasTransition(a,s,alpha))
				{
					System.out.println("State "+s+" on "+alpha );
					return true;
				}
					
			}
			
		}
		return false;
	}
	public boolean hasTransition(FiniteStateAutomaton a, State s , String alpha)
	{
		for(Transition t : a.getTransitionsFromState(s))
		{
			FSATransition temp = (FSATransition)t;
			if(temp.getLabel().equals(alpha))
			{
				return true;
			}
		}
		return false;
	}
	
	public Transition getTransition(FiniteStateAutomaton a, State s , String alpha)
	{
		for(Transition t : a.getTransitionsFromState(s))
		{
			FSATransition temp = (FSATransition)t;
			if(temp.getLabel().equals(alpha))
			{
				return temp;
			}
		}
		return null; //shouldn't get here ideally as trap state has already been added at this point
	}
	
	public List<String> getMergeableStates(FiniteStateAutomaton a)
	{
		List <String> mergeableStates = new ArrayList<String>();
		Map<String,Integer> mergeableMap = new HashMap<String,Integer>();
		for(State s1: a.getStates())
		{
			for(State s2 : a.getStates() )
			{
				if(s1.equals(s2))
				{
					continue;
				}
				String key = s1.getID()+":"+s2.getID();
				String reversekey = s2.getID()+":"+s1.getID();
				if(mergeableMap.containsKey(reversekey))
				{
					continue;
				}
				if((a.isFinalState(s1) && !a.isFinalState(s2)) || (!a.isFinalState(s1) && a.isFinalState(s2)))
				{
					mergeableMap.put(key, 0);
				}
				else
				{
					mergeableMap.put(key, 1);
				}
			}
		}
		System.out.println(mergeableMap);
		boolean shouldCompute = true;
		while(shouldCompute)
		{
			shouldCompute = false;
			for(String key : mergeableMap.keySet())
			{
				if(mergeableMap.get(key).equals(0))
				{
					continue;
				}
				String id[] = key.split(":");
				int id1 = Integer.parseInt(id[0]);
				int id2 = Integer.parseInt(id[1]);
				State s1 = a.getStateWithID(id1);
				State s2 = a.getStateWithID(id2);
				for(String alpha:alphabet)
				{
					State t1 = getTransition(a, s1, alpha).getToState();
					State t2 = getTransition(a, s2, alpha).getToState();
					if((a.isFinalState(t1)&& !a.isFinalState(t2)) || (!a.isFinalState(t1) && a.isFinalState(t2)))
					{
						shouldCompute = true;
						mergeableMap.put(key, 0);
					}
				}
			}
		}
		for(String key:mergeableMap.keySet())
		{
			if(mergeableMap.get(key).equals(1))
			{
				mergeableStates.add(key);
			}
		}
		System.out.println(mergeableStates);
		return mergeableStates;
	}
}
