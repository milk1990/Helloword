package com.example.mystructs.imgcache;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.support.v4.util.LruCache;
import android.util.Log;
/**
 * 建立外存缓存机制
 *@author milk
 *@date 2013.7.25
 * */
public class ExternalBitmapCache {
	
	private static final ExternalBitmapCache exterCache=new ExternalBitmapCache();
	/**
	 * 设置外部缓存 20M
	 * */
	private static final int MAX_CACHE_SIZE = 20 * 1024 * 1024;
	/**
	 * 缓存目录文件
	 * */
	private File mCacheDir =null;
	
	public static ExternalBitmapCache getInstance(){
		return exterCache;
	}
	/**
	 * 获取缓存目录文件
	 * @param context
	 * */
	private  void setCacheDir(Context context){
		mCacheDir= context.getCacheDir();
	}
	
	
	/**
	 * 缓存内存 初始化
	 * */
	private final LruCache<String, Long> sFileCache = new LruCache<String, Long>(MAX_CACHE_SIZE){  
		@Override  
		public int sizeOf(String key, Long value){  
			return value.intValue();  
		}  
		@Override  
		protected void entryRemoved(boolean evicted, String key, Long oldValue, Long newValue){  
			try {
				File file = getFile(key);
				if(file != null)  
					file.delete();  
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}  
		}  
	};
	/**
	 * 获取图片文件
	 * @throws FileNotFoundException  文件未找到Exception
	 * @param fileName   文件名称
	 * @return File     文件
	 * */
	private File getFile(String fileName) throws FileNotFoundException {  
		File file = new File(mCacheDir, fileName);  
		if(!file.exists() || !file.isFile())  
			throw new FileNotFoundException("文件不存在或有同名文件夹");  
		return file;  
	}  
	/**
	 * 缓存bitmap到外部存储  
	 * @param bitmap  图片
	 * @param key     图片索引名称
	 * @throws IOException   输出流Exception
	 * */
	public boolean putBitmap(String key, Bitmap bitmap) throws IOException{  
		File file = getFile(key);  
		if(file != null){  
			Log.v("tag", "文件已经存在");  
			return true;  
		}  
		FileOutputStream fos = getOutputStream(key);  
		boolean saved = bitmap.compress(CompressFormat.JPEG, 80, fos);  
		fos.flush();  
		fos.close();  
		if(saved){  
			synchronized(sFileCache){  
				sFileCache.put(key, getFile(key).length());  
			}  
			return true;   
		}  
		return false;  
	}  
	/**
	 * 根据key获取文件的OutputStream
	 * @param key  图片索引名称
	 * @return  FileOutputStream   文件输出流
	 * @throws FileNotFoundException  文件未找到Exception
	 * */
	private FileOutputStream getOutputStream(String key) throws FileNotFoundException{  
		if(mCacheDir == null)  
			return null;  
		FileOutputStream fos = new FileOutputStream(mCacheDir.getAbsolutePath() + File.separator + key);  
		return fos;  
	}
	/**
	 * 初始化夹在图片选项
	 * */
	private static BitmapFactory.Options sBitmapOptions;  
	static {  
		sBitmapOptions = new BitmapFactory.Options();  
		sBitmapOptions.inPurgeable=true; //bitmap can be purged to disk  
	}
	/**
	 * 获取bitmap
	 * @param key 图片索引名称
	 * @return Bitmap  图片
	 * @throws FileNotFoundException  文件未找到Exception
	 * */
	public Bitmap getBitmap(String key) throws FileNotFoundException{  
		File bitmapFile = getFile(key);  
		if(bitmapFile != null){  
			Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(bitmapFile), null, sBitmapOptions);  
			if(bitmap != null){  
				//重新将其缓存至硬引用中  
				return bitmap;
			}  
		}
		return null;
	}  
}
