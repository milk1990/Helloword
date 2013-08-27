package com.example.mystructs.imgcache;

import java.sql.Date;


public class GenerateBitmapKey {
	/**
	 * 获取唯一key
	 * @param fileId 文件的ID
	 * @param width  文件的宽
	 * @param height 文件的高
	 * @return String  key值
	 * */
	private static String generateKey(String fileId, int width, int height) {         
        String ret = fileId + "_" + Integer.toString(width) + "x" + Integer.toString(height);  
        return ret;  
    }
	/**
	 * 获取唯一key
	 * @param width  文件的宽
	 * @param height 文件的高
	 * @return String  key值
	 * */
	private static String generateKey(int width, int height){
		return null;
	}
	/**
	 * 获取唯一key
	 * @param height 文件的高
	 * @return String  key值
	 * */
	private static String generateKey(int height){
		return null;
	}
	/**
	 * 获取唯一key
	 * @return String  key值
	 * */
	private static String generateKey(){
		String key=System.currentTimeMillis()+"";
		return key;
	}
}
