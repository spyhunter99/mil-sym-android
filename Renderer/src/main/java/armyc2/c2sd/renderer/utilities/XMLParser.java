package armyc2.c2sd.renderer.utilities;

import java.io.IOException;
import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import android.util.Log;

public class XMLParser {

	  /**
	   * from www.androidhive.info/2011/11/android-xml-parsing-tutorial/
	   * @param xml
	   * @return
	   */
	  public static Document getDomElement(String xml)
	  {
		  Document doc = null;
		  DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		  try
		  {
			  DocumentBuilder db = dbf.newDocumentBuilder();
			  InputSource is = new InputSource();
			  is.setCharacterStream(new StringReader(xml));
			  doc = db.parse(is);
		  }
		  catch(ParserConfigurationException e)
		  {
			  Log.e("Error: ", e.getMessage());
			  return null;
		  }
		  catch(SAXException e)
		  {
			  Log.e("Error: ", e.getMessage());
			  return null;
		  }
		  catch(IOException e)
		  {
			  Log.e("Error: ", e.getMessage());
			  return null;
		  }
		  return doc;
	  }
	  
	  public static final String getElementValue(Node elem)
	  {
		  Node child;
		  if(elem != null)
			  if(elem.hasChildNodes()){
				  for(child = elem.getFirstChild(); child != null; child = child.getNextSibling()){
					  if(child.getNodeType() == Node.TEXT_NODE){
						  return child.getNodeValue();
					  }
				  }
			  }
		  return "";
	  }
	  
	  public static final String getAttributeValue(Node node, String str)
	  {
		  Element elem = null; 
		  if(node != null)
			  elem = (Element)node;
			  if(elem.hasAttribute(str)){
				  return elem.getAttribute(str);
			  }
		  return "";
	  }
	  
	  public static String getValue(Element item, String str)
	  {
		  NodeList n = item.getElementsByTagName(str);
		  return getElementValue(n.item(0));
	  }
	  
	  /**
	   * Get the attribute from an element.  If not present returns ""
	   * @param elem
	   * @param attribName
	   * @return
	   */
	  public static String getAttribute(Element elem, String attribName)
	  {
		  if(elem != null && elem.hasAttribute(attribName))
			  return elem.getAttribute(attribName);
		  else
			  return "";
	  }
}
