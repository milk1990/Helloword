package com.example.mystructs.util.provider;

import com.example.mystructs.util.constant.Constant;
import com.example.mystructs.util.constant.Constant.DB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBaseHelper extends SQLiteOpenHelper {
	public DataBaseHelper(Context context){
		super(context, Constant.DATABASE_NAME, null, Constant.VERSION);
	}

	public DataBaseHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void onCreate(SQLiteDatabase arg0) {
		// TODO Auto-generated method stub
		String[] sql=new String[4];
		sql[0]="CREATE TABLE IF NOT EXISTS "+DB.Table_Name1+"("+DB._ID+" integer primary key autoincrement,"
		          +DB.Consump_Column1+" integer,"+DB.Consump_Column2+" TEXT,"+DB.Consump_Column3+" INTEGER,"+
				  DB.Consump_Column4+" DATE,"+DB.Consump_Column5+" INTEGER,"+DB.Consump_Column6+" INTEGER"+")";
		sql[1]="CREATE TABLE IF NOT EXISTS "+DB.Table_Name2+"("+DB._ID+" integer primary key autoincrement,"
		          +DB.Put_Column1+" integer,"+DB.Put_Column2+" TEXT,"+DB.Put_Column3+" INTEGER,"+
				  DB.Put_Column4+" DATE,"+DB.Put_Column5+" INTEGER,"+DB.Put_Column6+" INTEGER"+")";
		sql[2]="CREATE TABLE IF NOT EXISTS "+DB.Table_Name3+"("+DB._ID+" integer primary key autoincrement,"
		          +DB.Bill_Column1+" Text,"+DB.Bill_Column2+" TEXT,"+DB.Bill_Column3+" INTEGER,"+
				  DB.Bill_Column4+" INTEGER,"+DB.Bill_Column5+" INTEGER,"+DB.Bill_Column6+" INTEGER,"
		          +DB.Bill_Column7+" INTEGER,"+DB.Bill_Column8+" INTEGER,"+DB.Bill_Column9+" INTEGER,"
		          +DB.Bill_Column10+" INTEGER,"+DB.Bill_Column11+" TEXT"+")";
		arg0.execSQL(sql[0]);
		arg0.execSQL(sql[1]);
		arg0.execSQL(sql[2]);
		arg0.execSQL("CREATE TABLE IF NOT EXISTS filedownlog (id integer primary key autoincrement, downpath varchar(100), threadid INTEGER, downlength INTEGER)");
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		arg0.execSQL("DROP TABLE IF EXISTS filedownlog");
		onCreate(arg0);
	}
    
}
