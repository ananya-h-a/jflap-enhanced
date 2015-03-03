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

import java.io.File;
import java.io.Serializable;
import java.util.Iterator;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;

import automata.Automaton;
import automata.turing.TuringMachine;
import file.Codec;
import file.ParseException;
import enhanced.action.ConvertFSAToREActionNew;
import enhanced.editor.CustomEditorPane;
import gui.editor.EditorPane;
import gui.environment.EnvironmentFactory;
import gui.environment.Universe;

/**
 * The <CODE>OpenAction</CODE> is an action to load a structure from a file,
 * and create a new environment with that object.
 * 
 * 
 */

public class OpenFileFromRepositoryAction {
	private boolean fromScheduledQuiz;
	public static Automaton  openedAutomaton = null;

	public boolean isFromScheduledQuiz() {
		return fromScheduledQuiz;
	}

	public void setFromScheduledQuiz(boolean fromScheduledQuiz) {
		this.fromScheduledQuiz = fromScheduledQuiz;
	}

	

		
	public static Object openHiddenFile(File file, Codec[] codecs) {
		ParseException p = null;
		for (int i = 0; i < codecs.length; i++) {
			try {
				Serializable object = codecs[i].decode(file, null);
				if (openOrRead && !(object instanceof TuringMachine)) {
                    JOptionPane.showMessageDialog(null,
                            "Only Turing Machine files can be added as building blocks.", "Wrong File Type",
                            JOptionPane.ERROR_MESSAGE);
                    return null;
					
				}
				lastObject = object;
				lastFile = file;
				// Set the file on the thing.
			
				return object;
			} catch (ParseException e) {
				p = e;
			}
		}
		if (codecs.length != 1)
			p = new ParseException("No format could read the file!");
		throw p;
	}
	
	public boolean openFromOther(String text, File file,int fileNameIndex){

		Codec[] codecs = null;
		gui.environment.AutomatonEnvironment env = null;

		codecs = makeFilters();
		try {
			Object automaton = openHiddenFile(file, codecs);
			if(automaton != null && automaton instanceof Automaton){
			//	new ConvertFSAToREActionNew((gui.environment.AutomatonEnvironment)EnvironmentFactory.getEnvironment((Automaton)automaton));
				env = (gui.environment.AutomatonEnvironment) EnvironmentFactory.getEnvironment((Automaton)automaton);
				if(automaton instanceof Automaton){
					env.remove(new EditorPane((Automaton)automaton));
					env.add(new CustomEditorPane((Automaton)automaton), "Editor",new EnvironmentFactory.EditorPermanentTag());

				}
				openedAutomaton = (Automaton)(((Automaton)automaton).clone());
				ConvertFSAToREActionNew csNew = new ConvertFSAToREActionNew(
						env);
				csNew.setQuestionIndex(fileNameIndex);
				boolean result = false;
			/*	if(isFromScheduledQuiz())
					result = csNew.displayData(ConvertFSAToREActionNew.fromFlow.SCHEDULEDQUIZ,"", text);
				else*/

					result = csNew.displayData(ConvertFSAToREActionNew.fromFlow.ENGLISHDESCRIPTION,"", text);
				return result;
			}
		} catch (ParseException e) {
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Error Showing Practice Problem. Please Try Again");
		}
		return false;

	}
	
	public static Codec[] makeFilters() {
		// Set up the file filters.
		Universe.CHOOSER.resetChoosableFileFilters();
		List decoders = Universe.CODEC_REGISTRY.getDecoders();
		Iterator it = decoders.iterator();
		while (it.hasNext())
			Universe.CHOOSER.addChoosableFileFilter((FileFilter) it.next());
		Universe.CHOOSER.setFileFilter(Universe.CHOOSER
				.getAcceptAllFileFilter());

		// Get the decoders.
		Codec[] codecs = null;
		FileFilter filter = Universe.CHOOSER.getFileFilter();
		if (filter == Universe.CHOOSER.getAcceptAllFileFilter()) {
			codecs = (Codec[]) decoders.toArray(new Codec[0]);
		} else {
			codecs = new Codec[1];
			codecs[0] = (Codec) filter;
		}
		

		return codecs;
	}


	
	// ** False causes file to be opened, True causes file to be read but not
	// opened"
	private static boolean openOrRead = false;
	private static Serializable lastObject = null;
	private static File lastFile = null;
		

}
