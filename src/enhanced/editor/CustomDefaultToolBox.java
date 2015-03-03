/*
 *  
 *
 */





package enhanced.editor;

import gui.editor.ArrowTool;
import gui.editor.BlockTransitionTool;
import gui.editor.BuildingBlockTool;
import gui.editor.DeleteTool;
import gui.editor.RedoTool;
import gui.editor.StateTool;
import gui.editor.ToolBox;
import gui.editor.TransitionTool;
import gui.editor.UndoTool;
import gui.viewer.AutomatonDrawer;
import gui.viewer.AutomatonPane;

import java.util.List;

import automata.turing.TuringMachine;

/**
 * The <CODE>DefaultToolBox</CODE> has all the tools for general editing of an
 * automaton.
 */

public class CustomDefaultToolBox implements ToolBox {
	/**
	 * Returns a list of tools including a <CODE>ArrowTool</CODE>, <CODE>StateTool</CODE>,
	 * <CODE>TransitionTool</CODE> and <CODE>DeleteTool</CODE>, in that
	 * order.
	 * 
	 * @param view
	 *            the component that the automaton will be drawn in
	 * @param drawer
	 *            the drawer that will draw the automaton in the view
	 * @return a list of <CODE>Tool</CODE> objects.
	 */
	public List tools(AutomatonPane view, AutomatonDrawer drawer) {
		List list = new java.util.ArrayList();
		list.add(new CustomArrowTool(view, drawer));
		list.add(new StateTool(view, drawer));
		list.add(new TransitionTool(view, drawer));
		list.add(new DeleteTool(view, drawer));
		list.add(new UndoTool(view, drawer));
		list.add(new RedoTool(view, drawer));
		if (drawer.getAutomaton() instanceof TuringMachine) {
			TuringMachine turingMachine = (TuringMachine) drawer.getAutomaton();
			if (turingMachine.tapes() == 1) {
				list.add(new BuildingBlockTool(view, drawer));
				list.add(new BlockTransitionTool(view, drawer));
			}
		}
		return list;
	}
}
