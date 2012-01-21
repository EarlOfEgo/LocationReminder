package com.htwgkonstanz.locationreminder.showtasks;

import java.text.SimpleDateFormat;
import java.util.List;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.htwgkonstanz.locationreminder.R;
import com.htwgkonstanz.locationreminder.database.LRTask;

public class ShowTask extends MapActivity {

	private MapView gMapView;
	private MapController controller;

	@Override
	protected void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.showtask);

		LRTask task = (LRTask) getIntent().getSerializableExtra("TASK");
		if (task != null) {

			TextView title = (TextView) findViewById(R.id.st_taskName);
			title.setText(task.getTaskName());
			TextView description = (TextView) findViewById(R.id.st_taskDescription);
			description.setText(task.getTaskDescription());

			ImageView icon = (ImageView) findViewById(R.id.st_Image);
			if (task.isTaskExecuted())
				icon.setImageResource(R.drawable.task_solved);
			else
				icon.setImageResource(R.drawable.task_button);

			addTabs(task);
			
			TextView creationDate = (TextView) findViewById(R.id.st_taskCreationDate);
			creationDate.setText(new SimpleDateFormat("dd.MM.yyyy HH:mm").format(task.getTaskCreationDate()));

			gMapView = (MapView) findViewById(R.id.st_mapView);
			
			double latitude = task.getTaskLatitude();
			double longitude = task.getTaskLongitude();
			GeoPoint p = new GeoPoint((int) (latitude * 1E6), (int) (longitude * 1E6));
			Log.d("CURRENT", "Latitude: " + String.valueOf(latitude) + " Longitude: " +String.valueOf(longitude));
			
			gMapView.setSatellite(true);
			gMapView.setBuiltInZoomControls(true);
			controller = gMapView.getController();
			controller.animateTo(p);
			controller.setZoom(18);

			MapOverlay mapOverlay = new MapOverlay(p);
			List<Overlay> listOfOverlays = gMapView.getOverlays();
			listOfOverlays.clear();
			listOfOverlays.add(mapOverlay);

			gMapView.invalidate();
		}

	}

	private void addTabs(LRTask task) {
		TabHost mTabHost = (TabHost) findViewById(R.id.tabhost);
		mTabHost.setup();

		Resources res = getResources();
		String[] days = { res.getString(R.string.sunday), res.getString(R.string.monday), res.getString(R.string.tuesday), res.getString(R.string.wednesday), res.getString(R.string.thursday), res.getString(R.string.friday), res.getString(R.string.saturday) };
		int[] idsFrom = { R.id.st_timeFromSunday, R.id.st_timeFromMonday, R.id.st_timeFromTuesday, R.id.st_timeFromWednesday, R.id.st_timeFromThursday, R.id.st_timeFromFriday, R.id.st_timeFromSaturday };
		int[] idsTo = { R.id.st_timeToSunday, R.id.st_timeToMonday, R.id.st_timeToTuesday, R.id.st_timeToWednesday, R.id.st_timeToThursday, R.id.st_timeToFriday, R.id.st_timeToSaturday };
		int[] idsGrids = { R.id.grid_tab_sunday, R.id.grid_tab_monday, R.id.grid_tab_tuesday, R.id.grid_tab_wednesday, R.id.grid_tab_thursday, R.id.grid_tab_friday, R.id.grid_tab_saturday };

		int[][] fromToTime = task.getRemindTimeRanges();
		for (int i = 0; i < fromToTime.length; i++) {
			if ((fromToTime[i][0] + fromToTime[i][1]) != 0) {
				TextView from = (TextView) findViewById(idsFrom[i]);
				TextView to = (TextView) findViewById(idsTo[i]);
				int hour = fromToTime[i][0] / 60;
				int minute = fromToTime[i][0] % 60;
				from.setText(new StringBuilder().append(pad(hour)).append(":").append(pad(minute)));
				hour = fromToTime[i][1] / 60;
				minute = fromToTime[i][1] % 60;
				to.setText(new StringBuilder().append(pad(hour)).append(":").append(pad(minute)));
				mTabHost.addTab(mTabHost.newTabSpec(days[i]).setIndicator(days[i]).setContent(idsGrids[i]));
			}
		}
		mTabHost.setCurrentTab(0);

	}

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}

	private static String pad(int c) {
		if (c >= 10)
			return String.valueOf(c);
		else
			return "0" + String.valueOf(c);
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
