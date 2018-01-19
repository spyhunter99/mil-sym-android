/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */

package armyc2.c2sd.renderer.utilities;

import android.graphics.Typeface;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author michael.spinelli
 */
public class UnitDefTable
{

    private static Boolean _initCalled = false;
    private static UnitDefTable _instance = null;
    // private static SymbolTableThingy
    private static Map<String, UnitDef> _UnitDefinitionsB = null;
    private static ArrayList<UnitDef> _UnitDefDupsB = null;

    private static Map<String, UnitDef> _UnitDefinitionsC = null;
    private static ArrayList<UnitDef> _UnitDefDupsC = null;

    private String TAG = "UnitDefTable";

    private static String propSymbolID = "SYMBOLID";
    private static String propDrawCategory = "DRAWCATEGORY";
    private static String propModifiers = "MODIFIERS";
    private static String propDescription = "DESCRIPTION";
    private static String propHierarchy = "HIERARCHY";
    private static String propAlphaHierarchy = "ALPHAHIERARCHY";
    private static String propPath = "PATH";

    private UnitDefTable()
    {

    }

    public static synchronized UnitDefTable getInstance()
    {
        if (_instance == null)
            _instance = new UnitDefTable();

        return _instance;
    }

    /*
     * public String[] searchByHierarchy(String hierarchy) { for(UnitDef foo : _UnitDefinitions.values() ) {
     * if(foo.getHierarchy().equalsIgnoreCase(hierarchy)) { return } } }
     */

    private String getXML(String xmlName)
    {
        String xmlFolder = "res/raw/";
        String xml = null;
        Typeface tf = null;
        InputStream is = null;
        try {
            is = this.getClass().getClassLoader().getResourceAsStream(xmlFolder + xmlName);
            if (is != null) {
                InputStreamReader isr = new InputStreamReader(is);
                BufferedReader r = new BufferedReader(isr);
                StringBuilder total = new StringBuilder();
                String line;
                while ((line = r.readLine()) != null) {
                    total.append(line);
                }
                xml = total.toString();

                // cleanup
                r.close();
                isr.close();
                is.close();
                r = null;
                isr = null;
                is = null;
                total = null;
            }
        } catch (Exception exc) {
            Log.e(TAG, exc.getMessage(), exc);
        }
        return xml;
    }

    public final synchronized void init()
    {
        if (_initCalled == false) {
            String[] xml = new String[2];
            xml[0] = getXML("unitconstantsb.xml");
            xml[1] = getXML("unitconstantsc.xml");

            init(xml);
        }
    }

    /**
     * must be called first
     */
    public synchronized void init(String[] unitConstantsXML)
    {
        if (_initCalled == false) {

            _UnitDefinitionsB = new HashMap<String, UnitDef>();
            _UnitDefDupsB = new ArrayList<UnitDef>();

            _UnitDefinitionsC = new HashMap<String, UnitDef>();
            _UnitDefDupsC = new ArrayList<UnitDef>();

            String lookupXmlB = unitConstantsXML[0];// FileHandler.InputStreamToString(xmlStreamB);
            String lookupXmlC = unitConstantsXML[1];
            // FileHandler.InputStreamToString(xmlStreamC);
            // String lookupXml = FileHandler.fileToString("C:\\UnitFontMappings.xml");
            populateLookup(lookupXmlB, RendererSettings.Symbology_2525B);
            populateLookup(lookupXmlC, RendererSettings.Symbology_2525C);

            _initCalled = true;
        }
    }

    private static void populateLookup(String xml, int symStd)
    {
        UnitDef ud = null;

        Document document = XMLParser.getDomElement(xml);

        NodeList symbols = XMLUtil.getItemList(document, "SYMBOL");
        for (int i = 0; i < symbols.getLength(); i++) {
            Node node = symbols.item(i);

            String symbolID = XMLUtil.parseTagValue(node, "SYMBOLID");
            String description = XMLUtil.parseTagValue(node, "DESCRIPTION");
            description = description.replaceAll("&amp;", "&");
            String drawCategory = XMLUtil.parseTagValue(node, "DRAWCATEGORY");
            String hierarchy = XMLUtil.parseTagValue(node, "HIERARCHY");
            String alphaHierarchy = XMLUtil.parseTagValue(node, "ALPHAHIERARCHY");
            String path = XMLUtil.parseTagValue(node, "PATH");

            if (SymbolUtilities.isInstallation(symbolID))
                symbolID = symbolID.substring(0, 10) + "H****";

            int idc = 0;
            if (drawCategory != null || drawCategory.equals("") == false)
                idc = Integer.valueOf(drawCategory);

            ud = new UnitDef(symbolID, description, idc, hierarchy, path);

            boolean isMCSSpecificFE = SymbolUtilities.isMCSSpecificForceElement(ud);

            if (symStd == RendererSettings.Symbology_2525B) {
                if (_UnitDefinitionsB.containsKey(symbolID) == false && isMCSSpecificFE == false)
                    _UnitDefinitionsB.put(symbolID, ud);// EMS have dupe symbols with same code
                else if (isMCSSpecificFE == false)
                    _UnitDefDupsB.add(ud);
            } else {
                if (_UnitDefinitionsC.containsKey(symbolID) == false && isMCSSpecificFE == false)
                    _UnitDefinitionsC.put(symbolID, ud);// EMS have dupe symbols with same code
                else if (isMCSSpecificFE == false)
                    _UnitDefDupsC.add(ud);
            }
        }
    }// end populateLookup

    /**
     * @name getSymbolDef
     *
     * @desc Returns a SymbolDef from the SymbolDefTable that matches the passed in Symbol Id
     *
     * @param basicSymbolID - IN - A 15 character MilStd code
     * @return SymbolDef whose Symbol Id matches what is passed in
     */
    public UnitDef getUnitDef(String basicSymbolID, int symStd)
    {
        UnitDef returnVal = null;
        try {
            if (symStd == RendererSettings.Symbology_2525B) {
                returnVal = _UnitDefinitionsB.get(basicSymbolID);
            } else if (symStd == RendererSettings.Symbology_2525C) {
                returnVal = _UnitDefinitionsC.get(basicSymbolID);
            }
        } catch (Exception exc) {
            Log.e("UnitDefTable", exc.getMessage(), exc);
        } catch (Throwable thrown) {
            Log.wtf("UnitDefTable", thrown.getMessage(), thrown);
        }
        return returnVal;
    }

    /**
     *
     * @return
     */
    public Map<String, UnitDef> getAllUnitDefs(int symStd)
    {
        if (symStd == RendererSettings.Symbology_2525B)
            return _UnitDefinitionsB;
        else
            return _UnitDefinitionsC;
    }

    /**
     * SymbolIDs are no longer unique thanks to 2525C and some EMS symbols.
     * Here are the EMS symbols that reused symbol IDs.
     * Like how EMS.INCDNT.CVDIS.DISPOP uses the same symbol code as STBOPS.ITM.RFG (O*I*R-----*****)
     * @param symStd
     * @return
     */
    public ArrayList<UnitDef> getUnitDefDups(int symStd)
    {
        if (symStd == RendererSettings.Symbology_2525B)
            return _UnitDefDupsB;
        else
            return _UnitDefDupsC;
    }

    /**
     * 
     * @param basicSymbolID
     * @return
     */
    public Boolean hasUnitDef(String basicSymbolID, int symStd)
    {
        if (basicSymbolID != null && basicSymbolID.length() == 15) {
            if (symStd == RendererSettings.Symbology_2525B)
                return _UnitDefinitionsB.containsKey(basicSymbolID);
            else if (symStd == RendererSettings.Symbology_2525C)
                return _UnitDefinitionsC.containsKey(basicSymbolID);
            else
                return false;
        } else
            return false;
    }

}
