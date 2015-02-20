/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Environment;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import armyc2.c2sd.graphics2d.Point;
import armyc2.c2sd.graphics2d.Point2D;
import armyc2.c2sd.renderer.utilities.IPointConversion;
import java.util.ArrayList;
import armyc2.c2sd.renderer.utilities.RendererSettings;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import sec.web.render.PointConverter;

/**
 *
 * @author Michael Deutch
 */
public class MyView extends View {

    public static String linetype = "";
    public static String extents="";
    public static String Rev="";
    
    public MyView(Context context) {
        super(context);
        this.context = context;
    			// TODO Auto-generated constructor stub

    }
    private static ArrayList<Point> _points = new ArrayList();
    private static Context context = null;

    /**
     * assumes utility extents have been set before call
     * @param event 
     */
    private static void displayGeo(MotionEvent event)
    {
        
        double sizeSquare = Math.abs(utility.rightLongitude - utility.leftLongitude);
        if (sizeSquare > 180) {
            sizeSquare = 360 - sizeSquare;
        }

        double scale = 541463 * sizeSquare;

        Point2D ptPixels = null;
        Point2D ptGeo = null;

        IPointConversion converter = null;
        converter = new PointConverter(utility.leftLongitude, utility.upperLatitude, scale);
        Point pt=new Point((int) event.getAxisValue(MotionEvent.AXIS_X), (int) event.getAxisValue(MotionEvent.AXIS_Y));
        ptGeo=converter.PixelsToGeo(pt);
        int n = Log.i("onTouchEvent", "longitude = " + Double.toString(ptGeo.getX()));
    }
    
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int rev = RendererSettings.getInstance().getSymbologyStandard();
        if(Rev.isEmpty()==false)
        {
            if(Rev.equalsIgnoreCase("B"))
                rev=0;
            else rev=1;
        }
        int qty = utility.GetAutoshapeQty(linetype, rev);
        if (event.getAction() == event.ACTION_DOWN) {
            _points.add(new Point((int) event.getAxisValue(MotionEvent.AXIS_X), (int) event.getAxisValue(MotionEvent.AXIS_Y)));
            
            //int n = Log.i("onTouchEvent", "longitude = " + Double.toString(ptGeo.longitude));
            displayGeo(event);
            if (_points.size() >= qty || _points.size() >= 4) {
                invalidate();
            }
        }
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // TODO Auto-generated method stub
        if (_points==null || _points.size() < 1) {
            return;
        }
        super.onDraw(canvas);
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.WHITE);
        canvas.drawPaint(paint);

        utility.set_displayPixelsWidth(canvas.getWidth());
        utility.set_displayPixelsHeight(canvas.getHeight());
        //utility.SetExtents(50, 51, 5, 4);
        utility.SetExtents(50, 55, 10, 4);
        if(extents.isEmpty()==false)
        {
            String ex[]=extents.split(",");
            double left=Double.parseDouble(ex[0]);
            double right=Double.parseDouble(ex[1]);
            double top=Double.parseDouble(ex[2]);
            double bottom=Double.parseDouble(ex[3]);
            utility.SetExtents(left, right, top, bottom);
        }
        //utility.SetExtents(178, -178, 32, 28);
        utility.DoubleClickGE(_points, linetype, canvas, context);
        String kmlStr=utility.DoubleClickSECRenderer(_points, linetype, canvas);
        String fileName="temp";
        //String body="put this in file";
        this.writeToFile(fileName, kmlStr);
        _points.clear();
    }
    private void writeToFile(String fileName, String body)
    {
        FileOutputStream fos = null;

        try {
            //final File dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/KML/" );
            final File dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath());

            if (!dir.exists())
            {
                dir.mkdirs(); 
            }

            final File myFile = new File(dir, fileName + ".kml");

            if (!myFile.exists()) 
            {    
                myFile.createNewFile();
            } 

            //fos = new FileOutputStream(myFile);
            //fos.write(body.getBytes());
            //fos.close();
            WriteKMLFile(myFile,body);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    private static void WriteKMLFile(File file,String str) {
        try {
            //File dir = new File("C:\\KML");
            //File file = new File(dir, "temp.kml");
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
            //ErrorLogger.LogException(_className ,"WriteFile",
            //new RendererException("Failed inside WriteFile", exc));
            //String s = exc.toString();
            exc.printStackTrace();
        }
    }
}
