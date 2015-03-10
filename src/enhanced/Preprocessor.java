package enhanced;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.swing.JOptionPane;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;



public class Preprocessor
{
	public Preprocessor(String logpath,String repopath)
	{
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
		File folder = new File(logpath);
		File[] listOfFiles = folder.listFiles();
		for (int i = 0; i < listOfFiles.length; i++) 
		{
			if (listOfFiles[i].isFile()) 
			{
				JOptionPane.showMessageDialog(null,"Not processing "+listOfFiles[i].getName()+".The structure required is wrong. Please refer documentation" , "",JOptionPane.ERROR_MESSAGE);
		    } 
		    else if (listOfFiles[i].isDirectory()) 
		    {
		       //System.out.println("Directory " + listOfFiles[i].getName());
		    	String username = listOfFiles[i].getName();
		    	File logs = new File(listOfFiles[i].getPath());
		    	File [] traces = logs.listFiles();
		    	List <File> toProcess = new ArrayList<File>();
		    	for(int j=0;j<traces.length;j++)
		    	{
		    		if (traces[j].isFile() && traces[j].getName().contains(".xml"))
		    		{
		    			toProcess.add(traces[j]);
		    		}
		    	}
		    	toProcess.sort(new Comparator<File>()
		    	{
		    		public int compare(File a,File b)
		    		{
		    			String tsa = null;
		    			String tsb = null;
		    			if (a.getName().indexOf(".") > 0)
		    			{
		    			    tsa = a.getName().substring(0, a.getName().lastIndexOf("."));
		    			}
		    			
		    			if (b.getName().indexOf(".") > 0)
		    			{
		    			    tsb = b.getName().substring(0, b.getName().lastIndexOf("."));
		    			}
		    			SimpleDateFormat formatter = new SimpleDateFormat("ddMMMyy_HH_mm_ss");
		    			try
		    			{
							Date datea = formatter.parse(tsa);
							Date dateb = formatter.parse(tsb);
							return datea.compareTo(dateb);
						} 
		    			catch (ParseException e) 
						{
							e.printStackTrace();
						}
		    			
		    			return 0;
		    		}
				});
		    	List<Integer> solved = new ArrayList<Integer>();
		    	for(File trace : toProcess)
		    	{
		    		
		    		try 
		    		{	
		    			Document doc = dBuilder.parse(trace);
		    			Element q = (Element) doc.getElementsByTagName("Question").item(0);
		    			int questionNumber = 0;
	    				if(q.getElementsByTagName("Type").item(0).getTextContent().equals("EnglishDescription"))
	    				{
	    					questionNumber = Integer.parseInt(q.getElementsByTagName("Index").item(0).getTextContent());
	    				}
	    				System.out.println("Processing "+questionNumber+":"+username+":"+trace.getName());
	    				
	    				if(solved.contains(questionNumber))
	    				{
	    					System.out.println("Ignoring "+questionNumber+":"+username+":"+trace.getName());
	    					continue;
	    				}
		    			NodeList attempts = doc.getElementsByTagName("TestAgainstSolution");
		    			if(attempts.getLength() == 0)
		    			{
		    				writeFileToRepository(repopath,doc,username+"_"+questionNumber+"_"+trace.getName());
		    				continue;
		    			}
		    			Element finalAttempt = (Element)attempts.item(attempts.getLength()-1);
		    			int status = Integer.parseInt(finalAttempt.getElementsByTagName("Status").item(0).getTextContent());
		    			if(status > 0)
		    			{
		    				System.out.println("Adding "+questionNumber+":"+username+":"+trace.getName());
		    				solved.add(questionNumber);
		    			}
		    			writeFileToRepository(repopath,doc,username+"_"+questionNumber+"_"+trace.getName());
					} 
		    		catch (SAXException e) 
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					} 
		    		catch (IOException e)
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		    	}
		    }
		}
	}
	private void writeFileToRepository(String repoPath,Document doc,String filename)
	{
		//System.out.println(filename);
		Element username = doc.createElement("UserName");
		username.appendChild(doc.createTextNode(filename.split("_")[0]));
		doc.getDocumentElement().appendChild(username);
		DOMSource source = new DOMSource(doc);
		StringWriter sw = new StringWriter();
		StreamResult result = new StreamResult(sw);
		
		Transformer transformer = null;
		TransformerFactory transformerFactory = TransformerFactory
				.newInstance();
		try 
		{
			transformer = transformerFactory.newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.transform(source, result);
			String xmlString = sw.toString();
			File output = new File(repoPath+System.getProperty("file.separator")+filename);
			if(output.createNewFile())
			{
				//System.out.println(output.getName());
				BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(output)));
				bw.write(xmlString);
				bw.flush();
				bw.close();
			}
			
		} 
		catch (Exception e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

	}
}
