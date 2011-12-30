package com.htwgkonstanz.locationreminder;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Window;

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
	private MapController mc;

	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.chooselocationonmap);
		// Creating and initializing Map
		gMapView = (MapView) findViewById(R.id.mapView);
		GeoPoint p = new GeoPoint((int) (lat * 1000000), (int) (longi * 1000000));
		gMapView.setSatellite(true);
		//get MapController that helps to set/get location, zoom etc.
		mc = gMapView.getController();
		mc.setCenter(p);
		mc.setZoom(14);
		
		
		List<Overlay> mapOverlays = gMapView.getOverlays();
		Drawable drawable = this.getResources().getDrawable(R.drawable.task_solved);
		TasksOverlay itemizedoverlay = new TasksOverlay(drawable);
		
		GeoPoint point = new GeoPoint(19240000,-99120000);
		OverlayItem overlayitem = new OverlayItem(point, "Hola, Mundo!", "I'm in Mexico City!");
		
		itemizedoverlay.addOverlay(overlayitem);
		mapOverlays.add(itemizedoverlay);
	}

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}
	
	
	private class TasksOverlay extends ItemizedOverlay {
		private ArrayList<OverlayItem> mOverlays = new ArrayList<OverlayItem>();
		private Context mContext;

		public TasksOverlay(Drawable defaultMarker) {
			super(boundCenterBottom(defaultMarker));
		}
		
		public TasksOverlay(Drawable defaultMarker, Context context) {
			super(defaultMarker);
		  	mContext = context;
		}
		
		public void addOverlay(OverlayItem overlay) {
		    mOverlays.add(overlay);
		    populate();
		}

		@Override
		protected OverlayItem createItem(int i) {
			return mOverlays.get(i);
		}

		@Override
		public int size() {
			return mOverlays.size();
		}
		
		@Override
		protected boolean onTap(int index) {
		  OverlayItem item = mOverlays.get(index);
		  AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
		  dialog.setTitle(item.getTitle());
		  dialog.setMessage(item.getSnippet());
		  dialog.show();
		  return true;
		}
		
	}

}
