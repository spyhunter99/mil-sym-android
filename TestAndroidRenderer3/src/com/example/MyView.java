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
import android.view.MotionEvent;
import android.view.View;
import armyc2.c2sd.graphics2d.Point;
import java.util.ArrayList;
import armyc2.c2sd.renderer.utilities.RendererSettings;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;

/**
 *
 * @author Michael Deutch
 */
public class MyView extends View {

    public static String linetype = "";
//    public static String T = "";
//    public static String T1 = "";
//    public static String H = "";
//    public static String H1 = "";
//    public static String W = "";
//    public static String W1 = "";
//    public static String linecolor = "";
//    public static String fillcolor = "";
    
    public MyView(Context context) {
        super(context);
        this.context = context;
    			// TODO Auto-generated constructor stub

    }
    private static ArrayList<Point> _points = new ArrayList();
    private static Context context = null;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int rev = RendererSettings.getInstance().getSymbologyStandard();
        int qty = utility.GetAutoshapeQty(linetype, rev);
        if (event.getAction() == event.ACTION_DOWN) {
            _points.add(new Point((int) event.getAxisValue(MotionEvent.AXIS_X), (int) event.getAxisValue(MotionEvent.AXIS_Y)));
            if (_points.size() >= qty || _points.size() >= 4) {
                invalidate();
            }
//            if (_points.size() >= 3 && linetype.equalsIgnoreCase("track")) {
//                invalidate();
//            }
//            if (_points.size() >= 3 && linetype.equalsIgnoreCase("saafr")) {
//                invalidate();
//            }
//            if (_points.size() >= 3 && linetype.equalsIgnoreCase("lltr")) {
//                invalidate();
//            }
//            if (_points.size() >= 3 && linetype.equalsIgnoreCase("mrr")) {
//                invalidate();
//            }
//            if (_points.size() >= 3 && linetype.equalsIgnoreCase("uav")) {
//                invalidate();
//            }
//            if (_points.size() >= 3 && linetype.equalsIgnoreCase("ac")) {
//                invalidate();
//            }
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
        //utility.SetExtents(50, 55, 10, 4);
        utility.SetExtents(178, -178, 32, 28);
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
