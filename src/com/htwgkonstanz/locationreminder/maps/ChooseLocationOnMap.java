package com.htwgkonstanz.locationreminder.maps;

import java.io.IOException;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.htwgkonstanz.locationreminder.R;

public class ChooseLocationOnMap extends MapActivity {

	private MapView gMapView;
	private MapController controller;
	private LocationTuple point;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.chooselocationonmap);
		// Creating and initializing Map
		gMapView = (MapView) findViewById(R.id.mapView);

		gMapView.setSatellite(true);
		gMapView.setBuiltInZoomControls(true);
		controller = gMapView.getController();
		controller.setZoom(18);
		
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
							Log.e("ERROR", "ChooseLocationOnMap" ,e);
						}
						
						if (addresses.size() > 0) {
							double latitude = addresses.get(0).getLatitude();
							double longitude = addresses.get(0).getLongitude();
							GeoPoint p = new GeoPoint((int) (latitude * 1E6), (int) (longitude * 1E6));
							point = new LocationTuple(longitude , latitude);
							Log.d("CURRENT", "Latitude: " + String.valueOf(latitude) + "Latitude: " +String.valueOf(longitude));
							controller.animateTo(p);
							controller.setZoom(18);

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
		
		Button cancelButton = (Button) findViewById(R.id.clom_cancelButton);
		cancelButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});

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

			Point screenPts = new Point();
			mapView.getProjection().toPixels(p, screenPts);

			Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.task_solved);
			canvas.drawBitmap(bmp, screenPts.x, screenPts.y - 32, null);
			return true;
		}
	}


}
