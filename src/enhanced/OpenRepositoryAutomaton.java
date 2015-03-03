package enhanced;

import java.io.File;
import java.util.Iterator;
import java.util.List;

import javax.swing.filechooser.FileFilter;

import automata.Automaton;
import enhanced.action.OpenFileFromRepositoryAction;
import file.Codec;
import gui.environment.Universe;

public class OpenRepositoryAutomaton 
{
	
	public static Automaton getAutomaton(int index) throws Exception
	{
		File automatonFile = getDFAResource(Integer.toString(index));
		Codec[] codecs = null;
		codecs = OpenFileFromRepositoryAction.makeFilters();
		Object automaton = OpenFileFromRepositoryAction.openHiddenFile(automatonFile, codecs);
		return (Automaton)automaton;
	}
	
	private static File getDFAResource(String fileNameIndex) throws Exception
	{
		File f = null;
		f = new File("DFA_LIBRARY" + System.getProperty("file.separator") + fileNameIndex + ".jff");	
		return f;
	}
	
	public Codec[] makeFilters() 
	{
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
}
