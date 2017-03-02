/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sec.sun.awt.geom;

import java.util.ArrayList;

/**
 *
 * @author Michael Deutch
 */
public class Enumeration {

    private ArrayList _vector = null;
    private int currentIndex = 0;

    public Enumeration(ArrayList vector) {
        _vector = vector;
    }

    public Object nextElement() {
        if (currentIndex < _vector.size()) {
            return _vector.get(currentIndex++);
        } else {
            return null;
        }
    }

    public boolean hasMoreElements() {
        if (currentIndex < _vector.size()) {
            return true;
        } else {
            return false;
        }
    }
}
