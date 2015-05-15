package armyc2.c2sd.renderer.utilities;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.graphics.*;
import android.graphics.Bitmap.Config;
import android.graphics.drawable.shapes.ArcShape;
import android.util.FloatMath;
import android.util.Log;

public class SVGPath 
{
    private String _ID = null;
    private String _strPath = null;
    private static String _regex1 = "(?=[M,m,Z,z,L,l,H,h,V,v,C,c,S,s,Q,q,T,t,A,a])";
    private static String _regex2 = "(?=[M,m,L,l,H,h,V,v,C,c,S,s,Q,q,T,t,A,a])";
    private static char[] svgCommands = { 'M', 'm', 'Z', 'z', 'L', 'l', 'H', 'h', 'V', 'v', 'C', 'c', 'S', 's', 'Q', 'q', 'T', 't', 'A', 'a' };


    private Path _path = null;

    public String getID()
    {
        return _ID;
    }

    /**
     * Returns bounds of the core symbol
     * */
    public RectF getBounds()
    {
    	RectF bounds = new RectF();
        _path.computeBounds(bounds,true);
        return bounds;
        
    }

    /*
     * Returns bounds of the symbol when it's being outlined.
     * */
    public RectF getBounds(float outlineWidth)
    {
    	RectF bounds = new RectF();
        _path.computeBounds(bounds,true);
        bounds = new RectF(bounds.left - outlineWidth, bounds.top - outlineWidth, bounds.right + outlineWidth, bounds.bottom + outlineWidth);
        return bounds;
    }

    @SuppressWarnings("unused")
	private SVGPath()
    {
    }

    public SVGPath(SVGPath path)
    {
        _path = new Path(path._path);
        _ID = path._ID;
        _strPath = path._strPath.substring(0);
    }

    public SVGPath(String unicodeHex, String path)
    {
        _ID = String.valueOf(Integer.parseInt(unicodeHex,  16));
        _strPath = path;

        _path = new Path();
        
        parsePath();

    }

    /*public override object Clone()
    {
        SVGPath clone = new SVGPath();
        clone._path = (GraphicsPath)_path.Clone();
        clone._ID = _ID;
        clone._strPath = (String)_strPath.Clone();
        return clone;
    }*/

    private void parsePath()
    {
        String delimiter = " ";
                
        String[] commands = _strPath.split(_regex1);
        String[] values = null;
        float[] points = new float[7];
        PointF[] pointFs = new PointF[4];
        PointF lastPoint = new PointF(0, 0);
        PointF lastControlPoint = new PointF(0, 0);
        //PointF firstPoint = new PointF(0, 0);
        //PointF firstPoint = new PointF(0, 0);

        try
        {

            for (int i = 0; i < commands.length; i++)
            {

                String strCommand = commands[i];
                char action = (strCommand != null && strCommand.length() > 0) ? strCommand.charAt(0) : ' ';
                values = strCommand.split(delimiter);

                if (action == 'M')
                {
                    points[0] = Float.valueOf(values[0].substring(1));
                    points[1] = -Float.valueOf(values[1]);
                    _path.moveTo(points[0], points[1]);
                    lastPoint.set(points[0], points[1]);
                    
                    //_path.StartFigure();
                    
                }
                else if (action == 'm')
                {
                    points[0] = Float.valueOf(values[0].substring(1)) + lastPoint.x;
                    points[1] = -Float.valueOf(values[1]) + lastPoint.y;
                    _path.moveTo(points[0], points[1]);
                    lastPoint.set(points[0], points[1]);
                    //_path.StartFigure();
                }
                else if (action == 'L')
                {
                    points[0] = Float.valueOf(values[0].substring(1));
                    points[1] = -Float.valueOf(values[1]);
                    _path.lineTo(points[0], points[1]);
                    lastPoint.set(points[0], points[1]);

                }
                else if (action == 'l')
                {
                    points[0] = Float.valueOf(values[0].substring(1)) + lastPoint.x;
                    points[1] = -Float.valueOf(values[1]) + lastPoint.y;
                    _path.lineTo(points[0], points[1]);
                    lastPoint.set(points[0], points[1]);
                }
                else if (action == 'H')
                {
                    points[0] = Float.valueOf(values[0].substring(1));
                    _path.lineTo(points[0], lastPoint.y);
                    lastPoint.set(points[0], lastPoint.y);

                }
                else if (action == 'h')
                {
                    points[0] = Float.valueOf(values[0].substring(1)) + lastPoint.x;
                    _path.lineTo(points[0], lastPoint.y);
                    lastPoint.set(points[0], lastPoint.y);
                }
                else if (action == 'V')
                {
                    points[0] = -Float.valueOf(values[0].substring(1));
                    _path.lineTo(lastPoint.x, points[0]);
                    lastPoint.set(lastPoint.x, points[0]);

                }
                else if (action == 'v')
                {
                    points[0] = -Float.valueOf(values[0].substring(1)) + lastPoint.y;
                    _path.lineTo(lastPoint.x, points[0]);
                    lastPoint.set(lastPoint.x, points[0]);
                }
                else if (action == 'C')//cubic bezier, 2 control points
                {
                    points[0] = Float.valueOf(values[0].substring(1));
                    points[1] = -Float.valueOf(values[1]);
                    points[2] = Float.valueOf(values[2]);
                    points[3] = -Float.valueOf(values[3]);
                    points[4] = Float.valueOf(values[4]);
                    points[5] = -Float.valueOf(values[5]);


                    _path.cubicTo(points[0], points[1], points[2], points[3], points[4], points[5]);
                    

                    lastPoint.set(points[4], points[5]);
                    lastControlPoint.set(points[2], points[3]);
                }
                else if (action == 'c')
                {
                    points[0] = Float.valueOf(values[0].substring(1)) + lastPoint.x;
                    points[1] = -Float.valueOf(values[1]) + lastPoint.y;
                    points[2] = Float.valueOf(values[2]) + lastPoint.x;
                    points[3] = -Float.valueOf(values[3]) + lastPoint.y;
                    points[4] = Float.valueOf(values[4]) + lastPoint.x;
                    points[5] = -Float.valueOf(values[5]) + lastPoint.y;

                    pointFs[0] = lastPoint;
                    pointFs[1] = new PointF(points[0], points[1]);
                    pointFs[2] = new PointF(points[2], points[3]);
                    pointFs[3] = new PointF(points[4], points[5]);
                    //_path.AddBezier(pointFs[0], pointFs[1], pointFs[2], pointFs[3]);

                    _path.cubicTo(points[0], points[1], points[2], points[3], points[4], points[5]);

                    //SvgCubicCurveSegment sccs = new SvgCubicCurveSegment(_path.getLastPoint(), pointFs[1], pointFs[2], pointFs[3]);
                    //sccs.AddToPath(_path);
                    //lastPoint = new PointF(points[4], points[5]);
                    lastPoint.set(points[2], points[3]);
                    lastControlPoint.set(points[2], points[3]);

                }
                else if (action == 'S')
                {
                    points[0] = Float.valueOf(values[0].substring(1));
                    points[1] = -Float.valueOf(values[1]);
                    points[2] = Float.valueOf(values[2]);
                    points[3] = -Float.valueOf(values[3]);

                    pointFs[0] = lastPoint;
                    pointFs[1] = mirrorControlPoint(lastControlPoint, lastPoint);
                    pointFs[2] = new PointF(points[0], points[1]);
                    pointFs[3] = new PointF(points[2], points[3]);
                    
                    _path.cubicTo(pointFs[1].x,pointFs[1].y, points[0], points[1], points[2], points[3]);

                    lastPoint.set(points[2], points[3]);
                    lastControlPoint.set(points[0], points[1]);
                }
                else if (action == 's')
                {
                    points[0] = Float.valueOf(values[0].substring(1)) + lastPoint.x;
                    points[1] = -Float.valueOf(values[1]) + lastPoint.y;
                    points[2] = Float.valueOf(values[2]) + lastPoint.x;
                    points[3] = -Float.valueOf(values[3]) + lastPoint.y;

                    pointFs[0] = lastPoint;
                    pointFs[1] = mirrorControlPoint(lastControlPoint, lastPoint);

                    _path.cubicTo(pointFs[1].x,pointFs[1].y, points[0], points[1], points[2], points[3]);

                    lastPoint.set(points[2], points[3]);
                    lastControlPoint.set(points[0], points[1]);
                }
                else if (action == 'Q')//quadratic bezier, 1 control point
                {
                    points[0] = Float.valueOf(values[0].substring(1));
                    points[1] = -Float.valueOf(values[1]);
                    points[2] = Float.valueOf(values[2]);
                    points[3] = -Float.valueOf(values[3]);

                    _path.quadTo(points[0], points[1], points[2], points[3]);

                    lastPoint.set(points[2], points[3]);

                    lastControlPoint.set(points[0], points[1]);
                }
                else if (action == 'q')
                {
                    points[0] = Float.valueOf(values[0].substring(1)) + lastPoint.x;
                    points[1] = -Float.valueOf(values[1]) + lastPoint.y;
                    points[2] = Float.valueOf(values[2]) + lastPoint.x;
                    points[3] = -Float.valueOf(values[3]) + lastPoint.y;

                    _path.quadTo(points[0], points[1], points[2], points[3]);


                    lastPoint.set(points[2], points[3]);

                    lastControlPoint.set(points[0], points[1]);
                }
                else if (action == 'T')
                {
                    points[0] = Float.valueOf(values[0].substring(1));
                    points[1] = -Float.valueOf(values[1]);


                    PointF QP1 = mirrorControlPoint(lastControlPoint, lastPoint);

                    _path.quadTo(QP1.x, QP1.y, points[0], points[1]);


                    lastPoint.set(points[0], points[1]);

                    lastControlPoint = QP1;
                }
                else if (action == 't')
                {
                    points[0] = Float.valueOf(values[0].substring(1)) + lastPoint.x;
                    points[1] = -Float.valueOf(values[1]) + lastPoint.y;

                    //convert quadratic to bezier
                    PointF QP1 = mirrorControlPoint(lastControlPoint, lastPoint);

                    _path.quadTo(QP1.x, QP1.y, points[0], points[1]);

                    //SvgQuadraticCurveSegment qcs = new SvgQuadraticCurveSegment(QP0, QP1, QP2);
                    //qcs.AddToPath(_path);
                    lastPoint.set(points[0], points[1]);

                    lastControlPoint = QP1;
                }
                else if (action == 'A')
                {
                    points[0] = Float.valueOf(values[0].substring(1));
                    points[1] = Float.valueOf(values[1]);
                    points[2] = Float.valueOf(values[2]);
                    points[3] = Float.valueOf(values[3]);
                    points[4] = Float.valueOf(values[4]);
                    points[5] = Float.valueOf(values[5]);
                    points[6] = -Float.valueOf(values[6]);

                    drawArc(_path,lastPoint.x, lastPoint.y,points[0],points[1],points[2],points[3],points[4],(int)points[5],(int)points[6]);
                    
                    lastPoint.set(points[5], points[6]);
                    lastControlPoint.set(points[2], points[3]);
                    
                }
                else if (action == 'a')
                {
                    points[0] = Float.valueOf(values[0].substring(1));
                    points[1] = Float.valueOf(values[1]);
                    points[2] = Float.valueOf(values[2]);
                    points[3] = Float.valueOf(values[3]);
                    points[4] = Float.valueOf(values[4]);
                    points[5] = Float.valueOf(values[5] + lastPoint.x);
                    points[6] = -Float.valueOf(values[6] + lastPoint.y);

                    drawArc(_path,lastPoint.x, lastPoint.y,points[0],points[1],points[2],points[3],points[4],(int)points[5],(int)points[6]);
                    
                    lastPoint.set(points[5], points[6]);
                    lastControlPoint.set(points[2], points[3]);

                }
                else if (action == 'Z' || action == 'z')
                {
                    _path.close();
                }

                //Matrix verticalFlip = new Matrix();
                //verticalFlip.Scale(1, -1);
                //_path.Transform(verticalFlip);
            

            }
        }
        catch (Exception exc)
        {
            //ErrorLogger.LogException("SVGPath", "parsePath", exc);
        	Log.e("SVGPath.parsePath", exc.getMessage());
        }

    }
    
    private PointF mirrorControlPoint(PointF cp, PointF endPoint)
    {

        float xOffset = endPoint.x - cp.x;
        float yOffset = endPoint.y - cp.y;

        PointF mirror = new PointF(endPoint.x + xOffset, endPoint.y + yOffset);

        return mirror;
    }
    
    private static double angle(double x1, double y1, double x2, double y2) 
    {
    	return Math.toDegrees(Math.atan2(x1, y1) - Math.atan2(x2, y2)) % 360;
	}
    
    private static final RectF arcRectf = new RectF();
    private static final Matrix arcMatrix = new Matrix();
    private static final Matrix arcMatrix2 = new Matrix();
    private static void drawArc(Path p, float lastX, float lastY, float x, float y, float rx, float ry, float theta,
    int largeArc, int sweepArc) 
    {
	    // Log.d("drawArc", "from (" + lastX + "," + lastY + ") to (" + x + ","+ y + ") r=(" + rx + "," + ry +
	    // ") theta=" + theta + " flags="+ largeArc + "," + sweepArc);
	    // http://www.w3.org/TR/SVG/implnote.html#ArcImplementationNotes
	    if (rx == 0 || ry == 0) 
	    {
		    p.lineTo(x, y);
		    return;
	    }
	    if (x == lastX && y == lastY) 
	    {
	    	return; // nothing to draw
	    }
	    rx = Math.abs(rx);
	    ry = Math.abs(ry);
	    final double thrad = theta * Math.PI / 180;
	    final double st = Math.sin(thrad);
	    final double ct = Math.cos(thrad);
	    final double xc = (lastX - x) / 2;
	    final double yc = (lastY - y) / 2;
	    final double x1t = ct * xc + st * yc;
	    final double y1t = -st * xc + ct * yc;
	    final double x1ts = x1t * x1t;
	    final double y1ts = y1t * y1t;
	    double rxs = rx * rx;
	    double rys = ry * ry;
	    double lambda = (x1ts / rxs + y1ts / rys) * 1.001f; // add 0.1% to be sure that no out of range occurs due to
	    // limited precision
	    if (lambda > 1) 
            {
	    		double lambdasr = Math.sqrt(lambda);
                rx *= lambdasr;
                ry *= lambdasr;
                rxs = rx * rx;
                rys = ry * ry;
	    }
	    final double R =
	    Math.sqrt((rxs * rys - rxs * y1ts - rys * x1ts) / (rxs * y1ts + rys * x1ts))
	    * ((largeArc == sweepArc) ? -1 : 1);
	    final double cxt = R * rx * y1t / ry;
	    final double cyt = -R * ry * x1t / rx;
	    final double cx = ct * cxt - st * cyt + (lastX + x) / 2;
	    final double cy = st * cxt + ct * cyt + (lastY + y) / 2;
	    final double th1 = angle(1, 0, (x1t - cxt) / rx, (y1t - cyt) / ry);
	    double dth = angle((x1t - cxt) / rx, (y1t - cyt) / ry, (-x1t - cxt) / rx, (-y1t - cyt) / ry);
	    if (sweepArc == 0 && dth > 0) 
            {
                dth -= 360;
	    } 
	    else if (sweepArc != 0 && dth < 0) 
	    {
	    	dth += 360;
	    }
	    // draw
	    if ((theta % 360) == 0) 
	    {
                // no rotate and translate need
                arcRectf.set((float)(cx - rx), (float)(cy - ry), (float)(cx + rx), (float)(cy + ry));
                p.arcTo(arcRectf, (float)th1, (float)dth);
	    } 
	    else 
	    {
                // this is the hard and slow part :-)
                arcRectf.set(-rx, -ry, rx, ry);
                arcMatrix.reset();
                arcMatrix.postRotate(theta);
                arcMatrix.postTranslate((float)cx, (float)cy);
                arcMatrix.invert(arcMatrix2);
                p.transform(arcMatrix2);
                p.arcTo(arcRectf, (float)th1, (float)dth);
                p.transform(arcMatrix);
	    }
    }
    
    public Matrix TransformToFitDimensions(int width, int height)
    {
    	RectF rect = new RectF(); 
    	_path.computeBounds(rect, true);
    	Matrix m = new Matrix();
        Matrix mScale = new Matrix();
        Matrix mTranslate = new Matrix();
        
        float sx = width / rect.width();
        float sy = height / rect.height();
        if (sx < sy)
        {
            mScale.setScale(sx, sx);
            m.setScale(sx, sx);
        }
        else
        {
            mScale.setScale(sy, sy);
            m.setScale(sy, sy);
        }

        _path.transform(mScale);
        
        _path.computeBounds(rect, true);//(testMatrix, testPen);

        float transx = 0;
        float transy = 0;
        if (rect.left < 0)
            transx = rect.left * -1.0f;
        if (rect.top < 0)
            transy = rect.top * -1.0f;
        mTranslate.setTranslate(transx, transy);
        m.postTranslate(transx, transy);
        _path.transform(mTranslate);
        
        
        return m;
    }
    
    public void Transform(Matrix m)
    {
        _path.transform(m);
    }
    
    public void Draw(Canvas c, Color lineColor, float lineWidth, Color fillColor, Matrix m)
    {
				
        if (m != null)
            _path.transform(m);
        if (lineColor != null)
        {
        	Paint strokePaint = new Paint();
    		strokePaint.setStyle(Paint.Style.STROKE);
    		strokePaint.setColor(lineColor.toARGB());
    		strokePaint.setAntiAlias(true);
            c.drawPath(_path, strokePaint);
        }

        if (fillColor != null)
        {
        	Paint fillPaint = new Paint();
    		fillPaint.setStyle(Paint.Style.FILL);
    		fillPaint.setColor(fillColor.toARGB());
    		fillPaint.setAntiAlias(true);
            c.drawPath(_path, fillPaint);
        }
    }

    /**
     * Draws SVG to fit into a image of the specified dimensions
     * */
    public Bitmap Draw(int width, int height, Color lineColor, Color fillColor)
    {
    	Bitmap bmp = Bitmap.createBitmap(width, height, Config.ARGB_8888);
		Canvas c = new Canvas(bmp);
		Bitmap foo = null;

        RectF rect = new RectF(); 
		_path.computeBounds(rect, true);
        Matrix m = new Matrix();

        float sx = width / rect.width();
        float sy = height / rect.height();
        if (sx < sy)
            m.postScale(sx, sx);
        else
            m.postScale(sy, sy);

        _path.transform(m);
        _path.computeBounds(rect, true);
        m = new Matrix();
        float transx = 0;
        float transy = 0;
        if (rect.left < 0)
            transx = rect.left * -1.0f;
        if (rect.top < 0)
            transy = rect.top * -1.0f;
        m.postTranslate(transx, transy);
        //m.Translate(300,300);

        _path.transform(m);
        _path.computeBounds(rect, true);

        //Console.WriteLine(rect.ToString());
        
        if (lineColor != null)
        {
        	Paint strokePaint = new Paint();
    		strokePaint.setStyle(Paint.Style.STROKE);
    		strokePaint.setColor(lineColor.toARGB());
    		strokePaint.setAntiAlias(true);
            c.drawPath(_path, strokePaint);
        }

        if (fillColor != null)
        {
        	Paint fillPaint = new Paint();
    		fillPaint.setStyle(Paint.Style.FILL);
    		fillPaint.setColor(fillColor.toARGB());
    		fillPaint.setAntiAlias(true);
            c.drawPath(_path, fillPaint);
        }
        
        return bmp;
/*
        c.DrawPath(_path, new Paint.);
        Color c = Color.FromArgb(128, 0, 255, 255);
        Pen p = new Pen(c);
        Brush b = new SolidBrush(c);
        g.FillPath(b, _gp);
        g.DrawRectangle(new Pen(Color.Green), 0, 0, width - 1, height - 1);
        foo = (Image)bmp;
        return foo;//*/
    }


}

//*/