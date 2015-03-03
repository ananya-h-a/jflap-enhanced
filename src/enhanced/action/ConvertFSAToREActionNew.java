/*
 * This is a custom class to Convert an Automaton to RE which uses Lambda Transitions to 
 * Convert an FSA to RE
 */

package enhanced.action;

import enhanced.CustomUtilities;
import enhanced.RegexLibrary;
import enhanced.RegexParser;
import gui.action.MultipleSimulateAction;
import gui.action.NewAction;
import gui.environment.AutomatonEnvironment;
import gui.environment.EnvironmentFactory;
import gui.environment.EnvironmentFrame;
import gui.environment.Universe;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import enhanced.editor.CustomEditorPane;
import enhanced.regular.CustomConvertPane;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import automata.AutomatonSimulator;
import automata.Configuration;
import automata.SimulatorFactory;
import automata.fsa.FiniteStateAutomaton;

/**
 * This action handles the conversion of an FSA to a regular expression. It also
 * handles the Practice flow to display the quiz window and it prepares the
 * trace flow xml.
 * 
 * 
 */

public class ConvertFSAToREActionNew {
	/** The automaton environment. */
	public static String computedRE = "";
	static String speaktext;
	public boolean fromSearchForProblemsFlow;
	public static boolean customConvertAction = false;
	private int questionIndex = -1;
	public static String TEST_INPUT = "TestInput";
	public static String DRAW_DFA = "DrawDFA";
	public static String VIEW_POSSIBLE_STRINGS = "ViewPossibleStrings";
	public static String SHOW_ANSWER = "ShowAnswer";
	//private boolean timeToRead;
	//private int timeTakenToRead;
	//private long startTime = 0;
	public boolean correctAnswer;
	private String exitScenario = "WindowClosed";
	public static boolean correctAnswerDrawn;
	public static Map<String, Integer> hintsUsed = new HashMap<String, Integer>();
	public static int totalClicks;
	public Document doc;
	public Document visualDoc;
	public Element rootElement;
	private EnvironmentFrame drawDFAFrame;
	private EnvironmentFrame showMeAnswerFrame;
	private JFrame viewPossibleStringsFrame;
	public JFrame questionFrame;
	private WindowAdapter dwa;
	private WindowAdapter sma;
	private WindowAdapter vps;
	List<Element> elementsDuringDrawDFA;
	private List flowFrames = new ArrayList();
	private FiniteStateAutomaton fsa;
	private fromFlow fromFlow;
	private boolean logMySession = true;
	private boolean logMyEncryptedSession;
	private String log = "";
	public boolean fromScheduledQuiz;

	Transformer transformer;

	public static boolean isCorrectAnswerDrawn() {
		return correctAnswerDrawn;
	}

	public static void setCorrectAnswerDrawn(boolean correctAnswerDrawn) {
		ConvertFSAToREActionNew.correctAnswerDrawn = correctAnswerDrawn;
	}

	public String getExitScenario() {
		return exitScenario;
	}

	public void setExitScenario(String exitScenario) {
		this.exitScenario = exitScenario;
	}

	final Map<String, Double> durationMap = new HashMap<String, Double>();
	final Map<String, Integer> clickCountMap = new HashMap<String, Integer>();
	final List flowList = new ArrayList();

	public int getQuestionIndex() {
		return questionIndex;
	}

	public void setQuestionIndex(int questionIndex) {
		this.questionIndex = questionIndex;
	}

	public static enum fromFlow {
		ENGLISHDESCRIPTION, REGULAR_EXPRESSION // for future use
												// SEARCHFORPROBLEMS,
												// SCHEDULEDQUIZ
	}

	/**
	 * Instantiates a new <CODE>ConvertFSAToREAction</CODE>.
	 * 
	 * @param environment
	 *            the environment
	 */
	public ConvertFSAToREActionNew(AutomatonEnvironment environment) {
		this.environment = environment;
		try {
			TransformerFactory transformerFactory = TransformerFactory
					.newInstance();
			transformer = transformerFactory.newTransformer();
		} catch (TransformerException te) {

		}
	}

	/*
	 * This method handles the display of quiz window based on the practice
	 * flow. It prepares the quiz window and also attaches action listener to
	 * each of the buttons.
	 */
	public boolean displayData(final fromFlow fromwWhichFlow,
			final String selectedValue, String DFAText) {
		elementsDuringDrawDFA = new ArrayList<Element>();
		totalClicks = 0;
		hintsUsed = new HashMap<String, Integer>();
		correctAnswerDrawn = false;
		clickCountMap.put(TEST_INPUT, 0);
		clickCountMap.put(VIEW_POSSIBLE_STRINGS, 0);
		clickCountMap.put(DRAW_DFA, 0);
		
		durationMap.put(TEST_INPUT, 0.0);
		durationMap.put(VIEW_POSSIBLE_STRINGS, 0.0);
		durationMap.put(DRAW_DFA, 0.0);

		JFrame frame = Universe.frameForEnvironment(environment);

		if (environment.getAutomaton().getInitialState() == null) {
			return false;
		}

		if (environment.getAutomaton().getFinalStates().length == 0) {
			return false;
		}

		String text = "";
		String endResult = "";
		
		// FOr "Random Regular Expression" practice flow we don't need to convert the automaton to RE. The RE text is already present in
		//"DFAText" methos argument which wa sent to "displayData"
		if (!(fromwWhichFlow == fromFlow.REGULAR_EXPRESSION)) {
			try {
				CustomUtilities.checkAndApplyLamdaTransition(environment
						.getAutomaton());
				computedRE = "";
				customConvertAction = true;
				new CustomConvertPane(environment);
				customConvertAction = false;

			} catch (Exception e) {
				customConvertAction = false;

				if (computedRE == "") {

					OpenFileFromRepositoryAction.openedAutomaton = null;
					return false;
				}

			}
			RegexParser.finalResult = "";
			RegexParser.t = new TreeSet<Character>();
			RegexParser.parse(computedRE, 0);

			String desc = "This "
					+ environment.getAutomaton().getStates().length
					+ " - State DFA ";
			if (RegexLibrary.h.get(computedRE) != null) {
				desc = RegexLibrary.h.get(computedRE);
			} else {
				desc += RegexParser.appendInitialString() + "\n\n"
						+ RegexParser.finalResult;
			}

			if (DFAText != null) {
				if (!(fromwWhichFlow == fromFlow.REGULAR_EXPRESSION)) {
					if (getQuestionIndex() > 0)
						desc = "\n Question " + getQuestionIndex() + " : \n\n"
								+ DFAText;
					else
						desc = "\n" + DFAText;
					text += desc + "\n";
					if (computedRE != "")
						text += "(Hint : " + computedRE + ")";
				} else {
					text = DFAText;
				}

			}

			else {
				text += "Regular Expression :\n" + computedRE;
			}
		}

		else {
			text = DFAText;
		}
		endResult = CustomUtilities.getXegerString(computedRE);

		try 
		{
			DocumentBuilderFactory docFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

			doc = docBuilder.newDocument();
			rootElement = doc.createElement("details");
			doc.appendChild(rootElement);

			//Element qType = doc.createElement("qType");
			if (fromwWhichFlow == fromFlow.ENGLISHDESCRIPTION
					&& getQuestionIndex() != -1) 
			{
				log += "<Question><Type>EnglishDescription</Type><Index>"+getQuestionIndex()+"</Index></Question>\n";
				//qType.appendChild(doc.createTextNode("E" + getQuestionIndex()));
			} else 
			{
				//qType.appendChild(doc.createTextNode("RegExp " + computedRE));
				log += "<Question><Type>Regex</Type><Regex>"+computedRE+"</Regex></Question>\n";
			}
			//rootElement.appendChild(qType);
		} 
		catch (Exception e) 
		{
			System.out.println(e.toString());
		}
		final String result = endResult;
		questionFrame = new JFrame();
		questionFrame.setSize(825, 575);
		JButton drawDFAButton = new JButton("Draw the DFA");
		JButton showMeAnswerButton = new JButton("Show me the Answer");
		JButton viewPossibleStringsButton = new JButton("View Possible Strings");
		showMeAnswerButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (showMeAnswerFrame != null && showMeAnswerFrame.isShowing()) 
				{
					JOptionPane.showMessageDialog(null,
							"You have already Opened the solution DFA",
							"Window Opened", JOptionPane.ERROR_MESSAGE);
					showMeAnswerFrame.toFront();
					return;
				}
				if (flowFrames.contains(SHOW_ANSWER)) {
					flowFrames.remove(SHOW_ANSWER);
				}
				flowFrames.add(SHOW_ANSWER);

				if (hintsUsed.get(SHOW_ANSWER) != null) {
					int clickCount = hintsUsed.get(SHOW_ANSWER);
					hintsUsed.put(SHOW_ANSWER, ++clickCount);
				} else
					hintsUsed.put(SHOW_ANSWER, 1);
				if (!isCorrectAnswerDrawn()) {
					setExitScenario("ShowMeTheAnswer");
				}

				updateTimeToRead();
				final List showAnswerList = new ArrayList();
				showAnswerList.add(SHOW_ANSWER);
				showAnswerList.add(0);
				showAnswerList.add(0.0);
				final long start = System.currentTimeMillis();

				OpenFileFromRepositoryAction.openedAutomaton.fromShowAnswer = true;
				showMeAnswerFrame = NewAction
						.createCustomWindow(OpenFileFromRepositoryAction.openedAutomaton);

				JTabbedPane jt = (JTabbedPane) showMeAnswerFrame
						.getEnvironment().getComponent(0);
				showMeAnswerFrame.getEnvironment().remove(jt.getComponent(0));
				showMeAnswerFrame.getEnvironment().add(
						new CustomEditorPane(
								OpenFileFromRepositoryAction.openedAutomaton),
						"Editor", new EnvironmentFactory.EditorPermanentTag());
				showMeAnswerFrame.setTitle("DFA Solution");
				List<Component> comps = getAllComponents(showMeAnswerFrame
						.getContentPane());
				for (Component comp : comps) {

					comp.addMouseListener(new java.awt.event.MouseAdapter() {
						public void mouseClicked(java.awt.event.MouseEvent evt) {
							// clickCountMap.put(DRAW_DFA,(Integer)clickCountMap.get(DRAW_DFA)+1);
							totalClicks++;
							int clickCount = (Integer) showAnswerList.get(1);
							clickCount++;
							showAnswerList.set(1, clickCount);

						}
					});

				}

				sma = new WindowAdapter() {
					@Override
					public void windowClosing(WindowEvent event) {
						flowFrames.remove(SHOW_ANSWER);
						long elapsedTime = System.currentTimeMillis() - start;
						float elapsedTimesec = Math
								.round(elapsedTime / (1000F));
						// durationMap.put(DRAW_DFA,(Double)durationMap.get(DRAW_DFA)+elapsedTimesec);
						int clickCount = (Integer) showAnswerList.get(1);
						showAnswerList.set(1, "Clicks-"
								+ ((Integer) showAnswerList.get(1)).toString());

						showAnswerList.set(2, elapsedTimesec + " Seconds");

						flowList.add(showAnswerList);
						Element showMeAnswer = doc.createElement("showAnswer");
						Element clicks = doc.createElement("clicks");
						Element time = doc.createElement("time");
						clicks.appendChild(doc.createTextNode(new Integer(
								clickCount).toString()));
						time.appendChild(doc.createTextNode(new Integer(
								(int) elapsedTimesec).toString()));
						showMeAnswer.appendChild(clicks);
						showMeAnswer.appendChild(time);
						if ((drawDFAFrame == null)
								|| (drawDFAFrame != null && !drawDFAFrame
										.isShowing())) {
							rootElement.appendChild(showMeAnswer);
						} else {
							if (fsa != null) {
								addTestdDfaElements(elementsDuringDrawDFA);
								fsa.flowMap.clear();

							}
							elementsDuringDrawDFA.add(showMeAnswer);
						}

					}
				};

				showMeAnswerFrame.addWindowListener(sma);

			}
		});
		drawDFAButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (drawDFAFrame != null && drawDFAFrame.isShowing()) {

					JOptionPane.showMessageDialog(null,
							"You have already Opened a window to draw the DFA",
							"Window Opened", JOptionPane.ERROR_MESSAGE);
					drawDFAFrame.toFront();
					return;
				}

				if (flowFrames.contains(DRAW_DFA)) {
					flowFrames.remove(DRAW_DFA);
				}
				flowFrames.add(DRAW_DFA);

				final long start = System.currentTimeMillis();
				log+="<drawDFA>\n<startTime>"+System.currentTimeMillis()+"</startTime>\n";
				updateTimeToRead();
				final List dfaList = new ArrayList();
				dfaList.add(DRAW_DFA);
				dfaList.add(0);
				dfaList.add(0.0);

				fsa = new automata.fsa.FiniteStateAutomaton();
				fsa.cfrReference = ConvertFSAToREActionNew.this;
				fsa.startTime = start;
				fsa.fromOriginalAnswer = true;
				drawDFAFrame = NewAction.createCustomWindow(fsa);
				AutomatonEnvironment temp = (AutomatonEnvironment)drawDFAFrame.getEnvironment();
				temp.setLog();
				List<Component> comps = getAllComponents(drawDFAFrame
						.getContentPane());
				for (Component comp : comps) {

					comp.addMouseListener(new java.awt.event.MouseAdapter() {
						public void mouseClicked(java.awt.event.MouseEvent evt) {
							totalClicks++;
							int clickCount = (Integer) dfaList.get(1);
							clickCount++;
							dfaList.set(1, clickCount);

						}
					});

				}
				dwa = new WindowAdapter() {
					@Override
					public void windowClosing(WindowEvent event) {
					
						flowFrames.remove(DRAW_DFA);
						long elapsedTime = System.currentTimeMillis() - start;
						float elapsedTimesec = elapsedTime / (1000F);
						Element drawDfa = doc.createElement("drawDfa");
						Element clicks = doc.createElement("clicks");
						Element time = doc.createElement("time");
						AutomatonEnvironment env =(AutomatonEnvironment) drawDFAFrame.getEnvironment();
						log+=env.getLog();
						clicks.appendChild(doc
								.createTextNode(((Integer) dfaList.get(1))
										.toString()));
						time.appendChild(doc.createTextNode(new Integer(
								(int) elapsedTimesec).toString()));

						if (fsa.nfaDrawn) {
							Element nfaDrawn = doc.createElement("nfaDrawn");
							nfaDrawn.appendChild(doc.createTextNode("true"));
							drawDfa.appendChild(nfaDrawn);
							dfaList.add("NFA was Drawn");
						}
						for (String s : fsa.flowList)
							dfaList.add(s);

						System.out.println("flowlist" + fsa.flowList.toString());

						drawDfa.appendChild(clicks);
						drawDfa.appendChild(time);
						for (Element element : elementsDuringDrawDFA) {
							drawDfa.appendChild(element);
						}
						elementsDuringDrawDFA.clear();
						List<Element> testdfaElements = new ArrayList<Element>();
						addTestdDfaElements(testdfaElements);
						for (Element e : testdfaElements)
							drawDfa.appendChild(e);

						if (fsa != null && testdfaElements.size() == 0)
							CustomUtilities.prepareAutomatonString(doc,
									drawDfa, fsa);
						rootElement.appendChild(drawDfa);
						if (correctAnswerDrawn
								&& getExitScenario() == "WindowClosed") {
							setExitScenario("CorrectAnswer");
						}
						flowList.add(dfaList);
						log += fsa.log;
						fsa = null;
						log += "<closeTime>"+System.currentTimeMillis()+"</closeTime>\n</drawDFA>\n";

					}
				};
				drawDFAFrame.addWindowListener(dwa);
		

			}
		});
		JButton testInputButton = new JButton("Test Input String");
		testInputButton.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				
				log += "<testInput>\n<TS>"+System.currentTimeMillis()+"</TS>";
				
				/*final long start = System.currentTimeMillis();
				//totalClicks++;
				//if (hintsUsed.get(TEST_INPUT) != null) {
				//	int clickCount = hintsUsed.get(TEST_INPUT);
				//	hintsUsed.put(TEST_INPUT, ++clickCount);
				} else
					hintsUsed.put(TEST_INPUT, 1);
				//updateTimeToRead();
				final List testInputList = new ArrayList();
				testInputList.add(TEST_INPUT);
				clickCountMap.put(TEST_INPUT,
						(Integer) clickCountMap.get(TEST_INPUT) + 1);*/
				String input = JOptionPane.showInputDialog(null,
						"Enter the Input String", "Test Input String",
						JOptionPane.NO_OPTION);
				log +="<input>"+input+"</input>";
				
				if (input != "" && input != null) 
				{
					Configuration[] configs = null;
					AutomatonSimulator as = SimulatorFactory
							.getSimulator(OpenFileFromRepositoryAction.openedAutomaton);
					configs = as.getInitialConfigurations(input);
					List associated = new ArrayList();
					try 
					{
						MultipleSimulateAction ms = new MultipleSimulateAction(
								OpenFileFromRepositoryAction.openedAutomaton,
								environment);
						int result = CustomUtilities.handleInput(environment,
								OpenFileFromRepositoryAction.openedAutomaton,
								as, configs, input, associated);
						log +="<result>"+result+"</result>\n</testInput>\n";
						Element testInput = doc.createElement("testInput");
						Element inputStr = doc.createElement("string");
						
						if (result == 0) 
						{
							JOptionPane
									.showMessageDialog(null,
											"Your Input will be accepted by the automaton");
							/*if (input.length() == 0) 
							{
								testInputList
										.add("The Blank String was Accepted");
								inputStr.appendChild(doc
										.createTextNode("Blank String Accepted"));
							} 
							else 
							{
								testInputList.add("The String \"" + input
										+ "\" was Accepted");
								inputStr.appendChild(doc.createTextNode(input
										+ " Accepted"));

							}*/
						} 
						else 
						{
							JOptionPane
									.showMessageDialog(null,
											"Your Input will be Rejected by the automaton");
							/*if (input.length() == 0) {
								testInputList
										.add("The Blank String was Rejected");
								inputStr.appendChild(doc
										.createTextNode("Blank String Rejected"));
							} 
							else 
							{
								testInputList.add("The String \"" + input
										+ "\" was Rejected");
								inputStr.appendChild(doc.createTextNode(input
										+ " Rejected"));

							}*/

						}
						/*testInput.appendChild(inputStr);
						//long elapsedTime = System.currentTimeMillis() - start;
						//float elapsedTimesec = elapsedTime / (1000F);
						Element time = doc.createElement("time");
						testInput.appendChild(time);
						//time.appendChild(doc.createTextNode(new Integer(
						//		(int) elapsedTimesec).toString()));
						if ((drawDFAFrame == null)
								|| (drawDFAFrame != null && !drawDFAFrame
										.isShowing())) {
							rootElement.appendChild(testInput);
						} else {
							if (fsa != null) {
								addTestdDfaElements(elementsDuringDrawDFA);
								fsa.flowMap.clear();
							}
							elementsDuringDrawDFA.add(testInput);
						}*/
					} catch (Exception ex) {
						System.out.println(ex.getStackTrace());
					}
					//flowList.add(testInputList);

				}
				else
				{
					log += "</testInput>\n";
				}
			}
		});

		viewPossibleStringsButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e)
			{
				if (viewPossibleStringsFrame != null
						&& viewPossibleStringsFrame.isShowing())
				{

					JOptionPane
							.showMessageDialog(
									null,
									"You have already Opened the window with the possible strings",
									"Window Opened", JOptionPane.ERROR_MESSAGE);
					viewPossibleStringsFrame.toFront();
					return;
				}
				log += "<viewPossibleStrings>\n<TS>"+System.currentTimeMillis()+"</TS>\n</viewPossibleStrings>\n";
				if (flowFrames.contains(VIEW_POSSIBLE_STRINGS))
				{
					flowFrames.remove(VIEW_POSSIBLE_STRINGS);
				}
				flowFrames.add(VIEW_POSSIBLE_STRINGS);

				/*if (hintsUsed.get(VIEW_POSSIBLE_STRINGS) != null) {
					int clickCount = hintsUsed.get(VIEW_POSSIBLE_STRINGS);
					hintsUsed.put(VIEW_POSSIBLE_STRINGS, ++clickCount);
				} else
					hintsUsed.put(VIEW_POSSIBLE_STRINGS, 1);

				updateTimeToRead();*/
				final List viewPossibleStringsList = new ArrayList();
				viewPossibleStringsList.add(VIEW_POSSIBLE_STRINGS);
				viewPossibleStringsList.add(0);
				viewPossibleStringsList.add(0.0);
				final long start = System.currentTimeMillis();
				JTextArea ta1 = new JTextArea(20, 60);
				ta1.setText(result);
				ta1.setEditable(false);
				ta1.select(0, 0);
				JScrollPane js = new JScrollPane(ta1);
				/*ta1.addMouseListener(new java.awt.event.MouseAdapter() {
					public void mouseClicked(java.awt.event.MouseEvent evt) {
						totalClicks++;
						int clickCount = (Integer) viewPossibleStringsList
								.get(1);
						clickCount++;
						viewPossibleStringsList.set(1, clickCount);
					}
				});*/
				viewPossibleStringsFrame = new JFrame();
				viewPossibleStringsFrame.setSize(400, 300);
				viewPossibleStringsFrame.setTitle("Strings in the Language");
				viewPossibleStringsFrame.setLocationRelativeTo(null);
			
				viewPossibleStringsFrame.add(js);

				vps = new WindowAdapter() {

					@Override
					public void windowClosing(WindowEvent event) {
						flowFrames.remove(VIEW_POSSIBLE_STRINGS);
						long elapsedTime = System.currentTimeMillis() - start;
						int elapsedTimeSec = (int) (elapsedTime / (1000F));
						Integer clickCount = (Integer) viewPossibleStringsList
								.get(1);
						String strings = result.trim().replace('\n', ',');
						Element ViewStrings = doc.createElement("ViewStrings");
						Element possibleStrings = doc.createElement("strings");
						Element time = doc.createElement("time");
						possibleStrings.appendChild(doc.createTextNode(strings));
						time.appendChild(doc
								.createTextNode(((Integer) elapsedTimeSec)
										.toString()));
						ViewStrings.appendChild(possibleStrings);
						ViewStrings.appendChild(time);
						if ((drawDFAFrame == null)
								|| (drawDFAFrame != null && !drawDFAFrame
										.isShowing())) {
							rootElement.appendChild(ViewStrings);
						} else {
							if (fsa != null) {
								addTestdDfaElements(elementsDuringDrawDFA);
								fsa.flowMap.clear();
							}
							elementsDuringDrawDFA.add(ViewStrings);
						}
					}
				};

				viewPossibleStringsFrame.addWindowListener(vps);
				viewPossibleStringsFrame.show();
			}
		});
		JButton refresh = null;
		try {

			refresh = new JButton((new ImageIcon(getClass().getResource(
					"/enhanced/refresh.gif"))));
		} catch (Exception e) {
			refresh = new JButton();
		}

		refresh.setToolTipText("Show Me Another Problem");
		refresh.setPreferredSize(new Dimension(50, 50));
		refresh.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				updateTimeToRead();
				if (!isCorrectAnswerDrawn()
						&& getExitScenario() == "WindowClosed")
					setExitScenario("Refresh Problem");
				if (fromwWhichFlow == fromFlow.ENGLISHDESCRIPTION) {
					performCloseOperation(fromwWhichFlow);
					questionFrame.dispose();

					new TestDFAAction().actionPerformed(null);
				}

				if (fromwWhichFlow == fromFlow.REGULAR_EXPRESSION) {
					performCloseOperation(fromwWhichFlow);
					questionFrame.dispose();
					Object[] possibleValues = { "Easy", "Medium", "Hard" };
					PracticeRandomRegularExpressionAction.generateRegex(selectedValue, possibleValues);
				}

			}

		});

		JPanel p = new JPanel();

		p.setLayout(new GridBagLayout());
		GridBagConstraints gc = new GridBagConstraints();
		gc.gridx = 0;
		gc.gridheight = 2;
		gc.fill = GridBagConstraints.VERTICAL;
		p.add(drawDFAButton, gc);

		gc = new GridBagConstraints();
		gc.gridx = 1;
		gc.fill = GridBagConstraints.VERTICAL;
		gc.insets = new Insets(0, 5, 5, 0);

		p.add(viewPossibleStringsButton, gc);
		gc = new GridBagConstraints();
		gc.gridx = 1;
		gc.fill = GridBagConstraints.HORIZONTAL;
		gc.insets = new Insets(0, 5, 0, 0);

		p.add(testInputButton, gc);

		gc = new GridBagConstraints();
		gc.gridx = 2;
		gc.gridheight = 2;
		gc.fill = GridBagConstraints.VERTICAL;
		gc.insets = new Insets(0, 5, 0, 0);

		//p.add(refresh, gc);

		gc = new GridBagConstraints();
		gc.gridx = 0;
		gc.gridy = 2;
		gc.gridwidth = 3;
		gc.insets = new Insets(5, 0, 0, 0);
		gc.fill = GridBagConstraints.HORIZONTAL;
		//p.add(showMeAnswerButton, gc);

		final JCheckBox logSession = new JCheckBox("Log Session");
		final JCheckBox logEncryptedSession = new JCheckBox(
				"Log Encrypted Session");

		logSession.setSelected(true);
		logSession.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (logSession.isSelected()) {
					logMySession = true;
					logEncryptedSession.setSelected(false);
				} else
					logMySession = false;
			}
		});

		logEncryptedSession.setSelected(false);
		logEncryptedSession.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (logEncryptedSession.isSelected()) {
					logMyEncryptedSession = true;
					logMySession = false;
					logSession.setSelected(false);
				} else
					logMyEncryptedSession = false;
			}
		});

		gc = new GridBagConstraints();
		gc.gridx = 3;
		gc.gridy = 1;
		gc.insets = new Insets(0, 30, 0, 0);

		p.add(logSession, gc);

		gc = new GridBagConstraints();
		gc.gridx = 3;
		gc.gridy = 2;
		gc.insets = new Insets(0, 90, 0, 0);

		//p.add(logEncryptedSession, gc);

		gc = new GridBagConstraints();
		gc.gridx = 4;
		gc.gridy = 1;
		gc.gridwidth = 2;
		gc.insets = new Insets(5, 50, 0, 0);
		JButton toolFeedBack = new JButton("Feedback");

		toolFeedBack.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				String downloadURL = "https://docs.google.com/forms/d/1KR_-GfhpPYzInlYhGJaCKEFd4ke8pr4qDW8Vxf4SZb8/viewform";
				java.awt.Desktop myNewBrowserDesktop = java.awt.Desktop
						.getDesktop();

				try {
					java.net.URI myNewLocation = new java.net.URI(downloadURL);
					myNewBrowserDesktop.browse(myNewLocation);

				} catch (Exception e) {
					JOptionPane
							.showMessageDialog(null,
									"FeedBack Form Could Not Be Opened. Please Try Again Later.");
				}

			}

		});

		p.add(toolFeedBack, gc);
		gc = new GridBagConstraints();
		gc.gridx = 4;
		gc.gridy = 2;
		gc.gridwidth = 2;
		JButton feedBack = new JButton("Question Feedback");
		gc.insets = new Insets(5, 75, 0, 0);
		feedBack.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				String downloadURL = "https://docs.google.com/forms/d/15ZsNbWNZDizL4ZmijNbVgjl_ih8umPYaxYwxR0Ob2Oo/viewform?usp=send_form";
				java.awt.Desktop myNewBrowserDesktop = java.awt.Desktop
						.getDesktop();

				try {
					java.net.URI myNewLocation = new java.net.URI(downloadURL);
					myNewBrowserDesktop.browse(myNewLocation);

				} catch (Exception e) {
					JOptionPane
							.showMessageDialog(null,
									"FeedBack Form Could Not Be Opened. Please Try Again Later.");
				}

			}
		});
		//p.add(feedBack, gc);

		JPanel main = new JPanel();
		main.add(p);

		JTextArea ta = new JTextArea(20, 60);
		ta.setText(text);
		ta.setFont(new Font("Dialog", Font.PLAIN, 15));
		JPanel p2 = new JPanel();
		ta.select(0, 0);
		ta.setEditable(false);
		ta.setLineWrap(true);
		JScrollPane jp = new JScrollPane(ta);
		p2.add(jp);
		main.add(p2);
		main.setLayout(new FlowLayout(FlowLayout.LEFT));
		questionFrame.add(main);
		questionFrame.setTitle("Practice");
		questionFrame.setLocationRelativeTo(null); // center
		questionFrame.setVisible(true);
		//timeToRead = false;
		//startTime = System.currentTimeMillis();
		log+= "<windowstarttime>"+System.currentTimeMillis()+"</windowstarttime>\n";

		questionFrame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent event) {
				performCloseOperation(null);

			}
		});

		return true;

	}

	private AutomatonEnvironment environment;

	/*
	 * This method is called when the quiz window is closed or when the quiz is
	 * answered correctly.
	 */
	public boolean performCloseOperation(fromFlow fromFlow) {
		try {
			List flowList = Arrays.asList(flowFrames.toArray());
			for (Object s : flowList) {
				String flow = (String) s;
				if (flow == DRAW_DFA) {
					if (drawDFAFrame != null && drawDFAFrame.isShowing()) {
						dwa.windowClosing(null);
						drawDFAFrame.close();
					}
				}

				if (flow == SHOW_ANSWER) {

					if (showMeAnswerFrame != null
							&& showMeAnswerFrame.isShowing()) {
						sma.windowClosing(null);
						showMeAnswerFrame.dispose();

					}
				}

				if (flow == VIEW_POSSIBLE_STRINGS) {
					if (viewPossibleStringsFrame != null
							&& viewPossibleStringsFrame.isShowing()) {
						vps.windowClosing(null);
						viewPossibleStringsFrame.dispose();
					}
				}
			}

			/*
			 * The following code is used to log the session in a trace xml. A
			 * new mxl file with the date and time will be saved in the same
			 * folder as the jar as soon as the quiz window is exited.
			 */

			if (logMySession || logMyEncryptedSession) {

				//long elapsedTime = System.currentTimeMillis() - startTime;
				//Integer totalTimeTaken = (int) (elapsedTime / (1000F));
				Element totalTime = doc.createElement("totalTime");
				//totalTime.appendChild(doc.createTextNode(totalTimeTaken
					//	.toString()));

				Element qReadTime = doc.createElement("qReadTime");
				//qReadTime.appendChild(doc.createTextNode(new Integer(
					//	timeTakenToRead).toString()));

				Element exit = doc.createElement("exit");
				exit.appendChild(doc.createTextNode(getExitScenario()));
				rootElement.appendChild(exit);

				Node nextChild = rootElement.getFirstChild().getNextSibling();

				rootElement.insertBefore(totalTime, nextChild);
				rootElement.insertBefore(qReadTime, totalTime);

				Element automaton = doc.createElement("automaton");
				CustomUtilities.prepareAutomatonString(doc, automaton,
						OpenFileFromRepositoryAction.openedAutomaton);
				rootElement.appendChild(automaton);

				DOMSource source = new DOMSource(doc);
				StringWriter sw = new StringWriter();
				StreamResult result = new StreamResult(sw);
				transformer.transform(source, result);
				String xmlString = sw.toString();

				if (logMyEncryptedSession)
					RSAUtils.encryptData(xmlString);
				else {

					String timeLog = new SimpleDateFormat("ddMMMyy_HH_mm_ss")
							.format(Calendar.getInstance().getTime());
					File file = new File(timeLog + ".xml");
					BufferedWriter bw = new BufferedWriter(
							new OutputStreamWriter(new FileOutputStream(file)));
					//bw.write(xmlString);
					log+="<windowclosingtime>"+System.currentTimeMillis()+"</windowclosingtime>\n";
					log = "<root>"+log+"</root>";
					bw.write(log);
					bw.flush();
					bw.close();
				}

			}
		}

		catch (Exception ex) {
			System.out.println(ex);
		}
		OpenFileFromRepositoryAction.openedAutomaton = null;
		return true;

	}

	public static class t implements Comparator<String> {
		public int compare(String a, String b) {
			return a.length() - b.length();

		}
	}

	public static List<Component> getAllComponents(final Container c) {
		Component[] comps = c.getComponents();
		List<Component> compList = new ArrayList<Component>();
		for (Component comp : comps) {
			compList.add(comp);
			if (comp instanceof Container)
				compList.addAll(getAllComponents((Container) comp));
		}
		return compList;
	}

	private void updateTimeToRead() {
		
		//if (!timeToRead) {
			//long elapsedTime = System.currentTimeMillis() - startTime;
			//int elapsedTimeSecs = (int) (elapsedTime / (1000F));
			//timeToRead = true;
			//timeTakenToRead = elapsedTimeSecs;
		//}
	}

	public void addTestdDfaElements(List<Element> parent) {
		for (Map<String, String> testMap : fsa.flowMap) {
			Element testDfa = doc.createElement("testAnswer");
			Element result = doc.createElement("result");

			result.appendChild(doc.createTextNode(testMap.get("Result")));
			testDfa.appendChild(result);
			if (testMap.get("Time") != null) {
				Element timeToAnswer = doc.createElement("timeToAnswer");
				timeToAnswer
						.appendChild(doc.createTextNode(testMap.get("Time")));
				testDfa.appendChild(timeToAnswer);
			}
			if (testMap.get("Accept") != null) {
				Element acceptStr = doc.createElement("shouldAccept");
				String accept = testMap.get("Accept").trim().replace("\n", ",");
				acceptStr.appendChild(doc.createTextNode(accept));
				testDfa.appendChild(acceptStr);
			}
			if (testMap.get("Reject") != null) {
				Element rejectStr = doc.createElement("shouldReject");
				String reject = testMap.get("Reject").trim().replace("\n", ",");
				rejectStr.appendChild(doc.createTextNode(reject));
				testDfa.appendChild(rejectStr);
			}
			if (testMap.get("AcceptStrings") != null) {
				Element acceptClicked = doc.createElement("acceptClicked");
				acceptClicked.appendChild(doc.createTextNode(testMap
						.get("AcceptStrings")));
				testDfa.appendChild(acceptClicked);

			}
			if (testMap.get("RejectStrings") != null) {
				Element rejectClicked = doc.createElement("rejectClicked");
				rejectClicked.appendChild(doc.createTextNode(testMap
						.get("RejectStrings")));
				testDfa.appendChild(rejectClicked);

			}

			if (testMap.get("States") != null) {
				Element States = doc.createElement("States");
				States.appendChild(doc.createTextNode(testMap.get("States")));
				testDfa.appendChild(States);
			}

			if (testMap.get("Transitions") != null) {
				Element transitions = doc.createElement("Transitions");
				transitions.appendChild(doc.createTextNode(testMap
						.get("Transitions")));
				testDfa.appendChild(transitions);
			}
			parent.add(testDfa);
		}
	}

}
