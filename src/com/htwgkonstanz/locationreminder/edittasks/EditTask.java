package com.htwgkonstanz.locationreminder.edittasks;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.TextView;

import com.htwgkonstanz.locationreminder.R;
import com.htwgkonstanz.locationreminder.database.LRDatabaseAdapter;
import com.htwgkonstanz.locationreminder.database.LRTask;
import com.htwgkonstanz.locationreminder.maps.ChooseLocationOnMap;
import com.htwgkonstanz.locationreminder.maps.LocationTuple;

public class EditTask extends Activity {

	private TextView taskName;
	private RatingBar urgencyRatingBar;
	private Button saveButton;
	private Button cancelButton;
	private LRTask task;
	private int range;
	private TextView taskDescription;
	private int taskUrgency;
	private Button chooseLocationButton;
	private Button specifyDaysButton;
	private LRDatabaseAdapter dbAdapter;

	private static final int BACK_FROM_LOCATION_CHOOSING = 1;
	private static final int BACK_FROM_SPECIFYING_DAYS = 2;
	private LocationTuple locationTuple;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.createnewtask);

		range = 10;
		taskUrgency = 1;

		taskName = (TextView) findViewById(R.id.cnt_taskName);
		taskDescription = (TextView) findViewById(R.id.cnt_taskDescriptionEditText);

		task = (LRTask) getIntent().getSerializableExtra("TASK");
		if (task != null) {
			taskName.setText(task.getTaskName());
			taskDescription.setText(task.getTaskDescription());
			locationTuple = new LocationTuple(task.getTaskLongitude(), task.getTaskLatitude());
		} else {
			locationTuple = new LocationTuple(0, 0);
		}

		dbAdapter = new LRDatabaseAdapter(this);
		dbAdapter.open();

		saveButton();
		cancelButton();
		specifyDaysButton();
		chooseLocationButton();
		urgencySettings();
		urgencySettings();
	}

	private void specifyDaysButton() {
		specifyDaysButton = (Button) findViewById(R.id.cnt_chooseDaysButton);
		specifyDaysButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(EditTask.this, SpecifyDaysAndTime.class);
				intent.putExtra("TASK", task);
				startActivityForResult(intent, BACK_FROM_SPECIFYING_DAYS);
			}
		});
	}

	private void chooseLocationButton() {
		chooseLocationButton = (Button) findViewById(R.id.cnt_chooseLocationButton);
		chooseLocationButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(EditTask.this, ChooseLocationOnMap.class);
				intent.putExtra("TASK", task);
				startActivityForResult(intent, BACK_FROM_LOCATION_CHOOSING);
			}
		});
	}

	private void urgencySettings() {
		urgencyRatingBar = (RatingBar) findViewById(R.id.cnt_setUrgency);
		urgencyRatingBar.setStepSize(1);

		if (task != null) {
			taskUrgency = task.getTaskUrgency();
			urgencyRatingBar.setRating(taskUrgency);
		} else
			urgencyRatingBar.setRating(2);

		urgencyRatingBar.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {

			@Override
			public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {

				taskUrgency = (int) rating;
			}
		});
	}

	private void saveButton() {
		saveButton = (Button) findViewById(R.id.cnt_saveButton);
		// saveButton.setEnabled(taskCanBeSaved);
		saveButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				String description = taskDescription.getText().toString().replace('\n', ' ');
				task.setTaskDescription(description);
				task.setTaskName(taskName.getText().toString());
				task.setTaskRange(range);
				task.setTaskUrgency(taskUrgency);
				task.setTaskRemindType(0);
				task.setTaskLatitude(locationTuple.latitude);
				task.setTaskLongitude(locationTuple.longitude);
				System.out.println(task.getTaskName());

				dbAdapter.updateTask(task);
				// TODO TOAST
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

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		switch (requestCode) {
		case BACK_FROM_LOCATION_CHOOSING:
			if (resultCode == Activity.RESULT_OK) {
				locationTuple = (LocationTuple) data.getSerializableExtra("POINT");
			}
			break;

		case BACK_FROM_SPECIFYING_DAYS:
			if (resultCode == Activity.RESULT_OK) {
				task = (LRTask) data.getSerializableExtra("TASK");
				System.out.println(task.getRemindTimeRanges()[0][0]);
			}

			break;

		default:
			break;
		}
	}
}
