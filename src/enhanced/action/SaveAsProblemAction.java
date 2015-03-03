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

import file.Codec;
import file.EncodeException;
import file.ParseException;
import enhanced.action.ConvertFSAToREActionNew;
import gui.action.SaveAction;
import gui.action.SaveAsAction;
import gui.editor.EditBlockPane;
import gui.editor.EditorPane;
import gui.environment.AutomatonEnvironment;
import gui.environment.Environment;
import gui.environment.EnvironmentFrame;
import gui.environment.Universe;
import gui.grammar.GrammarInputPane;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.Serializable;
import java.util.Iterator;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import javax.swing.filechooser.FileFilter;

import automata.AutomatonChecker;

/**
 * The <CODE>SaveAction</CODE> is an action to save a serializable object
 * contained in an environment to a file.
 * 
 * 
 */

public class SaveAsProblemAction extends SaveAsAction {
	public static String easyPath = "DFA_LIBRARY" + System.getProperty("file.separator") + "Easy";
	public static String hardPath = "DFA_LIBRARY" + System.getProperty("file.separator") + "Hard";
	public static String filePath = "DFA_LIBRARY" ;

	private  boolean fromSaveAsProblem;
	
	
	/**
	 * Instantiates a new <CODE>SaveAction</CODE>. 
	 * 
	 * @param environment
	 *            the environment that holds the serializable
	 */
	public SaveAsProblemAction(Environment environment) {
		super(environment);
		putValue(NAME, "Save As Problem");
		putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_S,
				MAIN_MENU_MASK));
		this.environment = environment;
	}

	/**
	 * If a save was attempted, call the methods that handle the saving of the
	 * serializable object to a file.
	 * 
	 * @param event
	 *            the action event
	 */
	public void actionPerformed(ActionEvent event) {
			if(testBeforeSave()){
				String problemDescription = JOptionPane.showInputDialog(null,"Enter a Problem Description to be Saved","Problem Description",JOptionPane.NO_OPTION);
				if(problemDescription == null || problemDescription.length() == 0){
					JOptionPane.showMessageDialog(null, "The Problem Cannot be Saved Without a Description!", "Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
				saveAsProblem(problemDescription);
			
		}
	}

	public boolean saveAsProblem(String problemDescription) {
		EnvironmentFrame eFrame = 	Universe.frameForEnvironment(environment);
		File file = environment.getFile();
        Codec codec = (Codec) environment.getEncoder();
        Serializable object = environment.getObject();
        if(environment.myObjects!=null  && environment.getActive()!=null && environment.getActive() instanceof EditorPane){
            EditorPane ep = (EditorPane)environment.getActive();
            File expected = new File(ep.getAutomaton().getFilePath()+ep.getAutomaton().getFileName());
            file = expected;
            object = ep.getAutomaton();
        }
        else if(environment.myObjects!=null  && environment.getActive()!=null && environment.getActive() instanceof GrammarInputPane){
            GrammarInputPane ep = (GrammarInputPane)environment.getActive();
            File expected = new File(ep.getGrammar().getFilePath()+ep.getGrammar().getFileName());
            file = expected;
            object = ep.getGrammar();
        }

		boolean blockEdit = false;
		if (environment.getActive() instanceof EditBlockPane) {
			EditBlockPane newPane = (EditBlockPane) environment.getActive();
			object = newPane.getAutomaton();
			blockEdit = true;
		}
		FileFilter[] filters = Universe.CHOOSER.getChoosableFileFilters();
		for (int i = 0; i < filters.length; i++)
			Universe.CHOOSER.removeChoosableFileFilter(filters[i]);
		List encoders = Universe.CODEC_REGISTRY.getEncoders(object);
		Iterator it = encoders.iterator();
		while (it.hasNext())
			Universe.CHOOSER.addChoosableFileFilter((FileFilter) it.next());
		if (codec != null && codec.canEncode(object)) {
			Universe.CHOOSER.setFileFilter(codec);
		} else {
			Universe.CHOOSER.setFileFilter((FileFilter) encoders.get(0));
		}
		// Is this encoder valid?
		if (file != null && (codec == null || !codec.canEncode(object))) {
			JOptionPane
					.showMessageDialog(
							eFrame,
							"We cannot write this structure in the same format\n"
									+ "it was read as!  Use Save As to select a new format.",
							"IO Error", JOptionPane.ERROR_MESSAGE);
			return false;
		}

		// Set the file filters.
		String path = "";
		// The save as loop.
		String filename = "";
				try{
					File count = null;

					
					count = new File(filePath);
					path = filePath;
					
					int fileNum = 0;
					if(count.isDirectory()){
						fileNum = count.list().length/2;
						fileNum++;
					}

				

	    filename = new Integer(fileNum).toString();

	    FileWriter fw = new FileWriter(file.getAbsoluteFile());
		BufferedWriter bw = new BufferedWriter(fw);
		bw.write(new Integer(fileNum));
		bw.close();
				}
				catch(Exception e){
				System.out.println(e.getStackTrace());
				}

//                  Get the suggested file name.
                	try{
                    codec = (Codec) Universe.CHOOSER.getFileFilter();
                    file = new File(path, codec.proposeFilename(
                            filename, object));
                    File textFile = new File(path + System.getProperty("file.separator") + filename + ".txt");
                    FileWriter fw = new FileWriter(textFile.getAbsoluteFile());
            		BufferedWriter bw = new BufferedWriter(fw);
            		bw.write(problemDescription);
            		bw.close();
            		JOptionPane.showMessageDialog(null,"The DFA and the Description have been Successfully Saved as Problem #" + filename ,"Success",JOptionPane.NO_OPTION);
                	}
                	catch(Exception e){

                	}

                    // Check for the existing file.


		// Use the codec to save the file.
		try {
		codec.encode(object, file, null);
			if (!blockEdit)
				environment.setFile(file);
			environment.setEncoder(codec);
			environment.clearDirty();
			return true;
		} catch (ParseException e) {
			JOptionPane.showMessageDialog(eFrame, e.getMessage(), "Write Error",
					JOptionPane.ERROR_MESSAGE);
			return false;
		} catch(EncodeException e){
            JOptionPane.showMessageDialog(eFrame, e.getMessage(), "Write Error",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }
	}

	public boolean testBeforeSave() {
		ConvertFSAToREActionNew.computedRE = "";
		AutomatonEnvironment aEnv = (AutomatonEnvironment)this.environment;
		JFrame frame = Universe.frameForEnvironment(aEnv);
		if (aEnv.getAutomaton().getInitialState() == null) {
			JOptionPane.showMessageDialog(frame,
					"This DFA requires an initial state!", "No Initial State",
					JOptionPane.ERROR_MESSAGE);
			return false;
		}
		if (aEnv.getAutomaton().getFinalStates().length == 0) {
			JOptionPane.showMessageDialog(frame, "The DFA must have at least\n"
					+ "one final state!", "No Final States",
					JOptionPane.ERROR_MESSAGE);
			return false;
		}

		AutomatonChecker ac = new AutomatonChecker();
		

		if (ac.isNFA(aEnv.getAutomaton())) {
			JOptionPane.showMessageDialog(null,
					"You've drawn an NFA. Please draw a DFA", "",
					JOptionPane.ERROR_MESSAGE);
			return false;
		}

		return true;
	}

}
