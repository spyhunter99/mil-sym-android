/*
 * Copyright 1998 Sun Microsystems, Inc.  All Rights Reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Sun designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Sun in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
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

package sec.sun.awt.geom;

//import java.awt.geom.Rectangle2D;
//import java.awt.geom.PathIterator;
//import java.util.Vector;
import armyc2.c2sd.graphics2d.PathIterator;
import armyc2.c2sd.graphics2d.Rectangle2D;
final class Order0 /* extends Curve */{
    
    private double x;
    private double y;
    public int direction=-1;
    public Order0(double x, double y) {
        //super(INCREASING);
        direction=Curve.INCREASING;
        
        this.x = x;
        this.y = y;
    }
    public int getOrder() {
        return 0;
    }

    public double getXTop() {
        return x;
    }

    public double getYTop() {
        return y;
    }

    public double getXBot() {
        return x;
    }

    public double getYBot() {
        return y;
    }

    public double getXMin() {
        return x;
    }

    public double getXMax() {
        return x;
    }

    public double getX0() {
        return x;
    }

    public double getY0() {
        return y;
    }

    public double getX1() {
        return x;
    }

    public double getY1() {
        return y;
    }

    public double XforY(double y) {
        return y;
    }

    public double TforY(double y) {
        return 0;
    }

    public double XforT(double t) {
        return x;
    }

    public double YforT(double t) {
        return y;
    }

    public double dXforT(double t, int deriv) {
        return 0;
    }

    public double dYforT(double t, int deriv) {
        return 0;
    }

    public double nextVertical(double t0, double t1) {
        return t1;
    }

    public int crossingsFor(double x, double y) {
        return 0;
    }

    public boolean accumulateCrossings(Crossings c) {
        return (x > c.getXLo() &&
                x < c.getXHi() &&
                y > c.getYLo() &&
                y < c.getYHi());
    }

    public void enlarge(Rectangle2D r) {
        r.add(x, y);
    }

    public Order0 getSubCurve(double ystart, double yend, int dir) {    //did return Curve
        return this;
    }

    public Order0 getReversedCurve() {  //did return Curve
        return this;
    }

    public int getSegment(double coords[]) {
        coords[0] = x;
        coords[1] = y;
        return PathIterator.SEG_MOVETO;
    }
    public String controlPointString() {
        return "";
    }
    public Order0 getWithDirection(int direction) {
        return (this.direction == direction ? this : getReversedCurve());
    }
    private CurveObject _parent=null;
    protected void setParent(CurveObject parent)
    {
        _parent=parent;
    }
    protected CurveObject getParent()
    {
        return _parent;
    }
}
