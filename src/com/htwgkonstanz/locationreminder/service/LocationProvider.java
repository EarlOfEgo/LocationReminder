package com.htwgkonstanz.locationreminder.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.net.Uri;
import android.os.IBinder;
import android.provider.MediaStore.Audio;
import android.text.format.Time;
import android.util.Log;

import com.htwgkonstanz.locationreminder.R;
import com.htwgkonstanz.locationreminder.database.LRDatabaseAdapter;
import com.htwgkonstanz.locationreminder.maps.GoogleMaps;
import com.htwgkonstanz.locationreminder.maps.LocationTuple;
import com.htwgkonstanz.locationreminder.showtasks.ShowNearTasks;

public class LocationProvider extends Service {

	private Timer timer;
	private LRDatabaseAdapter dbAdapter;
	private GoogleMaps googleMaps;
	private LocationTuple currentLocation;

	@Override
	public void onCreate() {
		super.onCreate();

		timer = new Timer();
		dbAdapter = new LRDatabaseAdapter(this);
		dbAdapter.open();
		googleMaps = new GoogleMaps(this);
		startLocationProvider();
	}

	private void startLocationProvider() {
		timer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				Time currentTime = getCurrentTime();
				if (timeIsRight(currentTime)) {
					currentLocation = googleMaps.getLocation();
					if (locationIsRight(currentLocation)) {
						alarmTheUser(currentLocation, currentTime);
					}
				}
			}

			private void alarmTheUser(LocationTuple currentLocation, Time currentTime) {
				ArrayList<Integer> ids = intersect(getTasksIds(currentLocation), getTasksIds(currentTime));
				if (ids.isEmpty())
					return;

				Context context = getApplicationContext();
				NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
				Notification tasksOpen = new Notification();
				tasksOpen.icon = R.drawable.task_solved;
				Resources res = getResources();

				tasksOpen.tickerText = res.getString(R.string.notificationText);
				tasksOpen.when = System.currentTimeMillis();

				Intent notificationIntent = new Intent(context, ShowNearTasks.class);
				notificationIntent.putExtra("IDS", ids);
				PendingIntent intent = PendingIntent.getActivity(context, 0, notificationIntent, android.content.Intent.FLAG_ACTIVITY_NEW_TASK);

				tasksOpen.setLatestEventInfo(context, res.getString(R.string.notificationTitle), res.getString(R.string.notificationText), intent);
				manager.notify(0, tasksOpen);

				SharedPreferences settings = getSharedPreferences("prefs", 0);
				if (settings.getBoolean("ALARM", false)) {
					if (settings.getBoolean("VIBRATOR", false)) {
						Log.i("SERVICE", "RRR RRR");
						tasksOpen.defaults |= Notification.DEFAULT_VIBRATE;
					}
					if (settings.getBoolean("SOUND", false)) {
						Log.i("SERVICE", "RING RING");
						tasksOpen.defaults |= Notification.DEFAULT_SOUND;
						tasksOpen.sound = Uri.parse("file:///sdcard/notification/ringer.mp3");
						tasksOpen.sound = Uri.withAppendedPath(Audio.Media.INTERNAL_CONTENT_URI, "6");
					}
				}

			}

			private ArrayList<Integer> intersect(List<Integer> locationIDs, List<Integer> timeIDs) {
				ArrayList<Integer> ids = new ArrayList<Integer>();
				for (Integer id : locationIDs)
					if (timeIDs.contains(id))
						ids.add(id);
				return ids;
			}

			private boolean locationIsRight(LocationTuple currentLocation) {
				List<Integer> todoItemsIds = getTasksIds(currentLocation);
				return !todoItemsIds.isEmpty();
			}

			private List<Integer> getTasksIds(LocationTuple currentLocation) {
				return dbAdapter.getTasksIds(currentLocation, getRange());
			}

			private double getRange() {
				return getSharedPreferences("prefs", 0).getInt("RANGE", 100);
			}

			private boolean timeIsRight(Time currentTime) {
				List<Integer> todoItemsIds = getTasksIds(currentTime);
				return !todoItemsIds.isEmpty();
			}

			private List<Integer> getTasksIds(Time currentTime) {
				return dbAdapter.getTasksIds(currentTime);
			}

			private Time getCurrentTime() {
				Time currentTime = new Time();
				currentTime.setToNow();
				return currentTime;
			}
		}, 0, getPeriod());
	}

	private long getPeriod() {
		SharedPreferences settings = getSharedPreferences("prefs", 0);
		System.out.println("UPDATE");
		return settings.getInt("TIME", 60000);
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (dbAdapter != null)
			dbAdapter.close();
		if (timer != null)
			timer.cancel();
	}

}
