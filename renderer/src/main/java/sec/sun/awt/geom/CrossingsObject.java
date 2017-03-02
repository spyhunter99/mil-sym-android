/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sec.sun.awt.geom;

import armyc2.c2sd.graphics2d.PathIterator;

/**
 *
 * @author Michael Deutch
 */
public class CrossingsObject {

    public static final int CROSSINGS = 0;
    public static final int NONZERO = 1;
    public static final int EVENODD = 2;
    public static final boolean debug = false;
    int limit = 0;
    double yranges[] = new double[10];
    double xlo, ylo, xhi, yhi;
    private int crosscounts[] = null;

    private EvenOdd evenOdd = null;
    private Crossings crossings = null;
    private NonZero nonZero = null;
    int type = -1;

    public CrossingsObject(double xlo, double ylo, double xhi, double yhi, int type) {
        //super(xlo, ylo, xhi, yhi);
        this.xlo = xlo;
        this.ylo = ylo;
        this.xhi = xhi;
        this.yhi = yhi;
        this.type = type;
        //crosscounts = new int[yranges.length / 2];
        switch (type) {
            case CROSSINGS:
                crossings = new Crossings(xlo, ylo, xhi, yhi);
                break;
            case NONZERO:
                nonZero = new NonZero(xlo, ylo, xhi, yhi);
                crosscounts = new int[yranges.length / 2];
                break;
            case EVENODD:
                evenOdd = new EvenOdd(xlo, ylo, xhi, yhi);
                break;
        }
    }

    public double getXLo() {
        switch (type) {
            case CROSSINGS:
                return crossings.getXLo();
            case EVENODD:
                return evenOdd.getXLo();
            case NONZERO:
                return nonZero.getXLo();
            default:
                return -1;
        }
    }

    public double getYLo() {
        switch (type) {
            case CROSSINGS:
                return crossings.getYLo();
            case EVENODD:
                return evenOdd.getYLo();
            case NONZERO:
                return nonZero.getYLo();
            default:
                return -1;
        }
    }

    public double getXHi() {
        //return xhi;
        switch (type) {
            case CROSSINGS:
                return crossings.getXHi();
            case EVENODD:
                return evenOdd.getXHi();
            case NONZERO:
                return nonZero.getXHi();
            default:
                return -1;
        }
    }

    public double getYHi() {
        //return yhi;
        switch (type) {
            case CROSSINGS:
                return crossings.getYHi();
            case EVENODD:
                return evenOdd.getYHi();
            case NONZERO:
                return nonZero.getYHi();
            default:
                return -1;
        }
    }

    public boolean isEmpty() {
        //return (limit == 0);
        switch (type) {
            case CROSSINGS:
                return crossings.isEmpty();
            case EVENODD:
                return evenOdd.isEmpty();
            case NONZERO:
                return nonZero.isEmpty();
            default:
                return true;
        }
    }

    public void record(double ystart, double yend, int direction) {
        switch (type) {
            case CROSSINGS:
                crossings.record(ystart, yend, direction);
            case EVENODD:
                evenOdd.record(ystart, yend, direction);
            case NONZERO:
                nonZero.record(ystart, yend, direction);
            default:
                return;
        }

    }

    public static CrossingsObject findCrossings(Vector curves,
            double xlo, double ylo,
            double xhi, double yhi) {
        //Crossings cross = new EvenOdd(xlo, ylo, xhi, yhi);
        CrossingsObject cross = new CrossingsObject(xlo, ylo, xhi, yhi, CrossingsObject.EVENODD);
        Enumeration enum_ = curves.elements();
        while (enum_.hasMoreElements()) {
            CurveObject c = (CurveObject) enum_.nextElement();
            if (c.accumulateCrossings(cross)) {
                return null;
            }
        }
        return cross;
    }

    public CrossingsObject findCrossings2(PathIterator pi,
            double xlo, double ylo,
            double xhi, double yhi) {
        CrossingsObject cross;
        if (pi.getWindingRule() == pi.WIND_EVEN_ODD) {
            cross = new CrossingsObject(xlo, ylo, xhi, yhi, EVENODD);
        } else {
            cross = new CrossingsObject(xlo, ylo, xhi, yhi, NONZERO);
        }
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
        double movx = 0;
        double movy = 0;
        double curx = 0;
        double cury = 0;
        double newx, newy;
        while (!pi.isDone()) {
            int type = pi.currentSegment(coords);
            switch (type) {
                case PathIterator.SEG_MOVETO:
                    if (movy != cury
                            && cross.accumulateLine(curx, cury, movx, movy)) {
                        return null;
                    }
                    movx = curx = coords[0];
                    movy = cury = coords[1];
                    break;
                case PathIterator.SEG_LINETO:
                    newx = coords[0];
                    newy = coords[1];
                    if (cross.accumulateLine(curx, cury, newx, newy)) {
                        return null;
                    }
                    curx = newx;
                    cury = newy;
                    break;
                case PathIterator.SEG_QUADTO:
                    newx = coords[2];
                    newy = coords[3];
                    if (cross.accumulateQuad(curx, cury, coords)) {
                        return null;
                    }
                    curx = newx;
                    cury = newy;
                    break;
                case PathIterator.SEG_CUBICTO:
                    newx = coords[4];
                    newy = coords[5];
                    if (cross.accumulateCubic(curx, cury, coords)) {
                        return null;
                    }
                    curx = newx;
                    cury = newy;
                    break;
                case PathIterator.SEG_CLOSE:
                    if (movy != cury
                            && cross.accumulateLine(curx, cury, movx, movy)) {
                        return null;
                    }
                    curx = movx;
                    cury = movy;
                    break;
            }
            pi.next();
        }
        if (movy != cury) {
            if (cross.accumulateLine(curx, cury, movx, movy)) {
                return null;
            }
        }
        return cross;
    }

    public boolean accumulateLine(double x0, double y0,
            double x1, double y1) {
        switch (this.type) {
            case CROSSINGS:
                return crossings.accumulateLine(x0, y0, x1, y1);
            case EVENODD:
                return evenOdd.accumulateLine(x0, y0, x1, y1);
            case NONZERO:
                return nonZero.accumulateLine(x0, y0, x1, y1);
            default:
                return false;
        }
    }

    public boolean accumulateLine2(double x0, double y0,
            double x1, double y1,
            int direction) {
        switch (this.type) {
            case CROSSINGS:
                return crossings.accumulateLine2(x0, y0, x1, y1, direction);
            case EVENODD:
                return evenOdd.accumulateLine2(x0, y0, x1, y1, direction);
            case NONZERO:
                return nonZero.accumulateLine2(x0, y0, x1, y1, direction);
            default:
                return false;
        }

    }

    private Vector tmp = new Vector();

    public boolean accumulateQuad(double x0, double y0, double coords[]) {
        if (y0 < ylo && coords[1] < ylo && coords[3] < ylo) {
            return false;
        }
        if (y0 > yhi && coords[1] > yhi && coords[3] > yhi) {
            return false;
        }
        if (x0 > xhi && coords[0] > xhi && coords[2] > xhi) {
            return false;
        }
        if (x0 < xlo && coords[0] < xlo && coords[2] < xlo) {
            if (y0 < coords[3]) {
                record(Math.max(y0, ylo), Math.min(coords[3], yhi), 1);
            } else if (y0 > coords[3]) {
                record(Math.max(coords[3], ylo), Math.min(y0, yhi), -1);
            }
            return false;
        }
        Curve.insertQuad(tmp, x0, y0, coords);
        Enumeration enum_ = tmp.elements();
        while (enum_.hasMoreElements()) {
            CurveObject c = (CurveObject) enum_.nextElement();
            if (c.accumulateCrossings(this)) {
                return true;
            }
        }
        tmp.clear();
        return false;
    }

    public boolean accumulateCubic(double x0, double y0, double coords[]) {
        if (y0 < ylo && coords[1] < ylo
                && coords[3] < ylo && coords[5] < ylo) {
            return false;
        }
        if (y0 > yhi && coords[1] > yhi
                && coords[3] > yhi && coords[5] > yhi) {
            return false;
        }
        if (x0 > xhi && coords[0] > xhi
                && coords[2] > xhi && coords[4] > xhi) {
            return false;
        }
        if (x0 < xlo && coords[0] < xlo
                && coords[2] < xlo && coords[4] < xlo) {
            if (y0 <= coords[5]) {
                record(Math.max(y0, ylo), Math.min(coords[5], yhi), 1);
            } else {
                record(Math.max(coords[5], ylo), Math.min(y0, yhi), -1);
            }
            return false;
        }
        Curve.insertCubic(tmp, x0, y0, coords);
        Enumeration enum_ = tmp.elements();
        while (enum_.hasMoreElements()) {
            CurveObject c = (CurveObject) enum_.nextElement();
            if (c.accumulateCrossings(this)) {
                return true;
            }
        }
        tmp.clear();
        return false;
    }
}
