package com.example.mystructs.util.mention;

import android.content.Context;
import android.widget.Toast;
/**
 * 提示类
 * @author milk
 * @date 2013.7.25
 * */
public class MyToast {
	/**
	 *短提示
	 *@param context
	 *@param message
	 * */
	public  static void shortToast(Context context,String message){
		Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
	}
	/**
	 * 长提示
	 * @param context
	 * @param message
	 * */
	public static void LongToast(Context context,String message){
		Toast.makeText(context, message, Toast.LENGTH_LONG).show();
	}
}
