package com.example.mystructs.imgcache;

import java.lang.ref.SoftReference;
import java.util.LinkedHashMap;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;
import android.util.Log;

/**
 * 建立内存缓存机制
 * @author milk
 * @date 2013.7.25
 * */
public class MemoryBitmapCache {
	
	private static final MemoryBitmapCache memoryCache=new MemoryBitmapCache();
	/**
	 *开辟8M硬缓存空间
	 **/
	private final int hardCachedSize = 8*1024*1024;     
	/**
	 * 软引用  大小空间
	 * */
	private static final int SOFT_CACHE_CAPACITY = 40;  
	
	private static MemoryBitmapCache getInstance(){
		return memoryCache;
	}
	/**
	 * hard cache  初始化
	 * */
	private final LruCache<String, Bitmap> sHardBitmapCache = new LruCache<String, Bitmap>(hardCachedSize){  
		@Override  
		public int sizeOf(String key, Bitmap value){  
			return value.getRowBytes() * value.getHeight();  
		}  
		/**
		 * 硬引用缓存区满，将一个最不经常使用的oldvalue推入到软引用缓存区  
		 * */
		@Override  
		protected void entryRemoved(boolean evicted, String key, Bitmap oldValue, Bitmap newValue){  
			Log.v("tag", "hard cache is full , push to soft cache");  
			sSoftBitmapCache.put(key, new SoftReference<Bitmap>(oldValue));  
		}  
	};  
	/**
	 *软引用 初始化 
	 * */
	private final static LinkedHashMap<String, SoftReference<Bitmap>> sSoftBitmapCache =   
			new  LinkedHashMap<String, SoftReference<Bitmap>>(SOFT_CACHE_CAPACITY, 0.75f, true){  
		@Override  
		public SoftReference<Bitmap> put(String key, SoftReference<Bitmap> value){  
			return super.put(key, value);  
		}

		@Override
		protected boolean removeEldestEntry(
				Entry<String, SoftReference<Bitmap>> eldest) {
			// TODO Auto-generated method stub
			if(size() > SOFT_CACHE_CAPACITY){  
				Log.v("tag", "Soft Reference limit , purge one");  
				return true;  
			}  
			return super.removeEldestEntry(eldest);
		}  
		
	};
	/**
	 * 缓存bitmap
	 * @param bitmap 图片
	 * @param key  图片索引名称
	 * */
	public boolean putBitmap(String key, Bitmap bitmap){  
		if(bitmap != null){  
			synchronized(sHardBitmapCache){  
				sHardBitmapCache.put(key, bitmap);  
			}  
			return true;  
		}         
		return false;  
	}  
	/**
	 * 从缓存中获取bitmap  
	 * @param key  图片索引名称
	 * @return BitMap  图片
	 * */
	public Bitmap getBitmap(String key){  
		synchronized(sHardBitmapCache){  
			final Bitmap bitmap = sHardBitmapCache.get(key);  
			if(bitmap != null)  
				return bitmap;  
		}  
		//硬引用缓存区间中读取失败，从软引用缓存区间读取  
		synchronized(sSoftBitmapCache){  
			SoftReference<Bitmap> bitmapReference = sSoftBitmapCache.get(key);  
			if(bitmapReference != null){  
				final Bitmap bitmap2 = bitmapReference.get();  
				if(bitmap2 != null)  
					return bitmap2;  
				else{  
					Log.v("tag", "soft reference 已经被回收");  
					sSoftBitmapCache.remove(key);  
				}  
			}  
		}  
		return null;  
	}  

}
