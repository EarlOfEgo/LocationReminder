package com.htwgkonstanz.locationreminder;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.SeekBar;
import android.widget.TextView;

import com.htwgkonstanz.locationreminder.database.LRTask;

public class CreateNewTask extends Activity implements SeekBar.OnSeekBarChangeListener {

	private TextView taskName;
	private RatingBar urgencyRatingBar;
	private Button specifyModeButton;
	private Button saveButton;
	private Button cancelButton;
	private Boolean taskCanBeSaved;
	private LRTask newTask;
	private SeekBar rangeSeekBar;
	private TextView rangeText;
	private int range;
	private TextView taskDescription;
	private int taskUrgency;
	private Button chooseLocationButton;
	private Button specifyDaysButton;
	private boolean locationChosen;
	
	private static final int BACK_FROM_LOCATION_CHOOSING = 1;
	private static final int BACK_FROM_SPECIFYING_DAYS = 2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.createnewtask);
		
		range = 10;
		taskUrgency = 1;
		
		taskName = (TextView) findViewById(R.id.cnt_taskName);
		taskDescription = (TextView) findViewById(R.id.cnt_taskDescriptionEditText);
		
		rangeSeekBar = (SeekBar) findViewById(R.id.cnt_specifyRangeSeekBar);
		rangeSeekBar.setOnSeekBarChangeListener(this);
		rangeText = (TextView) findViewById(R.id.cnt_range_count);
		
		newTask = new LRTask();
		
		taskCanBeSaved = false;
		saveButton();
		cancelButton();
		specifyModeButton();
		specifyDaysButton();
		chooseLocationButton();
		urgencySettings();
		urgencySettings();
		updateDisplayedInformations();
	}

	private void specifyDaysButton() {
		specifyDaysButton = (Button) findViewById(R.id.cnt_chooseDaysButton);
		specifyDaysButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
			}
		});
	}

	private void chooseLocationButton() {
		chooseLocationButton = (Button) findViewById(R.id.cnt_chooseLocationButton);
		chooseLocationButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(CreateNewTask.this, ChooseLocationOnMap.class);
				startActivityForResult(intent, BACK_FROM_LOCATION_CHOOSING);
			}
		});
	}

	private void urgencySettings() {
		urgencyRatingBar = (RatingBar) findViewById(R.id.cnt_setUrgency);
		urgencyRatingBar.setStepSize(1);
		urgencyRatingBar.setRating(2);
		urgencyRatingBar.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {
			
			@Override
			public void onRatingChanged(RatingBar ratingBar, float rating,
					boolean fromUser) {
				taskUrgency = (int) rating;
			}
		});
	}
	
	private void saveButton() {
		saveButton = (Button) findViewById(R.id.cnt_saveButton);
		saveButton.setEnabled(taskCanBeSaved);
		saveButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				newTask.setTaskDescription("" + taskDescription.getText());
				newTask.setTaskName("" + taskName.getText());
				newTask.setTaskRange(range);
				newTask.setTaskUrgency(taskUrgency);
				
				Intent intent = new Intent();
				intent.putExtra("POJO", newTask);
				if(getParent() == null)
					setResult(Activity.RESULT_OK, intent);
				else
					getParent().setResult(Activity.RESULT_OK, intent);
				finish();
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
				Intent intent = new Intent(CreateNewTask.this, SpecifyDaysAndTime.class);
				startActivityForResult(intent, BACK_FROM_SPECIFYING_DAYS);
			}
		});
	}

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {
		range = (progress + 5)*2;
		updateDisplayedInformations();
		
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
		updateDisplayedInformations();		
	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		updateDisplayedInformations();		
	}	
	
	
	private void updateDisplayedInformations() {
		rangeText.setText("" + range + " m");
	}
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		
		switch (requestCode) {
		case BACK_FROM_LOCATION_CHOOSING:
			//TODO
			locationChosen = true;
			break;
		
		case BACK_FROM_SPECIFYING_DAYS:
			//TODO
			break;
			
		default:
			break;
		}
	}
}
