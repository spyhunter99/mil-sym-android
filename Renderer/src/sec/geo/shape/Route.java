package sec.geo.shape;

import sec.sun.awt.geom.Area;
import sec.geo.GeoBlock2;
import sec.geo.GeoPoint;
import java.util.ArrayList;
import sec.geo.ShapeObject;
import sec.geo.kml.KmlOptions.AltitudeMode;

public class Route /*extends APath*/ {

    private double minAltitudeMeters;
    private double maxAltitudeMeters;
    protected double maxDistanceMeters;
    protected double flatnessDistanceMeters;

    protected AltitudeMode altitudeMode;
    protected int limit;
    private double leftWidthMeters;
    private double rightWidthMeters;
    private Area shape;
    protected final ArrayList<GeoPoint> points;

    public Route() {
        maxDistanceMeters = 100000;
        flatnessDistanceMeters = 1;
        limit = 4;
        points = new ArrayList<GeoPoint>();
    }

    public void addPoint(GeoPoint point) {
        points.add(point);
        shapeChanged();
    }

    public void addPoints(ArrayList<GeoPoint> points) {
        this.points.addAll(points);
        shapeChanged();
    }

    public void setLeftWidth(double widthMeters) {
        this.leftWidthMeters = widthMeters;
        shapeChanged();
    }

    public void setRightWidth(double widthMeters) {
        this.rightWidthMeters = widthMeters;
        shapeChanged();
    }

    //@Override
    protected Area createShape() {
        Area route = new Area();
        GeoPoint previousPoint = null;
        int n = points.size();
        //for (int i = 0; i < points.size(); i++) 
        for (int i = 0; i < n; i++) {

            GeoPoint point = points.get(i);

            if (previousPoint != null) {

                // Skip if points are the same -- doesn't take into account height difference
                if (previousPoint.equals(point)) {
                    continue;
                }

                // Draw rectangle connection
                GeoBlock2 block = new GeoBlock2(previousPoint, point, this.leftWidthMeters, this.rightWidthMeters, maxDistanceMeters,
                        flatnessDistanceMeters, limit);
                Area area = new Area(new ShapeObject(block));
                route.add(area);

            }
            previousPoint = point;
        }
        return route;
    }

    protected void shapeChanged() {
        shape = null;
    }

    public Area getShape() {
        if (shape == null) {
            shape = createShape();
        }
        return shape;
    }

	//protected abstract Shape createShape();
    public double getMinAltitude() {
        return minAltitudeMeters;
    }

    public void setMinAltitude(double minAltitudeMeters) {
        this.minAltitudeMeters = minAltitudeMeters;
        shapeChanged();
    }

    public double getMaxAltitude() {
        return maxAltitudeMeters;
    }

    public void setMaxAltitude(double maxAltitudeMeters) {
        this.maxAltitudeMeters = maxAltitudeMeters;
        shapeChanged();
    }

    public void setMaxDistance(double maxDistanceMeters) {
        this.maxDistanceMeters = maxDistanceMeters;
        shapeChanged();
    }

    public void setFlatness(double flatnessDistanceMeters) {
        this.flatnessDistanceMeters = flatnessDistanceMeters;
        shapeChanged();
    }

    public void setLimit(int limit) {
        this.limit = limit;
        shapeChanged();
    }

    public AltitudeMode getAltitudeMode() {
        return altitudeMode;
    }

    public void setAltitudeMode(AltitudeMode altitudeMode) {
        this.altitudeMode = altitudeMode;
    }
}
