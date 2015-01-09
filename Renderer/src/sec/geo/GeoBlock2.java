package sec.geo;
import org.gavaghan.geodesy.Ellipsoid;
import org.gavaghan.geodesy.GeodeticCalculator;
import armyc2.c2sd.JavaLineArray.POINT2;
import org.gavaghan.geodesy.GeodeticCurve;
import org.gavaghan.geodesy.GlobalCoordinates;
import java.util.ArrayList;
import armyc2.c2sd.graphics2d.GeneralPath;
import armyc2.c2sd.graphics2d.AffineTransform;
import armyc2.c2sd.graphics2d.PathIterator;
public class GeoBlock2 /*extends GeoPath*/ {
	protected static final Ellipsoid REFERENCE_ELLIPSOID = Ellipsoid.WGS84;
	
	private GeneralPath path;
	private ArrayList<GeoPoint> toPoints;
	private double maxDistanceMeters;
	private double flatnessDistanceMeters;
	private int limit;
	
	protected final GeodeticCalculator geoCalc;
	public GeoBlock2(GeoPoint p1, GeoPoint p2, double leftWidthMeters, double rightWidthMeters, double maxDistanceMeters,
			double flatnessDistanceMeters, int limit) {
		//super(maxDistanceMeters, flatnessDistanceMeters, limit);
		path = new GeneralPath();
		toPoints = new ArrayList<GeoPoint>();
		geoCalc = new GeodeticCalculator();
		this.maxDistanceMeters = maxDistanceMeters;
		//this.flatnessDistanceMeters = flatnessDistanceMeters;
		//this.limit = limit;
		
                GlobalCoordinates c1 = toGlobalCoord(p1);
		GlobalCoordinates c2 = toGlobalCoord(p2);                                
		GeodeticCurve curve = geoCalc.calculateGeodeticCurve(REFERENCE_ELLIPSOID, c1, c2);                
		double a1 = curve.getAzimuth();
		double a2 = curve.getReverseAzimuth();             
		double leftRadius = leftWidthMeters;                
                double rightRadius = rightWidthMeters;                
                //diagnostic to prevent error in calculate global coords if points are identical
                if(p1.x==p2.x && p1.y==p2.y)
                    return;
                //end section
		GlobalCoordinates c = geoCalc.calculateEndingGlobalCoordinates(REFERENCE_ELLIPSOID, c1, a1 - 90, leftRadius);                
                c = geoCalc.calculateEndingGlobalCoordinates(REFERENCE_ELLIPSOID, c1, a1 - 90, leftRadius);
                moveToLatLong(c.getLongitude(), c.getLatitude());
                c = geoCalc.calculateEndingGlobalCoordinates(REFERENCE_ELLIPSOID, c2, a2 + 90, leftRadius);                
                lineToLatLong(c.getLongitude(), c.getLatitude());
                c = geoCalc.calculateEndingGlobalCoordinates(REFERENCE_ELLIPSOID, c2, a2 - 90, rightRadius);                
                lineToLatLong(c.getLongitude(), c.getLatitude());
                c = geoCalc.calculateEndingGlobalCoordinates(REFERENCE_ELLIPSOID, c1, a1 + 90, rightRadius);                
                lineToLatLong(c.getLongitude(), c.getLatitude());
                closePath();  
 	}
	public void moveTo(GeoPoint point) {
		path.moveTo(point.x, point.y);
		toPoints.add(point);
	}
	
	public final void moveToLatLong(double longitudeDegrees, double latitudeDegrees) {
		moveTo(new GeoPoint(longitudeDegrees, latitudeDegrees));
	}
	
	public void lineTo(GeoPoint point) {
		GeneralPath newPath = new GeneralPath();
		
		// Move to the initial point
		GeoPoint lastPoint = new GeoPoint();
		if (toPoints.size() > 0) {
			lastPoint = toPoints.get(toPoints.size() - 1);
			newPath.moveTo(lastPoint.x, lastPoint.y);
		}
		
		// Calculate the curve to the new point
		GlobalCoordinates start = toGlobalCoord(lastPoint);
		GlobalCoordinates end = toGlobalCoord(point);
		GeodeticCurve curve = geoCalc.calculateGeodeticCurve(REFERENCE_ELLIPSOID, start, end);
		
		// Generate points along the curve, adding them to the new path
		double distance = maxDistanceMeters;
		while (distance < curve.getEllipsoidalDistance()) {
			GlobalCoordinates c = geoCalc.calculateEndingGlobalCoordinates(REFERENCE_ELLIPSOID, start, curve
					.getAzimuth(), distance);
			newPath.lineTo(c.getLongitude(), c.getLatitude());
			distance += maxDistanceMeters;
		}
		newPath.lineTo(point.x, point.y);
		
		// Append the new path to the existing path
		path.append(newPath, true);                
		toPoints.add(point);
                simplify();
              
	}
	
	public final void lineToLatLong(double longitudeDegrees, double latitudeDegrees) {
		lineTo(new GeoPoint(longitudeDegrees, latitudeDegrees));
	}
	
	
	public ArrayList<GeoPoint> getToPoints() {
		          return toPoints;
	}
	public final void closePath() {
            
                
		if (toPoints.size() > 0 && !toPoints.get(0).equals(toPoints.get(toPoints.size() - 1))) {                        
			lineTo(toPoints.get(0));                        
		}
	}
	
	
//	@Override
	public PathIterator getPathIterator(AffineTransform at, double flatness) {
		//return path.getPathIterator(at, flatness);
		return path.getPathIterator(at);
	}

        
        @Override
        public String toString() {
            return toPoints.toString();
        }
	
	protected final GlobalCoordinates toGlobalCoord(GeoPoint point) {
		return new GlobalCoordinates(point.getLatitude(), point.getLongitude());
	}
	public void simplify()
        {
            PathIterator pi=path.getPathIterator(null);
            ArrayList<POINT2>pts=pi.getPoints();
            ArrayList<POINT2>newPts=new ArrayList();
            int j=0;
            int style=-1,lastStyle=-1,nextstyle=-1;
            POINT2 currentPt=null, lastPt=null;
            int n=pts.size();
            //for(j=0;j<pts.size();j++)
            for(j=0;j<n;j++)
            {
                style=pts.get(j).style;
                currentPt=pts.get(j);
                if(j>0)
                {
                    lastStyle=pts.get(j-1).style;                
                    lastPt=pts.get(j-1);
                }
                if(lastStyle==PathIterator.SEG_LINETO && style==PathIterator.SEG_MOVETO)
                {
                    //if the last move as lineto and the current move is moveto then
                    //don't add the current point if the last point is equal
                    if(currentPt.x==lastPt.x && currentPt.y==lastPt.y)
                        continue;
                }
                newPts.add(currentPt);    
            }
            pi.setPathIterator(newPts);
        }
}
