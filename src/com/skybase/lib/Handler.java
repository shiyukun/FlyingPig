package com.skybase.lib;

import java.util.Vector;

/**
 * @author SunLin
 *
 */
public final class Handler implements Runnable {
	public interface HandlerMessage {
		
		public void onExecute();

		public void onCancel();
	}

	private Vector mMessagePool;

	public Handler() {
		mMessagePool = new Vector(2);
		new Thread(this).start();
	}
	
	public int getQueueSize(){
		return mMessagePool.size();
	}

	public void postHandlerMessage(HandlerMessage msg) {
		if (msg == null) {
			log("HandlerMessage can not be notified!");
			return;
		}
		synchronized (mMessagePool) {
			mMessagePool.addElement(msg);
			mMessagePool.notify();
		}
	}

	public boolean cancelHandlerMessage(HandlerMessage msg) {
		synchronized (mMessagePool) {
			boolean success = mMessagePool.removeElement(msg);
			msg.onCancel();
			return success;

		}
	}
	
	/**
	 * invoke this method, means the client is never need callback the onCancel().
	 */
	public void clearAllHandlerMessage(){
		synchronized (mMessagePool) {
			mMessagePool.removeAllElements();
		}
	}

	public void run() {
		try {
			HandlerMessage msg = null;
			while (true) {
				try {
					synchronized (mMessagePool) {
						if (mMessagePool.size() == 0) {
							mMessagePool.wait();
							continue;
						} else {
							msg = (HandlerMessage) mMessagePool.elementAt(0);
							mMessagePool.removeElementAt(0);
						}
					}
					msg.onExecute();
				} catch (InterruptedException ie) {
					log("The module thread is interrupted");
				} catch (Exception e) {
					log("", e);
				} finally{
				}
			}
		} catch (Exception e) {
			log("Root exception on THREAD#3");
			log("", e);
		} catch (Error e) {
			log("Root error on THREAD#3");
			log("", e);
			e.printStackTrace();
		}
	}

	private void log(String msg) {
	}

	private void log(String msg, Throwable r) {
	}
}