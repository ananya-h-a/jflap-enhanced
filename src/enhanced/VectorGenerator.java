package enhanced;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import automata.State;
import automata.Transition;
import automata.fsa.FSATransition;
import automata.fsa.FiniteStateAutomaton;

public class VectorGenerator 
{
	private List<String> alphabet;
	public VectorGenerator(Set<String> alphabet)
	{
		this.alphabet = new ArrayList<String>();
		for(String s: alphabet)
		{
			this.alphabet.add(s);
		}
		
	}
	public List<Integer>  generateVector(FiniteStateAutomaton automaton,State state)
	{
		List<Integer> vector = new ArrayList<Integer>();
		vector.add(automaton.isInitialState(state)?1:0);
		vector.add(automaton.isFinalState(state)?1:0);
		Map<String,Transition> transitionMap = new HashMap<String,Transition>();
		Transition [] transitions = automaton.getTransitionsFromState(state);
		for(Transition t: transitions)
		{
				FSATransition transition = (FSATransition) t;
				transitionMap.put(transition.getLabel(),t);
		}
		for(String alpha:alphabet)
		{
			if(transitionMap.containsKey(alpha))
			{
				FSATransition transition = (FSATransition) transitionMap.get(alpha);
				if(transition.getToState().equals(transition.getFromState()))
				{
					vector.add(0);
				}
				else
				{
					State toState = transition.getToState();
					if(automaton.isFinalState(toState))
					{
						vector.add(2);
					}
					else
					{
						vector.add(1);
					}
				}
			}
			else
			{
				vector.add(-1);
			}
		}
		return vector;
	}
}
