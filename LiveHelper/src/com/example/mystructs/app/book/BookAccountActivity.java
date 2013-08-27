package com.example.mystructs.app.book;

import java.text.Bidi;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.mystructs.R;
import com.example.mystructs.app.MyTabHost;
import com.example.mystructs.app.cart.ProgressBarDialog;
import com.example.mystructs.util.constant.Constant;
import com.example.mystructs.util.constant.Constant.DB;
import com.example.mystructs.util.mention.MyToast;

import android.R.integer;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class BookAccountActivity extends Activity implements OnClickListener {
	private ProgressBarDialog progressDialog = null;
	private Cursor cursor;
	private static String[] projection=new String[]{};
	private String sql;
	private List<Map<String, Object>> maps=new ArrayList<Map<String,Object>>();
	private TextView nomal_lone;
	private TextView nomal_more;
	private TextView special_lone;
	private TextView special_more;
	private TextView account_lone;
	private TextView account_more;
	private TextView count_lone;
	private TextView count_more;
	private Button cancel_bt;
	private Button change_bt;
	private Button save_bt;
	
	private long nomal_lone_tag;
	private long nomal_more_tag;
	private long special_lone_tag;
	private long special_more_tag;
	private long account_lone_tag;
	private long account_more_tag;
	private long count_lone_tag;
	private long count_more_tag;
	
	private AlertDialog.Builder dBuilder;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.book_account);
		ActionBar actionBar=getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.show();
		nomal_lone=(TextView)findViewById(R.id.consump_nomal_lone);
		nomal_more=(TextView)findViewById(R.id.consump_nomal_more);
		special_lone=(TextView)findViewById(R.id.consump_special_lone);
		special_more=(TextView)findViewById(R.id.consump_special_more);
		account_lone=(TextView)findViewById(R.id.consump_account_lone);
		account_more=(TextView)findViewById(R.id.consump_account_more);
		count_lone=(TextView)findViewById(R.id.consump_count_lone);
		count_more=(TextView)findViewById(R.id.consump_count_more);
		cancel_bt=(Button)findViewById(R.id.consump_bt_cancel);
		
		change_bt=(Button)findViewById(R.id.consump_bt_change);
		save_bt=(Button)findViewById(R.id.consump_bt_save);
		cancel_bt.setOnClickListener(this);
		change_bt.setOnClickListener(this);
		save_bt.setOnClickListener(this);
		sql=this.getIntent().getStringExtra("sql");
		startProgressDialog();
		loadData(sql);
	}
	
	private void loadData(String sql){
		cursor=this.getContentResolver().query(Constant.CONTENT_URI_Consump, projection, sql, null, null);
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
					Map<String, Object> map=new HashMap<String, Object>();
					map.put("id", cursor.getInt(0));
					map.put(DB.Consump_Column1,cursor.getInt(1));
					map.put(DB.Consump_Column2,cursor.getString(2));
					map.put(DB.Consump_Column3,cursor.getInt(3));
					map.put(DB.Consump_Column4,cursor.getString(4));
					map.put(DB.Consump_Column5,cursor.getInt(5));
					map.put(DB.Consump_Column6,cursor.getInt(6));
					maps.add(map);
					cursor.moveToNext();
				}
				
				Message msg = mUIHandler.obtainMessage(Constant.DIALOG_ID_0);
				msg.obj = maps;
				msg.sendToTarget();
			}
		}).start();
	}
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
    
    private Handler mUIHandler=new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {
			case Constant.DIALOG_ID_0:
				CountANDSetData();
				break;

			default:
				break;
			}
			stopProgressDialog();
		}
    };
    
    private void CountANDSetData(){
    	for (int i = 0; i < maps.size(); i++) {
			if ((Integer)maps.get(i).get(DB.Consump_Column6)==1) {
				if ((Integer)maps.get(i).get(DB.Consump_Column1)==1) {
					account_more_tag+=(Integer)maps.get(i).get(DB.Consump_Column3);
				}else {
					account_lone_tag+=(Integer)maps.get(i).get(DB.Consump_Column3);
				}
			} else if ((Integer)maps.get(i).get(DB.Consump_Column5)==1) {
				if ((Integer)maps.get(i).get(DB.Consump_Column1)==1) {
					special_more_tag+=(Integer)maps.get(i).get(DB.Consump_Column3);
				}else {
					special_lone_tag+=(Integer)maps.get(i).get(DB.Consump_Column3);
				}
			} else if ((Integer)maps.get(i).get(DB.Consump_Column1)==1) {
				nomal_more_tag+=(Integer)maps.get(i).get(DB.Consump_Column3);
			}else {
				nomal_lone_tag+=(Integer)maps.get(i).get(DB.Consump_Column3);
			}
		}
    	count_lone_tag=nomal_lone_tag+special_lone_tag+account_lone_tag;
    	count_more_tag=nomal_more_tag+special_more_tag+account_more_tag;
    	
    	nomal_lone.setText(nomal_lone_tag+"");
    	nomal_more.setText(nomal_more_tag+"");
    	special_lone.setText(special_lone_tag+"");
    	special_more.setText(special_more_tag+"");
    	account_lone.setText(account_lone_tag+"");
    	account_more.setText(account_more_tag+"");
    	count_lone.setText(count_lone_tag+"");
    	count_more.setText(count_more_tag+"");
    }

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.consump_bt_cancel:
			finish();
			break;
		case R.id.consump_bt_change:
			String b_id="";
			for (int i = 0; i < maps.size(); i++) {
				b_id+=""+maps.get(i).get("id")+",";
			}
			ContentValues values=new ContentValues();
			values.put(DB.Consump_Column6, 1);
			int i=updateDB(values, b_id.substring(0, b_id.length()-1));
			if (i>0) {
				MyToast.shortToast(BookAccountActivity.this, "修改成功");
				change_bt.setEnabled(false);
			}else {
				MyToast.shortToast(BookAccountActivity.this, "修改失败");
			}
			break;
		case R.id.consump_bt_save:
			insertDialog();
			break;

		default:
			break;
		}
	}
	
	private void insertDialog(){
		dBuilder=new AlertDialog.Builder(this);
		final EditText titler=new EditText(this);
		titler.setBackgroundResource(R.drawable.edittextbackground);
		dBuilder.setTitle("请输入标题");
		dBuilder.setView(titler);
		dBuilder.setPositiveButton("确定", new android.content.DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				if (!titler.getText().toString().trim().equals("")) {
					SimpleDateFormat dFormat=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
					Date date1=new Date(System.currentTimeMillis());
					String dateTag = dFormat.format(date1);
					String contain_id="";
					for (int i = 0; i < maps.size(); i++) {
						contain_id+=maps.get(i).get("id")+",";
					}
					ContentValues values=new ContentValues();
					values.put(DB.Bill_Column1, titler.getText().toString());
					values.put(DB.Bill_Column2, dateTag);
					values.put(DB.Bill_Column3, nomal_lone_tag);
					values.put(DB.Bill_Column4, nomal_more_tag);
					values.put(DB.Bill_Column5, special_lone_tag);
					values.put(DB.Bill_Column6, special_more_tag);
					values.put(DB.Bill_Column7, account_lone_tag);
					values.put(DB.Bill_Column8, account_more_tag);
					values.put(DB.Bill_Column9, count_lone_tag);
					values.put(DB.Bill_Column10, count_more_tag);
					values.put(DB.Bill_Column11, contain_id.substring(0, contain_id.length()-1));
					int i=insertDB(values);
					if (i>0) {
						MyToast.shortToast(BookAccountActivity.this, "保存成功！");
						save_bt.setEnabled(false);
						cancel_bt.setEnabled(false);
					}else {
						MyToast.shortToast(BookAccountActivity.this, "保存失败！");
					}
				}else {
					MyToast.shortToast(BookAccountActivity.this, "标题不为空");
					return;
				}
			}
		});
		dBuilder.setNegativeButton("取消", new android.content.DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				
			}
		});
		dBuilder.create().show();
	}
	
	private int insertDB(ContentValues cValues){
		Uri uri=this.getContentResolver().insert(Constant.CONTENT_UTI_Bill, cValues);
		int i=Integer.parseInt(uri.getPathSegments().get(1));
		return i;
	}
    
	private int updateDB(ContentValues values,String arg0){
		int i =this.getContentResolver().update(Constant.CONTENT_URI_Consump, values, arg0, null);
		return i;
	}
}
