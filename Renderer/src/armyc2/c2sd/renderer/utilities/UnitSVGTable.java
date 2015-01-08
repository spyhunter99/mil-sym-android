/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package armyc2.c2sd.renderer.utilities;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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

/**
 *
 * @author michael.spinelli
 */
public class UnitSVGTable {


	private static Boolean _initCalled = false;
    private static UnitSVGTable _instance = null;
    //private static SymbolTableThingy
    private static Map<String, SVGPath> _UnitDefinitions = null;

    private UnitSVGTable()
    {
        
    }

    public static synchronized UnitSVGTable getInstance()
    {
      if(_instance == null)
          _instance = new UnitSVGTable();

      return _instance;
    }



    /**
     * must be called first
     */
  public synchronized void init(String unitSVG)
  {
	  if(_initCalled==false)
	  {
		_instance = new UnitSVGTable();
        _UnitDefinitions = new HashMap<String, SVGPath>();
  
   
	    
	    String lookupXml = unitSVG.replace("&#x", "");//FileHandler.InputStreamToString(xmlStreamB);
	    
	    //String lookupXml = FileHandler.fileToString("C:\\UnitFontMappings.xml");
	    populateLookup(lookupXml);
	    
	    
	    _initCalled = true;
	  }
  }
  

  private void populateLookup(String xml)
  {
	  
	  Document doc = XMLParser.getDomElement(xml);
	  NodeList nl = doc.getElementsByTagName("glyph");
	  SVGPath path;
	  for(int i = 0; i < nl.getLength(); i++)
	  {
		  String index = XMLParser.getAttribute((Element)nl.item(i), "unicode");
		  String strPath = XMLParser.getAttribute((Element)nl.item(i), "d");
		  
		  if(strPath != null && strPath.equals("") != true && index != null && index.length() > 3 )
		  {
			  index = index.replace("&#x", "");
			  index = index.replace(";", "");
			  path = new SVGPath(index, strPath);
			  index = path.getID();
			  _UnitDefinitions.put(index, path);
		  }
		  
	  }  
  }//end populateLookup

  /**
   * @name getSymbolDef
   *
   * @description Returns a SymbolDef from the SymbolDefTable that matches the passed in Symbol Id
   *
   * @param index String representation of the index number
   * @return SVGPath
   */
    public SVGPath getSVGPath(String index)
    {
    	SVGPath returnVal = null;
        try
        {
            if (_UnitDefinitions.containsKey(index))
                returnVal = new SVGPath(_UnitDefinitions.get(index));
        }
        catch (Exception exc)
        {
            
        }
        return returnVal;
    }
    
    public Boolean HasSVGPath(String index)
    {
        if (index != null && index.length() > 0)
            return _UnitDefinitions.containsKey(index);
        else
            return false;
    }

}
