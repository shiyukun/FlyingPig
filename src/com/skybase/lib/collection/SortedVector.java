package com.skybase.lib.collection;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

/**
 * a map with all items automatically sorted, it support unlimited modes of sorting as long as the sorting modes are
 * properly implemented by the objects
 * 
 * @author yixiang
 * 
 */
public class SortedVector implements IComparator {
	Vector list;
	Hashtable table;
	int sortingMode;
	IComparator comparator;

	public SortedVector() {
		list = new Vector();
		table = new Hashtable();
	}

	public SortedVector(int capacity) {
		this(capacity, 0);
	}

	public SortedVector(int capacity, int sortingMode) {
		this(capacity, sortingMode, null);
	}

	public SortedVector(int capacity, int sortingMode, IComparator comparator) {
		this.sortingMode = sortingMode;
		list = new Vector(capacity);
		table = new Hashtable(capacity);
		if (comparator == null) {
			this.comparator = this; // use self as comparator
		} else {
			this.comparator = comparator;
		}
	}

	private void insertAndSort(Object item) {
		int size = list.size();
		if (size == 0) {
			list.addElement(item);
		} else {
			boolean inserted = false;
			for (int i = size - 1; i >= 0; --i) {
				Object member = list.elementAt(i);
				if (comparator.compare(member, item, sortingMode) <= 0) {
					list.insertElementAt(item, i + 1);
					inserted = true;
					break;
				}
			}
			if (!inserted) {
				list.insertElementAt(item, 0);
			}
		}
	}

	public int compare(Object lhs, Object rhs, int sortingMode) {
		return ((IComparable) lhs).compareTo((IComparable) rhs, sortingMode);
	}

	/**
	 * should be called when the properties of elements that may affect the sorting order has been changed
	 */
	public void resort() {
		int count = list.size();
		for (int i = 1; i < count; ++i) {
			IComparable item = (IComparable) list.elementAt(i);
			for (int j = i - 1; j >= 0; --j) {
				Object member = list.elementAt(j);
				if (comparator.compare(member, item, sortingMode) > 0) {
					list.setElementAt(member, j + 1);
					list.setElementAt(item, j);
				} else {
					break;
				}
			}
		}
	}

	public int size() {
		return table.size();
	}

	public IComparable elementAt(int index) {
		return (IComparable) list.elementAt(index);
	}

	public void put(Object key, Object item) {
		insertAndSort(item);
		table.put(key, item);
	}

	public IComparable get(Object key) {
		return (IComparable) table.get(key);
	}

	public void removeAll() {
		list.removeAllElements();
		table.clear();
	}

	public void remove(Object key) {
		Object item = table.get(key);
		if (item != null) {
			list.removeElement(item);
			table.remove(key);
		}
	}

	public boolean containsKey(Object key) {
		return table.containsKey(key);
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
}
