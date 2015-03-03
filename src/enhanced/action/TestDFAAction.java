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

import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.util.Random;

import javax.swing.JOptionPane;

/*
 * This action extracts the jff files from the repository
 */
public class TestDFAAction {
	
	private boolean randomREFlow;
	private boolean scheduledQuizFlow;
	public boolean isScheduledQuizFlow() {
		return scheduledQuizFlow;
	}

	public void setScheduledQuizFlow(boolean scheduledQuizFlow) {
		this.scheduledQuizFlow = scheduledQuizFlow;
	}

	public boolean isRandomREFlow() {
		return randomREFlow;
	}

	public void setRandomREFlow(boolean randomREFlow) {
		this.randomREFlow = randomREFlow;
	}


	
	public void actionPerformed(ActionEvent e) {
	

		File f = new File("DFA_LIBRARY");
		if(f.isDirectory()){
			int length =  (f.listFiles(new FilenameFilter() {
			        @Override
			        public boolean accept(File dir, String name) {
			            return name.toLowerCase().endsWith(".jff");
			        }
			    })).length;
			if(length > 0)
				openRandomFile(length);
		}
		else{
			 JOptionPane.showMessageDialog(null,
                     "There is no Repository by the name \"DFA_LIBRARY\" ","", JOptionPane.ERROR_MESSAGE);	
		}
		
}
	

	public void openRandomFile(int limit){

		Random rand = new Random();
		int face = 1 + rand.nextInt(limit);
		//int face = 9;
		String fileNameIndex = new Integer(face).toString();
		try{
		File f = getDFAResource(fileNameIndex);
			
			String text = isRandomREFlow()? null : getDFAText(fileNameIndex);
			OpenFileFromRepositoryAction oa = new OpenFileFromRepositoryAction();
			oa.setFromScheduledQuiz(isScheduledQuizFlow());
			boolean result = oa.openFromOther(text,f,face);
			if(!result)
				openRandomFile(limit);

		}
		catch(Exception e){
			openRandomFile(limit);

		}
	}
	
	private File getDFAResource(String fileNameIndex) throws Exception{

			File f = null;
			f = new File("DFA_LIBRARY" + System.getProperty("file.separator") + fileNameIndex + ".jff");	
			return f;
		}
	
	private String getDFAText(String fileNameIndex) throws Exception{

			File f = null;
			StringBuilder stringBuilder = new StringBuilder();
		
			 f = new File("DFA_LIBRARY" + System.getProperty("file.separator") + fileNameIndex + ".txt");
				
				FileReader reader = new FileReader(f);
				BufferedReader br = new BufferedReader(reader);
				for(String line = br.readLine(); line != null; line = br.readLine()) {
				    stringBuilder.append(line + "\n");
				}
				br.close();
		

				String text = stringBuilder.toString();
				return text;
		}
	
	
}
