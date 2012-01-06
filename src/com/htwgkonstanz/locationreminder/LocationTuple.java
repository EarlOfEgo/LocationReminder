package com.htwgkonstanz.locationreminder;

import java.io.Serializable;

public final class LocationTuple implements Serializable{

	private static final long serialVersionUID = 1L;
	public final double longitude;
	public final double latitude;

	public LocationTuple(double longtitude, double latitude) {
		this.longitude = longtitude;
		this.latitude = latitude;
	}

	@Override
	public boolean equals(Object o) {
		if (o == null)
			return false;
		try {
			return tryCastToLocation(o);
		} catch (ClassCastException cce) {
			return false;
		}
	}

	private boolean tryCastToLocation(Object o) {
		LocationTuple that = (LocationTuple) o;
		return equals(that);
	}

	private boolean equals(LocationTuple that) {
		return this.latitude == that.latitude && this.longitude == that.longitude;
	}
	
	@Override
	public String toString() {
		return longitude + " " + latitude;
	}
}
