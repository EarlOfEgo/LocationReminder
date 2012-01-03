package com.htwgkonstanz.locationreminder;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

public class LocationReminderActivity extends Activity {
    private Button createNewTaskButton;
	private Button showAllTasksButton;

	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main);
        
        showAllTasksButton();
        createNewTaskButton();  
        startService(new Intent(this, LocationProvider.class));
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
				startActivity(intent);
				
			}
		});
	}
}