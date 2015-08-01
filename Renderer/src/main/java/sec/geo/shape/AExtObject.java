/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sec.geo.shape;

import sec.geo.*;
import sec.geo.kml.KmlOptions.AltitudeMode;
import armyc2.c2sd.graphics2d.AffineTransform;
import java.util.ArrayList;

/**
 *
 * @author Michael Deutch
 */
public class AExtObject {
    public static final int CAKE = 0;
    public static final int LINE = 1;
    public static final int ORBIT = 2;
    public static final int POINT = 3;
    public static final int CIRCLE = 4;
    public static final int RADARC = 5;
    public static final int POLYARC = 6;
    public static final int POLYGON = 7;
    public static final int ROUTE = 8;
    public static final int TRACK = 9;
    private int type = -1;
    private Orbit orbit = null;
    private Cake cake = null;
    private Circle circle = null;
    private Point point = null;
    private Radarc radarc = null;
    private Polyarc polyarc = null;
    private Polygon polygon = null;
    private Line line = null;
    private Route route = null;
    private Track track = null;

    public AExtObject(Object obj) {
        if (obj instanceof Integer) {
            this.type = (Integer) obj;
            switch (type) {
                case LINE:
                    line = new Line();
                    break;
                case ORBIT:
                    orbit = new Orbit();
                    break;
                case ROUTE:
                    route = new Route();
                    break;
                case TRACK:
                    track = new Track();
                    break;
                case RADARC:
                    radarc = new Radarc();
                    break;
                case CAKE:
                    cake = new Cake();
                    break;
                default:
                    break;
            }
        } //we do not want new objects, this is a pass-thru
        //because the client is subsequently using the object it's passing
        else if (obj instanceof Line) {
            line = (Line) obj;
            type = LINE;
        } else if (obj instanceof Route) {
            route = (Route) obj;
            type = ROUTE;
        } else if (obj instanceof Polygon) {
            polygon = (Polygon) obj;
            type = POLYGON;
        } else if (obj instanceof Radarc) {
            radarc = (Radarc) obj;
            type = RADARC;
        } else if (obj instanceof Polyarc) {
            polyarc = (Polyarc) obj;
            type = POLYARC;
        } else if (obj instanceof Orbit) {
            orbit = (Orbit) obj;
            type = ORBIT;
        } else if (obj instanceof Cake) {
            cake = (Cake) obj;
            type = CAKE;
        } else if (obj instanceof Circle) {
            circle = (Circle) obj;
            type = CIRCLE;
        } else if (obj instanceof Point) {
            point = (Point) obj;
            type = POINT;
        } else if (obj instanceof Track) {
            track = (Track) obj;
            type = TRACK;
        }

    }

    public void setMaxDistance(double maxDistanceMeters) {
        //this.maxDistanceMeters = maxDistanceMeters;
        //shapeChanged();
        switch (this.type) {
            case ORBIT:
                orbit.setMaxDistance(maxDistanceMeters);
                break;
            case RADARC:
                radarc.setMaxDistance(maxDistanceMeters);
                break;
            case POLYARC:
                polyarc.setMaxDistance(maxDistanceMeters);
                break;
            case POLYGON:
                polygon.setMaxDistance(maxDistanceMeters);
                break;
            case LINE:
                line.setMaxDistance(maxDistanceMeters);
                break;
            case CIRCLE:
                circle.setMaxDistance(maxDistanceMeters);
                break;
            case ROUTE:
                route.setMaxDistance(maxDistanceMeters);
                break;
            default:
                break;
        }
    }

    public void addPoint(GeoPoint point) {
        switch (type) {
            case LINE:
                line.addPoint(point);
                break;
            case RADARC:
                //radarc.addPoint(point);
                break;
            case ORBIT:
                orbit.addPoint(point);
                break;
            case POLYARC:
                polyarc.addPoint(point);
                break;
            case POLYGON:
                polygon.addPoint(point);
                break;
            case ROUTE:
                route.addPoint(point);
                break;
            default:
                break;
        }
    }

    public void setFlatness(double flatnessDistanceMeters) {
        switch (type) {
            case LINE:
                line.flatnessDistanceMeters = flatnessDistanceMeters;
                break;
            case CIRCLE:
                circle.flatnessDistanceMeters = flatnessDistanceMeters;
                break;
            case ORBIT:
                orbit.flatnessDistanceMeters = flatnessDistanceMeters;
                break;
            case RADARC:
                radarc.flatnessDistanceMeters = flatnessDistanceMeters;
                break;
            case POLYARC:
                polyarc.flatnessDistanceMeters = flatnessDistanceMeters;
                break;
            case POLYGON:
                polygon.flatnessDistanceMeters = flatnessDistanceMeters;
                break;
            case ROUTE:
                route.flatnessDistanceMeters = flatnessDistanceMeters;
                break;
            default:
                break;
        }
        shapeChanged();
    }

    public void setLimit(int limit) {
        //this.limit = limit;
        //shapeChanged();
        switch (type) {
            case LINE:
                line.limit = limit;
                break;
            case CIRCLE:
                circle.limit = limit;
                break;
            case ORBIT:
                orbit.limit = limit;
                break;
            case RADARC:
                radarc.limit = limit;
                break;
            case POLYARC:
                polyarc.limit = limit;
                break;
            case POLYGON:
                polygon.limit = limit;
                break;
            case ROUTE:
                route.limit = limit;
                break;
            default:
                break;
        }
        shapeChanged();
    }

    public double getMinAltitude() {
        //return minAltitudeMeters;
        switch (type) {
            case LINE:
                return line.getMinAltitude();
            case CIRCLE:
                return circle.getMinAltitude();
            case ORBIT:
                return orbit.getMinAltitude();
            case POLYARC:
                return polyarc.getMinAltitude();
            case POLYGON:
                return polygon.getMinAltitude();
            case ROUTE:
                return route.getMinAltitude();
            case RADARC:
                return radarc.getMinAltitude();
            default:
                break;
        }
        return -1;
    }

    public double getMaxAltitude() {
        //return minAltitudeMeters;
        switch (type) {
            case LINE:
                return line.getMaxAltitude();
            case CIRCLE:
                return circle.getMaxAltitude();
            case ORBIT:
                return orbit.getMaxAltitude();
            case POLYARC:
                return polyarc.getMaxAltitude();
            case POLYGON:
                return polygon.getMaxAltitude();
            case ROUTE:
                return route.getMaxAltitude();
            case RADARC:
                return radarc.getMaxAltitude();
            default:
                break;
        }
        return -1;
    }

    public AltitudeMode getAltitudeMode() {
        switch (type) {
            case LINE:
                return line.getAltitudeMode();
            case CIRCLE:
                return circle.getAltitudeMode();
            case ORBIT:
                return orbit.getAltitudeMode();
            case POLYARC:
                return polyarc.getAltitudeMode();
            case POLYGON:
                return polygon.getAltitudeMode();
            case ROUTE:
                return route.getAltitudeMode();
            case RADARC:
                return radarc.getAltitudeMode();
            default:
                break;
        }
        return AltitudeMode.ABSOLUTE;
    }

    public ArrayList getElements() {
        if (track != null) {
            return this.track.elements;
        } else if (cake != null) {
            return cake.getElements();
        } else {
            return null;
        }
    }

    public Object getPathIterator(AffineTransform at) {
        switch (type) {
            case LINE:
                return line.getShape().getPathIterator(at);
            case ORBIT:
                return orbit.getShape().getPathIterator(at);
            case POLYGON:
                return polygon.getShape().getPathIterator(at);
            case POLYARC:
                return polyarc.getShape().getPathIterator(at);
            case CIRCLE:
                return circle.getShape().getPathIterator(at);
            case RADARC:
                return radarc.getShape().getPathIterator(at);
            case ROUTE:
                return route.getShape().getPathIterator(at);
            default:
                return null;
        }
    }

    private void shapeChanged() {
        switch (type) {
            case POLYGON:
                polygon.shapeChanged();
                break;
            case ORBIT:
                orbit.shapeChanged();
                break;
            case ROUTE:
                route.shapeChanged();
                break;
            case RADARC:
                radarc.shapeChanged();
                break;
            case POLYARC:
                polyarc.shapeChanged();
                break;
            default:
                break;
        }
    }
}
