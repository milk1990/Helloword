package com.example.mystructs.versionupdate;

import java.io.File;

import org.json.JSONArray;
import org.json.JSONObject;


import com.example.mystructs.R;
import com.example.mystructs.mention.MyToast;
import com.example.mystructs.versionupdate.multidownload.DownloadProgressListener;
import com.example.mystructs.versionupdate.multidownload.FileDownloader;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class UpdateActivity  extends Activity{
        private static final String TAG = "Update";
        public ProgressDialog pBar;
        private Handler handler = new Handler();

        private int newVerCode = 0;
        private String newVerName = "";

        @Override
        protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_main);
                
                if (getServerVerCode()) {
                        int vercode = GetConfig.getVersionCode(this);
                        if (newVerCode > vercode) {
                                doNewVersionUpdate();
                        } else {
                                notNewVersionShow();
                        }
                }

        }

        private boolean getServerVerCode() {
                try {
                        String verjson = GetNetVersion.getContent(GetConfig.UPDATE_SERVER
                                        + GetConfig.UPDATE_VERJSON);
                        JSONArray array = new JSONArray(verjson);
                        if (array.length() > 0) {
                                JSONObject obj = array.getJSONObject(0);
                                try {
                                        newVerCode = Integer.parseInt(obj.getString("verCode"));
                                        newVerName = obj.getString("verName");
                                } catch (Exception e) {
                                        newVerCode = -1;
                                        newVerName = "";
                                        return false;
                                }
                        }
                } catch (Exception e) {
                        Log.e(TAG, e.getMessage());
                        return false;
                }
                return true;
        }

        private void notNewVersionShow() {
                int verCode = GetConfig.getVersionCode(this);
                String verName = GetConfig.getVerName(this);
                StringBuffer sb = new StringBuffer();
                sb.append("当前版本:");
                sb.append(verName);
                sb.append(" Code:");
                sb.append(verCode);
                sb.append(",\n已是最新版,无需更新!");
                Dialog dialog = new AlertDialog.Builder(UpdateActivity.this)
                                .setTitle("软件更新").setMessage(sb.toString())// 设置内容
                                .setPositiveButton("确定",// 设置确定按钮
                                                new DialogInterface.OnClickListener() {

                                                        @Override
                                                        public void onClick(DialogInterface dialog,
                                                                        int which) {
                                                                finish();
                                                        }

                                                }).create();// 创建
                // 显示对话框
                dialog.show();
        }

        private void doNewVersionUpdate() {
                int verCode = GetConfig.getVersionCode(this);
                String verName = GetConfig.getVerName(this);
                StringBuffer sb = new StringBuffer();
                sb.append("当前版本:");
                sb.append(verName);
                sb.append(" Code:");
                sb.append(verCode);
                sb.append(", 发现新版本:");
                sb.append(newVerName);
                sb.append(" Code:");
                sb.append(newVerCode);
                sb.append(", 是否更新?");
                Dialog dialog = new AlertDialog.Builder(UpdateActivity.this)
                                .setTitle("软件更新")
                                .setMessage(sb.toString())
                                // 设置内容
                                .setPositiveButton("更新",// 设置确定按钮
                                                new DialogInterface.OnClickListener() {

                                                        @Override
                                                        public void onClick(DialogInterface dialog,
                                                                        int which) {
                                                                pBar = new ProgressDialog(UpdateActivity.this);
                                                                pBar.setTitle("正在下载");
                                                                pBar.setMessage("请稍候...");
                                                                pBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                                                                if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
                                                                	 downFile(GetConfig.UPDATE_SERVER+ GetConfig.UPDATE_APKNAME);
                                                        		}else{
                                                        			MyToast.LongToast(UpdateActivity.this, "");
                                                        		}
                                                        }

                                                })
                                .setNegativeButton("暂不更新",
                                                new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog,
                                                                        int whichButton) {
                                                                // 点击"取消"按钮之后退出程序
                                                                finish();
                                                        }
                                                }).create();// 创建
                // 显示对话框
                dialog.show();
        }

        void downFile(final String url) {
        	new Thread(new Runnable() {			
    			@Override
    			public void run() {
    				FileDownloader loader = new FileDownloader(UpdateActivity.this, url, Environment.getExternalStorageDirectory(), 3);
    		    	//progressBar.setMax(loader.getFileSize());//设置进度条的最大刻度为文件的长度
    				try {
    					loader.download(new DownloadProgressListener() {
    						@Override
    						public void onDownloadSize(int size) {//实时获知文件已经下载的数据长度
    							Message msg = new Message();
    							msg.what = 1;
    							msg.getData().putInt("size", size);
    							handler.sendMessage(msg);//发送消息
    						}
    					});
    				} catch (Exception e) {
    					handler.obtainMessage(-1).sendToTarget();
    				}
    			}
    		}).start();
        }

        void down() {
                handler.post(new Runnable() {
                        public void run() {
                                pBar.cancel();
                                update();
                        }
                });

        }

        void update() {

                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.fromFile(new File(Environment
                                .getExternalStorageDirectory(), GetConfig.UPDATE_SAVENAME)),
                                "application/vnd.android.package-archive");
                startActivity(intent);
        }

}
