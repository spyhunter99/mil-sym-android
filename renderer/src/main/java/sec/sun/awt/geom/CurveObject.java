/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sec.sun.awt.geom;

import armyc2.c2sd.graphics2d.Rectangle2D;

/**
 *
 * @author Michael Deutch
 */
public class CurveObject {

    private Order0 order0 = null;
    private Order1 order1 = null;
    private Order2 order2 = null;
    private Order3 order3 = null;
    int order = -1;

    public CurveObject(Object obj) {
        if (obj instanceof Order0) {
            order0 = (Order0) obj;
            order = 0;
        } else if (obj instanceof Order1) {
            order1 = (Order1) obj;
            order = 1;
        } else if (obj instanceof Order2) {
            order2 = (Order2) obj;
            order = 2;
        } else if (obj instanceof Order3) {
            order3 = (Order3) obj;
            order = 3;
        }
        setParent();
    }

    private void setParent() {
        switch (order) {
            case 0:
                order0.setParent(this);
                break;
            case 1:
                order1.setParent(this);
                break;
            case 2:
                order2.setParent(this);
                break;
            case 3:
                order3.setParent(this);
                break;
            default:
                break;
        }
        return;
    }

    public Object getCurve() {
        switch (order) {
            case 0:
                return order0;
            case 1:
                return order1;
            case 2:
                return order2;
            case 3:
                return order3;
            default:
                return null;
        }
    }

    public int getOrder() {
        return order;
    }

    public double getXTop() {
        switch (order) {
            case 0:
                return order0.getXTop();
            case 1:
                return order1.getXTop();
            case 2:
                return order2.getXTop();
            case 3:
                return order3.getXTop();
            default:
                return -7;
        }
    }

    public CurveObject(int direction) {
        //this.direction = direction;
        switch (order) {
            case 0:
                order0.direction = direction;
                break;
            case 1:
                order1.direction = direction;
                break;
            case 2:
                order2.direction = direction;
                break;
            case 3:
                order3.direction = direction;
                break;
            default:
                break;
        }
    }

    public double getYTop() {
        switch (order) {
            case 0:
                return order0.getYTop();
            case 1:
                return order1.getYTop();
            case 2:
                return order2.getYTop();
            case 3:
                return order3.getYTop();
            default:
                return -7;
        }
    }

    public double getXBot() {
        switch (order) {
            case 0:
                return order0.getXBot();
            case 1:
                return order1.getXBot();
            case 2:
                return order2.getXBot();
            case 3:
                return order3.getXBot();
            default:
                return -7;
        }

    }

    public double getYBot() {
        switch (order) {
            case 0:
                return order0.getYBot();
            case 1:
                return order1.getYBot();
            case 2:
                return order2.getYBot();
            case 3:
                return order3.getYBot();
            default:
                return -7;
        }

    }

    public double getXMin() {
        switch (order) {
            case 0:
                return order0.getXMin();
            case 1:
                return order1.getXMin();
            case 2:
                return order2.getXMin();
            case 3:
                return order3.getXMin();
            default:
                return -7;
        }
    }

    public double getXMax() {
        switch (order) {
            case 0:
                return order0.getXMax();
            case 1:
                return order1.getXMax();
            case 2:
                return order2.getXMax();
            case 3:
                return order3.getXMax();
            default:
                return -7;
        }
    }

    public final int getDirection() {
        //return direction;
        switch (order) {
            case 0:
                return order0.direction;
            case 1:
                return order1.direction;
            case 2:
                return order2.direction;
            case 3:
                return order3.direction;
            default:
                return -1;
        }
    }

    public double XforY(double y) {
        switch (order) {
            case 0:
                return order0.XforY(y);
            case 1:
                return order1.XforY(y);
            case 2:
                return order2.XforY(y);
            case 3:
                return order3.XforY(y);
            default:
                return -7;
        }
    }

    public Object getReversedCurve() {
        switch (order) {
            case 0:
                return order0.getReversedCurve();
            case 1:
                return order1.getReversedCurve();
            case 2:
                return order2.getReversedCurve();
            case 3:
                return order3.getReversedCurve();
            default:
                return null;
        }
    }

    public double getX0() {
        switch (order) {
            case 0:
                return order0.getX0();
            case 1:
                return order1.getX0();
            case 2:
                return order2.getX0();
            case 3:
                return order3.getX0();
            default:
                return -7;
        }
    }

    public double getY0() {
        switch (order) {
            case 0:
                return order0.getY0();
            case 1:
                return order1.getY0();
            case 2:
                return order2.getY0();
            case 3:
                return order3.getY0();
            default:
                return -7;
        }
    }

    public double getX1() {
        switch (order) {
            case 0:
                return order0.getX1();
            case 1:
                return order1.getX1();
            case 2:
                return order2.getX1();
            case 3:
                return order3.getX1();
            default:
                return -7;
        }
    }

    public double getY1() {
        switch (order) {
            case 0:
                return order0.getY1();
            case 1:
                return order1.getY1();
            case 2:
                return order2.getY1();
            case 3:
                return order3.getY1();
            default:
                return -7;
        }
    }

    public double XforT(double t) {
        switch (order) {
            case 0:
                return order0.XforT(t);
            case 1:
                return order1.XforT(t);
            case 2:
                return order2.XforT(t);
            case 3:
                return order3.XforT(t);
            default:
                return -7;
        }
    }

    public double YforT(double t) {
        switch (order) {
            case 0:
                return order0.YforT(t);
            case 1:
                return order1.YforT(t);
            case 2:
                return order2.YforT(t);
            case 3:
                return order3.YforT(t);
            default:
                return -7;
        }
    }

    public double TforY(double t) {
        switch (order) {
            case 0:
                return order0.TforY(t);
            case 1:
                return order1.TforY(t);
            case 2:
                return order2.TforY(t);
            case 3:
                return order3.TforY(t);
            default:
                return -7;
        }
    }

    public double nextVertical(double t0, double t1) {
        switch (order) {
            case 0:
                return order0.nextVertical(t0, t1);
            case 1:
                return order1.nextVertical(t0, t1);
            case 2:
                return order2.nextVertical(t0, t1);
            case 3:
                return order3.nextVertical(t0, t1);
            default:
                return -7;
        }
    }

    public String controlPointString() {
        switch (order) {
            case 0:
                return "";
            case 1:
                return "";
            case 2:
                return order2.controlPointString();
            case 3:
                return order3.controlPointString();
            default:
                return "";
        }
    }

    @Override
    public String toString() {
        return ("Curve["
                + getOrder() + ", "
                + ("(" + Curve.round(this.getX0()) + ", " + Curve.round(this.getY0()) + "), ")
                + this.controlPointString()
                + ("(" + Curve.round(getX1()) + ", " + Curve.round(getY1()) + "), ")
                + //(direction == Curve.INCREASING ? "D" : "U")+
                (this.getDirection() == Curve.INCREASING ? "D" : "U")
                + "]");
    }

    public int crossingsFor(double x, double y) {
        if (y >= this.getYTop() && y < this.getYBot()) {
            if (x < this.getXMax() && (x < this.getXMin() || x < this.XforY(y))) {
                return 1;
            }
        }
        return 0;
    }

    public boolean accumulateCrossings(CrossingsObject c) {
        double xhi = c.getXHi();
        if (getXMin() >= xhi) {
            return false;
        }
        double xlo = c.getXLo();
        double ylo = c.getYLo();
        double yhi = c.getYHi();
        double y0 = getYTop();
        double y1 = getYBot();
        double tstart, ystart, tend, yend;
        if (y0 < ylo) {
            if (y1 <= ylo) {
                return false;
            }
            ystart = ylo;
            tstart = this.TforY(ylo);
        } else {
            if (y0 >= yhi) {
                return false;
            }
            ystart = y0;
            tstart = 0;
        }
        if (y1 > yhi) {
            yend = yhi;
            tend = TforY(yhi);
        } else {
            yend = y1;
            tend = 1;
        }
        boolean hitLo = false;
        boolean hitHi = false;
        while (true) {
            double x = XforT(tstart);
            if (x < xhi) {
                if (hitHi || x > xlo) {
                    return true;
                }
                hitLo = true;
            } else {
                if (hitLo) {
                    return true;
                }
                hitHi = true;
            }
            if (tstart >= tend) {
                break;
            }
            tstart = nextVertical(tstart, tend);
        }
        if (hitLo) {
            //c.record(ystart, yend, direction);
            c.record(ystart, yend, this.getDirection());
        }
        return false;
    }

    public double refineTforY(double t0, double yt0, double y0) {
        double t1 = 1;
        while (true) {
            double th = (t0 + t1) / 2;
            if (th == t0 || th == t1) {
                return t1;
            }
            double y = YforT(th);
            if (y < y0) {
                t0 = th;
                yt0 = y;
            } else if (y > y0) {
                t1 = th;
            } else {
                return t1;
            }
        }
    }

    public boolean findIntersect(CurveObject that, double yrange[], double ymin,
            int slevel, int tlevel,
            double s0, double xs0, double ys0,
            double s1, double xs1, double ys1,
            double t0, double xt0, double yt0,
            double t1, double xt1, double yt1) {
        /*
         String pad = "        ";
         pad = pad+pad+pad+pad+pad;
         pad = pad+pad;
         System.out.println("----------------------------------------------");
         System.out.println(pad.substring(0, slevel)+ys0);
         System.out.println(pad.substring(0, slevel)+ys1);
         System.out.println(pad.substring(0, slevel)+(s1-s0));
         System.out.println("-------");
         System.out.println(pad.substring(0, tlevel)+yt0);
         System.out.println(pad.substring(0, tlevel)+yt1);
         System.out.println(pad.substring(0, tlevel)+(t1-t0));
         */
        if (ys0 > yt1 || yt0 > ys1) {
            return false;
        }
        if (Math.min(xs0, xs1) > Math.max(xt0, xt1)
                || Math.max(xs0, xs1) < Math.min(xt0, xt1)) {
            return false;
        }
        // Bounding boxes intersect - back off the larger of
        // the two subcurves by half until they stop intersecting
        // (or until they get small enough to switch to a more
        //  intensive algorithm).
        if (s1 - s0 > Curve.TMIN) {
            double s = (s0 + s1) / 2;
            double xs = this.XforT(s);
            double ys = this.YforT(s);
            if (s == s0 || s == s1) {
                System.out.println("s0 = " + s0);
                System.out.println("s1 = " + s1);
                throw new InternalError("no s progress!");
            }
            if (t1 - t0 > Curve.TMIN) {
                double t = (t0 + t1) / 2;
                double xt = that.XforT(t);
                double yt = that.YforT(t);
                if (t == t0 || t == t1) {
                    System.out.println("t0 = " + t0);
                    System.out.println("t1 = " + t1);
                    throw new InternalError("no t progress!");
                }
                if (ys >= yt0 && yt >= ys0) {
                    if (findIntersect(that, yrange, ymin, slevel + 1, tlevel + 1,
                            s0, xs0, ys0, s, xs, ys,
                            t0, xt0, yt0, t, xt, yt)) {
                        return true;
                    }
                }
                if (ys >= yt) {
                    if (findIntersect(that, yrange, ymin, slevel + 1, tlevel + 1,
                            s0, xs0, ys0, s, xs, ys,
                            t, xt, yt, t1, xt1, yt1)) {
                        return true;
                    }
                }
                if (yt >= ys) {
                    if (findIntersect(that, yrange, ymin, slevel + 1, tlevel + 1,
                            s, xs, ys, s1, xs1, ys1,
                            t0, xt0, yt0, t, xt, yt)) {
                        return true;
                    }
                }
                if (ys1 >= yt && yt1 >= ys) {
                    if (findIntersect(that, yrange, ymin, slevel + 1, tlevel + 1,
                            s, xs, ys, s1, xs1, ys1,
                            t, xt, yt, t1, xt1, yt1)) {
                        return true;
                    }
                }
            } else {
                if (ys >= yt0) {
                    if (findIntersect(that, yrange, ymin, slevel + 1, tlevel,
                            s0, xs0, ys0, s, xs, ys,
                            t0, xt0, yt0, t1, xt1, yt1)) {
                        return true;
                    }
                }
                if (yt1 >= ys) {
                    if (findIntersect(that, yrange, ymin, slevel + 1, tlevel,
                            s, xs, ys, s1, xs1, ys1,
                            t0, xt0, yt0, t1, xt1, yt1)) {
                        return true;
                    }
                }
            }
        } else if (t1 - t0 > Curve.TMIN) {
            double t = (t0 + t1) / 2;
            double xt = that.XforT(t);
            double yt = that.YforT(t);
            if (t == t0 || t == t1) {
                System.out.println("t0 = " + t0);
                System.out.println("t1 = " + t1);
                throw new InternalError("no t progress!");
            }
            if (yt >= ys0) {
                if (findIntersect(that, yrange, ymin, slevel, tlevel + 1,
                        s0, xs0, ys0, s1, xs1, ys1,
                        t0, xt0, yt0, t, xt, yt)) {
                    return true;
                }
            }
            if (ys1 >= yt) {
                if (findIntersect(that, yrange, ymin, slevel, tlevel + 1,
                        s0, xs0, ys0, s1, xs1, ys1,
                        t, xt, yt, t1, xt1, yt1)) {
                    return true;
                }
            }
        } else {
            // No more subdivisions
            double xlk = xs1 - xs0;
            double ylk = ys1 - ys0;
            double xnm = xt1 - xt0;
            double ynm = yt1 - yt0;
            double xmk = xt0 - xs0;
            double ymk = yt0 - ys0;
            double det = xnm * ylk - ynm * xlk;
            if (det != 0) {
                double detinv = 1 / det;
                double s = (xnm * ymk - ynm * xmk) * detinv;
                double t = (xlk * ymk - ylk * xmk) * detinv;
                if (s >= 0 && s <= 1 && t >= 0 && t <= 1) {
                    s = s0 + s * (s1 - s0);
                    t = t0 + t * (t1 - t0);
                    if (s < 0 || s > 1 || t < 0 || t > 1) {
                        System.out.println("Uh oh!");
                    }
                    double y = (this.YforT(s) + that.YforT(t)) / 2;
                    if (y <= yrange[1] && y > yrange[0]) {
                        yrange[1] = y;
                        return true;
                    }
                }
            }
            //System.out.println("Testing lines!");
        }
        return false;
    }

    public int compareTo(CurveObject that, double yrange[]) {
        /*
         System.out.println(this+".compareTo("+that+")");
         System.out.println("target range = "+yrange[0]+"=>"+yrange[1]);
         */
        if (order == 1) {
            return order1.compareTo(that, yrange);
        }
        double y0 = yrange[0];
        double y1 = yrange[1];
        //y1 = Math.min(Math.min(y1, this.getYBot()), that.getYBot());
        y1 = Math.min(Math.min(y1, this.getYBot()), that.getYBot());
        if (y1 <= yrange[0]) {
            System.err.println("this == " + this);
            System.err.println("that == " + that);
            System.out.println("target range = " + yrange[0] + "=>" + yrange[1]);
            throw new InternalError("backstepping from " + yrange[0] + " to " + y1);
        }
        yrange[1] = y1;
        if (this.getXMax() <= that.getXMin()) {
            if (this.getXMin() == that.getXMax()) {
                return 0;
            }
            return -1;
        }
        if (this.getXMin() >= that.getXMax()) {
            return 1;
        }
        // Parameter s for thi(s) curve and t for tha(t) curve
        // [st]0 = parameters for top of current section of interest
        // [st]1 = parameters for bottom of valid range
        // [st]h = parameters for hypothesis point
        // [d][xy]s = valuations of thi(s) curve at sh
        // [d][xy]t = valuations of tha(t) curve at th
        double s0 = this.TforY(y0);
        double ys0 = this.YforT(s0);
        if (ys0 < y0) {
            s0 = refineTforY(s0, ys0, y0);
            ys0 = this.YforT(s0);
        }
        double s1 = this.TforY(y1);
        if (this.YforT(s1) < y0) {
            s1 = refineTforY(s1, this.YforT(s1), y0);
            //System.out.println("s1 problem!");
        }
        double t0 = that.TforY(y0);
        double yt0 = that.YforT(t0);
        if (yt0 < y0) {
            t0 = that.refineTforY(t0, yt0, y0);
            yt0 = that.YforT(t0);
        }
        double t1 = that.TforY(y1);
        if (that.YforT(t1) < y0) {
            t1 = that.refineTforY(t1, that.YforT(t1), y0);
        }
        double xs0 = this.XforT(s0);
        double xt0 = that.XforT(t0);
        double scale = Math.max(Math.abs(y0), Math.abs(y1));
        double ymin = Math.max(scale * 1E-14, 1E-300);
        if (Curve.fairlyClose(xs0, xt0)) {
            double bump = ymin;
            double maxbump = Math.min(ymin * 1E13, (y1 - y0) * .1);
            double y = y0 + bump;
            while (y <= y1) {
                if (Curve.fairlyClose(this.XforY(y), that.XforY(y))) {
                    if ((bump *= 2) > maxbump) {
                        bump = maxbump;
                    }
                } else {
                    y -= bump;
                    while (true) {
                        bump /= 2;
                        double newy = y + bump;
                        if (newy <= y) {
                            break;
                        }
                        if (Curve.fairlyClose(this.XforY(newy), that.XforY(newy))) {
                            y = newy;
                        }
                    }
                    break;
                }
                y += bump;
            }
            if (y > y0) {
                if (y < y1) {
                    yrange[1] = y;
                }
                return 0;
            }
        }
        //double ymin = y1 * 1E-14;
        if (ymin <= 0) {
            System.out.println("ymin = " + ymin);
        }
        /*
         System.out.println("s range = "+s0+" to "+s1);
         System.out.println("t range = "+t0+" to "+t1);
         */
        while (s0 < s1 && t0 < t1) {
            double sh = this.nextVertical(s0, s1);
            double xsh = this.XforT(sh);
            double ysh = this.YforT(sh);
            double th = that.nextVertical(t0, t1);
            double xth = that.XforT(th);
            double yth = that.YforT(th);
            /*
             System.out.println("sh = "+sh);
             System.out.println("th = "+th);
             */
            try {
                if (findIntersect(that, yrange, ymin, 0, 0,
                        s0, xs0, ys0, sh, xsh, ysh,
                        t0, xt0, yt0, th, xth, yth)) {
                    break;
                }
            } catch (Throwable t) {
                System.err.println("Error: " + t);
                System.err.println("y range was " + yrange[0] + "=>" + yrange[1]);
                System.err.println("s y range is " + ys0 + "=>" + ysh);
                System.err.println("t y range is " + yt0 + "=>" + yth);
                System.err.println("ymin is " + ymin);
                return 0;
            }
            if (ysh < yth) {
                if (ysh > yrange[0]) {
                    if (ysh < yrange[1]) {
                        yrange[1] = ysh;
                    }
                    break;
                }
                s0 = sh;
                xs0 = xsh;
                ys0 = ysh;
            } else {
                if (yth > yrange[0]) {
                    if (yth < yrange[1]) {
                        yrange[1] = yth;
                    }
                    break;
                }
                t0 = th;
                xt0 = xth;
                yt0 = yth;
            }
        }
        double ymid = (yrange[0] + yrange[1]) / 2;
        /*
         System.out.println("final this["+s0+", "+sh+", "+s1+"]");
         System.out.println("final    y["+ys0+", "+ysh+"]");
         System.out.println("final that["+t0+", "+th+", "+t1+"]");
         System.out.println("final    y["+yt0+", "+yth+"]");
         System.out.println("final order = "+orderof(this.XforY(ymid),
         that.XforY(ymid)));
         System.out.println("final range = "+yrange[0]+"=>"+yrange[1]);
         */
        /*
         System.out.println("final sx = "+this.XforY(ymid));
         System.out.println("final tx = "+that.XforY(ymid));
         System.out.println("final order = "+orderof(this.XforY(ymid),
         that.XforY(ymid)));
         */
        return Curve.orderof(this.XforY(ymid), that.XforY(ymid));
    }

    public int getSegment(double coords[]) {
        switch (order) {
            case 0:
                return order0.getSegment(coords);
            case 1:
                return order1.getSegment(coords);
            case 2:
                return order2.getSegment(coords);
            case 3:
                return order3.getSegment(coords);
            default:
                return -7;
        }

    }

    public Object getSubCurve(double ystart, double yend, int dir) {    //did return Curve
        switch (order) {
            case 0:
                return order0.getSubCurve(ystart, yend, dir);
            case 1:
                return order1.getSubCurve(ystart, yend, dir);
            case 2:
                return order2.getSubCurve(ystart, yend, dir);
            case 3:
                return order3.getSubCurve(ystart, yend, dir);
            default:
                return null;
        }
    }

    public void enlarge(Rectangle2D r) {
        switch (order) {
            case 0:
                order0.enlarge(r);
            case 1:
                order1.enlarge(r);
            case 2:
                order2.enlarge(r);
            case 3:
                order3.enlarge(r);
            default:
                return;
        }
    }

    public Object getWithDirection(int direction) {
        //return (this.direction == direction ? this : getReversedCurve());
        //return (this.getDirection() == direction ? this : getReversedCurve());
        switch (order) {
            case 0:
                return order0.getWithDirection(direction);
            case 1:
                return order1.getWithDirection(direction);
            case 2:
                return order2.getWithDirection(direction);
            case 3:
                return order3.getWithDirection(direction);
            default:
                return null;
        }
    }
}
