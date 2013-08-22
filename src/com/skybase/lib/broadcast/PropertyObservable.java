package com.skybase.lib.broadcast;

import java.util.Vector;

import com.skybase.lib.collection.MultiHashMap;

/**
 * <p>观察者模式, 对属性的观察</p>
 * TODO PropertyObservable 和PropertyListener的命名不对应!!
 * @author Syk
 *
 */
public class PropertyObservable {
	private MultiHashMap observers = new MultiHashMap(12);

	public PropertyObservable() {
	}

	/**
	 * 或取对msgID这个消息监听的数目
	 * @param msgID
	 * @return
	 */
	public int getListenerCount(int msgID) {
		Vector vc = observers.get(String.valueOf(msgID));
		if (vc != null) {
			return vc.size();
		}
		return 0;
	}

	/**
	 * 是否有对msgID这个消息的监听
	 * @param msgID
	 * @return
	 */
	public boolean hasListener(int msgID) {
		return getListenerCount(msgID) > 0;
	}

	/**
	 * 添加一个对messageID消息的监听.
	 * @param msgID
	 * @param listener
	 */
	public synchronized void addListener(int msgID, BroadcastReceiver listener) {
		observers.put(String.valueOf(msgID), listener);
	}

	/**
	 * 清除messageID之前的所有监听, 添加新监听listener
	 * @param msgID
	 * @param listener
	 */
	public synchronized void setListener(int msgID, BroadcastReceiver listener) {
		observers.remove(String.valueOf(msgID));
		observers.put(String.valueOf(msgID), listener);
	}

	public int size() {
		return observers.size();
	}

	public synchronized void remove(int msgID, BroadcastReceiver observer) {
		observers.remove(String.valueOf(msgID), observer);
	}

//	public synchronized void remove(BroadcastReceiver observer) {
//		observers.removeValue(observer);
//	}

	public synchronized void clear() {
		observers.clear();
	}

	/**
	 * 激发一个广播,通知所有监听者, 
	 * @param sender  谁激发的, 可以是null, 由监听者和被监听者协商
	 * @param msgID  消息ID, 标识事件
	 * @param args   自定义的参数
	 */
	public void fire(Object sender, int msgID, Object args) {
		int size = 0;
		BroadcastReceiver[] arrays = null;
		synchronized (this) {
			Vector vc = observers.get(String.valueOf(msgID));
			if (vc == null) {
				return;
			}
			size = vc.size();
			arrays = new BroadcastReceiver[size];
			vc.copyInto(arrays);
		}
		if (arrays != null) {
			for (int i = 0; i < arrays.length; i++) {
				BroadcastReceiver observer = arrays[i];
				if (observer != null) {
					try {
						observer.onBroadcastReceive(sender, msgID, args);
					} catch (Exception e) {
						System.out.println(e.toString());
					}
				}
			}
		}
	}

	public void fire(int messageID, int actionType, Object args) {
		fire(null, messageID, args);
	}
}
