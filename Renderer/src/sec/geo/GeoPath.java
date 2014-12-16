package sec.geo;

import armyc2.c2sd.graphics2d.*;
import java.util.ArrayList;

import org.gavaghan.geodesy.Angle;
import org.gavaghan.geodesy.Ellipsoid;
import org.gavaghan.geodesy.GeodeticCalculator;
import org.gavaghan.geodesy.GeodeticCurve;
import org.gavaghan.geodesy.GlobalCoordinates;

public class GeoPath /* implements Shape */ {
	protected static final Ellipsoid REFERENCE_ELLIPSOID = Ellipsoid.WGS84;
	
	private GeneralPath path;
	private ArrayList<GeoPoint> toPoints;
	private double maxDistanceMeters;
	private double flatnessDistanceMeters;
	private int limit;
	
	protected final GeodeticCalculator geoCalc;
	
	public GeoPath() {
		this(100000, 1, 4);
	}
	
	public GeoPath(double maxDistanceMeters, double flatnessDistanceMeters, int limit) {
		path = new GeneralPath();
		toPoints = new ArrayList<GeoPoint>();
		geoCalc = new GeodeticCalculator();
		this.maxDistanceMeters = maxDistanceMeters;
		this.flatnessDistanceMeters = flatnessDistanceMeters;
		this.limit = limit;
	}
	
	public void moveTo(GeoPoint point) {
		path.moveTo(point.x, point.y);
		toPoints.add(point);
	}
	
	public void moveToLatLong(double longitudeDegrees, double latitudeDegrees) {
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
                
	}
	
	public void lineToLatLong(double longitudeDegrees, double latitudeDegrees) {
		lineTo(new GeoPoint(longitudeDegrees, latitudeDegrees));
	}
	
	public void arcTo(GeoPoint pivot, double widthMeters, double heightMeters, double leftAzimuthDegrees,
			double rightAzimuthDegrees) {
		GeneralPath newPath = new GeneralPath();
                Arc2D arc;
		                                
                if (leftAzimuthDegrees > rightAzimuthDegrees) {
                    arc = new Arc2D(-widthMeters / 2, -heightMeters / 2, widthMeters, heightMeters,
				leftAzimuthDegrees - 90, Math.abs((360 - leftAzimuthDegrees) + rightAzimuthDegrees), Arc2D.OPEN);
                } else {
                    arc = new Arc2D(-widthMeters / 2, -heightMeters / 2, widthMeters, heightMeters,
				leftAzimuthDegrees - 90, Math.abs(leftAzimuthDegrees - rightAzimuthDegrees), Arc2D.OPEN);
                }
		
		GeoPoint point = null;
		if (pivot != null) {
			FlatteningPathIterator it = new FlatteningPathIterator(arc.getPathIterator(null), flatnessDistanceMeters, limit);
			while (!it.isDone()) {
				// Add a point to the list for each segment flattened from the curve
				double[] strokePoints = new double[6];
				int type = it.currentSegment(strokePoints);
				double x = strokePoints[0];
				double y = strokePoints[1];
				double azimuth = Angle.toDegrees(Math.atan2(x, y));
				GlobalCoordinates coord = new GlobalCoordinates(pivot.getLatitude(), pivot.getLongitude());
				GlobalCoordinates c = geoCalc.calculateEndingGlobalCoordinates(REFERENCE_ELLIPSOID, coord, azimuth,
						//new Point2D(0,0).distance(x, y));
						Point2D.distance(0, 0, x, y));
				switch (type) {
					case PathIterator.SEG_MOVETO:
						newPath.moveTo(c.getLongitude(), c.getLatitude());
						GeoPoint startPoint = new GeoPoint(c.getLongitude(), c.getLatitude());
						if (toPoints.size() > 0 && !startPoint.equals(toPoints.get(toPoints.size() - 1))) {
							lineTo(startPoint);
						}
						break;
					case PathIterator.SEG_LINETO:
						newPath.lineTo(c.getLongitude(), c.getLatitude());
						point = new GeoPoint(c.getLongitude(), c.getLatitude());
						break;
				}
				it.next();
			}
		}
		
		path.append(newPath, true);
		toPoints.add(point);
	}
	
	public ArrayList<GeoPoint> getToPoints() {
		return toPoints;
	}
	
	public void closePath() {
            
                
		if (toPoints.size() > 0 && !toPoints.get(0).equals(toPoints.get(toPoints.size() - 1))) {                        
			lineTo(toPoints.get(0));                        
		}
	}
	
//	@Override
//	public boolean contains(Point2D p) {
//		return path.contains(p);
//	}
//	
//	@Override
//	public boolean contains(Rectangle2D r) {
//		return path.contains(r);
//	}
//	
//	@Override
//	public boolean contains(double x, double y) {
//		return path.contains(x, y);
//	}
//	
//	@Override
//	public boolean contains(double x, double y, double w, double h) {
//		return contains(x, y, w, h);
//	}
//	
//	@Override
//	public Rectangle getBounds() {
//		return path.getBounds();
//	}
//	
//	@Override
//	public Rectangle2D getBounds2D() {
//		return path.getBounds2D();
//	}
	
//	@Override
	public PathIterator getPathIterator(AffineTransform at) {
		return path.getPathIterator(at);
	}
	
//	@Override
//	public PathIterator getPathIterator(AffineTransform at, double flatness) {
//		return path.getPathIterator(at, flatness);
//	}
//	
//	@Override
//	public boolean intersects(Rectangle2D r) {
//		return path.intersects(r);
//	}
//	
//	@Override
//	public boolean intersects(double x, double y, double w, double h) {
//		return path.intersects(x, y, w, h);
//	}
        
        @Override
        public String toString() {
            return toPoints.toString();
        }
	
	protected GlobalCoordinates toGlobalCoord(GeoPoint point) {
		return new GlobalCoordinates(point.getLatitude(), point.getLongitude());
	}
}
