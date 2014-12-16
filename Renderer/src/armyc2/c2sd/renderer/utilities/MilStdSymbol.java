/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package armyc2.c2sd.renderer.utilities;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.util.SparseArray;
//import android.graphics.PointF;
import armyc2.c2sd.graphics2d.*;

/**
 *
 * @author michael.spinelli
 */
public class MilStdSymbol {




        //private SymbolDef _symbolDefinition = null;
        //private UnitDef _unitDefinition = null;

        /**
         * modifiers
         */
        private SparseArray<String> _Properties = null;
        
        //for tactical graphics
        private ArrayList<Double> _X_Altitude = null;
        private ArrayList<Double> _AM_Distance = null;
        private ArrayList<Double> _AN_Azimuth = null;


        private String _symbolID = "";

        /**
         * unique ID for this symbol, for client use
         */
        private String _UUID = null;

        private ArrayList<ShapeInfo> _SymbolShapes;

        /**
         * collection of shapes for the modifiers
         */
        private ArrayList<ShapeInfo> _ModifierShapes;


        private ArrayList<Point2D> _Coordinates;

        private int _UnitSize = 0;
        private double _scale = 0;
        private Boolean _KeepUnitRatio = true;

        Integer _LineWidth = 3;
        Color _LineColor = null;
        Color _FillColor = null;
        
        double _Rotation = 0.0;//DEGREES
        
        //outline singlepoint TGs
        boolean _Outline = false;
        //if null, renderer determines outline Color.
        Color _OutLineColor = null;
        int _OutLineWidth = 0;
        TexturePaint _tp=null;
        /**
        * 2525Bch2 and USAS 13/14 symbology
        */
        public static final int Symbology_2525Bch2_USAS_13_14 = 0;
        /**
        * 2525C, which includes 2525Bch2 & USAS 13/14
        */
        public static final int Symbology_2525C = 1;

        private static int _SymbologyStandard = 0;
        
        private static boolean _DrawAffiliationModifierAsLabel = true;
        
        private static boolean _UseLineInterpolation = false;

        Object _Tag = null;

		/**
		 * Used to hold metadata for each segment of the symbol for multi-point symbols.  Each segment can contain one
		 * object.
		 */
		//private Map _segmentData;


		// Constants for dynamic properties
/*
		public static final String SYMBOL_ID = "Symbol ID";
		//public static final String SOURCE = "Source";
		//public static final String EDITOR_CLASS_TYPE = "Editor Class Type";
		public static final String URN = "URN";
		public static final String UIC = "UIC";
		public static final String ANGLE_OF_ROTATION = "Angle of Rotation";
		public static final String LENGTH = "Length";
		public static final String WIDTH = "Width";
		public static final String RADIUS = "Radius";
		public static final String SEGMENT_DATA = "Segment Data";
*/

/*
                public static final String GEO_POINT = "point";
		public static final String GEO_LINE = "line";
		public static final String GEO_POLYGON = "area";
		public static final String GEO_TEXT = "text";
		public static final String GEO_CIRCLE = "circle";
		public static final String GEO_RECTANGLE = "rectangle";
		public static final String GEO_ARC = "arc";
		public static final String GEO_SQUARE = "square";
*/
                /*
                private static final String _COORDINATES = "Coordinates";
		private static final String _GEOMETRY = "Geometry";
		private static final String _FILL_COLOR = "Fill Color";
		private static final String _FILL_ALPHA = "Fill Alpha";
		private static final String _FILL_STYLE = "Fill Style";
		private static final String _LINE_WIDTH = "Line Width";
		private static final String _LINE_COLOR = "Line Color";
		private static final String _LINE_ALPHA = "Line Alpha";
		private static final String _TEXT_BACKGROUND_COLOR = "Background Color";
		private static final String _TEXT_FOREGROUND_COLOR = "Foreground Color";
		private static final String _USE_FILL = "Use Fill";
*/
		/*
		protected static const _COORDINATES:String = "Coordinates";
		protected static const _GEOMETRY:String = "Geometry";
		protected static const _FILL_COLOR:String = "Fill Color";
		protected static const _FILL_ALPHA:String = "Fill Alpha";
		private int _FILL_STYLE:String = "Fill Style";
		protected static const _LINE_WIDTH:String = 0;
		private Color _LINE_COLOR = Color.BLACK;
		private int _LINE_ALPHA:String = 0;
		private Color _TEXT_BACKGROUND_COLOR = Color.WHITE;
		private Color _TEXT_FOREGROUND_COLOR = Color.BLACK;
		private bool _USE_FILL:String = "Use Fill";*/

		/**
		 * Creates a new MilStdSymbol.
		 *
		 * @param symbolID code, 15 characters long that represents the symbol
		 * @param uniqueUD for the client's use

		 *
		 */

        /**
         *
         * @param symbolID code, 15 characters long that represents the symbol
         * @param uniqueID for the client's use
         * @param modifiers use keys from ModifiersTG or ModifiersUnits.
         * @param Coordinates
         * @throws RendererException
         * NULL is a valid value if you have no set modifiers
         */
		public MilStdSymbol(String symbolID, String uniqueID, ArrayList<Point2D> Coordinates, SparseArray<String>  modifiers)
		{
                    this(symbolID, uniqueID, Coordinates, modifiers, true);
		}
                
        /**
         *
         * @param symbolID code, 15 characters long that represents the symbol
         * @param uniqueID for the client's use
         * @param modifiers use keys from ModifiersTG or ModifiersUnits.
         * @param Coordinates
         * @param keepUnitRatio - default TRUE
         * @throws RendererException
         * NULL is a valid value if you have no set modifiers
         */
        public MilStdSymbol(String symbolID, String uniqueID, ArrayList<Point2D> Coordinates, SparseArray<String> modifiers, Boolean keepUnitRatio)
		{

	        if(modifiers == null)
	        	_Properties = new SparseArray<String>();
	        else
            _Properties = modifiers;

            _UUID = uniqueID;
            setCoordinates(Coordinates);

			// Set the given symbol id
			setSymbolID(symbolID);

            // Set up default line and fill colors based on affiliation
            setLineColor(SymbolUtilities.getLineColorOfAffiliation(_symbolID));
            //if(SymbolUtilities.isWarfighting(_symbolID))
            if(SymbolUtilities.hasDefaultFill(_symbolID))
                setFillColor(SymbolUtilities.getFillColorOfAffiliation(_symbolID));
            //if(SymbolUtilities.isNBC(_symbolID) && !(SymbolUtilities.isDeconPoint(symbolID)))
            //    setFillColor(SymbolUtilities.getFillColorOfAffiliation(_symbolID));
            
            
            setSymbologyStandard(RendererSettings.getInstance().getSymbologyStandard());
            
            _DrawAffiliationModifierAsLabel = RendererSettings.getInstance().getDrawAffiliationModifierAsLabel();
            
            _UseLineInterpolation = RendererSettings.getInstance().getUseLineInterpolation();
                        
		}
                
        /**
        * Controls what symbols are supported.
        * Set this before loading the renderer.
        * @param symbologyStandard
        * Like RendererSettings.Symbology_2525Bch2_USAS_13_14
        */
        public void setSymbologyStandard(int standard)
        {
            _SymbologyStandard = standard;
        }
        public TexturePaint getFillStyle()
        {
            return _tp;
        }
        public void setFillStyle(TexturePaint value)
        {
            _tp=value;
        }
        /**
        * Current symbology standard
        * @return symbologyStandard
        * Like RendererSettings.Symbology_2525Bch2_USAS_13_14
        */
        public int getSymbologyStandard()
        {
            return _SymbologyStandard;
        }
        
        public void setUseLineInterpolation(boolean value)
        {
            _UseLineInterpolation = value;
        }
        
        public boolean getUseLineInterpolation()
        {
            return _UseLineInterpolation;
        }
        
        /**
        * Determines how to draw the Affiliation Modifier.
        * True to draw as modifier label in the "E/F" location.
        * False to draw at the top right corner of the symbol
        */
        public void setDrawAffiliationModifierAsLabel(boolean value)
        {
            _DrawAffiliationModifierAsLabel = value;
        }
        /**
        * True to draw as modifier label in the "E/F" location.
        * False to draw at the top right corner of the symbol
        */
        public boolean getDrawAffiliationModifierAsLabel()
        {
            return _DrawAffiliationModifierAsLabel;
        }

        /**
         *
         * @return
         */
        public SparseArray<String>  getModifierMap()
        {
            return _Properties;
        }

        /**
         *
         * @param modifiers
         */
        public void setModifierMap(SparseArray<String> modifiers)
        {
            _Properties = modifiers;
        }

        /**
         *
         * @param modifier
         * @return
         */
        public String getModifier(int modifier)
        {
            if(_Properties.indexOfKey(modifier) >= 0)
                return _Properties.get(modifier);
            else
            {
                return getModifier(modifier,0);
            }
        }

        /**
         *
         * @param modifier
         * @param value
         */
        public void setModifier(int modifier, String value)
        {
            if(value.equals("")==false)
            {
                if(!(modifier == ModifiersTG.AM_DISTANCE) ||
                        modifier == (ModifiersTG.AN_AZIMUTH) ||
                        modifier == (ModifiersTG.X_ALTITUDE_DEPTH))
                {
                    _Properties.put(modifier, value);
                }
                else
                {
                    setModifier(modifier, value, 0);
                }
            }
        }

        /**
         *
         * @param modifier
         * @param index
         * @return
         */
        public String getModifier(int modifier, int index)
        {
            if(_Properties.indexOfKey(modifier)>=0)
                return _Properties.get(modifier);
            else if(modifier ==  (ModifiersTG.AM_DISTANCE) ||
                    modifier == (ModifiersTG.AN_AZIMUTH) ||
                    modifier == (ModifiersTG.X_ALTITUDE_DEPTH))
            {
                String value = String.valueOf(getModifier_AM_AN_X(modifier, index));
                if(value != null && !value.equalsIgnoreCase("null") && !value.equalsIgnoreCase(""))
                    return value;
                else
                    return null;
            }
            else
                return null;
            
        }

        /**
         *
         * @param modifier
         * @param index
         * @return
         */
        public Double getModifier_AM_AN_X(int modifier, int index)
        {
            ArrayList<Double> modifiers = null;
            if(modifier == (ModifiersTG.AM_DISTANCE))
                modifiers = _AM_Distance;
            else if(modifier == (ModifiersTG.AN_AZIMUTH))
                modifiers = _AN_Azimuth;
            else if(modifier == (ModifiersTG.X_ALTITUDE_DEPTH))
                modifiers = _X_Altitude;
            else
                return null;

            if(modifiers != null && modifiers.size() > index)
            {
                Double value = null;
                value = modifiers.get(index);
                if(value != null)
                    return value;
                else
                    return null;
            }
            else
                return null;
        }

        /**
         * Modifiers must be added in order.
         * No setting index 2 without first setting index 0 and 1.
         * If setting out of order is attempted, the value will just
         * be added to the end of the list.
         * @param modifier
         * @param value
         * @param index
         */
        public void setModifier(int modifier, String value, int index)
        {
            if(value.equals("")==false)
            {
                if(!(modifier == (ModifiersTG.AM_DISTANCE) ||
                        modifier == (ModifiersTG.AN_AZIMUTH) ||
                        modifier == (ModifiersTG.X_ALTITUDE_DEPTH)))
                {
                    _Properties.put(modifier, value);
                }
                else
                {
                    Double dblValue = Double.valueOf(value);
                    if(dblValue != null)
                        setModifier_AM_AN_X(modifier, dblValue, index);
                }
            }
        }

        public void setModifier_AM_AN_X(int modifier, Double value, int index)
        {
            if((modifier == (ModifiersTG.AM_DISTANCE) ||
                    modifier == (ModifiersTG.AN_AZIMUTH) ||
                    modifier == (ModifiersTG.X_ALTITUDE_DEPTH)))
            {
                ArrayList<Double> modifiers = null;
                if(modifier == (ModifiersTG.AM_DISTANCE))
                {
                    if(_AM_Distance == null)
                        _AM_Distance = new ArrayList<Double>();
                    modifiers = _AM_Distance;
                }
                else if(modifier == (ModifiersTG.AN_AZIMUTH))
                {
                    if(_AN_Azimuth == null)
                        _AN_Azimuth = new ArrayList<Double>();
                    modifiers = _AN_Azimuth;
                }
                else if(modifier == (ModifiersTG.X_ALTITUDE_DEPTH))
                {
                    if(_X_Altitude == null)
                        _X_Altitude = new ArrayList<Double>();
                    modifiers = _X_Altitude;
                }
                if(index + 1 > modifiers.size())
                {
                    modifiers.add(value);
                }
                else
                    modifiers.set(index,value);
            }
        }

        public ArrayList<Double> getModifiers_AM_AN_X(int modifier)
        {
            if(modifier == (ModifiersTG.AM_DISTANCE))
                return _AM_Distance;
            else if(modifier == (ModifiersTG.AN_AZIMUTH))
                return _AN_Azimuth;
            else if(modifier == (ModifiersTG.X_ALTITUDE_DEPTH))
                return _X_Altitude;

            return null;
        }

        public void setModifiers_AM_AN_X(int modifier, ArrayList<Double> modifiers)
        {
            if(modifier == (ModifiersTG.AM_DISTANCE))
                _AM_Distance = modifiers;
            else if(modifier == (ModifiersTG.AN_AZIMUTH))
                _AN_Azimuth = modifiers;
            else if(modifier == (ModifiersTG.X_ALTITUDE_DEPTH))
                _X_Altitude = modifiers;
        }


        /**
         *
         * @param value
         */
        public void setFillColor(Color value)
		{
			_FillColor = value;
		}

        /**
         *
         * @return
         */
        public Color getFillColor()
    	{
                return _FillColor;
    	}


        /**
         *
         * @param value
         */
        public void setLineWidth(int value)
        {
            _LineWidth = value;
        }

        /**
         *
         * @return
         */
        public int getLineWidth()
        {
            return _LineWidth;
        	}

        /**
         *
         * @param value
         */
        public void setLineColor(Color value)
        {
            _LineColor = value;
        }

        /**
         *
         * @return
         */
        public Color getLineColor()
        {
            return _LineColor;
        }
        

        /**
         * if null, renderer will white or black for the outline based
         * on the color of the symbol.  Otherwise, it will used the
         * passed color value.
         * @param value 
         */
        public void setOutlineColor(Color value)
        {
            _OutLineColor = value;
        }
        public Color getOutlineColor()
        {
            return _OutLineColor;
        }



                /**
                 * Extra value for client.
                 * defaults to null.
                 * Not used for rendering by JavaRenderer
                 * @param value
                 * @deprecated 
                 */
                public void setTag(Object value)
		{
                    _Tag = value;
		}


                /**
                 * Extra value for client.
                 * defaults to null.
                 * Not used for rendering by JavaRenderer
                 * @return
                 */
		public Object getTag()
		{
                    return _Tag;
		}


                /**
                 *
                 * @param value
                 */
                public void setCoordinates(ArrayList<Point2D> value)
		{
                    _Coordinates = value;
		}

                /**
                 *
                 * @return
                 */
                public ArrayList<Point2D> getCoordinates()
		{
                    return _Coordinates;
		}


                /**
                 * Shapes that represent the symbol modifiers
                 * @param value ArrayList<Shape>
                 */
                public void setModifierShapes(ArrayList<ShapeInfo> value)
		{
                    _ModifierShapes = value;
		}

                /**
                 * Shapes that represent the symbol modifiers
                 * @return
                 */
		public ArrayList<ShapeInfo> getModifierShapes()
		{
                    return _ModifierShapes;
		}

                /**
                 * the java shapes that make up the symbol
                 * @param value ArrayList<ShapeInfo>
                 */
                public void setSymbolShapes(ArrayList<ShapeInfo> value)
		{
                    _SymbolShapes = value;
		}

                /**
                 * the java shapes that make up the symbol
                 * @return
                 */
		public ArrayList<ShapeInfo> getSymbolShapes()
		{
                    return _SymbolShapes;
		}


		/**
		 * The Symbol Id of the MilStdSymbol.
                 *
                 * @return 
                 */
		public String getSymbolID()
		{
			return _symbolID;
		}

                /**
                 * Unique ID of the Symbol.  For client use.
                 *
                 * @return
                 */
                public String getUUID()
                {
                    return _UUID;
                }

                /**
                 * Unique ID of the Symbol.  For client use.
                 *
                 * @param ID
                 */
                public void setUUID(String ID)
                {
                    _UUID = ID;
                }

		/**
                 * Sets the Symbol ID for the symbol.  Should be a 15
                 * character string from the milstd.
                 * @param value
                 * @throws RendererException
                 */
		public void setSymbolID(String value)
		{

			String current = _symbolID;

			try
			{
                //set symbolID
                if(value != null && !value.equals("") && !current.equals(value))
                {
                        _symbolID = value;
                }

                //if hostile and specific TG, need to set 'N' to "ENY"
                if(SymbolUtilities.getAffiliation(value) == 'H')
                {
                    String basicID = SymbolUtilities.getBasicSymbolID(value);
                    if(SymbolUtilities.isObstacle(basicID) || //any obstacle
                            basicID.equals("G*M*NZ----****X") ||//ground zero
                            basicID.equals("G*M*NEB---****X") ||//biological
                            basicID.equals("G*M*NEC---****X"))//chemical )
                    {
                        this.setModifier(ModifiersTG.N_HOSTILE, "ENY");
                    }
                }
                            
			}// End try
			catch(Exception e)
			{
				// Log Error
				ErrorLogger.LogException("MilStdSymbol", "setSymbolID" + " - Did not fall under TG or FE", e);
			}
		}	// End set SymbolID



}
