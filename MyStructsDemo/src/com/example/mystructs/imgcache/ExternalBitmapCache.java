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
 * ������滺�����
 *@author milk
 *@date 2013.7.25
 * */
public class ExternalBitmapCache {
	
	private static final ExternalBitmapCache exterCache=new ExternalBitmapCache();
	/**
	 * �����ⲿ���� 20M
	 * */
	private static final int MAX_CACHE_SIZE = 20 * 1024 * 1024;
	/**
	 * ����Ŀ¼�ļ�
	 * */
	private File mCacheDir =null;
	
	public static ExternalBitmapCache getInstance(){
		return exterCache;
	}
	/**
	 * ��ȡ����Ŀ¼�ļ�
	 * @param context
	 * */
	private  void setCacheDir(Context context){
		mCacheDir= context.getCacheDir();
	}
	
	
	/**
	 * �����ڴ� ��ʼ��
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
	 * ��ȡͼƬ�ļ�
	 * @throws FileNotFoundException  �ļ�δ�ҵ�Exception
	 * @param fileName   �ļ�����
	 * @return File     �ļ�
	 * */
	private File getFile(String fileName) throws FileNotFoundException {  
		File file = new File(mCacheDir, fileName);  
		if(!file.exists() || !file.isFile())  
			throw new FileNotFoundException("�ļ������ڻ���ͬ���ļ���");  
		return file;  
	}  
	/**
	 * ����bitmap���ⲿ�洢  
	 * @param bitmap  ͼƬ
	 * @param key     ͼƬ��������
	 * @throws IOException   �����Exception
	 * */
	public boolean putBitmap(String key, Bitmap bitmap) throws IOException{  
		File file = getFile(key);  
		if(file != null){  
			Log.v("tag", "�ļ��Ѿ�����");  
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
	 * ����key��ȡ�ļ���OutputStream
	 * @param key  ͼƬ��������
	 * @return  FileOutputStream   �ļ������
	 * @throws FileNotFoundException  �ļ�δ�ҵ�Exception
	 * */
	private FileOutputStream getOutputStream(String key) throws FileNotFoundException{  
		if(mCacheDir == null)  
			return null;  
		FileOutputStream fos = new FileOutputStream(mCacheDir.getAbsolutePath() + File.separator + key);  
		return fos;  
	}
	/**
	 * ��ʼ������ͼƬѡ��
	 * */
	private static BitmapFactory.Options sBitmapOptions;  
	static {  
		sBitmapOptions = new BitmapFactory.Options();  
		sBitmapOptions.inPurgeable=true; //bitmap can be purged to disk  
	}
	/**
	 * ��ȡbitmap
	 * @param key ͼƬ��������
	 * @return Bitmap  ͼƬ
	 * @throws FileNotFoundException  �ļ�δ�ҵ�Exception
	 * */
	public Bitmap getBitmap(String key) throws FileNotFoundException{  
		File bitmapFile = getFile(key);  
		if(bitmapFile != null){  
			Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(bitmapFile), null, sBitmapOptions);  
			if(bitmap != null){  
				//���½��仺����Ӳ������  
				return bitmap;
			}  
		}
		return null;
	}  
}
