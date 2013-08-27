package com.example.mystructs.app.coin;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.example.mystructs.R;
import com.example.mystructs.util.constant.Constant;
import com.example.mystructs.util.constant.Constant.DB;
import com.example.mystructs.util.mention.MyToast;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

public class CoinAddActivity extends Activity implements OnClickListener   {
	private RadioGroup radioGroup;
	private EditText title;
	private EditText money;
	private TextView date;
	private ImageView date_bt;
	private CheckBox special;
	private CheckBox account;
	private ImageView forgive;
	private ImageView save;
	
	private int RadioTag;
	private int SpecialTag;
	private int AccountTag;
	private String titleTag;
	private String moneyTag;
	private String dateTag;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.coin_add);
		radioGroup=(RadioGroup)findViewById(R.id.coin_add_group);
		title=(EditText)findViewById(R.id.coin_add_title);
		money=(EditText)findViewById(R.id.coin_add_money);
		date=(TextView)findViewById(R.id.coin_add_date);
		date_bt=(ImageView)findViewById(R.id.coin_add_date_bt);
		special=(CheckBox)findViewById(R.id.coin_add_special);
		account=(CheckBox)findViewById(R.id.coin_add_account);
		forgive=(ImageView)findViewById(R.id.coin_add_forgive);
		save=(ImageView)findViewById(R.id.coin_add_save);
		date_bt.setOnClickListener(this);
		forgive.setOnClickListener(this);
		save.setOnClickListener(this);
		radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				switch (checkedId) {
				case R.id.coin_add_group_lone:
					RadioTag=0;
					break;
				case R.id.coin_add_group_more:
					RadioTag=1;
					break;
				default:
					break;
				}
			}
		});
		CheckBoxLisenter lisenter=new CheckBoxLisenter();
		special.setOnCheckedChangeListener(lisenter);
		account.setOnCheckedChangeListener(lisenter);
		
		//设置数据
		SimpleDateFormat dFormat=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		Date date1=new Date(System.currentTimeMillis());
		dateTag = dFormat.format(date1);
		date.setText(dateTag);
		
	}
	private class CheckBoxLisenter implements android.widget.CompoundButton.OnCheckedChangeListener{

		@Override
		public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
			// TODO Auto-generated method stub
			switch (buttonView.getId()) {
			case R.id.coin_add_special:
				if (special.isChecked()) {
					SpecialTag=1;
				} else {
					SpecialTag=0;
				}
				break;
			case R.id.coin_add_account:
				if (account.isChecked()) {
					AccountTag=1;
				} else {
					AccountTag=0;
				}
				break;
			default:
				break;
			}
		}
		
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.coin_add_date_bt:
			showDialog(Constant.DIALOG_ID_0);
			break;
		case R.id.coin_add_forgive:
			finish();
			break;
		case R.id.coin_add_save:
			titleTag=title.getText().toString().trim();
			moneyTag=money.getText().toString().trim();
			if (titleTag.equals("")) {
				MyToast.shortToast(this, "内容不为空");
			}else if (moneyTag.equals("")) {
				MyToast.shortToast(this, "内容不为空");
			}else if (addcoin()) {
				MyToast.shortToast(this, "添加成功！");
				clearEditText();
				setResult(Constant.DIALOG_ID_0);
				finish();
			} else {
				clearEditText();
				MyToast.shortToast(this, "添加失败！");
			}
			break;	
		
		default:
			break;
		}
	}

	@Override
	@Deprecated
	protected Dialog onCreateDialog(int id) {
		// TODO Auto-generated method stub
		switch (id) {
		case Constant.DIALOG_ID_0:
			return new DatePickerDialog(CoinAddActivity.this, onDateSetListener, 2013, 1, 20);
		default:
			break;
		}
		return super.onCreateDialog(id);
	}
	
	DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {

		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear,int dayOfMonth) {
			// TODO Auto-generated method stub
			dateTag="";
			monthOfYear++;
			SimpleDateFormat dFormat=new SimpleDateFormat("hh:mm:ss");
			Date date1=new Date(System.currentTimeMillis());
			String time = dFormat.format(date1);
			dateTag=""+year;
			if (monthOfYear<10) {
				dateTag+="-"+0+""+monthOfYear;
			}else {
				dateTag+="-"+monthOfYear;
			}
			if (dayOfMonth<10) {
				dateTag+="-"+0+""+dayOfMonth;
			}else {
				dateTag+="-"+dayOfMonth;
			}
			
            dateTag+=" "+time;
            date.setText(dateTag);
		}
	}; 
	
	private boolean addcoin(){
		ContentValues contentValues=new ContentValues();
		contentValues.put(DB.Put_Column1, RadioTag);
		contentValues.put(DB.Put_Column2, titleTag);
		contentValues.put(DB.Put_Column3, moneyTag);
		contentValues.put(DB.Put_Column4, dateTag);
		contentValues.put(DB.Put_Column5, SpecialTag);
		contentValues.put(DB.Put_Column6, AccountTag);
		Uri uri=this.getContentResolver().insert(Constant.CONTENT_URI_PutIn, contentValues);
		int result=Integer.parseInt(uri.getPathSegments().get(1));
		if (result>0) {
			return true;
		}else {
			return false;
		}
		
	}
	
	private void clearEditText(){
		title.setText("");
		money.setText("");
	}
}

