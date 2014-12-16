/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package armyc2.c2sd.renderer.utilities;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

/**
 *
 * @author michael.spinelli
 */
public class TacticalGraphicLookup {
    
    private Map<String,Integer> symbolMap = new HashMap<String, Integer>();

    private static TacticalGraphicLookup _instance = null;
    
    private TacticalGraphicLookup() 
    {

    }
    
    public static synchronized TacticalGraphicLookup getInstance()
    {
        if(_instance == null)
        {
        _instance = new TacticalGraphicLookup();
        }
        return _instance;
    }
    
    /**
    * @name init
    *
    * @desc Simply calls xmlLoaded
    *
    * @return None
    */
    public synchronized void init(String xml)
    {
        
        populateLookup(xml);
    }
    
      
  
    /**
   * @name populateLookup
   *
   * @desc
   *
   * @param xml - IN -
   * @return None
   */
  private void populateLookup(String xml)
  {
    ArrayList<String> al = XMLUtil.getItemList(xml, "<SYMBOL>", "</SYMBOL>");
    for(int i = 0; i < al.size(); i++)
    {
      String data = (String)al.get(i);

      String basicID = XMLUtil.parseTagValue(data, "<SYMBOLID>", "</SYMBOLID>");
      //String description = XMLUtil.parseTagValue(data, "<DESCRIPTION>", "</DESCRIPTION>");
      String mapping = XMLUtil.parseTagValue(data, "<MAPPING>", "</MAPPING>");

      symbolMap.put(basicID, Integer.valueOf(mapping));

    }
  }
  
    /**
   * given the milstd symbol code, find the font index for the symbol.
   * @param symbolCode
   * @return
   */
  public int getCharCodeFromSymbol(String symbolCode)
  {

      try
      {
    	  String basicID = symbolCode;
          if(SymbolUtilities.is3dAirspace(symbolCode)==false)
          {
              basicID = SymbolUtilities.getBasicSymbolID(symbolCode);
          }
          if(symbolMap.containsKey(basicID))
          {
        	  return symbolMap.get(basicID);
          }
          else
        	  return -1;
      }
      catch(Exception exc)
      {
          ErrorLogger.LogException("TacticalGraphicLookup", "getCharCodeFromSymbol", exc, Level.WARNING);
      }
    return -1;

  } 
    
}
