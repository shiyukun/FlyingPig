package com.skybase.lib.broadcast;

public class SimplePropertyObservable {
	private PropertyObservable observers = new PropertyObservable();

	protected PropertyObservable getListeners() {
		return observers;
	}

	public void setListener(int eventId, BroadcastReceiver listener) {
		observers.setListener(eventId, listener);
	}

	public void addListener(BroadcastReceiver listener, int[] eventIds) {

		for (int i = 0; i < eventIds.length; i++) {
			int id = eventIds[i];
			observers.addListener(id, listener);
		}
	}

	public void removeListener(BroadcastReceiver listener, int[] eventIds) {
		for (int i = 0; i < eventIds.length; i++) {
			int id = eventIds[i];
			observers.remove(id, listener);
		}
	}

	public void clearListeners() {
		observers.clear();
	}

	public void fireEvent(Object sender, int eventId, Object args) {
		observers.fire(sender, eventId, args);
	}

	public void fireEvent(int eventId, Object args) {
		observers.fire(this, eventId, args);
	}
}
