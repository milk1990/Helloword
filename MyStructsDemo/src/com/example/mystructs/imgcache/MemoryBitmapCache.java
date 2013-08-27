package com.example.mystructs.imgcache;

import java.lang.ref.SoftReference;
import java.util.LinkedHashMap;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;
import android.util.Log;

/**
 * �����ڴ滺�����
 * @author milk
 * @date 2013.7.25
 * */
public class MemoryBitmapCache {
	
	private static final MemoryBitmapCache memoryCache=new MemoryBitmapCache();
	/**
	 *����8MӲ����ռ�
	 **/
	private final int hardCachedSize = 8*1024*1024;     
	/**
	 * ������  ��С�ռ�
	 * */
	private static final int SOFT_CACHE_CAPACITY = 40;  
	
	private static MemoryBitmapCache getInstance(){
		return memoryCache;
	}
	/**
	 * hard cache  ��ʼ��
	 * */
	private final LruCache<String, Bitmap> sHardBitmapCache = new LruCache<String, Bitmap>(hardCachedSize){  
		@Override  
		public int sizeOf(String key, Bitmap value){  
			return value.getRowBytes() * value.getHeight();  
		}  
		/**
		 * Ӳ���û�����������һ�������ʹ�õ�oldvalue���뵽�����û�����  
		 * */
		@Override  
		protected void entryRemoved(boolean evicted, String key, Bitmap oldValue, Bitmap newValue){  
			Log.v("tag", "hard cache is full , push to soft cache");  
			sSoftBitmapCache.put(key, new SoftReference<Bitmap>(oldValue));  
		}  
	};  
	/**
	 *������ ��ʼ�� 
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
	 * ����bitmap
	 * @param bitmap ͼƬ
	 * @param key  ͼƬ��������
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
	 * �ӻ����л�ȡbitmap  
	 * @param key  ͼƬ��������
	 * @return BitMap  ͼƬ
	 * */
	public Bitmap getBitmap(String key){  
		synchronized(sHardBitmapCache){  
			final Bitmap bitmap = sHardBitmapCache.get(key);  
			if(bitmap != null)  
				return bitmap;  
		}  
		//Ӳ���û��������ж�ȡʧ�ܣ��������û��������ȡ  
		synchronized(sSoftBitmapCache){  
			SoftReference<Bitmap> bitmapReference = sSoftBitmapCache.get(key);  
			if(bitmapReference != null){  
				final Bitmap bitmap2 = bitmapReference.get();  
				if(bitmap2 != null)  
					return bitmap2;  
				else{  
					Log.v("tag", "soft reference �Ѿ�������");  
					sSoftBitmapCache.remove(key);  
				}  
			}  
		}  
		return null;  
	}  

}
