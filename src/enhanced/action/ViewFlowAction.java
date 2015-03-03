
package enhanced.action;

import enhanced.CustomUtilities;
import gui.action.NewAction;
import gui.environment.AutomatonEnvironment;
import gui.environment.EnvironmentFactory;
import gui.environment.EnvironmentFrame;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.Timer;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableCellRenderer;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import enhanced.editor.CustomEditorPane;
import enhanced.regular.CustomConvertPane;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import automata.Automaton;
import automata.AutomatonSimulator;
import automata.Configuration;
import automata.SimulatorFactory;
import automata.State;
import automata.fsa.FSATransition;
import automata.fsa.FiniteStateAutomaton;

/**
 * The <CODE>ViewFlowAction</CODE> 
 * This Class opens the trace xml, parses it and displays the encrypted/non-encrypted
 * versions of the xml.
 * 
 * 
 */

public class ViewFlowAction {
	 JButton next ;
	 JButton start;
	 JButton back;
	 boolean fromBack;
	public static String easyPath = "DFA_LIBRARY" + System.getProperty("file.separator") + "Easy";
	public static String hardPath = "DFA_LIBRARY" + System.getProperty("file.separator") + "Hard";
	public static String filePath = "DFA_LIBRARY" ;
	private boolean useEncryptedFile;
	private int count;
	private int tableHighLightCount;
	private boolean inTestAnswerElement;
	private JFrame parentDFAFrame;
	private EnvironmentFrame showMeAnswerFrame;
	private Document doc;
	private JFrame viewPossibleStringsFrame;
	FiniteStateAutomaton fsa = null;
	String acceptStrings;
	String rejectStrings;
	JFrame tableFrame;
	JTable table ;
	Timer timer;
	private static List<String> elements = Arrays.asList("ViewStrings","testInput","showAnswer","drawDfa","testAnswer","showAnswer",
			"exit","acceptClicked","rejectClicked");
	ActionListener actionToPerform;
	
	/*
	 * This method parses the Trace xml and displays the elements in a table.
	 */
	public boolean ViewTrace() {
		try{
			refreshConfig();
		count = -1;
		tableHighLightCount = -1;
		File workingDirectory = new File(System.getProperty("user.dir"));
		 JFileChooser chooser = new JFileChooser();
		 chooser.setCurrentDirectory(workingDirectory);
         FileNameExtensionFilter xmlfilter = new FileNameExtensionFilter(
        		 
             "xml files (*.xml)", "xml");
         chooser.setFileFilter(xmlfilter);
         chooser.setDialogTitle("Open Trace file");
         int val = chooser.showOpenDialog(null);
         if(val == JFileChooser.APPROVE_OPTION){
        		DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
        		DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
        		
        		// If the trace xml is encrypted, prompt for a password
			if(isUseEncryptedFile()){
				if(!chooser.getSelectedFile().getPath().contains("Encrypted")){
		        	JOptionPane.showMessageDialog(null,"The selected Trace File is not Encrypted!","Invalid Trace File",JOptionPane.ERROR_MESSAGE);
		        	return false;
				}
			JPasswordField pwd = new JPasswordField(10);  
			int action = JOptionPane.showConfirmDialog(null, pwd,"Enter Password",JOptionPane.OK_CANCEL_OPTION);
	        if(action != 0){
	        	JOptionPane.showMessageDialog(null,"Please enter password to proceed","Auth Error",JOptionPane.ERROR_MESSAGE);
	        	return false;
	        }
	        if(RSAUtils.compareHash(new String(pwd.getPassword())))
	        	doc = docBuilder.parse(RSAUtils.decryptData(new File(chooser.getSelectedFile().getPath())));
			}
         
			else{
				if(chooser.getSelectedFile().getPath().contains("Encrypted")){
		        	JOptionPane.showMessageDialog(null,"The selected Trace File is Encrypted!","Invalid Trace File",JOptionPane.ERROR_MESSAGE);
		        	return false;
				}
				doc = docBuilder.parse(new File(chooser.getSelectedFile().getPath()));
			}
			
		final List<Node> elementList = new ArrayList<Node>();
		final Node details = doc.getElementsByTagName("details").item(0);
		// normalize text representation
		doc.getDocumentElement ().normalize ();
		NodeList children = details.getChildNodes();
		Object data[] = new Object[2];
		List<Object[]> dataList  = new ArrayList<Object[]>();
		int time = 0;
		for(int i = 0;i <children.getLength() ;i++){
			Node n = children.item(i);
			if(n.getNodeType() == Node.ELEMENT_NODE){
				if(n.getNodeName().equals("drawDfa")){
					data = new Object[2];
					data[0] = "Draw DFA Window";
					data[1] =((Element)n).getElementsByTagName("time").item(0).getTextContent();
					time+=Integer.parseInt((String)data[1]);
					dataList.add(data);
					elementList.add(n);
					NodeList DFAchildren = n.getChildNodes();
					for(int j = 0;j <DFAchildren.getLength() ;j++){
						Node dfaChild = DFAchildren.item(j);
						if(dfaChild.getNodeType() == Node.ELEMENT_NODE &&
								elements.contains(dfaChild.getNodeName())){
								if(dfaChild.getNodeName().equals("testAnswer")){
									elementList.add(dfaChild);
									data = new Object[2];
									data[0] = "DFA was Tested";
									data[1] = "";
									dataList.add(data);

									NodeList testAnswerchildren = dfaChild.getChildNodes();
									for(int k = 0; k<testAnswerchildren.getLength() ;k++){
										Node testAnswerChild = testAnswerchildren.item(k);
										if(testAnswerChild.getNodeType() == Node.ELEMENT_NODE &&
												elements.contains(testAnswerChild.getNodeName())){
										
											data = new Object[2];
											if(testAnswerChild.getNodeName().equals("acceptClicked"))
												data[0] = "Strings to be accepted ";
											if(testAnswerChild.getNodeName().equals("rejectClicked"))
												data[0] = "Strings to be rejected ";
											data[1] = "";
											dataList.add(data);

											elementList.add(testAnswerChild);
										}
										else if(testAnswerChild.getNodeName().equals("result") &&
												testAnswerChild.getTextContent().toString().contains("NFA"))
											data[0] = "NFA was Tested"; 
									}
								}
								else{
									elementList.add(dfaChild);
									data = new Object[2];
									data[0] = dfaChild.getNodeName();
									data[1] = "";
									dataList.add(data);

								}
						}
					}
				}
				else if(elements.contains(n.getNodeName())){
					data = new Object[2];
						dataList.add(data);
						
					if(n.getNodeName().equals("ViewStrings")){
						data[0] = "View Possible Strings";
						data[1] = "";
						if(!n.getParentNode().getNodeName().equals("drawDfa")){
							data[1] =((Element)n).getElementsByTagName("time").item(0).getTextContent();
							time+=Integer.parseInt((String)data[1]);
						}
						
					}
					if(n.getNodeName().equals("testInput")){
						data[0] = "Test Input String";
						data[1] = "";
						if(!n.getParentNode().getNodeName().equals("drawDfa")){
							data[1] =((Element)n).getElementsByTagName("time").item(0).getTextContent();
							time+=Integer.parseInt((String)data[1]);
						}

					}
					if(n.getNodeName().equals("showAnswer")){
						data[0] = "Show Me The Answer";
						data[1] = "";
						if(!n.getParentNode().getNodeName().equals("drawDfa")){
								data[1] =((Element)n).getElementsByTagName("time").item(0).getTextContent();
								time+=Integer.parseInt((String)data[1]);
						}
					}
					if(n.getNodeName().equals("exit")){
						String scenario = n.getTextContent();
						data[0] = "Exit : " + scenario;
						data[1] = "";
					}
						elementList.add(n);
				}
			}

		}
		Object rowData[][] = new Object[dataList.size()][];
		int rowcount=0;
		for(Object[] listData : dataList){
			rowData[rowcount++] = listData;
		}
		
		// Code to fetch the Question title
		String question=null;
		Node qType = details.getChildNodes().item(0);
		if(qType.getNodeName().equals("qType"))
			question = qType.getTextContent();
		if(question.contains("RegExp"))
			question = "Regular Expression : " + question.replaceAll("RegExp", "").trim();
		else{
			question = question.replaceAll("E","").trim();
			int number = (Integer.parseInt(question));
			try{
				File f = new File("DFA_LIBRARY" + System.getProperty("file.separator") + number + ".txt");
				StringBuilder stringBuilder = new StringBuilder();
				FileReader reader = new FileReader(f);
				BufferedReader br = new BufferedReader(reader);
				for(String line = br.readLine(); line != null; line = br.readLine()) {
				    stringBuilder.append(line + "\n");
				}
			br.close();
			 question = "Question " + number + " :\n " + stringBuilder.toString();
			}
			catch(Exception e){
			}
	

			
		}
		int totalTime = Integer.parseInt(((Element)details).getElementsByTagName("totalTime").item(0).getTextContent());
		int qReadTime = Integer.parseInt(((Element)details).getElementsByTagName("qReadTime").item(0).getTextContent());
		createTable(elementList,rowData,question,time,totalTime,qReadTime);

		System.out.println ("Root element of the doc is " + doc.getDocumentElement().getNodeName());
		 
		start.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				start.setEnabled(false);
				count = 0;
				if(elementList.size() > 1)
				next.setEnabled(true); 
				tableHighLightCount = count;
				table.repaint();
				tableFrame.repaint();
			    table.setModel(table.getModel());  
				useElement(false,elementList,count);
			
		        
	}
		});
		next.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				
				back.setEnabled(true);
				fromBack = false;
				tableHighLightCount = count;
				table.repaint();
				table.requestFocus();
				useElement(false,elementList,count);
		}
		});
	
		back.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
			
				if(count-2 < 0){
					count = 0;
					back.setEnabled(false);
					tableHighLightCount = count;
					table.repaint();
					return;
				}
				count-=2;
				fromBack = true;
				if(!next.isEnabled())
					next.setEnabled(true);
				tableHighLightCount = count;
				table.repaint();
				useElement(false,elementList,count);
		}
		});

		tableFrame.addWindowListener(new WindowAdapter() {
	          @Override
	          public void windowClosing(WindowEvent event) {
	        	  if(timer != null){
	        		  timer.stop();
	        		  timer = null;
	        	  }
	        	  
	        	  count = -1;
	          		NewAction.showNew();
	          }
	      });
}
      
         }
		
		catch(Exception e){
			System.out.println(e);
			JOptionPane.showMessageDialog(null, "Error Opening Tace File", "Trace Error", JOptionPane.ERROR_MESSAGE);
			return false;
		}
		return true;
	}
	

	
	public void useElement(boolean fromAutoFlow, List<Node> details, int localCount){
		closeFrames();
		Node children = details.get(localCount);
		details.get(localCount);
		boolean found = false;
		if(children.getNodeType() == Node.ELEMENT_NODE){
			Element child = (Element)children;
			if(child.getNodeName().equals("ViewStrings")){
				if(!child.getParentNode().getNodeName().equals("drawDfa") &&
						parentDFAFrame != null){
					parentDFAFrame.dispose();
				}
				found = true;
				if(viewPossibleStringsFrame != null)
					viewPossibleStringsFrame.dispose();
				viewPossibleStringsFrame = new JFrame();
				viewPossibleStringsFrame.setSize(400, 300);
	    		viewPossibleStringsFrame.setTitle("Strings in the Language");
	    		viewPossibleStringsFrame.setLocationRelativeTo(null);
	    		String viewStrings = child.getTextContent().toString().replaceAll(",","\n");
	    		JTextArea ta1 = new JTextArea(20, 60);  
	    		ta1.setText(viewStrings);
	    		ta1.setEditable(false);  
	    		ta1.select(0,0);
	    		JScrollPane js = new JScrollPane(ta1);
	    		viewPossibleStringsFrame.add(js);
	    		viewPossibleStringsFrame.setVisible(true);
			}
			else if(child.getNodeName().equals("testInput")){
				if(!child.getParentNode().getNodeName().equals("drawDfa") &&
						parentDFAFrame != null){
					parentDFAFrame.dispose();
				}
				found = true;
				String originalStr = child.getElementsByTagName("string").item(0).getTextContent().toString();
				String str = "";
				if(fromAutoFlow){
					if(originalStr.contains("Rejected"))
						str = "    The String \"" + originalStr.replaceAll("Rejected", "").trim() + "\" was Rejected.";
					else
						str = "     The String \"" + originalStr.replaceAll("Accepted", "").trim() + "\" was Accepted .";
					
					if(viewPossibleStringsFrame != null)
						viewPossibleStringsFrame.dispose();
					viewPossibleStringsFrame = new JFrame();
					JPanel jp = new JPanel(new BorderLayout());
					jp.add(new JLabel(str),BorderLayout.CENTER);
					viewPossibleStringsFrame.add(jp);
					viewPossibleStringsFrame.setSize(400,100);
					viewPossibleStringsFrame.setLocationRelativeTo(null);
					viewPossibleStringsFrame.setTitle("Test Input String");
					viewPossibleStringsFrame.setVisible(true);
				}
				else{
					if(originalStr != "" && originalStr != null){
						Element automatonElement = (Element)((Element)doc.getElementsByTagName("details").item(0)).getElementsByTagName("automaton").item(0);
						String states = ((Element)automatonElement).getElementsByTagName("States").item(0).getTextContent().toString();
						String trans = ((Element)automatonElement).getElementsByTagName("Transitions").item(0).getTextContent().toString();
						FiniteStateAutomaton fsa = prepareFSA(states,trans);
						AutomatonEnvironment env= new AutomatonEnvironment(fsa);
	
						if(originalStr.contains("Rejected"))
							str = originalStr.replaceAll("Rejected", "").trim() + " was Rejected. Try Another String";
						else
							str = originalStr.replaceAll("Accepted", "").trim() + " was Accepted . Try Another String";
								
						String input = JOptionPane.showInputDialog(null,str,"Test Input String",JOptionPane.NO_OPTION);
							if (input != null) {
								Configuration[] configs = null;
								AutomatonSimulator as = SimulatorFactory
										.getSimulator(fsa);
								configs = as.getInitialConfigurations(input);
								List associated = new ArrayList();
	
							
								int result = CustomUtilities.handleInput(env,fsa, as, configs,
										input, associated);
								if (result == 0) {
									JOptionPane
											.showMessageDialog(null,
													"Your Input will be accepted by the automaton");
								} else {
									JOptionPane
											.showMessageDialog(null,
													"Your Input will be Rejected by the automaton");
								}
	
							}
						
		      
		
		        	}
				}
			}
			else if(child.getNodeName().equals("drawDfa")){
				found = true;
				if(!child.getParentNode().getNodeName().equals("drawDfa") &&
						parentDFAFrame!=null){
					parentDFAFrame.dispose();
				}
				FiniteStateAutomaton fsa = null;
				NodeList testAnswerList = child.getElementsByTagName("testAnswer");
				if(testAnswerList.getLength() > 0){
					
					Node firstTestAnswer = testAnswerList.item(0);

					if ( ((Element) firstTestAnswer)
							.getElementsByTagName("States").getLength() == 0) {
				
					} else {
						String states = ((Element) firstTestAnswer)
								.getElementsByTagName("States").item(0)
								.getTextContent().toString();
						String trans = ((Element) firstTestAnswer)
								.getElementsByTagName("Transitions").item(0)
								.getTextContent().toString();
				
						
						fsa = prepareFSA(states, trans);
						parentDFAFrame = NewAction.createCustomWindow(fsa);
						parentDFAFrame.setLocation(500, 50);
					}
				
			}
			
			
				
				else {
					if (((Element) child).getElementsByTagName("States")
							.getLength() == 0) {
						
						fsa = new FiniteStateAutomaton();
					} else {
						String states = ((Element) child)
								.getElementsByTagName("States").item(0)
								.getTextContent();
						Node transitions = ((Element) child)
								.getElementsByTagName("Transitions").item(0); 
						String trans = null;
						fsa = new FiniteStateAutomaton();
						if (transitions != null)
							trans = transitions.getTextContent().toString();
						fsa = prepareFSA(states, trans);
					}
					parentDFAFrame = NewAction.createCustomWindow(fsa);
					parentDFAFrame.setLocation(500,50);
				}

		 }
			else if(child.getNodeName().equals("testAnswer")){
				
				found = true;
				String result = ((Element)child).getElementsByTagName("result").item(0).getTextContent().toString();
				if(parentDFAFrame != null)
					parentDFAFrame.dispose();
					inTestAnswerElement = true;
					String states = ((Element)child).getElementsByTagName("States").item(0).getTextContent().toString();
					String trans = ((Element)child).getElementsByTagName("Transitions").item(0).getTextContent().toString();
					fsa = prepareFSA(states,trans);
					parentDFAFrame =  NewAction.createCustomWindow(fsa);
					parentDFAFrame.setLocation(500, 50);
				if(result.contains("Incorrect"))
				 prepareStringsAndDisplay(fsa,fromAutoFlow);
				
				 
			}
			else if(child.getNodeName().equals("acceptClicked")){
				String states = ((Element)child.getParentNode()).getElementsByTagName("States").item(0).getTextContent().toString();
				String trans = ((Element)child.getParentNode()).getElementsByTagName("Transitions").item(0).getTextContent().toString();
				 fsa = prepareFSA(states,trans);
				acceptStrings =  prepareStrings(fsa).get(0);
				
				found= true;
				JTextArea jt = new JTextArea(acceptStrings);
				jt.setSize(20, 60);
				if(fromAutoFlow)
					createStringFrame(new JScrollPane(jt),"Strings to be Accepted",new Dimension(400,300));
				else
					JOptionPane.showMessageDialog(parentDFAFrame,new JScrollPane(jt),"Strings to be Accepted",JOptionPane.NO_OPTION);
			
					 
					 
			}
			else if(child.getNodeName().equals("rejectClicked")){
				String states = ((Element)child.getParentNode()).getElementsByTagName("States").item(0).getTextContent().toString();
				String trans = ((Element)child.getParentNode()).getElementsByTagName("Transitions").item(0).getTextContent().toString();
				 fsa = prepareFSA(states,trans);
				 rejectStrings =  prepareStrings(fsa).get(1);
				found= true;
				JTextArea jt = new JTextArea(rejectStrings);
				jt.setSize(20, 60);
				if(fromAutoFlow)
					createStringFrame(new JScrollPane(jt), "Strings to be Rejected",new Dimension(400,300));
				else
				JOptionPane.showMessageDialog(parentDFAFrame,new JScrollPane(jt),"Strings to be Rejected",JOptionPane.NO_OPTION);

			}
			else if(child.getNodeName().equals("showAnswer")){
				found= true;
				Element automatonElement = (Element)((Element)doc.getElementsByTagName("details").item(0)).getElementsByTagName("automaton").item(0);
				String states = ((Element)automatonElement).getElementsByTagName("States").item(0).getTextContent().toString();
				String trans = ((Element)automatonElement).getElementsByTagName("Transitions").item(0).getTextContent().toString();
				FiniteStateAutomaton openedAutomaton = prepareFSA(states,trans);
				if(!child.getParentNode().getNodeName().equals("drawDfa") &&
						parentDFAFrame != null){
					parentDFAFrame.dispose();
				}
				tableFrame.requestFocusInWindow();
				showMeAnswerFrame =  NewAction.createCustomWindow((FiniteStateAutomaton)openedAutomaton);
				JTabbedPane jt = (JTabbedPane) showMeAnswerFrame.getEnvironment().getComponent(0);
				showMeAnswerFrame.getEnvironment().remove(jt.getComponent(0));
				openedAutomaton.fromShowAnswer = true;
				showMeAnswerFrame.getEnvironment().add(new CustomEditorPane(openedAutomaton), "Editor",new EnvironmentFactory.EditorPermanentTag());
				showMeAnswerFrame.setTitle("DFA Solution");
				showMeAnswerFrame.setLocation(500, 50);
				showMeAnswerFrame.setAlwaysOnTop(true);
		
			}
			
			else if(child.getNodeName().equals("exit")){

				if(showMeAnswerFrame != null)
				showMeAnswerFrame.dispose();
				if(parentDFAFrame != null)
					parentDFAFrame.dispose();
						
			}
		
		count++;
		if(count >= details.size()){
			next.setEnabled(false);
			
		} else if(!found)
			useElement(false, details, count);
		
	}
	}
	
	public FiniteStateAutomaton prepareFSA(String states, String trans){
		FiniteStateAutomaton fsa = new FiniteStateAutomaton();
		if(states.trim().length() > 0){
			String[] stdet = states.split(",");
			int count = 0;
			State[] automatonStates = new State[stdet.length];
			for(String stateDet : stdet){
			String[] state = stateDet.split(" ");				
	 		automatonStates[count++] = fsa.createState(new Point(Integer.parseInt(state[1]),Integer.parseInt(state[2])));
				if(state.length == 5){
					fsa.setInitialState(automatonStates[count-1]);
					fsa.addFinalState(automatonStates[count-1]);
				}
				else if(state.length == 4){
					if(state[3].equals("Initial")){
						fsa.setInitialState(automatonStates[count-1]);
					}
					else
						fsa.addFinalState(automatonStates[count-1]);
				}
			}
		
		if(trans != null){
		String[] transDet = trans.split(",");
		for(String t : transDet){
			if(!(t.trim().equals(""))){
			String[] transition = t.split(" ");
			FSATransition ft = new FSATransition(automatonStates[Integer.parseInt(transition[0])], automatonStates[Integer.parseInt(transition[2])], transition[1]);
			fsa.addTransition(ft);
			}
		}
		}
	}
		
		return fsa;
	}

	
	
	private void prepareStringsAndDisplay(FiniteStateAutomaton aut, final boolean fromAutoFlow){
		
		List<String> list  = prepareStrings(aut);
		JPanel jp = new JPanel();
		JButton jb1 = new JButton("Accept These Strings");
		JButton jb2 = new JButton("Reject These Strings");
		jp.setLayout(new GridBagLayout());
		String acceptTheseStrings = list.get(0);
		String rejectTheseStrings = list.get(1);
		if(acceptTheseStrings == "\u00F8" || acceptTheseStrings == "" || acceptTheseStrings.length() == 0){
			jb1.setEnabled(false);
		}
		
		
		if(rejectTheseStrings == "\u00F8" || rejectTheseStrings == "" || rejectTheseStrings.length() == 0){
			jb2.setEnabled(false);
		}
		
	
		
		GridBagConstraints gc = new GridBagConstraints();
	
       	JPanel main = new JPanel();
       	main.setLayout(new GridLayout(2,1));
       	main.add(new JLabel("This is not the Right Answer.Please Try Again!"));
		gc = new GridBagConstraints();
		gc.gridx=0;
		gc.gridy=0;
       	gc.insets = new Insets(5,0,0,0); 

		jp.add(jb1,gc);
		gc = new GridBagConstraints();
		gc.gridx=1;
		gc.gridy=0;

       	gc.insets = new Insets(5,5,0,0); 
		jp.add(jb2,gc);
		 acceptStrings = acceptTheseStrings;
		 rejectStrings = rejectTheseStrings;
		jb1.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
			
				JTextArea jt = new JTextArea(acceptStrings);
				jt.setSize(20, 60);
				if(fromAutoFlow)
					createStringFrame(new JScrollPane(jt), "Strings to be Accepted",new Dimension(400,300));
				else				
				JOptionPane.showMessageDialog(null,new JScrollPane(jt));

				
				
			}
		});
		
		jb2.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
							
			
				JTextArea jt = new JTextArea(rejectStrings);
				jt.setSize(20, 60);
				if(fromAutoFlow)
					createStringFrame(new JScrollPane(jt) , "Strings to be Rejected",new Dimension(400,300));
				else	
					JOptionPane.showMessageDialog(null,new JScrollPane(jt));


			}
		});
		main.add(jp);
		if(fromAutoFlow)
			createStringFrame(main, "DFA was tested", new Dimension(400,200));
		else
			JOptionPane.showMessageDialog(parentDFAFrame, main,null,JOptionPane.ERROR_MESSAGE);


      }
	
	private String prepareRE(Automaton automaton) {
		
		String computedRE = "";
		ConvertFSAToREActionNew.computedRE = "";
		try {
			CustomUtilities.checkAndApplyLamdaTransition(automaton);
			computedRE = "";
			ConvertFSAToREActionNew.customConvertAction = true;
			new CustomConvertPane(new AutomatonEnvironment(
					automaton));
			ConvertFSAToREActionNew.customConvertAction = false;
			
		} catch (Exception e) {
			ConvertFSAToREActionNew.customConvertAction = false;
			if (ConvertFSAToREActionNew.computedRE == "") {
			return null;
			}
			computedRE = ConvertFSAToREActionNew.computedRE;
		}
		computedRE = ConvertFSAToREActionNew.computedRE;
		return computedRE;
	}
	
	private void closeFrames(){
		if(showMeAnswerFrame != null)
			showMeAnswerFrame.dispose();
		if(viewPossibleStringsFrame !=null)
			viewPossibleStringsFrame.dispose();
	}
	
	private void refreshConfig(){
		fsa = null;
		parentDFAFrame=showMeAnswerFrame=null;
		inTestAnswerElement = false;
		viewPossibleStringsFrame = new JFrame();
		tableFrame = null;
		next = new JButton("Next");
		start = new JButton("Start");
		back = new JButton("Back");
				
	}

	
	private void createTable(final List<Node> elementList, Object[][] rowData, String question, int time, int totalTime , int qReadTime){
		Object columnNames[] = { "Event", "Time Taken(Seconds)"};
		table = new JTable(rowData,columnNames){
		      public boolean isCellEditable(int rowIndex, int vColIndex) {
		          return false;
		        }
		      };
		TableSearchRenderer renderer = new TableSearchRenderer();
        table.setDefaultRenderer(Object.class, renderer);
		//table = new JTable();
		
		JScrollPane scrollPane = new JScrollPane(table);
		JLabel ques = new JLabel(question + System.getProperty("line.separator"));
		JLabel total = new JLabel("Total Time : " + totalTime +(totalTime > 1? " Seconds" : "Second"));
		JLabel qReadtime = new JLabel("Question Read Time : " + qReadTime + (qReadTime > 1? " Seconds" : "Second"));
		
		JButton answer = new JButton("View Solution");
		answer.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				Element automatonElement = (Element)((Element)doc.getElementsByTagName("details").item(0)).getElementsByTagName("automaton").item(0);
				String states = automatonElement.getElementsByTagName("States").item(0).getTextContent().toString();
				String trans = automatonElement.getElementsByTagName("Transitions").item(0).getTextContent().toString();
				FiniteStateAutomaton openedAutomaton = prepareFSA(states,trans);
				EnvironmentFrame answerFrame = NewAction.createCustomWindow(openedAutomaton);
				JTabbedPane jt = (JTabbedPane) answerFrame.getEnvironment().getComponent(0);
				answerFrame.getEnvironment().remove(jt.getComponent(0));
				openedAutomaton.fromShowAnswer = true;
				answerFrame.getEnvironment().add(new CustomEditorPane(openedAutomaton), "Editor",new EnvironmentFactory.EditorPermanentTag());
				answerFrame.setTitle("DFA Solution");
				answerFrame.setLocation(500, 50);
				answerFrame.addWindowListener(new WindowAdapter() {
			          @Override
			          public void windowClosing(WindowEvent event) {
			        	NewAction.hideNew();
			        	}
			      });
			}
		});
		
		
	JPanel pan=new JPanel(new BorderLayout());
	JPanel timePanel = new JPanel(new BorderLayout());
    Border lowerEtched = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
	timePanel.setBorder(BorderFactory.createTitledBorder(lowerEtched,"Details"));
	timePanel.add(total,BorderLayout.NORTH);
	timePanel.add(qReadtime,BorderLayout.CENTER);
	timePanel.add(answer,BorderLayout.EAST);
	pan.add(new JLabel("\n\n"));
	pan.add(new JLabel("\n\n"));
	pan.add(new JLabel("\n\n"));
	
	JButton runFlow = new JButton("Automatically Visualize Flow");
	runFlow.addActionListener(new ActionListener() {
		
		@Override
		public void actionPerformed(ActionEvent arg0) {
			count = 0;
			start.setEnabled(false);
			next.setEnabled(false);
			back.setEnabled(false);
			table.repaint();
			actionToPerform = new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					if(count >= elementList.size()){
						timer.stop();
						count = -1;
						start.setEnabled(true);
						table.repaint();
						return;
					}
					
					if(count >=0)
					fromBack = false;
					tableHighLightCount = count;
					table.repaint();
					table.requestFocus();
					useElement(true,elementList,count);					
				}
			};
			timer = new Timer(5000, actionToPerform);
			timer.setInitialDelay(500);
			if(!timer.isRunning())
			timer.start();
			 
			
		}
	});
	
	
	
	
	pan.add(timePanel,BorderLayout.NORTH);
	pan.add(new JLabel("\n\n\n\n"));
		pan.add(scrollPane,BorderLayout.SOUTH);
		JPanel pan1 = new JPanel();
		pan1.add(start);
		pan1.add(next);
		pan1.add(back);
		next.setEnabled(false);
		back.setEnabled(false);
		start.setEnabled(true);
		tableFrame=new JFrame();
		tableFrame.setVisible(true);
		JPanel quesPanel = new JPanel(new BorderLayout());
		quesPanel.add(new JLabel("\n\n"),BorderLayout.NORTH);
		quesPanel.add(runFlow,BorderLayout.WEST);
		runFlow.setSize(300,100);
		
		
		quesPanel.add(ques,BorderLayout.SOUTH);
		quesPanel.add(new JLabel("\n\n"));

		
		tableFrame.add(quesPanel, BorderLayout.NORTH);
		tableFrame.add(pan,BorderLayout.CENTER); 
		tableFrame.add(pan1,BorderLayout.SOUTH); 

		tableFrame.pack();
		
		
       
	}
	private List<String> prepareStrings(FiniteStateAutomaton aut){
		List<String> list = new ArrayList<String>();
		Element automatonElement = (Element)((Element)doc.getElementsByTagName("details").item(0)).getElementsByTagName("automaton").item(0);
		String states = ((Element)automatonElement).getElementsByTagName("States").item(0).getTextContent().toString();
		String trans = ((Element)automatonElement).getElementsByTagName("Transitions").item(0).getTextContent().toString();
		FiniteStateAutomaton openedAutomaton = prepareFSA(states,trans);
		FiniteStateAutomaton userAutomaton = aut;
		final Map<String,String> testAnswerFlow = new HashMap<String,String>(); 
		testAnswerFlow.put("Result", "Incorrect");

		 String acceptTheseStrings = "";
		 String rejectTheseStrings = "";
		String originalRE = prepareRE((FiniteStateAutomaton)openedAutomaton.clone());
		String userRE = prepareRE((Automaton)userAutomaton.clone());

		
		List<String> originalPossibleStrings = CustomUtilities.getXegerList(originalRE);
		List<String> userPossibleStrings = CustomUtilities.getXegerList(userRE);
		
		for(String str : originalPossibleStrings){
			AutomatonEnvironment env= new AutomatonEnvironment(aut);
			if(CustomUtilities.checkInputString(env,str) != 0){
				acceptTheseStrings += (str + "\n");
			}
		}
		
		AutomatonEnvironment answerEnvironment = new AutomatonEnvironment(openedAutomaton);
		for(String str : userPossibleStrings){

			if(CustomUtilities.checkInputString(answerEnvironment, str) != 0){
				if(!str.equals("\u00F8"))
				rejectTheseStrings += (str + "\n");
			}
		}
		list.add(acceptTheseStrings);
		list.add(rejectTheseStrings);
		return list;
	}
	
	private class TableSearchRenderer extends DefaultTableCellRenderer {

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            setBackground(null);
            Component tableCellRendererComponent = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            if(tableHighLightCount == row){
            	setBackground(Color.green);
            }
            return tableCellRendererComponent;
        }
    }
	
	private void createStringFrame(Component jt, String title, Dimension d){
		if(viewPossibleStringsFrame != null)
			viewPossibleStringsFrame.dispose();
		viewPossibleStringsFrame = new JFrame();
		viewPossibleStringsFrame.add(new JScrollPane(jt));
		viewPossibleStringsFrame.setSize(d);
		viewPossibleStringsFrame.setTitle(title);
		viewPossibleStringsFrame.setVisible(true);	
		viewPossibleStringsFrame.setLocation(500,50);
	}

	public boolean isUseEncryptedFile() {
		return useEncryptedFile;
	}
	
	public void setUseEncryptedFile(boolean useEncryptedFile) {
		this.useEncryptedFile = useEncryptedFile;
	}

}