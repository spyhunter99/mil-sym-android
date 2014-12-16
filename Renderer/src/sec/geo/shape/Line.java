package sec.geo.shape;

import sec.geo.ShapeObject;
import sec.geo.GeoPath;
import sec.geo.GeoPoint;
import java.util.ArrayList;
import sec.geo.kml.KmlOptions.AltitudeMode;

public class Line /*extends APath*/ {   //APath extends AExtrusion

    private double minAltitudeMeters;
    private double maxAltitudeMeters;
    private ShapeObject shape;
    protected double maxDistanceMeters;
    protected double flatnessDistanceMeters;

    protected AltitudeMode altitudeMode;
    protected int limit;
    protected final ArrayList<GeoPoint> points;

    public Line() {
        points = new ArrayList<GeoPoint>();
        maxDistanceMeters = 100000;
        flatnessDistanceMeters = 1;
        limit = 4;
    }

    public void addPoint(GeoPoint point) {
        points.add(point);
        shapeChanged();
    }

    public void addPoints(ArrayList<GeoPoint> points) {
        this.points.addAll(points);
        shapeChanged();
    }

    protected ShapeObject createShape() {
        GeoPath path = new GeoPath(maxDistanceMeters, flatnessDistanceMeters, limit);
        int n = points.size();
        //for (int i = 0; i < points.size(); i++) 
        for (int i = 0; i < n; i++) {
            if (i > 0) {
                path.lineTo(points.get(i));
            } else {
                path.moveTo(points.get(i));
            }
        }
        return new ShapeObject(path);
    }

    public ShapeObject getShape() {
        if (shape == null) {
            shape = createShape();
        }
        return shape;
    }

    protected void shapeChanged() {
        shape = null;
    }

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
