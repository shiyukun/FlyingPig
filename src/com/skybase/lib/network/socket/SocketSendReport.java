package com.skybase.lib.network.socket;

public interface SocketSendReport {
	
	public void onSuccess(byte[] buffer);

	public void onFailed(byte[] buffer);

	public void onCancel(byte[] buffer);
}