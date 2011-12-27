package com.htwgkonstanz.locationreminder.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class LRDatabaseHelper extends SQLiteOpenHelper{
	private static final String DBNAME = "locationreminderdatabase";
	private static final int DBVERSION = 1;
	public static final String DB_taskName = "task_name";
	public static final String DB_taskID = "task_id";
	public static final String DB_taskLongitude = "task_long";
	public static final String DB_taskLatitude = "task_lat";
	public static final String DB_taskRange = "task_range";
	public static final String DB_taskUrgency = "task_urg";
	public static final String DB_taskReminderType = "task_remt";
	public static final String DB_taskCreationDate = "task_crea";
	public static final String DB_taskExpireDate = "task_exp";
	public static final String DB_taskExecuted = "task_exec";
	public static final String DB_taskMondayFrom = "task_mofro";
	public static final String DB_taskMondayTo = "task_motu";
	public static final String DB_taskTuesdayFrom = "task_tufro";
	public static final String DB_taskTuesdayTo = "task_tuto";
	public static final String DB_taskWednesdayFrom = "task_wefro";
	public static final String DB_taskWednesdayTo = "task_weto";
	public static final String DB_taskThursdayFrom = "task_thufro";
	public static final String DB_taskThursdayTo = "task_thuto";
	public static final String DB_taskFridayFrom = "task_frifro";
	public static final String DB_taskFridayTo = "task_frito";
	public static final String DB_taskSaturdayFrom = "task_safro";
	public static final String DB_taskSaturdayTo = "task_sato";
	public static final String DB_taskSundayFrom = "task_sufro";
	public static final String DB_taskSundayTo = "task_suto";
	
	private static final String LocationReminderCreateTable = 
									"CREATE TABLE " + DB_taskName + " (" +
									DB_taskID  + " INTEGER primary key autoincrement, " +
									DB_taskLongitude + " REAL not NULL, " +
									DB_taskLatitude + " REAL not NULL, " +
									DB_taskRange + " REAL not NULL, " +
									DB_taskUrgency + " INTEGER not NULL, " +
									DB_taskReminderType + " INTEGER not NULL, " +
									DB_taskCreationDate + " LONG not NULL, " +
									DB_taskExpireDate +" LONG, " +
									DB_taskExecuted +" INTEGER not NULL, " +
									DB_taskMondayFrom +" TEXT not NULL, " +
									DB_taskMondayTo +" TEXT not NULL, " +
									DB_taskTuesdayFrom +" TEXT not NULL, " +
									DB_taskTuesdayTo +" TEXT not NULL, " +
									DB_taskWednesdayFrom +" TEXT not NULL, " +
									DB_taskWednesdayTo +" TEXT not NULL, " +
									DB_taskThursdayFrom +" TEXT not NULL, " +
									DB_taskThursdayTo +" TEXT not NULL, " +
									DB_taskFridayFrom +" TEXT not NULL, " +
									DB_taskFridayTo +" TEXT not NULL, " +
									DB_taskSaturdayFrom +" TEXT not NULL, " +
									DB_taskSaturdayTo +" TEXT not NULL, " +
									DB_taskSundayFrom +" TEXT not NULL, " +
									DB_taskSundayTo +" TEXT not NULL);";
	
	
	public LRDatabaseHelper(Context context) {
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
