/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sec.sun.awt.geom;

/**
 *
 * @author Michael Deutch
 */
public class NonZero {

    public static final boolean debug = false;
    int limit = 0;
    double yranges[] = new double[10];
    double xlo, ylo, xhi, yhi;
    private int crosscounts[];

    public NonZero(double xlo, double ylo, double xhi, double yhi) {
        //super(xlo, ylo, xhi, yhi);
        this.xlo = xlo;
        this.ylo = ylo;
        this.xhi = xhi;
        this.yhi = yhi;
        crosscounts = new int[yranges.length / 2];
    }

    public final boolean covers(double ystart, double yend) {
        int i = 0;
        while (i < limit) {
            double ylo = yranges[i++];
            double yhi = yranges[i++];
            if (ystart >= yhi) {
                continue;
            }
            if (ystart < ylo) {
                return false;
            }
            if (yend <= yhi) {
                return true;
            }
            ystart = yhi;
        }
        return (ystart >= yend);
    }

    public void remove(int cur) {
        limit -= 2;
        int rem = limit - cur;
        if (rem > 0) {
            System.arraycopy(yranges, cur + 2, yranges, cur, rem);
            System.arraycopy(crosscounts, cur / 2 + 1,
                    crosscounts, cur / 2,
                    rem / 2);
        }
    }

    public void insert(int cur, double lo, double hi, int dir) {
        int rem = limit - cur;
        double oldranges[] = yranges;
        int oldcounts[] = crosscounts;
        if (limit >= yranges.length) {
            yranges = new double[limit + 10];
            System.arraycopy(oldranges, 0, yranges, 0, cur);
            crosscounts = new int[(limit + 10) / 2];
            System.arraycopy(oldcounts, 0, crosscounts, 0, cur / 2);
        }
        if (rem > 0) {
            System.arraycopy(oldranges, cur, yranges, cur + 2, rem);
            System.arraycopy(oldcounts, cur / 2,
                    crosscounts, cur / 2 + 1,
                    rem / 2);
        }
        yranges[cur + 0] = lo;
        yranges[cur + 1] = hi;
        crosscounts[cur / 2] = dir;
        limit += 2;
    }

    public void record(double ystart, double yend, int direction) {
        if (ystart >= yend) {
            return;
        }
        int cur = 0;
        // Quickly jump over all pairs that are completely "above"
        while (cur < limit && ystart > yranges[cur + 1]) {
            cur += 2;
        }
        if (cur < limit) {
            int rdir = crosscounts[cur / 2];
            double yrlo = yranges[cur + 0];
            double yrhi = yranges[cur + 1];
            if (yrhi == ystart && rdir == direction) {
                // Remove the range from the list and collapse it
                // into the range being inserted.  Note that the
                // new combined range may overlap the following range
                // so we must not simply combine the ranges in place
                // unless we are at the last range.
                if (cur + 2 == limit) {
                    yranges[cur + 1] = yend;
                    return;
                }
                remove(cur);
                ystart = yrlo;
                rdir = crosscounts[cur / 2];
                yrlo = yranges[cur + 0];
                yrhi = yranges[cur + 1];
            }
            if (yend < yrlo) {
                // Just insert the new range at the current location
                insert(cur, ystart, yend, direction);
                return;
            }
            if (yend == yrlo && rdir == direction) {
                // Just prepend the new range to the current one
                yranges[cur] = ystart;
                return;
            }
            // The ranges must overlap - (yend > yrlo && yrhi > ystart)
            if (ystart < yrlo) {
                insert(cur, ystart, yrlo, direction);
                cur += 2;
                ystart = yrlo;
            } else if (yrlo < ystart) {
                insert(cur, yrlo, ystart, rdir);
                cur += 2;
                yrlo = ystart;
            }
            // assert(yrlo == ystart);
            int newdir = rdir + direction;
            double newend = Math.min(yend, yrhi);
            if (newdir == 0) {
                remove(cur);
            } else {
                crosscounts[cur / 2] = newdir;
                yranges[cur++] = ystart;
                yranges[cur++] = newend;
            }
            ystart = yrlo = newend;
            if (yrlo < yrhi) {
                insert(cur, yrlo, yrhi, rdir);
            }
        }
        if (ystart < yend) {
            insert(cur, ystart, yend, direction);
        }
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
                                  double x1, double y1)
    {
        if (y0 <= y1) {
            return accumulateLine2(x0, y0, x1, y1, 1);
        } else {
            return accumulateLine2(x1, y1, x0, y0, -1);
        }
    }

    public boolean accumulateLine2(double x0, double y0,
                                  double x1, double y1,
                                  int direction)
    {
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
}
