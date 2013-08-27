package com.example.mystructs.util.provider;

import java.io.File;

import com.example.mystructs.util.constant.Constant;
import com.example.mystructs.util.constant.Constant.DB;


import android.R.integer;
import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

public class DataProvider extends ContentProvider {
	
	private String path=null;
	private DataBaseHelper abBaseHelper=null;
	private SQLiteDatabase db=null ;
	private static UriMatcher uMatcher;
	static{
		uMatcher= new UriMatcher(UriMatcher.NO_MATCH);
		uMatcher.addURI(Constant.AUTOHORITY,DB.Table_Name1+"", Constant.DIALOG_ID_0);
		uMatcher.addURI(Constant.AUTOHORITY, DB.Table_Name1+"/#", Constant.DIALOG_ID_0);
		uMatcher.addURI(Constant.AUTOHORITY, DB.Table_Name2+"", Constant.DIALOG_ID_1);
		uMatcher.addURI(Constant.AUTOHORITY, DB.Table_Name2+"/#", Constant.DIALOG_ID_1);
		uMatcher.addURI(Constant.AUTOHORITY, DB.Table_Name3+"", Constant.DIALOG_ID_2);
		uMatcher.addURI(Constant.AUTOHORITY, DB.Table_Name3+"/#", Constant.DIALOG_ID_2);
	}
	
	@Override
	public int delete(Uri arg0, String arg1, String[] arg2) {
		// TODO Auto-generated method stub
		createDataBase();
		String tablename =getType(arg0);
		int i=db.delete(tablename, arg1, null);
		return i;
	}

	@Override
	public String getType(Uri arg0) {
		// TODO Auto-generated method stub
		switch (uMatcher.match(arg0)) {
		case Constant.DIALOG_ID_0:
			
			return DB.Table_Name1;
			
		case Constant.DIALOG_ID_1:
			
			return DB.Table_Name2;
			
		case Constant.DIALOG_ID_2:
			
			return DB.Table_Name3;

		default:
			break;
		}
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues arg1) {
		// TODO Auto-generated method stub
		createDataBase();
		String tablename =getType(uri);
		long rowId = db.insert(tablename, DB.Consump_Column1, arg1);
		Uri diaryUri = ContentUris.withAppendedId(uri, rowId);
		return diaryUri;
	}

	@Override
	public boolean onCreate() {
		// TODO Auto-generated method stub
		try {
			abBaseHelper=new DataBaseHelper(this.getContext());
			path = getDatabasePath(Constant.DATABASE_NAME).getPath();
			db =SQLiteDatabase.openOrCreateDatabase(path, null);
			abBaseHelper.onCreate(db);
			return true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public Cursor query(Uri arg0, String[] arg1, String arg2, String[] arg3,
			String arg4) {
		// TODO Auto-generated method stub
		createDataBase();
		Cursor cursor=db.rawQuery(arg2, null);
		int i= cursor.getCount();
		return cursor;
	}

	@Override
	public int update(Uri arg0, ContentValues arg1, String arg2, String[] arg3) {
		// TODO Auto-generated method stub
		createDataBase();
		String tablename =getType(arg0);
		int i=db.update(tablename, arg1, "_id in ("+arg2+")", null);
		return i;
	}
	
	public File getDatabasePath(String name){
        return new File(Constant.packge + name);
    }
	public void createDataBase(){
		if (db==null) {
			onCreate();
		}
	}

}
