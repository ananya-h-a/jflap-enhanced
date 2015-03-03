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





package enhanced.editor;

import gui.editor.ArrowTool;
import gui.editor.TransitionCreator;
import gui.viewer.AutomatonDrawer;
import gui.viewer.AutomatonPane;

import java.awt.event.MouseEvent;

import automata.fsa.FiniteStateAutomaton;

/**
 * The arrow tool is used mostly for editing existing objects.
 * 
 * @author Thomas Finley, Henry Qin
 */

public class CustomArrowTool extends ArrowTool {
	/**
	 * Instantiates a new arrow tool.
	 * 
	 * @param view
	 *            the view where the automaton is drawn
	 * @param drawer
	 *            the object that draws the automaton
	 * @param creator
	 *            the transition creator used for editing transitions
	 */
	public CustomArrowTool(AutomatonPane view, AutomatonDrawer drawer,
			TransitionCreator creator) {
		super(view, drawer, creator);
		
	}

	/**
	 * Instantiates a new arrow tool.
	 * 
	 * @param view
	 *            the view where the automaton is drawn
	 * @param drawer
	 *            the object that draws the automaton
	 */
	public CustomArrowTool(AutomatonPane view, AutomatonDrawer drawer) {
		super(view, drawer);
		
	}

	

	/**
	 * Possibly show a popup menu.
	 * 
	 * @param event
	 *            the mouse event
	 */
	protected void showPopup(MouseEvent event) {
		// Should we show a popup menu?
		if (event.isPopupTrigger()) {
			
				if(!(getDrawer().getAutomaton() instanceof FiniteStateAutomaton && getDrawer().getAutomaton().fromShowAnswer)){ 
					super.showPopup(event);
				}
			}
		
		
	}
	
	protected boolean shouldShowStatePopup() {
		return false;
	}

	
}
