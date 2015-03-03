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

package enhanced;

import gui.JTableExtender;
import enhanced.action.AutoTestDFA;
import gui.action.BatchMultipleSimulateAction;
import gui.action.MultipleSimulateAction;

import java.awt.Color;
import java.awt.Component;

import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;

public class CustomJTableExtender extends JTableExtender{
	public CustomJTableExtender(TableModel model, MultipleSimulateAction mult){
		super(model,mult);
		myMultSimAct = mult;
	}
	
	public CustomJTableExtender(TableModel model, BatchMultipleSimulateAction mult){
		super(model,mult);
		myMultSimAct = mult;
	}
	
	
	public void changeSelection (int row, int column, boolean toggle, boolean extend) {
		 super.changeSelection (row, column, toggle, extend);
		 myMultSimAct.viewAutomaton(this);
		 }
	
	public Component prepareRenderer(TableCellRenderer renderer, int row, int column)
	{
		Component c = super.prepareRenderer(renderer, row, column);
		if (getValueAt(row, column).equals(AutoTestDFA.mesgMatch)){
			c.setBackground(Color.GREEN);
		}
		else if( getValueAt(row, column).equals(AutoTestDFA.mesgMismatch)){
			c.setBackground(Color.RED);
		}
		else if(getValueAt(row, column).equals(AutoTestDFA.mesgException)){
			c.setBackground(Color.YELLOW);
		}
        else {
        	c.setBackground(Color.white);
        }
		return c;
	}
	
	private MultipleSimulateAction myMultSimAct;
}
