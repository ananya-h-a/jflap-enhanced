package enhanced.ee.ut.math.automaton.nfa;

import java.util.HashSet;
import java.util.Set;

import enhanced.ee.ut.math.automaton.core.Automaton;
import enhanced.ee.ut.math.automaton.core.State;
import enhanced.ee.ut.math.automaton.core.Transition;

/**
 * Nondeterministic finite state automaton
 *
 */
public class NFA extends Automaton {
	private static final long serialVersionUID = 8495207087144945064L;


	public Set<State> getStates(State state, Character character) {
		Set<State> states = new HashSet<State>();
		
		for(Transition transition : state.getOutgoing()) {
			if(transition.isCharacter() && 
					transition.getCharacter().equals(character)) {
				states.add(transition.getTo());
			}
		}
		return states;
	}
	
	
	public Set<State> getEpsilonStates(State state) {
		Set<State> states = new HashSet<State>();
		
		for(Transition transition : state.getOutgoing()) {
			if(transition.isEpsilon()) {
				states.add(transition.getTo());
			}
		}	
		return states;
	}

}
