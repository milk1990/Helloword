package com.example.mystructs.app.file;

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

public class FileItemAdapter extends BaseAdapter {
    private Context context;
    private List<Map<String, Object>> list=null;
	private LayoutInflater inflater;
    private static class MyItem{
    	private TextView  bTitler;
    	private TextView  bId;
    	private TextView  bDate;
    }
    
    public FileItemAdapter(Context context,List<Map<String, Object>> list){
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
			convertView=(LinearLayout)inflater.inflate(R.layout.file_item, null);
			item.bTitler=(TextView)convertView.findViewById(R.id.file_item_titler);
			item.bId=(TextView)convertView.findViewById(R.id.file_item_id);
			item.bDate=(TextView)convertView.findViewById(R.id.file_item_date);
			convertView.setTag(item);
		} else {
			item=(MyItem)convertView.getTag();
		}
		item.bId.setText(position+"");
		item.bTitler.setText(list.get(position).get(DB.Bill_Column1).toString());
		item.bDate.setText(list.get(position).get(DB.Bill_Column2).toString());
		
		return convertView;
	}

}
