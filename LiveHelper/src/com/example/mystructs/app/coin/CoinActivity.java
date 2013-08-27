package com.example.mystructs.app.coin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.mystructs.R;
import com.example.mystructs.app.book.PullDownView;
import com.example.mystructs.app.book.PullDownView.OnPullDownListener;
import com.example.mystructs.app.cart.CartActivity;
import com.example.mystructs.app.cart.DialogCallBack;
import com.example.mystructs.app.cart.DialogCreate;
import com.example.mystructs.app.cart.ProgressBarDialog;
import com.example.mystructs.util.constant.Constant;
import com.example.mystructs.util.constant.Constant.DB;
import com.example.mystructs.util.mention.MyToast;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class CoinActivity extends Activity implements OnPullDownListener,OnItemClickListener {

	private static final int WHAT_DID_LOAD_DATA = 0;
	private static final int WHAT_DID_REFRESH = 1;
	private static final int WHAT_DID_MORE = 2;
	private ListView mListView;
    private CoinItemAdapter bAdapter;
    private List<Map<String, Object>> maps=new ArrayList<Map<String,Object>>();
    private Cursor cursor;
    private static String sql="select * from "+DB.Table_Name2+" order by "+DB.Put_Column4+" desc";
    private ProgressBarDialog progressDialog = null;  
    private static String search=null;
    private DialogCreate dialogCreate;
    
	private static String[] projection=new String[]{DB.Put_Column1,DB.Put_Column2,DB.Put_Column3,DB.Put_Column4,DB.Put_Column5,DB.Put_Column6,};
	
	
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
		mListView.setOnItemClickListener(this);
		bAdapter=new CoinItemAdapter(this, maps);
		mListView.setAdapter(bAdapter);
		mPullDownView.enableAutoFetchMore(true, 1);
		
		dialogCreate=new DialogCreate(this, callBack);
		
		loadData(sql);
	}

	private void loadData(String sql){
		cursor=this.getContentResolver().query(Constant.CONTENT_URI_PutIn, projection, sql, null, null);
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
					map.put(DB.Put_Column1,cursor.getInt(1));
					map.put(DB.Put_Column2,cursor.getString(2));
					map.put(DB.Put_Column3,cursor.getInt(3));
					map.put(DB.Put_Column4,cursor.getString(4));
					map.put(DB.Put_Column5,cursor.getInt(5));
					map.put(DB.Put_Column6,cursor.getInt(6));
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
	public void onItemClick(AdapterView<?> parent,View view,final int position,long id){
		new AlertDialog.Builder(this).setTitle("功能表").setItems(new String[]{"删除"}, new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				switch (which) {
				case 0:
					int i=deleteItem(" _id ="+maps.get(position).get("id").toString());
					if (i>0) {
						MyToast.shortToast(CoinActivity.this, "删除成功");
						startProgressDialog();
						maps.clear();
						loadData(sql);
					}else {
						MyToast.shortToast(CoinActivity.this, "删除失败");
					}
					break;

				default:
					break;
				}
			}
		}).create().show();
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
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		menu.add(Menu.NONE, Menu.FIRST + 2, 1, "删除").setIcon(

				android.R.drawable.ic_menu_delete);

		// setIcon()方法为菜单设置图标，这里使用的是系统自带的图标，同学们留意一下,以

		menu.add(Menu.NONE, Menu.FIRST + 3, 2, "搜索").setIcon(

				android.R.drawable.ic_menu_help);
		
		menu.add(Menu.NONE, Menu.FIRST + 4, 3, "刷新").setIcon(

				android.R.drawable.ic_menu_revert);

		menu.add(Menu.NONE, Menu.FIRST + 1, 0, "添加").setIcon(

				android.R.drawable.ic_menu_add);


		return super.onCreateOptionsMenu(menu);  
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case Menu.FIRST+1:
			Intent intent=new Intent();
		    intent.setClass(CoinActivity.this, CoinAddActivity.class);
		    startActivityForResult(intent, Constant.DIALOG_ID_0);
		    overridePendingTransition(R.anim.slide_left_out, R.anim.slide_right_in);
			break;
		case Menu.FIRST+2:
			
			break;
		case Menu.FIRST+3:
			dialogCreate.createDialog(Constant.DIALOG_ID_0).show();
			break;
		case Menu.FIRST+4:
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
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	private int deleteItem(String arg0){
		int i=this.getContentResolver().delete(Constant.CONTENT_URI_PutIn, arg0, null);
		return i;
	}

}