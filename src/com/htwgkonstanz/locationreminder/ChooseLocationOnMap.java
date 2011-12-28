package com.htwgkonstanz.locationreminder;

import android.os.Bundle;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;

public class ChooseLocationOnMap extends MapActivity {

	private MapView gMapView;
	private double longi;
	private double lat;
	private MapController mc;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.chooselocationonmap);
		// Creating and initializing Map
		gMapView = (MapView) findViewById(R.id.mapView);
		GeoPoint p = new GeoPoint((int) (lat * 1000000), (int) (longi * 1000000));
		gMapView.setSatellite(true);
		//get MapController that helps to set/get location, zoom etc.
		mc = gMapView.getController();
		mc.setCenter(p);
		mc.setZoom(14);
	}

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}

}
