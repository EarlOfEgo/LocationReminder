package com.htwgkonstanz.locationreminder.edittasks;

import android.app.Activity;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.htwgkonstanz.locationreminder.R;
import com.htwgkonstanz.locationreminder.database.LRTask;

public class SpecifyDaysAndTime extends Activity {

	private Button okButton;
	private Button cancelButton;
	private TextView mondayFrom;
	private TextView tuesdayFrom;
	private TextView wednesdayFrom;
	private TextView thursdayFrom;
	private TextView fridayFrom;
	private TextView saturdayFrom;
	private TextView sundayFrom;
	private TextView mondayTo;
	private TextView tuesdayTo;
	private TextView wednesdayTo;
	private TextView thursdayTo;
	private TextView fridayTo;
	private TextView saturdayTo;
	private TextView sundayTo;
	private CheckBox mondayCheckBox;
	private CheckBox tuesdayCheckBox;
	private CheckBox wednesdayCheckBox;
	private CheckBox thursdayCheckBox;
	private CheckBox fridayCheckBox;
	private CheckBox saturdayCheckBox;
	private CheckBox sundayCheckBox;

	private static final int SUNDAY = 0;
	private static final int MONDAY = 1;
	private static final int TUESDAY = 2;
	private static final int WEDNESDAY = 3;
	private static final int THURSDAY = 4;
	private static final int FRIDAY = 5;
	private static final int SATURDAY = 6;

	private static final int SUNDAY_FROM = 0;
	private static final int MONDAY_FROM = 1;
	private static final int TUESDAY_FROM = 2;
	private static final int WEDNESDAY_FROM = 3;
	private static final int THURSDAY_FROM = 4;
	private static final int FRIDAY_FROM = 5;
	private static final int SATURDAY_FROM = 6;

	private static final int SUNDAY_TO = 7;
	private static final int MONDAY_TO = 8;
	private static final int TUESDAY_TO = 9;
	private static final int WEDNESDAY_TO = 10;
	private static final int THURSDAY_TO = 11;
	private static final int FRIDAY_TO = 12;
	private static final int SATURDAY_TO = 13;

	private static final int HOUR = 0;
	private static final int MINUTE = 1;

	String[][] fromAndTo = new String[7][2];

	private int[][] fromTime = new int[7][2]; // H M
	private int[][] toTime = new int[7][2]; // H M
	private GridLayout gridMonday;
	private GridLayout gridTuesday;
	private GridLayout gridWednesday;
	private GridLayout gridThursday;
	private GridLayout gridFriday;
	private GridLayout gridSaturday;
	private GridLayout gridSunday;

	private boolean[] everythingOk = new boolean[7];

	private LRTask task;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.chooseremindingtime);

		task = (LRTask) getIntent().getSerializableExtra("TASK");

		okButton();
		cancelButton();
		getCheckBoxes();
		getTextFields();
		getGrids();

		if (task == null) {
			task = new LRTask();
			tuesdayCheckBox.setChecked(true);
			mondayCheckBox.setChecked(true);
			wednesdayCheckBox.setChecked(true);
			thursdayCheckBox.setChecked(true);
			fridayCheckBox.setChecked(true);
			saturdayCheckBox.setChecked(true);
			sundayCheckBox.setChecked(true);
			for (int i = 0; i < 7; i++) {
				everythingOk[i] = true;
				fromTime[i][0] = 0;
				fromTime[i][1] = 1;
				toTime[i][0] = 23;
				toTime[i][1] = 59;
			}
			copyFromToTime();

		} else {
			for (int i = 0; i < 7; i++) {
				fromTime[i][0] = task.getRemindFromSpecific(i) / 60;
				fromTime[i][1] = task.getRemindFromSpecific(i) % 60;
				toTime[i][0] = task.getRemindToSpecific(i) / 60;
				toTime[i][1] = task.getRemindToSpecific(i) % 60;
				everythingOk[i] = true;
			}
			mondayCheckBox.setChecked((task.getRemindFromSpecific(MONDAY) == 0 && task.getRemindToSpecific(MONDAY) == 0) ? false : true);
			tuesdayCheckBox.setChecked((task.getRemindFromSpecific(TUESDAY) == 0 && task.getRemindToSpecific(TUESDAY) == 0) ? false : true);
			wednesdayCheckBox.setChecked((task.getRemindFromSpecific(WEDNESDAY) == 0 && task.getRemindToSpecific(WEDNESDAY) == 0) ? false : true);
			thursdayCheckBox.setChecked((task.getRemindFromSpecific(THURSDAY) == 0 && task.getRemindToSpecific(THURSDAY) == 0) ? false : true);
			fridayCheckBox.setChecked((task.getRemindFromSpecific(FRIDAY) == 0 && task.getRemindToSpecific(FRIDAY) == 0) ? false : true);
			saturdayCheckBox.setChecked((task.getRemindFromSpecific(SATURDAY) == 0 && task.getRemindToSpecific(SATURDAY) == 0) ? false : true);
			sundayCheckBox.setChecked((task.getRemindFromSpecific(SUNDAY) == 0 && task.getRemindToSpecific(SUNDAY) == 0) ? false : true);

		}

		enOrDisableTextFields();
		updateDisplays();
	}

	private void copyFromToTime() {
		int[][] allranges = new int[7][2];
		for (int i = 0; i < allranges.length; i++) {
			allranges[i][0] = (fromTime[i][0] * 60) + fromTime[i][1];
			allranges[i][1] = (toTime[i][0] * 60) + toTime[i][1];
		}
		task.setRemindTimeRanges(allranges);
	}

	private void getGrids() {
		gridMonday = (GridLayout) findViewById(R.id.grid_monday);
		gridTuesday = (GridLayout) findViewById(R.id.grid_tuesday);
		gridWednesday = (GridLayout) findViewById(R.id.grid_wednesday);
		gridThursday = (GridLayout) findViewById(R.id.grid_thursday);
		gridFriday = (GridLayout) findViewById(R.id.grid_friday);
		gridSaturday = (GridLayout) findViewById(R.id.grid_saturday);
		gridSunday = (GridLayout) findViewById(R.id.grid_sunday);
	}

	private void enOrDisableTextFields() {
		mondayFrom.setEnabled(mondayCheckBox.isChecked());
		mondayTo.setEnabled(mondayCheckBox.isChecked());
		tuesdayFrom.setEnabled(tuesdayCheckBox.isChecked());
		tuesdayTo.setEnabled(tuesdayCheckBox.isChecked());
		wednesdayFrom.setEnabled(wednesdayCheckBox.isChecked());
		wednesdayTo.setEnabled(wednesdayCheckBox.isChecked());
		thursdayFrom.setEnabled(thursdayCheckBox.isChecked());
		thursdayTo.setEnabled(thursdayCheckBox.isChecked());
		fridayFrom.setEnabled(fridayCheckBox.isChecked());
		fridayTo.setEnabled(fridayCheckBox.isChecked());
		saturdayFrom.setEnabled(saturdayCheckBox.isChecked());
		saturdayTo.setEnabled(saturdayCheckBox.isChecked());
		sundayFrom.setEnabled(sundayCheckBox.isChecked());
		sundayTo.setEnabled(sundayCheckBox.isChecked());
		checkBoxesUncheck();
		updateDisplays();
	}

	private void checkBoxesUncheck() {
		if (!mondayCheckBox.isChecked()) {
			setTimeToZero(MONDAY);
			gridMonday.setBackgroundColor(Color.TRANSPARENT);
		} else if (checkTime(MONDAY)) {
			setDefaultTime(MONDAY);
		}
		if (!tuesdayCheckBox.isChecked()) {
			setTimeToZero(TUESDAY);
			gridTuesday.setBackgroundColor(Color.TRANSPARENT);
		} else if (checkTime(TUESDAY)) {
			setDefaultTime(TUESDAY);
		}
		if (!wednesdayCheckBox.isChecked()) {
			setTimeToZero(WEDNESDAY);
			gridWednesday.setBackgroundColor(Color.TRANSPARENT);
		} else if (checkTime(WEDNESDAY)) {
			setDefaultTime(WEDNESDAY);
		}
		if (!thursdayCheckBox.isChecked()) {
			setTimeToZero(THURSDAY);
			gridThursday.setBackgroundColor(Color.TRANSPARENT);
		} else if (checkTime(THURSDAY)) {
			setDefaultTime(THURSDAY);
		}
		if (!fridayCheckBox.isChecked()) {
			setTimeToZero(FRIDAY);
			gridFriday.setBackgroundColor(Color.TRANSPARENT);
		} else if (checkTime(FRIDAY)) {
			setDefaultTime(FRIDAY);
		}
		if (!saturdayCheckBox.isChecked()) {
			setTimeToZero(SATURDAY);
			gridSaturday.setBackgroundColor(Color.TRANSPARENT);
		} else if (checkTime(SATURDAY)) {
			setDefaultTime(SATURDAY);
		}
		if (!sundayCheckBox.isChecked()) {
			setTimeToZero(SUNDAY);
			gridSunday.setBackgroundColor(Color.TRANSPARENT);
		} else if (checkTime(SUNDAY)) {
			setDefaultTime(SUNDAY);
		}
	}

	private void setDefaultTime(int day) {
		fromTime[day][HOUR] = 0;
		fromTime[day][MINUTE] = 1;
		toTime[day][HOUR] = 23;
		toTime[day][MINUTE] = 59;
	}

	private void setTimeToZero(int day) {
		fromTime[day][HOUR] = 0;
		fromTime[day][MINUTE] = 0;
		toTime[day][HOUR] = 0;
		toTime[day][MINUTE] = 0;
	}

	private boolean checkTime(int day) {
		return fromTime[day][HOUR] == 0 && fromTime[day][MINUTE] == 0 && toTime[day][HOUR] == 0 && toTime[day][MINUTE] == 0;
	}

	private void checkChoosenTime() {
		if ((fromTime[MONDAY][HOUR] * 60 + fromTime[MONDAY][MINUTE]) > (toTime[MONDAY][HOUR] * 60 + toTime[MONDAY][MINUTE])) {
			gridMonday.setBackgroundColor(Color.RED);
			everythingOk[MONDAY] = false;
		} else {
			gridMonday.setBackgroundColor(Color.TRANSPARENT);
			everythingOk[MONDAY] = true;
		}
		if ((fromTime[TUESDAY][HOUR] * 60 + fromTime[TUESDAY][MINUTE]) > (toTime[TUESDAY][HOUR] * 60 + toTime[TUESDAY][MINUTE])) {
			gridTuesday.setBackgroundColor(Color.RED);
			everythingOk[TUESDAY] = false;
		} else {
			gridTuesday.setBackgroundColor(Color.TRANSPARENT);
			everythingOk[TUESDAY] = true;
		}
		if ((fromTime[WEDNESDAY][HOUR] * 60 + fromTime[WEDNESDAY][MINUTE]) > (toTime[WEDNESDAY][HOUR] * 60 + toTime[WEDNESDAY][MINUTE])) {
			gridWednesday.setBackgroundColor(Color.RED);
			everythingOk[WEDNESDAY] = false;
		} else {
			gridWednesday.setBackgroundColor(Color.TRANSPARENT);
			everythingOk[WEDNESDAY] = true;
		}
		if ((fromTime[THURSDAY][HOUR] * 60 + fromTime[THURSDAY][MINUTE]) > (toTime[THURSDAY][HOUR] * 60 + toTime[THURSDAY][MINUTE])) {
			gridThursday.setBackgroundColor(Color.RED);
			everythingOk[THURSDAY] = false;
		} else {
			gridThursday.setBackgroundColor(Color.TRANSPARENT);
			everythingOk[TUESDAY] = true;
		}
		if ((fromTime[FRIDAY][HOUR] * 60 + fromTime[FRIDAY][MINUTE]) > (toTime[FRIDAY][HOUR] * 60 + toTime[FRIDAY][MINUTE])) {
			gridFriday.setBackgroundColor(Color.RED);
			everythingOk[FRIDAY] = false;
		} else {
			gridFriday.setBackgroundColor(Color.TRANSPARENT);
			everythingOk[FRIDAY] = true;
		}
		if ((fromTime[SATURDAY][HOUR] * 60 + fromTime[SATURDAY][MINUTE]) > (toTime[SATURDAY][HOUR] * 60 + toTime[SATURDAY][MINUTE])) {
			gridSaturday.setBackgroundColor(Color.RED);
			everythingOk[SATURDAY] = false;
		} else {
			gridSaturday.setBackgroundColor(Color.TRANSPARENT);
			everythingOk[SATURDAY] = true;
		}
		if ((fromTime[SUNDAY][HOUR] * 60 + fromTime[SUNDAY][MINUTE]) > (toTime[SUNDAY][HOUR] * 60 + toTime[SUNDAY][MINUTE])) {
			gridSunday.setBackgroundColor(Color.RED);
			everythingOk[SUNDAY] = false;
		} else {
			gridSunday.setBackgroundColor(Color.TRANSPARENT);
			everythingOk[SUNDAY] = true;
		}

		for (int i = 0; i < 7; i++) {
			if (!everythingOk[i]) {
				okButton.setEnabled(false);
				return;
			}
		}
		okButton.setEnabled(true);
	}

	private void updateDisplays() {
		mondayFrom.setText(new StringBuilder().append(pad(fromTime[MONDAY][HOUR])).append(":").append(pad(fromTime[MONDAY][MINUTE])));
		tuesdayFrom.setText(new StringBuilder().append(pad(fromTime[TUESDAY][HOUR])).append(":").append(pad(fromTime[TUESDAY][MINUTE])));
		wednesdayFrom.setText(new StringBuilder().append(pad(fromTime[WEDNESDAY][HOUR])).append(":").append(pad(fromTime[WEDNESDAY][MINUTE])));
		thursdayFrom.setText(new StringBuilder().append(pad(fromTime[THURSDAY][HOUR])).append(":").append(pad(fromTime[THURSDAY][MINUTE])));
		fridayFrom.setText(new StringBuilder().append(pad(fromTime[FRIDAY][HOUR])).append(":").append(pad(fromTime[FRIDAY][MINUTE])));
		saturdayFrom.setText(new StringBuilder().append(pad(fromTime[SATURDAY][HOUR])).append(":").append(pad(fromTime[SATURDAY][MINUTE])));
		sundayFrom.setText(new StringBuilder().append(pad(fromTime[SUNDAY][HOUR])).append(":").append(pad(fromTime[SUNDAY][MINUTE])));

		mondayTo.setText(new StringBuilder().append(pad(toTime[MONDAY][HOUR])).append(":").append(pad(toTime[MONDAY][MINUTE])));
		tuesdayTo.setText(new StringBuilder().append(pad(toTime[TUESDAY][HOUR])).append(":").append(pad(toTime[TUESDAY][MINUTE])));
		wednesdayTo.setText(new StringBuilder().append(pad(toTime[WEDNESDAY][HOUR])).append(":").append(pad(toTime[WEDNESDAY][MINUTE])));
		thursdayTo.setText(new StringBuilder().append(pad(toTime[THURSDAY][HOUR])).append(":").append(pad(toTime[THURSDAY][MINUTE])));
		fridayTo.setText(new StringBuilder().append(pad(toTime[FRIDAY][HOUR])).append(":").append(pad(toTime[FRIDAY][MINUTE])));
		saturdayTo.setText(new StringBuilder().append(pad(toTime[SATURDAY][HOUR])).append(":").append(pad(toTime[SATURDAY][MINUTE])));
		sundayTo.setText(new StringBuilder().append(pad(toTime[SUNDAY][HOUR])).append(":").append(pad(toTime[SUNDAY][MINUTE])));
		checkChoosenTime();
	}

	private static String pad(int c) {
		if (c >= 10)
			return String.valueOf(c);
		else
			return "0" + String.valueOf(c);
	}

	private void getCheckBoxes() {
		mondayCheckBox = (CheckBox) findViewById(R.id.crt_mondayCheckBox);
		mondayCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				enOrDisableTextFields();
			}
		});
		tuesdayCheckBox = (CheckBox) findViewById(R.id.crt_tuesdayCheckBox);
		tuesdayCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				enOrDisableTextFields();
			}
		});
		wednesdayCheckBox = (CheckBox) findViewById(R.id.crt_wednesdayCheckBox);
		wednesdayCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				enOrDisableTextFields();
			}
		});
		thursdayCheckBox = (CheckBox) findViewById(R.id.crt_thursdayCheckBox);
		thursdayCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				enOrDisableTextFields();
			}
		});
		fridayCheckBox = (CheckBox) findViewById(R.id.crt_fridayCheckBox);
		fridayCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				enOrDisableTextFields();
			}
		});
		saturdayCheckBox = (CheckBox) findViewById(R.id.crt_saturdayCheckBox);
		saturdayCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				enOrDisableTextFields();
			}
		});
		sundayCheckBox = (CheckBox) findViewById(R.id.crt_sundayCheckBox);
		sundayCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				enOrDisableTextFields();
			}
		});
	}

	private void getTextFields() {
		mondayFrom = (TextView) findViewById(R.id.crt_timeFromMonday);
		mondayFrom.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showDialog(MONDAY_FROM);
			}
		});

		tuesdayFrom = (TextView) findViewById(R.id.crt_timeFromTuesday);
		tuesdayFrom.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showDialog(TUESDAY_FROM);
			}
		});
		wednesdayFrom = (TextView) findViewById(R.id.crt_timeFromWednesday);
		wednesdayFrom.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showDialog(WEDNESDAY_FROM);
			}
		});
		thursdayFrom = (TextView) findViewById(R.id.crt_timeFromThursday);
		thursdayFrom.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showDialog(THURSDAY_FROM);
			}
		});
		fridayFrom = (TextView) findViewById(R.id.crt_timeFromFriday);
		fridayFrom.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showDialog(FRIDAY_FROM);
			}
		});
		saturdayFrom = (TextView) findViewById(R.id.crt_timeFromSaturday);
		saturdayFrom.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showDialog(SATURDAY_FROM);
			}
		});
		sundayFrom = (TextView) findViewById(R.id.crt_timeFromSunday);
		sundayFrom.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showDialog(SUNDAY_FROM);
			}
		});

		mondayTo = (TextView) findViewById(R.id.crt_timeToMonday);
		mondayTo.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showDialog(MONDAY_TO);
			}
		});
		tuesdayTo = (TextView) findViewById(R.id.crt_timeToTuesday);
		tuesdayTo.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showDialog(TUESDAY_TO);
			}
		});
		wednesdayTo = (TextView) findViewById(R.id.crt_timeToWednesday);
		wednesdayTo.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showDialog(WEDNESDAY_TO);
			}
		});
		thursdayTo = (TextView) findViewById(R.id.crt_timeToThursday);
		thursdayTo.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showDialog(THURSDAY_TO);
			}
		});
		fridayTo = (TextView) findViewById(R.id.crt_timeToFriday);
		fridayTo.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showDialog(FRIDAY_TO);
			}
		});
		saturdayTo = (TextView) findViewById(R.id.crt_timeToSaturday);
		saturdayTo.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showDialog(SATURDAY_TO);
			}
		});
		sundayTo = (TextView) findViewById(R.id.crt_timeToSunday);
		sundayTo.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showDialog(SUNDAY_TO);
			}
		});
	}

	private void cancelButton() {
		cancelButton = (Button) findViewById(R.id.crt_cancelButton);
		cancelButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	private void okButton() {
		okButton = (Button) findViewById(R.id.crt_okButton);
		okButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (task == null)
					task = new LRTask();
				copyFromToTime();
				Intent intent = new Intent();
				intent.putExtra("TASK", task);
				if (getParent() == null) {
					setResult(Activity.RESULT_OK, intent);
				} else {
					getParent().setResult(Activity.RESULT_OK, intent);
				}
				finish();
			}
		});
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case MONDAY_FROM:
			return new TimePickerDialog(this, mondayFromTimeSetListener, fromTime[MONDAY][HOUR], fromTime[MONDAY][MINUTE], false);
		case TUESDAY_FROM:
			return new TimePickerDialog(this, tuesdayFromTimeSetListener, fromTime[TUESDAY][HOUR], fromTime[TUESDAY][MINUTE], false);
		case WEDNESDAY_FROM:
			return new TimePickerDialog(this, wednesdayFromTimeSetListener, fromTime[WEDNESDAY][HOUR], fromTime[WEDNESDAY][MINUTE], false);
		case THURSDAY_FROM:
			return new TimePickerDialog(this, thursdayFromTimeSetListener, fromTime[THURSDAY][HOUR], fromTime[THURSDAY][MINUTE], false);
		case FRIDAY_FROM:
			return new TimePickerDialog(this, fridayFromTimeSetListener, fromTime[FRIDAY][HOUR], fromTime[FRIDAY][MINUTE], false);
		case SATURDAY_FROM:
			return new TimePickerDialog(this, saturdayFromTimeSetListener, fromTime[SATURDAY][HOUR], fromTime[SATURDAY][MINUTE], false);
		case SUNDAY_FROM:
			return new TimePickerDialog(this, sundayFromTimeSetListener, fromTime[SUNDAY][HOUR], fromTime[SUNDAY][MINUTE], false);
		case MONDAY_TO:
			return new TimePickerDialog(this, mondayToTimeSetListener, toTime[MONDAY][HOUR], toTime[MONDAY][MINUTE], false);
		case TUESDAY_TO:
			return new TimePickerDialog(this, tuesdayToTimeSetListener, toTime[TUESDAY][HOUR], toTime[TUESDAY][MINUTE], false);
		case WEDNESDAY_TO:
			return new TimePickerDialog(this, wednesdayToTimeSetListener, toTime[WEDNESDAY][HOUR], toTime[WEDNESDAY][MINUTE], false);
		case THURSDAY_TO:
			return new TimePickerDialog(this, thursdayToTimeSetListener, toTime[THURSDAY][HOUR], toTime[THURSDAY][MINUTE], false);
		case FRIDAY_TO:
			return new TimePickerDialog(this, fridayToTimeSetListener, toTime[FRIDAY][HOUR], toTime[FRIDAY][MINUTE], false);
		case SATURDAY_TO:
			return new TimePickerDialog(this, saturdayToTimeSetListener, toTime[SATURDAY][HOUR], toTime[SATURDAY][MINUTE], false);
		case SUNDAY_TO:
			return new TimePickerDialog(this, sundayToTimeSetListener, toTime[SUNDAY][HOUR], toTime[SUNDAY][MINUTE], false);
		}
		return null;

	}

	private TimePickerDialog.OnTimeSetListener mondayFromTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			fromTime[MONDAY][HOUR] = hourOfDay;
			fromTime[MONDAY][MINUTE] = minute;
			updateDisplays();
		}
	};
	private TimePickerDialog.OnTimeSetListener tuesdayFromTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			fromTime[TUESDAY][HOUR] = hourOfDay;
			fromTime[TUESDAY][MINUTE] = minute;
			updateDisplays();
		}
	};
	private TimePickerDialog.OnTimeSetListener wednesdayFromTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			fromTime[WEDNESDAY][HOUR] = hourOfDay;
			fromTime[WEDNESDAY][MINUTE] = minute;
			updateDisplays();
		}
	};
	private TimePickerDialog.OnTimeSetListener thursdayFromTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			fromTime[THURSDAY][HOUR] = hourOfDay;
			fromTime[THURSDAY][MINUTE] = minute;
			updateDisplays();
		}
	};
	private TimePickerDialog.OnTimeSetListener fridayFromTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			fromTime[FRIDAY][HOUR] = hourOfDay;
			fromTime[FRIDAY][MINUTE] = minute;
			updateDisplays();
		}
	};
	private TimePickerDialog.OnTimeSetListener saturdayFromTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			fromTime[SATURDAY][HOUR] = hourOfDay;
			fromTime[SATURDAY][MINUTE] = minute;
			updateDisplays();
		}
	};
	private TimePickerDialog.OnTimeSetListener sundayFromTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			fromTime[SUNDAY][HOUR] = hourOfDay;
			fromTime[SUNDAY][MINUTE] = minute;
			updateDisplays();
		}
	};
	private TimePickerDialog.OnTimeSetListener mondayToTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			toTime[MONDAY][HOUR] = hourOfDay;
			toTime[MONDAY][MINUTE] = minute;
			updateDisplays();
		}
	};
	private TimePickerDialog.OnTimeSetListener tuesdayToTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			toTime[TUESDAY][HOUR] = hourOfDay;
			toTime[TUESDAY][MINUTE] = minute;
			updateDisplays();
		}
	};
	private TimePickerDialog.OnTimeSetListener wednesdayToTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			toTime[WEDNESDAY][HOUR] = hourOfDay;
			toTime[WEDNESDAY][MINUTE] = minute;
			updateDisplays();
		}
	};
	private TimePickerDialog.OnTimeSetListener thursdayToTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			toTime[THURSDAY][HOUR] = hourOfDay;
			toTime[THURSDAY][MINUTE] = minute;
			updateDisplays();
		}
	};
	private TimePickerDialog.OnTimeSetListener fridayToTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			toTime[FRIDAY][HOUR] = hourOfDay;
			toTime[FRIDAY][MINUTE] = minute;
			updateDisplays();
		}
	};
	private TimePickerDialog.OnTimeSetListener saturdayToTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			toTime[SATURDAY][HOUR] = hourOfDay;
			toTime[SATURDAY][MINUTE] = minute;
			updateDisplays();
		}
	};
	private TimePickerDialog.OnTimeSetListener sundayToTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			toTime[SUNDAY][HOUR] = hourOfDay;
			toTime[SUNDAY][MINUTE] = minute;
			updateDisplays();
		}
	};

}
