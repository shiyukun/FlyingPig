package com.skybase.lib;



/**
 * 
 * @author SunLin
 *
 */
public interface SuccessFailedCallback{
	
	/**
	 * @param args
	 * 		第一个参数args[0]为eventtype,第二个参数args[1]为msgid
	 */
	public void onSuccess(int requestID, Object args);
	public void onFailed(int requestID, int errorCode, String errorMsg, Object args);
	public void onCancel(int requestID, Object args);
}


