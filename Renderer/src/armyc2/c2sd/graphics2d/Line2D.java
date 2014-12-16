/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package armyc2.c2sd.graphics2d;
/*
 * Copyright 1997-2006 Sun Microsystems, Inc. All Rights Reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation. Sun designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Sun in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Sun Microsystems, Inc., 4150 Network Circle, Santa Clara,
 * CA 95054 USA or visit www.sun.com if you need additional information or
 * have any questions.
 */

import java.io.Serializable;
import armyc2.c2sd.JavaLineArray.lineutility;
/**
 * This <code>Line2D</code> represents a line segment in {@code (x,y)}
 * coordinate space. This class, like all of the Java 2D API, uses a default
 * coordinate system called <i>user space</i> in which the y-axis values
 * increase downward and x-axis values increase to the right. For more
 * information on the user space coordinate system, see the <a href=
 * "http://java.sun.com/j2se/1.3/docs/guide/2d/spec/j2d-intro.fm2.html#61857">
 * Coordinate Systems</a> section of the Java 2D Programmer's Guide.
 * <p>
 * This class is only the abstract superclass for all objects that store a 2D
 * line segment. The actual storage representation of the coordinates is left to
 * the subclass.
 *
 * @author Jim Graham
 * @since 1.2
 */
public abstract class Line2D {
        public Rectangle2D getBounds2D()
        {
            double x1=getX1();
            double y1=getY1();
            double x2=getX1();
            double y2=getY1();
            double x=x1;
            double y=y1;
            if(x2<x1)
                x=x2;
            if(y2<y1)
                y=y2;
            double width=Math.abs(x1-x2);
            double height=Math.abs(y1-y2);
            Rectangle2D rect=new Rectangle2D.Double(x,y,width,height);
            return rect;
        }

        public boolean intersectsLine(Line2D edge)
        {
            double x1=getX1();
            double y1=getY1();
            double x2=getX2();
            double y2=getY2();
            double edgex1=edge.getX1();
            double edgey1=edge.getY1();
            double edgex2=edge.getX2();
            double edgey2=edge.getY2();
            
            //handle vertical lines
            if(x2==x1 && edgex2==edgex1)
            {
                if(x1 != edgex1)
                    return false;
                
                if(y1<y2)
                {
                    if(y1<=edgey1 && edgey1<=y2)
                        return true;
                    else if(y1<=edgey2 && edgey2<=y2)
                        return true;
                    else
                        return false;
                }
                else if(y2<y1)
                {
                    if(y2<=edgey1 && edgey1<=y1)
                        return true;
                    else if(y2<=edgey2 && edgey2<=y1)
                        return true;
                    else
                        return false;                    
                }
            }
            //do the mbr's intersect?
            if(x1<x2)   //was x1<=x2
            {
                if(edgex1<x1 && edgex2<x1)
                    return false;
                if(edgex1>x2 && edgex2>x2)
                    return false;
            }
            else if(x2<x1)
            {
                if(edgex1<x2 && edgex2<x2)
                    return false;
                if(edgex1>x1 && edgex2>x1)
                    return false;                
            }
            if(y1<y2)   //was y1<=y2
            {
                if(edgey1<y1 && edgey2<x1)
                    return false;
                if(edgey1>y2 && edgey2>y2)
                    return false;
            }
            else if(y2<y1)
            {
                if(edgey1<y2 && edgey2<y2)
                    return false;
                if(edgey1>y1 && edgey2>y1)
                    return false;                
            }
            if(x1==x2)
            {
                if(x1<edgex1 && x1<edgex2)
                    return false;
                if(x1>edgex1 && x1>edgex2)
                    return false;
            }
            if(y1==y2)
            {
                if(y1<edgey1 && y1<edgey2)
                    return false;
                if(y1>edgey1 && y1>edgey2)
                    return false;
            }
            
            //if we reach this point we have nonvertical lines with intersecting mbr's
            double slope=(y2-y1)/(x2-x1);
            double b1=y2-slope*x2;
            double edgeSlope=(edgey2-edgey1)/(edgex2-edgex1);
            double b2=edgey2-edgeSlope*edgex2;
            Rectangle2D rect=new Rectangle2D.Double(x1,y1,x2,y2);
            Rectangle2D rect2=new Rectangle2D.Double(edgex1,edgey1,Math.abs(edgex1-edgex2),Math.abs(edgey1-edgey2));
            if(slope==edgeSlope)
            {
                if(b1==b2 && rect.intersects(rect2)==true)
                    return true;
                else
                    return false;
            }
            else
            {   
                //non-vertical lines
                //calculate the theoretical intersection point x,y from the two line equations
                double x=(b2-b1)/(slope-edgeSlope);
                double y=(slope*x+b1);
                //the first rect
                if(x1<x2)
                {
                    if(x<x1)
                        return false;
                    if(x>x2)
                        return false;
                }
                else if(x2<x1)
                {
                    if(x<x2)
                        return false;
                    if(x>x1)
                        return false;                    
                }
                if(y1<y2)
                {
                    if(y<y1)
                        return false;
                    if(y>y2)
                        return false;
                }
                else if(y2<y1)
                {
                    if(y<y2)
                        return false;
                    if(y>y1)
                        return false;                    
                }
                //the edge rect
                if(edgex1<edgex2)
                {
                    if(x<edgex1)
                        return false;
                    if(x>edgex2)
                        return false;
                }
                else if(edgex2<edgex1)
                {
                    if(x<edgex2)
                        return false;
                    if(x>edgex1)
                        return false;                    
                }
                if(edgey1<edgey2)
                {
                    if(y<edgey1)
                        return false;
                    if(y>edgey2)
                        return false;
                }
                else if(edgey2<edgey1)
                {
                    if(y<edgey2)
                        return false;
                    if(y>edgey1)
                        return false;                    
                }
            }            
            return true;
        }
    /**
     * A line segment specified with float coordinates.
     *
     * @since 1.2
     */
    public static class Float extends Line2D implements Serializable {

        /**
         * The X coordinate of the start point of the line segment.
         *
         * @since 1.2
         * @serial
         */
        public float x1;
        /**
         * The Y coordinate of the start point of the line segment.
         *
         * @since 1.2
         * @serial
         */
        public float y1;
        /**
         * The X coordinate of the end point of the line segment.
         *
         * @since 1.2
         * @serial
         */
        public float x2;
        /**
         * The Y coordinate of the end point of the line segment.
         *
         * @since 1.2
        Line2D.java : » App » android-app-examples » org » loon » framework » android » game ...Page 1 of 14
        http://www.java2s.com/Open-Source/Android/App/android-app-examples/org/loon/frame... 2/25/2013
         * @serial
         */
        public float y2;

        /**
         * Constructs and initializes a Line with coordinates (0, 0) -> (0, 0).
         *
         * @since 1.2
         */
        public Float() {
        }

        /**
         * Constructs and initializes a Line from the specified coordinates.
         *
         * @param x1
         * the X coordinate of the start point
         * @param y1
         * the Y coordinate of the start point
         * @param x2
         * the X coordinate of the end point
         * @param y2
         * the Y coordinate of the end point
         * @since 1.2
         */
        public Float(float x1, float y1, float x2, float y2) {
            setLine(x1, y1, x2, y2);
        }

        /**
         * Constructs and initializes a <code>Line2D</code> from the specified
         * <code>Point2D</code> objects.
         *
         * @param p1
         * the start <code>Point2D</code> of this line segment
         * @param p2
         * the end <code>Point2D</code> of this line segment
         * @since 1.2
         */
        public Float(Point2D p1, Point2D p2) {
            setLine(p1, p2);
        }

        /**
         * {@inheritDoc}
         *
         * @since 1.2
         */
        public double getX1() {
            return (double) x1;
        }

        /**
         * {@inheritDoc}
         *
         * @since 1.2
         */
        public double getY1() {
            return (double) y1;
        }

        /**
         * {@inheritDoc}
         *
         * @since 1.2
         */
        public Point2D getP1() {
            return new Point2D.Float(x1, y1);
        }

        /**
         * {@inheritDoc}
         *
         * @since 1.2
         */
        public double getX2() {
            return (double) x2;
        }

        /**
         * {@inheritDoc}
         *
         * @since 1.2
         */
        public double getY2() {
            return (double) y2;
        }

        /**
         * {@inheritDoc}
         *
        Line2D.java : » App » android-app-examples » org » loon » framework » android » game ...Page 2 of 14
        http://www.java2s.com/Open-Source/Android/App/android-app-examples/org/loon/frame... 2/25/2013
         * @since 1.2
         */
        public Point2D getP2() {
            return new Point2D.Float(x2, y2);
        }

        /**
         * {@inheritDoc}
         *
         * @since 1.2
         */
        public void setLine(double x1, double y1, double x2, double y2) {
            this.x1 = (float) x1;
            this.y1 = (float) y1;
            this.x2 = (float) x2;
            this.y2 = (float) y2;
        }

        /**
         * Sets the location of the end points of this <code>Line2D</code> to
         * the specified float coordinates.
         *
         * @param x1
         * the X coordinate of the start point
         * @param y1
         * the Y coordinate of the start point
         * @param x2
         * the X coordinate of the end point
         * @param y2
         * the Y coordinate of the end point
         * @since 1.2
         */
        public void setLine(float x1, float y1, float x2, float y2) {
            this.x1 = x1;
            this.y1 = y1;
            this.x2 = x2;
            this.y2 = y2;
        }

        /**
         * {@inheritDoc}
         *
         * @since 1.2
         */
        public Rectangle2D getBounds2D() {
            float x, y, w, h;
            if (x1 < x2) {
                x = x1;
                w = x2 - x1;
            } else {
                x = x2;
                w = x1 - x2;
            }
            if (y1 < y2) {
                y = y1;
                h = y2 - y1;
            } else {
                y = y2;
                h = y1 - y2;
            }
            return new Rectangle2D.Double(x, y, w, h);
        }
        /*
         * JDK 1.6 serialVersionUID
         */
        private static final long serialVersionUID = 6161772511649436349L;
    }
    
    /**
     * A line segment specified with double coordinates.
     *
     * @since 1.2
     */
    public static class Double extends Line2D implements Serializable {

//        public boolean intersectsLine(Line2D.Double edge)
//        {
//            double x1=this.x1;
//            double y1=this.y1;
//            double x2=this.x2;
//            double y2=this.y2;
//            double edgex1=edge.x1;
//            double edgey1=edge.y1;
//            double edgex2=edge.x2;
//            double edgey2=edge.y2;
//            
//            //handle vertical lines
//            if(x2==x1 && edgex2==edgex1)
//            {
//                if(x1 != edgex1)
//                    return false;
//                                
//                if(y1<y2)
//                {
//                    if(y1<=edgey1 && edgey1<=y2)
//                        return true;
//                    else if(y1<=edgey2 && edgey2<=y2)
//                        return true;
//                    else
//                        return false;
//                }
//                else if(y2<y1)
//                {
//                    if(y2<=edgey1 && edgey1<=y1)
//                        return true;
//                    else if(y2<=edgey2 && edgey2<=y1)
//                        return true;
//                    else
//                        return false;                    
//                }
//            }
//            
//            //if we reach this point we have nonvertical lines
//            double slope=(y2-y1)/(x2-x1);
//            double b1=y2-slope*x2;
//            double edgeSlope=(edgey2-edgey1)/(edgex2-edgex1);
//            double b2=edgey2-edgeSlope*edgex2;
//            Rectangle2D rect=new Rectangle2D.Double(x1,y1,x2,y2);
//            Rectangle2D rect2=new Rectangle2D.Double(edgex1,edgey1,Math.abs(edgex1-edgex2),Math.abs(edgey1-edgey2));
//            if(slope==edgeSlope)
//            {
//                if(b1==b2 && rect.intersects(rect2)==true)
//                    return true;
//                else
//                    return false;
//            }
//            else
//            {   
//                //non-vertical lines
//                //calculate the theoretical intersection point x,y from the two line equations
//                double x=(b2-b1)/(slope-edgeSlope);
//                double y=(slope*x+b1);
//                //the first rect
//                if(x1<x2)
//                {
//                    if(x<x1)
//                        return false;
//                    if(x>x2)
//                        return false;
//                }
//                else if(x2<x1)
//                {
//                    if(x<x2)
//                        return false;
//                    if(x>x1)
//                        return false;                    
//                }
//                if(y1<y2)
//                {
//                    if(y<y1)
//                        return false;
//                    if(y>y2)
//                        return false;
//                }
//                else if(y2<y1)
//                {
//                    if(y<y2)
//                        return false;
//                    if(y>y1)
//                        return false;                    
//                }
//                //the edge rect
//                if(edgex1<edgex2)
//                {
//                    if(x<edgex1)
//                        return false;
//                    if(x>edgex2)
//                        return false;
//                }
//                else if(edgex2<edgex1)
//                {
//                    if(x<edgex2)
//                        return false;
//                    if(x>edgex1)
//                        return false;                    
//                }
//                if(edgey1<edgey2)
//                {
//                    if(y<edgey1)
//                        return false;
//                    if(y>edgey2)
//                        return false;
//                }
//                else if(edgey2<edgey1)
//                {
//                    if(y<edgey2)
//                        return false;
//                    if(y>edgey1)
//                        return false;                    
//                }
//            }            
//            return true;
//        }
        /**
         * The X coordinate of the start point of the line segment.
         *
         * @since 1.2
         * @serial
         */
        public double x1;
        /**
         * The Y coordinate of the start point of the line segment.
         *
         * @since 1.2
         * @serial
         */
        public double y1;
        /**
         * The X coordinate of the end point of the line segment.
         *
         * @since 1.2
         * @serial
         */
        public double x2;
        /**
         * The Y coordinate of the end point of the line segment.
         *
         * @since 1.2
         * @serial
         */
        public double y2;

        /**
         * Constructs and initializes a Line with coordinates (0, 0) -> (0, 0).
         *
         * @since 1.2
         */
        public Double() {
        }

        /**
         * Constructs and initializes a <code>Line2D</code> from the specified
         * coordinates.
         *
         * @param x1
         * the X coordinate of the start point
         * @param y1
         * the Y coordinate of the start point
         * @param x2
         * the X coordinate of the end point
         * @param y2
         * the Y coordinate of the end point
         * @since 1.2
         */
        public Double(double x1, double y1, double x2, double y2) {
            setLine(x1, y1, x2, y2);
        }

        /**
         * Constructs and initializes a <code>Line2D</code> from the specified
         * <code>Point2D</code> objects.
         *
         * @param p1
         * the start <code>Point2D</code> of this line segment
         * @param p2
         * the end <code>Point2D</code> of this line segment
         * @since 1.2
         */
        public Double(Point2D p1, Point2D p2) {
            setLine(p1, p2);
        }

        /**
         * {@inheritDoc}
         *
         * @since 1.2
         */
        public double getX1() {
            return x1;
        }

        /**
         * {@inheritDoc}
         *
         * @since 1.2
         */
        public double getY1() {
            return y1;
        }

        /**
         * {@inheritDoc}
         *
         * @since 1.2
         */
        public Point2D getP1() {
            return new Point2D.Double(x1, y1);
        }

        /**
         * {@inheritDoc}
         *
         * @since 1.2
         */
        public double getX2() {
            return x2;
        }

        /**
         * {@inheritDoc}
         *
         * @since 1.2
         */
        public double getY2() {
            return y2;
        }

        /**
         * {@inheritDoc}
         *
         * @since 1.2
         */
        public Point2D getP2() {
            return new Point2D.Double(x2, y2);
        }

        /**
         * {@inheritDoc}
         *
         * @since 1.2
         */
        public void setLine(double x1, double y1, double x2, double y2) {
            this.x1 = x1;
            this.y1 = y1;
            this.x2 = x2;
            this.y2 = y2;
        }

        /**
         * {@inheritDoc}
         *
         * @since 1.2
         */
        public Rectangle2D getBounds2D() {
            double x, y, w, h;
            if (x1 < x2) {
                x = x1;
                w = x2 - x1;
            } else {
                x = x2;
                w = x1 - x2;
            }
            if (y1 < y2) {
                y = y1;
                h = y2 - y1;
            } else {
                y = y2;
                h = y1 - y2;
            }
            return new Rectangle2D.Double(x, y, w, h);
        }
        /*
         * JDK 1.6 serialVersionUID
         */
        private static final long serialVersionUID = 7979627399746467499L;
    }

    /**
     * This is an abstract class that cannot be instantiated directly.
     * Type-specific implementation subclasses are available for instantiation
     * and provide a number of formats for storing the information necessary to
     * satisfy the various accessory methods below.
     *
     * @see and.awt.geom.Line2D.Float
     * @see and.awt.geom.Line2D.Double
     * @since 1.2
     */
    protected Line2D() {
    }

    /**
     * Returns the X coordinate of the start point in double precision.
     *
     * @return the X coordinate of the start point of this {@code Line2D}
     * object.
     * @since 1.2
     */
    public abstract double getX1();

    /**
     * Returns the Y coordinate of the start point in double precision.
     *
     * @return the Y coordinate of the start point of this {@code Line2D}
     * object.
     * @since 1.2
     */
    public abstract double getY1();

    /**
     * Returns the start <code>Point2D</code> of this <code>Line2D</code>.
     *
     * @return the start <code>Point2D</code> of this <code>Line2D</code>.
     * @since 1.2
     */
    public abstract Point2D getP1();

    /**
     * Returns the X coordinate of the end point in double precision.
     *
     * @return the X coordinate of the end point of this {@code Line2D} object.
     * @since 1.2
     */
    public abstract double getX2();

    /**
     * Returns the Y coordinate of the end point in double precision.
     *
     * @return the Y coordinate of the end point of this {@code Line2D} object.
     * @since 1.2
     */
    public abstract double getY2();

    /**
     * Returns the end <code>Point2D</code> of this <code>Line2D</code>.
     *
     * @return the end <code>Point2D</code> of this <code>Line2D</code>.
     * @since 1.2
     */
    public abstract Point2D getP2();

    /**
     * Sets the location of the end points of this <code>Line2D</code> to the
     * specified double coordinates.
     *
     * @param x1
     * the X coordinate of the start point
     * @param y1
     * the Y coordinate of the start point
     * @param x2
     * the X coordinate of the end point
     * @param y2
     * the Y coordinate of the end point
     * @since 1.2
     */
    public abstract void setLine(double x1, double y1, double x2, double y2);

    /**
     * Sets the location of the end points of this <code>Line2D</code> to the
     * specified <code>Point2D</code> coordinates.
     *
     * @param p1
     * the start <code>Point2D</code> of the line segment
     * @param p2
     * the end <code>Point2D</code> of the line segment
     * @since 1.2
     */
    public void setLine(Point2D p1, Point2D p2) {
        setLine(p1.getX(), p1.getY(), p2.getX(), p2.getY());
    }

    /**
     * Sets the location of the end points of this <code>Line2D</code> to the
     * same as those end points of the specified <code>Line2D</code>.
     *
     * @param l
     * the specified <code>Line2D</code>
     * @since 1.2
     */
    public void setLine(Line2D l) {
        setLine(l.getX1(), l.getY1(), l.getX2(), l.getY2());
    }

    /**
     * Returns an indicator of where the specified point {@code (px,py)} lies
     * with respect to the line segment from {@code (x1,y1)} to {@code (x2,y2)}.
     * The return value can be either 1, -1, or 0 and indicates in which
     * direction the specified line must pivot around its first end point,
     * {@code (x1,y1)}, in order to point at the specified point {@code (px,py)}
     * .
     * <p>
     * A return value of 1 indicates that the line segment must turn in the
     * direction that takes the positive X axis towards the negative Y axis. In
     * the default coordinate system used by Java 2D, this direction is
     * counterclockwise.
     * <p>
     * A return value of -1 indicates that the line segment must turn in the
     * direction that takes the positive X axis towards the positive Y axis. In
     * the default coordinate system, this direction is clockwise.
    Line2D.java : » App » android-app-examples » org » loon » framework » android » game ...Page 6 of 14
    http://www.java2s.com/Open-Source/Android/App/android-app-examples/org/loon/frame... 2/25/2013
     * <p>
     * A return value of 0 indicates that the point lies exactly on the line
     * segment. Note that an indicator value of 0 is rare and not useful for
     * determining colinearity because of floating point rounding issues.
     * <p>
     * If the point is colinear with the line segment, but not between the end
     * points, then the value will be -1 if the point lies "beyond {@code
     * (x1,y1)}" or 1 if the point lies "beyond {@code (x2,y2)}".
     *
     * @param x1
     * the X coordinate of the start point of the specified line
     * segment
     * @param y1
     * the Y coordinate of the start point of the specified line
     * segment
     * @param x2
     * the X coordinate of the end point of the specified line
     * segment
     * @param y2
     * the Y coordinate of the end point of the specified line
     * segment
     * @param px
     * the X coordinate of the specified point to be compared with
     * the specified line segment
     * @param py
     * the Y coordinate of the specified point to be compared with
     * the specified line segment
     * @return an integer that indicates the position of the third specified
     * coordinates with respect to the line segment formed by the first
     * two specified coordinates.
     * @since 1.2
     */
    public static int relativeCCW(double x1, double y1, double x2, double y2,
            double px, double py) {
        x2 -= x1;
        y2 -= y1;
        px -= x1;
        py -= y1;
        double ccw = px * y2 - py * x2;
        if (ccw == 0.0) {
// The point is colinear, classify based on which side of
// the segment the point falls on. We can calculate a
// relative value using the projection of px,py onto the
// segment - a negative value indicates the point projects
// outside of the segment in the direction of the particular
// endpoint used as the origin for the projection.
            ccw = px * x2 + py * y2;
            if (ccw > 0.0) {
// Reverse the projection to be relative to the original x2,y2
// x2 and y2 are simply negated.
// px and py need to have (x2 - x1) or (y2 - y1) subtracted
// from them (based on the original values)
// Since we really want to get a positive answer when the
// point is "beyond (x2,y2)", then we want to calculate
// the inverse anyway - thus we leave x2 & y2 negated.
                px -= x2;
                py -= y2;
                ccw = px * x2 + py * y2;
                if (ccw < 0.0) {
                    ccw = 0.0;
                }
            }
        }
        return (ccw < 0.0) ? -1 : ((ccw > 0.0) ? 1 : 0);
    }

    /**
     * Returns an indicator of where the specified point {@code (px,py)} lies
     * with respect to this line segment. See the method comments of
     * {@link #relativeCCW(double, double, double, double, double, double)} to
     * interpret the return value.
     *
     * @param px
     * the X coordinate of the specified point to be compared with
     * this <code>Line2D</code>
     * @param py
     * the Y coordinate of the specified point to be compared with
     * this <code>Line2D</code>
     * @return an integer that indicates the position of the specified
     * coordinates with respect to this <code>Line2D</code>
     * @see #relativeCCW(double, double, double, double, double, double)
     * @since 1.2
     */
    public int relativeCCW(double px, double py) {
        return relativeCCW(getX1(), getY1(), getX2(), getY2(), px, py);
    }

    /**
     * Returns an indicator of where the specified <code>Point2D</code> lies
     * with respect to this line segment. See the method comments of
     * {@link #relativeCCW(double, double, double, double, double, double)} to
    Line2D.java : » App » android-app-examples » org » loon » framework » android » game ...Page 7 of 14
    http://www.java2s.com/Open-Source/Android/App/android-app-examples/org/loon/frame... 2/25/2013
     * i nt e r pr e t t he r e t ur n va l ue .
     *
     * + pa r am p
     * t he s pe c i f i e d , c ode - Poi nt 2D, / c ode - t o . e c ompa r e d wi t h t hi s
     * , c ode - Li ne 2D, / c ode -
     * + r e t ur n a n i nt e ge r t ha t i ndi c a t e s t he pos i t i on of t he s pe c i f i e d
     * , c ode - Poi nt 2D, / c ode - wi t h r e s pe c t t o t hi s , c ode - Li ne 2D, / c ode -
     * + s e e / r e l a t i ve 0 0 1 2 dou. l e 3 dou. l e 3 dou. l e 3 dou. l e 3 dou. l e 3 dou. l e 4
     * + s i nc e 1. 2
     * /
    public int r e l a t i ve 0 0 1 2 Poi nt 2D p4 5
    return r e l a t i ve 0 0 1 2 ge t 6 12 4 3 ge t 7 12 4 3 ge t 6 22 4 3 ge t 7 22 4 3 p. ge t 6 2 4 3 p
    . ge t 7 2 4 4 8
    9
    / * *
     * : e s t s i f t he l i ne s e gme nt f r om 5 + c ode 2 x13 ; 14 9 t o 5 + c ode 2 x23 ; 24 9
     * i nt e r s e c t s t he l i ne s e gme nt f r om 5 + c ode 2 x33 ; 34 9 t o 5 + c ode 2 x43 ; 44 9 .
     *
     * + pa r am x1
     * t he 6 c oor di na t e of t he s t a r t poi nt of t he f i r s t s pe c i f i e d
     * l i ne s e gme nt
     * + pa r am ; 1
     * t he 7 c oor di na t e of t he s t a r t poi nt of t he f i r s t s pe c i f i e d
     * l i ne s e gme nt
     * + pa r am x2
     * t he 6 c oor di na t e of t he e nd poi nt of t he f i r s t s pe c i f i e d l i ne
     * s e gme nt
     * + pa r am ; 2
     * t he 7 c oor di na t e of t he e nd poi nt of t he f i r s t s pe c i f i e d l i ne
     * s e gme nt
     * + pa r am x3
     * t he 6 c oor di na t e of t he s t a r t poi nt of t he s e c ond s pe c i f i e d
     * l i ne s e gme nt
     * + pa r am ; 3
     * t he 7 c oor di na t e of t he s t a r t poi nt of t he s e c ond s pe c i f i e d
     * l i ne s e gme nt
     * + pa r am x4
     * t he 6 c oor di na t e of t he e nd poi nt of t he s e c ond s pe c i f i e d l i ne
     * s e gme nt
     * + pa r am ; 4
     * t he 7 c oor di na t e of t he e nd poi nt of t he s e c ond s pe c i f i e d l i ne
     * s e gme nt
     * + r e t ur n , c ode - t r ue , / c ode - i f t he f i r s t s pe c i f i e d l i ne s e gme nt a nd t he
     * s e c ond s pe c i f i e d l i ne s e gme nt i nt e r s e c t e a c h ot he r 8
     * , c ode - f a l s e , / c ode - ot he r wi s e .
     * + s i nc e 1. 2
     * /
    public static boolean l i ne s < nt e r s e c t 2 double x13 double ; 13 double x23
    double ; 23 double x33 double ; 33 double x43 double ; 44 5
    return 2 2 r e l a t i ve 0 0 1 2 x13 ; 13 x23 ; 23 x33 ; 34
     * r e l a t i ve 0 0 1 2 x13 ; 13 x23 ; 23 x43 ; 44 , = 04 > > 2 r e l a t i ve 0 0 1 2 x33
    ; 33 x43 ; 43 x13 ; 14
     * r e l a t i ve 0 0 1 2 x33 ; 33 x43 ; 43 x23 ; 24 , = 04 4 8
    9
    / * *
     * : e s t s i f t he l i ne s e gme nt f r om 5 + c ode 2 x13 ; 14 9 t o 5 + c ode 2 x23 ; 24 9
     * i nt e r s e c t s t hi s l i ne s e gme nt .
     *
     * + pa r am x1
     * t he 6 c oor di na t e of t he s t a r t poi nt of t he s pe c i f i e d l i ne
     * s e gme nt
     * + pa r am ; 1
     * t he 7 c oor di na t e of t he s t a r t poi nt of t he s pe c i f i e d l i ne
     * s e gme nt
     * + pa r am x2
     * t he 6 c oor di na t e of t he e nd poi nt of t he s pe c i f i e d l i ne
     * s e gme nt
     * + pa r am ; 2
     * t he 7 c oor di na t e of t he e nd poi nt of t he s pe c i f i e d l i ne
     * s e gme nt
     * + r e t ur n , t r ue - i f t hi s l i ne s e gme nt a nd t he s pe c i f i e d l i ne s e gme nt
     * i nt e r s e c t e a c h ot he r 8 , c ode - f a l s e , / c ode - ot he r wi s e .
     * + s i nc e 1. 2
     * /
    public boolean i nt e r s e c t s Li ne 2 double x13 double ; 13 double x23 double ; 24 5
    return l i ne s < nt e r s e c t 2 x13 ; 13 x23 ; 23 ge t 6 12 4 3 ge t 7 12 4 3 ge t 6 22 4 3
    ge t 7 22 4 4 8
    9
    / * *
     * : e s t s i f t he s pe c i f i e d l i ne s e gme nt i nt e r s e c t s t hi s l i ne s e gme nt .
     *
     * + pa r am l
     * t he s pe c i f i e d , c ode - Li ne 2D, / c ode -
     * + r e t ur n , c ode - t r ue , / c ode - i f t hi s l i ne s e gme nt a nd t he s pe c i f i e d l i ne
     * s e gme nt i nt e r s e c t e a c h ot he r 8 , c ode - f a l s e , / c ode - ot he r wi s e .
     * + s i nc e 1. 2
     * /
    public boolean i nt e r s e c t s Li ne 2 Li ne 2D l 4 5
    Line2D.java : » App » android-app-examples » org » loon » framework » android » game ...Page 8 of 14
    http://www.java2s.com/Open-Source/Android/App/android-app-examples/org/loon/frame... 2/25/2013
    return l i ne s * nt e r s e c t + l . ge t , 1+ - . l . ge t / 1+ - . l . ge t , 2+ - . l . ge t / 2+ - .
    ge t , 1+ - . ge t / 1+ - . ge t , 2+ - . ge t / 2+ - - 0
    1
    / 2 2
    2 3 e t ur ns t he s 4 ua r e of t he di s t a nc e f r om a poi nt t o a l i ne s e gme nt . 5 he
    2 di s t a nc e me a s ur e d i s t he di s t a nc e 6 e t we e n t he s pe c i f i e d poi nt a nd t he
    2 c l os e s t poi nt 6 e t we e n t he s pe c i f i e d e nd poi nt s . * f t he s pe c i f i e d poi nt
    2 i nt e r s e c t s t he l i ne s e gme nt i n 6 e t we e n t he e nd poi nt s . t hi s me t hod
    2 r e t ur ns 0. 0.
    2
    2 7 pa r am x1
    2 t he , c oor di na t e of t he s t a r t poi nt of t he s pe c i f i e d l i ne
    2 s e gme nt
    2 7 pa r am 8 1
    2 t he / c oor di na t e of t he s t a r t poi nt of t he s pe c i f i e d l i ne
    2 s e gme nt
    2 7 pa r am x2
    2 t he , c oor di na t e of t he e nd poi nt of t he s pe c i f i e d l i ne
    2 s e gme nt
    2 7 pa r am 8 2
    2 t he / c oor di na t e of t he e nd poi nt of t he s pe c i f i e d l i ne
    2 s e gme nt
    2 7 pa r am px
    2 t he , c oor di na t e of t he s pe c i f i e d poi nt 6 e i ng me a s ur e d a ga i ns t
    2 t he s pe c i f i e d l i ne s e gme nt
    2 7 pa r am p8
    2 t he / c oor di na t e of t he s pe c i f i e d poi nt 6 e i ng me a s ur e d a ga i ns t
    2 t he s pe c i f i e d l i ne s e gme nt
    2 7 r e t ur n a dou6 l e va l ue t ha t i s t he s 4 ua r e of t he di s t a nc e f r om t he
    2 s pe c i f i e d poi nt t o t he s pe c i f i e d l i ne s e gme nt .
    2 7 s e e 9 pt Li neDi s t S4 + dou6 l e . dou6 l e . dou6 l e . dou6 l e . dou6 l e . dou6 l e -
    2 7 s i nc e 1. 2
    2 /
    public static double pt Se gDi s t S4 + double x1. double 8 1. double x2.
    double 8 2. double px. double p8 - :
    / / Adj us t ve c t or s r e l a t i ve t o x1. 8 1
    / / x2. 8 2 6 e c ome s r e l a t i ve ve c t or f r om x1. 8 1 t o e nd of s e gme nt
    x2 - ; x10
    8 2 - ; 8 10
    / / px. p8 6 e c ome s r e l a t i ve ve c t or f r om x1. 8 1 t o t e s t poi nt
    px - ; x10
    p8 - ; 8 10
    double dot pr od ; px 2 x2 < p8 2 8 20
    double pr oj l e nS4 0
    if + dot pr od = ; 0. 0- :
    / / px. p8 i s on t he s i de of x1. 8 1 awa 8 f r om x2. 8 2
    / / di s t a nc e t o s e gme nt i s l e ngt h of px. p8 ve c t or
    / / > l e ngt h of i t s + c l i ppe d- pr oj e c t i on> i s now 0. 0
    pr oj l e nS4 ; 0. 00
    1 else :
    / / s wi t c h t o 6 a c kwa r ds ve c t or s r e l a t i ve t o x2. 8 2
    / / x2. 8 2 a r e a l r e a d8 t he ne ga t i ve of x1. 8 1; ? x2. 8 2
    / / t o ge t px. p8 t o 6 e t he ne ga t i ve of px. p8 ; ? x2. 8 2
    / / t he dot pr oduc t of t wo ne ga t e d ve c t or s i s t he s ame
    / / a s t he dot pr oduc t of t he t wo nor ma l ve c t or s
    px ; x2 - px0
    p8 ; 8 2 - p8 0
    dot pr od ; px 2 x2 < p8 2 8 20
    if + dot pr od = ; 0. 0- :
    / / px. p8 i s on t he s i de of x2. 8 2 awa 8 f r om x1. 8 1
    / / di s t a nc e t o s e gme nt i s l e ngt h of + 6 a c kwa r ds - px. p8 ve c t or
    / / > l e ngt h of i t s + c l i ppe d- pr oj e c t i on> i s now 0. 0
    pr oj l e nS4 ; 0. 00
    1 else :
    / / px. p8 i s 6 e t we e n x1. 8 1 a nd x2. 8 2
    / / dot pr od i s t he l e ngt h of t he px. p8 ve c t or
    / / pr oj e c t e d on t he x2. 8 2; ? x1. 8 1 ve c t or t i me s t he
    / / l e ngt h of t he x2. 8 2; ? x1. 8 1 ve c t or
    pr oj l e nS4 ; dot pr od 2 dot pr od / + x2 2 x2 < 8 2 2 8 2- 0
    1
    1
    / / Di s t a nc e t o l i ne i s now t he l e ngt h of t he r e l a t i ve poi nt
    / / ve c t or mi nus t he l e ngt h of i t s pr oj e c t i on ont o t he l i ne
    / / + whi c h i s @ e r o i f t he pr oj e c t i on f a l l s out s i de t he r a nge
    / / of t he l i ne s e gme nt - .
    double l e nS4 ; px 2 px < p8 2 p8 - pr oj l e nS4 0
    if + l e nS4 = 0- :
    l e nS4 ; 00
    1
    return l e nS4 0
    1
    / 2 2
    2 3 e t ur ns t he di s t a nc e f r om a poi nt t o a l i ne s e gme nt . 5 he di s t a nc e
    2 me a s ur e d i s t he di s t a nc e 6 e t we e n t he s pe c i f i e d poi nt a nd t he c l os e s t
    2 poi nt 6 e t we e n t he s pe c i f i e d e nd poi nt s . * f t he s pe c i f i e d poi nt i nt e r s e c t s
    2 t he l i ne s e gme nt i n 6 e t we e n t he e nd poi nt s . t hi s me t hod r e t ur ns 0. 0.
    2
    2 7 pa r am x1
    2 t he , c oor di na t e of t he s t a r t poi nt of t he s pe c i f i e d l i ne
    Line2D.java : » App » android-app-examples » org » loon » framework » android » game ...Page 9 of 14
    http://www.java2s.com/Open-Source/Android/App/android-app-examples/org/loon/frame... 2/25/2013
     * s e gme nt
     * + pa r am , 1
     * t he - c oor di na t e of t he s t a r t poi nt of t he s pe c i f i e d l i ne
     * s e gme nt
     * + pa r am x2
     * t he . c oor di na t e of t he e nd poi nt of t he s pe c i f i e d l i ne
     * s e gme nt
     * + pa r am , 2
     * t he - c oor di na t e of t he e nd poi nt of t he s pe c i f i e d l i ne
     * s e gme nt
     * + pa r am px
     * t he . c oor di na t e of t he s pe c i f i e d poi nt / e i ng me a s ur e d a ga i ns t
     * t he s pe c i f i e d l i ne s e gme nt
     * + pa r am p,
     * t he - c oor di na t e of t he s pe c i f i e d poi nt / e i ng me a s ur e d a ga i ns t
     * t he s pe c i f i e d l i ne s e gme nt
     * + r e t ur n a dou/ l e va l ue t ha t i s t he di s t a nc e f r om t he s pe c i f i e d poi nt t o
     * t he s pe c i f i e d l i ne s e gme nt .
     * + s e e 0 pt Li neDi s t 1 dou/ l e 2 dou/ l e 2 dou/ l e 2 dou/ l e 2 dou/ l e 2 dou/ l e 3
     * + s i nc e 1. 2
     * /
    public static double pt Se gDi s t 1 double x12 double , 12 double x22 double , 22
    double px2 double p, 3 4
    return 5 a t h. s 6 r t 1 pt Se gDi s t S6 1 x12 , 12 x22 , 22 px2 p, 3 3 7
    8
    / * *
     * 9 e t ur ns t he s 6 ua r e of t he di s t a nc e f r om a poi nt t o t hi s l i ne s e gme nt . : he
     * di s t a nc e me a s ur e d i s t he di s t a nc e / e t we e n t he s pe c i f i e d poi nt a nd t he
     * c l os e s t poi nt / e t we e n t he c ur r e nt l i ne ; s e nd poi nt s . < f t he s pe c i f i e d
     * poi nt i nt e r s e c t s t he l i ne s e gme nt i n / e t we e n t he e nd poi nt s 2 t hi s me t hod
     * r e t ur ns 0. 0.
     *
     * + pa r am px
     * t he . c oor di na t e of t he s pe c i f i e d poi nt / e i ng me a s ur e d a ga i ns t
     * t hi s l i ne s e gme nt
     * + pa r am p,
     * t he - c oor di na t e of t he s pe c i f i e d poi nt / e i ng me a s ur e d a ga i ns t
     * t hi s l i ne s e gme nt
     * + r e t ur n a dou/ l e va l ue t ha t i s t he s 6 ua r e of t he di s t a nc e f r om t he
     * s pe c i f i e d poi nt t o t he c ur r e nt l i ne s e gme nt .
     * + s e e 0 pt Li neDi s t S6 1 dou/ l e 2 dou/ l e 3
     * + s i nc e 1. 2
     * /
    public double pt Se gDi s t S6 1 double px2 double p, 3 4
    return pt Se gDi s t S6 1 ge t . 11 3 2 ge t - 11 3 2 ge t . 21 3 2 ge t - 21 3 2 px2 p, 3 7
    8
    / * *
     * 9 e t ur ns t he s 6 ua r e of t he di s t a nc e f r om a = c ode > Poi nt 2D= / c ode > t o t hi s
     * l i ne s e gme nt . : he di s t a nc e me a s ur e d i s t he di s t a nc e / e t we e n t he s pe c i f i e d
     * poi nt a nd t he c l os e s t poi nt / e t we e n t he c ur r e nt l i ne ; s e nd poi nt s . < f t he
     * s pe c i f i e d poi nt i nt e r s e c t s t he l i ne s e gme nt i n / e t we e n t he e nd poi nt s 2
     * t hi s me t hod r e t ur ns 0. 0.
     *
     * + pa r am pt
     * t he s pe c i f i e d = c ode > Poi nt 2D= / c ode > / e i ng me a s ur e d a ga i ns t t hi s
     * l i ne s e gme nt .
     * + r e t ur n a dou/ l e va l ue t ha t i s t he s 6 ua r e of t he di s t a nc e f r om t he
     * s pe c i f i e d = c ode > Poi nt 2D= / c ode > t o t he c ur r e nt l i ne s e gme nt .
     * + s e e 0 pt Li neDi s t S6 1 Poi nt 2D3
     * + s i nc e 1. 2
     * /
    public double pt Se gDi s t S6 1 Poi nt 2D pt 3 4
    return pt Se gDi s t S6 1 ge t . 11 3 2 ge t - 11 3 2 ge t . 21 3 2 ge t - 21 3 2 pt . ge t . 1 3 2 pt
    . ge t - 1 3 3 7
    8
    / * *
     * 9 e t ur ns t he di s t a nc e f r om a poi nt t o t hi s l i ne s e gme nt . : he di s t a nc e
     * me a s ur e d i s t he di s t a nc e / e t we e n t he s pe c i f i e d poi nt a nd t he c l os e s t
     * poi nt / e t we e n t he c ur r e nt l i ne ; s e nd poi nt s . < f t he s pe c i f i e d poi nt
     * i nt e r s e c t s t he l i ne s e gme nt i n / e t we e n t he e nd poi nt s 2 t hi s me t hod
     * r e t ur ns 0. 0.
     *
     * + pa r am px
     * t he . c oor di na t e of t he s pe c i f i e d poi nt / e i ng me a s ur e d a ga i ns t
     * t hi s l i ne s e gme nt
     * + pa r am p,
     * t he - c oor di na t e of t he s pe c i f i e d poi nt / e i ng me a s ur e d a ga i ns t
     * t hi s l i ne s e gme nt
     * + r e t ur n a dou/ l e va l ue t ha t i s t he di s t a nc e f r om t he s pe c i f i e d poi nt t o
     * t he c ur r e nt l i ne s e gme nt .
     * + s e e 0 pt Li neDi s t 1 dou/ l e 2 dou/ l e 3
     * + s i nc e 1. 2
     * /
    public double pt Se gDi s t 1 double px2 double p, 3 4
    return pt Se gDi s t 1 ge t . 11 3 2 ge t - 11 3 2 ge t . 21 3 2 ge t - 21 3 2 px2 p, 3 7
    8
    / * *
    Line2D.java : » App » android-app-examples » org » loon » framework » android » ga... Page 10 of 14
    http://www.java2s.com/Open-Source/Android/App/android-app-examples/org/loon/frame... 2/25/2013
     * Returns the distance from a <code>Point2D</code> to this line segment.
     * The distance measured is the distance between the specified point and the
     * closest point between the current line's end points. If the specified
     * point intersects the line segment in between the end points, this method
     * returns 0.0.
     *
     * @param pt
     * the specified <code>Point2D</code> being measured against this
     * line segment
     * @return a double value that is the distance from the specified
     * <code>Point2D</code> to the current line segment.
     * @see #ptLineDist(Point2D)
     * @since 1.2
     */
//public double ptSegDist(Point2D pt) {
//return ptSegDist(getX1(), getY1(), getX2(), getY2(), pt.getX(), pt
//.getY());
//}
    /**
     * Returns the square of the distance from a point to a line. The distance
     * measured is the distance between the specified point and the closest
     * point on the infinitely-extended line defined by the specified
     * coordinates. If the specified point intersects the line, this method
     * returns 0.0.
     *
     * @param x1
     * the X coordinate of the start point of the specified line
     * @param y1
     * the Y coordinate of the start point of the specified line
     * @param x2
     * the X coordinate of the end point of the specified line
     * @param y2
     * the Y coordinate of the end point of the specified line
     * @param px
     * the X coordinate of the specified point being measured against
     * the specified line
     * @param py
     * the Y coordinate of the specified point being measured against
     * the specified line
     * @return a double value that is the square of the distance from the
     * specified point to the specified line.
     * @see #ptSegDistSq(double, double, double, double, double, double)
     * @since 1.2
     */
    public static double ptLineDistSq(double x1, double y1, double x2,
            double y2, double px, double py) {
// Adjust vectors relative to x1,y1
// x2,y2 becomes relative vector from x1,y1 to end of segment
        x2 -= x1;
        y2 -= y1;
// px,py becomes relative vector from x1,y1 to test point
        px -= x1;
        py -= y1;
        double dotprod = px * x2 + py * y2;
// dotprod is the length of the px,py vector
// projected on the x1,y1=>x2,y2 vector times the
// length of the x1,y1=>x2,y2 vector
        double projlenSq = dotprod * dotprod / (x2 * x2 + y2 * y2);
// Distance to line is now the length of the relative point
// vector minus the length of its projection onto the line
        double lenSq = px * px + py * py - projlenSq;
        if (lenSq < 0) {
            lenSq = 0;
        }
        return lenSq;
    }

    /**
     * Returns the distance from a point to a line. The distance measured is the
     * distance between the specified point and the closest point on the
     * infinitely-extended line defined by the specified coordinates. If the
     * specified point intersects the line, this method returns 0.0.
     *
     * @param x1
     * the X coordinate of the start point of the specified line
     * @param y1
     * the Y coordinate of the start point of the specified line
     * @param x2
     * the X coordinate of the end point of the specified line
     * @param y2
     * the Y coordinate of the end point of the specified line
     * @param px
     * the X coordinate of the specified point being measured against
     * the specified line
     * @param py
     * the Y coordinate of the specified point being measured against
     * the specified line
     * @return a double value that is the distance from the specified point to
     * the specified line.
     * @see #ptSegDist(double, double, double, double, double, double)
    Line2D.java : » App » android-app-examples » org » loon » framework » android » ga... Page 11 of 14
    http://www.java2s.com/Open-Source/Android/App/android-app-examples/org/loon/frame... 2/25/2013
     * @since 1.2
     */
    public static double ptLineDist(double x1, double y1, double x2, double y2,
            double px, double py) {
        return Math.sqrt(ptLineDistSq(x1, y1, x2, y2, px, py));
    }

    /**
     * Returns the square of the distance from a point to this line. The
     * distance measured is the distance between the specified point and the
     * closest point on the infinitely-extended line defined by this
     * <code>Line2D</code>. If the specified point intersects the line, this
     * method returns 0.0.
     *
     * @param px
     * the X coordinate of the specified point being measured against
     * this line
     * @param py
     * the Y coordinate of the specified point being measured against
     * this line
     * @return a double value that is the square of the distance from a
     * specified point to the current line.
     * @see #ptSegDistSq(double, double)
     * @since 1.2
     */
    public double ptLineDistSq(double px, double py) {
        return ptLineDistSq(getX1(), getY1(), getX2(), getY2(), px, py);
    }

    /**
     * Returns the square of the distance from a specified <code>Point2D</code>
     * to this line. The distance measured is the distance between the specified
     * point and the closest point on the infinitely-extended line defined by
     * this <code>Line2D</code>. If the specified point intersects the line,
     * this method returns 0.0.
     *
     * @param pt
     * the specified <code>Point2D</code> being measured against this
     * line
     * @return a double value that is the square of the distance from a
     * specified <code>Point2D</code> to the current line.
     * @see #ptSegDistSq(Point2D)
     * @since 1.2
     */
    public double ptLineDistSq(Point2D pt) {
        return ptLineDistSq(getX1(), getY1(), getX2(), getY2(), pt.getX(), pt.getY());
    }

    /**
     * Returns the distance from a point to this line. The distance measured is
     * the distance between the specified point and the closest point on the
     * infinitely-extended line defined by this <code>Line2D</code>. If the
     * specified point intersects the line, this method returns 0.0.
     *
     * @param px
     * the X coordinate of the specified point being measured against
     * this line
     * @param py
     * the Y coordinate of the specified point being measured against
     * this line
     * @return a double value that is the distance from a specified point to the
     * current line.
     * @see #ptSegDist(double, double)
     * @since 1.2
     */
    public double ptLineDist(double px, double py) {
        return ptLineDist(getX1(), getY1(), getX2(), getY2(), px, py);
    }

    /**
     * Returns the distance from a <code>Point2D</code> to this line. The
     * distance measured is the distance between the specified point and the
     * closest point on the infinitely-extended line defined by this
     * <code>Line2D</code>. If the specified point intersects the line, this
     * method returns 0.0.
     *
     * @param pt
     * the specified <code>Point2D</code> being measured
     * @return a double value that is the distance from a specified
     * <code>Point2D</code> to the current line.
     * @see #ptSegDist(Point2D)
     * @since 1.2
     */
    public double ptLineDist(Point2D pt) {
        return ptLineDist(getX1(), getY1(), getX2(), getY2(), pt.getX(), pt.getY());
    }

    /**
     * Tests if a specified coordinate is inside the boundary of this
    Line2D.java : » App » android-app-examples » org » loon » framework » android » ga... Page 12 of 14
    http://www.java2s.com/Open-Source/Android/App/android-app-examples/org/loon/frame... 2/25/2013
     * <code>Line2D</code>. This method is required to implement the
     * {@link Shape} interface, but in the case of <code>Line2D</code> objects
     * it always returns <code>false</code> since a line contains no area.
     *
     * @param x
     * the X coordinate of the specified point to be tested
     * @param y
     * the Y coordinate of the specified point to be tested
     * @return <code>false</code> because a <code>Line2D</code> contains no
     * area.
     * @since 1.2
     */
    public boolean contains(double x, double y) {
        return false;
    }

    /**
     * Tests if a given <code>Point2D</code> is inside the boundary of this
     * <code>Line2D</code>. This method is required to implement the
     * {@link Shape} interface, but in the case of <code>Line2D</code> objects
     * it always returns <code>false</code> since a line contains no area.
     *
     * @param p
     * the specified <code>Point2D</code> to be tested
     * @return <code>false</code> because a <code>Line2D</code> contains no
     * area.
     * @since 1.2
     */
    public boolean contains(Point2D p) {
        return false;
    }

    /**
     * {@inheritDoc}
     *
     * @since 1.2
     */
//public boolean intersects(double x, double y, double w, double h) {
//return intersects(new RectF((float)x, (float)y, (float)w, (float)h));
//}
    /**
     * {@inheritDoc}
     *
     * @since 1.2
     */
//public boolean intersects(RectF r) {
//return r.intersectsLine(getX1(), getY1(), getX2(), getY2());
//}
    /**
     * Tests if the interior of this <code>Line2D</code> entirely contains the
     * specified set of rectangular coordinates. This method is required to
     * implement the <code>Shape</code> interface, but in the case of
     * <code>Line2D</code> objects it always returns false since a line contains
     * no area.
     *
     * @param x
     * the X coordinate of the upper-left corner of the specified
     * rectangular area
     * @param y
     * the Y coordinate of the upper-left corner of the specified
     * rectangular area
     * @param w
     * the width of the specified rectangular area
     * @param h
     * the height of the specified rectangular area
     * @return <code>false</code> because a <code>Line2D</code> contains no
     * area.
     * @since 1.2
     */
    public boolean contains(double x, double y, double w, double h) {
        return false;
    }

    /**
     * Tests if the interior of this <code>Line2D</code> entirely contains the
     * specified <code>Rectangle2D</code>. This method is required to implement
     * the <code>Shape</code> interface, but in the case of <code>Line2D</code>
     * objects it always returns <code>false</code> since a line contains no
     * area.
     *
     * @param r
     * the specified <code>Rectangle2D</code> to be tested
     * @return <code>false</code> because a <code>Line2D</code> contains no
     * area.
     * @since 1.2
     */
    public boolean contains(Rectangle2D r) {
        return false;
    }

    /**
     * {@inheritDoc}
     *
     * @since 1.2
     */
    //public RectF getBounds() {
    //    return getBounds2D().getBounds();
    //}

    /**
     * Returns an iteration object that defines the boundary of this
     * <code>Line2D</code>. The iterator for this class is not multi-threaded
     * safe, which means that this <code>Line2D</code> class does not guarantee
     * that modifications to the geometry of this <code>Line2D</code> object do
     * not affect any iterations of that geometry that are already in process.
     *
     * @param at
     * the specified {@link AffineTransform}
     * @return a {@link PathIterator} that defines the boundary of this
     * <code>Line2D</code>.
     * @since 1.2
     */
//public PathIterator getPathIterator(AffineTransform at) {
//return new LineIterator(this, at);
//}
    /**
     * Returns an iteration object that defines the boundary of this flattened
     * <code>Line2D</code>. The iterator for this class is not multi-threaded
     * safe, which means that this <code>Line2D</code> class does not guarantee
     * that modifications to the geometry of this <code>Line2D</code> object do
     * not affect any iterations of that geometry that are already in process.
     *
     * @param at
     * the specified <code>AffineTransform</code>
     * @param flatness
     * the maximum amount that the control points for a given curve
     * can vary from colinear before a subdivided curve is replaced
     * by a straight line connecting the end points. Since a
     * <code>Line2D</code> object is always flat, this parameter is
     * ignored.
     * @return a <code>PathIterator</code> that defines the boundary of the
     * flattened <code>Line2D</code>
     * @since 1.2
     */
//public PathIterator getPathIterator(AffineTransform at, double flatness) {
//return new LineIterator(this, at);
//}
    /**
     * Creates a new object of the same class as this object.
     *
     * @return a clone of this instance.
     * @exception OutOfMemoryError
     * if there is not enough memory.
     * @see java.lang.Cloneable
     * @since 1.2
     */
    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
// this shouldn't happen, since we are Cloneable
            throw new InternalError();
        }
    }
}
