package com.htwgkonstanz.locationreminder;

import java.util.List;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;
import com.htwgkonstanz.locationreminder.database.LRTask;

public class ShowTask extends MapActivity{
	
	private MapView gMapView;
	private MapController controller;

	@Override
	protected void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.showtask);

		
		LRTask task = (LRTask) getIntent().getSerializableExtra("TASK");
		if(task != null) {
			
			TextView title = (TextView) findViewById(R.id.st_taskName);
			title.setText(task.getTaskName());
			TextView description = (TextView) findViewById(R.id.st_taskDescription);
			description.setText(task.getTaskDescription());
			
			
			gMapView = (MapView) findViewById(R.id.st_mapView);
			
			GeoPoint gPunkt = new GeoPoint((int) task.getTaskLatitude(), (int) task.getTaskLongitude());
			
			gMapView.setSatellite(true);
			// get MapController that helps to set/get location, zoom etc.
			controller = gMapView.getController();
			controller.setCenter(gPunkt);
			controller.setZoom(18);

			gMapView.displayZoomControls(true);
			
			List<Overlay> mapOverlays = gMapView.getOverlays();
			Drawable drawable = this.getResources().getDrawable(R.drawable.task_solved);
			TasksOverlay itemizedoverlay = new TasksOverlay(drawable);
			OverlayItem overlayitem = new OverlayItem(gPunkt, "Hola, Mundo!", "I'm in Mexico City!");
			itemizedoverlay.addOverlay(overlayitem);
			mapOverlays.add(itemizedoverlay);

			controller.setCenter(gPunkt);
		}
		
		
	}

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}

}
