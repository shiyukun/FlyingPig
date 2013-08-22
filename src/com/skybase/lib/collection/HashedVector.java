package com.skybase.lib.collection;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

/**
 * a vector with all objects accessible through a hash key
 * 
 * @author yixiang
 * 
 */
public class HashedVector {
	private Vector list;
	private Hashtable table;

	public HashedVector() {
		list = new Vector();
		table = new Hashtable();
	}

	public HashedVector(int capacity) {
		list = new Vector(capacity);
		table = new Hashtable(capacity);
	}

	public void append(IHashable element) {
		removeDuplicateFromList(element);
		list.addElement(element);
		table.put(element.getHashKey(), element);
	}

	public void insertElement(IHashable element, int index) {
		removeDuplicateFromList(element);
		list.insertElementAt(element, index);
		table.put(element.getHashKey(), element);
	}
	
//	public void addElement(IHashable element){
//		list.removeElement(element);
//		list.addElement(element);
//		table.put(element.getHashKey(), element);
//	}
//	
//	public void setE

	public IHashable getElementForKey(Object key) {
		return (IHashable) table.get(key);
	}
	
	public void copyToArray(Object[] anArray){
		list.copyInto(anArray);
	}

	public int size() {
		return table.size();
	}

	public void removeAll() {
		list.removeAllElements();
		table.clear();
	}

	public void removeElementForKey(Object key) {
		Object element = table.get(key);
		if (element != null) {
			list.removeElement(element);
			table.remove(key);
		}
	}

	public void removeElementAt(int index) {
		IHashable element = (IHashable) list.elementAt(index);
		list.removeElementAt(index);
		table.remove(element.getHashKey());
	}

	public boolean containsKey(Object key) {
		return table.containsKey(key);
	}
	public String toString() {
		StringBuffer sb = new StringBuffer();
		int size = list.size();
		for (int i = 0; i < size; ++i) {
			if (i == 0) {
				sb.append('[');
			} else {
				sb.append(',');
			}
			Object elem = list.elementAt(i);
			sb.append(keyForObject(elem)).append(':').append(elem);
		}
		sb.append(']');
		return sb.toString();
	}

	private void removeDuplicateFromList(IHashable element) {
		Object key = element.getHashKey();
		Object existing = table.get(key);
		if (existing != null) {
			list.removeElement(existing);
		}
	}

	private Object keyForObject(Object obj) {
		Enumeration keys = table.keys();
		Object e = null;
		while ((e = keys.nextElement()) != null) {
			if (table.get(e).equals(obj)) {
				return e;
			}
		}
		return null;
	}

}
