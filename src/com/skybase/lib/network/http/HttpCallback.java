package com.skybase.lib.network.http;

import java.io.IOException;
import java.net.HttpURLConnection;

import org.apache.http.*;;

/**
 * @author SunLin
 *
 */
public interface HttpCallback {
	/**
	 * Http call back function
	 * 
	 * @param code
	 *            http response code
	 * @param buffer
	 *            http response content
	 * @param connection
	 *           
	 * @throws IOException
	 */
	public void onResponse(int code, byte[] buffer, HttpURLConnection connection) throws IOException;

	public void onError(Exception e, int errorCode);
}
