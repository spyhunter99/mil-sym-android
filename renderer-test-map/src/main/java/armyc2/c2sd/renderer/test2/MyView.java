/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package armyc2.c2sd.renderer.test2;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Environment;
import android.util.Log;
import armyc2.c2sd.JavaLineArray.TacticalLines;
import armyc2.c2sd.graphics2d.Point;
import java.util.ArrayList;
import armyc2.c2sd.renderer.utilities.RendererSettings;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.model.VisibleRegion;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.Polygon;

/**
 *
 * @author Michael Deutch
 */
public class MyView {

    protected static String linetype = "";
    protected GoogleMap map = null;
    private Context context = null;

    public MyView(Context context) {
        this.context = context;
        // TODO Auto-generated constructor stub
    }
    protected static ArrayList<Point> _points = new ArrayList();
    protected static ArrayList<LatLng> _pointsGeo = new ArrayList();

    //@Override
    public boolean onTouchEvent(LatLng ptGeo) {
        int rev = RendererSettings.getInstance().getSymbologyStandard();
        android.graphics.Point ptPixels2 = map.getProjection().toScreenLocation(ptGeo);
        Point ptPixels = new Point(ptPixels2.x, ptPixels2.y);
        if (_points.isEmpty()) {
            _pointsGeo.clear();
        }
        _points.add(ptPixels);
        _pointsGeo.add(ptGeo);
        int n = Log.i("onTouchEvent", "longitude = " + Double.toString(ptGeo.longitude));
        if (linetype.equalsIgnoreCase("test") && _points.size() > 0) {
            ArrayList latlngs = new ArrayList();
            LatLng latlng = new LatLng(18.76380640619125, 1.443411000072956);
            latlngs.add(latlng);
            latlng = new LatLng(18.37500364013602, 1.384817473590374);
            latlngs.add(latlng);
            latlng = new LatLng(18.847004684958044, 1.384817473590374);
            latlngs.add(latlng);
            latlng = new LatLng(18.847004684958044, 0.3594264015555382);
            latlngs.add(latlng);
            latlng = new LatLng(18.235932923244995, 0.3594264015555382);
            latlngs.add(latlng);
            latlng = new LatLng(18.847004684958044, 0.3594264015555382);
            latlngs.add(latlng);
            latlng = new LatLng(18.95787232022654, -0.665963664650917);
            latlngs.add(latlng);
            latlng = new LatLng(18.235932923244995, -0.607370138168335);
            latlngs.add(latlng);
            latlng = new LatLng(18.95787232022654, -0.636666901409626);
            latlngs.add(latlng);
            latlng = new LatLng(19.096353815402786, -2.7753393352031708);
            latlngs.add(latlng);
            latlng = new LatLng(21.323124911567373, -2.8339331969618797);
            latlngs.add(latlng);
            latlng = new LatLng(23.16693417130892, -0.22651053965091703);
            latlngs.add(latlng);
            latlng = new LatLng(18.76380640619125, 1.443411000072956);
            latlngs.add(latlng);

            Polygon aPolygon = map.addPolygon(new PolygonOptions()
                    .addAll(latlngs)
                    .strokeColor(Color.BLUE)
                    .strokeWidth(2)
                    .fillColor(Color.YELLOW));

            return true;
        } else {
            int qty = utility.GetAutoshapeQty(linetype, rev);
            if (_points.size() >= qty || _points.size() >= 4) {
                onDraw(null);
            }
        }
        return true;
    }

    protected void setExtents() {
        if (map != null) {
            VisibleRegion region = map.getProjection().getVisibleRegion();
            double ullon = region.farLeft.longitude;
            double ullat = region.farLeft.latitude;
            double lrlon = region.nearRight.longitude;
            double lrlat = region.nearRight.latitude;
            //set the geo extents
            utility.SetExtents(ullon, lrlon, ullat, lrlat);
            //set the pixels extents
            android.graphics.Point ul = map.getProjection().toScreenLocation(region.farLeft);
            android.graphics.Point lr = map.getProjection().toScreenLocation(region.nearRight);
            double width = lr.x - ul.x;
            double height = lr.y - ul.y;
            utility.set_displayPixelsWidth(width);
            utility.set_displayPixelsHeight(height);
        }
        return;
    }

    private void ptsGeoToPixels() {
        _points.clear();
        int j = 0;
        LatLng ptGeo = null;
        double longitude = 0;
        double latitude = 0;
        Projection projection = map.getProjection();
        android.graphics.Point ptPixels = null;
        for (j = 0; j < _pointsGeo.size(); j++) {
            ptGeo = _pointsGeo.get(j);
            longitude = ptGeo.longitude;
            latitude = ptGeo.longitude;
            ptPixels = projection.toScreenLocation(ptGeo);
            _points.add(new Point(ptPixels.x, ptPixels.y));
        }
    }

    protected void DrawFromZoom(Canvas canvas) {
        // TODO Auto-generated method stub
        ptsGeoToPixels();
        if (_points == null) {
            return;
        }
        if (_points.size() < 1) {
            _points.clear();
            return;
        }
        //initialize
        map.clear();
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.WHITE);
        //canvas.drawPaint(paint);

        //canvas will be ignored in these calls
        if (!linetype.equalsIgnoreCase("")) {
            utility.DoubleClickGE(_points, linetype, context);
            String kmlStr = utility.DoubleClickSECRenderer(_points, linetype);
            String fileName = "temp";
            //String body="put this in file";
            this.writeToFile(fileName, kmlStr);
        }
        _points.clear();
    }

    protected void onDraw(Canvas canvas) {
        // TODO Auto-generated method stub
        if (_points == null || _points.isEmpty()) {
            return;
        }
        //initialize
        map.clear();
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.WHITE);
        //canvas.drawPaint(paint);

        int rev = RendererSettings.getInstance().getSymbologyStandard();
        int lineType = utility.GetLinetype(linetype, rev);
        if (lineType < 0) {
            String defaultText = utility.GetLinetype2(linetype, rev);
            lineType = utility.GetLinetype(defaultText, rev);
        }

        switch (lineType) {
            case TacticalLines.CATK:
            case TacticalLines.CATKBYFIRE:
            case TacticalLines.AAFNT:
            case TacticalLines.AAAAA:
            case TacticalLines.AIRAOA:
            case TacticalLines.MAIN:
            case TacticalLines.SPT:
            case TacticalLines.AXAD:
            case TacticalLines.CHANNEL:
                Point pt = utility.ComputeLastPoint(_points);
                _points.add(pt);
                android.graphics.Point aPt = new android.graphics.Point(pt.x, pt.y);
                LatLng latlng = map.getProjection().fromScreenLocation(aPt);
                _pointsGeo.add(latlng);
                break;
            default:
                break;
        }

        //canvas will be ignored in these calls
        if (!linetype.equalsIgnoreCase("")) {
            utility.DoubleClickGE(_points, linetype, context);
            String kmlStr = utility.DoubleClickSECRenderer(_points, linetype);
            String fileName = "temp";
            //String body="put this in file";
            this.writeToFile(fileName, kmlStr);
        }
        _points.clear();
    }

    private void writeToFile(String fileName, String body) {
        FileOutputStream fos = null;

        try {
            //final File dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/KML/");
            final File dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath());

            if (!dir.exists()) {
                dir.mkdirs();
            }

            final File myFile = new File(dir, fileName + ".kml");

            if (!myFile.exists()) {
                myFile.createNewFile();
            }

            WriteKMLFile(myFile, body);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private static void WriteKMLFile(File file, String str) {
        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file));
            //header
            bufferedWriter.write("<?xml version='1.0' encoding='UTF-8'?>");
            bufferedWriter.newLine();
            bufferedWriter.write("<kml xmlns='http://www.opengis.net/kml/2.2'>");
            bufferedWriter.newLine();
            bufferedWriter.write("<Document>");
            bufferedWriter.newLine();
            //KML string
            bufferedWriter.write(str);
            bufferedWriter.newLine();
            //footer
            bufferedWriter.write("</Document>");
            bufferedWriter.newLine();
            bufferedWriter.write("</kml>");
            bufferedWriter.close();
            bufferedWriter = null;
        } catch (IOException exc) {
            exc.printStackTrace();
        }
    }
}
