package com.skybase.lib.collection;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
/**
 * @author SunLin
 *
 */
public class MultiHashMap {
	private Hashtable model;

	public MultiHashMap() {
		this(8);
	}

	public MultiHashMap(int capacity) {
		model = new Hashtable(capacity);
	}

	public void clear() {
		model.clear();
	}

	public boolean containsKey(String key) {
		return model.containsKey(key);
	}
	
//	public boolean containsValue(Object value) {
//		Enumeration e = model.elements();
//		if (e != null) {
//			while (e.hasMoreElements()) {
//				Vector vc = (Vector) e.nextElement();
//				return vc.contains(value);
//			}
//		}
//
//		return false;
//	}
	
	public boolean containsValue(String key, Object value){
		Vector vc = get(key);
		if(vc != null){
			return vc.contains(value);
		}
		return false;
	}

	/**
	 * 根据Key查找对应的Value的集合, 应该对返回的结果集只进行读操作
	 * 
	 * @param key
	 * @return
	 */
	public Vector get(String key) {
		return (Vector) model.get(key);
	}

	public boolean isEmpty() {
		return model.isEmpty();
	}

	public Enumeration keyElements() {
		return model.keys();
	}

	public void put(String key, Object value) {
		Vector vc = (Vector) model.get(key);
		if (vc == null) {
			vc = new Vector();
			model.put(key, vc);
		}
		if (!vc.contains(value)) {
			vc.addElement(value);
		}
	}

	public Vector remove(String key) {
		return (Vector) model.remove(key);
	}

	public void remove(String key, Object value) {
		Vector vc = (Vector) model.get(key);
		if (vc != null) {
			vc.removeElement(value);
		}
	}

	/**
	 * 低效率, 需要遍历
	 * 
	 * @param val
	 */
//	public void removeValue(Object value) {
//		Enumeration e = model.elements();
//		if (e != null) {
//			while (e.hasMoreElements()) {
//				Vector vc = (Vector) e.nextElement();
//				vc.removeElement(value);
//			}
//		}
//	}

	public int size() {
		return model.size();
	}

	public Enumeration values() {
		return model.elements();
	}

}
