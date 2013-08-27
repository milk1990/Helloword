package com.example.mystructs.app.coin;

import java.util.List;
import java.util.Map;

import com.example.mystructs.R;
import com.example.mystructs.util.constant.Constant.DB;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CoinItemAdapter extends BaseAdapter {
    private Context context;
    private List<Map<String, Object>> list=null;
	private LayoutInflater inflater;
    private static class MyItem{
    	private ImageView cNumber;
    	private TextView  cTitler;
    	private TextView  cId;
    	private TextView  cMoney;
    	private TextView  cDate;
    	private ImageView cSpecial;
    	private ImageView cAccount;
    }
    
    public CoinItemAdapter(Context context,List<Map<String, Object>> list){
    	this.context=context;
    	this.list=list;
    	inflater=LayoutInflater.from(context);
    }
    
    public void setList(List<Map<String, Object>> list){
    	this.list=list;
    }
    
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		MyItem item;
		if (convertView==null) {
			item=new MyItem();
			convertView=(LinearLayout)inflater.inflate(R.layout.coin_item, null);
			item.cNumber=(ImageView)convertView.findViewById(R.id.putin_number);
			item.cTitler=(TextView)convertView.findViewById(R.id.putin_title);
			item.cId=(TextView)convertView.findViewById(R.id.putin_id);
			item.cMoney=(TextView)convertView.findViewById(R.id.putin_money);
			item.cDate=(TextView)convertView.findViewById(R.id.putin_date);
			item.cSpecial=(ImageView)convertView.findViewById(R.id.putin_special);
			item.cAccount=(ImageView)convertView.findViewById(R.id.putin_clear);
			convertView.setTag(item);
		} else {
			item=(MyItem)convertView.getTag();
		}
		item.cId.setText(position+"");
		item.cTitler.setText(list.get(position).get(DB.Put_Column2).toString());
		item.cMoney.setText(list.get(position).get(DB.Put_Column3).toString());
		item.cDate.setText(list.get(position).get(DB.Put_Column4).toString());
		if ((Integer)list.get(position).get(DB.Put_Column1)==1) {
			item.cNumber.setBackgroundResource(R.drawable.coin_users);
		}else {
			item.cNumber.setBackgroundResource(R.drawable.coin_user);
		}
		if ((Integer)list.get(position).get(DB.Put_Column5)==1) {
			item.cSpecial.setVisibility(View.VISIBLE);
		}else {
			item.cSpecial.setVisibility(View.GONE);
		}
		if ((Integer)list.get(position).get(DB.Put_Column6)==1) {
			item.cAccount.setVisibility(View.VISIBLE);
		}else {
			item.cAccount.setVisibility(View.GONE);
		}
		return convertView;
	}

}
