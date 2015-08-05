/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package armyc2.c2sd.graphics2d;
//import android.graphics.RectF;
/**
 *
 * @author Michael Deutch
 */
public class TexturePaint {
    private Rectangle2D _rect=null;
    private Graphics2D _g2d=null;
    private BufferedImage _bi=null;
    public TexturePaint(BufferedImage bi, Rectangle2D rect)
    {
        _rect=rect;
        _bi=bi;
    }
}
