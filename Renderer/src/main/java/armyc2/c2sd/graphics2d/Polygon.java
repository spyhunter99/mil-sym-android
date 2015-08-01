package armyc2.c2sd.graphics2d;
/*
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

import android.graphics.Path;
import android.graphics.RectF;
import armyc2.c2sd.JavaLineArray.POINT2;
/**
 * Polygon
 * This class provides support for polygon shapes. It is based directly
 * on the java.awt.Polygon class available in Java.
 *
 * @see http://java.sun.com/j2se/1.4.2/docs/api/java/awt/Polygon.html
 * @see http://developer.android.com/reference/android/graphics/Path.html
 *
 * @author Paul Gregoire (mondain@gmail.com)
 */
public final class Polygon extends Path implements Shape{
//private final static String tag = "Polygon";
    /**
     * Bounds of the polygon
     */
    Rectangle2D bounds;
    /**
     * Total number of points
     */
    int npoints = 0;
    /**
     * Array of x coordinate
     */
    int[] xpoints;
    /**
     * Array of y coordinates
     */
    int[] ypoints;

    /**
     * Creates an empty polygon with 16 points
     */
    public Polygon() {
        this(1000);
    }

    /**
     * Creates an empty polygon expecting a given number of points
     *
     * @param numPoints the total number of points in the Polygon
     */
    public Polygon(int numPoints) {
        super();
        xpoints = new int[numPoints];
        ypoints = new int[numPoints];
    }

    /**
     * Constructs and initializes a Polygon from the specified parameters
     *
     * @param xpoints an array of x coordinates
     * @param ypoints an array of y coordinates
     * @param npoints the total number of points in the Polygon
     */
    public Polygon(int[] xpoints, int[] ypoints, int npoints) {
        super();
        this.xpoints = xpoints;
        this.ypoints = ypoints;
        this.npoints = npoints;
        moveTo(xpoints[0], ypoints[0]);
        for (int p = 1; p < npoints; p++) {
            lineTo(xpoints[p], ypoints[p]);
        }
        close();
    }

    /**
     * Appends the specified coordinates to this Polygon. Remember to close
     * the polygon after adding all the points.
     */
    public void addPoint(int x, int y) {
        xpoints[npoints] = x;
        ypoints[npoints] = y;
        if (npoints > 0) {
            lineTo(x, y);
        } else {
            moveTo(x, y);
        }
        npoints++;
    }

    /**
     * Determines whether the specified Point is inside this Polygon
     *
     * @param p the specified Point to be tested
     * @return true if the Polygon contains the Point; false otherwise
     */
    public boolean contains(Point p) 
    {
        return contains((float)p.x,(float)p.y);
    }

    /**
     * Determines whether the specified coordinates are inside this Polygon
     *
     * @param x the specified x coordinate to be tested
     * @param y the specified y coordinate to be tested
     * @return true if this Polygon contains the specified coordinates, (x, y);
     * false otherwise
     */
    public boolean contains(int x, int y) 
    {
        return contains((float)x,(float)y);
    }

    /**
     * added method M. Deutch 2-25-13
     * @param x
     * @param y
     * @return 
     */
    public boolean contains(float x, float y) 
    {                    
        //buildPath();
        if(npoints<3)
            return false;
        
        if(  (xpoints[0] != xpoints[npoints-1])  ||  (ypoints[0] != ypoints[npoints-1])  )
        {
            addPoint(xpoints[0],ypoints[0]);
        }
        float lowVal=-100;
        POINT2 pt0=new POINT2(lowVal,y);
        POINT2 pt1=new POINT2(x,y);
        Line2D edge=null;
        Line2D ray=new Line2D.Double(pt0.x,pt0.y,pt1.x,pt1.y);
        int j=0;
        int intersectCounter=0;                
        
        for(j=0;j<npoints-1;j++)
        {
            edge=new Line2D.Double(xpoints[j],ypoints[j],xpoints[j+1],ypoints[j+1]);
            if(ray.intersectsLine(edge))
                intersectCounter++;                
        }
        if(intersectCounter%2==1)
            return true;

        return false;
    }
    public boolean contains(Point2D pt)
    {
        //buildPath();
        return contains((float)pt.getX(),(float)pt.getY());
    }
    public boolean contains(double x, double y) {
        //buildPath();
        return contains((float)x,(float)y);
    }
    /**
     * Tests if the interior of this Polygon entirely contains the specified
     * set of rectangular coordinates
     *
     * @param x the x coordinate of the top-left corner of the specified set of
     * rectangular coordinates
     * @param y the y coordinate of the top-left corner of the specified set of
     * rectangular coordinates
     * @param w the width of the set of rectangular coordinates
     * @param h the height of the set of rectangular coordinates
     * @return
     */
    public boolean contains(double x, double y, double w, double h) {
        if (bounds != null) {
            float fx = (float) x;
            float fy = (float) y;
            float fw = (float) w;
            float fh = (float) h;
//not sure if math is correct here
            Path that = new Path();
//start
            that.moveTo(fx, fy);
//go right
            that.lineTo(fx + fw, fy);
//go down
            that.lineTo(fx + fw, fy - fh);
//go left
            that.lineTo(fx, fy - fh);
//close
            that.close();
//bounds holder
            Rectangle2D thatBounds = new Rectangle2D.Double();            
            RectF rectf=new RectF((float)thatBounds.x,(float)thatBounds.y,(float)thatBounds.x+(float)thatBounds.width,(float)thatBounds.y+(float)thatBounds.height);
            that.computeBounds(rectf, false);
            return bounds.contains(thatBounds);
        } else {
            return false;
        }
    }

    /**
     * Tests if the interior of this Polygon entirely contains the specified
     * Rectangle
     *
     * @param r the specified RectF
     * @return true if this Polygon entirely contains the specified RectF;
     * false otherwise.
     */
    public boolean contains(Rectangle2D r) {
        if (bounds != null) {
            
            return bounds.contains(r);
        } else {
            return false;
        }
    }

    /**
     * Tests if the interior of this Polygon intersects the interior of a
     * specified set of rectangular coordinates
     *
     * @param x the x coordinate of the specified rectangular shape's top-left
     * corner
     * @param y the y coordinate of the specified rectangular shape's top-left
     * corner
     * @param w the width of the specified rectangular shape
     * @param h the height of the specified rectangular shape
     * @return
     */
    public boolean intersects(double x, double y, double w, double h) {
        if (bounds != null) {
            float fx = (float) x;
            float fy = (float) y;
            float fw = (float) w;
            float fh = (float) h;
//not sure if math is correct here
            Path that = new Path();
//start
            that.moveTo(fx, fy);
//go right
            that.lineTo(fx + fw, fy);
//go down
            that.lineTo(fx + fw, fy - fh);
//go left
            that.lineTo(fx, fy - fh);
//close
            that.close();
//bounds holder
            RectF thatBounds = new RectF();
            RectF rectf=new RectF((float)bounds.x,(float)bounds.y,(float)bounds.x+(float)bounds.width,(float)bounds.y+(float)bounds.height);
            return RectF.intersects(rectf, thatBounds);
        } else {
            return false;
        }
    }

    /**
     * Tests if the interior of this Polygon intersects the interior of a
     * specified Rectangle
     *
     * @param r a specified RectF
     * @return true if this Polygon and the interior of the specified RectF
     * intersect each other; false otherwise
     */
    public boolean intersects(RectF r) {
        if (bounds != null) {
            RectF rectf=new RectF((float)bounds.x,(float)bounds.y,(float)bounds.x+(float)bounds.width,(float)bounds.y+(float)bounds.height);
            return RectF.intersects(rectf, r);
        } else {
            return false;
        }
    }

    /**
     * Invalidates or flushes any internally-cached data that depends on the
     * vertex coordinates of this Polygon
     */
    public void invalidate() {
        //reset();
        xpoints = new int[npoints];
        ypoints = new int[npoints];
        bounds = null;
    }

    /**
     * Close the current contour and generate the bounds.
     */
    @Override
    public void close() {
        super.close();
        bounds = new Rectangle2D.Double();
        RectF rectf=new RectF((float)bounds.x,(float)bounds.y,(float)bounds.x+(float)bounds.width,(float)bounds.y+(float)bounds.height);
        this.computeBounds(rectf, false);
    }
    public boolean contains (int x, int y, int width, int height)
    {
        return true;
    }
    public Rectangle2D getBounds2D()
    {
        return null;
    }
    public Rectangle getBounds()
    {
        return null;
    }
    public boolean intersects(Rectangle2D rect)
    {
        return false;
    }
    public PathIterator getPathIterator(AffineTransform at)
    {
        PathIterator pi=new PathIterator(null);
        int j=0;
        if(npoints>0)
        {
            pi.moveTo(xpoints[0], ypoints[0]);
            for(j=1;j<npoints;j++)
            {
                pi.lineTo(xpoints[j], ypoints[j]);
            }
        }
        pi.reset();
        return pi;
    }
    
}
