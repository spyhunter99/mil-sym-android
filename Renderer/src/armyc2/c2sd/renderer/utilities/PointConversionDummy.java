/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package armyc2.c2sd.renderer.utilities;

//import android.graphics.Point;
import android.graphics.PointF;
//import armyc2.c2sd.graphics2d.Point2D;
//import armyc2.c2sd.graphics2d.Point2D;
import armyc2.c2sd.graphics2d.*;

/**
 * Makes no change to the passed points.  Useful for when the points
 * are already in pixels.
 * @author michael.spinelli
 */
public class PointConversionDummy implements IPointConversion {


    public PointConversionDummy()
    {
    }

    public PointF PixelsToGeo(PointF pixel)
    {
    	PointF coords = new PointF();

        coords.x = pixel.x;
        coords.y = pixel.y;

        return coords;
    }

    public PointF GeoToPixels(PointF coord)
    {
    	PointF pixel = new PointF();

        pixel.x = (int)coord.x;

        pixel.y = (int)coord.y;

        return pixel;
    }

//	@Override
//	public Point2D PixelsToGeo(Point pixel) {
//		return new Point2D.Double(pixel.x,pixel.y);
//	}

//	@Override
//	public Point GeoToPixels(Point2D coord) {
//
//		return new Point((int)coord.getX(),(int)coord.getY());
//	}

	@Override
	public Point2D PixelsToGeo(Point2D pixel) {

		return new Point2D.Double(pixel.getX(),pixel.getY());
	}

	//@Override
	public Point2D GeoToPixels(Point2D coord) {

		return new Point2D.Double(coord.getX(),coord.getY());
	}



}
