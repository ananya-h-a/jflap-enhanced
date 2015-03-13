package enhanced;

import gui.environment.FrameFactory;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import automata.fsa.FiniteStateAutomaton;

public class ViewAttempts 
{
	
	public void viewAttempts(String path)
	{
		XMLReader reader = new XMLReader(path);
		List<FiniteStateAutomaton> attempts  = reader.getAutomatonsFromXML();
		JFrame frame = new JFrame("Attempts");
		frame.setLayout(new GridLayout(0,1));
		frame.setVisible(true);
		frame.setSize(350, 225);
		if(attempts.size() == 0)
		{
			JOptionPane.showMessageDialog(null, "No attempts made");
			return;
		}
		for(int i=0;i<attempts.size();i++)
		{
			frame.add(createButton(i,attempts.get(i)));
		}
	}
	public JButton createButton(final int attemptNo, final FiniteStateAutomaton automaton)
	{
		JButton button = new JButton("Attempt "+attemptNo);
		button.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) 
			{
				FrameFactory.createFrame(automaton);
			}
		});
		return button;
	}
}
