package com.htwgkonstanz.locationreminder;

import com.htwgkonstanz.locationreminder.database.LRDatabaseAdapter;
import com.htwgkonstanz.locationreminder.database.LRTask;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class LocationReminderActivity extends Activity {
    private Button createNewTaskButton;
	private Button showAllTasksButton;
	private LRTask task;
	private LRDatabaseAdapter dbAdapter;
	private static final int BACK_FROM_NEW = 1;

	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        dbAdapter = new LRDatabaseAdapter(this);
        dbAdapter.open();
        
        int minute = 1048%60;
        int hour = 1048/60;
        System.out.println("hour:" + hour + " minute:" +minute);
        
        showAllTasksButton();
        createNewTaskButton();
        
    }

	private void showAllTasksButton() {
		showAllTasksButton = (Button) findViewById(R.id.main_showAllTaskButton);
        showAllTasksButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(LocationReminderActivity.this, ShowAllTasks.class);
				startActivity(intent);
			}
		});
	}

	private void createNewTaskButton() {
		createNewTaskButton = (Button) findViewById(R.id.main_createNewTaskButton);
        createNewTaskButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(LocationReminderActivity.this, CreateNewTask.class);
				startActivityForResult(intent, BACK_FROM_NEW);
				
			}
		});
	}
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		switch (requestCode) {
		case BACK_FROM_NEW:
			if(resultCode == Activity.RESULT_OK) {
				task = (LRTask) data.getSerializableExtra("POJO");
				System.out.println(task.getTaskName());
				System.out.println(task.getTaskDescription());
				System.out.println(task.getTaskUrgency());
			}
			
			
			
			break;
			
		default:
			break;
		}
	}
}