package parking.direct;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class Utility {

	public static String[][] getDirectionData(String srcLat,String srcLong, String destLat, String destLong) {

	    String urlString = "http://maps.googleapis.com/maps/api/directions/xml?origin="
	            + srcLat +","+srcLong+ "&destination=" + destLat+ "," + destLong
	            + "&sensor=true";
	    
	    Document doc = null;
	    HttpURLConnection urlConnection = null;
	    URL url = null;
	    
	   
	    try {

	        url = new URL(urlString.toString());
	        urlConnection = (HttpURLConnection) url.openConnection();
	        urlConnection.setRequestMethod("GET");
	        urlConnection.setDoOutput(true);
	        urlConnection.setDoInput(true);
	        urlConnection.connect();
	        
	        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	        DocumentBuilder db = dbf.newDocumentBuilder();
	        doc = db.parse(urlConnection.getInputStream());

	    } catch (Exception e) {
	    }
	    
	    NodeList nl = doc.getElementsByTagName("html_instructions");
	    
	    String[][] data = new String[nl.getLength()][4];
	    //[dirs][dist][lat][long]
	    for (int s = 0; s < nl.getLength(); s++) {
	        Node dirsNode = nl.item(s);
	        Node distNode = dirsNode.getNextSibling().getNextSibling();
	        NodeList dirsList = dirsNode.getChildNodes();
	        StringBuffer SBuffer = new StringBuffer();
	       // Node dirsTemp = dirsNode.getFirstChild();
	        Node temp;
	        for(int i=0; i< dirsList.getLength(); i++)
	        {
	        	temp = dirsList.item(i);
	        	SBuffer.append(temp.getNodeValue());
	        }
	        data[s][0] = SBuffer.toString();
	        //head,<,b,>
	        if (distNode != null)
	        {
	        	if(distNode.hasChildNodes())
	        	{
	        	Node distNode2 = distNode.getFirstChild().getNextSibling().getNextSibling().getNextSibling();
	        		data[s][1] = distNode2.getFirstChild().getNodeValue();
	        	}
	        }
	        else
	        {
	        	data[s][1] =  "noDist";
	        }
	        Node endLoc = dirsNode.getPreviousSibling().getPreviousSibling().getPreviousSibling().getPreviousSibling().getPreviousSibling().getPreviousSibling();
	        if (endLoc != null)
	        {
	        	Node latNode = endLoc.getFirstChild().getNextSibling();
	        	data[s][2] = latNode.getFirstChild().getNodeValue();
	        	Node longNode = latNode.getNextSibling().getNextSibling();
	        	data[s][3] = longNode.getFirstChild().getNodeValue();
	        }
	    }
	    
	    return data;
	}
	public static Document getParsedXMLDocument(String xmlUrl)
	{
	//parse xml file
    		//get the factory   		
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			Document dom = null;
			try {

				//Using factory get an instance of document builder
				DocumentBuilder db = dbf.newDocumentBuilder();

				//parse using builder to get DOM representation of the XML file
				 URL url = new URL(xmlUrl);
				 
				 dom = db.parse(url.openStream());
				 
				 
			}catch(ParserConfigurationException pce) {
				pce.printStackTrace();
			}catch(SAXException se) {
				se.printStackTrace();
			}catch(IOException ioe) {
				ioe.printStackTrace();
			}
			return dom;
	//parse document\
	}
    public static String[][] getParsedData(Document dom)
    {
    	//price, spots, lat, long
    	Element docEle = dom.getDocumentElement();	
		 NodeList nl = docEle.getElementsByTagName("entry");
		 String[][] data = new String[nl.getLength()][4];
			if(nl != null && nl.getLength() > 0) {
				for(int i = 0 ; i < nl.getLength();i++) {

					//get the name  element
					Node entry = nl.item(i);
					Node price = entry.getFirstChild().getNextSibling().getNextSibling().getNextSibling();
					data[i][0] = price.getFirstChild().getNodeValue();
					Node numSpots = price.getNextSibling().getNextSibling();
					data[i][1] = numSpots.getFirstChild().getNodeValue();
					Node lattitude = numSpots.getNextSibling().getNextSibling();
					data[i][2] = lattitude.getFirstChild().getNodeValue();
					Node longitude = lattitude.getNextSibling().getNextSibling();
					data[i][3] = longitude.getFirstChild().getNodeValue();
					//for 2.1 api
					//getTextContent is replaced gy getFirstChild().getNodeValue();
					//add it to list
					//myEmpls.add(e);
				}
			}
		//return null;
		
    	//String[][] data = {{"hi","lo"},{"le", "ri"}};
    	return data;
    }
}
