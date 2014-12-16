/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package armyc2.c2sd.renderer.utilities;
import armyc2.c2sd.graphics2d.*;
import java.util.ArrayList;
import android.graphics.BitmapShader;   //for pattern fill. this is the only native android graphics to import
/**
 *
 * @author michael.spinelli
 */
public class ShapeInfo {


    public static int SHAPE_TYPE_POLYLINE=0;
    //public static int SHAPE_TYPE_POLYGON=1;
    public static int SHAPE_TYPE_FILL=1;
    public static int SHAPE_TYPE_MODIFIER=2;
    public static int SHAPE_TYPE_MODIFIER_FILL=3;
    public static int SHAPE_TYPE_UNIT_FRAME=4;
    public static int SHAPE_TYPE_UNIT_FILL=5;
    public static int SHAPE_TYPE_UNIT_SYMBOL1=6;
    public static int SHAPE_TYPE_UNIT_SYMBOL2=7;
    public static int SHAPE_TYPE_UNIT_DISPLAY_MODIFIER=8;
    public static int SHAPE_TYPE_UNIT_ECHELON=9;
    public static int SHAPE_TYPE_UNIT_AFFILIATION_MODIFIER=10;
    public static int SHAPE_TYPE_UNIT_HQ_STAFF=11;
    public static int SHAPE_TYPE_TG_SP_FILL=12;
    public static int SHAPE_TYPE_TG_SP_FRAME=13;
    public static int SHAPE_TYPE_TG_Q_MODIFIER=14;
    public static int SHAPE_TYPE_TG_SP_OUTLINE=15;
    public static int SHAPE_TYPE_SINGLE_POINT_OUTLINE=16;
    public static int SHAPE_TYPE_UNIT_OUTLINE=17;
    
    protected Shape _Shape;
    private Stroke stroke;
    private GeneralPath gp;
    private String fillStyle;
    private TexturePaint texturePaint;
    private int shapeType=-1;
    private Color lineColor = null;
    private Color fillColor = null;
    private int lineWidth = 2;
//    private AffineTransform affineTransform = null;

    //private GlyphVector _GlyphVector = null;
    private TextLayout _TextLayout = null;
    private Point2D _Position = null;
    private String _ModifierString = null;
    private Point2D _ModifierStringPosition = null;
    private double _ModifierStringAngle = 0;
    private Object _Tag = null;
    private BitmapShader _shader=null;
    //for google earth
    private ArrayList<ArrayList<Point2D>> _Polylines = null;
    
    //for google earth
    //private ArrayList<ArrayList<Point2D>> _Polylines = null;

    //enum DrawMethod{Draw,Fill;}

    //private Polygon poly=new Polygon();
    protected ShapeInfo()
    {

    }

    public ShapeInfo(Shape shape)
    {
        _Shape = shape;
    }

//    public ShapeInfo(GlyphVector glyphVector, Point2D position)
//    {
//        _GlyphVector = glyphVector;
//        _Position = position;
//    }

    public ShapeInfo(TextLayout textLayout, Point2D position)
    {
        _TextLayout = textLayout;
        _Position = position;
    }

    /**
     *
     * @param shape
     * @param shapeType
     * ShapeInfo.SHAPE_TYPE_
     */
    public ShapeInfo(Shape shape, int shapeType)
    {
        _Shape = shape;
    }

    public Shape getShape()
    {
        return _Shape;
    }

    public void setShape(Shape value)
    {
        _Shape = value;
        //_GlyphVector = null;
        _TextLayout = null;
    }

//    public GlyphVector getGlyphVector()
//    {
//        return _GlyphVector;
//    }
//
//    public void setGlyphVector(GlyphVector value, Point2D position)
//    {
//        _GlyphVector = value;
//        _Position = position;
//        _Shape = null;
//        _TextLayout = null;
//    }

    public TextLayout getTextLayout()
    {
        return _TextLayout;
    }

    public void setTextLayout(TextLayout value)
    {
        _TextLayout = value;
        //_GlyphVector = null;
        _Shape = null;
    }

    //set this when returning text string.
    public void setModifierString(String value)
    {
        _ModifierString = value;
    }

    public String getModifierString()
    {
        return _ModifierString;
    }

    //location to draw ModifierString.
    public void setModifierStringPosition(Point2D value)
    {
        _ModifierStringPosition = value;
    }

    public Point2D getModifierStringPosition()
    {
        return _ModifierStringPosition;
    }

    //angle to draw ModifierString.
    public void setModifierStringAngle(double value)
    {
        _ModifierStringAngle = value;
    }

    public double getModifierStringAngle()
    {
        return _ModifierStringAngle;
    }

    /**
     * Object that can be used to store anything.
     * Will not be looked at when rendering.
     * Null by default
     * @param value
     */
    public void setTag(Object value)
    {
        _Tag = value;
    }

    /**
     * Object that can be used to store anything.
     * Will not be looked at when rendering.
     * Null by default
     * @return
     */
    public Object getTag()
    {
        return _Tag;
    }


    /**
     * OLD
     * @return
     *//*
    public Rectangle getBounds()
    {
        Rectangle temp = null;

        if(_Shape != null)
            return _Shape.getBounds();
        else if(_GlyphVector != null)
            return _GlyphVector.getPixelBounds(null, (float)_Position.getX(), (float)_Position.getY());
        else if(_TextLayout != null && _Position != null)
        {
            temp = _TextLayout.getPixelBounds(null, (float)_Position.getX(), (float)_Position.getY());
            return temp;
        }
        else if(_TextLayout != null)//for deutch multipoint labels
        {
            //in this case, user set position using affine tranformation.
            temp = new Rectangle();
            temp.setRect(_TextLayout.getBounds());
            return temp;
        }
        else
            return null;
    }//*/

    /**
     * Gets bounds for the shapes.  Incorporates AffineTransform if not null
     * in the ShapeInfo object.
     * @return
     */
    public Rectangle getBounds()
    {
        Rectangle temp = null;
        if(_Shape != null)
        {
            temp = _Shape.getBounds();
            if(_Shape instanceof GeneralPath)
            {
                if(shapeType == SHAPE_TYPE_UNIT_OUTLINE)
                {
                    if(lineColor != null && stroke != null)
                    {
                        BasicStroke bs = (BasicStroke)stroke;
                        if(bs != null && bs.getLineWidth() > 2)
                          temp.grow((int)bs.getLineWidth()/2, (int)bs.getLineWidth()/2);
                    }
                }
                else
                {
                    //mobility and other drawn symbol decorations.
                    if(lineColor != null && stroke != null)
                    {
                        BasicStroke bs = (BasicStroke)stroke;
                        if(bs != null && bs.getLineWidth() > 2)
                            temp.grow((int)bs.getLineWidth()-1, (int)bs.getLineWidth()-1);
                    }
                }
            }
        }
//        else if(_GlyphVector != null)
//        {
//            temp = _GlyphVector.getPixelBounds(null, (float)_Position.getX(), (float)_Position.getY());
//        }
        if(_TextLayout != null && _Position != null)
        {
            temp = _TextLayout.getPixelBounds(null, (float)_Position.getX(), (float)_Position.getY());

        }
        else if(_TextLayout != null)//for deutch multipoint labels
        {
            temp = new Rectangle(0,0,0,0);
            temp.setRect(_TextLayout.getBounds());
            //return temp;
        }
        else
            return null;


//        if(this.affineTransform != null)
//        {
//            //position set by affinetransform
//            
//            Shape sTemp = temp;
//            sTemp = affineTransform.createTransformedShape(temp);
//            temp = sTemp.getBounds();
//
//        }

        return temp;
    }

    /**
     * needed to draw Glyphs and TextLayouts
     * @param position
     */
    public void setGlyphPosition(Point position)
    {
        _Position = new Point2D.Double(position.x,position.y);
        this._ModifierStringPosition=new Point2D.Double(position.x,position.y);
    }

        /**
     * needed to draw Glyphs and TextLayouts
     * @param position
     */
    public void setGlyphPosition(Point2D position)
    {
        _Position = position;
        this._ModifierStringPosition=new Point2D.Double(position.getX(),position.getY());
    }

    /**
     * needed to draw Glyphs and TextLayouts
     * @return
     */
    public Point2D getGlyphPosition()
    {
        return _Position;
    }

    public void setLineColor(Color value)
    {
        lineColor=value;
    }
    public Color getLineColor()
    {
        return lineColor;
    }

//    /**
//     *
//     * @param value
//     * @deprecated Use setStroke
//     */
//    public void setLineWidth(int value)
//    {
//        lineWidth=value;
//    }
//    /**
//     * @deprecated Use getStroke
//     * @return
//     */
//    public int getLineWidth()
//    {
//        return lineWidth;
//    }

    public void setFillColor(Color value)
    {
        fillColor=value;
    }
    public Color getFillColor()
    {
        return fillColor;
    }

//    public void setAffineTransform(AffineTransform value)
//    {
//        affineTransform=value;
//    }
//    public AffineTransform getAffineTransform()
//    {
//        return affineTransform;
//    }


    public Stroke getStroke()
    {
        return stroke;
    }
    //client will use this to do fills (if it is not null)

    public TexturePaint getTexturePaint()
    {
        return texturePaint;
    }
    public void setTexturePaint(TexturePaint value)
    {
        texturePaint=value;
    }

     public void setStroke(Stroke s)
    {
        stroke=s;
    }

    /**
     * For Internal Renderer use
     * @param value
     * ShapeInfo.SHAPE_TYPE_
     * 
     */
    public void setShapeType(int value)
    {
        shapeType=value;
    }
    /**
     * For Internal Renderer use
     * @return ShapeInfo.SHAPE_TYPE_
     * 
     */
    public int getShapeType()
    {
        return shapeType;
    }

    public ArrayList<ArrayList<Point2D>> getPolylines()
    {
        return _Polylines;
    }

    public void setPolylines(ArrayList<ArrayList<Point2D>> value)
    {
        _Polylines = value;
    }
    public void setShader(BitmapShader value)
    {
        _shader=value;
    }
    public BitmapShader getShader()
    {
        return _shader;
    }
}
