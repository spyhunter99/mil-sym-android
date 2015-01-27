package armyc2.c2sd.renderer.utilities;

import java.util.Map;
import java.util.LinkedHashMap;

public class ImageInfoCache
{

    private int _size = 50;
    private final Map<String, ImageInfo> _cache = new LinkedHashMap<String, ImageInfo>(16, 0.75f, true)
    {
        /**
         *
         */
        private static final long serialVersionUID = 1L;

        @Override
        protected boolean removeEldestEntry(Map.Entry<String, ImageInfo> eldest)
        {
            boolean ifRemove = this.size() > _size;
            return ifRemove;
        }
    };//new HashMap<String, ImageInfoCacheItem>();

    public ImageInfoCache()
    {

    }

    public ImageInfoCache(int sizeLimit)
    {
        _size = sizeLimit;
    }

    public synchronized void put(String key, ImageInfo value)
    {
        _cache.put(key, value);
    }

    public synchronized ImageInfo get(String key)
    {
    	return _cache.get(key);
    	/*ImageInfo returnVal = _cache.get(key);
    	if(returnVal != null)
    		return returnVal.getLightClone();
    	else
    		return null;*/
    }

    public static String makeKey(String symbolID, int lineColor, int fillColor, int size, boolean keepUnitRatio, int symStd)
    {
        String key = symbolID.substring(0, 10) + String.valueOf(lineColor) + String.valueOf(fillColor) + String.valueOf(size) + String.valueOf(keepUnitRatio) + String.valueOf(symStd);
        return key;
    }
}
