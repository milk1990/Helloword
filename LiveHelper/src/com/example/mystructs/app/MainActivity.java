package com.example.mystructs.app;




import java.io.File;

import com.example.mystructs.R;
import com.example.mystructs.app.book.BookActivity;
import com.example.mystructs.app.cart.CartActivity;
import com.example.mystructs.app.cart.ProgressBarDialog;
import com.example.mystructs.app.coin.CoinActivity;
import com.example.mystructs.app.file.FileActivity;
import com.example.mystructs.util.constant.Constant;

import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TabHost;
import android.annotation.SuppressLint;
import android.app.TabActivity;
import android.content.Intent;

@SuppressLint("NewApi")
public class MainActivity extends TabActivity {
	private TabHost tHost;
	private RadioGroup radioGroup;
	private int currentTabID = 0;
	private String[]  Tag=new String[]{"TAB_1","TAB_2","TAB_3","TAB_4"};
    @Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.activity_main);
    	File file = new File (Constant.packge);
        if(!file.exists())//如果不存在
        {
         file.mkdirs();//测试下这个能否创建多级目录结构
        }
    	tHost=getTabHost();
    	tHost.addTab(tHost.newTabSpec("TAB_1").setIndicator(Tag[0]).setContent(new Intent(this,BookActivity.class)));
    	tHost.addTab(tHost.newTabSpec("TAB_2").setIndicator(Tag[1]).setContent(new Intent(this,CartActivity.class)));
    	tHost.addTab(tHost.newTabSpec("TAB_3").setIndicator(Tag[2]).setContent(new Intent(this,CoinActivity.class)));
    	tHost.addTab(tHost.newTabSpec("TAB_4").setIndicator(Tag[3]).setContent(new Intent(this,FileActivity.class)));
    	tHost.setCurrentTab(currentTabID);
    	radioGroup=(RadioGroup)findViewById(R.id.main_tab_group);
    	radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				switch (checkedId) {
				case R.id.main_tab_book:
					tHost.setCurrentTabByTag(Tag[0]);
					break;
				case R.id.main_tab_cart:
					tHost.setCurrentTabByTag(Tag[1]);
					break;
				case R.id.main_tab_coin:
					tHost.setCurrentTabByTag(Tag[2]);
					break;
				case R.id.main_tab_file:
					tHost.setCurrentTabByTag(Tag[3]);
					break;

				default:
					break;
				}
			}
		});
    	
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		getMenuInflater().inflate(R.menu.main, menu);
		return super.onCreateOptionsMenu(menu);
	}
      
}
