/*
 * @(#)Arc2D.java	1.31 06/02/24
 *
 * Copyright 2006 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package armyc2.c2sd.graphics2d;

/**
 * <CODE>Arc2D</CODE> is the abstract superclass for all objects that store a 2D
 * arc defined by a framing rectangle, start angle, angular extent (length of
 * the arc), and a closure type (<CODE>OPEN</CODE>, <CODE>CHORD</CODE>, or
 * <CODE>PIE</CODE>).
 * <p>
 * <a name="inscribes">
 * The arc is a partial section of a full ellipse which inscribes the framing
 * rectangle of its parent {@link RectangularShape}.
 * </a>
 * <a name="angles">
 * The angles are specified relative to the non-square framing rectangle such
 * that 45 degrees always falls on the line from the center of the ellipse to
 * the upper right corner of the framing rectangle. As a result, if the framing
 * rectangle is noticeably longer along one axis than the other, the angles to
 * the start and end of the arc segment will be skewed farther along the longer
 * axis of the frame.
 * </a>
 * <p>
 * The actual storage representation of the coordinates is left to the subclass.
 *
 * @version 10 Feb 1997
 * @author	Jim Graham
 * @since 1.2
 */
public /* abstract */ class Arc2D /*extends RectangularShape*/ {

    /**
     * The closure type for an open arc with no path segments connecting the two
     * ends of the arc segment.
     *
     * @since 1.2
     */
    public final static int OPEN = 0;

    /**
     * The closure type for an arc closed by drawing a straight line segment
     * from the start of the arc segment to the end of the arc segment.
     *
     * @since 1.2
     */
    public final static int CHORD = 1;

    /**
     * The closure type for an arc closed by drawing straight line segments from
     * the start of the arc segment to the center of the full ellipse and from
     * that point to the end of the arc segment.
     *
     * @since 1.2
     */
    public final static int PIE = 2;

    /**
     * The X coordinate of the upper-left corner of the framing rectangle of the
     * arc.
     *
     * @since 1.2
     * @serial
     */
    public double x;

    /**
     * The Y coordinate of the upper-left corner of the framing rectangle of the
     * arc.
     *
     * @since 1.2
     * @serial
     */
    public double y;

    /**
     * The overall width of the full ellipse of which this arc is a partial
     * section (not considering the angular extents).
     *
     * @since 1.2
     * @serial
     */
    public double width;

    /**
     * The overall height of the full ellipse of which this arc is a partial
     * section (not considering the angular extents).
     *
     * @since 1.2
     * @serial
     */
    public double height;

    /**
     * The starting angle of the arc in degrees.
     *
     * @since 1.2
     * @serial
     */
    public double start;

    /**
     * The angular extent of the arc in degrees.
     *
     * @since 1.2
     * @serial
     */
    public double extent;

    /**
     * Constructs a new arc, initialized to the specified location, size,
     * angular extents, and closure type.
     *
     * @param x The X coordinate of the upper-left corner of the arc's framing
     * rectangle.
     * @param y The Y coordinate of the upper-left corner of the arc's framing
     * rectangle.
     * @param w The overall width of the full ellipse of which this arc is a
     * partial section.
     * @param h The overall height of the full ellipse of which this arc is a
     * partial section.
     * @param start The starting angle of the arc in degrees.
     * @param extent The angular extent of the arc in degrees.
     * @param type The closure type for the arc: {@link #OPEN}, {@link #CHORD},
     * or {@link #PIE}.
     * @since 1.2
     */
    public Arc2D(double x, double y, double w, double h,
            double start, double extent, int type) {
        //super(type);
        setArcType(type);
        this.x = x;
        this.y = y;
        this.width = w;
        this.height = h;
        this.start = start;
        this.extent = extent;
    }

    /**
     * Constructs a new arc, initialized to the specified location, size,
     * angular extents, and closure type.
     *
     * @param ellipseBounds The framing rectangle that defines the outer
     * boundary of the full ellipse of which this arc is a partial section.
     * @param start The starting angle of the arc in degrees.
     * @param extent The angular extent of the arc in degrees.
     * @param type The closure type for the arc: {@link #OPEN}, {@link #CHORD},
     * or {@link #PIE}.
     * @since 1.2
     */
    //public Double(Rectangle2D ellipseBounds,
    public Arc2D(Rectangle2D ellipseBounds,
            double start, double extent, int type) {
        //super(type);
        setArcType(type);
        this.x = ellipseBounds.getX();
        this.y = ellipseBounds.getY();
        this.width = ellipseBounds.getWidth();
        this.height = ellipseBounds.getHeight();
        this.start = start;
        this.extent = extent;
    }

    /**
     * {@inheritDoc} Note that the arc
     * <a href="Arc2D.html#inscribes">partially inscribes</a>
     * the framing rectangle of this {@code RectangularShape}.
     *
     * @since 1.2
     */
    public double getX() {
        return x;
    }

    /**
     * {@inheritDoc} Note that the arc
     * <a href="Arc2D.html#inscribes">partially inscribes</a>
     * the framing rectangle of this {@code RectangularShape}.
     *
     * @since 1.2
     */
    public double getY() {
        return y;
    }

    /**
     * {@inheritDoc} Note that the arc
     * <a href="Arc2D.html#inscribes">partially inscribes</a>
     * the framing rectangle of this {@code RectangularShape}.
     *
     * @since 1.2
     */
    public double getWidth() {
        return width;
    }

    /**
     * {@inheritDoc} Note that the arc
     * <a href="Arc2D.html#inscribes">partially inscribes</a>
     * the framing rectangle of this {@code RectangularShape}.
     *
     * @since 1.2
     */
    public double getHeight() {
        return height;
    }

    /**
     * {@inheritDoc}
     *
     * @since 1.2
     */
    public double getAngleStart() {
        return start;
    }

    /**
     * {@inheritDoc}
     *
     * @since 1.2
     */
    public double getAngleExtent() {
        return extent;
    }

    /**
     * {@inheritDoc}
     *
     * @since 1.2
     */
    public boolean isEmpty() {
        return (width <= 0.0 || height <= 0.0);
    }

    /**
     * {@inheritDoc}
     *
     * @since 1.2
     */
    public void setArc(double x, double y, double w, double h,
            double angSt, double angExt, int closure) {
        this.setArcType(closure);
        this.x = x;
        this.y = y;
        this.width = w;
        this.height = h;
        this.start = angSt;
        this.extent = angExt;
    }

    /**
     * {@inheritDoc}
     *
     * @since 1.2
     */
    public void setAngleStart(double angSt) {
        this.start = angSt;
    }

    /**
     * {@inheritDoc}
     *
     * @since 1.2
     */
    public void setAngleExtent(double angExt) {
        this.extent = angExt;
    }

    /*
     * JDK 1.6 serialVersionUID
     */
    private static final long serialVersionUID = 728264085846882001L;

    /**
     * Writes the default serializable fields to the
     * <code>ObjectOutputStream</code> followed by a byte indicating the arc
     * type of this <code>Arc2D</code> instance.
     *
     * @serialData
     * <ol>
     * <li>The default serializable fields.
     * <li>
     * followed by a <code>byte</code> indicating the arc type
     * {@link #OPEN}, {@link #CHORD}, or {@link #PIE}.
     * </ol>
     */
    private void writeObject(java.io.ObjectOutputStream s)
            throws java.io.IOException {
        s.defaultWriteObject();

        s.writeByte(getArcType());
    }

    /**
     * Reads the default serializable fields from the
     * <code>ObjectInputStream</code> followed by a byte indicating the arc type
     * of this <code>Arc2D</code> instance.
     *
     * @serialData
     * <ol>
     * <li>The default serializable fields.
     * <li>
     * followed by a <code>byte</code> indicating the arc type
     * {@link #OPEN}, {@link #CHORD}, or {@link #PIE}.
     * </ol>
     */
    private void readObject(java.io.ObjectInputStream s)
            throws java.lang.ClassNotFoundException, java.io.IOException {
        s.defaultReadObject();

        try {
            setArcType(s.readByte());
        } catch (IllegalArgumentException iae) {
            throw new java.io.InvalidObjectException(iae.getMessage());
        }
    }
//    }

    private int type;

    /**
     * This is an abstract class that cannot be instantiated directly.
     * Type-specific implementation subclasses are available for instantiation
     * and provide a number of formats for storing the information necessary to
     * satisfy the various accessor methods below.
     * <p>
     * This constructor creates an object with a default closure type of
     * {@link #OPEN}. It is provided only to enable serialization of subclasses.
     *
     * @see java.awt.geom.Arc2D.Float
     * @see java.awt.geom.Arc2D.Double
     */
    Arc2D() {
        this(OPEN);
    }

    /**
     * This is an abstract class that cannot be instantiated directly.
     * Type-specific implementation subclasses are available for instantiation
     * and provide a number of formats for storing the information necessary to
     * satisfy the various accessor methods below.
     *
     * @param type The closure type of this arc: {@link #OPEN}, {@link #CHORD},
     * or {@link #PIE}.
     * @see java.awt.geom.Arc2D.Float
     * @see java.awt.geom.Arc2D.Double
     * @since 1.2
     */
    protected Arc2D(int type) {
        setArcType(type);
    }

    /**
     * Returns the arc closure type of the arc: {@link #OPEN},
     * {@link #CHORD}, or {@link #PIE}.
     *
     * @return One of the integer constant closure types defined in this class.
     * @see #setArcType
     * @since 1.2
     */
    public int getArcType() {
        return type;
    }

    /**
     * Returns the starting point of the arc. This point is the intersection of
     * the ray from the center defined by the starting angle and the elliptical
     * boundary of the arc.
     *
     * @return A <CODE>Point2D</CODE> object representing the x,y coordinates of
     * the starting point of the arc.
     * @since 1.2
     */
    public Point2D getStartPoint() {
        double angle = Math.toRadians(-getAngleStart());
        double x = getX() + (Math.cos(angle) * 0.5 + 0.5) * getWidth();
        double y = getY() + (Math.sin(angle) * 0.5 + 0.5) * getHeight();
        //return new Point2D.Double(x, y);
        return new Point2D.Double(x, y);
    }

    /**
     * Returns the ending point of the arc. This point is the intersection of
     * the ray from the center defined by the starting angle plus the angular
     * extent of the arc and the elliptical boundary of the arc.
     *
     * @return A <CODE>Point2D</CODE> object representing the x,y coordinates of
     * the ending point of the arc.
     * @since 1.2
     */
    public Point2D getEndPoint() {
        double angle = Math.toRadians(-getAngleStart() - getAngleExtent());
        double x = getX() + (Math.cos(angle) * 0.5 + 0.5) * getWidth();
        double y = getY() + (Math.sin(angle) * 0.5 + 0.5) * getHeight();
        //return new Point2D.Double(x, y);
        return new Point2D.Double(x, y);
    }

    /**
     * Sets the location, size, angular extents, and closure type of this arc to
     * the specified values.
     *
     * @param rect The framing rectangle that defines the outer boundary of the
     * full ellipse of which this arc is a partial section.
     * @param angSt The starting angle of the arc in degrees.
     * @param angExt The angular extent of the arc in degrees.
     * @param closure The closure type for the arc:
     * {@link #OPEN}, {@link #CHORD}, or {@link #PIE}.
     * @since 1.2
     */
    public void setArc2(Rectangle2D rect, double angSt, double angExt,
            int closure) {
        setArc(rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight(),
                angSt, angExt, closure);
    }

    /**
     * Sets this arc to be the same as the specified arc.
     *
     * @param a The <CODE>Arc2D</CODE> to use to set the arc's values.
     * @since 1.2
     */
    public void setArc3(Arc2D a) {
        setArc(a.getX(), a.getY(), a.getWidth(), a.getHeight(),
                a.getAngleStart(), a.getAngleExtent(), a.type);
    }

    /**
     * Sets the position, bounds, angular extents, and closure type of this arc
     * to the specified values. The arc is defined by a center point and a
     * radius rather than a framing rectangle for the full ellipse.
     *
     * @param x The X coordinate of the center of the arc.
     * @param y The Y coordinate of the center of the arc.
     * @param radius The radius of the arc.
     * @param angSt The starting angle of the arc in degrees.
     * @param angExt The angular extent of the arc in degrees.
     * @param closure The closure type for the arc:
     * {@link #OPEN}, {@link #CHORD}, or {@link #PIE}.
     * @since 1.2
     */
    public void setArcByCenter(double x, double y, double radius,
            double angSt, double angExt, int closure) {
        setArc(x - radius, y - radius, radius * 2.0, radius * 2.0,
                angSt, angExt, closure);
    }

    /**
     * Sets the position, bounds, and angular extents of this arc to the
     * specified value. The starting angle of the arc is tangent to the line
     * specified by points (p1, p2), the ending angle is tangent to the line
     * specified by points (p2, p3), and the arc has the specified radius.
     *
     * @param p1 The first point that defines the arc. The starting angle of the
     * arc is tangent to the line specified by points (p1, p2).
     * @param p2 The second point that defines the arc. The starting angle of
     * the arc is tangent to the line specified by points (p1, p2). The ending
     * angle of the arc is tangent to the line specified by points (p2, p3).
     * @param p3 The third point that defines the arc. The ending angle of the
     * arc is tangent to the line specified by points (p2, p3).
     * @param radius The radius of the arc.
     * @since 1.2
     */
    public void setArcByTangent(Point2D p1, Point2D p2, Point2D p3,
            double radius) {
        double ang1 = Math.atan2(p1.getY() - p2.getY(),
                p1.getX() - p2.getX());
        double ang2 = Math.atan2(p3.getY() - p2.getY(),
                p3.getX() - p2.getX());
        double diff = ang2 - ang1;
        if (diff > Math.PI) {
            ang2 -= Math.PI * 2.0;
        } else if (diff < -Math.PI) {
            ang2 += Math.PI * 2.0;
        }
        double bisect = (ang1 + ang2) / 2.0;
        double theta = Math.abs(ang2 - bisect);
        double dist = radius / Math.sin(theta);
        double x = p2.getX() + dist * Math.cos(bisect);
        double y = p2.getY() + dist * Math.sin(bisect);
        // REMIND: This needs some work...
        if (ang1 < ang2) {
            ang1 -= Math.PI / 2.0;
            ang2 += Math.PI / 2.0;
        } else {
            ang1 += Math.PI / 2.0;
            ang2 -= Math.PI / 2.0;
        }
        ang1 = Math.toDegrees(-ang1);
        ang2 = Math.toDegrees(-ang2);
        diff = ang2 - ang1;
        if (diff < 0) {
            diff += 360;
        } else {
            diff -= 360;
        }
        setArcByCenter(x, y, radius, ang1, diff, type);
    }

    /**
     * Sets the closure type of this arc to the specified value:
     * <CODE>OPEN</CODE>, <CODE>CHORD</CODE>, or <CODE>PIE</CODE>.
     *
     * @param type The integer constant that represents the closure type of this
     * arc: {@link #OPEN}, {@link #CHORD}, or {@link #PIE}.
     *
     * @throws IllegalArgumentException if <code>type</code> is not 0, 1, or 2.+
     * @see #getArcType
     * @since 1.2
     */
    public void setArcType(int type) {
        if (type < OPEN || type > PIE) {
            throw new IllegalArgumentException("invalid type for Arc: " + type);
        }
        this.type = type;
    }

    /**
     * {@inheritDoc} Note that the arc
     * <a href="Arc2D.html#inscribes">partially inscribes</a>
     * the framing rectangle of this {@code RectangularShape}.
     *
     * @since 1.2
     */
    public void setFrame(double x, double y, double w, double h) {
        setArc(x, y, w, h, getAngleStart(), getAngleExtent(), type);
    }

    /**
     * Returns the high-precision framing rectangle of the arc. The framing
     * rectangle contains only the part of this <code>Arc2D</code> that is in
     * between the starting and ending angles and contains the pie wedge, if
     * this <code>Arc2D</code> has a <code>PIE</code> closure type.
     * <p>
     * This method differs from the
     * {@link RectangularShape#getBounds() getBounds} in that the
     * <code>getBounds</code> method only returns the bounds of the enclosing
     * ellipse of this <code>Arc2D</code> without considering the starting and
     * ending angles of this <code>Arc2D</code>.
     *
     * @return the <CODE>Rectangle2D</CODE> that represents the arc's framing
     * rectangle.
     * @since 1.2
     */
    public Rectangle2D getBounds2D() {
        if (isEmpty()) {
            return makeBounds(getX(), getY(), getWidth(), getHeight());
        }
        double x1, y1, x2, y2;
        if (getArcType() == PIE) {
            x1 = y1 = x2 = y2 = 0.0;
        } else {
            x1 = y1 = 1.0;
            x2 = y2 = -1.0;
        }
        double angle = 0.0;
        for (int i = 0; i < 6; i++) {
            if (i < 4) {
                // 0-3 are the four quadrants
                angle += 90.0;
                if (!containsAngle(angle)) {
                    continue;
                }
            } else if (i == 4) {
                // 4 is start angle
                angle = getAngleStart();
            } else {
                // 5 is end angle
                angle += getAngleExtent();
            }
            double rads = Math.toRadians(-angle);
            double xe = Math.cos(rads);
            double ye = Math.sin(rads);
            x1 = Math.min(x1, xe);
            y1 = Math.min(y1, ye);
            x2 = Math.max(x2, xe);
            y2 = Math.max(y2, ye);
        }
        double w = getWidth();
        double h = getHeight();
        x2 = (x2 - x1) * 0.5 * w;
        y2 = (y2 - y1) * 0.5 * h;
        x1 = getX() + (x1 * 0.5 + 0.5) * w;
        y1 = getY() + (y1 * 0.5 + 0.5) * h;
        return makeBounds(x1, y1, x2, y2);
    }

    protected Rectangle2D makeBounds(double x, double y,
            double w, double h) {
        return null;
    }
    /*
     * Normalizes the specified angle into the range -180 to 180.
     */
    static double normalizeDegrees(double angle) {
        if (angle > 180.0) {
            if (angle <= (180.0 + 360.0)) {
                angle = angle - 360.0;
            } else {
                angle = Math.IEEEremainder(angle, 360.0);
                // IEEEremainder can return -180 here for some input values...
                if (angle == -180.0) {
                    angle = 180.0;
                }
            }
        } else if (angle <= -180.0) {
            if (angle > (-180.0 - 360.0)) {
                angle = angle + 360.0;
            } else {
                angle = Math.IEEEremainder(angle, 360.0);
                // IEEEremainder can return -180 here for some input values...
                if (angle == -180.0) {
                    angle = 180.0;
                }
            }
        }
        return angle;
    }

    /**
     * Determines whether or not the specified angle is within the angular
     * extents of the arc.
     *
     * @param angle The angle to test.
     *
     * @return <CODE>true</CODE> if the arc contains the angle,
     * <CODE>false</CODE> if the arc doesn't contain the angle.
     * @since 1.2
     */
    public boolean containsAngle(double angle) {
        double angExt = getAngleExtent();
        boolean backwards = (angExt < 0.0);
        if (backwards) {
            angExt = -angExt;
        }
        if (angExt >= 360.0) {
            return true;
        }
        angle = normalizeDegrees(angle) - normalizeDegrees(getAngleStart());
        if (backwards) {
            angle = -angle;
        }
        if (angle < 0.0) {
            angle += 360.0;
        }

        return (angle >= 0.0) && (angle < angExt);
    }

    /**
     * Determines whether or not the specified point is inside the boundary of
     * the arc.
     *
     * @param x The X coordinate of the point to test.
     * @param y The Y coordinate of the point to test.
     *
     * @return <CODE>true</CODE> if the point lies within the bound of the arc,
     * <CODE>false</CODE> if the point lies outside of the arc's bounds.
     * @since 1.2
     */
    public boolean contains(double x, double y) {
	// Normalize the coordinates compared to the ellipse
        // having a center at 0,0 and a radius of 0.5.
        double ellw = getWidth();
        if (ellw <= 0.0) {
            return false;
        }
        double normx = (x - getX()) / ellw - 0.5;
        double ellh = getHeight();
        if (ellh <= 0.0) {
            return false;
        }
        double normy = (y - getY()) / ellh - 0.5;
        double distSq = (normx * normx + normy * normy);
        if (distSq >= 0.25) {
            return false;
        }
        double angExt = Math.abs(getAngleExtent());
        if (angExt >= 360.0) {
            return true;
        }
        boolean inarc = containsAngle(-Math.toDegrees(Math.atan2(normy,
                normx)));
        if (type == PIE) {
            return inarc;
        }
        // CHORD and OPEN behave the same way
        if (inarc) {
            if (angExt >= 180.0) {
                return true;
            }
            // point must be outside the "pie triangle"
        } else {
            if (angExt <= 180.0) {
                return false;
            }
            // point must be inside the "pie triangle"
        }
	// The point is inside the pie triangle iff it is on the same
        // side of the line connecting the ends of the arc as the center.
        double angle = Math.toRadians(-getAngleStart());
        double x1 = Math.cos(angle);
        double y1 = Math.sin(angle);
        angle += Math.toRadians(-getAngleExtent());
        double x2 = Math.cos(angle);
        double y2 = Math.sin(angle);
        boolean inside = (Line2D.relativeCCW(x1, y1, x2, y2, 2 * normx, 2 * normy)
                * Line2D.relativeCCW(x1, y1, x2, y2, 0, 0) >= 0);
        return inarc ? !inside : inside;
    }

    /**
     * Returns an iteration object that defines the boundary of the arc. This
     * iterator is multithread safe. <code>Arc2D</code> guarantees that
     * modifications to the geometry of the arc do not affect any iterations of
     * that geometry that are already in process.
     *
     * @param at an optional <CODE>AffineTransform</CODE> to be applied to the
     * coordinates as they are returned in the iteration, or null if the
     * untransformed coordinates are desired.
     *
     * @return A <CODE>PathIterator</CODE> that defines the arc's boundary.
     * @since 1.2
     */
    public ArcIterator getPathIterator(AffineTransform at) {
        return new ArcIterator(this, at);
    }

    /**
     * Returns the hashcode for this <code>Arc2D</code>.
     *
     * @return the hashcode for this <code>Arc2D</code>.
     * @since 1.6
     */
    public int hashCode() {
        long bits = java.lang.Double.doubleToLongBits(getX());
        bits += java.lang.Double.doubleToLongBits(getY()) * 37;
        bits += java.lang.Double.doubleToLongBits(getWidth()) * 43;
        bits += java.lang.Double.doubleToLongBits(getHeight()) * 47;
        bits += java.lang.Double.doubleToLongBits(getAngleStart()) * 53;
        bits += java.lang.Double.doubleToLongBits(getAngleExtent()) * 59;
        bits += getArcType() * 61;
        return (((int) bits) ^ ((int) (bits >> 32)));
    }

    /**
     * Determines whether or not the specified <code>Object</code> is equal to
     * this <code>Arc2D</code>. The specified <code>Object</code> is equal to
     * this <code>Arc2D</code> if it is an instance of <code>Arc2D</code> and if
     * its location, size, arc extents and type are the same as this
     * <code>Arc2D</code>.
     *
     * @param obj an <code>Object</code> to be compared with this
     * <code>Arc2D</code>.
     * @return  <code>true</code> if <code>obj</code> is an instance of
     * <code>Arc2D</code> and has the same values; <code>false</code> otherwise.
     * @since 1.6
     */
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof Arc2D) {
            Arc2D a2d = (Arc2D) obj;
            return ((getX() == a2d.getX())
                    && (getY() == a2d.getY())
                    && (getWidth() == a2d.getWidth())
                    && (getHeight() == a2d.getHeight())
                    && (getAngleStart() == a2d.getAngleStart())
                    && (getAngleExtent() == a2d.getAngleExtent())
                    && (getArcType() == a2d.getArcType()));
        }
        return false;
    }
}
