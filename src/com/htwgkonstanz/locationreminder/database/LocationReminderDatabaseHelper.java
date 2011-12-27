package com.htwgkonstanz.locationreminder.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class LocationReminderDatabaseHelper extends SQLiteOpenHelper{
	private static final String DBNAME = "locationreminderdatabase";
	private static final int DBVERSION = 1;
	private static final String DB_taskName = "task_name";
	private static final String DB_taskID = "task_id";
	private static final String DB_longitude = "task_long";
	private static final String DB_latitude = "task_lat";
	private static final String DB_taskCategory = "task_cate";
	private static final String DB_taskUrgency = "task_urg";
	private static final String DB_taskReminderType = "task_remt";
	private static final String DB_taskCreationDate = "task_crea";
	private static final String DB_taskExpireDate = "task_exp";
	private static final String DB_taskExecuted = "task_exec";
	private static final String DB_taskMondayFrom = "task_mofro";
	private static final String DB_taskMondayTo = "task_motu";
	private static final String DB_taskTuesdayFrom = "task_tufro";
	private static final String DB_taskTuesdayTo = "task_tuto";
	private static final String DB_taskWednesdayFrom = "task_wefro";
	private static final String DB_taskWednesdayto = "task_weto";
	private static final String DB_taskThursdayFrom = "task_thufro";
	private static final String DB_taskThursdayTo = "task_thuto";
	private static final String DB_taskFridayFrom = "task_frifro";
	private static final String DB_taskFridayTo = "task_frito";
	private static final String DB_taskSaturdayFrom = "task_safro";
	private static final String DB_taskSaturdayTo = "task_sato";
	private static final String DB_taskSundayFrom = "task_sufro";
	private static final String DB_taskSundayTo = "task_suto";
	
	private static final String LocationReminderCreateTable = 
									"CREATE TABLE " + DB_taskName + " (" +
									DB_taskID  + " INTEGER primary key autoincrement, " +
									DB_longitude + " REAL not NULL, " +
									DB_latitude + " REAL not NULL, " +
									DB_taskCategory + " TEXT not NULL, " +
									DB_taskUrgency + " INTEGER not NULL, " +
									DB_taskReminderType + " INTEGER not NULL, " +
									DB_taskCreationDate + " LONG not NULL, " +
									DB_taskExpireDate +" LONG not NULL, " +
									DB_taskExecuted +" INTEGER not NULL, " +
									DB_taskMondayFrom +" TEXT not NULL, " +
									DB_taskMondayTo +" TEXT not NULL, " +
									DB_taskTuesdayFrom +" TEXT not NULL, " +
									DB_taskTuesdayTo +" TEXT not NULL, " +
									DB_taskWednesdayFrom +" TEXT not NULL, " +
									DB_taskWednesdayto +" TEXT not NULL, " +
									DB_taskThursdayFrom +" TEXT not NULL, " +
									DB_taskThursdayTo +" TEXT not NULL, " +
									DB_taskFridayFrom +" TEXT not NULL, " +
									DB_taskFridayTo +" TEXT not NULL, " +
									DB_taskSaturdayFrom +" TEXT not NULL, " +
									DB_taskSaturdayTo +" TEXT not NULL, " +
									DB_taskSundayFrom +" TEXT not NULL, " +
									DB_taskSundayTo +" TEXT not NULL);";
	
	
	public LocationReminderDatabaseHelper(Context context, String name,
			CursorFactory factory, int version) {
		super(context, DBNAME, null, DBVERSION);
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(LocationReminderCreateTable);
	}
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + DB_taskName);
		onCreate(db);
	}
}
