package com.htwgkonstanz.locationreminder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

public class ChooseLocationOnMap extends MapActivity {

	private MapView gMapView;
	private double longi;
	private double lat;
	private MapController controller;
	private LocationTuple point;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.chooselocationonmap);
		// Creating and initializing Map
		gMapView = (MapView) findViewById(R.id.mapView);

		 GeoPoint p1 = new GeoPoint((int) (lat * 1000000), (int) (longi * 1000000));
		gMapView.setSatellite(true);
		// get MapController that helps to set/get location, zoom etc.
		controller = gMapView.getController();
		controller.setCenter(p1);
		controller.setZoom(14);

		gMapView.displayZoomControls(true);
		
		Button searchButton = (Button) findViewById(R.id.clom_searchButton);
		searchButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				final Dialog dialog = new Dialog(ChooseLocationOnMap.this);
				dialog.setContentView(R.layout.searchlocationdialog);
				dialog.setTitle(R.string.searchLocationTitle);
				
				Button okButton = (Button) dialog.findViewById(R.id.searchLocationOkButton);
				okButton.setOnClickListener(new OnClickListener() {
					
					private List<Overlay> listOfOverlays;

					@Override
					public void onClick(View v) {
						Geocoder geoCoder = new Geocoder(ChooseLocationOnMap.this);
						TextView address = (TextView) dialog.findViewById(R.id.searchLocationTextField);
						
						List<Address> addresses = null;
						try {
							addresses = geoCoder.getFromLocationName(address.getText().toString(), 5);
						} catch (IOException e) {
							System.out.println(e);
						}
						
						if (addresses.size() > 0) {
							GeoPoint p = new GeoPoint((int) (addresses.get(0).getLatitude() * 1E6), (int) (addresses.get(0).getLongitude() * 1E6));
							point = new LocationTuple(p.getLongitudeE6(), p.getLatitudeE6());
							controller.animateTo(p);
							controller.setZoom(12);

							MapOverlay mapOverlay = new MapOverlay(p);
							listOfOverlays = gMapView.getOverlays();
							listOfOverlays.clear();
							listOfOverlays.add(mapOverlay);

							gMapView.invalidate();
						} else {
							AlertDialog.Builder adb = new AlertDialog.Builder(ChooseLocationOnMap.this);
							adb.setTitle("Google Map");
							adb.setMessage(R.string.searchLocationWrong);
							adb.setPositiveButton(R.string.close, null);
							adb.show();
						}
						dialog.dismiss();
					
					}
				});
				dialog.show();
			}
		});

		Button okButton = (Button) findViewById(R.id.clom_okButton);
		okButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.putExtra("POINT", point);
				if(getParent() == null) {
					setResult(Activity.RESULT_OK, intent);
				} else {
					getParent().setResult(Activity.RESULT_OK, intent);
				}
				finish();
			}
		});

		List<Overlay> mapOverlays = gMapView.getOverlays();
		Drawable drawable = this.getResources().getDrawable(R.drawable.task_solved);
		TasksOverlay itemizedoverlay = new TasksOverlay(drawable);

		GeoPoint point = new GeoPoint(19240000, -99120000);
		OverlayItem overlayitem = new OverlayItem(point, "Hola, Mundo!", "I'm in Mexico City!");

		itemizedoverlay.addOverlay(overlayitem);
		mapOverlays.add(itemizedoverlay);

		controller.setCenter(point);
	}

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}

	class MapOverlay extends com.google.android.maps.Overlay {
		GeoPoint p;
		public MapOverlay(GeoPoint p) {
			this.p = p;
		}
		
		public boolean draw(Canvas canvas, MapView mapView, boolean shadow, long when) {
			super.draw(canvas, mapView, shadow);

			// ---translate the GeoPoint to screen pixels---
			Point screenPts = new Point();
			mapView.getProjection().toPixels(p, screenPts);

			// ---add the marker---
			Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.task_solved);
			canvas.drawBitmap(bmp, screenPts.x, screenPts.y - 32, null);
			return true;
		}
	}


}
