package com.example.mystructs.util.versionupdate;


import com.example.mystructs.R;

import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
/**
 * @author milk
 * @date 2013.7.24
 * @extends
 * 获取 版本信息
 * */
public class GetConfig {
	public static final String UPDATE_SERVER = "http://10.20.147.117/jtapp12/";
    public static final String UPDATE_APKNAME = "jtapp-12-updateapksamples.apk";
    public static final String UPDATE_VERJSON = "ver.json";
    public static final String UPDATE_SAVENAME = "updateapksamples.apk";
	
	
	/**
	 * @param Context
	 * 获取版本号
	 * */
	public static int getVersionCode(Context context){
		int versionCode =-1;
		try {
			versionCode=context.getPackageManager().getPackageInfo("", 0).versionCode;
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return versionCode;
	}
	
	/**
	 * @param Context
	 * 获取版本名称
	 * */
	public  static String getVersionName(Context context){
		String versionName=null;
		try {
			versionName=context.getPackageManager().getPackageInfo("", 0).versionName;
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return versionName;
	}
	/**
	 * @param Context
	 * */
	public  static String getVerName(Context context){
		String verName=context.getResources().getText(R.string.app_name).toString();
		return verName;
	}
}
