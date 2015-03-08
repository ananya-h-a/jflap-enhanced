package enhanced;

import automata.State;

public class UnionOfDifferenceLanguage implements IsFinal
{
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

}
