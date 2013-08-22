package com.skybase.lib.network.http;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

import org.apache.http.*;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

/**
 * Http can be used to send get/post request, can't apply to Https<br/>
 * 
 * @author Syk
 * 
 */
public class HttpModule {

	public static String METHOD_GET = "GET";
	public static String METHOD_POST = "POST";
	public static int TIMEOUT = 60000;
	public final static int HTTP_CODE_ERROR = -99;
	public final static int HTTP_CODE_NETWORK_UNAVAILIBLE = -120;

	
	public static void GET(final String url, final HttpCallback callback){
		new Thread(new Runnable(){

			@Override
			public void run() {
				try{
					HttpClient client = new DefaultHttpClient();
					HttpGet request = new HttpGet(url);
					HttpResponse response = client.execute(request);
					byte[] buffer = null;
					if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
						buffer = new byte[1024];
						int len = 0;
						ByteArrayOutputStream baos = new ByteArrayOutputStream();
						InputStream is = response.getEntity().getContent();
						while((len = is.read(buffer)) > 0){
							baos.write(buffer, 0, len);
						}
						buffer = baos.toByteArray();
						is.close();
					}
					callback.onResponse(response.getStatusLine().getStatusCode(), buffer, null);
				}catch(Exception e){
					if (callback != null) {
						callback.onError(e, HTTP_CODE_ERROR);
					}
				}
				
			}
			
		}).start();
	}
	
	public static void POST(final String url, final HashMap<Object, Object> map, final HttpCallback callback){
		new Thread(new Runnable(){
			@Override
			public void run() {
				try{
					HttpClient client = new DefaultHttpClient();
					HttpPost request = new HttpPost(url);
					List<NameValuePair> postData = new ArrayList<NameValuePair>();
					for(Object obj : map.keySet()){
						postData.add(new BasicNameValuePair(obj.toString(), map.get(obj).toString()));
					}
					UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(postData);
					request.setEntity(formEntity);
					HttpResponse response = client.execute(request);
					byte[] buffer = null;
					if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
						buffer = new byte[1024];
						int len = 0;
						ByteArrayOutputStream baos = new ByteArrayOutputStream();
						InputStream is = response.getEntity().getContent();
						while((len = is.read(buffer)) > 0){
							baos.write(buffer, 0, len);
						}
						buffer = baos.toByteArray();
						is.close();
					}
					callback.onResponse(response.getStatusLine().getStatusCode(), buffer, null);
				}catch(Exception e){
					if (callback != null) {
						callback.onError(e, HTTP_CODE_ERROR);
					}
				}
				
			}
			
		}).start();		
	}
}
