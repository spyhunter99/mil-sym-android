/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package armyc2.c2sd.graphics2d;
import armyc2.c2sd.renderer.utilities.ErrorLogger;
import armyc2.c2sd.renderer.utilities.RendererException;
import java.util.ArrayList;
import armyc2.c2sd.JavaLineArray.POINT2;
import armyc2.c2sd.JavaLineArray.lineutility;
import java.util.HashMap;
import java.util.Map;
/**
 *
 * @author Michael Deutch
 */
public class Area extends GeneralPath{
    private static final String _className="Area";
    //private ArrayList<POINT2>_pts=null;
    public Area(Polygon poly)
    {
        int j=0;
        int n=poly.npoints;
        //for(j=0;j<poly.npoints;j++)
        for(j=0;j<n;j++)
        {
            if(j==0)
                moveTo(poly.xpoints[j],poly.ypoints[j]);
            else
                lineTo(poly.xpoints[j],poly.ypoints[j]);
        }
    }
    public Area(Shape shape)
    {
        int j=0;
        PathIterator p=shape.getPathIterator(null);
        ArrayList<POINT2>pts=p.getPoints();
        POINT2 pt=null;
        int n=pts.size();
        //for(j=0;j<pts.size();j++)
        for(j=0;j<n;j++)
        {
            pt=pts.get(j);
            switch(pt.style)
            {
                case IPathIterator.SEG_MOVETO:
                    moveTo(pt.x,pt.y);
                    break;
                case IPathIterator.SEG_LINETO:
                    lineTo(pt.x,pt.y);
                    break;
                default:
                    break;
            }
        }    
    }
    /**
     * organizes intersect points by increasing distance from the hatch line origin
     * @param hatchLine
     * @param pts 
     */
    private void reorderPointsByDistance(Line2D hatchLine, ArrayList<Point2D>pts)
    {
        try
        {
            double minDistance=0,dist=0;
            int j=0,minIndex=-1;
            Map<Integer,Double>distances=new HashMap();
            ArrayList<Point2D>ptsOrdered=new ArrayList();
            Point2D origin=hatchLine.getP1();
            POINT2 pt0=new POINT2(origin.getX(),origin.getY());
            POINT2 pt1=null;
            //build the distances array
            int n=pts.size();
            //for(j=0;j<pts.size();j++)
            for(j=0;j<n;j++)
            {
                pt1=new POINT2(pts.get(j).getX(), pts.get(j).getY());
                dist=lineutility.CalcDistanceDouble(pt0, pt1);
                distances.put(j, dist);
            }
            while (distances.size()>0)
            {
                //initialize minDistance after an array element was removed
                //for(j=0;j<pts.size();j++)
                for(j=0;j<n;j++)
                {
                    if(distances.containsKey(j))
                    {
                        minIndex=j;
                        minDistance=distances.get(j);
                        break;
                    }
                }
                //loop through the remaining elements to find the next minimum distance
                //for(j=0;j<pts.size();j++)
                for(j=0;j<n;j++)
                {
                    if(distances.containsKey(j))
                    {
                        dist=distances.get(j);
                        if(dist<minDistance)
                        {
                            minDistance=dist;
                            minIndex=j;
                        }
                    }
                }                                    
                //add the next point to the array
                ptsOrdered.add(pts.get(minIndex));
                distances.remove(minIndex);
            }
            pts.clear();
            n=ptsOrdered.size();
            //for(j=0;j<ptsOrdered.size();j++)
            for(j=0;j<n;j++)
                pts.add(ptsOrdered.get(j));
        }
        catch(Exception exc)
        {
            ErrorLogger.LogException(_className, "reorderPointsByDistance",
                    new RendererException("Failed inside reorderPointsByDistance", exc));
        }
    }
    Rectangle2D getMBR(ArrayList<POINT2>polygon)
    {
        int j=0;
        double left=polygon.get(0).x;
        double top=polygon.get(0).y;
        double right=polygon.get(0).x;
        double bottom=polygon.get(0).y;
        int n=polygon.size();
        //for (j=1;j<polygon.size();j++)
        for (j=1;j<n;j++)
        {
            if(polygon.get(j).x<left)
                left=polygon.get(j).x;
            if(polygon.get(j).x>right)
                right=polygon.get(j).x;
            
            if(polygon.get(j).y<top)
                top=polygon.get(j).y;
            if(polygon.get(j).y>bottom)
                bottom=polygon.get(j).y;
        }
        return new Rectangle2D.Double(left,top,right-left,bottom-top);
    }
    boolean isVertical(Line2D edge)
    {
        if(edge.getX1()==edge.getX2())
            return true;
        else return false;
    }
    private void adjustVerticalLine(Line2D line)
    {
        Point2D linePt0=line.getP1();
        Point2D linePt1=line.getP1();
        if(isVertical(line))
        {
            double x=line.getX2()+1;
            double y=line.getY2();
            linePt1.setLocation(x, y);
            line.setLine(linePt0, linePt1);
        }             
    }
    /**
     * 
     * @param hatchLine the hatch line to intersect against the area points.
     * the thatch line is assumed to start outside the area (polygon) MBR
     * @return the GeneralPath which represents the intersection
     */
    private ArrayList<POINT2> getLineIntersectPoints(ArrayList<POINT2> polygon, Line2D hatchLine)
    {
        ArrayList<POINT2>pts=null;
        try
        {
            int j=0,k=0;
            Line2D segment=null;
            Point2D pt0=null,pt1=null;
            //no (exactly) vertical hatch lines
            adjustVerticalLine(hatchLine);
            ArrayList<Point2D>ptsPath=new ArrayList();
            double x=0,y=0;
            double m1=0,    //hatch line
                    m2=0,   //segment slope
                    b1=0,   //hatch line y intercept
                    b2=0;   //segment y intercept
            int n=polygon.size();
            //for(j=0;j<polygon.size()-1;j++)
            for(j=0;j<n-1;j++)
            {
                pt0=new Point2D.Double(polygon.get(j));
                pt1=new Point2D.Double(polygon.get(j+1));
                segment=new Line2D.Double(pt0,pt1);
                //no vertical segments
                adjustVerticalLine(segment);
                pt0=segment.getP1();
                pt1=segment.getP2();
                m1=(hatchLine.getY1()-hatchLine.getY2())/(hatchLine.getX1()-hatchLine.getX2());
                m2=(pt0.getY()-pt1.getY())/(pt0.getX()-pt1.getX());
                if( hatchLine.intersectsLine(segment) )
                {
                    //m1=(hatchLine.getY1()-hatchLine.getY2())/(hatchLine.getX1()-hatchLine.getX2());
                    //m2=(pt0.getY()-pt1.getY())/(pt0.getX()-pt1.getX());
                    if(m1==m2)
                    {
                        ptsPath.add(pt0);
                        ptsPath.add(pt1);
                    }
                    else    //slopes not equal
                    {
                        //add one intersection point
                        b1=hatchLine.getY1()-m1*hatchLine.getX1();
                        b2=segment.getY1()-m2*segment.getX1();
                        x=(b2-b1)/(m1-m2);  //cannot blow up
                        y=(m1*x+b1);
                        ptsPath.add(new Point2D.Double(x,y));
                    }
                }
            }
            //reorder ptsPath by distance from the hatch line origin
            reorderPointsByDistance(hatchLine,ptsPath);
            Point2D pt=null;
            pts=new ArrayList();
            n=ptsPath.size();
            //for(k=0;k<ptsPath.size();k++)
            for(k=0;k<n;k++)
            {
                pt=ptsPath.get(k);
                if(k%2==0)                
                {
                   pts.add(new POINT2(pt.getX(),pt.getY(),IPathIterator.SEG_MOVETO));
                }
                else
                {
                   pts.add(new POINT2(pt.getX(),pt.getY(),IPathIterator.SEG_LINETO));
                }

            }
            ptsPath.clear();
        }
        catch(Exception exc)
        {
            ErrorLogger.LogException(_className, "getLineIntersectPoints",
                    new RendererException("Failed inside getLineIntersectPoints", exc));
        }
        return pts;
    }
    /**
     * this is functionality for clsUtilityGE.buildHatchFillwhich calls hatchLineArea.intersect(shapeArea).
     * so it assumes that this._pts is the hatch lines so it is hatchLines.intersect(shape) where
     * shape is the polygon to be filled with hatch lines
     * @param area 
     */
    public void intersect(Area area)
    {
        try
        {
            //assume area is the polygon and "this" is the hatch line shape
            int j=0;
            ArrayList<POINT2>polygon=area.getPathIterator(null).getPoints();
            ArrayList<POINT2>hatchLines=this.getPathIterator(null).getPoints();
            //close the polygon
            if(polygon.get(0).x != polygon.get(polygon.size()-1).x || polygon.get(0).y != polygon.get(polygon.size()-1).y)
            {
                polygon.add(new POINT2(polygon.get(polygon.size()-1)));            
            }
            //GeneralPath gp=null;
            //GeneralPath masterGP=null;
            Line2D hatchLine=null;
            Rectangle2D rectHatch=null;
            Rectangle2D rectPoly=getMBR(polygon);
            ArrayList<POINT2> pts=new ArrayList();
            ArrayList<POINT2> ptsTemp=null;
            int n=hatchLines.size();
            //for(j=0;j<hatchLines.size()-1;j++)
            for(j=0;j<n-1;j++)
            {
                hatchLine=new Line2D.Double(hatchLines.get(j).x,hatchLines.get(j).y,hatchLines.get(j+1).x,hatchLines.get(j+1).y);
                rectHatch=hatchLine.getBounds2D();
                if(rectHatch.intersects(rectPoly)==false)
                    continue;

                ptsTemp=getLineIntersectPoints(polygon,hatchLine);
                if(ptsTemp != null)
                    pts.addAll(ptsTemp);
            }   
            POINT2 pt=null;
            //area.getPathIterator(null).reset();
            //area.getPathIterator(null).getPoints().clear();
            //this._pts.clear();            
            this.getPathIterator(null).getPoints().clear();
            //area._pts.clear();
            n=pts.size();
            //for(j=0;j<pts.size();j++)
            for(j=0;j<n;j++)
            {
                pt=pts.get(j);
                switch(pt.style)
                {
                    case IPathIterator.SEG_MOVETO:
                        moveTo(pt.x,pt.y);
                        break;
                    case IPathIterator.SEG_LINETO:
                        lineTo(pt.x,pt.y);
                        break;
                    default:
                        break;
                }
            }      
            this.getPathIterator(null).reset();
        }
        catch(Exception exc)
        {
            ErrorLogger.LogException(_className, "intersect",
                    new RendererException("Failed inside intersect", exc));
        }
    }
}
