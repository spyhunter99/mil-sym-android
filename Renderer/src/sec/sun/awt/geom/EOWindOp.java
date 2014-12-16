/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sec.sun.awt.geom;

/**
 *
 * @author Michael Deutch
 */
public class EOWindOp {
        private boolean inside;

        public void newRow() {
            inside = false;
        }

        public int classify(Edge e) {
            // Note: the right curves should be an empty set with this op...
            // assert(e.getCurveTag() == CTAG_LEFT);
            boolean newInside = !inside;
            inside = newInside;
            return (newInside ? AreaOp2.ETAG_ENTER : AreaOp2.ETAG_EXIT);
        }

        public int getState() {
            return (inside ? AreaOp2.RSTAG_INSIDE : AreaOp2.RSTAG_OUTSIDE);
        }
    
}
