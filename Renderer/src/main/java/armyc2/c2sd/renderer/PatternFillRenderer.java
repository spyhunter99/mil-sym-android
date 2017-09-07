package armyc2.c2sd.renderer;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.Base64;
import android.util.SparseArray;

import armyc2.c2sd.renderer.utilities.ImageInfo;
import armyc2.c2sd.renderer.utilities.MilStdAttributes;


/**
 * Created by michael.spinelli on 8/23/2017.
 */

public class PatternFillRenderer {


    private static String duriBeachSlopeModerate = "iVBORw0KGgoAAAANSUhEUgAAADYAAAAzBAMAAAAupuZdAAAAAXNSR0IArs4c6QAAAARnQU1BAACxjwv8YQUAAAAJcEhZcwAADsMAAA7DAcdvqGQAAAAYdEVYdFNvZnR3YXJlAHBhaW50Lm5ldCA0LjAuNWWFMmUAAAAYUExURQAAAMXFxczMzM3NzdXV1dnZ2eLi4ubm5nz94OEAAAABdFJOUwBA5thmAAAAWklEQVQ4y2NgoD8wA5PMCQgSKgTkCjuAKEMTBAkRYgIyhAxAfCNlBAkVAgJ3MMlSgCChQiMc4AtrIcywFoKFtTJmWCuPhjXJaVcIT9odDU/qpl1hPGl3SJUTACZHFZF+np+2AAAAAElFTkSuQmCC";
    private static String duriBeachSlopeSteep = "iVBORw0KGgoAAAANSUhEUgAAABoAAAAaBAMAAABbZFH9AAAAAXNSR0IArs4c6QAAAARnQU1BAACxjwv8YQUAAAAJcEhZcwAADsMAAA7DAcdvqGQAAAAYdEVYdFNvZnR3YXJlAHBhaW50Lm5ldCA0LjAuNWWFMmUAAAAVUExURQAAAJSUlKKioru7u87OztfX1+Li4hAmdeYAAAABdFJOUwBA5thmAAAAJElEQVQY02NgIB+YIpEMzEIOMJIJiAQVGGAkELghkfQHg8WdABqRBBGoXfbeAAAAAElFTkSuQmCC";
    private static String duriBeigeStipple = "iVBORw0KGgoAAAANSUhEUgAAAB4AAAAeAQMAAAAB/jzhAAAAAXNSR0IArs4c6QAAAAlwSFlzAAALEwAACxMBAJqcGAAAAAd0SU1FB9gMHhIBKQOEJ3wAAAADUExURfnz8ehEBXEAAAAMSURBVAgdY2AYjAAAAJYAASEQeSYAAAAASUVORK5CYII=";
    private static String duriFoulGround = "iVBORw0KGgoAAAANSUhEUgAAADwAAAA8CAMAAAANIilAAAAAAXNSR0IArs4c6QAAAARnQU1BAACxjwv8YQUAAAAJcEhZcwAADsMAAA7DAcdvqGQAAAAYdEVYdFNvZnR3YXJlAHBhaW50Lm5ldCA0LjAuNWWFMmUAAABOUExURQAAAICAgIaGhouLi42NjZCQkJSUlJiYmJycnKCgoKWlpampqa6urrGxsbe3t7u7u7y8vMbGxsjIyMzMzNHR0djY2N/f3+Xl5evr6/Ly8oBtGtwAAAABdFJOUwBA5thmAAAA50lEQVRIx+2UwW7DMAxDySVy0iqRl7WL6v//0R2GDttR8iHoEN4JU7T0gFOngrpZBZqZZ8yFM1DJe8LbRl4ApTwS5k/SgJklk3olHRBeo8bdzArH1ZSUaGPG37KYuf4x12DT7u/k4l5Y3Fuur1bifQGAcgL28MDfsYWzu5JbOHZX213m3UZSq76xmNkenfrK8lzvzD1enusdlg9cAKVkHt7IDRBOGXNzB+D+wH/R4VAdk1CtwJz7xE6oDqstpETv5bgz/4Hq1ANV6YNqzdyacHJX8iMcux4I1YHUqgPl1aCqJ1RPvZy+AED/Df1MKozlAAAAAElFTkSuQmCC";
    private static String duriKelp = "iVBORw0KGgoAAAANSUhEUgAAAFUAAAA5CAMAAABwHZdJAAAAAXNSR0IArs4c6QAAAARnQU1BAACxjwv8YQUAAAAJcEhZcwAADsMAAA7DAcdvqGQAAAAYdEVYdFNvZnR3YXJlAHBhaW50Lm5ldCA0LjAuNWWFMmUAAABFUExURQAAAICAgIeHh4qKio+Pj5KSkpSUlJqamp6enqGhoaenp6+vr7Gxsbi4uMDAwMjIyM/Pz9bW1tra2t7e3uDg4OXl5evr67APy9YAAAABdFJOUwBA5thmAAABCUlEQVQYGe3BQXbCMAwFwO8KXMWJI6uudP+j9kGB57AjMaw8AwzD8FnueAON6MDxJGUcx5QNG1HQgcRzQcOpoAP9msKiuNF1CjOeVbxojgYUDknUJIVF7DedDRuF8BrL+FclBVJcFUoVd1aYHXslUjwUpjhRuOBZsZfQikadKP3gIGd2NFJUHCZB0FBa0cGM1kqG7owdwzB8TsU7kOENlHCcECs2MqMDTbQ4GpnRgX9HigU3VdLp5HjihtcUEqDOxLlYzcRSLQfBhlPBa2bDlclyChlXnmLBnddMir2EMh5sDueJw0VMBXtZTI5GDpMYDlqpoFFCxmEWE1rMjuOyouG0oj9SDMPwcX/MrgqpcUsGFwAAAABJRU5ErkJggg==";
    private static String duriRigField = "iVBORw0KGgoAAAANSUhEUgAAACwAAAAsBAMAAADsqkcyAAAAAXNSR0IArs4c6QAAAARnQU1BAACxjwv8YQUAAAAJcEhZcwAADsMAAA7DAcdvqGQAAAAYdEVYdFNvZnR3YXJlAHBhaW50Lm5ldCA0LjAuNWWFMmUAAAAqUExURQAAAMDAwMTExMjIyM3NzdHR0dXV1dnZ2d3d3eHh4eXl5enp6e3t7fDw8Awi1VsAAAABdFJOUwBA5thmAAAAh0lEQVQoz2NgGAqATdAHxiwSngBj8goKCkI5TIKCojBhZqCwAIRZCGRugCpmBLKlIGxFIDMBqhokLAlhAlmCAUjC0hCmIZBZABVmBbIVIMyJQOYFmJ2GgkIH4Cok4e7mCm2AMbeFX2AYBbgBjqDCHrDYo4EXR6ThiWIsCQJ78sGV2HAkzUENAN13GlU/PHOyAAAAAElFTkSuQmCC";
    private static String duriSweptArea = "iVBORw0KGgoAAAANSUhEUgAAACQAAAAkBAMAAAATLoWrAAAAAXNSR0IArs4c6QAAAARnQU1BAACxjwv8YQUAAAAJcEhZcwAADsMAAA7DAcdvqGQAAAAYdEVYdFNvZnR3YXJlAHBhaW50Lm5ldCA0LjAuNWWFMmUAAAAnUExURQAAAP4A/v41/v5k/v51/v6F/v6T/v6h/v6t/v7E/v7P/v7Z/v7j/s8+NL4AAAABdFJOUwBA5thmAAAAbElEQVQoz2NgGHLgmKHZAVQRVkFBQVFUIUOgkKABsggLSERQBFloIlhIcAK6PkFBByQhiIigAEKEGyokjVcIi0YGRYiQApJQIUSoAEmICSwihOxUTrCQJAOG8yegCPEECgrGoAfYigUMwwIAAJevDkkSlISwAAAAAElFTkSuQmCC";
    private static String duriWeirs = "iVBORw0KGgoAAAANSUhEUgAAACQAAAAiBAMAAADFd2a2AAAAAXNSR0IArs4c6QAAAARnQU1BAACxjwv8YQUAAAAJcEhZcwAADsMAAA7DAcdvqGQAAAAYdEVYdFNvZnR3YXJlAHBhaW50Lm5ldCA0LjAuNWWFMmUAAAAhUExURQAAAMDAwMTExMjIyM7OztHR0dXV1d3d3eHh4enp6fDw8MnCp1cAAAABdFJOUwBA5thmAAAASUlEQVQoz2NgIBp4YoiwCU7AEFOUxBBioqEy1lAgCBEUCQUDsNBCQWSwACxk0QEE7R1gYAgRCkAyJXDICWHxI6YQ1ypkwDB4AQCRDzCzcuhQTwAAAABJRU5ErkJggg==";

    private static Bitmap patternBeachSlopeModerate = null;
    private static Bitmap patternBeachSlopeSteep = null;
    private static Bitmap patternBeigeStipple = null;
    private static Bitmap patternFoulGround = null;
    private static Bitmap patternKelp = null;
    private static Bitmap patternRigField = null;
    private static Bitmap patternSweptArea = null;
    private static Bitmap patternWeirs = null;

    public static Bitmap MakeSymbolPatternFill(String symbolFillIDs, int symbolFillSize)
    {
        String[] IDs = symbolFillIDs.split(",");
        return MakeSymbolPatternFill(IDs, symbolFillSize);
    }

    public static Bitmap MakeSymbolPatternFill(String[] symbolFillIDs, int symbolFillSize)
    {
        Bitmap fill = null;
        int symCount = symbolFillIDs.length;
        ImageInfo[] symbols = new ImageInfo[symCount];
        int width = 0;
        int height = 0;
        int top = 0;
        int bottom = 0;
        int spacerW = 0;
        int spacerH = 0;
        Rect rect = null;
        SparseArray<String> mods = new SparseArray<String>();
        SparseArray<String> sa = new SparseArray<String>();
        sa.put(MilStdAttributes.PixelSize, String.valueOf(symbolFillSize));

        //calculate texture dimensions
        for(int i = 0; i < symCount; i++)
        {
            if(symbolFillIDs[i].charAt(0)=='W')
                mods.put(MilStdAttributes.OutlineSymbol,"false");
            else
                mods.put(MilStdAttributes.OutlineSymbol,"true");
            symbols[i] = MilStdIconRenderer.getInstance().RenderIcon(symbolFillIDs[i],mods,sa);
            if(symbols[i] != null)
            {
                rect = symbols[i].getImageBounds();

                if(rect.width() > width)
                    width = rect.width();
                if(rect.height() > height)
                    height = rect.height();
                if(top < symbols[i].getCenterPoint().y)
                    top = symbols[i].getCenterPoint().y;
                if(bottom < rect.height() - symbols[i].getCenterPoint().y)
                    bottom = rect.height() - symbols[i].getCenterPoint().y;
            }
        }
        height = top + bottom;
        spacerW = width / 3;
        spacerH = height / 3;

        //create bitmap
        int bWidth = (width * symCount) + (spacerW * symCount);
        int bHeight = height + spacerH;
        if(bWidth > 0 && bHeight > 0)
        {
            fill = Bitmap.createBitmap(bWidth,bHeight, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(fill);
            //draw to bitmap
            int x = spacerW;
            int y = spacerH;
            for(int j = 0; j < symCount; j++)
            {
                ImageInfo ii = symbols[j];
                if(ii != null)
                {
                    Point center = ii.getCenterPoint();
                    canvas.drawBitmap(ii.getImage(),x + width/2 - center.x, y + top - center.y,null);
                    x += spacerW + width;
                }
            }
        }
        return fill;
    }

    /*public static Bitmap MakeSymbolPatternFill(ImageInfo[] iia)
    {
        Bitmap fill = null;
        int imageCount = 0;
        int width = 0;
        int height = 0;
        int top = 0;
        int bottom = 0;
        int spacerW = 0;
        int spacerH = 0;
        Rect rect = null;

        if(iia != null)
            imageCount = iia.length;

        //calculate texture dimensions
        for(int i = 0; i < imageCount; i++)
        {
            rect = iia[i].getImageBounds();
            if(rect.width() > width)
                width = rect.width();
            if(rect.height() > height)
                height = rect.height();
            if(top < iia[i].getCenterPoint().y)
                top = iia[i].getCenterPoint().y;
            if(bottom < rect.height() - iia[i].getCenterPoint().y)
                bottom = rect.height() - iia[i].getCenterPoint().y;
        }
        height = top + bottom;
        spacerW = width / 3;
        spacerH = height / 3;

        //create bitmap
        int bWidth = (width * imageCount) + (spacerW * imageCount);
        int bHeight = height + spacerH;
        fill = Bitmap.createBitmap(bWidth,bHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(fill);
        //draw to bitmap
        int x = spacerW;
        int y = spacerH;
        for(int j = 0; j < imageCount; j++)
        {
            ImageInfo ii = iia[j];
            Point center = ii.getCenterPoint();
            canvas.drawBitmap(ii.getImage(),x + width/2 - center.x, y + top - center.y,null);
            x += spacerW + width;
        }

        return fill;
    }//*/

    public static Bitmap MakeMetocPatternFill(String symbolID)
    {
        if(symbolID.equals("WO-DBSM-----A--"))//beach slope moderate
        {
            if(patternBeachSlopeModerate == null)
                patternBeachSlopeModerate = LoadBMPFromDataURL(duriBeachSlopeModerate);
            return patternBeachSlopeModerate;
        }
        else if(symbolID.equals("WO-DBST-----A--"))//beach slope steep
        {
            if(patternBeachSlopeSteep == null)
                patternBeachSlopeSteep = LoadBMPFromDataURL(duriBeachSlopeSteep);
            return patternBeachSlopeSteep;
        }
        else if(symbolID.equals("WO-DHCB-----A--"))//beige stipple
        {
            if(patternBeigeStipple == null)
                patternBeigeStipple = LoadBMPFromDataURL(duriBeigeStipple);
            return patternBeigeStipple;
        }
        else if(symbolID.equals("WO-DHHDF----A--"))//Foul Ground
        {
            if(patternFoulGround == null)
                patternFoulGround = LoadBMPFromDataURL(duriFoulGround);
            return patternFoulGround;
        }
        else if(symbolID.equals("WO-DHHDK----A--"))//Kelp
        {
            if(patternKelp == null)
                patternKelp = LoadBMPFromDataURL(duriKelp);
            return patternKelp;
        }
        else if(symbolID.equals("WO-DMOA-----A--"))//OIL/GAS RIG FIELD
        {
            if(patternRigField == null)
                patternRigField = LoadBMPFromDataURL(duriRigField);
            return patternRigField;
        }
        else if(symbolID.equals("WO-DL-SA----A--"))//swept area
        {
            if(patternSweptArea == null)
                patternSweptArea = LoadBMPFromDataURL(duriSweptArea);
            return patternSweptArea;
        }
        else if(symbolID.equals("WOS-HPFF----A--"))//Weirs
        {
            if(patternWeirs == null)
                patternWeirs = LoadBMPFromDataURL(duriWeirs);
            return patternWeirs;
        }
        else
            return null;
    }

    private static Bitmap LoadBMPFromDataURL(String durl)
    {
        byte[] decodedString = Base64.decode(durl, Base64.DEFAULT);
        Bitmap bmp = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        return bmp;
    }

    public static Bitmap MakeHatchPatternFill()
    {
        return null;
    }

    /*private static Bitmap[] fillBMP = null;

    protected static boolean createBitmapShader(TGLight tg,
                                                ShapeInfo shape,
                                                Context context)
    {
        try
        {
            if(fillBMP==null)
            {
                fillBMP=new Bitmap[8];
                for(int j=0;j<8;j++)
                    fillBMP[j]=null;
            }
            BitmapShader fillBMPshader=null;
            int linetype=tg.get_LineType();
            int useIndex=-1;
            switch(linetype)
            {
                case TacticalLines.WEIRS:
                    if(fillBMP[0]==null)
                        fillBMP[0] = BitmapFactory.decodeResource(context.getResources(), R.drawable.weirs);
                    useIndex=0;
                    break;
                case TacticalLines.BEACH_SLOPE_MODERATE:
                    if(fillBMP[1]==null)
                        fillBMP[1] = BitmapFactory.decodeResource(context.getResources(), R.drawable.beach_slope_moderate);
                    useIndex=1;
                    break;
                case TacticalLines.BEACH_SLOPE_STEEP:
                    if(fillBMP[2]==null)
                        fillBMP[2] = BitmapFactory.decodeResource(context.getResources(), R.drawable.beach_slope_steep);
                    useIndex=2;
                    break;
                case TacticalLines.KELP:
                    if(fillBMP[3]==null)
                        fillBMP[3] = BitmapFactory.decodeResource(context.getResources(), R.drawable.kelp);
                    useIndex=3;
                    break;
                case TacticalLines.OIL_RIG_FIELD:
                    if(fillBMP[4]==null)
                        fillBMP[4] = BitmapFactory.decodeResource(context.getResources(), R.drawable.oil_rig_field);
                    useIndex=4;
                    break;
                case TacticalLines.BEACH:
                    if(fillBMP[5]==null)
                        fillBMP[5] = BitmapFactory.decodeResource(context.getResources(), R.drawable.beige_stipple);
                    useIndex=5;
                    break;
                case TacticalLines.FOUL_GROUND:
                    if(fillBMP[6]==null)
                        fillBMP[6] = BitmapFactory.decodeResource(context.getResources(), R.drawable.foul_ground);
                    useIndex=6;
                    break;
                case TacticalLines.SWEPT_AREA:
                    if(fillBMP[7]==null)
                        fillBMP[7] = BitmapFactory.decodeResource(context.getResources(), R.drawable.swept_area);
                    useIndex=7;
                    break;
                default:    //we do not use pattern fill for these
                    return false;
            }
            if(useIndex >= 0)
            {
                fillBMPshader = new BitmapShader(fillBMP[useIndex], Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
                shape.setShader(fillBMPshader);
                shape.setFillColor(Color.WHITE);
                //fillBMP.recycle();
            }
        }
        catch(Exception exc)
        {
            ErrorLogger.LogException("clsUtility", "createBitmapShader",
                    new RendererException("Failed inside createBitmapShader", exc));

        }
        return true;
    }//*/

}
