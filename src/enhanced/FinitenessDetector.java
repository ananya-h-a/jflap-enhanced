package enhanced;

import java.util.ArrayList;
import java.util.List;

import automata.State;
import automata.Transition;
import automata.fsa.FiniteStateAutomaton;

public class FinitenessDetector {
	
	public boolean isFinite(FiniteStateAutomaton automaton)
	{
		
		State [] states = automaton.getStates();
		List<State> statesList = new ArrayList<State>();
		for(State s : states)
		{
			statesList.add(s);
		}
		List<State> statesIgnoreList = new ArrayList<State>();
		statesIgnoreList = getTrapStates(automaton);
		statesList.removeAll(statesIgnoreList);
		int len = statesList.size();
		for(int i=0;i<len;i++)
		{
			State toRemove = null;
			for(State s : statesList)
			{
				boolean removable = true;
				Transition[] trans = automaton.getTransitionsToState(s);
				for(Transition t : trans)
				{
					if(!statesIgnoreList.contains(t.getFromState()))
					{
						removable = false;
						break;
					}
				}
				if(removable)
				{
					toRemove = s;
					break;
				}
			}
			
			if(toRemove != null)
			{
				statesIgnoreList.add(toRemove);
				statesList.remove(toRemove);
			}
			else
			{
				System.out.println(statesList);
				return false;
			}
		}
		System.out.println(statesList);
		return true;
	}
	public List<State> getTrapStates(FiniteStateAutomaton automaton)
	{
		List<State> trapStates = new ArrayList<State>();
		for(State s : automaton.getStates())
		{
			if(automaton.isInitialState(s) || automaton.isFinalState(s))
			{
				continue;
			}
			Transition [] transitions = automaton.getTransitionsFromState(s);
			boolean trap = true;
			for(Transition t : transitions)
			{
				if(!t.getToState().equals(s))
				{
					trap = false;
					break;
				}
			}
			if(trap)
			{
				trapStates.add(s);
			}
		}
		System.out.println(trapStates);
		return trapStates;
	}

}
