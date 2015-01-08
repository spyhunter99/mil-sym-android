/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package armyc2.c2sd.renderer.utilities;

/*import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.*;
import org.xml.sax.SAXException;
import java.io.*;
import java.util.HashMap;
import java.util.Map;*/
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.FileHandler;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import android.util.Log;
/**
 * Responsible for loading tactical graphic symbol definitions into a hash table.
 *
 * @author michael.spinelli
 */
@SuppressWarnings("unused")
public class SymbolSVGTable {


	private static Boolean _initCalled = false;
    private static SymbolSVGTable _instance = null;
    //private static SymbolTableThingy
    private static Map<String, SVGPath> _SymbolDefinitions = null;

    
    


    /*
     * Holds SymbolDefs for all symbols.  (basicSymbolID, Description,
     * MinPoint, MaxPoints, etc...)
     * Call getInstance().
     *
     * */
    private SymbolSVGTable()
    {

    }

    public static synchronized SymbolSVGTable getInstance()
    {
        if(_instance == null)
            _instance = new SymbolSVGTable();

        return _instance;
    }

    public synchronized void init(String symbolSVG)
    {
  	  if(_initCalled==false)
  	  {
  		_instance = new SymbolSVGTable();
          _SymbolDefinitions = new HashMap<String, SVGPath>();
    
     
  	    
  	    String lookupXml = symbolSVG;//FileHandler.InputStreamToString(xmlStreamB);
  	    
  	    //String lookupXml = FileHandler.fileToString("C:\\UnitFontMappings.xml");
  	    populateLookup(lookupXml);
  	    
  	    
  	    _initCalled = true;
  	  }
    }

  private void populateLookup(String xml)
  {
	  try
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
				  index = index.substring(3);
				  path = new SVGPath(index, strPath);
				  index = path.getID();
				  _SymbolDefinitions.put(index, path);
			  }
			  
		  }
	  }
	  catch(Exception exc)
	  {
		  Log.e("SymbolSVGTable", exc.getMessage(), exc);
	  }

  }

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
            if (_SymbolDefinitions.containsKey(index))
                returnVal = new SVGPath(_SymbolDefinitions.get(index));
        }
        catch (Exception exc)
        {
            
        }
        return returnVal;
    }
    
    public Boolean HasSVGPath(String index)
    {
        if (index != null && index.length() > 0)
            return _SymbolDefinitions.containsKey(index);
        else
            return false;
    }

}
