/*
 * @(#)Area.java	1.21 06/02/24
 *
 * Copyright 2006 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package sec.sun.awt.geom;

import armyc2.c2sd.graphics2d.*;
import sec.geo.ShapeObject;
/**
 * An <code>Area</code> object stores and manipulates a
 * resolution-independent description of an enclosed area of
 * 2-dimensional space.
 * <code>Area</code> objects can be transformed and can perform
 * various Constructive Area Geometry (CAG) operations when combined
 * with other <code>Area</code> objects.
 * The CAG operations include area
 * {@link #add addition}, {@link #subtract subtraction},
 * {@link #intersect intersection}, and {@link #exclusiveOr exclusive or}.
 * See the linked method documentation for examples of the various
 * operations.
 * <p>
 * The <code>Area</code> class implements the <code>Shape</code>
 * interface and provides full support for all of its hit-testing
 * and path iteration facilities, but an <code>Area</code> is more
 * specific than a generalized path in a number of ways:
 * <ul>
 * <li>Only closed paths and sub-paths are stored.
 *     <code>Area</code> objects constructed from unclosed paths
 *     are implicitly closed during construction as if those paths
 *     had been filled by the <code>Graphics2D.fill</code> method.
 * <li>The interiors of the individual stored sub-paths are all
 *     non-empty and non-overlapping.  Paths are decomposed during
 *     construction into separate component non-overlapping parts,
 *     empty pieces of the path are discarded, and then these
 *     non-empty and non-overlapping properties are maintained
 *     through all subsequent CAG operations.  Outlines of different
 *     component sub-paths may touch each other, as long as they
 *     do not cross so that their enclosed areas overlap.
 * <li>The geometry of the path describing the outline of the
 *     <code>Area</code> resembles the path from which it was
 *     constructed only in that it describes the same enclosed
 *     2-dimensional area, but may use entirely different types
 *     and ordering of the path segments to do so.
 * </ul>
 * Interesting issues which are not always obvious when using
 * the <code>Area</code> include:
 * <ul>
 * <li>Creating an <code>Area</code> from an unclosed (open)
 *     <code>Shape</code> results in a closed outline in the
 *     <code>Area</code> object.
 * <li>Creating an <code>Area</code> from a <code>Shape</code>
 *     which encloses no area (even when "closed") produces an
 *     empty <code>Area</code>.  A common example of this issue
 *     is that producing an <code>Area</code> from a line will
 *     be empty since the line encloses no area.  An empty
 *     <code>Area</code> will iterate no geometry in its
 *     <code>PathIterator</code> objects.
 * <li>A self-intersecting <code>Shape</code> may be split into
 *     two (or more) sub-paths each enclosing one of the
 *     non-intersecting portions of the original path.
 * <li>An <code>Area</code> may take more path segments to
 *     describe the same geometry even when the original
 *     outline is simple and obvious.  The analysis that the
 *     <code>Area</code> class must perform on the path may
 *     not reflect the same concepts of "simple and obvious"
 *     as a human being perceives.
 * </ul>
 *
 * @since 1.2
 */
public class Area /* implements Shape, Cloneable */{
    private static Vector EmptyCurves = new Vector();
    private static final boolean normalizeGeoPoints = true;
    private Vector curves;

    /**
     * Default constructor which creates an empty area.
     * @since 1.2
     */
    public Area() {
	curves = EmptyCurves;
    }

    /**
     * The <code>Area</code> class creates an area geometry from the
     * specified {@link Shape} object.  The geometry is explicitly
     * closed, if the <code>Shape</code> is not already closed.  The
     * fill rule (even-odd or winding) specified by the geometry of the
     * <code>Shape</code> is used to determine the resulting enclosed area.
     * @param s  the <code>Shape</code> from which the area is constructed
     * @throws NullPointerException if <code>s</code> is null
     * @since 1.2
     */
    public Area(ShapeObject s) {
//	if (s instanceof Area) {
//	    curves = ((Area) s).curves;
//	} 
//        else 
//        {
            curves = pathToCurves(s.getPathIterator(null));
//        }
    }
//    public Area(Shape s)
//    {
//        curves=pathToCurves(s.getPathIterator(null));
//    }

    private static Vector pathToCurves(PathIterator pi) {
	Vector curves = new Vector();
	int windingRule = pi.getWindingRule();
	// coords array is big enough for holding:
	//     coordinates returned from currentSegment (6)
	//     OR
	//         two subdivided quadratic curves (2+4+4=10)
	//         AND
	//             0-1 horizontal splitting parameters
	//             OR
	//             2 parametric equation derivative coefficients
	//     OR
	//         three subdivided cubic curves (2+6+6+6=20)
	//         AND
	//             0-2 horizontal splitting parameters
	//             OR
	//             3 parametric equation derivative coefficients
	double coords[] = new double[23];
	double movx = 0, movy = 0;
	double curx = 0, cury = 0;
	double newx, newy;
	while (!pi.isDone()) {
	    switch (pi.currentSegment(coords)) {
	    case PathIterator.SEG_MOVETO:
                if(normalizeGeoPoints==true)
                {
                    if(movx>0)
                        movx-=360;
                    if(curx>0)
                        curx-=360;
                }
		Curve.insertLine(curves, curx, cury, movx, movy);
		curx = movx = coords[0];
		cury = movy = coords[1];
                if(normalizeGeoPoints==true)
                {
                    if(movx>0)
                        movx-=360;
                }
		Curve.insertMove(curves, movx, movy);
		break;
	    case PathIterator.SEG_LINETO:
		newx = coords[0];
		newy = coords[1];
                if(normalizeGeoPoints==true)
                {
                    if(newx>0)
                        newx-=360;
                    if(curx>0)
                        curx-=360;
                }
		Curve.insertLine(curves, curx, cury, newx, newy);
		curx = newx;
		cury = newy;
		break;
	    case PathIterator.SEG_QUADTO:
		newx = coords[2];
		newy = coords[3];
                if(normalizeGeoPoints==true)
                {
                    if(curx>0)
                        curx-=360;
                }
		Curve.insertQuad(curves, curx, cury, coords);
		curx = newx;
		cury = newy;
		break;
	    case PathIterator.SEG_CUBICTO:
		newx = coords[4];
		newy = coords[5];
                if(normalizeGeoPoints==true)
                {
                    if(curx>0)
                        curx-=360;
                }
		Curve.insertCubic(curves, curx, cury, coords);
		curx = newx;
		cury = newy;
		break;
	    case PathIterator.SEG_CLOSE:
                if(normalizeGeoPoints==true)
                {
                    if(movx>0)
                        movx-=360;
                    if(curx>0)
                        curx-=360;
                }
		Curve.insertLine(curves, curx, cury, movx, movy);
		curx = movx;
		cury = movy;
		break;
	    }
	    pi.next();
	}
        if(normalizeGeoPoints==true)
        {
            if(movx>0)
                movx-=360;
            if(curx>0)
                curx-=360;
        }
	Curve.insertLine(curves, curx, cury, movx, movy);	
        AreaOp2 operator2=null;
	if (windingRule == PathIterator.WIND_EVEN_ODD) {
            operator2=new AreaOp2(AreaOp2.EOWINDOP);
	} else {
            operator2=new AreaOp2(AreaOp2.NZWINDOP);
	}
        return operator2.calculate(curves, EmptyCurves);
    }

    /**
     * Adds the shape of the specified <code>Area</code> to the
     * shape of this <code>Area</code>.
     * The resulting shape of this <code>Area</code> will include
     * the union of both shapes, or all areas that were contained
     * in either this or the specified <code>Area</code>.
     * <pre>
     *     // Example:
     *     Area a1 = new Area([triangle 0,0 =&gt; 8,0 =&gt; 0,8]);
     *     Area a2 = new Area([triangle 0,0 =&gt; 8,0 =&gt; 8,8]);
     *     a1.add(a2);
     *
     *        a1(before)     +         a2         =     a1(after)
     *
     *     ################     ################     ################
     *     ##############         ##############     ################
     *     ############             ############     ################
     *     ##########                 ##########     ################
     *     ########                     ########     ################
     *     ######                         ######     ######    ######
     *     ####                             ####     ####        ####
     *     ##                                 ##     ##            ##
     * </pre>
     * @param   rhs  the <code>Area</code> to be added to the
     *          current shape
     * @throws NullPointerException if <code>rhs</code> is null
     * @since 1.2
     */
    public void add(Area rhs) {
	//curves = new AreaOp.AddOp().calculate(this.curves, rhs.curves);
	//curves = new AddOp().calculate(this.curves, rhs.curves);
	curves = new SomeOp(SomeOp.ADDOP).calculate(this.curves, rhs.curves);
	invalidateBounds();
    }

    /**
     * Subtracts the shape of the specified <code>Area</code> from the 
     * shape of this <code>Area</code>.
     * The resulting shape of this <code>Area</code> will include
     * areas that were contained only in this <code>Area</code>
     * and not in the specified <code>Area</code>.
     * <pre>
     *     // Example:
     *     Area a1 = new Area([triangle 0,0 =&gt; 8,0 =&gt; 0,8]);
     *     Area a2 = new Area([triangle 0,0 =&gt; 8,0 =&gt; 8,8]);
     *     a1.subtract(a2);
     *
     *        a1(before)     -         a2         =     a1(after)
     *
     *     ################     ################
     *     ##############         ##############     ##
     *     ############             ############     ####
     *     ##########                 ##########     ######
     *     ########                     ########     ########
     *     ######                         ######     ######
     *     ####                             ####     ####
     *     ##                                 ##     ##
     * </pre>
     * @param   rhs  the <code>Area</code> to be subtracted from the 
     *		current shape
     * @throws NullPointerException if <code>rhs</code> is null
     * @since 1.2
     */
    public void subtract(Area rhs) {
	//curves = new AreaOp.SubOp().calculate(this.curves, rhs.curves);
	curves = new SomeOp(SomeOp.SUBOP).calculate(this.curves, rhs.curves);
	invalidateBounds();
    }

    /**
     * Sets the shape of this <code>Area</code> to the intersection of 
     * its current shape and the shape of the specified <code>Area</code>.
     * The resulting shape of this <code>Area</code> will include
     * only areas that were contained in both this <code>Area</code>
     * and also in the specified <code>Area</code>.
     * <pre>
     *     // Example:
     *     Area a1 = new Area([triangle 0,0 =&gt; 8,0 =&gt; 0,8]);
     *     Area a2 = new Area([triangle 0,0 =&gt; 8,0 =&gt; 8,8]);
     *     a1.intersect(a2);
     *
     *      a1(before)   intersect     a2         =     a1(after)
     *
     *     ################     ################     ################
     *     ##############         ##############       ############
     *     ############             ############         ########
     *     ##########                 ##########           ####
     *     ########                     ########
     *     ######                         ######
     *     ####                             ####
     *     ##                                 ##
     * </pre>
     * @param   rhs  the <code>Area</code> to be intersected with this
     *		<code>Area</code>
     * @throws NullPointerException if <code>rhs</code> is null
     * @since 1.2
     */
    public void intersect(Area rhs) {
	//curves = new AreaOp.IntOp().calculate(this.curves, rhs.curves);
	curves = new SomeOp(SomeOp.INTOP).calculate(this.curves, rhs.curves);
	invalidateBounds();
    }

    /**
     * Sets the shape of this <code>Area</code> to be the combined area
     * of its current shape and the shape of the specified <code>Area</code>, 
     * minus their intersection.
     * The resulting shape of this <code>Area</code> will include
     * only areas that were contained in either this <code>Area</code>
     * or in the specified <code>Area</code>, but not in both.
     * <pre>
     *     // Example:
     *     Area a1 = new Area([triangle 0,0 =&gt; 8,0 =&gt; 0,8]);
     *     Area a2 = new Area([triangle 0,0 =&gt; 8,0 =&gt; 8,8]);
     *     a1.exclusiveOr(a2);
     *
     *        a1(before)    xor        a2         =     a1(after)
     *
     *     ################     ################
     *     ##############         ##############     ##            ##
     *     ############             ############     ####        ####
     *     ##########                 ##########     ######    ######
     *     ########                     ########     ################
     *     ######                         ######     ######    ######
     *     ####                             ####     ####        ####
     *     ##                                 ##     ##            ##
     * </pre>
     * @param   rhs  the <code>Area</code> to be exclusive ORed with this 
     *		<code>Area</code>.
     * @throws NullPointerException if <code>rhs</code> is null
     * @since 1.2
     */
    public void exclusiveOr(Area rhs) {
	//curves = new AreaOp.XorOp().calculate(this.curves, rhs.curves);
	curves = new SomeOp(SomeOp.XOROP).calculate(this.curves, rhs.curves);
	invalidateBounds();
    }

    /**
     * Removes all of the geometry from this <code>Area</code> and
     * restores it to an empty area.
     * @since 1.2
     */
    public void reset() {
	curves = new Vector();
	invalidateBounds();
    }

    /**
     * Tests whether this <code>Area</code> object encloses any area.
     * @return    <code>true</code> if this <code>Area</code> object
     * represents an empty area; <code>false</code> otherwise.
     * @since 1.2
     */
    public boolean isEmpty() {
	return (curves.size() == 0);
    }

    /**
     * Tests whether this <code>Area</code> consists entirely of
     * straight edged polygonal geometry.
     * @return    <code>true</code> if the geometry of this
     * <code>Area</code> consists entirely of line segments;
     * <code>false</code> otherwise.
     * @since 1.2
     */
    public boolean isPolygonal() {
	Enumeration enum_ = curves.elements();
	while (enum_.hasMoreElements()) {
	    if (((CurveObject) enum_.nextElement()).getOrder() > 1) {
		return false;
	    }
	}
	return true;
    }

    /**
     * Tests whether this <code>Area</code> is rectangular in shape.
     * @return    <code>true</code> if the geometry of this
     * <code>Area</code> is rectangular in shape; <code>false</code>
     * otherwise.
     * @since 1.2
     */
    public boolean isRectangular() {
	int size = curves.size();
	if (size == 0) {
	    return true;
	}
	if (size > 3) {
	    return false;
	}
	CurveObject c1 = (CurveObject) curves.get(1);
	CurveObject c2 = (CurveObject) curves.get(2);
	if (c1.getOrder() != 1 || c2.getOrder() != 1) {
	    return false;
	}
	if (c1.getXTop() != c1.getXBot() || c2.getXTop() != c2.getXBot()) {
	    return false;
	}
	if (c1.getYTop() != c2.getYTop() || c1.getYBot() != c2.getYBot()) {
	    // One might be able to prove that this is impossible...
	    return false;
	}
	return true;
    }

    /**
     * Tests whether this <code>Area</code> is comprised of a single
     * closed subpath.  This method returns <code>true</code> if the 
     * path contains 0 or 1 subpaths, or <code>false</code> if the path
     * contains more than 1 subpath.  The subpaths are counted by the 
     * number of {@link PathIterator#SEG_MOVETO SEG_MOVETO}  segments 
     * that appear in the path.
     * @return    <code>true</code> if the <code>Area</code> is comprised
     * of a single basic geometry; <code>false</code> otherwise.
     * @since 1.2
     */
    public boolean isSingular() {
	if (curves.size() < 3) {
	    return true;
	}
	Enumeration enum_ = curves.elements();
	enum_.nextElement(); // First Order0 "moveto"
	while (enum_.hasMoreElements()) {
	    if (((CurveObject) enum_.nextElement()).getOrder() == 0) {
		return false;
	    }
	}
	return true;
    }

    private Rectangle2D cachedBounds;
    private void invalidateBounds() {
	cachedBounds = null;
    }

    /**
     * Tests whether the geometries of the two <code>Area</code> objects
     * are equal.
     * This method will return false if the argument is null.
     * @param   other  the <code>Area</code> to be compared to this
     *		<code>Area</code>
     * @return  <code>true</code> if the two geometries are equal;
     *		<code>false</code> otherwise.
     * @since 1.2
     */
    public boolean equals(Area other) {
	// REMIND: A *much* simpler operation should be possible...
	// Should be able to do a curve-wise comparison since all Areas
	// should evaluate their curves in the same top-down order.
	if (other == this) {
	    return true;
	}
	if (other == null) {
	    return false;
	}
	//Vector c = new AreaOp.XorOp().calculate(this.curves, other.curves);
	Vector c = new SomeOp(SomeOp.XOROP).calculate(this.curves, other.curves);
	return c.isEmpty();
    }

    /**
     * Creates a {@link PathIterator} for the outline of this 
     * <code>Area</code> object.  This <code>Area</code> object is unchanged.
     * @param at an optional <code>AffineTransform</code> to be applied to
     * the coordinates as they are returned in the iteration, or
     * <code>null</code> if untransformed coordinates are desired
     * @return    the <code>PathIterator</code> object that returns the 
     *		geometry of the outline of this <code>Area</code>, one 
     *		segment at a time.
     * @since 1.2
     */
    public AreaIterator getPathIterator(AffineTransform at) {   //did return PathIterator
	return new AreaIterator(curves, at);
    }

}

