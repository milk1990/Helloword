package com.example.mystructs.util.constant;

import android.net.Uri;
import android.os.Environment;
import android.provider.BaseColumns;

public class Constant {
	public static final int DIALOG_ID_0=0;
	public static final int DIALOG_ID_1=1;
	public static final int DIALOG_ID_2=2;
	public static final int DIALOG_ID_3=3;
	public static final String DATABASE_NAME="life.db";
	public static final String AUTOHORITY="com.example.mystructs.util.provider.DataProvider";
	public static final String CONTENT_TYPE = "vnd.android.cursor.dir";
    public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item";
     
    public static final Uri CONTENT_URI_Consump = Uri.parse("content://" + AUTOHORITY +"/"+ DB.Table_Name1);
    public static final Uri CONTENT_URI_PutIn = Uri.parse("content://" + AUTOHORITY +"/"+DB.Table_Name2);
    public static final Uri CONTENT_UTI_Bill=Uri.parse("content://"+AUTOHORITY+"/"+DB.Table_Name3);
	public static final String packge=Environment.getExternalStorageDirectory().toString()+"/LifeHelper/Data/";
	public static final int VERSION = 1;
	public static final String Table_Sort="created DESC";
	public static final class DB implements BaseColumns{
		//第一张表
		public static final String Table_Name1="consump";
		public static final String Consump_Column1="Cnumber";
		public static final String Consump_Column2="Ctitle";
		public static final String Consump_Column3="Cmoney";
		public static final String Consump_Column4="Cdate";
		public static final String Consump_Column5="Cspecial";
		public static final String Consump_Column6="Ccheck";
		//第二张表
		public static final String Table_Name2="putIn";
		public static final String Put_Column1="Pnumber";
		public static final String Put_Column2="Ptitle";
		public static final String Put_Column3="Pmoney";
		public static final String Put_Column4="Pdate";
		public static final String Put_Column5="Pspecial";
		public static final String Put_Column6="Pcheck";
		//第三张表
		public static final String Table_Name3="bill";
		public static final String Bill_Column1="Btitler";
		public static final String Bill_Column2="Bdate";
		public static final String Bill_Column3="Bnomal_lone";
		public static final String Bill_Column4="Bnomal_more";
		public static final String Bill_Column5="Bspecial_lone";
		public static final String Bill_Column6="Bspecial_more";
		public static final String Bill_Column7="Baccount_lone";
		public static final String Bill_Column8="Baccount_more";
		public static final String Bill_Column9="Bcount_lone";
		public static final String Bill_Column10="Bcount_more";
		public static final String Bill_Column11="B_id";
	}
}
