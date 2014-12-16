package armyc2.c2sd.renderer.utilities;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.util.SparseArray;

public class RendererUtilities {
	
	private static SparseArray<Color> pastIdealOutlineColors = new SparseArray<Color>();
	/**
     * 
     * @param {String} color like "#FFFFFF"
     * @returns {String}
     */
    public static Color getIdealOutlineColor(Color color){
        Color idealColor = Color.white;
        
        if(pastIdealOutlineColors.indexOfKey(color.toInt())>=0)
        {
            return pastIdealOutlineColors.get(color.toInt());
        }//*/
        
        if(color != null)
        {
        	
        	int threshold = RendererSettings.getInstance().getTextBackgroundAutoColorThreshold();
			
            int r = color.getRed();
            int g = color.getGreen();
            int b = color.getBlue();
        
            float delta = ((r * 0.299f) + (g * 0.587f) + (b * 0.114f));
            
            if((255 - delta < threshold))
            {
                idealColor = Color.black;
            }
            else
            {
                idealColor = Color.white;
            }
        }
        pastIdealOutlineColors.put(color.toInt(),idealColor);
        return idealColor;
    }
    
    public static void renderSymbolCharacter(Canvas ctx, String symbol, int x, int y, Paint paint, Color color, int outlineWidth)
    {
        int tbm = RendererSettings.getInstance().getTextBackgroundMethod();

        Color outlineColor = RendererUtilities.getIdealOutlineColor(color);

        //if(tbm == RendererSettings.TextBackgroundMethod_OUTLINE_QUICK)
        //{    
            //draw symbol outline
        	paint.setStyle(Style.FILL);

        	paint.setColor(outlineColor.toInt());
            if(outlineWidth > 0)
            {
                for(int i = 1; i <= outlineWidth; i++)
                {
                	if(i % 2 == 1)
                	{
                		ctx.drawText(symbol, x - i, y, paint);
                    	ctx.drawText(symbol, x + i, y, paint);
                    	ctx.drawText(symbol, x, y + i, paint);
                    	ctx.drawText(symbol, x, y - i, paint);
                	}
                	else
                	{
                		ctx.drawText(symbol, x - i, y - i, paint);
                    	ctx.drawText(symbol, x + i, y - i, paint);
                    	ctx.drawText(symbol, x - i, y + i, paint);
                    	ctx.drawText(symbol, x + i, y + i, paint);
                	}
                	
                }
                
            }
            //draw symbol
            paint.setColor(color.toInt());
            
        	ctx.drawText(symbol, x, y, paint);
            
        /*}
        else
        {
            //draw text outline
        	paint.setStyle(Style.STROKE);
        	paint.setStrokeWidth(RendererSettings.getInstance().getTextOutlineWidth());
        	paint.setColor(outlineColor.toInt());
            if(outlineWidth > 0)
            {
                
                ctx.drawText(symbol, x, y, paint);
                
            }
            //draw text
            paint.setColor(color.toInt());
            paint.setStyle(Style.FILL);
            
        	ctx.drawText(symbol, x, y, paint);
        }//*/     
    }

}
