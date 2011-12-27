package com.htwgkonstanz.locationreminder.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class LRDatabaseAdapter {
	private Context context;
	private LRDatabaseHelper dbHelper;
	private SQLiteDatabase database;
	
	public LRDatabaseAdapter(Context context) {
		this.context = context;
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
		
		return database.insert(LRDatabaseHelper.DB_taskName, null, contentValues);
	}
	
	public int removeTask(int id) {
		return database.delete(LRDatabaseHelper.DB_taskName, LRDatabaseHelper.DB_taskID + "=" + id, null);
	}
	
	public Cursor getTask(int id) {
		String select = "SELECT * FROM " + LRDatabaseHelper.DB_taskName + " WHERE " + LRDatabaseHelper.DB_taskID + " == " +id;
		Cursor cursor = database.rawQuery(select, null);
		if(cursor != null)
			cursor.moveToFirst();
		return cursor;
	}
	
	public int updateTask(LRTask task) {
		ContentValues contentValues = createAllContentValues(task);
		
		return database.update(LRDatabaseHelper.DB_taskName, contentValues, LRDatabaseHelper.DB_taskID + " = " + task.getTaskId(), null);
	}

	private ContentValues createAllContentValues(LRTask task) {
		ContentValues contentValues = new ContentValues();
		
		contentValues.put(LRDatabaseHelper.DB_taskName, task.getTaskName());
		contentValues.put(LRDatabaseHelper.DB_taskExecuted, 0);
		contentValues.put(LRDatabaseHelper.DB_taskLatitude, task.getTaskLatitude());
		contentValues.put(LRDatabaseHelper.DB_taskLongitude, task.getTaskLongitude());
		contentValues.put(LRDatabaseHelper.DB_taskCreationDate, task.getTaskCreationDate().getTime());
		contentValues.put(LRDatabaseHelper.DB_taskRange, task.getTaskRange());
		
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
}