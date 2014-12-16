package sec.geo;

//import java.awt.geom.Point2D;

public class GeoPoint /*extends Point2D.Double*/ {
        public double x=0;
        public double y=0;
	private static final long serialVersionUID = -1693809094948881246L;
	
	public GeoPoint() {
		//super();
            x=0;
            y=0;
	}
	
	public GeoPoint(double longitudeDegrees, double latitudeDegrees) {
		//super(longitudeDegrees, latitudeDegrees);
                x=longitudeDegrees;
                y=latitudeDegrees;
	}
	
	public double getLatitude() {
		return y;
	}
	
	public void setLatitude(double latitudeDegrees) {
		y = latitudeDegrees;
	}
	
	public double getLongitude() {
		return x;
	}
	
	public void setLongitude(double longitudeDegrees) {
		x = longitudeDegrees;
	}
	
	@Override
	public String toString() {
		return x + "," + y;
	}
}
