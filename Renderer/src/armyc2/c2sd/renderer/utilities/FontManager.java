package armyc2.c2sd.renderer.utilities;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.graphics.Typeface;
import android.util.Log;



public class FontManager {
	
	public static String FONT_UNIT = "UF";
	public static String FONT_SPTG = "SF";
	public static String FONT_MPTG = "MF";
	
	private static FontManager _instance = null;
	private static Boolean _initSuccess = false;
	
	private Typeface _tfUnits = null;
	private Typeface _tfSP = null;
	private Typeface _tfTG = null;
	private String unitFontName = "unitfont.ttf";
	private String spFontName = "singlepointfont.ttf";
	private String tgFontName = "tacticalgraphicsfont.ttf";
	private String _cacheDir = "";
	
	private FontManager()
	{
		
	}
	
	public static synchronized FontManager getInstance()
    {
      if(_instance == null)
          _instance = new FontManager();

      return _instance;
    }
	
	public synchronized void init(String cacheDir)
	{
		if(!_initSuccess)
		{
			_cacheDir = cacheDir;
			_tfUnits = loadFont(unitFontName);
			_tfSP = loadFont(spFontName);
			_tfTG = loadFont(tgFontName);
			if( _tfUnits != null && _tfSP != null && _tfTG != null)
				_initSuccess = true;
			else
				throw new Error("FontManager:  failed to load font files using " + cacheDir + " as the cache directory.");
		}
	}
	
	public Typeface getTypeface(String fontName)
	{
		if(_initSuccess)
		{
			if(fontName == FONT_UNIT)
			{
				return _tfUnits;
			}
			else if(fontName == FONT_SPTG)
			{
				return _tfSP;
			}
			else if(fontName == FONT_MPTG)
			{
				return _tfTG;
			}
			else
				return null;
		}
		throw new Error("FontManager:  Must call \".init(String cacheDir)\" before using");
	}
	
	private Typeface loadFont(String fontName)
	{
		String fontFolder = "res/raw/";
		Typeface tf = null;
		InputStream is = null;
		try
		{
			
			//InputStream fontStream = this.getClass().getClassLoader().getResourceAsStream("assets/fonts/unitfonts.ttf");
			is = this.getClass().getClassLoader().getResourceAsStream(fontFolder + fontName);
			
			if(is != null)
			{
				//Log.wtf("SPR.getFont", "we have input stream");
			}
			else
			{
				//Log.wtf(TAG, "Fail to load font file at: " + fontFolder + fontName);
				return null;
			}
					
			/////////////////////
			
			//String sdState = Environment.getExternalStorageState();
			//String cacheDir = Environment.getDownloadCacheDirectory().getAbsolutePath();
			String cacheDir = _cacheDir;
			//Log.wtf("SPR.getFont", "Cache Directory: " + cacheDir);


			String path = cacheDir + "/secrenderer";
			//Log.wtf("SPR.getFont", "Cache Directory: " + path);
			File f = new File(path);
			
			if(f.exists()==false)
			{
				//make directory
				if(f.mkdirs()==false)
				{
					Log.wtf("SPR.getFont", "make temp SD dir \"" + path + "\" fail");
				}
				else
				{
					//Log.wtf("SPR.getFont", "make temp SD dir success");
				}
			}
			else
			{
				//Log.wtf("SPR.getFont", "temp SD dir exists");
			}
			
			String outPath = path + "/secraw.dat";
			try
			{
				byte[] buffer = new byte[is.available()];
				BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(outPath));
				
				int l = 0;
				while((l=is.read(buffer))>0)
				{
					bos.write(buffer,0,l);
				}
				bos.close();
				//Log.wtf("SPR.getFont", "saved temp file");
				bos = null;
				tf = Typeface.createFromFile(outPath);
				if(tf!=null)
				{
					//Log.wtf("SPR.getFont", "created TF from temp file");
				}
				File f2 = new File(outPath);
				if(f2.delete())
				{
					//Log.wtf("SPR.getFont", "deleted temp file");
				}
			}
			catch(IOException ioe)
			{
				return null;
			}
			catch(Exception exc)
			{
				return null;
			}
		
			/*
			//tf = Typeface.createFromFile("assets/fonts/unitfonts.ttf");
			tf = Typeface.createFromFile("res/raw/unitfont.ttf");
			//AssetManager am = _context.getAssets();
			//Typeface tf = Typeface.createFromAsset(am, "fonts/unitfonts.ttf");
			*/
			
			if(tf != null)
			{
				//Log.wtf("SPR.getFont", "we have typeface");
			}
			else
				Log.wtf("SPR.getFont", "NO TYPEFACE");
			//*/
			
			return tf;
		}
		catch(Exception exc)
		{
			Log.wtf("SPR.getFont", exc.getMessage(),exc);
			if(exc != null)
				ErrorLogger.LogException("SPR", "getFont", exc);
		}
		return tf;
	}//*/

}
