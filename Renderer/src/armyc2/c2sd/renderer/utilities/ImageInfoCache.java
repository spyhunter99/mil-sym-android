package armyc2.c2sd.renderer.utilities;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Comparator;

public class ImageInfoCache {

	 private Map<String, ImageInfoCacheItem> _cache = new HashMap<String, ImageInfoCacheItem>();
	 private int _size=50;
	 private Comparator<ImageInfoCacheItem> _comp = new Comparator<ImageInfoCacheItem>(){
		 @Override
			public int compare(ImageInfoCacheItem lhs, ImageInfoCacheItem rhs) {
				
				if(lhs._lastAccess < rhs._lastAccess)
					return -1;
				else if(lhs._lastAccess > rhs._lastAccess)
					return 1;
				else
					return 0;

			}
	 };
	 
 
	 public ImageInfoCache()
	 {
		 
	 }
	 
	 public ImageInfoCache(int sizeLimit)
	 {
		 _size = sizeLimit;
	 }
	 
	 public boolean containsKey(String key)
	 {
		 boolean temp = false;
		 synchronized(_cache)
		 {
			 temp = _cache.containsKey(key);
		 }
		 return temp;
	 }
	 
	 public void put(String key, ImageInfo value)
	 {
		 if(_cache.containsKey(key)==false)
		 {
			 synchronized(_cache)
			 {
				 _cache.put(key, new ImageInfoCacheItem(key, value));
			 }
			 if(_cache.size() > _size)
			 {
				 //cleanup in worker thread.
				 new Thread(new Runnable(){
					 public void run(){
						 cleanup();
					 }
				 }).start();
				 
				 //cleanup();
			 }
		 }	 
	 }
	 
	 public ImageInfo get(String key)
	 {
		 ImageInfo temp = null;
		 synchronized(_cache)
		 {
			 temp = _cache.get(key).getImageInfo();
		 }
		 return temp;
	 }
	 
	 public void cleanup()
	 {
		 List<ImageInfoCacheItem> list = new ArrayList<ImageInfoCacheItem>(_cache.values());
		 Collections.sort(list, _comp);
		 
		 synchronized (_cache)
		 {
			 int size = list.size();
			 int limit = _size - 5;
			 while(size > limit  && size > 0)
			 {
				 _cache.remove(list.get(0));
				 list.remove(0);
				 size--;		 
			 }
		 }
		 list.clear();
		 list = null;
		 
	 }
	 
	 public static String makeKey(String symbolID, int lineColor, int fillColor, int size, boolean keepUnitRatio, int symStd)
	 {
		 String key = symbolID + String.valueOf(lineColor) + String.valueOf(fillColor) + String.valueOf(size) + String.valueOf(keepUnitRatio) + String.valueOf(symStd);
		 return key;
	 }
	 
	 private class ImageInfoCacheItem
	 {
		 private ImageInfo _ii = null;
		 private String _key = null;
		 private long _accessCount = 0;
		 private long _lastAccess = 0;
		 public ImageInfoCacheItem(String key, ImageInfo ii)
		 {
	 		_key = key;
 			_ii = ii; 
		 }
	 	
		 public String getKey()
 		 { 
		  	return _key;
		 }
	 	
	 	 public ImageInfo getImageInfo()
 		 { 
		    _accessCount++;
	 	 	_lastAccess = new Date().getTime();
			return _ii;
 		 } 
	 }
	 
	 private class iiciComparator implements Comparator
	 {

		@Override
		public int compare(Object lhs, Object rhs) {
			// TODO Auto-generated method stub
			ImageInfoCacheItem iic1 = (ImageInfoCacheItem)lhs;
			ImageInfoCacheItem iic2 = (ImageInfoCacheItem)rhs;
			
			if(iic1._lastAccess < iic2._lastAccess)
				return -1;
			else if(iic1._lastAccess > iic2._lastAccess)
				return 1;
			else
				return 0;

		}
		 
	 }
	 
}

