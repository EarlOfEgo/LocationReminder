package com.htwgkonstanz.locationreminder.showtasks;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.htwgkonstanz.locationreminder.R;
import com.htwgkonstanz.locationreminder.database.LRDatabaseAdapter;
import com.htwgkonstanz.locationreminder.database.LRDatabaseHelper;
import com.htwgkonstanz.locationreminder.database.LRTask;
import com.htwgkonstanz.locationreminder.edittasks.EditTask;

public class ShowNearTasks extends Activity {
	
	private LRDatabaseAdapter dbAdapter;
	private Cursor cursor;
	private MyCursorAdapter cursorAdapter;
	private ListView listOfTasks;
	private ArrayList<Integer> ids;
	private int id;
	private boolean completed;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.listoftasks);
		
		dbAdapter = new LRDatabaseAdapter(this);
		dbAdapter.open();
		
		listOfTasks = (ListView) findViewById(R.id.listoftasks_listView);
		registerForContextMenu(listOfTasks);
		ids = getIntent().getIntegerArrayListExtra("IDS");
		if(ids == null)
			ids = new ArrayList<Integer>();
		
		listOfTasks.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				updatePosition(position);
				openContextMenu(view);
			}

			private void updatePosition(int position) {
				Cursor cursor = (Cursor) cursorAdapter.getItem(position);
				id = cursor.getInt(cursor.getColumnIndex(LRDatabaseHelper.DB_taskID));
				completed = cursor.getInt(cursor.getColumnIndex(LRDatabaseHelper.DB_taskExecuted)) == 0;
			}
		});
		
		populateDate();
	}
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		MenuInflater inflater = getMenuInflater();
		if (completed)
			inflater.inflate(R.menu.listmenucompleted, menu);
		else
			inflater.inflate(R.menu.listmenu, menu);
	}
	
	
	private void populateDate() {
		cursor = dbAdapter.getNearTasks(ids);
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
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		Intent intent;
		LRTask task;
		switch (item.getItemId()) {
		case R.id.showTask:
			intent = new Intent(this, ShowTask.class);
			task = new LRTask();
			task = dbAdapter.getFullTaskById(id);
			intent.putExtra("TASK", task);
			startActivity(intent);
			break;

		case R.id.editTask:
			intent = new Intent(this, EditTask.class);
			task = new LRTask();
			task = dbAdapter.getFullTaskById(id);
			intent.putExtra("TASK", task);
			startActivityForResult(intent, 10);
			break;
		case R.id.deleteTask:
			dbAdapter.deleteTask(id);

			break;

		case R.id.completeTask:
			dbAdapter.completeTask(id);
			break;
		}
		populateDate();
		return true;
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		populateDate();
		super.onActivityResult(requestCode, resultCode, data);
	}
}
