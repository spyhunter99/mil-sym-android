package sec.geo.shape;

import sec.geo.GeoPoint;
import sec.geo.ShapeObject;
import sec.geo.GeoEllipse;
import sec.geo.kml.KmlOptions.AltitudeMode;

public class Circle /*extends APivot*/ {    //APivot extends AExtrusion

    protected GeoPoint pivot;
    protected double radiusMeters;
    private ShapeObject shape;
    protected double maxDistanceMeters;
    protected double flatnessDistanceMeters;
    protected AltitudeMode altitudeMode;
    private double minAltitudeMeters;
    private double maxAltitudeMeters;
    protected int limit;

    public Circle() {
        pivot = new GeoPoint();
        maxDistanceMeters = 100000;
        flatnessDistanceMeters = 1;
        limit = 4;
    }

    public ShapeObject getShape() {
        if (shape == null) {
            shape = createShape();
        }
        return shape;
    }

    //@Override
    public void setRadius(double radiusMeters) {
        this.radiusMeters = radiusMeters;
        shapeChanged();
    }

    //@Override
    public void setPivot(GeoPoint pivot) {
        this.pivot = pivot;
        shapeChanged();
    }

    //@Override
    protected ShapeObject createShape() {
        GeoEllipse e = new GeoEllipse(pivot, radiusMeters * 2, radiusMeters * 2, maxDistanceMeters,
                flatnessDistanceMeters, limit);
        return new ShapeObject(e);
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
