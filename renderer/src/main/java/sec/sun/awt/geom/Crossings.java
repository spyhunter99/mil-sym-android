/*
 * Copyright 1998-2003 Sun Microsystems, Inc.  All Rights Reserved.
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

public class Crossings {

    public static final boolean debug = false;

    int limit = 0;
    double yranges[] = new double[10];

    double xlo, ylo, xhi, yhi;

    public Crossings(double xlo, double ylo, double xhi, double yhi) {
        this.xlo = xlo;
        this.ylo = ylo;
        this.xhi = xhi;
        this.yhi = yhi;
    }

    public final double getXLo() {
        return xlo;
    }

    public final double getYLo() {
        return ylo;
    }

    public final double getXHi() {
        return xhi;
    }

    public final double getYHi() {
        return yhi;
    }

    public final boolean isEmpty() {
        return (limit == 0);
    }

    public boolean accumulateLine(double x0, double y0,
            double x1, double y1) {
        if (y0 <= y1) {
            return accumulateLine2(x0, y0, x1, y1, 1);
        } else {
            return accumulateLine2(x1, y1, x0, y0, -1);
        }
    }

    public boolean accumulateLine2(double x0, double y0,
            double x1, double y1,
            int direction) {
        if (yhi <= y0 || ylo >= y1) {
            return false;
        }
        if (x0 >= xhi && x1 >= xhi) {
            return false;
        }
        if (y0 == y1) {
            return (x0 >= xlo || x1 >= xlo);
        }
        double xstart, ystart, xend, yend;
        double dx = (x1 - x0);
        double dy = (y1 - y0);
        if (y0 < ylo) {
            xstart = x0 + (ylo - y0) * dx / dy;
            ystart = ylo;
        } else {
            xstart = x0;
            ystart = y0;
        }
        if (yhi < y1) {
            xend = x0 + (yhi - y0) * dx / dy;
            yend = yhi;
        } else {
            xend = x1;
            yend = y1;
        }
        if (xstart >= xhi && xend >= xhi) {
            return false;
        }
        if (xstart > xlo || xend > xlo) {
            return true;
        }
        record(ystart, yend, direction);
        return false;
    }

    public void record(double ystart, double yend, int direction) {
        if (ystart >= yend) {
            return;
        }
        int from = 0;
        // Quickly jump over all pairs that are completely "above"
        while (from < limit && ystart > yranges[from + 1]) {
            from += 2;
        }
        int to = from;
        while (from < limit) {
            double yrlo = yranges[from++];
            double yrhi = yranges[from++];
            if (yend < yrlo) {
                // Quickly handle insertion of the new range
                yranges[to++] = ystart;
                yranges[to++] = yend;
                ystart = yrlo;
                yend = yrhi;
                continue;
            }
            // The ranges overlap - sort, collapse, insert, iterate
            double yll, ylh, yhl, yhh;
            if (ystart < yrlo) {
                yll = ystart;
                ylh = yrlo;
            } else {
                yll = yrlo;
                ylh = ystart;
            }
            if (yend < yrhi) {
                yhl = yend;
                yhh = yrhi;
            } else {
                yhl = yrhi;
                yhh = yend;
            }
            if (ylh == yhl) {
                ystart = yll;
                yend = yhh;
            } else {
                if (ylh > yhl) {
                    ystart = yhl;
                    yhl = ylh;
                    ylh = ystart;
                }
                if (yll != ylh) {
                    yranges[to++] = yll;
                    yranges[to++] = ylh;
                }
                ystart = yhl;
                yend = yhh;
            }
            if (ystart >= yend) {
                break;
            }
        }
        if (to < from && from < limit) {
            System.arraycopy(yranges, from, yranges, to, limit - from);
        }
        to += (limit - from);
        if (ystart < yend) {
            if (to >= yranges.length) {
                double newranges[] = new double[to + 10];
                System.arraycopy(yranges, 0, newranges, 0, to);
                yranges = newranges;
            }
            yranges[to++] = ystart;
            yranges[to++] = yend;
        }
        limit = to;
    }
}
