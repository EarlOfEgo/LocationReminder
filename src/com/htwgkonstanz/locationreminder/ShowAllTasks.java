package com.htwgkonstanz.locationreminder;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.htwgkonstanz.locationreminder.database.LRDatabaseAdapter;
import com.htwgkonstanz.locationreminder.database.LRDatabaseHelper;

public class ShowAllTasks extends Activity {
	
	private LRDatabaseAdapter dbAdapter;
	private Cursor cursor;
	private MyCursorAdapter cursorAdapter;
	private ListView listOfTasks;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.listoftasks);
		
		dbAdapter = new LRDatabaseAdapter(this);
		dbAdapter.open();
		
		listOfTasks = (ListView) findViewById(R.id.listoftasks_listView);
		if(listOfTasks == null) System.out.println("ICH");
		
		populateDate();
	}
	
	
	private void populateDate() {
		cursor = dbAdapter.getAllTasks();
		String[] from = new String[] {LRDatabaseHelper.DB_taskName};
		int[] to = new int[]{R.id.listitem_taskName};
		cursorAdapter = new MyCursorAdapter(this, R.layout.listitem, cursor, from, to);
		
		listOfTasks.setAdapter(cursorAdapter);
	}
	
	
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if(dbAdapter != null)
			dbAdapter.close();
	}

	private class MyCursorAdapter extends SimpleCursorAdapter {

		public MyCursorAdapter(Context context, int layout, Cursor c, String[] from, int[] to) {
			super(context, layout, c, from, to);
		}
		
		@Override
		public View newView(Context context, Cursor cursor, ViewGroup parent) {
			View view = super.newView(context, cursor, parent);
			TextView taskName = (TextView) view.findViewById(R.id.listitem_taskName);
			View urgencyView = (View) view.findViewById(R.id.listitem_urgencyView);
			ImageView picture = (ImageView) view.findViewById(R.id.listItemPicture);
			taskName.setText(cursor.getString(cursor.getColumnIndex(LRDatabaseHelper.DB_taskName)));
			int urgency = cursor.getInt(cursor.getColumnIndex(LRDatabaseHelper.DB_taskUrgency));
			if(urgency <= 2)
				urgencyView.setBackgroundColor(Color.GREEN);
			else if(urgency > 2 && urgency <= 4)
				urgencyView.setBackgroundColor(Color.YELLOW);
			else
				urgencyView.setBackgroundColor(Color.RED);
			if(cursor.getInt(cursor.getColumnIndex(LRDatabaseHelper.DB_taskExecuted)) != 0)
				picture.setImageResource(R.drawable.task_solved);
			else 
				picture.setImageResource(R.drawable.task_button);
			
			return view;
		}
		
	}
}
