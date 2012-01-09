package com.htwgkonstanz.locationreminder.edittasks;

import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.htwgkonstanz.locationreminder.R;
import com.htwgkonstanz.locationreminder.R.id;
import com.htwgkonstanz.locationreminder.R.layout;
import com.htwgkonstanz.locationreminder.R.string;
import com.htwgkonstanz.locationreminder.database.LRDatabaseAdapter;
import com.htwgkonstanz.locationreminder.database.LRTask;
import com.htwgkonstanz.locationreminder.maps.ChooseLocationOnMap;
import com.htwgkonstanz.locationreminder.maps.LocationTuple;

public class CreateNewTask extends Activity {

	private TextView taskName;
	private RatingBar urgencyRatingBar;
	private Button saveButton;
	private Button cancelButton;
	private LRTask newTask;
	private int range;
	private TextView taskDescription;
	private int taskUrgency;
	private Button chooseLocationButton;
	private Button specifyDaysButton;
	private boolean locationChosen;
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
		taskName.addTextChangedListener(new MyTextWatcher());
		taskDescription = (TextView) findViewById(R.id.cnt_taskDescriptionEditText);
		taskDescription.addTextChangedListener(new MyTextWatcher());

		newTask = new LRTask();
		int[][] time = newTask.getRemindTimeRanges();
		for (int i = 0; i < time.length; i++) {
			time[i][0] = 1;
			time[i][1] = 1439;
		}
		newTask.setRemindTimeRanges(time);

		dbAdapter = new LRDatabaseAdapter(this);
		dbAdapter.open();

		locationChosen = false;
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
				Intent intent = new Intent(CreateNewTask.this, SpecifyDaysAndTime.class);
				intent.putExtra("TASK", newTask);
				startActivityForResult(intent, BACK_FROM_SPECIFYING_DAYS);
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
			public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
				taskUrgency = (int) rating;
			}
		});
	}

	private void saveButton() {
		saveButton = (Button) findViewById(R.id.cnt_saveButton);
		saveButton.setEnabled(locationChosen);
		saveButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				String description = taskDescription.getText().toString().replace('\n', ' ');
				newTask.setTaskDescription(description);
				newTask.setTaskName(taskName.getText().toString());
				newTask.setTaskRange(range);
				newTask.setTaskUrgency(taskUrgency);
				newTask.setTaskCreationDate(new Date(System.currentTimeMillis()));
				newTask.setTaskRemindType(0);
				newTask.setTaskLatitude(locationTuple.latitude);
				newTask.setTaskLongitude(locationTuple.longitude);
				System.out.println(locationTuple);

				dbAdapter.insertNewTask(newTask);
				Toast.makeText(CreateNewTask.this, R.string.newTaskCreated, Toast.LENGTH_LONG).show();
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

	private void updateButtons() {
		saveButton.setEnabled(locationChosen && taskName.getText().length() > 0 && taskDescription.getText().length() > 0);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		switch (requestCode) {
		case BACK_FROM_LOCATION_CHOOSING:
			locationChosen = true;
			if (resultCode == Activity.RESULT_OK) {
				locationTuple = (LocationTuple) data.getSerializableExtra("POINT");
			}
			if (locationTuple == null)
				locationChosen = false;
			updateButtons();
			break;

		case BACK_FROM_SPECIFYING_DAYS:
			if (resultCode == Activity.RESULT_OK) {
				newTask = (LRTask) data.getSerializableExtra("TASK");
			}
			updateButtons();
			break;

		default:
			break;
		}
	}

	private final class MyTextWatcher implements TextWatcher {
		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {
		}

		@Override
		public void afterTextChanged(Editable s) {
			updateButtons();
		}
	}
}
