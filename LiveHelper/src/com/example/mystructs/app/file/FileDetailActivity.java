package com.example.mystructs.app.file;


import com.example.mystructs.R;
import com.example.mystructs.app.cart.ProgressBarDialog;
import com.example.mystructs.util.constant.Constant;
import com.example.mystructs.util.constant.Constant.DB;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class FileDetailActivity extends Activity {
	private ProgressBarDialog progressDialog = null;
	private Cursor cursor;
	private String[] projection=new String[]{};
	private String sql=" select * from "+DB.Table_Name3+" where "+DB._ID+" =";
	
	private TextView titler;
	private TextView nomal_lone;
	private TextView nomal_more;
	private TextView special_lone;
	private TextView special_more;
	private TextView account_lone;
	private TextView account_more;
	private TextView count_lone;
	private TextView count_more;
	private Button detail_bt;
	
	private int id;
	private String titler_tag;
	private String date_tag;
	private String b_id_tag;
	private long nomal_lone_tag;
	private long nomal_more_tag;
	private long special_lone_tag;
	private long special_more_tag;
	private long account_lone_tag;
	private long account_more_tag;
	private long count_lone_tag;
	private long count_more_tag;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.file_detail);
		titler=(TextView)findViewById(R.id.file_detail_titler);
		nomal_lone=(TextView)findViewById(R.id.file_detail_nomal_lone);
		nomal_more=(TextView)findViewById(R.id.file_detail_nomal_more);
		special_lone=(TextView)findViewById(R.id.file_detail_special_lone);
		special_more=(TextView)findViewById(R.id.file_detail_special_more);
		account_lone=(TextView)findViewById(R.id.file_detail_account_lone);
		account_more=(TextView)findViewById(R.id.file_detail_account_more);
		count_lone=(TextView)findViewById(R.id.file_detail_count_lone);
		count_more=(TextView)findViewById(R.id.file_detail_count_more);
		detail_bt=(Button)findViewById(R.id.file_detail_bt_detail);
		sql+=this.getIntent().getIntExtra("id", 1);
		startProgressDialog();
		detail_bt.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent();
				intent.setClass(FileDetailActivity.this, ItemDetailActivity.class);
				intent.putExtra("b_id", b_id_tag);
				startActivity(intent);
			}
		});
		loadData(sql);
	}
	
	private void loadData(String sql){
		cursor=this.getContentResolver().query(Constant.CONTENT_UTI_Bill, projection, sql, null, null);
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				cursor.moveToFirst();
				while (!cursor.isAfterLast()) {
					id=cursor.getInt(0);
					titler_tag=cursor.getString(1);
					date_tag=cursor.getString(2);
					nomal_lone_tag=cursor.getInt(3);
					nomal_more_tag=cursor.getInt(4);
					special_lone_tag=cursor.getInt(5);
					special_more_tag=cursor.getInt(6);
					account_lone_tag=cursor.getInt(7);
					account_more_tag=cursor.getInt(8);
					count_lone_tag=cursor.getInt(9);
					count_more_tag=cursor.getInt(10);
					b_id_tag=cursor.getString(11);
					cursor.moveToNext();
				}
				
				Message msg = mHandler.obtainMessage(Constant.DIALOG_ID_0);
				msg.sendToTarget();
			}
		}).start();
	}
	private Handler mHandler=new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {
			case Constant.DIALOG_ID_0:
				titler.setText(titler_tag);
				nomal_lone.setText(nomal_lone_tag+"");
				nomal_more.setText(nomal_more_tag+"");
				special_lone.setText(special_lone_tag+"");
				special_more.setText(special_more_tag+"");
				account_lone.setText(account_lone_tag+"");
				account_more.setText(account_more_tag+"");
				count_lone.setText(count_lone_tag+"");
				count_more.setText(count_more_tag+"");
				break;

			default:
				break;
			}
			stopProgressDialog();
		}
		
	};
	
	private void startProgressDialog(){  
        if (progressDialog == null){  
            progressDialog = ProgressBarDialog.createDialog(this);  
            progressDialog.setMessage("正在加载中...");  
        }  
          
        progressDialog.show();  
    }  
      
    private void stopProgressDialog(){  
        if (progressDialog != null){  
            progressDialog.dismiss();  
            progressDialog = null;  
        }  
    }
}
