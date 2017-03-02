package sec.geo.shape;

import sec.geo.ShapeObject;
import java.util.ArrayList;
import sec.geo.GeoPoint;
import sec.geo.GeoPath;
import sec.geo.kml.KmlOptions.AltitudeMode;

public class Polygon /*extends APath*/ {    //APath extends AExtrusion

    private double minAltitudeMeters;
    private double maxAltitudeMeters;
    protected double maxDistanceMeters;
    protected double flatnessDistanceMeters;
    protected AltitudeMode altitudeMode;
    protected int limit;
    private ShapeObject shape;
    //@Override
    protected final ArrayList<GeoPoint> points;

    public Polygon() {
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
        int n=points.size();
        //for (int i = 0; i < points.size(); i++) 
        for (int i = 0; i < n; i++) 
        {
            if (i > 0) {
                path.lineTo(points.get(i));
            } else {
                path.moveTo(points.get(i));
            }
        }
        path.closePath();
        return new ShapeObject(path);
    }

    protected void shapeChanged() {
        shape = null;
    }

    public ShapeObject getShape() {
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
