package com.htwgkonstanz.locationreminder;

import com.htwgkonstanz.locationreminder.database.LRTask;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.ToggleButton;

public class CreateNewTask extends Activity {

	private TextView taskName;
	private ToggleButton locationButton;
	private CheckBox specifyDaysCheckBox;
	private RatingBar urgencyRatingBar;
	private Button specifyModeButton;
	private Button saveButton;
	private Button cancelButton;
	private Boolean taskCanBeSaved;
	private LRTask newTask;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.createnewtask);
		
		taskName =(TextView) findViewById(R.id.cnt_taskName);
//		locationButton = (ToggleButton) findViewById(R.id.cnt_locationButton);
		specifyDaysCheckBox = (CheckBox) findViewById(R.id.cnt_checkBox_specify_days);
		urgencyRatingBar = (RatingBar) findViewById(R.id.cnt_setUrgency);
		
		newTask = new LRTask();
		
		taskCanBeSaved = false;
		saveButton();
		cancelButton();
		specifyModeButton();
	}
	
	private void saveButton() {
		saveButton = (Button) findViewById(R.id.cnt_saveButton);
		saveButton.setEnabled(taskCanBeSaved);
		saveButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//TODO				
				newTask.setTaskName((String) taskName.getText());
			}
		});
	}
	
	
	private void cancelButton() {
		cancelButton = (Button) findViewById(R.id.cnt_cancelButton);
		cancelButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}
	
	
	private void specifyModeButton() {
		specifyModeButton = (Button) findViewById(R.id.cnt_specifyModeButton);
		specifyModeButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//TODO
			}
		});
	}
	
	
}
