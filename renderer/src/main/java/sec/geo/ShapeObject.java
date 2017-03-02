/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sec.geo;

import armyc2.c2sd.graphics2d.*;
import org.gavaghan.geodesy.GeodeticCalculator;

/**
 *
 * @author Michael Deutch
 */
public class ShapeObject {

    public static final int GEOARC = 0;
    public static final int GEOBLOCK = 1;
    public static final int GEOBLOCK2 = 2;
    public static final int GEOELLIPSE = 3;
    public static final int GEOPATH = 4;
    public static final int GEOPOINT = 5;
    public static final int AREA = 6;
    private int type = -1;
    private GeoEllipse geoEllipse = null;
    private GeoPath geoPath = null;
    private GeoArc geoArc = null;
    private GeoBlock geoBlock = null;
    private GeoBlock2 geoBlock2 = null;
    private GeoPoint geoPoint = null;
    private Area area = null;
    protected final GeodeticCalculator geoCalc = null;

    public ShapeObject(Object obj) {
        if (obj instanceof GeoArc) {
            geoArc = (GeoArc) obj;
            type = GEOARC;
        } else if (obj instanceof GeoPath) {
            geoPath = (GeoPath) obj;
            type = GEOPATH;
        } else if (obj instanceof GeoEllipse) {
            geoEllipse = (GeoEllipse) obj;
            type = GEOELLIPSE;
        } else if (obj instanceof GeoBlock) {
            geoBlock = (GeoBlock) obj;
            type = GEOBLOCK;
        } else if (obj instanceof GeoBlock2) {
            geoBlock2 = (GeoBlock2) obj;
            type = GEOBLOCK2;
        } else if (obj instanceof GeoPoint) {
            geoPoint = (GeoPoint) obj;
            type = GEOPOINT;
        } else if (obj instanceof Area) {
            area = (Area) obj;
            type = AREA;
        }
    }

    public void arcTo(GeoPoint pivot, double widthMeters, double heightMeters, double leftAzimuthDegrees,
            double rightAzimuthDegrees) {
        switch (type) {
            case GEOELLIPSE:
                geoEllipse.arcTo(pivot, widthMeters, heightMeters, leftAzimuthDegrees, rightAzimuthDegrees);
                break;
            case GEOARC:
                geoArc.arcTo(pivot, widthMeters, heightMeters, leftAzimuthDegrees, rightAzimuthDegrees);
                break;
            case GEOPATH:
                geoPath.arcTo(pivot, widthMeters, heightMeters, leftAzimuthDegrees, rightAzimuthDegrees);
                break;
            default:
                break;
        }

    }

    public PathIterator getPathIterator(AffineTransform at) {
        switch (type) {
            case GEOELLIPSE:
                return geoEllipse.getPathIterator(at);
            case GEOPATH:
                return geoPath.getPathIterator(at);
            case GEOBLOCK:
                return geoBlock.getPathIterator(at);
            case GEOBLOCK2:
                return geoBlock2.getPathIterator(at, 0);
            case GEOARC:
                return geoArc.getPathIterator(at);
            default:
                return null;
        }
    }

    public void moveTo(GeoPoint point) {
        switch (type) {
            case GEOELLIPSE:
                //geoEllipse.moveTo(point);
                break;
            case GEOPOINT:
                //geoEllipse.moveTo(point);
                break;
            case GEOARC:
                geoArc.moveTo(point);
                break;
            case GEOPATH:
                geoPath.moveTo(point);
                break;
            case GEOBLOCK:
                geoBlock.moveTo(point);
                break;
            case GEOBLOCK2:
                geoBlock2.moveTo(point);
                break;
            default:
                break;
        }
    }

    public void moveToLatLong(double longitudeDegrees, double latitudeDegrees) {
        switch (type) {
            case GEOELLIPSE:
                //geoEllipse.moveTo(point);
                break;
            case GEOPOINT:
                //geoPoint.moveTo(point);
                break;
            case GEOARC:
                geoArc.moveToLatLong(longitudeDegrees, latitudeDegrees);
                break;
            case GEOPATH:
                geoPath.moveToLatLong(longitudeDegrees, latitudeDegrees);
                break;
            case GEOBLOCK:
                geoBlock.moveToLatLong(longitudeDegrees, latitudeDegrees);
                break;
            case GEOBLOCK2:
                geoBlock2.moveToLatLong(longitudeDegrees, latitudeDegrees);
                break;
            default:
                break;
        }
    }

    public void lineTo(GeoPoint point) {
        switch (type) {
            case GEOELLIPSE:
                //geoEllipse.moveTo(point);
                break;
            case GEOPOINT:
                //geoEllipse.moveTo(point);
                break;
            case GEOARC:
                geoArc.lineTo(point);
                break;
            case GEOPATH:
                geoPath.lineTo(point);
                break;
            case GEOBLOCK:
                geoBlock.lineTo(point);
                break;
            case GEOBLOCK2:
                geoBlock2.lineTo(point);
                break;
            default:
                break;
        }
    }

    public void lineToLatLong(double longitudeDegrees, double latitudeDegrees) {
        lineTo(new GeoPoint(longitudeDegrees, latitudeDegrees));
    }

    public void closePath() {
        switch (type) {
            case GEOARC:
                geoArc.closePath();
                break;
            case GEOBLOCK:
                geoBlock.closePath();
                break;
            case GEOBLOCK2:
                geoBlock2.closePath();
                break;
            case GEOPATH:
                geoPath.closePath();
                ;
                break;
            default:
                break;
        }

    }
}
