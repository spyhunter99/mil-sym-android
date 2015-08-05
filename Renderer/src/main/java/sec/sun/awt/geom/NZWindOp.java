/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sec.sun.awt.geom;

/**
 *
 * @author Michael Deutch
 */
public class NZWindOp {

    private int count;

    public void newRow() {
        count = 0;
    }

    public int classify(Edge e) {
            // Note: the right curves should be an empty set with this op...
        // assert(e.getCurveTag() == CTAG_LEFT);
        int newCount = count;
        int type = (newCount == 0 ? AreaOp2.ETAG_ENTER : AreaOp2.ETAG_IGNORE);
        newCount += e.getCurve().getDirection();
        count = newCount;
        return (newCount == 0 ? AreaOp2.ETAG_EXIT : type);
    }

    public int getState() {
        return ((count == 0) ? AreaOp2.RSTAG_OUTSIDE : AreaOp2.RSTAG_INSIDE);
    }

}
