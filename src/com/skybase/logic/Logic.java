package com.skybase.logic;

import java.io.EOFException;
import java.io.IOException;

import android.util.Log;

import com.skybase.lib.broadcast.BroadcastReceiver;
import com.skybase.lib.broadcast.SimplePropertyObservable;
import com.skybase.lib.network.socket.SocketActiveListener;
import com.skybase.lib.network.socket.SocketModule;

public class Logic extends SimplePropertyObservable implements SocketActiveListener {

	private SocketModule mSocket;
	private static Logic INSTANCE;
	private static final String TAG = "Logic ";
	
	
	public final static byte BROCAST_ID_RECEIVE_SOCKET = 100;
	
	private Logic(){
		
	}
	
	public static synchronized Logic getInstance(){
		if(INSTANCE == null){
			INSTANCE = new Logic();
		}
		return INSTANCE;
	}
	
	public void addListener(BroadcastReceiver listener, int[] eventIds) {
		super.addListener(listener, eventIds);
	}
	
	public void createSocket(String host, String[] ports) {
		mSocket = new SocketModule();
		mSocket.setSocketActiveListener(this);
		mSocket.start(host, ports);
	}
	
	public void closeSocket(){
		if(mSocket != null){
			mSocket.close();
		}
	}
	
	public void sendToSocket(String str){
		mSocket.send(str.getBytes(), null);
	}
	
	@Override
	public void onReceiveData(byte[] data) {
		// TODO Auto-generated method stub
		Log.i(TAG, new String(data));
		this.fireEvent(this, BROCAST_ID_RECEIVE_SOCKET, new String(data));
	}

	@Override
	public void onIOException(IOException e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onEOFException(EOFException e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onIllegalArgumentException(IllegalArgumentException e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onUnKnownException(Exception e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onOpen() {
		Log.i(TAG, "connect to socket");
	}

	@Override
	public void onClose() {
		
	}

}
