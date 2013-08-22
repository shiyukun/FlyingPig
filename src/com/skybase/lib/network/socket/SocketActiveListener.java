package com.skybase.lib.network.socket;

import java.io.EOFException;
import java.io.IOException;

/**
 * @author Syk
 *
 */
public interface SocketActiveListener {
	
	public void onReceiveData(byte[] data);

	public void onIOException(IOException e);

	public void onEOFException(EOFException e);

	public void onIllegalArgumentException(IllegalArgumentException e);

	public void onUnKnownException(Exception e);
	
	public void onOpen();
	
	public void onClose();
}