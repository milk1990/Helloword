package com.example.mystructs.imgcache;

import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.http.AndroidHttpClient;

public class DownloadImage {
	/**
	 * 连接网络下载图片
	 * @param url  地址
	 * @return Bitmap 图片
	 * */
	public static Bitmap downloadImage(String url) {
		Bitmap bitmap = null;
		HttpClient client = new DefaultHttpClient();   
		HttpParams params = client.getParams();
		HttpConnectionParams.setConnectionTimeout(params, 3000);
		HttpConnectionParams.setSocketBufferSize(params, 3000);
		HttpResponse response = null;
		InputStream inputStream = null;
		HttpGet httpGet = null;
		try {
			httpGet = new HttpGet(url);
			response = client.execute(httpGet);
			int stateCode = response.getStatusLine().getStatusCode();
			if (stateCode != HttpStatus.SC_OK) {
				return bitmap;
			}
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				try {
					inputStream = entity.getContent();
					return bitmap = BitmapFactory.decodeStream(inputStream);
				} finally {
					if (inputStream != null) {
						inputStream.close();
					}
					entity.consumeContent();
				}
			}
		} catch (ClientProtocolException e) {
			httpGet.abort();
			e.printStackTrace();
		} catch (IOException e) {
			httpGet.abort();
			e.printStackTrace();
		} finally {
			((AndroidHttpClient) client).close();
		}
		return bitmap;
	}

	/**
	 * 连接网络下载图片
	 * @param url  地址
	 * @return Bitmap 图片
	 * */
	public static  Bitmap loadImage(String url){   
		Bitmap bitmap=null;
		InputStream inputStream=null;
		try {   
			//创建一个HttpClient对象   
			HttpClient httpclient = new DefaultHttpClient();   
			//远程登录URL   
			String processURL1=url;   
			HttpGet request=new HttpGet(processURL1);   
			request.addHeader("Accept","text/json");   
			//获取响应的结果   
			HttpResponse response =httpclient.execute(request);   
			//获取HttpEntity    
			HttpEntity entity=response.getEntity();   
			if (entity != null) {
				try {
					inputStream = entity.getContent();
					return bitmap = BitmapFactory.decodeStream(inputStream);
				} finally {
					if (inputStream != null) {
						inputStream.close();
					}
					entity.consumeContent();
				}
			}


		} catch (ClientProtocolException e) {   
			// TODO Auto-generated catch block   
			e.printStackTrace();   
		} catch (IOException e) {   
			// TODO Auto-generated catch block   
			e.printStackTrace();

		}catch (Exception e) {
			// TODO: handle exception
		}
		return bitmap;
	}

}
