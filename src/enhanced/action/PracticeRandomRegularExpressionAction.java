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

import enhanced.ee.ut.math.automaton.regex.RegularExpression;
import enhanced.ParseTreeTester;
import gui.AboutBox;
import gui.action.RestrictedAction;
import gui.environment.AutomatonEnvironment;
import gui.environment.Environment;
import gui.environment.EnvironmentFactory;

import java.awt.Point;
import java.awt.event.ActionEvent;

import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.JWindow;

import enhanced.editor.CustomEditorPane;
import enhanced.action.OpenFileFromRepositoryAction;

import automata.Automaton;
import automata.fsa.FiniteStateAutomaton;

/**
 * This action will display a small about box that lists the tool version
 * number, and other version.
 * 
 * 
 */

public class PracticeRandomRegularExpressionAction extends RestrictedAction {
	/**
	 * Instantiates a new <CODE>AboutAction</CODE>.
	 */
	public PracticeRandomRegularExpressionAction() {
		super("Quiz Me...", null); 
	}

	/**
	 * Shows the about box.
	 */
	public void actionPerformed(ActionEvent e) {
		
		Object[] possibleValues = {"Easy", "Medium" , "Hard"};
		Object selectedValue = JOptionPane.showInputDialog(null,
	            "Select the Level of Difficulty", "Regular Expression",
	            JOptionPane.INFORMATION_MESSAGE, null,
	            possibleValues, possibleValues[0]);
		if(selectedValue == null)
			return;
		generateRegex(selectedValue.toString(),possibleValues);

		}
	
	public static void generateRegex(String selectedValue, Object[] possibleValues){
		String regExpString = "";

		if (selectedValue==possibleValues[0])
			regExpString = ParseTreeTester.getRE(1);
	
		else if(selectedValue == possibleValues[1])
			regExpString = ParseTreeTester.getRE(2);
		else if(selectedValue == possibleValues[2])
			regExpString = ParseTreeTester.getRE(3);
		else
				return;
		
		String newregExpString = regExpString.replaceAll("\\+", "|");
		FiniteStateAutomaton dfa = RegularExpression.convertToDFA(newregExpString);
	
		OpenFileFromRepositoryAction.openedAutomaton = dfa;
	AutomatonEnvironment env = (AutomatonEnvironment) EnvironmentFactory.getEnvironment((Automaton)dfa);
	env.add(new CustomEditorPane(dfa), "Editor",new EnvironmentFactory.EditorPermanentTag());
	ConvertFSAToREActionNew.computedRE = regExpString;
	new ConvertFSAToREActionNew(
			env).displayData(ConvertFSAToREActionNew.fromFlow.REGULAR_EXPRESSION, selectedValue, "Problem Description : \n "+regExpString);
	}

			
	}


