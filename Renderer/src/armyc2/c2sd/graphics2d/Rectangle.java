/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package armyc2.c2sd.graphics2d;
/**
 *
 * @author Michael Deutch
 */
public class Rectangle implements Shape{
    public int x=0;
    public int y=0;
    public int width=0;
    public int height=0;
    public Rectangle()
    {
        x=0;
        y=0;
        width=0;
        height=0;
    }
    public Rectangle(int x1, int y1, int width1, int height1)
    {
        x=x1;
        y=y1;
        width=width1;
        height=height1;
    }
    public Rectangle getBounds()
    {
        return null;
    }
    public PathIterator getPathIterator(AffineTransform at)
    {
        return null;
    }
    public boolean intersects(Rectangle2D rect)
    {
        if(x+width<rect.x)
            return false;
        if(x>rect.x+rect.width)
            return false;
        if(y+height<rect.y)
            return false;
        if(y>rect.y+rect.height)
            return false;
        
        return true;
    }
    public boolean intersects(double x1, double y1, double width1, double height1)
    {
        if(x+width<x1)
            return false;
        if(x>x1+width1)
            return false;
        if(y+height<y1)
            return false;
        if(y>y1+height1)
            return false;
        
        return true;
    }
    public boolean contains (int x1, int y1)
    {
        if(x<=x1 && x1<=x+width  && 
                y<=y1 && y1<=y+height)
            return true;
        else return false;
    }
    public boolean contains (int x1, int y1, int width1, int height1)
    {                             
        if(this.contains(x1, y1) && this.contains(x1+width1, y1+height1))
            return true;
        else return false;
    }
    public boolean contains (Point2D pt)
    {
        if(x<=pt.getX() && pt.getX()<=x+width  && 
                y<=pt.getY() && pt.getY()<=y+height)
            return true;
        else return false;
    }
    public Rectangle2D getBounds2D()
    {
        return new Rectangle2D.Double(x,y,width,height);
    }
     public double getX()
    {
        return x;
    }
    public int getY()
    {
        return y;
    }
    public int getMinX()
    {
        return x;
    }
    public int getMinY()
    {
        return y;
    }
    public int getMaxX()
    {
        return x+width;
    }
    public int getMaxY()
    {
        return y+height;
    }
    public int getHeight()
    {
        return height;
    }
    public int getWidth()
    {
        return width;
    }
    //must complete this function
    public void grow(int h, int v)  
    {
        //return;
    }
    public void setRect(Rectangle rect)
    {
        x=rect.x;
        y=rect.y;
        width=rect.width;
        height=rect.height;
    }
}
