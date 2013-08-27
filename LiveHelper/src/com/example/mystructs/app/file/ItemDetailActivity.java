package com.example.mystructs.app.file;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.example.mystructs.R;
import com.example.mystructs.app.book.BookItemAdapter;
import com.example.mystructs.app.book.PullDownView;
import com.example.mystructs.app.book.PullDownView.OnPullDownListener;
import com.example.mystructs.app.cart.DialogCreate;
import com.example.mystructs.app.cart.ProgressBarDialog;
import com.example.mystructs.util.constant.Constant;
import com.example.mystructs.util.constant.Constant.DB;
@SuppressLint("NewApi")
public class ItemDetailActivity extends Activity implements OnPullDownListener {
	private static final int WHAT_DID_LOAD_DATA = 0;
	private static final int WHAT_DID_REFRESH = 1;
	private static final int WHAT_DID_MORE = 2;
	private ListView mListView;
    private BookItemAdapter bAdapter;
    private List<Map<String, Object>> maps=new ArrayList<Map<String,Object>>();
    private ProgressBarDialog progressDialog = null;  
    private Cursor cursor;
    private  String sql="select * from "+DB.Table_Name1+" where "+DB._ID+" in ";
    
    
	private static String[] projection=new String[]{};
	
	private PullDownView mPullDownView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.book);
		/*
		 * 1.使用PullDownView
		 * 2.设置OnPullDownListener
		 * 3.从mPullDownView里面获取ListView
		 */
		
		mPullDownView = (PullDownView) findViewById(R.id.pull_down_view);
		mPullDownView.setOnPullDownListener(this);
		mListView = mPullDownView.getListView();
		bAdapter=new BookItemAdapter(this, maps);
		mListView.setAdapter(bAdapter);
		mPullDownView.enableAutoFetchMore(false, 1);
		startProgressDialog();
		sql+=" ("+this.getIntent().getStringExtra("b_id")+" )";
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
				
				Message msg = mUIHandler.obtainMessage(WHAT_DID_LOAD_DATA);
				msg.obj = maps;
				msg.sendToTarget();
			}
		}).start();
	}
	
	
	@Override
	public void onRefresh() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				Message msg = mUIHandler.obtainMessage(WHAT_DID_REFRESH);
				msg.sendToTarget();
			}
		}).start();
	}

	@Override
	public void onMore() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				Message msg = mUIHandler.obtainMessage(WHAT_DID_MORE);
				msg.sendToTarget();
			}
		}).start();
	}

	private Handler mUIHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			
			switch (msg.what) {
			case WHAT_DID_LOAD_DATA:{
				if(!maps.isEmpty()){
					bAdapter.setList(maps);
					bAdapter.notifyDataSetChanged();
				}
				// 诉它数据加载完毕;
				mPullDownView.notifyDidLoad();
				break;
			}
			case WHAT_DID_REFRESH :{
				bAdapter.notifyDataSetChanged();
				// 告诉它更新完毕
				mPullDownView.notifyDidRefresh();
				break;
			}

			case WHAT_DID_MORE:{
				bAdapter.notifyDataSetChanged();
				// 告诉它获取更多完毕
				mPullDownView.notifyDidMore();
				break;
			}
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
