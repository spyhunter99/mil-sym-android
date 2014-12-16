package sec.geo.shape;

import sec.sun.awt.geom.Area;
import sec.geo.kml.KmlOptions.AltitudeMode;
import sec.geo.GeoBlock;
import sec.geo.GeoEllipse;
import sec.geo.GeoPoint;
import sec.geo.ShapeObject;
import java.util.ArrayList;

public class Orbit /* extends APath */ { //APath extends AExtrusion

    private double minAltitudeMeters;
    private double maxAltitudeMeters;
    protected double maxDistanceMeters;
    protected double flatnessDistanceMeters;
    protected AltitudeMode altitudeMode;
    private Area shape;
    protected int limit;
    private double widthMeters;
    protected final ArrayList<GeoPoint> points;

    public Orbit() {
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

    public void setWidth(double widthMeters) {
        this.widthMeters = widthMeters;
        shapeChanged();
    }

    //@Override
    protected Area createShape() {
        Area orbit = new Area();
        GeoPoint previousPoint = null;

        for (GeoPoint point : points) {
            GeoEllipse ellipse = new GeoEllipse(point, widthMeters, widthMeters, maxDistanceMeters,
                    flatnessDistanceMeters, limit);
            ShapeObject el = new ShapeObject(ellipse);
			//orbit.add(new Area(ellipse));
            //orbit.add(new Area(el));
            Area rhs = new Area(el);
            orbit.add(rhs);

            if (previousPoint != null) {
                GeoBlock block = new GeoBlock(previousPoint, point, widthMeters, maxDistanceMeters,
                        flatnessDistanceMeters, limit);
                ShapeObject bl = new ShapeObject(block);
                Area rhs2 = new Area(bl);
                //orbit.add(new Area(bl));
                orbit.add(rhs2);
            }
            previousPoint = point;
        }
        return orbit;
    }

    public Area getShape() {
        if (shape == null) {
            shape = createShape();
        }
        return shape;
    }

    protected void shapeChanged() {
        shape = null;
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
