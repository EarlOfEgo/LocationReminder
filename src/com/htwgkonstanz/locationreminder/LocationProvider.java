package com.htwgkonstanz.locationreminder;

import java.util.ArrayList;
import java.util.Arrays;
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
import android.os.IBinder;
import android.text.format.Time;

import com.htwgkonstanz.locationreminder.database.LRDatabaseAdapter;

public class LocationProvider extends Service {

	private Timer timer;
	private LRDatabaseAdapter dbAdapter;
	private GoogleMaps googleMaps;
	

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
					System.out.println("time is right");
					LocationTuple currentLocation = googleMaps.getLocation();
					System.out.println(currentLocation);
					if (locationIsRight(currentLocation)) {
						System.out.println("location is right");
						alarmTheUser(currentLocation, currentTime);
					}
				}
			}

			private void alarmTheUser(LocationTuple currentLocation, Time currentTime) {
				System.out.println(getTasksIds(currentLocation));
				ArrayList<Integer> ids = intersect(getTasksIds(currentLocation), getTasksIds(currentTime));
				if(ids.isEmpty()) 
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
				System.out.println(settings.getBoolean("ALARM", false));
				if(settings.getBoolean("ALARM", false)) {
					if(settings.getBoolean("VIBRATOR", false)) {
						System.out.println("RRR RRR");
					}
					if(settings.getBoolean("SOUND", false)) {
						System.out.println("RING RING");
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
		// TODO Auto-generated method stub
		return 1000;
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (timer != null)
			timer.cancel();
	}

}
