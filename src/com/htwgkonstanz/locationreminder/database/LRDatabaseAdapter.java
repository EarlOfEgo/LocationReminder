package com.htwgkonstanz.locationreminder.database;

import java.util.ArrayList;
import java.util.List;

import com.htwgkonstanz.locationreminder.LocationTuple;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.text.format.Time;

import static com.htwgkonstanz.locationreminder.CollectionTools.*;

public class LRDatabaseAdapter {
	private Context context;
	private LRDatabaseHelper dbHelper;
	private SQLiteDatabase database;

	public LRDatabaseAdapter(Context context) {
		this.context = context;
	}

	public Cursor getAllTasks() {
		return database.query(LRDatabaseHelper.DBNAME, new String[] { LRDatabaseHelper.DB_taskID, LRDatabaseHelper.DB_taskName, LRDatabaseHelper.DB_taskDescription, LRDatabaseHelper.DB_taskLongitude, LRDatabaseHelper.DB_taskLatitude, LRDatabaseHelper.DB_taskRange, LRDatabaseHelper.DB_taskUrgency, LRDatabaseHelper.DB_taskReminderType, LRDatabaseHelper.DB_taskCreationDate, LRDatabaseHelper.DB_taskExpireDate, LRDatabaseHelper.DB_taskExecuted, LRDatabaseHelper.DB_taskMondayFrom, LRDatabaseHelper.DB_taskMondayTo, LRDatabaseHelper.DB_taskTuesdayFrom, LRDatabaseHelper.DB_taskTuesdayTo, LRDatabaseHelper.DB_taskWednesdayFrom, LRDatabaseHelper.DB_taskWednesdayTo, LRDatabaseHelper.DB_taskThursdayFrom, LRDatabaseHelper.DB_taskThursdayTo, LRDatabaseHelper.DB_taskFridayFrom, LRDatabaseHelper.DB_taskFridayTo, LRDatabaseHelper.DB_taskSaturdayFrom, LRDatabaseHelper.DB_taskSaturdayTo, LRDatabaseHelper.DB_taskSundayFrom, LRDatabaseHelper.DB_taskSundayTo }, null, null, null, null, null);
	}

	public LRDatabaseAdapter open() throws SQLException {
		dbHelper = new LRDatabaseHelper(context);
		database = dbHelper.getWritableDatabase();
		return this;
	}

	public void close() {
		dbHelper.close();
	}

	public long insertNewTask(LRTask task) {
		ContentValues contentValues = createAllContentValues(task);

		return database.insert(LRDatabaseHelper.DBNAME, null, contentValues);
	}

	public int removeTask(int id) {
		return database.delete(LRDatabaseHelper.DBNAME, LRDatabaseHelper.DB_taskID + "=" + id, null);
	}

	public Cursor getTask(int id) {
		String select = "SELECT * FROM " + LRDatabaseHelper.DBNAME + " WHERE " + LRDatabaseHelper.DB_taskID + " == " + id;
		Cursor cursor = database.rawQuery(select, null);
		if (cursor != null)
			cursor.moveToFirst();
		return cursor;
	}

	public int updateTask(LRTask task) {
		ContentValues contentValues = createAllContentValues(task);

		return database.update(LRDatabaseHelper.DBNAME, contentValues, LRDatabaseHelper.DB_taskID + " = " + task.getTaskId(), null);
	}

	private ContentValues createAllContentValues(LRTask task) {
		ContentValues contentValues = new ContentValues();

		contentValues.put(LRDatabaseHelper.DB_taskName, task.getTaskName());
		contentValues.put(LRDatabaseHelper.DB_taskExecuted, 0);
		contentValues.put(LRDatabaseHelper.DB_taskLatitude, task.getTaskLatitude());
		contentValues.put(LRDatabaseHelper.DB_taskLongitude, task.getTaskLongitude());
		contentValues.put(LRDatabaseHelper.DB_taskCreationDate, task.getTaskCreationDate().getTime());
		contentValues.put(LRDatabaseHelper.DB_taskRange, task.getTaskRange());
		contentValues.put(LRDatabaseHelper.DB_taskDescription, task.getTaskDescription());
		contentValues.put(LRDatabaseHelper.DB_taskReminderType, task.getTaskRemindType());
		contentValues.put(LRDatabaseHelper.DB_taskUrgency, task.getTaskUrgency());

		contentValues.put(LRDatabaseHelper.DB_taskMondayFrom, task.getRemindFromSpecific(0));
		contentValues.put(LRDatabaseHelper.DB_taskTuesdayFrom, task.getRemindFromSpecific(1));
		contentValues.put(LRDatabaseHelper.DB_taskWednesdayFrom, task.getRemindFromSpecific(2));
		contentValues.put(LRDatabaseHelper.DB_taskThursdayFrom, task.getRemindFromSpecific(3));
		contentValues.put(LRDatabaseHelper.DB_taskFridayFrom, task.getRemindFromSpecific(4));
		contentValues.put(LRDatabaseHelper.DB_taskSaturdayFrom, task.getRemindFromSpecific(5));
		contentValues.put(LRDatabaseHelper.DB_taskSundayFrom, task.getRemindFromSpecific(6));

		contentValues.put(LRDatabaseHelper.DB_taskMondayTo, task.getRemindToSpecific(0));
		contentValues.put(LRDatabaseHelper.DB_taskTuesdayTo, task.getRemindToSpecific(1));
		contentValues.put(LRDatabaseHelper.DB_taskWednesdayTo, task.getRemindToSpecific(2));
		contentValues.put(LRDatabaseHelper.DB_taskThursdayTo, task.getRemindToSpecific(3));
		contentValues.put(LRDatabaseHelper.DB_taskFridayTo, task.getRemindToSpecific(4));
		contentValues.put(LRDatabaseHelper.DB_taskSaturdayTo, task.getRemindToSpecific(5));
		contentValues.put(LRDatabaseHelper.DB_taskSundayTo, task.getRemindToSpecific(6));
		return contentValues;
	}

	public List<Integer> getTasksIds(Time currentTime) {
		String query = getTimeDependentQuery(currentTime);
		return getAllTasksIds(query);
	}

	private String getTimeDependentQuery(Time currentTime) {
		int timeInMinutes = currentTime.hour * 60 + currentTime.minute;
		String[] daysFrom = { LRDatabaseHelper.DB_taskSundayFrom, LRDatabaseHelper.DB_taskMondayFrom, LRDatabaseHelper.DB_taskTuesdayFrom, LRDatabaseHelper.DB_taskWednesdayFrom, LRDatabaseHelper.DB_taskThursdayFrom, LRDatabaseHelper.DB_taskFridayFrom, LRDatabaseHelper.DB_taskSaturdayFrom };
		String[] daysTo = { LRDatabaseHelper.DB_taskSundayTo, LRDatabaseHelper.DB_taskMondayTo, LRDatabaseHelper.DB_taskTuesdayTo, LRDatabaseHelper.DB_taskWednesdayTo, LRDatabaseHelper.DB_taskThursdayTo, LRDatabaseHelper.DB_taskFridayTo, LRDatabaseHelper.DB_taskSaturdayTo };
		String query = "SELECT * FROM " + LRDatabaseHelper.DBNAME + " WHERE " + daysFrom[currentTime.weekDay] + " <= " + timeInMinutes + " AND " + daysTo[currentTime.weekDay] + " >= " + timeInMinutes + ";";
		return query;
	}

	private List<Integer> getAllTasksIds(String query) {
		Cursor cursor = database.rawQuery(query, null);
		List<Integer> ids = new ArrayList<Integer>();
		if (cursor != null) {
			cursor.moveToFirst();
			while (!cursor.isAfterLast()) {
				ids.add(cursor.getInt(cursor.getColumnIndex(LRDatabaseHelper.DB_taskID)));
				cursor.moveToNext();
			}
			cursor.close();
		}
		return ids;
	}

	public List<Integer> getTasksIds(LocationTuple currentLocation, double range) {
		String query = getLocationDependentQuery(currentLocation);
		Cursor cursor = database.rawQuery(query, null);
		List<Integer> ids = new ArrayList<Integer>();
		if (cursor != null) {
			cursor.moveToFirst();

			while (!cursor.isAfterLast()) {
				double latitude = cursor.getDouble(cursor.getColumnIndex(LRDatabaseHelper.DB_taskLatitude));
				double longitude = cursor.getDouble(cursor.getColumnIndex(LRDatabaseHelper.DB_taskLongitude));
				float[] resultsc = new float[10];
				Location.distanceBetween(currentLocation.latitude, currentLocation.longitude, latitude, longitude, resultsc);
				if (range >= resultsc[0])
					ids.add(cursor.getInt(cursor.getColumnIndex(LRDatabaseHelper.DB_taskID)));
				cursor.moveToNext();
			}
			cursor.close();
		}

		return ids;
	}

	private String getLocationDependentQuery(LocationTuple currentLocation) {
		return "SELECT * FROM " + LRDatabaseHelper.DBNAME + ";";

	}

	public Cursor getNearTasks(ArrayList<Integer> ids) {
		String query = getQueryByIDs(ids);
		System.out.println(query);
		Cursor cursor = database.rawQuery(query, null);
		if (cursor != null)
			cursor.moveToFirst();
		return cursor;
	}

	private String getQueryByIDs(ArrayList<Integer> ids) {
		String seperator = " == " + LRDatabaseHelper.DB_taskID + " OR ";
		String where = makeString(ids, seperator) + " == " + LRDatabaseHelper.DB_taskID;
		return "SELECT * FROM " + LRDatabaseHelper.DBNAME + " WHERE " + where + ";" ;
	}

	public void deleteTask(int id) {
		database.delete(LRDatabaseHelper.DBNAME, LRDatabaseHelper.DB_taskID + "=" + id, null);
	}

	public void completeTask(int id) {
		ContentValues values = new ContentValues();
		values.put(LRDatabaseHelper.DB_taskExecuted, 1);
		database.update(LRDatabaseHelper.DBNAME, values, LRDatabaseHelper.DB_taskID + " = " + id, null);
	}
}
