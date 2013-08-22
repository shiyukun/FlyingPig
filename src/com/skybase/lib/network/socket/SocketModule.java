package com.skybase.lib.network.socket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;

//import javax.microedition.io.SocketConnection;

import java.net.InetAddress;
import java.net.Socket;

//import net.rim.device.api.io.transport.ConnectionDescriptor;
//import net.rim.device.api.io.transport.ConnectionFactory;
//import net.rim.device.api.io.transport.TransportInfo;

import android.util.Log;

import com.skybase.lib.Handler;
import com.skybase.lib.Handler.HandlerMessage;
import com.skybase.lib.Utility;

/**
 * <p>
 * Socket function
 * </p>
 * 
 * @author Syk
 * 
 */
public class SocketModule {
	
	public static final int MSG_CLOSED = 120;

	private static int CONNECT_TIMEOUT = 30000;

	private Socket mSocket = null;
	
	private DataOutputStream output = null;
	private DataInputStream input = null;

	private Thread readThread = null;

	private boolean enable = true;

	private static Handler handler = new Handler();

	private SocketActiveListener mSocketActiveListener;

	public SocketModule() {
		handler.clearAllHandlerMessage();
	}

	public void setSocketActiveListener(SocketActiveListener listener) {
		mSocketActiveListener = listener;
	}

	private Runnable readerRunnable = new Runnable() {
		public void run() {
			try {
				do {
//					// 1.get package Size
//					int length = input.readInt();
//					if (length < 0) {
//						throw new EOFException();
//					}
//					
//					if(length > 300000){
//						throw new EOFException();
//					}
//
//					byte[] result = new byte[length + 4];
//					result[3] = (byte) (length & 0xff);
//					result[2] = (byte) ((length >> 8) & 0xff);
//					result[1] = (byte) ((length >> 16) & 0xff);
//					result[0] = (byte) ((length >> 24) & 0xff);
//					input.readFully(result, 4, length);
					byte[] result = new byte[5];
					input.readFully(result, 0, 5);
					try {
						onReceive(result);
					} catch (Exception e) {
					
					}
				} while (enable);
			} catch (EOFException e) {
				onIOException(e, true);
				return;
			} catch (IOException e) {
				onIOException(e, true);
				return;
			} catch (Exception e) {
				onException(e, true);
				return;
			} finally {
				
			}
		}
	};

	protected synchronized void onIOException(IOException e, boolean isRead) {
		if (!enable) {
			return;
		}
		finish(true);
		
		if (mSocketActiveListener != null) {
			mSocketActiveListener.onIOException(e);
		}
	}
	
	protected synchronized void onException(Exception e, boolean isRead) {
		if (!enable) {
			return;
		}
		finish(true);
		if (mSocketActiveListener != null) {
			mSocketActiveListener.onUnKnownException(e);
		}
	}

	protected void onReceive(final byte[] buffer) {
		handler.postHandlerMessage(new HandlerMessage() {
			public void onExecute() {
				if (mSocketActiveListener != null) {
					mSocketActiveListener.onReceiveData(buffer);
				}
			}

			public void onCancel() {
				log("Notify Read Data Failed!");
			}
		});
	}

	public void send(final byte[] buffer, final SocketSendReport report) {

		handler.postHandlerMessage(new HandlerMessage() {

			public void onExecute() {
				writedata(buffer, report);
			}

			public void onCancel() {
				log("writeAsyn Data Failed!");
				if (report != null) {
					report.onCancel(buffer);
				}
			}
		});
	}

	synchronized private boolean writedata(byte[] buffer, SocketSendReport writeReport) {
		if (!isActive()) {
			if (writeReport != null) {
				writeReport.onFailed(buffer);
			}
			return false;
		}
		try {
			output.write(buffer);
			output.flush();
			log("+++++++++++ Write DATA Begin ++++++++++");
			log(Utility.bytesToHex(buffer));
			log("+++++++++++ Write DATA End ++++++++");
			try {
				if (writeReport != null) {
					writeReport.onSuccess(buffer);
				}
			} catch (Exception e) {
				log(e.toString());
			}
			return true;
		} catch (IOException e) {
			try {
				if (writeReport != null) {
					writeReport.onFailed(buffer);
				}
			} catch (Exception ex) {
				log(ex.toString());
			}
			onIOException(e, false);
		} catch (Exception e) {
			try {
				if (writeReport != null) {
					writeReport.onFailed(buffer);
				}
			} catch (Exception ex) {
				log(ex.toString());
			}
			onException(e, false);
		}
		return false;
	}

	public void start(final String host, final String[] ports) {
		handler.postHandlerMessage(new HandlerMessage() {
			public void onExecute() {
				for(int i = 0; i < ports.length; i++){
					boolean result = startConnect(host, ports[i], i == ports.length - 1);
					if(result){
						break;
					}
				}
			}

			public void onCancel() {
				log("start Failed!");
			}
		});
	}
	
	private synchronized boolean startConnect(String host, String port, boolean broadcastException) {
		if (isActive()) {
			log("Socket is not null , close it before open !!");
			return false;
		}
		try {
			InetAddress serverAddr = InetAddress.getByName(host);
			mSocket = new Socket(serverAddr, Integer.valueOf(port));
			
			output = new DataOutputStream(mSocket.getOutputStream());
			input = new DataInputStream(mSocket.getInputStream());
			log("socket connect success, url=" + host + ":" + port);

			readThread = new Thread(readerRunnable);
			readThread.start();
			enable = true;
			mSocketActiveListener.onOpen();
			return true;
		} catch (IllegalArgumentException e) {
			log("bad server address: " + host + ":" + port + "     " + e.toString());
			if (broadcastException && mSocketActiveListener != null) {
				mSocketActiveListener.onIllegalArgumentException(e);
			}
		} catch (IOException e) {
			log("the socket is already connected or an error occurs while connecting!    " + e);
			if (broadcastException && mSocketActiveListener != null) {
				mSocketActiveListener.onIOException(e);
			}
		} catch (Exception e) {
			log(e.toString());
			if (broadcastException && mSocketActiveListener != null) {
				mSocketActiveListener.onUnKnownException(e);
			}
		}
		log("Socket Connect Failed");
		enable = false;
		return false;

	}
	
	public void close(){
		handler.clearAllHandlerMessage();
		handler.postHandlerMessage(new HandlerMessage() {
			public void onExecute() {
				finish();
			}

			public void onCancel() {
				log("close Failed!");
			}
		});
	
	}

	private void finish() {
		finish(false);
	}

	synchronized public void finish(boolean byException) {
		enable = false;

		if(input != null){
			try {
				input.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			input = null;
		}
		
		if(output != null){
			try {
				output.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			output = null;
		}
		
		if (mSocket != null) {
			try {
				mSocket.close();
			} catch (Exception e) {
				log(e.toString());
			}
			mSocket = null;
		}
		if (!byException) {
			if (mSocketActiveListener != null) {
				mSocketActiveListener.onClose();
			}
		}
	}

	synchronized public boolean isActive() {
		return mSocket != null;
	}
	
	private void log(String s){
		Log.i("socket ", s);
	}
}
