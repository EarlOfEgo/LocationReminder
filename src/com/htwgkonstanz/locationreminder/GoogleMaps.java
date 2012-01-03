package com.htwgkonstanz.locationreminder;

import android.content.Context;
import android.location.*;
import android.os.Bundle;

public class GoogleMaps implements LocationListener {
	private double latitude;
	private double longitude;

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
	}

	@Override
	public void onProviderEnabled(String provider) {
	}

	@Override
	public void onProviderDisabled(String provider) {
	}

	@Override
	public void onLocationChanged(Location location) {
		if (location != null) {
			latitude = location.getLatitude();
			longitude = location.getLongitude();
		}
	}

	public LocationTuple getLocation(Context context) {
		LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

		Criteria criteria = new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_FINE);
		String provider = lm.getBestProvider(criteria, true);
		Location mostRecentLocation = lm.getLastKnownLocation(provider);
		if (mostRecentLocation != null) {
			latitude = mostRecentLocation.getLatitude();
			longitude = mostRecentLocation.getLongitude();
		}
		lm.requestLocationUpdates(provider, 1, 0, this);

		return new LocationTuple(longitude, latitude);
	}
}
