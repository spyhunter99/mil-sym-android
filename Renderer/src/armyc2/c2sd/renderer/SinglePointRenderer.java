package armyc2.c2sd.renderer;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Paint.Align;
import android.graphics.Paint.FontMetrics;
import android.graphics.Typeface;
import android.util.Log;
import android.util.SparseArray;

import armyc2.c2sd.renderer.utilities.Color;
import armyc2.c2sd.renderer.utilities.ErrorLogger;
import armyc2.c2sd.renderer.utilities.FontManager;
import armyc2.c2sd.renderer.utilities.ImageInfo;
import armyc2.c2sd.renderer.utilities.ImageInfoCache;
import armyc2.c2sd.renderer.utilities.MilStdAttributes;
import armyc2.c2sd.renderer.utilities.ModifiersTG;
import armyc2.c2sd.renderer.utilities.RectUtilities;
import armyc2.c2sd.renderer.utilities.RendererSettings;
import armyc2.c2sd.renderer.utilities.RendererUtilities;
import armyc2.c2sd.renderer.utilities.SettingsChangedEvent;
import armyc2.c2sd.renderer.utilities.SettingsChangedEventListener;
import armyc2.c2sd.renderer.utilities.SinglePointLookup;
import armyc2.c2sd.renderer.utilities.SinglePointLookupInfo;
import armyc2.c2sd.renderer.utilities.SymbolDef;
import armyc2.c2sd.renderer.utilities.SymbolDimensions;
import armyc2.c2sd.renderer.utilities.SymbolUtilities;
import armyc2.c2sd.renderer.utilities.UnitFontLookup;
import armyc2.c2sd.renderer.utilities.UnitFontLookupInfo;

public class SinglePointRenderer implements SettingsChangedEventListener
{

    private final String TAG = "armyc2.c2sd.singlepointrenderer.SinglePointRenderer";
    private static SinglePointRenderer _instance = null;

    private Typeface _tfUnits = null;
    private Typeface _tfSP = null;
    private Typeface _tfTG = null;

    private Paint _modifierFont = new Paint();
    private Paint _modifierOutlineFont = new Paint();
    private float _modifierDescent = 2;
    private float _modifierFontHeight = 10;
    private int _deviceDPI = 72;

    private final ImageInfoCache _unitCache = new ImageInfoCache();
    private final ImageInfoCache _tgCache = new ImageInfoCache();

    private SinglePointRenderer()
    {
        _tfUnits = FontManager.getInstance().getTypeface(FontManager.FONT_UNIT);
        _tfSP = FontManager.getInstance().getTypeface(FontManager.FONT_SPTG);
        _tfTG = FontManager.getInstance().getTypeface(FontManager.FONT_MPTG);
        TacticalGraphicIconRenderer.setTGTypeFace(_tfTG);
        RendererSettings.getInstance().addEventListener(this);
        //get modifier font values.
        onSettingsChanged(null);
    }

    public static synchronized SinglePointRenderer getInstance()
    {
        if (_instance == null)
        {
            _instance = new SinglePointRenderer();
        }

        return _instance;
    }

    /**
     *
     * @param symbolID
     * @param modifiers
     * @return
     */
    public ImageInfo RenderUnit(String symbolID, SparseArray<String> modifiers, SparseArray<String> attributes)
    {
        ImageInfo temp = null;

        float fontSize = RendererSettings.getInstance().getUnitFontSize();
        Color lineColor = SymbolUtilities.getLineColorOfAffiliation(symbolID);
        Color fillColor = SymbolUtilities.getFillColorOfAffiliation(symbolID);
        int alpha = -1;

        int symStd = RendererSettings.getInstance().getSymbologyStandard();
        //get fill character
        int charFillIndex = UnitFontLookup.getFillCode(symbolID);
        //get frame character
        int charFrameIndex = UnitFontLookup.getFrameCode(symbolID, charFillIndex);
        int charSymbol1Index = -1;
        int charSymbol2Index = -1;
        int charFrameAssumeIndex = -1;
        char frameAssume;

        Paint fillPaint = null;
        Paint framePaint = null;
        Paint symbol1Paint = null;
        Paint symbol2Paint = null;
        Paint frameAssumePaint = null;

        UnitFontLookupInfo lookup = null;

        Rect symbolBounds = null;
        Rect fullBounds = null;
        Bitmap fullBMP = null;

        boolean hasDisplayModifiers = false;
        boolean hasTextModifiers = false;

        int pixelSize = -1;
        boolean keepUnitRatio = true;
        boolean icon = false;
        float[] dimensions =
        {
            0f, 0f, 0f, 0f
        };

        try
        {

            //get MilStdAttributes
            if (attributes.indexOfKey(MilStdAttributes.SymbologyStandard) >= 0)
            {
                symStd = Integer.parseInt(attributes.get(MilStdAttributes.SymbologyStandard));
            }

            if (symStd > RendererSettings.Symbology_2525Bch2_USAS_13_14)
            {
                char affiliation = symbolID.charAt(1);
                switch (affiliation)
                {
                    case 'P':
                    case 'A':
                    case 'S':
                    case 'G':
                    case 'M':
                        charFrameAssumeIndex = charFillIndex + 2;
                        break;
                }
                if (charFrameAssumeIndex > 0)
                {
                    frameAssume = (char) (charFrameAssumeIndex);
                }
            }

            if (attributes.indexOfKey(MilStdAttributes.PixelSize) >= 0)
            {
                pixelSize = Integer.parseInt(attributes.get(MilStdAttributes.PixelSize));
            }
            else
            {
                pixelSize = RendererSettings.getInstance().getDefaultPixelSize();
            }

            if (attributes.indexOfKey(MilStdAttributes.KeepUnitRatio) >= 0)
            {
                keepUnitRatio = Boolean.parseBoolean(attributes.get(MilStdAttributes.KeepUnitRatio));
            }

            if (attributes.indexOfKey(MilStdAttributes.DrawAsIcon) >= 0)
            {
                icon = Boolean.parseBoolean(attributes.get(MilStdAttributes.DrawAsIcon));
            }

            if (icon)//icon won't show modifiers or display icons
            {
                keepUnitRatio = false;
                hasDisplayModifiers = false;
                hasTextModifiers = false;
                symbolID = symbolID.substring(0, 10) + "-----";
            }
            else
            {
                hasDisplayModifiers = ModifierRenderer.hasDisplayModifiers(symbolID, modifiers);
                hasTextModifiers = ModifierRenderer.hasTextModifiers(symbolID, modifiers, attributes);
            }

            if (attributes.indexOfKey(MilStdAttributes.LineColor) >= 0)
            {
                lineColor = new Color(attributes.get(MilStdAttributes.LineColor));
            }
            if (attributes.indexOfKey(MilStdAttributes.FillColor) >= 0)
            {
                fillColor = new Color(attributes.get(MilStdAttributes.FillColor));
            }

            //get symbol info
            lookup = UnitFontLookup.getInstance().getLookupInfo(symbolID, symStd);
            if (lookup == null)//if lookup fails, fix code/use unknown symbol code.
            {
                //if symbolID bad, do best to find a workable code
                lookup = ResolveUnitFontLookupInfo(symbolID, symStd);
            }

            //TODO: pixel size calculations
            ////////////////////////////////////////////////////////////////////
            dimensions = SymbolDimensions.getUnitBounds(charFillIndex, 50);
            symbolBounds = RectUtilities.makeRect(0f, 0f, dimensions[2], dimensions[3]);
            Rect rect = new Rect(symbolBounds);
            float ratio = -1;

            if (pixelSize > 0 && keepUnitRatio == true)
            {
                float heightRatio = UnitFontLookup.getUnitRatioHeight(charFillIndex);
                float widthRatio = UnitFontLookup.getUnitRatioWidth(charFillIndex);

                if (heightRatio > widthRatio)
                {
                    pixelSize = (int) ((pixelSize / 1.5f) * heightRatio);
                }
                else
                {
                    pixelSize = (int) ((pixelSize / 1.5f) * widthRatio);
                }
            }
            if (pixelSize > 0)
            {
                float p = pixelSize;
                float h = rect.height();
                float w = rect.width();

                ratio = Math.min((p / h), (p / w));

                float fontsize = 50;
                //ratio = ratio / 72 * 96;
                fontSize = (((fontsize * ratio)));
	            //fontSize = (((fontsize * ratio) / 96) * 72);
                //fontSize = (((fontsize * ratio) / 96) * _deviceDPI);

                //ctx.font= "75pt UnitFontsC";
                //symbolBounds = SymbolDimensions.getUnitBounds(charFillIndex, (50 * ratio));
                dimensions = SymbolDimensions.getUnitBounds(charFillIndex, 50 * ratio);
                symbolBounds = RectUtilities.makeRect(0f, 0f, dimensions[2], dimensions[3]);
            }//*/

            ////////////////////////////////////////////////////////////////////
            fillPaint = new Paint();
            fillPaint.setStyle(Paint.Style.FILL);
            fillPaint.setColor(fillColor.toARGB());
            fillPaint.setTextSize(fontSize);
            fillPaint.setAntiAlias(true);
            fillPaint.setTextAlign(Align.CENTER);
            fillPaint.setTypeface(_tfUnits);

            framePaint = new Paint();
            framePaint.setStyle(Paint.Style.FILL);
            framePaint.setColor(lineColor.toARGB());
            framePaint.setTextSize(fontSize);
            framePaint.setAntiAlias(true);
            framePaint.setTextAlign(Align.CENTER);
            framePaint.setTypeface(_tfUnits);

            symbol1Paint = new Paint();
            symbol1Paint.setStyle(Paint.Style.FILL);
            symbol1Paint.setColor(lookup.getColor1().toARGB());
            symbol1Paint.setTextSize(fontSize);
            symbol1Paint.setAntiAlias(true);
            symbol1Paint.setTextAlign(Align.CENTER);
            symbol1Paint.setTypeface(_tfUnits);

            symbol2Paint = new Paint();
            symbol2Paint.setStyle(Paint.Style.FILL);
            symbol2Paint.setColor(lookup.getColor2().toARGB());
            symbol2Paint.setTextSize(fontSize);
            symbol2Paint.setAntiAlias(true);
            symbol2Paint.setTextAlign(Align.CENTER);
            symbol2Paint.setTypeface(_tfUnits);

            frameAssumePaint = new Paint();
            frameAssumePaint.setStyle(Paint.Style.FILL);
            frameAssumePaint.setColor(Color.WHITE.toARGB());
            frameAssumePaint.setTextSize(fontSize);
            frameAssumePaint.setAntiAlias(true);
            frameAssumePaint.setTextAlign(Align.CENTER);
            frameAssumePaint.setTypeface(_tfUnits);
        }
        catch (Exception excModifiers)
        {
            ErrorLogger.LogException("MilStdIconRenderer", "RenderUnit", excModifiers);
        }

        try
        {
            ImageInfo ii = null;
            String key = ImageInfoCache.makeKey(symbolID, lineColor.toInt(), fillColor.toInt(), pixelSize, keepUnitRatio, symStd);

            //see if it's in the cache
            ii = _unitCache.get(key);
            //if not, generate symbol
            if (ii == null)//*/
            {

                if (lookup != null)
                {
                    //get Symbol1 character mapping
                    charSymbol1Index = lookup.getMapping1(symbolID);
                    //get Symbol2 character mapping
                    charSymbol2Index = lookup.getMapping2();
                }

                //dimensions of the unit at specified font size
                //dimensions = SymbolDimensions.getUnitBounds(charFillIndex, fontSize);
                //symbolBounds = RectUtilities.makeRect(0, 0, dimensions[2], dimensions[3]);
                //get centerpoint of the image
                Point centerPoint = new Point(Math.round(symbolBounds.width() / 2), Math.round(symbolBounds.height() / 2));
                Point centerCache = new Point(centerPoint);
                //y offset to get centerpoint so we set back to zero when done.
                symbolBounds.top = 0;

                //Draw glyphs to bitmap
                Bitmap bmp = Bitmap.createBitmap((int) (symbolBounds.width()), (int) (symbolBounds.height()), Config.ARGB_8888);
                Canvas canvas = new Canvas(bmp);

                //Log.i("HWA?","HWA: " + String.valueOf(canvas.isHardwareAccelerated()));
                String strFill = null;
                String strFrame = null;
                String strSymbol1 = null;
                String strSymbol2 = null;
                String strFrameAssume = null;
                if (charFillIndex > 0)
                {
                    strFill = String.valueOf((char) charFillIndex);
                }
                if (charFrameIndex > 0)
                {
                    strFrame = String.valueOf((char) charFrameIndex);
                }
                if (charSymbol1Index > 0)
                {
                    strSymbol1 = String.valueOf((char) charSymbol1Index);
                }
                if (charSymbol2Index > 0)
                {
                    strSymbol2 = String.valueOf((char) charSymbol2Index);
                }
                if (charFrameAssumeIndex > 0)
                {
                    strFrameAssume = String.valueOf((char) charFrameAssumeIndex);
                }

				//test
				/*Paint ptTest = new Paint();
                 ptTest.setColor(Color.GREEN);
                 Rect rTest = new Rect(0,0,bmp.getWidth(),bmp.getHeight());
                 canvas.drawRect(rTest, ptTest);//*/
                //end test
                if (strFill != null)
                {
                    canvas.drawText(strFill, centerPoint.x, centerPoint.y + (int) dimensions[1], fillPaint);
                }
                if (strFrame != null)
                {
                    canvas.drawText(strFrame, centerPoint.x, centerPoint.y + (int) dimensions[1], framePaint);
                }
                if (strFrameAssume != null)
                {
                    canvas.drawText(strFrameAssume, centerPoint.x, centerPoint.y + (int) dimensions[1], symbol2Paint);
                }
                if (strSymbol2 != null)
                {
                    canvas.drawText(strSymbol2, centerPoint.x, centerPoint.y + (int) dimensions[1], symbol2Paint);
                }
                if (strSymbol1 != null)
                {
                    canvas.drawText(strSymbol1, centerPoint.x, centerPoint.y + (int) dimensions[1], symbol1Paint);
                }

                //adjust centerpoint for HQStaff if present
                if (SymbolUtilities.isHQ(symbolID))
                {
                    PointF point1 = new PointF();
                    PointF point2 = new PointF();
                    char affiliation = symbolID.charAt(1);
                    if (affiliation == ('F')
                            || affiliation == ('A')
                            || affiliation == ('D')
                            || affiliation == ('M')
                            || affiliation == ('J')
                            || affiliation == ('K')
                            || affiliation == ('N')
                            || affiliation == ('L'))
                    {
                        point1.x = (symbolBounds.left);
                        point1.y = symbolBounds.top + (symbolBounds.height());
                        point2.x = point1.x;
                        point2.y = point1.y + symbolBounds.height();
                    }
                    else
                    {
                        point1.x = (symbolBounds.left + 1);
                        point1.y = symbolBounds.top + (symbolBounds.height() / 2);
                        point2.x = point1.x;
                        point2.y = point1.y + symbolBounds.height();
                    }
                    centerPoint = new Point((int) point2.x, (int) point2.y);
                }

                ii = new ImageInfo(bmp, centerPoint, symbolBounds);

                if (icon == false)
                {
                    _unitCache.put(key, new ImageInfo(bmp, new Point(centerCache), new Rect(symbolBounds)));
                }
            }

            ImageInfo iinew = null;

            ////////////////////////////////////////////////////////////////////
            //process display modifiers
            if (hasDisplayModifiers)
            {
                iinew = ModifierRenderer.processUnitDisplayModifiers(ii, symbolID, modifiers, hasTextModifiers, attributes);
            }

            if (iinew != null)
            {
                ii = iinew;
            }
            iinew = null;

            //process test modifiers
            if (hasTextModifiers)
            {
                iinew = ModifierRenderer.processUnitTextModifiers(ii, symbolID, modifiers, hasTextModifiers, attributes);
            }

            if (iinew != null)
            {
                ii = iinew;
            }
            iinew = null;

            //cleanup///////////////////////////////////////////////////////////
            //bmp.recycle();
            symbolBounds = null;
            fullBMP = null;
            fullBounds = null;
            //fullCanvas = null;

            fillPaint = null;
            framePaint = null;
            symbol1Paint = null;
            symbol2Paint = null;
            lookup = null;
            ////////////////////////////////////////////////////////////////////

            if (icon == true)
            {
                return ii.getSquareImageInfo();
            }
            else
            {
                return ii;
            }

        }
        catch (Exception exc)
        {
            ErrorLogger.LogException("MilStdIconRenderer", "RenderUnit", exc);
        }
        return temp;
    }

    /**
     *
     * @param symbolID
     * @param modifiers
     * @return
     */
    @SuppressWarnings("unused")
    public ImageInfo RenderSP(String symbolID, SparseArray<String> modifiers, SparseArray<String> attributes)
    {
        ImageInfo temp = null;
        String basicSymbolID = null;
        float fontSize = RendererSettings.getInstance().getSPFontSize();
        Color lineColor = SymbolUtilities.getLineColorOfAffiliation(symbolID);
        Color fillColor = null;//SymbolUtilities.getFillColorOfAffiliation(symbolID);

        int alpha = -1;

        int symStd = RendererSettings.getInstance().getSymbologyStandard();
        //fill character
        int charFillIndex = -1;
        //frame character
        int charFrameIndex = -1;
        //made up symbol ID for fill characters
        String fillID = null;

        SymbolDef sd = null;

        Paint fillPaint = null;
        Paint framePaint = null;

        SinglePointLookupInfo lookup = null;

        Rect symbolBounds = null;
        RectF fullBounds = null;
        Bitmap fullBMP = null;

        boolean drawAsIcon = false;
        int pixelSize = -1;
        boolean keepUnitRatio = true;
        boolean hasDisplayModifiers = false;
        boolean hasTextModifiers = false;
        int symbolOutlineWidth = RendererSettings.getInstance().getSinglePointSymbolOutlineWidth();
        float scale = -999;

        try
        {
            if (modifiers == null)
            {
                modifiers = new SparseArray<String>();
            }
            //get MilStdAttributes
            if (attributes != null && attributes.indexOfKey(MilStdAttributes.SymbologyStandard) >= 0)
            {
                symStd = Integer.parseInt(attributes.get(MilStdAttributes.SymbologyStandard));
            }

            //get symbol info
            basicSymbolID = SymbolUtilities.getBasicSymbolID(symbolID);
            lookup = SinglePointLookup.getInstance().getSPLookupInfo(basicSymbolID, symStd);
            if (lookup == null)//if lookup fails, fix code/use unknown symbol code.
            {
                //if symbolID bad, do best to find a workable code
                if (modifiers.get(ModifiersTG.H_ADDITIONAL_INFO_1) != null)
                {
                    modifiers.put(ModifiersTG.H1_ADDITIONAL_INFO_2, modifiers.get(ModifiersTG.H_ADDITIONAL_INFO_1));
                }
                modifiers.put(ModifiersTG.H_ADDITIONAL_INFO_1, symbolID.substring(0, 10));

                symbolID = "G" + SymbolUtilities.getAffiliation(symbolID)
                        + "G" + SymbolUtilities.getStatus(symbolID) + "GPP---****X";
                basicSymbolID = SymbolUtilities.getBasicSymbolID(symbolID);
                lookup = SinglePointLookup.getInstance().getSPLookupInfo(basicSymbolID, symStd);
                lineColor = SymbolUtilities.getLineColorOfAffiliation(symbolID);
                fillColor = null;//SymbolUtilities.getFillColorOfAffiliation(symbolID);
            }

            if (SymbolUtilities.hasDefaultFill(symbolID))
            {
                fillColor = SymbolUtilities.getFillColorOfAffiliation(symbolID);
            }
            if (SymbolUtilities.isTGSPWithFill(symbolID))
            {
                fillID = SymbolUtilities.getTGFillSymbolCode(symbolID);
                if (fillID != null)
                {
                    charFillIndex = SinglePointLookup.getInstance().getCharCodeFromSymbol(fillID, symStd);
                }
            }
            else if (SymbolUtilities.isWeatherSPWithFill(symbolID))
            {
                charFillIndex = charFrameIndex + 1;
                fillColor = SymbolUtilities.getFillColorOfWeather(symbolID);

            }

            if (attributes != null)
            {
                if (attributes.indexOfKey(MilStdAttributes.LineColor) >= 0)
                {
                    lineColor = SymbolUtilities.getColorFromHexString(attributes.get(MilStdAttributes.LineColor));
                }

                if (attributes.indexOfKey(MilStdAttributes.FillColor) >= 0)
                {
                    fillColor = SymbolUtilities.getColorFromHexString(attributes.get(MilStdAttributes.FillColor));
                }

                if (attributes.indexOfKey(MilStdAttributes.Alpha) >= 0)
                {
                    alpha = Integer.parseInt(attributes.get(MilStdAttributes.Alpha));
                }

                if (attributes.indexOfKey(MilStdAttributes.DrawAsIcon) >= 0)
                {
                    drawAsIcon = Boolean.parseBoolean(attributes.get(MilStdAttributes.DrawAsIcon));
                }

                if (attributes.indexOfKey(MilStdAttributes.PixelSize) >= 0)
                {
                    pixelSize = Integer.parseInt(attributes.get(MilStdAttributes.PixelSize));
                }
                else
                {
                    pixelSize = 35;
                }

                if (attributes.indexOfKey(MilStdAttributes.KeepUnitRatio) >= 0)
                {
                    keepUnitRatio = Boolean.parseBoolean(attributes.get(MilStdAttributes.KeepUnitRatio));
                }

                /*if (attributes.indexOfKey(MilStdAttributes.OutlineWidth)>=0)
                 symbolOutlineWidth = Integer.parseInt(attributes.get(MilStdAttributes.OutlineWidth));//*/
            }

            if (drawAsIcon)//icon won't show modifiers or display icons
            {
                keepUnitRatio = false;
                hasDisplayModifiers = false;
                hasTextModifiers = false;
                symbolOutlineWidth = 0;
            }
            else
            {
                hasDisplayModifiers = ModifierRenderer.hasDisplayModifiers(symbolID, modifiers);
                hasTextModifiers = ModifierRenderer.hasTextModifiers(symbolID, modifiers, attributes);
            }

            int outlineOffset = symbolOutlineWidth;
            if (outlineOffset > 2)
            {
                outlineOffset = (outlineOffset - 1) / 2;
            }
            else
            {
                outlineOffset = 0;
            }

            //check symbol font size////////////////////////////////////////////
            Rect rect = null;

            float ratio = 0;

            if (pixelSize > 0)
            {
                symbolBounds = SymbolDimensions.getSymbolBounds(basicSymbolID, symStd, fontSize);
                rect = new Rect(symbolBounds);

                if (keepUnitRatio == true)
                {
	                   //scale it somehow for consistency with units.

                    //when SymbolSizeMedium = 80;
                    //a pixel size of 35 = scale value of 1.0
                    scale = pixelSize / 35.0f;
                }

                //adjust size
                ratio = Math.min((pixelSize / rect.height()), (pixelSize / rect.width()));

            }

            //scale overrides pixel size.
            if (scale != -999)
            {
                ratio = scale;
            }

            if (ratio > 0)
            {
                fontSize = fontSize * ratio;
            }

            //symbolBounds = SymbolDimensions.getSymbolBounds(basicSymbolID, symStd, fontSize);

            ////////////////////////////////////////////////////////////////////
            if (SymbolUtilities.isTGSPWithFill(symbolID) && fillColor != null)
            {
                fillPaint = new Paint();
                fillPaint.setStyle(Paint.Style.FILL);
                fillPaint.setColor(fillColor.toARGB());
                fillPaint.setTextSize(fontSize);
                fillPaint.setAntiAlias(true);
                fillPaint.setTextAlign(Align.CENTER);
                fillPaint.setTypeface(_tfSP);
            }

            framePaint = new Paint();
            framePaint.setStyle(Paint.Style.FILL);
            framePaint.setColor(lineColor.toARGB());
            framePaint.setTextSize(fontSize);
            framePaint.setAntiAlias(true);
            framePaint.setTextAlign(Align.CENTER);
            framePaint.setTypeface(_tfSP);

            //Check if we need to set 'N' to "ENY"
            if (symbolID.charAt(1) == 'H'
                    && modifiers.indexOfKey(MilStdAttributes.DrawAsIcon) >= 0
                    && (Boolean.parseBoolean(modifiers.get(MilStdAttributes.DrawAsIcon)) == false))
            {
                modifiers.put(ModifiersTG.N_HOSTILE, "ENY");
            }

        }
        catch (Exception excModifiers)
        {
            ErrorLogger.LogException("MilStdIconRenderer", "RenderUnit", excModifiers);
        }

        try
        {
            ImageInfo ii = null;
            int intFill = -1;
            if (fillColor != null)
            {
                intFill = fillColor.toInt();
            }
            String key = ImageInfoCache.makeKey(symbolID, lineColor.toInt(), intFill, pixelSize, keepUnitRatio, symStd);

            //see if it's in the cache
            ii = _tgCache.get(key);
            //if not, generate symbol
            if (ii == null)//*/
            {
                //get fill character
                //get frame character
                //get symbol info
                charFrameIndex = -1;//SinglePointLookup.instance.getCharCodeFromSymbol(symbolID);
                charFillIndex = -1;

                if (SymbolUtilities.getStatus(symbolID).equals("A"))
                {
                    charFrameIndex = lookup.getMappingA();
                }
                else
                {
                    charFrameIndex = lookup.getMappingP();
                }

                if (SymbolUtilities.isTGSPWithFill(symbolID) && fillColor != null)
                {
                    fillID = SymbolUtilities.getTGFillSymbolCode(symbolID);
                    if (fillID != null)
                    {
                        charFillIndex = SinglePointLookup.getInstance().getCharCodeFromSymbol(fillID, symStd);
                    }
                }

                //dimensions of the unit at specified font size
                RectF rect = new RectF(0, 0, lookup.getWidth(), lookup.getHeight());

                if (fontSize != 60.0)//adjust boundaries ratio if font size is not at the default setting.
                {
                    double ratio = fontSize / 60;

                    rect = new RectF(0, 0, Math.round(rect.width() * ratio), Math.round(rect.height() * ratio));
                }

                //matrix to place the symbol centered in the MilStdBmp
                Matrix matrix = new Matrix();
                Point centerPoint = null;
                centerPoint = SymbolDimensions.getSymbolCenter(lookup.getBasicSymbolID(), rect);

                if (symbolOutlineWidth > 0)
                {	//adjust matrix and centerpoint to account for outline if present
                    matrix.postTranslate(centerPoint.x + symbolOutlineWidth, centerPoint.y + symbolOutlineWidth);
                    centerPoint.offset(symbolOutlineWidth, symbolOutlineWidth);
                    rect = new RectF(0, 0, (rect.width() + (symbolOutlineWidth * 2)), (rect.height() + (symbolOutlineWidth * 2)));
                }
                else
                {
                    matrix.postTranslate(centerPoint.x, centerPoint.y);
                }

                //Draw glyphs to bitmap
                Bitmap bmp = Bitmap.createBitmap((int) (rect.width() + 0.5), (int) (rect.height() + 0.5), Config.ARGB_8888);
                Canvas canvas = new Canvas(bmp);

                symbolBounds = new Rect(0, 0, bmp.getWidth(), bmp.getHeight());

                String strFill = null;
                String strFrame = null;
                if (charFillIndex > 0)
                {
                    strFill = String.valueOf((char) charFillIndex);
                }
                if (charFrameIndex > 0)
                {
                    strFrame = String.valueOf((char) charFrameIndex);
                }

                canvas.setMatrix(matrix);
                if (strFill != null)
                {
                    canvas.drawText(strFill, 0, 0, fillPaint);
                }

                if (strFrame != null)
                {
                    RendererUtilities.renderSymbolCharacter(canvas, strFrame, 0, 0, framePaint, lineColor, symbolOutlineWidth);
                }

                ii = new ImageInfo(bmp, centerPoint, symbolBounds);

                if (drawAsIcon == false)
                {
                    _tgCache.put(key, ii);//*/
                }
            }

            //Process Modifiers
            ImageInfo iiNew = null;
            if (drawAsIcon == false && (hasTextModifiers || hasDisplayModifiers || SymbolUtilities.isTGSPWithIntegralText(symbolID)))
            {
                if (SymbolUtilities.isTGSPWithSpecialModifierLayout(symbolID)
                        || SymbolUtilities.isTGSPWithIntegralText(symbolID))
                {
                    iiNew = ModifierRenderer.ProcessTGSPWithSpecialModifierLayout(ii, symbolID, modifiers, attributes, lineColor);
                }
                else
                {
                    iiNew = ModifierRenderer.ProcessTGSPModifiers(ii, symbolID, modifiers, attributes, lineColor);
                }

            }

            if (iiNew != null)
            {
                ii = iiNew;
            }

            //cleanup
            //bmp.recycle();
            symbolBounds = null;
            fullBMP = null;
            fullBounds = null;

            fillPaint = null;
            framePaint = null;

            lookup = null;

            if (drawAsIcon)
            {
                return ii.getSquareImageInfo();
            }
            else
            {
                return ii;
            }

        }
        catch (Exception exc)
        {
            ErrorLogger.LogException("MilStdIconRenderer", "RenderSP", exc);
        }
        return null;
    }

    /**
     * Tries to get a valid UnitFontLookupInfo object when the symbolID is
     * poorly formed or there's no match in the lookup. Use this if you get a
     * null return value from:
     * "UnitFontLookupC.getInstance().getLookupInfo(symbolID)" or "CanRender"
     * returns false.
     *
     * @param symbolID
     * @return
     */
    private UnitFontLookupInfo ResolveUnitFontLookupInfo(String symbolID, int symStd)
    {
        String id = symbolID;
        UnitFontLookupInfo lookup = null;
        String affiliation = "";
        String status = "";
        if (id != null && id.length() >= 10)//if lookup fails, fix code/use unknown symbol code.
        {
            StringBuilder sb = new StringBuilder("");
            sb.append(id.charAt(0));

            if (SymbolUtilities.hasValidAffiliation(id) == false)
            {
                sb.append('U');
                affiliation = "U";
            }
            else
            {
                sb.append(id.charAt(1));
                affiliation = id.substring(1, 2);
            }

            if (SymbolUtilities.hasValidBattleDimension(id) == false)
            {
                sb.append('Z');
                sb.replace(0, 1, "S");
            }
            else
            {
                sb.append(id.charAt(2));
            }

            if (SymbolUtilities.hasValidStatus(id) == false)
            {
                sb.append('P');
                status = "P";
            }
            else
            {
                sb.append(id.charAt(3));
                status = id.substring(3, 4);
            }

            sb.append("------");
            if (id.length() >= 15)
            {
                sb.append(id.substring(10, 15));
            }
            else
            {
                sb.append("*****");
            }
            id = sb.toString();

            lookup = UnitFontLookup.getInstance().getLookupInfo(id, symStd);
        }
        else if (symbolID == null || symbolID.equals(""))
        {
            lookup = UnitFontLookup.getInstance().getLookupInfo("SUZP------*****", symStd);
        }
        return lookup;
    }

    public Bitmap getTestSymbol()
    {
        Bitmap temp = null;
        try
        {
            temp = Bitmap.createBitmap(70, 70, Config.ARGB_8888);

            Canvas canvas = new Canvas(temp);

            if (canvas.isHardwareAccelerated())
            {
                System.out.println("HW acceleration supported");
            }
			//canvas.drawColor(Color.WHITE);

            //Typeface tf = Typeface.createFromAsset(_am, "fonts/unitfonts.ttf");
            Typeface tf = _tfUnits;

            Paint fillPaint = new Paint();
            fillPaint.setStyle(Paint.Style.FILL);
            fillPaint.setColor(Color.CYAN.toInt());
            fillPaint.setTextSize(50);
            fillPaint.setAntiAlias(true);
            fillPaint.setTextAlign(Align.CENTER);
            fillPaint.setTypeface(tf);

            Paint framePaint = new Paint();
            framePaint.setStyle(Paint.Style.FILL);
            framePaint.setColor(Color.BLACK.toInt());
            framePaint.setTextSize(50);
            framePaint.setAntiAlias(true);
            framePaint.setTextAlign(Align.CENTER);
            framePaint.setTypeface(tf);

            Paint symbolPaint = new Paint();
            symbolPaint.setStyle(Paint.Style.FILL);
            symbolPaint.setColor(Color.BLACK.toInt());
            symbolPaint.setTextSize(50);
            symbolPaint.setAntiAlias(true);
            symbolPaint.setTextAlign(Align.CENTER);
            symbolPaint.setTypeface(tf);

            String strFill = String.valueOf((char) 800);
            String strFrame = String.valueOf((char) 801);
            String strSymbol = String.valueOf((char) 1121);

            canvas.drawText(strFill, 35, 35, fillPaint);
            canvas.drawText(strFrame, 35, 35, framePaint);
            canvas.drawText(strSymbol, 35, 35, symbolPaint);

            FontMetrics mf = framePaint.getFontMetrics();
            float height = mf.bottom - mf.top;
            float width = fillPaint.measureText(strFrame);

            Log.i(TAG, "top: " + String.valueOf(mf.top));
            Log.i(TAG, "bottom: " + String.valueOf(mf.bottom));
            Log.i(TAG, "ascent: " + String.valueOf(mf.ascent));
            Log.i(TAG, "descent: " + String.valueOf(mf.descent));
            Log.i(TAG, "leading: " + String.valueOf(mf.leading));
            Log.i(TAG, "width: " + String.valueOf(width));
            Log.i(TAG, "height: " + String.valueOf(height));

        }
        catch (Exception exc)
        {
            Log.e(TAG, exc.getMessage());
            Log.e(TAG, getStackTrace(exc));
        }

        return temp;
    }//*/

    public void logError(String tag, Throwable thrown)
    {
        if (tag == null || tag.equals(""))
        {
            tag = "singlePointRenderer";
        }

        String message = thrown.getMessage();
        String stack = getStackTrace(thrown);
        if (message != null)
        {
            Log.e(tag, message);
        }
        if (stack != null)
        {
            Log.e(tag, stack);
        }
    }

    public String getStackTrace(Throwable thrown)
    {
        try
        {
            if (thrown != null)
            {
                if (thrown.getStackTrace() != null)
                {
                    String eol = System.getProperty("line.separator");
                    StringBuilder sb = new StringBuilder();
                    sb.append(thrown.toString());
                    sb.append(eol);
                    for (StackTraceElement element : thrown.getStackTrace())
                    {
                        sb.append("        at ");
                        sb.append(element);
                        sb.append(eol);
                    }
                    return sb.toString();
                }
                else
                {
                    return thrown.getMessage() + "- no stack trace";
                }
            }
            else
            {
                return "no stack trace";
            }
        }
        catch (Exception exc)
        {
            Log.e("getStackTrace", exc.getMessage());
        }
        return thrown.getMessage();
    }//*/

    /*
     private static String PrintList(ArrayList list)
     {
     String message = "";
     for(Object item : list)
     {

     message += item.toString() + "\n";
     }
     return message;
     }//*/
    /*
     private static String PrintObjectMap(Map<String, Object> map)
     {
     Iterator<Object> itr = map.values().iterator();
     String message = "";
     String temp = null;
     while(itr.hasNext())
     {
     temp = String.valueOf(itr.next());
     if(temp != null)
     message += temp + "\n";
     }
     //ErrorLogger.LogMessage(message);
     return message;
     }//*/
    @Override
    public void onSettingsChanged(SettingsChangedEvent sce)
    {

        synchronized (_modifierFont)
        {
            _modifierFont = RendererSettings.getInstance().getModiferFont();
            _modifierOutlineFont = RendererSettings.getInstance().getModiferFont();
            FontMetrics fm = new FontMetrics();
            fm = _modifierFont.getFontMetrics();
            _modifierDescent = fm.descent;
            //_modifierFontHeight = fm.top + fm.bottom;
            _modifierFontHeight = fm.bottom - fm.top;

            _modifierFont.setStrokeWidth(RendererSettings.getInstance().getTextOutlineWidth());
            _modifierOutlineFont.setColor(Color.white.toInt());
            _deviceDPI = RendererSettings.getInstance().getDeviceDPI();

            ModifierRenderer.setModifierFont(_modifierFont, _modifierFontHeight, _modifierDescent);

        }

    }

}
