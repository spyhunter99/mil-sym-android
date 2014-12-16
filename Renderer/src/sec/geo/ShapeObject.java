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
    private int type=-1;
    private GeoEllipse geoEllipse = null;
    private GeoPath geoPath = null;
    private GeoArc geoArc = null;
    private GeoBlock geoBlock = null;
    private GeoBlock2 geoBlock2 = null;
    private GeoPoint geoPoint = null;
    private Area area=null;
    //protected static final Ellipsoid REFERENCE_ELLIPSOID = Ellipsoid.WGS84;
    //private final Path2D path = null;
    //private final GeneralPath path = null;
    //private final ArrayList<GeoPoint> toPoints = null;
    //private final double maxDistanceMeters=0;
    //private final double flatnessDistanceMeters=0;
    //private final int limit=0;
    protected final GeodeticCalculator geoCalc = null;
//	public GeoEllipse(GeoPoint pivot, double widthMeters, double heightMeters, double maxDistanceMeters,
//			double flatnessDistanceMeters, int limit) {
    public ShapeObject(Object obj)
    {
        if(obj instanceof GeoArc)
        {
            geoArc=(GeoArc)obj;
            type=GEOARC;
        }
        else if(obj instanceof GeoPath)
        {
            geoPath=(GeoPath)obj;
            type=GEOPATH;
        }
        else if(obj instanceof GeoEllipse)
        {
            geoEllipse=(GeoEllipse)obj;
            type=GEOELLIPSE;
        }
        else if(obj instanceof GeoBlock)
        {
            geoBlock=(GeoBlock)obj;
            type=GEOBLOCK;
        }
        else if(obj instanceof GeoBlock2)
        {
            geoBlock2=(GeoBlock2)obj;
            type=GEOBLOCK2;
        }
        else if(obj instanceof GeoPoint)
        {
            geoPoint=(GeoPoint)obj;
            type=GEOPOINT;
        }
        else if(obj instanceof Area)
        {
            area=(Area)obj;
            type=AREA;
        }
    }
//    public ShapeObject(Object[] args) {
//        type = (Integer) args[0];
//        switch (type) {
//            case GEOPOINT:
//                break;
//            case GEOARC:
//                break;
//            case GEOPATH:
//                double maxDistanceMeters = (Double) args[1];
//                double flatnessDistanceMeters = (Double) args[2];
//                int limit = (Integer) args[3];
//                geoPath=new GeoPath(maxDistanceMeters,flatnessDistanceMeters,limit);
//                break;
//            case GEOBLOCK:
//                GeoPoint p1=(GeoPoint)args[1];
//                GeoPoint p2=(GeoPoint)args[2];
//                double widthMeters = (Double) args[3];
//                maxDistanceMeters=(Double)args[4];
//                flatnessDistanceMeters = (Double) args[5];
//                limit = (Integer) args[6];
//                geoBlock=new GeoBlock(p1,p2,widthMeters,maxDistanceMeters,flatnessDistanceMeters,limit);
//                break;
//            case GEOBLOCK2:
//                p1=(GeoPoint)args[1];
//                p2=(GeoPoint)args[2];
//                double leftWidthMeters = (Double) args[3];
//                double rightWidthMeters = (Double) args[4];
//                maxDistanceMeters=(Double)args[5];
//                flatnessDistanceMeters = (Double) args[6];
//                limit = (Integer) args[7];
//                geoBlock2=new GeoBlock2(p1,p2,leftWidthMeters,rightWidthMeters,maxDistanceMeters,flatnessDistanceMeters,limit);
//                break;
//            case GEOELLIPSE:
//                GeoPoint pivot = (GeoPoint) args[1];
//                widthMeters = (Double) args[2];
//                double heightMeters = (Double) args[3];
//                maxDistanceMeters = (Double) args[4];
//                flatnessDistanceMeters = (Double) args[5];
//                limit = (Integer) args[5];
//                geoEllipse = new GeoEllipse(pivot, widthMeters, heightMeters, maxDistanceMeters, flatnessDistanceMeters, limit);
//                break;
//            default:
//                break;
//        }
//    }

    public void arcTo(GeoPoint pivot, double widthMeters, double heightMeters, double leftAzimuthDegrees,
            double rightAzimuthDegrees) {
        switch(type)
        {
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
 	//@Override
	public PathIterator getPathIterator(AffineTransform at) {
		//return path.getPathIterator(at);
            switch(type)
            {
                case GEOELLIPSE:
                    return geoEllipse.getPathIterator(at);
                case GEOPATH:
                    return geoPath.getPathIterator(at);
                case GEOBLOCK:
                    return geoBlock.getPathIterator(at);
                case GEOBLOCK2:
                    return geoBlock2.getPathIterator(at,0);
                case GEOARC:
                    return geoArc.getPathIterator(at);
                default:
                    return null;
            }
	}
	
	//@Override
//	public PathIterator getPathIterator(AffineTransform at, double flatness) {
//		//return path.getPathIterator(at, flatness);
//            switch(type)
//            {
//                case GEOELLIPSE:
//                    return geoEllipse.getPathIterator(at,flatness);
//                case GEOPATH:
//                    return geoPath.getPathIterator(at,flatness);
//                default:
//                    return null;
//            }
//	}

	public void moveTo(GeoPoint point) {
            switch(type)
            {
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
            switch(type)
            {
                case GEOELLIPSE:
                    //geoEllipse.moveTo(point);
                    break;
                case GEOPOINT:
                    //geoPoint.moveTo(point);
                    break;
                case GEOARC:
                    geoArc.moveToLatLong(longitudeDegrees,latitudeDegrees);
                    break;
                case GEOPATH:
                    geoPath.moveToLatLong(longitudeDegrees,latitudeDegrees);
                    break;
                case GEOBLOCK:
                    geoBlock.moveToLatLong(longitudeDegrees,latitudeDegrees);
                    break;
                case GEOBLOCK2:
                    geoBlock2.moveToLatLong(longitudeDegrees,latitudeDegrees);
                    break;
                default:
                    break;
            }		
	}
	
	public void lineTo(GeoPoint point) {
            switch(type)
            {
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
            lineTo(new GeoPoint(longitudeDegrees,latitudeDegrees));
        }
	
    public void closePath() {        
        switch(type)
        {
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
                geoPath.closePath();;
                break;
            default:
                break;
        }
                
    }
}
