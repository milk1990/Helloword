package com.example.mystructs.imgcache;

import java.sql.Date;


public class GenerateBitmapKey {
	/**
	 * ��ȡΨһkey
	 * @param fileId �ļ���ID
	 * @param width  �ļ��Ŀ�
	 * @param height �ļ��ĸ�
	 * @return String  keyֵ
	 * */
	private static String generateKey(String fileId, int width, int height) {         
        String ret = fileId + "_" + Integer.toString(width) + "x" + Integer.toString(height);  
        return ret;  
    }
	/**
	 * ��ȡΨһkey
	 * @param width  �ļ��Ŀ�
	 * @param height �ļ��ĸ�
	 * @return String  keyֵ
	 * */
	private static String generateKey(int width, int height){
		return null;
	}
	/**
	 * ��ȡΨһkey
	 * @param height �ļ��ĸ�
	 * @return String  keyֵ
	 * */
	private static String generateKey(int height){
		return null;
	}
	/**
	 * ��ȡΨһkey
	 * @return String  keyֵ
	 * */
	private static String generateKey(){
		String key=System.currentTimeMillis()+"";
		return key;
	}
}
