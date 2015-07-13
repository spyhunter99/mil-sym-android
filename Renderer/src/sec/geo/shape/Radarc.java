package sec.geo.shape;

import sec.sun.awt.geom.Area;
import sec.geo.GeoArc;
import sec.geo.GeoEllipse;
import sec.geo.ShapeObject;
import sec.geo.GeoPoint;
import sec.geo.kml.KmlOptions.AltitudeMode;

public class Radarc/* extends AArc */ {  //AArc extends APivot which extends AExtrusion

    private double minAltitudeMeters;
    private double maxAltitudeMeters;
    private Area shape;
    protected double maxDistanceMeters;
    protected double flatnessDistanceMeters;
    private double minRadiusMeters;
    protected double leftAzimuthDegrees, rightAzimuthDegrees;
    protected GeoPoint pivot;
    protected double radiusMeters;
    protected AltitudeMode altitudeMode;
    protected int limit;

    public Radarc() {
        maxDistanceMeters = 100000;
        flatnessDistanceMeters = 1;
        limit = 4;  //was 4
    }

    public void setRightAzimuthDegrees(double rightAzimuthDegrees) {
        this.rightAzimuthDegrees = rightAzimuthDegrees;
        shapeChanged();
    }

    public void setLeftAzimuthDegrees(double leftAzimuthDegrees) {
        this.leftAzimuthDegrees = leftAzimuthDegrees;
        shapeChanged();
    }

    public void setMinRadius(double minRadiusMeters) {
        this.minRadiusMeters = minRadiusMeters;
        shapeChanged();
    }

    protected void shapeChanged() {
        shape = null;
    }

    //@Override
    protected Area createShape() {
        GeoArc arc = new GeoArc(pivot, radiusMeters * 2, radiusMeters * 2, leftAzimuthDegrees, rightAzimuthDegrees,
                maxDistanceMeters, flatnessDistanceMeters, limit);
        ShapeObject arcObj = new ShapeObject(arc);
        //Area shape = new Area(arc);
        Area shape1 = new Area(arcObj);
        GeoEllipse ellipse = new GeoEllipse(pivot, minRadiusMeters * 2, minRadiusMeters * 2, maxDistanceMeters,
                flatnessDistanceMeters, limit);

        ShapeObject ellipseObj = new ShapeObject(ellipse);

        shape1.subtract(new Area(ellipseObj));
        return shape1;
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

    public void setRadius(double radiusMeters) {
        this.radiusMeters = radiusMeters;
        shapeChanged();
    }

    public void setPivot(GeoPoint pivot) {
        this.pivot = pivot;
        shapeChanged();
    }

    public Area getShape() {
        if (shape == null) {
            shape = createShape();
        }
        return shape;
    }

}
