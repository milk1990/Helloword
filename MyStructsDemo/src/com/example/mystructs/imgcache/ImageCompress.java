package com.example.mystructs.imgcache;

import java.io.ByteArrayOutputStream;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

public  class ImageCompress {
	
	/**
	 * @method calculateInSampleSize
	 * @param options
	 * @param reqwidth
	 * @param repHeight
	 * @description 获取图片的缩放比
	 * */
	public static int calculateInSampleSize(BitmapFactory.Options options,int reqWidth, int reqHeight) {
	    final int height = options.outHeight;
	    final int width = options.outWidth;
	    int inSampleSize = 1;

	    if (height > reqHeight || width > reqWidth) {
	             final int heightRatio = Math.round((float) height/ (float) reqHeight);
	             final int widthRatio = Math.round((float) width / (float) reqWidth);
	             inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
	    }
	        return inSampleSize;
	}
	
	/**
	 * @method getSmallBitmap
	 * @param filePath
	 * @description 根据缩放比 对图片进行缩小 并返回图片
	 * */ 
	public static Bitmap getSmallBitmap(String filePath) {
	        final BitmapFactory.Options options = new BitmapFactory.Options();
	        options.inJustDecodeBounds = true;
	        BitmapFactory.decodeFile(filePath, options);

	        // Calculate inSampleSize
	    options.inSampleSize = calculateInSampleSize(options, 480, 800);

	        // Decode bitmap with inSampleSize set
	    options.inJustDecodeBounds = false;

	    return BitmapFactory.decodeFile(filePath, options);
	    }
	
	/**
	 * @method 
	 * @param filePath
	 * @description 根据缩放比 缩小图片 并返回 图片字符
	 * */
	public static String bitmapToString(String filePath) {

	        Bitmap bm = getSmallBitmap(filePath);
	        ByteArrayOutputStream baos = new ByteArrayOutputStream();
	        bm.compress(Bitmap.CompressFormat.JPEG, 40, baos);
	        byte[] b = baos.toByteArray();
	        return Base64.encodeToString(b, Base64.DEFAULT);
	    }
}
