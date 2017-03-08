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
 * responsible for character index lookups for single point.
 * @author michael.spinelli
 *
 */
public class SinglePointLookup {

	 private static boolean _initCalled = false;
    private static SinglePointLookup _instance;
  private boolean _ready = false;
  private static Map<String, SinglePointLookupInfo> hashMapB = null;
  private static Map<String, SinglePointLookupInfo> hashMapC = null;

  /**
   *
   */
  public boolean getReady()
  {
    return this._ready;
  }

  private SinglePointLookup()
  {
    
  }

  /**
   *
   */
  public static synchronized SinglePointLookup getInstance()
  {
    if(_instance == null)
    {
      _instance = new SinglePointLookup();
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
  public synchronized void init(String[] singlepointLookupXML)
  {

	  if(_initCalled==false)
	  {
		  _instance = new SinglePointLookup();
		  hashMapB = new HashMap<String, SinglePointLookupInfo>();
		  hashMapC = new HashMap<String, SinglePointLookupInfo>();
	      
	      String lookupXmlB = singlepointLookupXML[0];//FileHandler.InputStreamToString(xmlStreamB);
	      String lookupXmlC = singlepointLookupXML[1];//FileHandler.InputStreamToString(xmlStreamC);
	      
	      populateLookup(lookupXmlB, RendererSettings.Symbology_2525B);
	      populateLookup(lookupXmlC, RendererSettings.Symbology_2525C);
	      _initCalled = true;
	  }
  }



  /**
   * @name populateLookup
   *
   * @desc
   *
   * @param xml - IN -
   * @return None
   */
  private static void populateLookup(String xml, int symStd)
  {
    ArrayList<String> al = XMLUtil.getItemList(xml, "<SYMBOL>", "</SYMBOL>");
    for(int i = 0; i < al.size(); i++)
    {
        String data = (String)al.get(i);

        SinglePointLookupInfo spli = null;
        String basicID = XMLUtil.parseTagValue(data, "<SYMBOLID>", "</SYMBOLID>");
        String description = XMLUtil.parseTagValue(data, "<DESCRIPTION>", "</DESCRIPTION>");
        String mappingP = XMLUtil.parseTagValue(data, "<MAPPINGP>", "</MAPPINGP>");
        String mappingA = XMLUtil.parseTagValue(data, "<MAPPINGA>", "</MAPPINGA>");
        String width = XMLUtil.parseTagValue(data, "<WIDTH>", "</WIDTH>");
        String height = XMLUtil.parseTagValue(data, "<HEIGHT>", "</HEIGHT>");
        
        mappingP = checkMappingIndex(mappingP);
        mappingA = checkMappingIndex(mappingA);

        spli = new SinglePointLookupInfo(basicID,description,mappingP,mappingA,width,height);

        if(symStd==RendererSettings.Symbology_2525B)
            hashMapB.put(basicID, spli);
        else if(symStd==RendererSettings.Symbology_2525C)
            hashMapC.put(basicID, spli);

    }
  }
  
  /**
   * Until XML files are updated, we need to shift the index
   * @param index
   * @return 
   */  
 private static String checkMappingIndex(String index)
 {
      int i = -1;
      if(SymbolUtilities.isNumber(index))
      {
          i = Integer.valueOf(index);
          
       	  return String.valueOf(i + 57000);
      }
      return index;
 }


  /**
   * given the milstd symbol code, find the font index for the symbol.
   * @param symbolCode
   * @return
   */
  public int getCharCodeFromSymbol(String symbolCode, int symStd)
  {

      try
      {
          String strSymbolLookup = symbolCode;
          
          Map<String, SinglePointLookupInfo> hashMap = null;
          
          if(symStd==RendererSettings.Symbology_2525B)
              hashMap=hashMapB;
          else if(symStd==RendererSettings.Symbology_2525C)
              hashMap=hashMapC;

          SinglePointLookupInfo spli = null;
          if(SymbolUtilities.isWeather(strSymbolLookup) || symbolCode.contains("FILL"))
          {
              spli = hashMap.get(strSymbolLookup);
              if(spli != null)
                  return spli.getMappingP();
              else
                  return -1;
                 
          }
          else
          {
              if(!hashMap.containsKey(strSymbolLookup))
                  strSymbolLookup = SymbolUtilities.getBasicSymbolID(strSymbolLookup);
              
              spli = hashMap.get(strSymbolLookup);
              if(spli != null)
              {
                  if(SymbolUtilities.getStatus(symbolCode).equals("A")==true)
                      return spli.getMappingA();
                  else
                      return spli.getMappingP();
              }    
              else
              {
                  return -1;
              }
          }
      }
      catch(Exception exc)
      {
          ErrorLogger.LogException("SinglePointLookup", "getCharCodeFromSymbol", exc, Level.WARNING);
      }
    return -1;

  }

  /**
   * Method that retrieves a reference to a SPSymbolDef object from the
   * SinglePointLookup Dictionary.
   *
   * @param strSymbolID - IN - The 15 character symbol Id.
   * @return SPSymbolDef, or null if there was an error.
   */
  /*public SPSymbolDef getSPSymbolDef(String strSymbolID)
  {
        String strGenericSymbolID = "";
        if(strSymbolID.substring(0, 1).equals("G"))
        {
            strGenericSymbolID = strSymbolID.substring(0, 1) + "*" + strSymbolID.substring(2, 12);
        }
        else
        {
            strGenericSymbolID = strSymbolID.substring(0, 10);
        }

        SPSymbolDef data = hashMap.get(strGenericSymbolID);
        return data;
  }*/
  
  /**
   * Method that retrieves a reference to a SinglePointLookupInfo object from the
   * SinglePointLookup Dictionary.
   * @param basicSymbolID
   * @return 
   */
  public SinglePointLookupInfo getSPLookupInfo(String basicSymbolID, int symStd)
  {
      SinglePointLookupInfo spli = null;
      if(symStd==RendererSettings.Symbology_2525B)
        spli = hashMapB.get(basicSymbolID);
      else if(symStd==RendererSettings.Symbology_2525C)
        spli = hashMapC.get(basicSymbolID);
      return spli;
  }

  /*
  public static void main(String[] args)
  {
    SPSymbolDef data = SinglePointLookup.instance().getSPSymbolDef("G*FPPTC---****X");
    int mapping = SinglePointLookup.instance().getCharCodeFromSymbol("G*FPPTC---****X");
    String junk = "";
  }*/
}
