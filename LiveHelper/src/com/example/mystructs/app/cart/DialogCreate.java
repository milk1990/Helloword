package com.example.mystructs.app.cart;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.example.mystructs.R;
import com.example.mystructs.util.constant.Constant;
import com.example.mystructs.util.constant.Constant.DB;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

public class DialogCreate extends Activity {
	private Context context;
	private AlertDialog.Builder dialog;
	private  DialogCallBack dCallBack;
	
	private CheckBox numberBox;
	private RadioGroup numberGroup;
	private CheckBox titlerBox;
	private EditText titlerText;
	private CheckBox moneyBox;
	private EditText moneyText;
	private CheckBox datebBox;
	private TextView fromtView;
	private ImageView from_bt;
	private TextView toView;
	private ImageView to_bt;
	private CheckBox specialBox;
	private RadioGroup specialGroup;
	private CheckBox accountBox;
	private RadioGroup accountGroup;
	
	private boolean numberflag=false;
	private int numbertag;
	private boolean titlerflag=false;
	private String titlertag;
	private boolean moneyflag=false;
	private String moneytag;
	private boolean dateflag=false;
	private String date_fromtag;
	private String date_totag;
	private boolean specialflag=false;
	private int specialtag;
	private boolean accountflag=false;
	private int accounttag;
	
	private boolean from=false;
	private boolean to=false;
	private String date="";
	private LayoutInflater inflater=null;
	
	private String searchSql="";
	
	public DialogCreate (Context context1,DialogCallBack callBack) {
		this.context=context1;
		this.dCallBack=callBack;
		inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	public AlertDialog createDialog(final int Id){
		View view=inflater.inflate(R.layout.search, null);
		numberBox=(CheckBox)view.findViewById(R.id.search_numberflag);
		numberGroup=(RadioGroup)view.findViewById(R.id.search_number_group);
		titlerBox=(CheckBox)view.findViewById(R.id.search_titlerflag);
		titlerText=(EditText)view.findViewById(R.id.search_title);
		moneyBox=(CheckBox)view.findViewById(R.id.search_moneyflag);
		moneyText=(EditText)view.findViewById(R.id.search_money);
		datebBox=(CheckBox)view.findViewById(R.id.search_dateflag);
		fromtView=(TextView)view.findViewById(R.id.search_date_from);
		from_bt=(ImageView)view.findViewById(R.id.search_date_bt);
		toView=(TextView)view.findViewById(R.id.search_date_to);
		to_bt=(ImageView)view.findViewById(R.id.search_date_bt2);
		specialBox=(CheckBox)view.findViewById(R.id.search_specialflag);
		specialGroup=(RadioGroup)view.findViewById(R.id.search_special_group);
		accountBox=(CheckBox)view.findViewById(R.id.search_accountflag);
		accountGroup=(RadioGroup)view.findViewById(R.id.search_account_group);
		
		numberBox.setChecked(true);
		titlerBox.setChecked(true);
		moneyBox.setChecked(true);
		datebBox.setChecked(true);
		specialBox.setChecked(true);
		accountBox.setChecked(true);
		
		CheckBoxLisenter boxLisenter=new CheckBoxLisenter();
		RadioGroupListener groupListener=new RadioGroupListener();
		ClickListener clickListener=new ClickListener();
		
		numberBox.setOnCheckedChangeListener(boxLisenter);
		numberGroup.setOnCheckedChangeListener(groupListener);
		titlerBox.setOnCheckedChangeListener(boxLisenter);
		moneyBox.setOnCheckedChangeListener(boxLisenter);
		specialBox.setOnCheckedChangeListener(boxLisenter);
		specialGroup.setOnCheckedChangeListener(groupListener);
		accountBox.setOnCheckedChangeListener(boxLisenter);
		accountGroup.setOnCheckedChangeListener(groupListener);
		titlerText.setOnClickListener(clickListener);
		moneyText.setOnClickListener(clickListener);
		from_bt.setOnClickListener(clickListener);
		to_bt.setOnClickListener(clickListener);
		
		dialog=new AlertDialog.Builder(context);
		dialog.setView(view);
		dialog.setTitle("按条件查询");
		dialog.setNegativeButton("取消", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				searchSql="";
				dialog.dismiss();
			}
		});
		dialog.setPositiveButton("确定", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				String search="";
				boolean flag=false;
				titlertag=titlerText.getText().toString().trim();
				moneytag=moneyText.getText().toString().trim();
				date_fromtag=fromtView.getText().toString().trim();
				date_totag=toView.getText().toString().trim();
				switch (Id) {
				case Constant.DIALOG_ID_0:
					searchSql=createCoinSql(search,flag);
					break;
				case Constant.DIALOG_ID_1:
					searchSql=createCartSql(search,flag);
					break;

				default:
					break;
				}
				clearflag(false);
				dCallBack.refreshData();
			}
		});
		return dialog.create();
	}
	private class CheckBoxLisenter implements android.widget.CompoundButton.OnCheckedChangeListener{

		@Override
		public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
			// TODO Auto-generated method stub
			switch (buttonView.getId()) {
			case R.id.search_numberflag:
				if (isChecked) {
					numberGroup.clearCheck();
					numberflag=false;
				}else {
					numberflag=true;
				}
				break;
			case R.id.search_titlerflag:
				if (isChecked) {
					titlerText.setText("");
					titlerText.clearFocus();
					titlerflag=false;
				} else {
					titlerflag=true;
				}
				break;
			case R.id.search_moneyflag:
				if (isChecked) {
					moneyText.setText("");
					moneyText.clearFocus();
					moneyflag=false;
				} else {
					moneyflag=true;
				}
				break;
			case R.id.search_dateflag:
				if (isChecked) {
					dateflag=false;
					fromtView.setText("");
					toView.setText("");
				} else {
					dateflag=true;
				}
				break;
			case R.id.search_specialflag:
				if (isChecked) {
					specialGroup.clearCheck();
					specialflag=false;
				} else {
					specialflag=true;
				}
				break;
			case R.id.search_accountflag:
				if (isChecked) {
					accountGroup.clearCheck();
					accountflag=false;
				} else {
					accountflag=true;
				}
				break;

			default:
				break;
			}
		}
		
	}
	private class RadioGroupListener implements android.widget.RadioGroup.OnCheckedChangeListener{
		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) {
			// TODO Auto-generated method stub
			switch (group.getId()) {
			case R.id.search_number_group:
				switch (checkedId) {
				case R.id.search_number_group_lone:
					numberBox.setChecked(false);
					numberflag=true;
					numbertag=0;
					break;
				case R.id.search_number_group_more:
					numberBox.setChecked(false);
					numberflag=true;
					numbertag=1;
					break;
				case -1:
					numberBox.setChecked(true);
					numberflag=false;
					numbertag=0;
					break;
				default:
					break;
				}
				break;
			case R.id.search_account_group:
				switch (checkedId) {
				case R.id.search_account_group_yes:
					accountBox.setChecked(false);
					accountflag=true;
					accounttag=1;
					break;
				case R.id.search_account_group_no:
					accountBox.setChecked(false);
					accountflag=true;
					accounttag=0;
					break;
				case -1:
					accountBox.setChecked(true);
					accountflag=false;
					accounttag=0;
					break;
				default:
					break;
				}
				break;
			case R.id.search_special_group:
				switch (checkedId) {
				case R.id.search_special_group_yes:
					specialBox.setChecked(false);
					specialflag=true;
					specialtag=1;
					break;
				case R.id.search_special_group_no:
					specialBox.setChecked(false);
					specialflag=true;
					specialtag=0;
					break;
				case -1:
					specialBox.setChecked(true);
					specialflag=false;
					specialtag=0;
					break;
				default:
					break;
				}
				break;
				
			default:
				break;
			}
		}
		
	}
	
	private class ClickListener implements android.view.View.OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.search_title:
				titlerBox.setChecked(false);
				titlerflag=true;
				break;
			case R.id.search_money:
				moneyBox.setChecked(false);
				moneyflag=true;
				break;
			case R.id.search_date_bt:
				datebBox.setChecked(false);
				dateflag=true;
				from=true;
				new DatePickerDialog(context, onDateSetListener, 2013, 1, 20).show();
				break;
			case R.id.search_date_bt2:
				datebBox.setChecked(false);
				dateflag=true;
				to=true;
				new DatePickerDialog(context, onDateSetListener, 2013, 1, 20).show();
				break;
			default:
				break;
			}
		}
		
	}
	
	DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {
		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear,int dayOfMonth) {
			// TODO Auto-generated method stub
			date="";
			monthOfYear++;
			date=""+year;
			if (monthOfYear<10) {
				date+="-"+0+""+monthOfYear;
			}else {
				date+="-"+monthOfYear;
			}
			if (dayOfMonth<10) {
				date+="-"+0+""+dayOfMonth;
			}else {
				date+="-"+dayOfMonth;
			}
			if(from){
				fromtView.setText(date);
			}
			if (to) {
				toView.setText(date);
			}
			from=false;
			to=false;
		}
	}; 
	private String createCoinSql(String search,boolean flag){
		search=search+"select * from "+DB.Table_Name2;
		if (numberflag) {
			search=search+" where "+DB.Put_Column1+"="+numbertag;
			flag=true;
		}
		if (titlerflag&&flag) {
			search=search+" and "+DB.Put_Column2+" like '%"+titlertag+"%'"+" ";
		}else if (titlerflag) {
			search=search+" where "+DB.Put_Column2+" like '%"+titlertag+"%'"+" ";
			flag=true;
		}
		if (moneyflag&&flag) {
			search=search+" and "+DB.Put_Column3+" like '%"+moneytag+"%'"+" ";
		}else if (moneyflag) {
			search=search+" where "+DB.Put_Column3+" like '%"+moneytag+"%'"+" ";
			flag=true;
		}
		if (dateflag&&flag) {
			if (!date_fromtag.equals("")) {
				if (date_totag.equals("")) {
					SimpleDateFormat dFormat=new SimpleDateFormat("yyyy-MM-dd");
					Date date1=new Date(System.currentTimeMillis());
					date_totag = dFormat.format(date1);
				}
				search=search+" and "+DB.Put_Column4+" between '"+date_fromtag+"' and '"+date_totag+"' ";
			}
		}else if(dateflag){
			if (!date_fromtag.equals("")) {
				if (date_totag.equals("")) {
					SimpleDateFormat dFormat=new SimpleDateFormat("yyyy-MM-dd");
					Date date1=new Date(System.currentTimeMillis());
					date_totag = dFormat.format(date1);
				}
				search=search+" where "+DB.Put_Column4+" between '"+date_fromtag+"' and '"+date_totag+"' ";
				flag=true;
			}
		}
		if (specialflag&&flag) {
			search=search+" and "+DB.Put_Column5+"="+specialtag+" ";
		}else if(specialflag){
			search=search+" where "+DB.Put_Column5+"="+specialtag+" ";
			flag=true;
		}
		if (accountflag&&flag) {
			search=search+" and "+DB.Put_Column6+"="+accounttag+" ";
		}else if(accountflag) {
			search=search+" where "+DB.Put_Column6+"="+accounttag+" ";
		}
		return search;
	}
	
	private String createCartSql(String search,boolean flag){
		search=search+"select * from "+DB.Table_Name1;
		if (numberflag) {
			search=search+" where "+DB.Consump_Column1+"="+numbertag;
			flag=true;
		}
		if (titlerflag&&flag) {
			search=search+" and "+DB.Consump_Column2+" like '%"+titlertag+"%'"+" ";
		}else if (titlerflag) {
			search=search+" where "+DB.Consump_Column2+" like '%"+titlertag+"%'"+" ";
			flag=true;
		}
		if (moneyflag&&flag) {
			search=search+" and "+DB.Consump_Column3+" like '%"+moneytag+"%'"+" ";
		}else if (moneyflag) {
			search=search+" where "+DB.Consump_Column3+" like '%"+moneytag+"%'"+" ";
			flag=true;
		}
		if (dateflag&&flag) {
			if (!date_fromtag.equals("")) {
				if (date_totag.equals("")) {
					SimpleDateFormat dFormat=new SimpleDateFormat("yyyy-MM-dd");
					Date date1=new Date(System.currentTimeMillis());
					date_totag = dFormat.format(date1);
				}
				search=search+" and "+DB.Consump_Column4+" between '"+date_fromtag+"' and '"+date_totag+"' ";
			}
		}else if(dateflag){
			if (!date_fromtag.equals("")) {
				if (date_totag.equals("")) {
					SimpleDateFormat dFormat=new SimpleDateFormat("yyyy-MM-dd");
					Date date1=new Date(System.currentTimeMillis());
					date_totag = dFormat.format(date1);
				}
				search=search+" where "+DB.Consump_Column4+" between '"+date_fromtag+"' and '"+date_totag+"' ";
				flag=true;
			}
		}
		if (specialflag&&flag) {
			search=search+" and "+DB.Consump_Column5+"="+specialtag+" ";
		}else if(specialflag){
			search=search+" where "+DB.Consump_Column5+"="+specialtag+" ";
			flag=true;
		}
		if (accountflag&&flag) {
			search=search+" and "+DB.Consump_Column6+"="+accounttag+" ";
		}else if(accountflag) {
			search=search+" where "+DB.Consump_Column6+"="+accounttag+" ";
		}
		return search;
	}
	
	public  String getSql(){
		return searchSql;
	}
	private void clearflag(boolean flag){
		numberflag=flag;
		titlerflag=flag;
		moneyflag=flag;
		dateflag=flag;
		specialflag=flag;
		accountflag=flag;
	}
}
