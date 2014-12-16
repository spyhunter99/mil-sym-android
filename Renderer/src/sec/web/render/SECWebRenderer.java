package sec.web.render;
// This import is if we need to call a javascript function
// It requires that you import the plugins.jar from the jdk folder into the project libraries
//import netscape.javascript.JSObject;

import android.util.SparseArray;
import armyc2.c2sd.renderer.MilStdIconRenderer;
import armyc2.c2sd.renderer.utilities.ErrorLogger;
import armyc2.c2sd.renderer.utilities.MilStdAttributes;
import armyc2.c2sd.renderer.utilities.MilStdSymbol;
import armyc2.c2sd.renderer.utilities.ModifiersTG;
import armyc2.c2sd.renderer.utilities.RendererSettings;
//import armyc2.c2sd.renderer.utilities.ShapeInfo;
import armyc2.c2sd.renderer.utilities.SymbolUtilities;
import armyc2.c2sd.renderer.utilities.Color;
import armyc2.c2sd.graphics2d.*;
import sec.web.render.utilities.JavaRendererUtilities;
//import java.util.ArrayList;
//import java.util.Map;
import java.util.logging.Level;
import sec.geo.kml.KmlOptions;
import sec.web.json.utilities.JSONArray;
import sec.web.json.utilities.JSONException;
import sec.web.json.utilities.JSONObject;
import sec.geo.utilities.StringBuilder;

/**
 *
 * @author Administrator
 */
//@SuppressWarnings("unused")
public final class SECWebRenderer /* extends Applet */ {
	private static final long serialVersionUID = -2691218568602318366L;
	
	// constants for the available shape types that can be generated into KML
    public static final String CYLINDER = "CYLINDER-------";
    public static final String ORBIT = "ORBIT----------";
    public static final String ROUTE = "ROUTE----------";
    public static final String POLYGON = "POLYGON--------";
    public static final String RADARC = "RADARC---------";
    public static final String POLYARC = "POLYARC--------";
    public static final String CAKE = "CAKE-----------";
    public static final String TRACK = "TRACK----------";
    // Attribute names of the 3D shapes
    public static final String ATTRIBUTES = "attributes";
    public static final String MIN_ALT = "minalt";
    public static final String MAX_ALT = "maxalt";
    public static final String RADIUS1 = "radius1";
    public static final String RADIUS2 = "radius2";
    public static final String LEFT_AZIMUTH = "leftAzimuth";
    public static final String RIGHT_AZIMUTH = "rightAzimuth";
    // Arbitrary default values of attributes
    public static final double MIN_ALT_DEFAULT = 0.0D;
    public static final double MAX_ALT_DEFAULT = 100.0D;
    public static final double RADIUS1_DEFAULT = 50.0D;
    public static final double RADIUS2_DEFAULT = 100.0D;
    public static final double LEFT_AZIMUTH_DEFAULT = 0.0D;
    public static final double RIGHT_AZIMUTH_DEFAULT = 90.0D;
    
    public static String ERR_ATTRIBUTES_NOT_FORMATTED = "{\"type\":\"error\","
            + "\"error\":\"The attribute paramaters are not formatted "
            + "correctly";
    
    public static final String DEFAULT_ATTRIBUTES = "[{radius1:"
            + RADIUS1_DEFAULT + ",radius2:"
            + RADIUS2_DEFAULT + ",minalt:"
            + MIN_ALT_DEFAULT + ",maxalt:"
            + MAX_ALT_DEFAULT + ",rightAzimuth:"
            + RIGHT_AZIMUTH_DEFAULT + ",leftAzimuth:"
            + LEFT_AZIMUTH_DEFAULT + "}]";

    /**
     * Initialization method that will be called after the applet is loaded
     * into the browser.
     */
    // JSObject allows calls to be made to the browsers javascript functions
    // We do not neeed to do this currently
    // JSObject jso;
//    private SinglePointServer sps = null;
    
    private static boolean _initSuccess = false;
    

    public static synchronized void init(String cacheDir) {

        try
        {
        	if(_initSuccess == false)
        	{
        		MilStdIconRenderer.getInstance().init(cacheDir);
	            //use SECWebRenderer.setLoggingLevel()
	            
	            //sets default value for single point symbology to have an outline.
	            //outline color will be automatically determined based on line color
	            //unless a color value is manually set.
	            
	            //Set Renderer Settings/////////////////////////////////////////////
	            RendererSettings.getInstance().setSinglePointSymbolOutlineWidth(1);
	            RendererSettings.getInstance().setTextRenderMethod(RendererSettings.RenderMethod_NATIVE);
	            // RendererSettings.getInstance().setTextBackgroundMethod(RendererSettings.TextBackgroundMethod_COLORFILL);
	            // RendererSettings.getInstance().setTextBackgroundMethod(RendererSettings.TextBackgroundMethod_OUTLINE);
	            RendererSettings.getInstance().setTextBackgroundMethod(
	                            RendererSettings.TextBackgroundMethod_OUTLINE_QUICK);
	            RendererSettings.getInstance().setTextOutlineWidth(2);
	            RendererSettings.getInstance().setLabelForegroundColor(Color.BLACK.toARGB());
	            RendererSettings.getInstance().setLabelBackgroundColor(new Color(255, 255, 255, 200).toARGB());
	            // RendererSettings.getInstance().setLabelBackgroundColor(Color.WHITE);
	           //default label settings
	            //RendererSettings.getInstance().setLabelFont("arial", Font.BOLD, 12, false, 0.04f);//default
	            //adjusted tracking to give more space between letters.
	            //RendererSettings.getInstance().setLabelFont("arial", Font.BOLD, 12, false, 0.05f);
	            RendererSettings.getInstance().setModifierFont("arial", Font.PLAIN, 12);
	            ////////////////////////////////////////////////////////////////////
	            ErrorLogger.setLevel(Level.FINE);
        	}
            
        }
        catch(Exception exc)
        {
            ErrorLogger.LogException("SECWebRenderer", "init", exc, Level.WARNING);
        }


    }

    
    /**\
     * Set minimum level at which an item can be logged.
     * In descending order:
     * OFF = Integer.MAX_VALUE
     * Severe = 1000
     * Warning = 900
     * Info = 800
     * Config = 700
     * Fine = 500
     * Finer = 400 
     * Finest = 300
     * All = Integer.MIN_VALUE
     * Use like SECWebRenderer.setLoggingLevel(Level.INFO);
     * or
     * Use like SECWebRenderer.setLoggingLevel(800);
     * @param level java.util.logging.level
     */
    public static void setLoggingLevel(Level level)
    {
        try
        {
            ErrorLogger.setLevel(level,true);
            ErrorLogger.LogMessage("SECWebRenderer", "setLoggingLevel(Level)", 
                    "Logging level set to: " + ErrorLogger.getLevel().getName(), 
                    Level.CONFIG);
        }
        catch(Exception exc)
        {
            ErrorLogger.LogException("SECWebRenderer", "setLoggingLevel(Level)", exc, Level.INFO);
        }
    }
    
    /**\
     * Set minimum level at which an item can be logged.
     * In descending order:
     * OFF = Integer.MAX_VALUE
     * Severe = 1000
     * Warning = 900
     * Info = 800
     * Config = 700
     * Fine = 500
     * Finer = 400 
     * Finest = 300
     * All = Integer.MIN_VALUE
     * Use like SECWebRenderer.setLoggingLevel(Level.INFO);
     * or
     * Use like SECWebRenderer.setLoggingLevel(800);
     * @param level int
     */
    public static void setLoggingLevel(int level)
    {
        try
        {
            if(level > 1000)
                  ErrorLogger.setLevel(Level.OFF,true);
            else if(level > 900)
                  ErrorLogger.setLevel(Level.SEVERE,true);
            else if(level > 800)
                  ErrorLogger.setLevel(Level.WARNING,true);
            else if(level > 700)
                  ErrorLogger.setLevel(Level.INFO,true);
            else if(level > 500)
                  ErrorLogger.setLevel(Level.CONFIG,true);
            else if(level > 400)
                  ErrorLogger.setLevel(Level.FINE,true);
            else if(level > 300)
                  ErrorLogger.setLevel(Level.FINER,true);
            else if(level > Integer.MIN_VALUE)
                  ErrorLogger.setLevel(Level.FINEST,true);
            else
                ErrorLogger.setLevel(Level.ALL,true);
            
            ErrorLogger.LogMessage("SECWebRenderer", "setLoggingLevel(int)", 
                    "Logging level set to: " + ErrorLogger.getLevel().getName(), 
                    Level.CONFIG);
        }
        catch(Exception exc)
        {
            ErrorLogger.LogException("SECWebRenderer", "setLoggingLevel(int)", exc, Level.INFO);
        }
    }
    
    /**
     * Let's user choose between 2525Bch2 and 2525C.
     * Ideally, set only once at startup.
     * 2525Bch2 = 0, 2525C = 1.
     * @param symStd 
     */
    public static void setDefaultSymbologyStandard(int symStd)
    {
        RendererSettings.getInstance().setSymbologyStandard(symStd);
    }
    
    /**
     * Single Point Tactical Graphics are rendered from font files.
     * The font size you specify here determines how big the symbols will 
     * be rendered.  This should be set once at startup.
     * @param size 
     */
    public static void setTacticalGraphicPointSize(int size)
    {
//        sps.setTacticalGraphicPointSize(size);
    }
    
    /**
     * Units are rendered from font files.
     * The font size you specify here determines how big the symbols will 
     * be rendered.  This should be set once at startup. 
     * @param size 
     */
    public static void setUnitPointSize(int size)
    {
//        sps.setUnitPointSize(size);
    }
    
    /**
     * Modifier Text Color will by default match the line color.
     * This will override all modifier text color.
     * @param hexColor 
     */
    public static void setModifierTextColor(String hexColor)
    {
        Color textColor = SymbolUtilities.getColorFromHexString(hexColor);
        if(textColor==null)
        {
            textColor = Color.black;
        }
        //System.out.println("Text Color: " + textColor.toString());
        RendererSettings.getInstance().setLabelForegroundColor(textColor.toARGB());
    }


    


    /**
     * Renders all multi-point symbols, creating KML that can be used to draw
     * it on a Google map.  Multipoint symbols cannot be draw the same 
     * at different scales. For instance, graphics with arrow heads will need to 
     * redraw arrowheads when you zoom in on it.  Similarly, graphics like a 
     * Forward Line of Troops drawn with half circles can improve performance if 
     * clipped when the parts of the graphic that aren't on the screen.  To help 
     * readjust graphics and increase performance, this function requires the 
     * scale and bounding box to help calculate the new locations.
     * @param id A unique identifier used to identify the symbol by Google map. 
     * The id will be the folder name that contains the graphic.
     * @param name a string used to display to the user as the name of the 
     * graphic being created.
     * @param description a brief description about the graphic being made and 
     * what it represents.
     * @param symbolCode A 15 character symbolID corresponding to one of the
     * graphics in the MIL-STD-2525C
     * @param controlPoints The vertices of the graphics that make up the
     * graphic.  Passed in the format of a string, using decimal degrees 
     * separating lat and lon by a comma, separating coordinates by a space.  
     * The following format shall be used "x1,y1[,z1] [xn,yn[,zn]]..."
     * @param altitudeMode Indicates whether the symbol should interpret 
     * altitudes as above sea level or above ground level. Options are 
     * "clampToGround", "relativeToGround" (from surface of earth), "absolute" 
     * (sea level), "relativeToSeaFloor" (from the bottom of major bodies of 
     * water).
     * @param scale A number corresponding to how many meters one meter of our 
     * map represents. A value "50000" would mean 1:50K which means for every 
     * meter of our map it represents 50000 meters of real world distance.
     * @param bbox The viewable area of the map.  Passed in the format of a
     * string "lowerLeftX,lowerLeftY,upperRightX,upperRightY." Not required
     * but can speed up rendering in some cases.
     * example: "-50.4,23.6,-42.2,24.2"
     * @param modifiers SparseArray<String>, keyed using constants from ModifiersTG.  
     * Pass in comma delimited String for modifiers with multiple values like AM, AN & X
     * @param attributes SparseArray<String>, keyed using constants from MilStdAttributes.  
     * @param format An enumeration: 0 for KML, 1 for JSON.
     * @param symStd An enumeration: 0 for 2525Bch2, 1 for 2525C.
     * @return A JSON string representation of the graphic.
     */
    public static String RenderSymbol(String id, String name, String description, 
            String symbolCode, String controlPoints, String altitudeMode,
            double scale, String bbox, SparseArray<String> modifiers, SparseArray<String> attributes,  int format, int symStd) {
        
        String output = "";
        try {                
        
            if (JavaRendererUtilities.is3dSymbol(symbolCode, modifiers))
            {
        
                output = RenderMilStd3dSymbol(name, id, symbolCode, description, altitudeMode, controlPoints,
                        modifiers, attributes);
                
                //get modifiers/////////////////////////////////////////////////
                String modifierKML = MultiPointHandler.getModififerKML(id, name, description, symbolCode, controlPoints,
                        scale, bbox, modifiers, attributes, format,symStd);

                modifierKML += "</Folder>";

                output = output.replaceFirst("</Folder>", modifierKML);

                ////////////////////////////////////////////////////////////////
                
                
                
                // Check the output of the 3D Symbol Drawing.  If this returned an error
                // it should either be "" or it should be a JSON string starting with "{".
                // This really is not a good solution, but was up to 13.0.6 and had to make
                // this bug fix in quick turnaround.  More consistent error handling should
                // be done through code.
               
                if (output.equals("") || output.startsWith("{")) {
                    output = MultiPointHandler.RenderSymbol(id, name, description, symbolCode, controlPoints,
                        scale, bbox, modifiers, attributes, format,symStd);
                }
            }
            else
            {            
                output = MultiPointHandler.RenderSymbol(id, name, description, symbolCode, controlPoints,
                        scale, bbox, modifiers, attributes, format,symStd);
                
                //DEBUGGING
                if(ErrorLogger.getLevel().intValue() <= Level.FINER.intValue())
                {
                    System.out.println("");
                    StringBuilder sb = new StringBuilder();
                    sb.append("\nID: " + id + "\n");
                    sb.append("Name: " + name + "\n");
                    sb.append("Description: " + description + "\n");
                    sb.append("SymbolID: " + symbolCode + "\n");
                    sb.append("SymStd: " + String.valueOf(symStd) + "\n");
                    sb.append("Scale: " + String.valueOf(scale) + "\n");
                    sb.append("BBox: " + bbox + "\n");
                    sb.append("Coords: " + controlPoints + "\n");
                    sb.append("Modifiers: " + modifiers + "\n");
                    ErrorLogger.LogMessage("SECWebRenderer", "RenderSymbol", sb.toString(),Level.FINER);
                }
                if(ErrorLogger.getLevel().intValue() <= Level.FINEST.intValue())
                {
                    String briefOutput = output.replaceAll("</Placemark>", "</Placemark>\n");
                    briefOutput = output.replaceAll("(?s)<description[^>]*>.*?</description>", "<description></description>");
                    ErrorLogger.LogMessage("SECWebRenderer", "RenderSymbol", "Output:\n" + briefOutput,Level.FINEST);
                }
            }
            
            
        } catch (Exception ea) {
            
            output = "{\"type\":'error',error:'There was an error creating the MilStdSymbol - " + ea.toString() + "'}";
            ErrorLogger.LogException("SECWebRenderer", "RenderSymbol", ea, Level.WARNING);
        }
        
        return output;
    }
    

         


    /**
     * Renders all multi-point symbols, creating KML or JSON for the user to
     * parse and render as they like.
     * This function requires the bounding box to help calculate the new
     * locations.
     * @param id A unique identifier used to identify the symbol by Google map.
     * The id will be the folder name that contains the graphic.
     * @param name a string used to display to the user as the name of the 
     * graphic being created.
     * @param description a brief description about the graphic being made and 
     * what it represents.
     * @param symbolCode A 15 character symbolID corresponding to one of the
     * graphics in the MIL-STD-2525C
     * @param controlPoints The vertices of the graphics that make up the
     * graphic.  Passed in the format of a string, using decimal degrees
     * separating lat and lon by a comma, separating coordinates by a space.
     * The following format shall be used "x1,y1 [xn,yn]..."
     * @param pixelWidth pixel dimensions of the viewable map area
     * @param pixelHeight pixel dimensions of the viewable map area
     * @param bbox The viewable area of the map.  Passed in the format of a
     * string "lowerLeftX,lowerLeftY,upperRightX,upperRightY."
     * example: "-50.4,23.6,-42.2,24.2"
     * @param modifiers SparseArray<String>, keyed using constants from ModifiersTG.  
     * Pass in comma delimited String for modifiers with multiple values like AM, AN & X
     * @param attributes SparseArray<String>, keyed using constants from MilStdAttributes.
     * @param format An enumeration: 0 for KML, 1 for JSON.
     * @param symStd An enumeration: 0 for 2525Bch2, 1 for 2525C.
     * @return A JSON (1) or KML (0) string representation of the graphic.
     */
    public static String RenderSymbol2D(String id, String name, String description, String symbolCode, String controlPoints,
            int pixelWidth, int pixelHeight, String bbox, SparseArray<String> modifiers, 
            SparseArray<String> attributes, int format, int symStd)
    {
        String output = "";
        try
        {
            output = MultiPointHandler.RenderSymbol2D(id, name, description, 
                    symbolCode, controlPoints, pixelWidth, pixelHeight, bbox, 
                    modifiers, attributes, format, symStd);
        }
        catch(Exception exc)
        {
            output = "{\"type\":'error',error:'There was an error creating the MilStdSymbol: " + symbolCode + " - " + exc.toString() + "'}";
        }
        return output;
    }

    /**
     * Creates a 3D symbol to be displayed on some 3D globe surface.  Generates 
     * Keyhole Markup Language (KML) to return that specifies the points and format of 
     * the rendering.
     * <br/>
     * Control points should be of the format of:
     * <tr><code>"x,y,z [x,y,z]..."</code></tr>
     * Attributes should be passed in as a JSON array.  If more than one set of 
     * parameters are passed in as an array or more than one item, they will map 
     * to the vertex specified in the control points.  The attributes are
     * of the format:
     * <tr><code>{"attributes":[{"<i>attribute1</i>":<i>value</i>,...},{<i>[optional]</i>]}</code></tr>
     * 
     * @param name The user displayed name for the symbol.  Users will use this 
     * to identify with the symbol.
     * @param id An internally used unique id that developers can use to 
     * uniquely distinguish this symbol from others.
     * @param shapeType A 15 character ID of the type of symbol to draw.
     * @param description A brief description of what the symbol represents.  
     * Generic text that does not require any format.
     * @param color The fill color of the graphic
     * @param altitudeMode Indicates whether the symbol should interpret 
     * altitudes as above sea level or above ground level. Options are 
     * "relativeToGround" (from surface of earth), "absolute" (sea level), 
     * "relativeToSeaFloor" (from the bottom of major bodies of water).
     * @param controlPoints The vertices of the shape.  The number of required
     * vertices varies based on the shapeType of the symbol.  The simplest shape 
     * requires at least one point.  Shapes that require more points than 
     * required will ignore extra points.  Format for numbers is as follows: 
     * <br/><br/>
     * "x,y,z [x,y,z ]..."
     * @param attributes A JSON  array holding the parameters for the 
     * shape.  Attributes should be of the following format: <br/><br/>
     * <tr><code>{"attributes":[{"<i>attribute1</i>":<i>value</i>,...},{<i>[optional]</i>]}</code></tr>
     * @return A KML string that represents a placemark for the 3D shape
     */
    public static String Render3dSymbol(String name, String id, String shapeType, 
            String description, String color, String altitudeMode, 
            String controlPoints,
            String attributes) {
        
        String returnValue = "";
        
        try {
            
            StringBuilder output = new StringBuilder();
            SymbolModifiers modifiers = new SymbolModifiers();
            JSONObject attributesJSON;
                        
            // Retrieve the attributes from the attributes object
            // Attributes should be a JSON array string of this format 
            // { "attributes":[{"radius1":50, "minalt":0, "maxalt:100"}]}
            // There should only be one item in the JSON array, but if 
            // there is more items this will ignore them.            
            //attributes="{attributes:[{radius1:5000, minalt:0, maxalt:100}]}";
            attributesJSON = new JSONObject(attributes);

            // If no attributes passed in or attributes set to null
            // default to an empty string.
            if (attributesJSON == null || attributes.equals("")) {
                attributesJSON = new JSONObject(DEFAULT_ATTRIBUTES);
            }

            JSONArray attributesArray = attributesJSON.getJSONArray(ATTRIBUTES);
            int attributesArrayLength = attributesArray.length();
            if (attributesArrayLength> 0) {
                
                for(int i = 0; i < attributesArrayLength; i++) {
                
                    // get the first item in the array and use those parameters.
                    // if any of the parameters don't exist, it will use defaults.
                    // Defaults are arbitrary, no reason not to change them.
                    JSONObject currentAttributeSet = attributesArray.getJSONObject(i);

                    if (currentAttributeSet.has(RADIUS1)) {
                        modifiers.AM_DISTANCE.add(currentAttributeSet.getDouble(RADIUS1));
                    }                                    

                    if (currentAttributeSet.has(RADIUS2)) {
                        modifiers.AM_DISTANCE.add(currentAttributeSet.getDouble(RADIUS2));
                    }

                    if (currentAttributeSet.has(MIN_ALT)) {
                        modifiers.X_ALTITUDE_DEPTH.add(currentAttributeSet.getDouble(MIN_ALT));
                    }

                    if (currentAttributeSet.has(MAX_ALT)) {
                        modifiers.X_ALTITUDE_DEPTH.add(currentAttributeSet.getDouble(MAX_ALT));
                    }

                    if (currentAttributeSet.has(LEFT_AZIMUTH)) {
                        modifiers.AN_AZIMUTH.add(currentAttributeSet.getDouble(LEFT_AZIMUTH));
                    }

                    if (currentAttributeSet.has(RIGHT_AZIMUTH)) {
                        modifiers.AN_AZIMUTH.add(currentAttributeSet.getDouble(RIGHT_AZIMUTH));
                    }
                }
            }
            
            // Send to the 3D renderer for generating the 3D point and creating
            // the KML to return.            
            returnValue = Shape3DHandler.render3dSymbol(name, id, shapeType, 
                description, color, altitudeMode, controlPoints, modifiers);            
        } 
        catch (JSONException je) {

            return ERR_ATTRIBUTES_NOT_FORMATTED;
        }            
        catch (Exception ea) {
            //ErrorLogger.LogException(this.getName(), "Render3dSymbol()", ea);
            ErrorLogger.LogException("SECWebRenderer", "Render3dSymbol()", ea);
            return "";
        }            
        
        return returnValue;
    }
    
     /**
     * Creates a 3D symbol from the MilStd2525B USAS or MIL-STD-2525C to be 
     * displayed on a 3D globe surface.  Only certain symbols from the MIL-STD
     * can be displayed in 3D.   Most of these are graphics that fall under Fire
     * Support.  Any graphic that has an X modifier (altitude/depth) should
     * have a 3D representation.  Generates 
     * Keyhole Markup Language (KML) to return that 
     * specifies the points and format of 
     * the rendering.
     * <br/>
     * Control points should be of the format of:
     * <tr><code>"x,y,z [x,y,z]..."</code></tr>
     * 
     * 
     * @param name The user displayed name for the symbol.  Users will use this 
     * to identify with the symbol.
     * @param id An internally used unique id that developers can use to 
     * uniquely distinguish this symbol from others.
     * @param symbolCode A 15 character ID of the type of symbol to draw.  Only
     * symbols with an X modifier from the standard will draw.
     * @param description A brief description of what the symbol represents.  
     * Generic text that does not require any format.  
     * @param altitudeMode Indicates whether the symbol should interpret 
     * altitudes as above sea level or above ground level. Options are 
     * "relativeToGround" (from surface of earth), "absolute" (sea level), 
     * "relativeToSeaFloor" (from the bottom of major bodies of water).
     * @param controlPoints The vertices of the shape.  The number of required
     * vertices varies based on the shapeType of the symbol.  The simplest shape 
     * requires at least one point.  Shapes that require more points than 
     * required will ignore extra points.  Format for numbers is as follows: 
     * <br/><br/>
     * "x,y,z [x,y,z ]..."
     * @saModifiers SparseArray<String> keyed on ModifiersTG. Only using values AM, AN and X.
     * Strings should be comma delimited for multiple values.
     * @saAttributes SparseArray<String> keyed on MilStdAttributes. Only using value FillColor.
     * @return A KML string that represents a placemark for the 3D shape
     */
    private static String RenderMilStd3dSymbol(String name, String id, String symbolCode, 
            String description, 
            String altitudeMode,
            String controlPoints,
            SparseArray<String> saModifiers,
            SparseArray<String> saAttributes) {
               
        String symbolId = symbolCode.substring(4,10);
        SymbolModifiers attributes = new SymbolModifiers();
        String output = "";
        
        KmlOptions.AltitudeMode convertedAltitudeMode = KmlOptions.AltitudeMode.RELATIVE_TO_GROUND;

        // Convert altitude mode to an enum that we understand.  If it does not
        // understand or is "", then convert to ALTITUDE_RELATIVE_TO_GROUND.
        if (!altitudeMode.equals(""))
        {
            convertedAltitudeMode = KmlOptions.AltitudeMode.fromString(altitudeMode);
        }
        
        try
        {
        
            
            String[] altitudeDepth = null;
            String[] distance = null;
            String[] azimuth = null;
            int altitudeDepthLength = 0;
            int distanceLength = 0;
            int azimuthLength = 0;
            String color = "";
            
            if (saModifiers.indexOfKey(ModifiersTG.X_ALTITUDE_DEPTH)>=0)
            {
                altitudeDepth = saModifiers.get(ModifiersTG.X_ALTITUDE_DEPTH).split(",");
                altitudeDepthLength = altitudeDepth.length;
            }
            
            if (saModifiers.indexOfKey(ModifiersTG.AN_AZIMUTH)>=0)
            {
            	azimuth = saModifiers.get(ModifiersTG.AN_AZIMUTH).split(",");
            	azimuthLength = azimuth.length;
            }
            
            if (saModifiers.indexOfKey(ModifiersTG.AM_DISTANCE)>=0)
            {
            	distance = saModifiers.get(ModifiersTG.AM_DISTANCE).split(",");
            	distanceLength = distance.length;
            }
            
            if (saAttributes.indexOfKey(MilStdAttributes.FillColor)>=0)
            {
            	color = saAttributes.get(MilStdAttributes.FillColor);
            }
            else
            {   
                Color c = SymbolUtilities.getFillColorOfAffiliation(symbolCode);
                //color = Integer.toHexString(c.getRGB());
                color = c.toHexString();
                //color = JavaRendererUtilities.getAffiliationFillColor(symbolCode);
                // ensure that some color is selected.  If no color can be
                // found, use black.
                if (color == null)
                {
                    color = "AA000000";
                }
            }
            
            color = JavaRendererUtilities.ARGBtoABGR(color);
                            
            for (int i=0; i < altitudeDepthLength; i++)
            {
                // if it's a killbox, need to set minimum alt to 0.
                if (symbolId.startsWith("AJP"))
                {
                    attributes.X_ALTITUDE_DEPTH.add(0D);
                    i++;
                }                                        
                attributes.X_ALTITUDE_DEPTH.add(Double.parseDouble(altitudeDepth[i]));
            }
            for (int i=0; i < distanceLength; i++)
            {
                // If this is a 'track' type graphic, then we need to take the distance
                // and divide it by half, than add it twice.  This is due 
                // to the TAIS requirement that Tracks must have a left width 
                // and a right width. 
                if (symbolId.equals("ACAR--") || // ACA - rectangular
                    symbolId.equals("AKPR--") || // Killbox - rectangular
                    symbolId.equals("ALC---") || // air corricor
                    symbolId.equals("ALM---") || // MRR
                    symbolId.equals("ALS---") || // SAAFR
                    symbolId.equals("ALU---") || // unmanned aircraft
                    symbolId.equals("ALL---")) {  // LLTR) {
                    double width = Double.parseDouble(distance[i]) / 2;
                    attributes.AM_DISTANCE.add(width);
                    attributes.AM_DISTANCE.add(width);
                } else {
                    attributes.AM_DISTANCE.add(Double.parseDouble(distance[i]));
                }
            }  

            if (symbolId.equals("ACAI--") || // ACA - irregular
                    symbolId.equals("AKPI--") || // Killbox - irregular
                    symbolId.equals("AAR---") || // ROZ
                    symbolId.equals("AAF---") || // SHORADEZ
                    symbolId.equals("AAH---") || // HIDACZ
                    symbolId.equals("AAM---") || // MEZ
                    symbolId.equals("AAML--") || // LOMEZ
                    symbolId.equals("AAMH--")) // HIMEZ
            {
                output = Shape3DHandler.buildPolygon(controlPoints, id, name, 
                    description, color, convertedAltitudeMode, attributes);
            }
            else if (symbolId.equals("ACAR--") || // ACA - rectangular
                    symbolId.equals("AKPR--") || // Killbox - rectangular
                    symbolId.equals("ALC---") || // air corricor
                    symbolId.equals("ALM---") || // MRR
                    symbolId.equals("ALS---") || // SAAFR
                    symbolId.equals("ALU---") || // unmanned aircraft
                    symbolId.equals("ALL---"))   // LLTR
            {
                output = Shape3DHandler.buildTrack(controlPoints, id, name, 
                    description, color, convertedAltitudeMode, attributes);
            }
            else if (symbolId.equals("ACAC--") || // ACA - circular
                    symbolId.equals("AKPC--"))    // Killbox - circular
            {
                output = Shape3DHandler.buildCylinder(controlPoints, id, name, 
                    description, color, convertedAltitudeMode, attributes);

            }   

            
        }
        catch (Exception exc)
        {
            output = "";
        } 
        return output;
    }

    /**
	 * Renders all multi-point symbols, creating MilStdSymbol that contains the
	 * information needed to draw the symbol on the map.
	 * ArrayList<Point2D> milStdSymbol.getSymbolShapes.get(index).getPolylines()
	 * and 
	 * ShapeInfo = milStdSymbol.getModifierShapes.get(index). 
	 * 
	 * 
	 * @param id
	 *            A unique identifier used to identify the symbol by Google map.
	 *            The id will be the folder name that contains the graphic.
	 * @param name
	 *            a string used to display to the user as the name of the
	 *            graphic being created.
	 * @param description
	 *            a brief description about the graphic being made and what it
	 *            represents.
	 * @param symbolCode
	 *            A 15 character symbolID corresponding to one of the graphics
	 *            in the MIL-STD-2525C
	 * @param controlPoints
	 *            The vertices of the graphics that make up the graphic. Passed
	 *            in the format of a string, using decimal degrees separating
	 *            lat and lon by a comma, separating coordinates by a space. The
	 *            following format shall be used "x1,y1[,z1] [xn,yn[,zn]]..."
	 * @param altitudeMode
	 *            Indicates whether the symbol should interpret altitudes as
	 *            above sea level or above ground level. Options are
	 *            "clampToGround", "relativeToGround" (from surface of earth),
	 *            "absolute" (sea level), "relativeToSeaFloor" (from the bottom
	 *            of major bodies of water).
	 * @param scale
	 *            A number corresponding to how many meters one meter of our map
	 *            represents. A value "50000" would mean 1:50K which means for
	 *            every meter of our map it represents 50000 meters of real
	 *            world distance.
	 * @param bbox
	 *            The viewable area of the map. Passed in the format of a string
	 *            "lowerLeftX,lowerLeftY,upperRightX,upperRightY." Not required
	 *            but can speed up rendering in some cases. example:
	 *            "-50.4,23.6,-42.2,24.2"
	 * @param modifiers
	 *            A JSON string representing all the possible symbol modifiers
	 *            represented in the MIL-STD-2525C. Format of the string will be
	 *            {"attributeName":"value"[,"attributeNamen":"valuen"]...}} The
	 *            quotes are literal in the above notation. Example:
	 *            {"quantity":"4","speed":"300","azimuth":[100,200]}}
	 * @param symStd
	 *            An enumeration: 0 for 2525Bch2, 1 for 2525C.
     * @return MilStdSymbol
     */
    public static MilStdSymbol RenderMultiPointAsMilStdSymbol(String id, String name, String description, String symbolCode,
			String controlPoints, String altitudeMode, double scale, String bbox, SparseArray<String> modifiers, SparseArray<String> attributes, int symStd)
    {
		MilStdSymbol mSymbol = null;
		try 
		{

			if (JavaRendererUtilities.is3dSymbol(symbolCode, modifiers)==false) 
            {
                mSymbol = MultiPointHandler.RenderSymbolAsMilStdSymbol(id, name, description, symbolCode, 
                		controlPoints, scale, bbox, modifiers, attributes, symStd);
			}

		} 
		catch (Exception ea) 
		{
			mSymbol=null;
			ErrorLogger.LogException("SECRenderer", "RenderMultiPointAsMilStdSymbol", ea, Level.WARNING);
		}
		
		//System.out.println("RenderMultiPointAsMilStdSymbol exit");
		return mSymbol;
    }


    
    /**
     * Given a symbol code meant for a single point symbol, returns the
     * anchor point at which to display that image based off the image returned
     * from the URL of the SinglePointServer.
     * 
     * @param symbolID - the 15 character symbolID of a single point MilStd2525
     * symbol. 
     * @return A pixel coordinate of the format "x,y".
     * Returns an empty string if an error occurs.
     * @deprecated 
     */
	public String getSinglePointAnchor(String symbolID) {
        String anchorPoint = "";
//        if (sps != null)
//        {
            Point2D anchor = new Point2D.Double();
//            sps.getSinglePointDimensions(symbolID, anchor);
            anchorPoint = anchor.getX() + "," + anchor.getY();
//        }
        
        return anchorPoint;
    }

    /**
     * Given a symbol code meant for a single point symbol, returns the
     * anchor point at which to display that image based off the image returned
     * from the URL of the SinglePointServer.
     *
     * @param symbolID - the 15 character symbolID of a single point MilStd2525
     * symbol.
     * @return A pixel coordinate of the format "anchorX,anchorY,SymbolBoundsX,
     * SymbolBoundsY,SymbolBoundsWidth,SymbolBoundsHeight,IconWidth,IconHeight".
     * Anchor, represents the center point of the core symbol within the image.
     * The image should be centered on this point.
     * Symbol bounds represents the bounding rectangle of the core symbol within
     * the image.
     * IconWidth/Height represents the height and width of the image in its
     * entirety.
     * Returns an empty string if an error occurs.
     */
    public static String getSinglePointInfo(String symbolID)
    {
        String info = "";
        Point2D anchor = new Point2D.Double();
        Rectangle2D symbolBounds = new Rectangle2D.Double();
//        Dimension2D iconSize = new Dimension();
//        sps.getSinglePointDimensions(symbolID, anchor, symbolBounds, iconSize);
//        info = anchor.getX() + "," + anchor.getY() + "," +
//                symbolBounds.getX() + "," + symbolBounds.getY() + "," +
//                symbolBounds.getWidth() + "," + symbolBounds.getHeight() + "," + 
//                iconSize.getWidth() + "," + iconSize.getHeight();
        return info;
    }
    
    /**
     * Given a symbol code meant for a single point symbol, returns the
     * anchor point at which to display that image based off the image returned
     * from the URL of the SinglePointServer.
     * @param batch like {"iconURLs":["SFGP------*****?size=35&T=Hello","SHGPE-----*****?size=50"]}
     * @return like {"singlepoints":[{"x":0,"y":0,"boundsx":0,"boundsy":0,"boundswidth":35,"boundsheight":35,"iconwidth":35,"iconwidth":35}, ... ]}
     */
//    public static String getSinglePointInfoBatch(String batch)
//    {
//        String info = "";
//        Point2D anchor = new Point2D.Double();
//        Rectangle2D symbolBounds = new Rectangle2D();
////        Dimension2D iconSize = new Dimension();
//        JSONObject symbolInfo = null;
//        StringBuilder sb = new StringBuilder();
//        try
//        {
//
//            //must escape '=' so that JSONObject can parse string
//            batch = batch.replaceAll("=", "%3D");
//            
//            String data = null;
//            JSONObject jsonSPString = new JSONObject(batch);
//            JSONArray jsa = jsonSPString.getJSONArray("iconURLs");
//            int len = jsa.length();
//            sb.append("{\"singlepoints\":[");
//            String item = null;
//            for(int i = 0; i < len; i++)
//            {
//                if(i>0)
//                {
//                    sb.append(",");
//                }
//                
//                info = jsa.get(i).toString();
//                //System.out.println(info);
//                //info = java.net.URLDecoder.decode(info, "UTF-8");
//                info = info.replaceAll("%3D", "=");
//                //System.out.println(info);
//                anchor = new Point2D.Double();
//                symbolBounds = new Rectangle2D();
////                iconSize = new Dimension();
////                sps.getSinglePointDimensions(info, anchor, symbolBounds, iconSize);
////                item = SymbolDimensionsToJSON(anchor, symbolBounds, iconSize);
//                sb.append(item);
//            }
//            sb.append("]}");
//        }
//        catch(Exception exc)
//        {
//            System.out.println(exc.getMessage());
//            exc.printStackTrace();
//        }
//        return sb.toString();
//    }
    
//    private String SymbolDimensionsToJSON(Point2D anchor, Rectangle2D bounds, Dimension2D iconSize)
//    {
//        StringBuilder sb = new StringBuilder();
//        try
//        {
//            sb.append("{\"x\":");
//            sb.append(anchor.getX());
//            sb.append(",\"y\":");
//            sb.append(anchor.getY());
//            sb.append(",\"boundsx\":");
//            sb.append(bounds.getX());
//            sb.append(",\"boundsy\":");
//            sb.append(bounds.getY());
//            sb.append(",\"boundswidth\":");
//            sb.append(bounds.getWidth());
//            sb.append(",\"boundsheight\":");
//            sb.append(bounds.getHeight());
//            sb.append(",\"iconwidth\":");
//            sb.append(iconSize.getWidth());
//            sb.append(",\"iconheight\":");
//            sb.append(iconSize.getHeight());
//            sb.append("}");
//        }
//        catch(Exception exc)
//        {
//            System.out.println(exc.getMessage());
//            exc.printStackTrace();
//        }
//        return sb.toString();
//    }
    
    /**
     * Returns true if we recommend clipping a particular symbol.
     * Would return false for and Ambush but would return true for a Line of 
     * Contact due to the decoration on the line.
     * @param symbolID
     * @return 
     */
    public static String ShouldClipMultipointSymbol(String symbolID)
    {
        if(MultiPointHandler.ShouldClipSymbol(symbolID))
            return "true";
        else
            return "false";
    }
    
    /**
     * Put this here rather than in multipointhandler so that I could get the
     * port info from the single point server.
     * @param modifiers
     * @param clip
     * @return 
     * @deprecated use GenerateSymbolLineFillUrl
     */
//    public static String GenerateSymbolAreaFillUrl(Map<String,String> modifiers, ArrayList<ShapeInfo> clip)
//    {
//        int shapeType = 0;
//        String url = "";
//        String symbolFillIDs=null;
//        String symbolLineIDs=null;
//        //int symbolSize = AreaSymbolFill.DEFAULT_SYMBOL_SIZE;
//        int symbolSize = 25;
//        int imageoffset = 0;
//        ArrayList<ArrayList<Point2D>> lines = null;
//        ArrayList<Point2D> points = null;
//        Point2D point = null;
//        
//        Shape shape = null;
//        //PathIterator itr = null;
//        double height = 0;
//        double width = 0;
//        int offsetX = 0;
//        int offsetY = 0;
//        int x = 0;
//        int y = 0;
//        Rectangle2D bounds = null;
//        try {
//            for(ShapeInfo si : clip) {
//                shapeType = si.getShapeType(); 
//                
//                if (shapeType==ShapeInfo.SHAPE_TYPE_POLYLINE) {
//                    if (bounds==null) {
////                        bounds = si.getBounds();
//                    } else {
////                        Rectangle2D.union(bounds, si.getBounds(), bounds);
//            		}
//                    
//                    height = bounds.getHeight();
//                    width = bounds.getWidth();
//                    shape = si.getShape();
//                    lines = si.getPolylines();
//                    System.out.println("bounds: "+ bounds.toString());
////                  System.out.println("height: "+ String.valueOf(height));
//                    System.out.println("width: "+ String.valueOf(width));
//                    if (bounds.getX() < 0) {
//                        offsetX = (int)(bounds.getX()*-1);
//                    }
//                    if(bounds.getY() < 0) {
//                        offsetY = (int)(bounds.getY()*-1);
//                    }
//                    
//                }//end if polyline
//                
//            }//end for
//                    
//            //itr = shape.getPathIterator(new AffineTransform());
//            StringBuilder sbCoords = new StringBuilder();
//            StringBuilder sbUrl = new StringBuilder();
//            //itr.next();
//
//
//            if(modifiers.containsKey("symbolFillIds"))
//            {
//                symbolFillIDs = modifiers.get("symbolFillIds");
//            }
//            if(modifiers.containsKey("symbolLineIds"))
//            {
//                symbolLineIDs = modifiers.get("symbolLineIds");
//            }
//            if(modifiers.containsKey("symbolFillIconSize"))
//            {
//                symbolSize = Integer.getInteger(modifiers.get("symbolFillIconSize").toString());
//            }
//
//            if(symbolLineIDs != null && symbolSize > 0)
//            {
//
//            }
//            //if(symbolFillIDs != null)
//            //{
//                sbCoords.append("clipCoords=");
//                for(int j = 0; j<lines.size();j++)
//                {
//                    points = lines.get(j);
//                    for(int i = 0; i< points.size(); i++)
//                    {
//                        if(i>0 || j>0)
//                        {
//                            sbCoords.append(",");
//                        }
//                        point = points.get(i);
//                        x = (int)(point.getX() + offsetX);
//                        y = (int)(point.getY() + offsetY);
//                        sbCoords.append(x);
//                        sbCoords.append(",");
//                        sbCoords.append(y);
//                    }
//                }
//            //}
//            //build image url
//            sbUrl.append("http://127.0.0.1:");
//            sbUrl.append(String.valueOf(spsPortNumber));
//            sbUrl.append("/AREASYMBOLFILL?");
//            sbUrl.append("renderer=AreaSymbolFillRenderer&");
//            //if(sbCoords.length()>0)
//            //{
//                sbUrl.append(sbCoords.toString());
//            //}
//            if(symbolFillIDs != null)
//            {
//                sbUrl.append("&symbolFillIds=");
//                sbUrl.append(symbolFillIDs);
//            }
//            if(symbolLineIDs != null)
//            {
//                sbUrl.append("&symbolLineIds=");
//                sbUrl.append(symbolLineIDs);
//            }
//            if(symbolSize>0)
//            {
//                sbUrl.append("&symbolFillIconSize=");
//                sbUrl.append(symbolSize);
//            }
//
//
//
//            sbUrl.append("&height=");
//            sbUrl.append(Integer.valueOf((int)height));
//            sbUrl.append("&width=");
//            sbUrl.append(Integer.valueOf((int)width));
//
//            url = sbUrl.toString();
//
//        }
//        catch(Exception exc)
//        {
//            System.out.println(exc.getMessage());
//            exc.printStackTrace();
//        }
//        return url;
//    }
    
    
     /**
     * Put this here rather than in multipointhandler so that I could get the
     * port info from the single point server.
     * @param modifiers
     * @param clip
     * @return 
     */
//    public static String GenerateSymbolLineFillUrl(Map<String,String> modifiers, ArrayList<Point2D> pixels, Rectangle2D clip)
//    {
//        int shapeType = 0;
//        String url = "";
//        String symbolFillIDs=null;
//        String symbolLineIDs=null;
//        String strClip=null;
//        //int symbolSize = AreaSymbolFill.DEFAULT_SYMBOL_SIZE;
//        int symbolSize = 25;
//        int imageoffset = 0;
//        ArrayList<ArrayList<Point2D>> lines = null;
//        ArrayList<Point2D> points = null;
//        Point2D point = null;
//        
//        Shape shape = null;
//        //PathIterator itr = null;
//        double height = 0;
//        double width = 0;
//        int offsetX = 0;
//        int offsetY = 0;
//        int x = 0;
//        int y = 0;
//        Rectangle2D bounds = null;
//        try
//        {
//            Point2D temp = null;
//            //Get bounds of the polygon/polyline path
//            for(int i=0; i<pixels.size();i++)
//            {
//                temp = pixels.get(i);
//                if(i>0)
//                {
////                    path.lineTo(temp.getX(), temp.getY());
//                }
//                else if(i==0)
//                {
////                    path.moveTo(temp.getX(), temp.getY());
//                }
//            }
//            
//            height = bounds.getHeight();
//            width = bounds.getWidth();
//
//            
//            //pixels may be in negative space so get offsets to put everything
//            //in the positive
//            if(bounds.getX()<0)
//            {
//                offsetX = (int)(bounds.getX()*-1);
//            }
//            else if((bounds.getX()+bounds.getWidth()) > width)
//            {
//                offsetX = (int)((bounds.getX()+bounds.getWidth())-width)*-1;
//            }
//            
//            if(bounds.getY()<0)
//            {
//                offsetY = (int)(bounds.getY()*-1);
//            }
//            else if((bounds.getY()+bounds.getHeight()) > height)
//            {
//                offsetY = (int)((bounds.getY()+bounds.getHeight())-height)*-1;
//            }
//
//            //build clip string
//            if(clip!=null)
//            {
//                StringBuilder sbClip = new StringBuilder();
//                sbClip.append("&clip=");
//                sbClip.append(clip.getX());
//                sbClip.append(",");
//                sbClip.append(clip.getY());
//                sbClip.append(",");
//                sbClip.append(clip.getWidth());
//                sbClip.append(",");
//                sbClip.append(clip.getHeight());
//                strClip=sbClip.toString();
//            }
//
//                    
//            //itr = shape.getPathIterator(new AffineTransform());
//            StringBuilder sbCoords = new StringBuilder();
//            StringBuilder sbUrl = new StringBuilder();
//            sbCoords.append("coords=");
//            //itr.next();
//
//            //get parameters
//            if(modifiers.containsKey("symbolFillIds"))
//            {
//                symbolFillIDs = modifiers.get("symbolFillIds");
//            }
//            if(modifiers.containsKey("symbolLineIds"))
//            {
//                symbolLineIDs = modifiers.get("symbolLineIds");
//            }
//            if(modifiers.containsKey("symbolFillIconSize"))
//            {
//                symbolSize = Integer.getInteger(modifiers.get("symbolFillIconSize").toString());
//            }
//            if(modifiers.containsKey("clip"))
//            {
//                strClip = modifiers.get("clip").toString();
//            }
//            
//            //build coordinate string
//            for(int i = 0; i< pixels.size(); i++)
//            {
//                if(i>0)
//                {
//                    sbCoords.append(",");
//                }
//                point = pixels.get(i);
//                x = (int)(point.getX() + offsetX);
//                y = (int)(point.getY() + offsetY);
//                sbCoords.append(x);
//                sbCoords.append(",");
//                sbCoords.append(y);
//            }
//            
//            //build image url
//            sbUrl.append("http://127.0.0.1:");
//            sbUrl.append(String.valueOf(spsPortNumber));
//            sbUrl.append("/AREASYMBOLFILL?");
//            sbUrl.append("renderer=AreaSymbolFillRenderer&");
//            sbUrl.append(sbCoords.toString());
//            if(symbolFillIDs != null)
//            {
//                sbUrl.append("&symbolFillIds=");
//                sbUrl.append(symbolFillIDs);
//            }
//            if(symbolLineIDs != null)
//            {
//                sbUrl.append("&symbolLineIds=");
//                sbUrl.append(symbolLineIDs);
//            }
//            if(symbolSize>0)
//            {
//                sbUrl.append("&symbolFillIconSize=");
//                sbUrl.append(symbolSize);
//            }
//            if(strClip!=null)
//            {
//                sbUrl.append(strClip);
//            }
//
//
//
//            sbUrl.append("&height=");
//            sbUrl.append(Integer.valueOf((int)height));
//            sbUrl.append("&width=");
//            sbUrl.append(Integer.valueOf((int)width));
//
//            url = sbUrl.toString();
//
//        }
//        catch(Exception exc)
//        {
//            System.out.println(exc.getMessage());
//            exc.printStackTrace();
//        }
//        return url;
//    }
    
    /**
     * Only provide the end of the url like:
     * "AREASYMBOLFILL?renderer=AreaSymbolFillRenderer&height=300&width=300&symbolIDs=SFGP-----------,SHGP-----------"
     * User need to provide the front:
     * "http://127.0.0.1:6789/"
     * @deprecated 
     */
//    public static String GenerateSymbolAreaFillUrl(String clipCoords, String SymbolIDs, int height, int width, int symbolSize)
//    {
//        StringBuilder sbUrl = new StringBuilder();
//        sbUrl.append("http://127.0.0.1:");
//        sbUrl.append(String.valueOf(spsPortNumber));
//        sbUrl.append("/AREASYMBOLFILL?");
//        sbUrl.append("renderer=AreaSymbolFillRenderer&");
//        sbUrl.append(clipCoords);
//        sbUrl.append("&symbolFillIds=");
//        sbUrl.append(SymbolIDs);
//        sbUrl.append("&height=");
//        sbUrl.append(Integer.valueOf((int)height));
//        sbUrl.append("&width=");
//        sbUrl.append(Integer.valueOf((int)width));
//        if(symbolSize>0)
//        {
//            sbUrl.append("&symbolFillIconSize=");
//            sbUrl.append(symbolSize);
//        }
//        return sbUrl.toString();
//    }

     /**
     * Given a symbol code meant for a single point symbol, returns the
     * symbol as a byte array.
     *
     * @param symbolID - the 15 character symbolID of a single point MilStd2525
     * symbol.
     * @return byte array.
     */
    public static byte[] getSinglePointByteArray(String symbolID)
    {
        //return sps.getSinglePointByteArray(symbolID);
    	return null;
    }
    
    
    
    
//    @Override
//    public void destroy() {
//        //sps.stop();
//    }
}
