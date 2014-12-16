/*
 * @(#)CubicCurve2D.java	1.35 06/04/17
 *
 * Copyright 2006 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package armyc2.c2sd.graphics2d;

/**
 * The <code>CubicCurve2D</code> class defines a cubic parametric curve 
 * segment in {@code (x,y)} coordinate space.
 * <p>
 * This class is only the abstract superclass for all objects which
 * store a 2D cubic curve segment.
 * The actual storage representation of the coordinates is left to
 * the subclass.
 *
 * @version 	1.35, 04/17/06
 * @author	Jim Graham
 * @since 1.2
 */
public /* abstract */ final class CubicCurve2D /*implements Shape, Cloneable*/ {

    /**
     * This is an abstract class that cannot be instantiated directly.
     * Type-specific implementation subclasses are available for
     * instantiation and provide a number of formats for storing
     * the information necessary to satisfy the various accessor
     * methods below.
     *
     * @see java.awt.geom.CubicCurve2D.Float
     * @see java.awt.geom.CubicCurve2D.Double
     * @since 1.2
     */
//    protected CubicCurve2D() {
//    }

    /**
     * Returns the square of the flatness of the cubic curve specified
     * by the indicated control points. The flatness is the maximum distance 
     * of a control point from the line connecting the end points.
     *
     * @param x1 the X coordinate that specifies the start point
     *           of a {@code CubicCurve2D}
     * @param y1 the Y coordinate that specifies the start point
     *           of a {@code CubicCurve2D}
     * @param ctrlx1 the X coordinate that specifies the first control point
     *               of a {@code CubicCurve2D}
     * @param ctrly1 the Y coordinate that specifies the first control point
     *               of a {@code CubicCurve2D}
     * @param ctrlx2 the X coordinate that specifies the second control point
     *               of a {@code CubicCurve2D}
     * @param ctrly2 the Y coordinate that specifies the second control point
     *               of a {@code CubicCurve2D}
     * @param x2 the X coordinate that specifies the end point
     *           of a {@code CubicCurve2D}
     * @param y2 the Y coordinate that specifies the end point
     *           of a {@code CubicCurve2D}
     * @return the square of the flatness of the {@code CubicCurve2D}
     *		represented by the specified coordinates.
     * @since 1.2
     */
    public static double getFlatnessSq2(double x1, double y1,
				       double ctrlx1, double ctrly1,
				       double ctrlx2, double ctrly2,
				       double x2, double y2) {
	//return Math.max(Line2D.ptSegDistSq(x1, y1, x2, y2, ctrlx1, ctrly1),
	//		Line2D.ptSegDistSq(x1, y1, x2, y2, ctrlx2, ctrly2));
			
	return Math.max(Line2D.ptLineDistSq(x1, y1, x2, y2, ctrlx1, ctrly1),
			Line2D.ptLineDistSq(x1, y1, x2, y2, ctrlx2, ctrly2));
    }

    /**
     * Returns the flatness of the cubic curve specified
     * by the indicated control points. The flatness is the maximum distance 
     * of a control point from the line connecting the end points.
     *
     * @param x1 the X coordinate that specifies the start point
     *           of a {@code CubicCurve2D}
     * @param y1 the Y coordinate that specifies the start point
     *           of a {@code CubicCurve2D}
     * @param ctrlx1 the X coordinate that specifies the first control point
     *               of a {@code CubicCurve2D}
     * @param ctrly1 the Y coordinate that specifies the first control point
     *               of a {@code CubicCurve2D}
     * @param ctrlx2 the X coordinate that specifies the second control point
     *               of a {@code CubicCurve2D}
     * @param ctrly2 the Y coordinate that specifies the second control point
     *               of a {@code CubicCurve2D}
     * @param x2 the X coordinate that specifies the end point
     *           of a {@code CubicCurve2D}
     * @param y2 the Y coordinate that specifies the end point
     *           of a {@code CubicCurve2D}
     * @return the flatness of the {@code CubicCurve2D}
     *		represented by the specified coordinates.
     * @since 1.2
     */
    public static double getFlatness(double x1, double y1,
				     double ctrlx1, double ctrly1,
				     double ctrlx2, double ctrly2,
				     double x2, double y2) {
	return Math.sqrt(getFlatnessSq2(x1, y1, ctrlx1, ctrly1,
				       ctrlx2, ctrly2, x2, y2));
    }

    /**
     * Returns the square of the flatness of the cubic curve specified
     * by the control points stored in the indicated array at the 
     * indicated index. The flatness is the maximum distance 
     * of a control point from the line connecting the end points.
     * @param coords an array containing coordinates
     * @param offset the index of <code>coords</code> from which to begin 
     *          getting the end points and control points of the curve
     * @return the square of the flatness of the <code>CubicCurve2D</code>
     *		specified by the coordinates in <code>coords</code> at
     *		the specified offset.
     * @since 1.2
     */
    public static double getFlatnessSq(double coords[], int offset) {
	return getFlatnessSq2(coords[offset + 0], coords[offset + 1],
			     coords[offset + 2], coords[offset + 3],
			     coords[offset + 4], coords[offset + 5],
			     coords[offset + 6], coords[offset + 7]);
    }

    /**
     * Returns the flatness of the cubic curve specified
     * by the control points stored in the indicated array at the 
     * indicated index.  The flatness is the maximum distance 
     * of a control point from the line connecting the end points.
     * @param coords an array containing coordinates
     * @param offset the index of <code>coords</code> from which to begin 
     *          getting the end points and control points of the curve
     * @return the flatness of the <code>CubicCurve2D</code>
     *		specified by the coordinates in <code>coords</code> at
     *		the specified offset.
     * @since 1.2
     */
    public static double getFlatness2(double coords[], int offset) {
	return getFlatness(coords[offset + 0], coords[offset + 1],
			   coords[offset + 2], coords[offset + 3],
			   coords[offset + 4], coords[offset + 5],
			   coords[offset + 6], coords[offset + 7]);
    }

    /**
     * Subdivides the cubic curve specified by the coordinates
     * stored in the <code>src</code> array at indices <code>srcoff</code> 
     * through (<code>srcoff</code>&nbsp;+&nbsp;7) and stores the
     * resulting two subdivided curves into the two result arrays at the
     * corresponding indices.
     * Either or both of the <code>left</code> and <code>right</code>
     * arrays may be <code>null</code> or a reference to the same array 
     * as the <code>src</code> array.
     * Note that the last point in the first subdivided curve is the
     * same as the first point in the second subdivided curve. Thus,
     * it is possible to pass the same array for <code>left</code>
     * and <code>right</code> and to use offsets, such as <code>rightoff</code>
     * equals (<code>leftoff</code> + 6), in order
     * to avoid allocating extra storage for this common point.
     * @param src the array holding the coordinates for the source curve
     * @param srcoff the offset into the array of the beginning of the
     * the 6 source coordinates
     * @param left the array for storing the coordinates for the first
     * half of the subdivided curve
     * @param leftoff the offset into the array of the beginning of the
     * the 6 left coordinates
     * @param right the array for storing the coordinates for the second
     * half of the subdivided curve
     * @param rightoff the offset into the array of the beginning of the
     * the 6 right coordinates
     * @since 1.2
     */
    public static void subdivide(double src[], int srcoff,
				 double left[], int leftoff,
				 double right[], int rightoff) {
	double x1 = src[srcoff + 0];
	double y1 = src[srcoff + 1];
	double ctrlx1 = src[srcoff + 2];
	double ctrly1 = src[srcoff + 3];
	double ctrlx2 = src[srcoff + 4];
	double ctrly2 = src[srcoff + 5];
	double x2 = src[srcoff + 6];
	double y2 = src[srcoff + 7];
	if (left != null) {
	    left[leftoff + 0] = x1;
	    left[leftoff + 1] = y1;
	}
	if (right != null) {
	    right[rightoff + 6] = x2;
	    right[rightoff + 7] = y2;
	}
	x1 = (x1 + ctrlx1) / 2.0;
	y1 = (y1 + ctrly1) / 2.0;
	x2 = (x2 + ctrlx2) / 2.0;
	y2 = (y2 + ctrly2) / 2.0;
	double centerx = (ctrlx1 + ctrlx2) / 2.0;
	double centery = (ctrly1 + ctrly2) / 2.0;
	ctrlx1 = (x1 + centerx) / 2.0;
	ctrly1 = (y1 + centery) / 2.0;
	ctrlx2 = (x2 + centerx) / 2.0;
	ctrly2 = (y2 + centery) / 2.0;
	centerx = (ctrlx1 + ctrlx2) / 2.0;
	centery = (ctrly1 + ctrly2) / 2.0;
	if (left != null) {
	    left[leftoff + 2] = x1;
	    left[leftoff + 3] = y1;
	    left[leftoff + 4] = ctrlx1;
	    left[leftoff + 5] = ctrly1;
	    left[leftoff + 6] = centerx;
	    left[leftoff + 7] = centery;
	}
	if (right != null) {
	    right[rightoff + 0] = centerx;
	    right[rightoff + 1] = centery;
	    right[rightoff + 2] = ctrlx2;
	    right[rightoff + 3] = ctrly2;
	    right[rightoff + 4] = x2;
	    right[rightoff + 5] = y2;
	}
    }

    /**
     * Solves the cubic whose coefficients are in the <code>eqn</code> 
     * array and places the non-complex roots back into the same array, 
     * returning the number of roots.  The solved cubic is represented 
     * by the equation:
     * <pre>
     *     eqn = {c, b, a, d}
     *     dx^3 + ax^2 + bx + c = 0
     * </pre>
     * A return value of -1 is used to distinguish a constant equation
     * that might be always 0 or never 0 from an equation that has no
     * zeroes.
     * @param eqn an array containing coefficients for a cubic
     * @return the number of roots, or -1 if the equation is a constant.
     * @since 1.2
     */
    public static int solveCubic(double eqn[]) {
	return solveCubic2(eqn, eqn);
    }

    /**
     * Solve the cubic whose coefficients are in the <code>eqn</code>
     * array and place the non-complex roots into the <code>res</code>
     * array, returning the number of roots.
     * The cubic solved is represented by the equation:
     *     eqn = {c, b, a, d}
     *     dx^3 + ax^2 + bx + c = 0
     * A return value of -1 is used to distinguish a constant equation,
     * which may be always 0 or never 0, from an equation which has no
     * zeroes.
     * @param eqn the specified array of coefficients to use to solve
     *        the cubic equation
     * @param res the array that contains the non-complex roots 
     *        resulting from the solution of the cubic equation
     * @return the number of roots, or -1 if the equation is a constant
     * @since 1.3
     */
    public static int solveCubic2(double eqn[], double res[]) {
	// From Numerical Recipes, 5.6, Quadratic and Cubic Equations
	double d = eqn[3];
	if (d == 0.0) {
	    // The cubic has degenerated to quadratic (or line or ...).
	    return QuadCurve2D.solveQuadratic2(eqn, res);
	}
	double a = eqn[2] / d;
	double b = eqn[1] / d;
	double c = eqn[0] / d;
	int roots = 0;
	double Q = (a * a - 3.0 * b) / 9.0;
	double R = (2.0 * a * a * a - 9.0 * a * b + 27.0 * c) / 54.0;
	double R2 = R * R;
	double Q3 = Q * Q * Q;
	a = a / 3.0;
	if (R2 < Q3) {
	    double theta = Math.acos(R / Math.sqrt(Q3));
	    Q = -2.0 * Math.sqrt(Q);
	    if (res == eqn) {
		// Copy the eqn so that we don't clobber it with the
		// roots.  This is needed so that fixRoots can do its
		// work with the original equation.
		eqn = new double[4];
		System.arraycopy(res, 0, eqn, 0, 4);
	    }
	    res[roots++] = Q * Math.cos(theta / 3.0) - a;
	    res[roots++] = Q * Math.cos((theta + Math.PI * 2.0)/ 3.0) - a;
	    res[roots++] = Q * Math.cos((theta - Math.PI * 2.0)/ 3.0) - a;
	    fixRoots(res, eqn);
	} else {
	    boolean neg = (R < 0.0);
	    double S = Math.sqrt(R2 - Q3);
	    if (neg) {
		R = -R;
	    }
	    double A = Math.pow(R + S, 1.0 / 3.0);
	    if (!neg) {
		A = -A;
	    }
	    double B = (A == 0.0) ? 0.0 : (Q / A);
	    res[roots++] = (A + B) - a;
	}
	return roots;
    }

    /*
     * This pruning step is necessary since solveCubic uses the
     * cosine function to calculate the roots when there are 3
     * of them.  Since the cosine method can have an error of
     * +/- 1E-14 we need to make sure that we don't make any
     * bad decisions due to an error.
     * 
     * If the root is not near one of the endpoints, then we will
     * only have a slight inaccuracy in calculating the x intercept
     * which will only cause a slightly wrong answer for some
     * points very close to the curve.  While the results in that
     * case are not as accurate as they could be, they are not
     * disastrously inaccurate either.
     * 
     * On the other hand, if the error happens near one end of
     * the curve, then our processing to reject values outside
     * of the t=[0,1] range will fail and the results of that
     * failure will be disastrous since for an entire horizontal
     * range of test points, we will either overcount or undercount
     * the crossings and get a wrong answer for all of them, even
     * when they are clearly and obviously inside or outside the
     * curve.
     * 
     * To work around this problem, we try a couple of Newton-Raphson
     * iterations to see if the true root is closer to the endpoint
     * or further away.  If it is further away, then we can stop
     * since we know we are on the right side of the endpoint.  If
     * we change direction, then either we are now being dragged away
     * from the endpoint in which case the first condition will cause
     * us to stop, or we have passed the endpoint and are headed back.
     * In the second case, we simply evaluate the slope at the
     * endpoint itself and place ourselves on the appropriate side
     * of it or on it depending on that result.
     */
    private static void fixRoots(double res[], double eqn[]) {
	final double EPSILON = 1E-5;
	for (int i = 0; i < 3; i++) {
	    double t = res[i];
	    if (Math.abs(t) < EPSILON) {
		res[i] = findZero(t, 0, eqn);
	    } else if (Math.abs(t - 1) < EPSILON) {
		res[i] = findZero(t, 1, eqn);
	    }
	}
    }

    private static double solveEqn(double eqn[], int order, double t) {
	double v = eqn[order];
	while (--order >= 0) {
	    v = v * t + eqn[order];
	}
	return v;
    }

    private static double findZero(double t, double target, double eqn[]) {
	double slopeqn[] = {eqn[1], 2*eqn[2], 3*eqn[3]};
	double slope;
	double origdelta = 0;
	double origt = t;
	while (true) {
	    slope = solveEqn(slopeqn, 2, t);
	    if (slope == 0) {
		// At a local minima - must return
		return t;
	    }
	    double y = solveEqn(eqn, 3, t);
	    if (y == 0) {
		// Found it! - return it
		return t;
	    }
	    // assert(slope != 0 && y != 0);
	    double delta = - (y / slope);
	    // assert(delta != 0);
	    if (origdelta == 0) {
		origdelta = delta;
	    }
	    if (t < target) {
		if (delta < 0) return t;
	    } else if (t > target) {
		if (delta > 0) return t;
	    } else { /* t == target */
		return (delta > 0
			? (target + java.lang.Double.MIN_VALUE)
			: (target - java.lang.Double.MIN_VALUE));
	    }
	    double newt = t + delta;
	    if (t == newt) {
		// The deltas are so small that we aren't moving...
		return t;
	    }
	    if (delta * origdelta < 0) {
		// We have reversed our path.
		int tag = (origt < t
			   ? getTag(target, origt, t)
			   : getTag(target, t, origt));
		if (tag != INSIDE) {
		    // Local minima found away from target - return the middle
		    return (origt + t) / 2;
		}
		// Local minima somewhere near target - move to target
		// and let the slope determine the resulting t.
		t = target;
	    } else {
		t = newt;
	    }
	}
    }

    /*
     * Fill an array with the coefficients of the parametric equation
     * in t, ready for solving against val with solveCubic.
     * We currently have:
     * <pre>
     *   val = P(t) = C1(1-t)^3 + 3CP1 t(1-t)^2 + 3CP2 t^2(1-t) + C2 t^3
     *              = C1 - 3C1t + 3C1t^2 - C1t^3 +
     *                3CP1t - 6CP1t^2 + 3CP1t^3 +
     *                3CP2t^2 - 3CP2t^3 +
     *                C2t^3
     *            0 = (C1 - val) +
     *                (3CP1 - 3C1) t +
     *                (3C1 - 6CP1 + 3CP2) t^2 +
     *                (C2 - 3CP2 + 3CP1 - C1) t^3
     *            0 = C + Bt + At^2 + Dt^3
     *     C = C1 - val
     *     B = 3*CP1 - 3*C1
     *     A = 3*CP2 - 6*CP1 + 3*C1
     *     D = C2 - 3*CP2 + 3*CP1 - C1
     * </pre>
     */
    private static void fillEqn(double eqn[], double val,
				double c1, double cp1, double cp2, double c2) {
	eqn[0] = c1 - val;
	eqn[1] = (cp1 - c1) * 3.0;
	eqn[2] = (cp2 - cp1 - cp1 + c1) * 3.0;
	eqn[3] = c2 + (cp1 - cp2) * 3.0 - c1;
	return;
    }

    /*
     * Evaluate the t values in the first num slots of the vals[] array
     * and place the evaluated values back into the same array.  Only
     * evaluate t values that are within the range <0, 1>, including
     * the 0 and 1 ends of the range iff the include0 or include1
     * booleans are true.  If an "inflection" equation is handed in,
     * then any points which represent a point of inflection for that
     * cubic equation are also ignored.
     */
//    private static int evalCubic(double vals[], int num,
//				 boolean include0,
//				 boolean include1,
//				 double inflect[],
//				 double c1, double cp1,
//				 double cp2, double c2) {
//	int j = 0;
//	for (int i = 0; i < num; i++) {
//	    double t = vals[i];
//	    if ((include0 ? t >= 0 : t > 0) &&
//		(include1 ? t <= 1 : t < 1) &&
//		(inflect == null ||
//		 inflect[1] + (2*inflect[2] + 3*inflect[3]*t)*t != 0))
//	    {
//		double u = 1 - t;
//		vals[j++] = c1*u*u*u + 3*cp1*t*u*u + 3*cp2*t*t*u + c2*t*t*t;
//	    }
//	}
//	return j;
//    }

    private static final int BELOW = -2;
    private static final int LOWEDGE = -1;
    private static final int INSIDE = 0;
    private static final int HIGHEDGE = 1;
    private static final int ABOVE = 2;

    /*
     * Determine where coord lies with respect to the range from
     * low to high.  It is assumed that low <= high.  The return
     * value is one of the 5 values BELOW, LOWEDGE, INSIDE, HIGHEDGE,
     * or ABOVE.
     */
    private static int getTag(double coord, double low, double high) {
	if (coord <= low) {
	    return (coord < low ? BELOW : LOWEDGE);
	}
	if (coord >= high) {
	    return (coord > high ? ABOVE : HIGHEDGE);
	}
	return INSIDE;
    }

    /*
     * Determine if the pttag represents a coordinate that is already
     * in its test range, or is on the border with either of the two
     * opttags representing another coordinate that is "towards the
     * inside" of that test range.  In other words, are either of the
     * two "opt" points "drawing the pt inward"?
     */
    private static boolean inwards(int pttag, int opt1tag, int opt2tag) {
	switch (pttag) {
	case BELOW:
	case ABOVE:
	default:
	    return false;
	case LOWEDGE:
	    return (opt1tag >= INSIDE || opt2tag >= INSIDE);
	case INSIDE:
	    return true;
	case HIGHEDGE:
	    return (opt1tag <= INSIDE || opt2tag <= INSIDE);
	}
    }

    @Override
    /**
     * Creates a new object of the same class as this object.
     *
     * @return     a clone of this instance.
     * @exception  OutOfMemoryError            if there is not enough memory.
     * @see        java.lang.Cloneable
     * @since      1.2
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
