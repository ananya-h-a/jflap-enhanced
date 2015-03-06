package enhanced;


import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import automata.State;
import automata.StatePlacer;
import automata.Transition;
import automata.UnreachableStatesDetector;
import automata.fsa.FSAAlphabetRetriever;
import automata.fsa.FSATransition;
import automata.fsa.FiniteStateAutomaton;

public class NewMinimizer {
	
	private FSAAlphabetRetriever retriever;
	private String[] alphabet;
	private StatePlacer placer;
	public NewMinimizer()
	{
		 retriever = new FSAAlphabetRetriever();
		 placer = new StatePlacer();
	}
	public FiniteStateAutomaton getMinimizedAutomaton(FiniteStateAutomaton b)
	{
		FiniteStateAutomaton a = (FiniteStateAutomaton)b.clone();
		alphabet = retriever.getAlphabet(a);
		UnreachableStatesDetector usd = new UnreachableStatesDetector(a);
		State[] unreachableStates = usd.getUnreachableStates();
		for (int k = 0; k < unreachableStates.length; k++) 
		{
			a.removeState(unreachableStates[k]);
		}
		System.out.println(a);
		addTrapState(a);
		//System.out.println(a);
		List<String> mergeableStates = getMergeableStates(a);
		List<List<Integer>> partitions = getMergeableSet(mergeableStates); 
		System.out.println(partitions);
		for(State s : a.getStates())
		{
			int newid = getPartitionState(s.getID(), partitions);
			if(newid != s.getID())
			{
				State mergedState = a.getStateWithID(newid);
				if(a.isInitialState(s))
				{
					a.setInitialState(mergedState);
				}
				for(Transition t : a.getTransitionsFromState(s))
				{
					FSATransition newTransition = (FSATransition) t ;
					String label = newTransition.getLabel();
					if(!hasTransition(a, mergedState, label))
					{
						State newToState = a.getStateWithID(getPartitionState(newTransition.getToState().getID(), partitions)); 
						a.addTransition(new FSATransition(mergedState,newToState, label));
					}
				}
				for(Transition t : a.getTransitionsToState(s))
				{
					FSATransition transition = (FSATransition) t;
					a.addTransition(new FSATransition(transition.getFromState(), mergedState, transition.getLabel()));
				}
				a.removeState(s);
			}
		}
		//System.out.println(a);
		return a;
	}
	
	private int getPartitionState(int id,List<List<Integer>> partitions)
	{
		for(List<Integer> partition : partitions)
		{
			if(partition.contains(id))
			{
				return partition.get(0);
			}
		}
		return id;
	}
	
	public void addTrapState(FiniteStateAutomaton a)
	{
		List<State> trapStates = getTrapState(a);
		//System.out.println("Trap states "+trapStates);
		for(State s : trapStates)
		{
			a.removeState(s);
		}
		if(needsTrapState(a))
		{
			System.out.println("Trap State needed");
			State trapState = a.createState(placer.getRandomPoint());
			for(State s : a.getStates())
			{
				for(String alpha : alphabet)
				{
					if(!hasTransition(a, s, alpha))
					{
						//System.out.println("Adding transition from "+s+ " on "+alpha);
						a.addTransition(new FSATransition(s, trapState, alpha));
					}
				}
			}
			
		}
	}
	private List<State> getTrapState(FiniteStateAutomaton a)
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
	
	private boolean needsTrapState(FiniteStateAutomaton a)
	{
		
		for(State s : a.getStates())
		{
			for(String alpha : alphabet)
			{
				if(!hasTransition(a,s,alpha))
				{
					//System.out.println("State "+s+" on "+alpha );
					return true;
				}
					
			}
			
		}
		return false;
	}
	private boolean hasTransition(FiniteStateAutomaton a, State s , String alpha)
	{
		for(Transition t : a.getTransitionsFromState(s))
		{
			FSATransition temp = (FSATransition)t;
			if(temp.getLabel().equals(alpha))
			{
				//System.out.println("Transition is "+temp);
				return true;
			}
		}
		return false;
	}
	
	private Transition getTransition(FiniteStateAutomaton a, State s , String alpha)
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
	
	private List<String> getMergeableStates(FiniteStateAutomaton a)
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
		//System.out.println(mergeableMap);
		boolean shouldCompute = true;
		while(shouldCompute)
		{
			//System.out.println("Here");
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
					else
					{
						if(t1.getID() != t2.getID())
						{
							String newkey = t1.getID()+":"+t2.getID();
							if(!mergeableMap.containsKey(newkey))
							{
								newkey = t2.getID()+":"+t1.getID();
							}
							int newCompatibility = mergeableMap.get(newkey);
							int currentComptibilty = mergeableMap.get(key);
							if(newCompatibility == 0 && currentComptibilty != newCompatibility)
							{
								//System.out.println(key+" and "+newkey);
								mergeableMap.put(key,newCompatibility);
								//System.out.println(mergeableMap);
								shouldCompute = true;
							}
						}
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
		//System.out.println(mergeableStates);
		return mergeableStates;
	}
	
	private List<List<Integer>> getMergeableSet(List<String> mergeableStates)
	{
		List<List<Integer>> mergeableSet = new ArrayList<List<Integer>>();
		while(mergeableStates.size() > 0)
		{
			String mergeableState = mergeableStates.get(0);
			Set<Integer> partition = new HashSet<Integer>();
			for(String s:  mergeableState.split(":"))
			{
				partition.add(Integer.parseInt(s));
			}
			int len = partition.size();
			boolean flag = true;
			while(flag)
			{
				List<String> keysToRemove = new ArrayList<String>();
				flag = false;
				for(String key: mergeableStates)
				{
					String [] ids = key.split(":");
					int id1 = Integer.parseInt(ids[0]);
					int id2 = Integer.parseInt(ids[1]);
					if(partition.contains(id1) || partition.contains(id2))
					{
						partition.add(id1);
						partition.add(id2);
						keysToRemove.add(key);
					}
				}
				for(String key: keysToRemove)
				{
					mergeableStates.remove(key);
				}
				int newlen = partition.size();
				if(newlen != len)
				{
					len = newlen;
					flag = true;
				}
			}
			mergeableSet.add(new ArrayList<Integer>(partition));

		}
		return mergeableSet;
	}
}
