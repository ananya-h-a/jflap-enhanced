package enhanced;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import java.awt.Point;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import automata.State;
import automata.StatePlacer;
import automata.fsa.FSATransition;
import automata.fsa.FiniteStateAutomaton;
public class XMLReader {
	
	private Document doc;
	private StatePlacer placer;
	private long startTime;
	private TreeMap <Long ,FiniteStateAutomaton> attemptsMap;
	public XMLReader(String path)
	{
		File fXmlFile = new File(path);
		placer = new StatePlacer();
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = null;
		try 
		{
			dBuilder = dbFactory.newDocumentBuilder();
		} 
		catch (ParserConfigurationException e)
		{
			e.printStackTrace();
		}
		try 
		{
			doc = dBuilder.parse(fXmlFile);
		} 
		catch (SAXException e) 
		{
			e.printStackTrace();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		doc.getDocumentElement().normalize();
		Element element = (Element) doc.getElementsByTagName("windowstarttime").item(0);
		startTime = Long.parseLong(element.getTextContent());
		this.setAttemptsMap();
	}
	
	public int getQuestionNumberFromXML()
	{
		int questionNumber = -1;
		Element q = (Element) doc.getElementsByTagName("Question").item(0);
		if(q.getElementsByTagName("Type").item(0).getTextContent().equals("EnglishDescription"))
		{
			questionNumber = Integer.parseInt(q.getElementsByTagName("Index").item(0).getTextContent());
		}
		return questionNumber;
	}
	
	public long getTimeStamp(Node n)
	{
		Element e = (Element) n;
		return Long.parseLong(e.getElementsByTagName("TS").item(0).getTextContent());
	}
	
	
	public List <FiniteStateAutomaton> getAutomatonsFromXML()
	{
		List <FiniteStateAutomaton> attempts = new ArrayList<FiniteStateAutomaton>();
		for(Map.Entry<Long, FiniteStateAutomaton> entry : attemptsMap.entrySet())
		{
			attempts.add(entry.getValue());
		}
		return attempts;
	}
	
	public List <Long> getTimeStamps()
	{
		List<Long> tsList = new ArrayList<Long>();
		for(Map.Entry<Long, FiniteStateAutomaton> entry : attemptsMap.entrySet())
		{
			tsList.add(entry.getKey());
		}
		return tsList;
	}
	
	private TreeMap<Long,FiniteStateAutomaton> getAttemptsMap()
	{
		return attemptsMap;
	}
	private void setAttemptsMap()
	{
		TreeMap <Long ,FiniteStateAutomaton> attemptsMap = new TreeMap<Long,FiniteStateAutomaton>();
		NodeList testAgainstSolution = doc.getElementsByTagName("TestAgainstSolution");
		NodeList testAgainstCode = doc.getElementsByTagName("TestAgainstCode");
		NodeList testAgainstGUI = doc.getElementsByTagName("TestAgainstGUI");
		for (int i = 0; i < testAgainstSolution.getLength(); i++)
		{
			Node node = testAgainstSolution.item(i);
			Element element = (Element) node;
			Node automaton  = element.getElementsByTagName("Automaton").item(0);
			int status = Integer.parseInt(element.getElementsByTagName("Status").item(0).getTextContent());
			if(status >= 0)
			{
				long ts = getTimeStamp(node);
				attemptsMap.put(ts,constructAutomaton(automaton));
			}
			else
			{
				//possible handling later (in case of NFAs maybe)
			}
			
		}
		this.attemptsMap = attemptsMap;
	}
	public FiniteStateAutomaton constructAutomaton(Node automaton)
	{
		FiniteStateAutomaton result = new FiniteStateAutomaton();
		Element fsa = (Element)automaton;
		NodeList states = fsa.getElementsByTagName("State");
		for(int i=0;i<states.getLength();i++)
		{
			Element state = (Element) states.item(i);
			int id = Integer.parseInt(state.getElementsByTagName("Id").item(0).getTextContent());
			State temp = result.createStateWithId(placer.getRandomPoint(), id);
			NodeList attributes = state.getElementsByTagName("Attribute");
			for(int j=0;j<attributes.getLength();j++)
			{
				Node attribute = attributes.item(j);
				String attr = attribute.getTextContent();
				if(attr.equals("Initial State"))
				{
					result.setInitialState(temp);
				}
				if(attr.equals("Final State"))
				{
					result.addFinalState(temp);
				}
			}
		}
		
		NodeList transitions = fsa.getElementsByTagName("Transition");
		for(int i=0; i<transitions.getLength();i++)
		{
			Element transition = (Element) transitions.item(i);
			int from = Integer.parseInt(transition.getElementsByTagName("From").item(0).getTextContent());
			int to = Integer.parseInt(transition.getElementsByTagName("To").item(0).getTextContent());
			String label = transition.getElementsByTagName("Label").item(0).getTextContent();
			State fromState = result.getStateWithID(from);
			State toState = result.getStateWithID(to);
			FSATransition newTransition = new FSATransition(fromState, toState, label);
			result.addTransition(newTransition);
			
		}
		
		return result;
	}
	public long getStartTime()
	{
		return startTime;
	}
}
