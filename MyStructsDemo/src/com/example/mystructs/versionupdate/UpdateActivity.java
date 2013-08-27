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
                sb.append("��ǰ�汾:");
                sb.append(verName);
                sb.append(" Code:");
                sb.append(verCode);
                sb.append(",\n�������°�,�������!");
                Dialog dialog = new AlertDialog.Builder(UpdateActivity.this)
                                .setTitle("�������").setMessage(sb.toString())// ��������
                                .setPositiveButton("ȷ��",// ����ȷ����ť
                                                new DialogInterface.OnClickListener() {

                                                        @Override
                                                        public void onClick(DialogInterface dialog,
                                                                        int which) {
                                                                finish();
                                                        }

                                                }).create();// ����
                // ��ʾ�Ի���
                dialog.show();
        }

        private void doNewVersionUpdate() {
                int verCode = GetConfig.getVersionCode(this);
                String verName = GetConfig.getVerName(this);
                StringBuffer sb = new StringBuffer();
                sb.append("��ǰ�汾:");
                sb.append(verName);
                sb.append(" Code:");
                sb.append(verCode);
                sb.append(", �����°汾:");
                sb.append(newVerName);
                sb.append(" Code:");
                sb.append(newVerCode);
                sb.append(", �Ƿ����?");
                Dialog dialog = new AlertDialog.Builder(UpdateActivity.this)
                                .setTitle("�������")
                                .setMessage(sb.toString())
                                // ��������
                                .setPositiveButton("����",// ����ȷ����ť
                                                new DialogInterface.OnClickListener() {

                                                        @Override
                                                        public void onClick(DialogInterface dialog,
                                                                        int which) {
                                                                pBar = new ProgressDialog(UpdateActivity.this);
                                                                pBar.setTitle("��������");
                                                                pBar.setMessage("���Ժ�...");
                                                                pBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                                                                if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
                                                                	 downFile(GetConfig.UPDATE_SERVER+ GetConfig.UPDATE_APKNAME);
                                                        		}else{
                                                        			MyToast.LongToast(UpdateActivity.this, "");
                                                        		}
                                                        }

                                                })
                                .setNegativeButton("�ݲ�����",
                                                new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog,
                                                                        int whichButton) {
                                                                // ���"ȡ��"��ť֮���˳�����
                                                                finish();
                                                        }
                                                }).create();// ����
                // ��ʾ�Ի���
                dialog.show();
        }

        void downFile(final String url) {
        	new Thread(new Runnable() {			
    			@Override
    			public void run() {
    				FileDownloader loader = new FileDownloader(UpdateActivity.this, url, Environment.getExternalStorageDirectory(), 3);
    		    	//progressBar.setMax(loader.getFileSize());//���ý����������̶�Ϊ�ļ��ĳ���
    				try {
    					loader.download(new DownloadProgressListener() {
    						@Override
    						public void onDownloadSize(int size) {//ʵʱ��֪�ļ��Ѿ����ص����ݳ���
    							Message msg = new Message();
    							msg.what = 1;
    							msg.getData().putInt("size", size);
    							handler.sendMessage(msg);//������Ϣ
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
