/* Geodesy by Mike Gavaghan
 * 
 * http://www.gavaghan.org/blog/free-source-code/geodesy-library-vincentys-formula/
 * 
 * This code may be freely used and modified on any personal or professional
 * project.  It comes with no warranty.
 */
package org.gavaghan.geodesy;

/**
 * This is the outcome of a three dimensional geodetic calculation. It
 * represents the path a between two GlobalPositions for a specified reference
 * ellipsoid.
 * 
 * @author Mike Gavaghan
 */
public class GeodeticMeasurement //extends GeodeticCurve
{
   /**
    * The elevation change, in meters, going from the starting to the ending
    * point.
    */
   private final double mElevationChange;

   /** The distance traveled, in meters, going from one point to the next. */
   private final double mP2P;

  //the following added after removing extends GeodeticCurve from this class
   private final double mEllipsoidalDistance;
   /** Azimuth (degrees from north). */
   private final double mAzimuth;
   /** Reverse azimuth (degrees from north). */
   private final double mReverseAzimuth;
   //end section
   
   /**
    * Creates a new instance of GeodeticMeasurement.
    * 
    * @param ellipsoidalDistance ellipsoidal distance in meters
    * @param azimuth azimuth in degrees
    * @param reverseAzimuth reverse azimuth in degrees
    * @param elevationChange the change in elevation, in meters, going from the
    *           starting point to the ending point
    */
   public GeodeticMeasurement(double ellipsoidalDistance, double azimuth, double reverseAzimuth, double elevationChange)
   {
      //removed extends
      //super(ellipsoidalDistance, azimuth, reverseAzimuth);
      mEllipsoidalDistance=ellipsoidalDistance;
      mAzimuth=azimuth;
      mReverseAzimuth=reverseAzimuth;
      //end section
      
      mElevationChange = elevationChange;
      mP2P = Math.sqrt(ellipsoidalDistance * ellipsoidalDistance + mElevationChange * mElevationChange);
   }

   /**
    * Creates a new instance of GeodeticMeasurement.
    * 
    * @param averageCurve average geodetic curve
    * @param elevationChange the change in elevation, in meters, going from the
    *           starting point to the ending point
    */
   public GeodeticMeasurement(GeodeticCurve averageCurve, double elevationChange)
   {
      this(averageCurve.getEllipsoidalDistance(), averageCurve.getAzimuth(), averageCurve.getReverseAzimuth(), elevationChange);
   }

   /**
    * Get the elevation change.
    * 
    * @return elevation change, in meters, going from the starting to the ending
    *         point
    */
   public double getElevationChange()
   {
      return mElevationChange;
   }

   /**
    * Get the point-to-point distance.
    * 
    * @return the distance travelled, in meters, going from one point to the
    *         next
    */
   public double getPointToPointDistance()
   {
      return mP2P;
   }

   /**
    * Get the GeodeticMeasurement as a string.
    */
   @Override
   public String toString()
   {
//      StringBuffer buffer = new StringBuffer();
//      buffer.append(super.toString());
//      buffer.append("elev12=");
//      buffer.append(mElevationChange);
//      buffer.append(";p2p=");
//      buffer.append(mP2P);
//      return buffer.toString();
      
      String buffer = "";
      
      //added to replace extending GeodeticCurve
      //buffer+=(super.toString());
      buffer+="s=";
      buffer+=mEllipsoidalDistance;
      buffer+=";a12=";
      buffer+=mAzimuth;
      buffer+=";a21=";
      buffer+=mReverseAzimuth;
      buffer+=";";
      //end section
      
      buffer+=("elev12=");
      buffer+=(mElevationChange);
      buffer+=(";p2p=");
      buffer+=(mP2P);
      return buffer;
   }

   
}
