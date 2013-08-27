package com.example.mystructs.app.book;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.mystructs.R;
import com.example.mystructs.app.book.PullDownView.OnPullDownListener;
import com.example.mystructs.app.cart.DialogCallBack;
import com.example.mystructs.app.cart.DialogCreate;
import com.example.mystructs.app.cart.ProgressBarDialog;
import com.example.mystructs.util.constant.Constant;
import com.example.mystructs.util.constant.Constant.DB;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

@SuppressLint("NewApi")
public class BookActivity extends Activity implements OnPullDownListener {

	private static final int WHAT_DID_LOAD_DATA = 0;
	private static final int WHAT_DID_REFRESH = 1;
	private static final int WHAT_DID_MORE = 2;
	private ListView mListView;
    private BookItemAdapter bAdapter;
    private List<Map<String, Object>> maps=new ArrayList<Map<String,Object>>();
    private Cursor cursor;
    private  String sql="select * from "+DB.Table_Name1+" where "+DB.Consump_Column6+"=0 "+" order by "+DB.Consump_Column4+" desc";
    private ProgressBarDialog progressDialog = null;  
    private  String search="";
    private DialogCreate dialogCreate;
	
    private static String[] projection=new String[]{DB.Consump_Column1,DB.Consump_Column2,DB.Consump_Column3,DB.Consump_Column4,DB.Consump_Column5,DB.Consump_Column6,};
	
	
	private PullDownView mPullDownView;
	private LinearLayout layout;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.book);
		/*
		 * 1.使用PullDownView
		 * 2.设置OnPullDownListener
		 * 3.从mPullDownView里面获取ListView
		 */
		layout=(LinearLayout)findViewById(R.id.layout_ll);
		mPullDownView = (PullDownView) findViewById(R.id.pull_down_view);
		mPullDownView.setOnPullDownListener(this);
		mListView = mPullDownView.getListView();
		bAdapter=new BookItemAdapter(this, maps);
		mListView.setAdapter(bAdapter);
		mPullDownView.enableAutoFetchMore(true, 1);
		dialogCreate=new DialogCreate(this, callBack);
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
				if(maps!=null){
					bAdapter.setList(maps);
					bAdapter.notifyDataSetChanged();
				}
				if(maps.size()!=0){
					layout.setVisibility(View.GONE);
				}else {
					layout.setVisibility(View.VISIBLE);
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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub

		// setIcon()方法为菜单设置图标，这里使用的是系统自带的图标，同学们留意一下,以

		menu.add(Menu.NONE, Menu.FIRST + 3, 2, "搜索").setIcon(

				android.R.drawable.ic_menu_help);
		
		menu.add(Menu.NONE, Menu.FIRST + 4, 3, "刷新").setIcon(

				android.R.drawable.ic_menu_revert);

		menu.add(Menu.NONE, Menu.FIRST + 1, 0, "结算").setIcon(

				android.R.drawable.ic_menu_add);


		return super.onCreateOptionsMenu(menu);  
	}
	
	private DialogCallBack callBack=new DialogCallBack() {
		
		@Override
		public void refreshData() {
			// TODO Auto-generated method stub
			maps.clear();
			search=dialogCreate.getSql();
			startProgressDialog();
			loadData(search);
		}
	};
	
	
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case Menu.FIRST+1:
			new AlertDialog.Builder(this).setMessage("使用当前数据生成账单？")
			  .setNegativeButton("取消", new OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
				}
			}).setPositiveButton("确定", new OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					Intent intent=new Intent();
				    intent.setClass(BookActivity.this, BookAccountActivity.class);
				    if (search.equals("")) {
						search=sql;
					}
				    intent.putExtra("sql", search);
				    startActivity(intent);
				    overridePendingTransition(R.anim.slide_left_out, R.anim.slide_right_in);
				}
			}).create().show();
			break;
		case Menu.FIRST+2:
			
			break;
		case Menu.FIRST+3:
			dialogCreate.createDialog(Constant.DIALOG_ID_1).show();
			break;
		case Menu.FIRST+4:
			search="";
			maps.clear();
		    startProgressDialog();
			loadData(sql);
			break;

		default:
			break;
		}
		return super.onOptionsItemSelected(item);
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
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if (resultCode==Constant.DIALOG_ID_0) {
			maps.clear();
			startProgressDialog();
			loadData(sql);
		}
	}

}
