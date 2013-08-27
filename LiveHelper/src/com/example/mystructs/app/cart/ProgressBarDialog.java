package com.example.mystructs.app.cart;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mystructs.R;

public class ProgressBarDialog extends Dialog {
	private Context context = null;  
	private static ProgressBarDialog customProgressDialog = null;  

	public ProgressBarDialog(Context context){  
		super(context);  
		this.context = context;  
	}  

	public ProgressBarDialog(Context context, int theme) {  
		super(context, theme);  
	}  

	public static ProgressBarDialog createDialog(Context context){  
		customProgressDialog = new ProgressBarDialog(context,R.style.CustomProgressDialog);  
		customProgressDialog.setContentView(R.layout.progressbar);  
		customProgressDialog.getWindow().getAttributes().gravity = Gravity.CENTER;  

		return customProgressDialog;  
	}  

	public void onWindowFocusChanged(boolean hasFocus){  

		if (customProgressDialog == null){  
			return;  
		}  

		ImageView imageView = (ImageView) customProgressDialog.findViewById(R.id.loadingImageView);  
		AnimationDrawable animationDrawable = (AnimationDrawable) imageView.getBackground();  
		animationDrawable.start();  
	}  

	/** 
	 *  
	 * [Summary] 
	 *       setTitile 标题 
	 * @param strTitle 
	 * @return 
	 * 
	 */  
	 public ProgressBarDialog setTitile(String strTitle){  
		return customProgressDialog;  
	 }  

	 /** 
	  *  
	  * [Summary] 
	  *       setMessage 提示内容 
	  * @param strMessage 
	  * @return 
	  * 
	  */  
	 public ProgressBarDialog setMessage(String strMessage){  
		 TextView tvMsg = (TextView)customProgressDialog.findViewById(R.id.id_tv_loadingmsg);  

		 if (tvMsg != null){  
			 tvMsg.setText(strMessage);  
		 }  

		 return customProgressDialog;  
	 }  
}
