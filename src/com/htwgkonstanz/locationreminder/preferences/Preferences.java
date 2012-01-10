package com.htwgkonstanz.locationreminder.preferences;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.SeekBar;
import android.widget.TextView;

import com.htwgkonstanz.locationreminder.R;

public class Preferences extends Activity implements SeekBar.OnSeekBarChangeListener {

	private SeekBar rangeSeekBar;
	private TextView rangeText;
	private int range;
	private Button okButton;
	private SharedPreferences settings;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.preferencesview);

		settings = getSharedPreferences("prefs", 0);
		range = settings.getInt("RANGE", 5);
		boolean alarm = settings.getBoolean("ALARM", false);
		boolean vibrator = settings.getBoolean("VIBRATOR", false);
		boolean sound = settings.getBoolean("SOUND", false);

		rangeSeekBar = (SeekBar) findViewById(R.id.pv_specifyRangeSeekBar);
		rangeSeekBar.setOnSeekBarChangeListener(this);

		CheckBox alarmCheckBox = (CheckBox) findViewById(R.id.pv_alarm);
		alarmCheckBox.setChecked(alarm);

		final CheckBox vibratorCheckBox = (CheckBox) findViewById(R.id.pv_vibrator);
		vibratorCheckBox.setChecked(vibrator);

		final CheckBox soundCheckBox = (CheckBox) findViewById(R.id.pv_sound);
		soundCheckBox.setChecked(sound);

		vibratorCheckBox.setEnabled(alarm);
		soundCheckBox.setEnabled(alarm);

		vibratorCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				SharedPreferences.Editor editor = settings.edit();
				editor.putBoolean("VIBRATOR", isChecked);
				editor.commit();

			}
		});

		soundCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				SharedPreferences.Editor editor = settings.edit();
				editor.putBoolean("SOUND", isChecked);
				editor.commit();

			}
		});

		alarmCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				vibratorCheckBox.setEnabled(isChecked);
				soundCheckBox.setEnabled(isChecked);
				SharedPreferences.Editor editor = settings.edit();
				editor.putBoolean("ALARM", isChecked);
				editor.commit();

			}
		});

		rangeText = (TextView) findViewById(R.id.pv_range_count);
		rangeSeekBar.setProgress(range / 5);
		updateDisplayedInformation();

		okButton = (Button) findViewById(R.id.pv_okButton);
		okButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				SharedPreferences.Editor editor = settings.edit();
				editor.putInt("RANGE", range);
				editor.commit();
				finish();
			}
		});

		Button cancelButton = (Button) findViewById(R.id.pv_cancelButton);
		cancelButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});

		Button periodButton = (Button) findViewById(R.id.pv_periodButton);
		periodButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Resources res = getResources();
				String minute = res.getString(R.string.minute);
				String hour = res.getString(R.string.hour);

				String minutes = res.getString(R.string.minutes);
				String hours = res.getString(R.string.hours);

				final CharSequence[] items = { "1 " + minute, "10 " + minutes, "30 " + minutes, "1 " + hour, "2 " + hours, "5 " + hours, "10 " + hours, "24 " + hours, "DEBUG" };
				final int[] time = { 60000, 600000, 1800000, 3600000, 7200000, 18000000, 36000000, 86400000, 10000 };

				AlertDialog.Builder builder = new AlertDialog.Builder(Preferences.this);
				builder.setTitle(R.string.pv_UpdateTime);
				builder.setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int item) {
						int updateTime = time[item];
						dialog.dismiss();
						SharedPreferences.Editor editor = settings.edit();
						editor.putInt("TIME", updateTime);
						editor.commit();
						if (getParent() == null) {
							setResult(Activity.RESULT_OK, new Intent());
						} else {
							getParent().setResult(Activity.RESULT_OK, new Intent());
						}
					}
				});
				AlertDialog alert = builder.create();
				alert.show();
			}
		});
	}

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
		range = progress * 5;
		updateDisplayedInformation();

	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
		updateDisplayedInformation();
	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		updateDisplayedInformation();
	}

	private void updateDisplayedInformation() {
		rangeText.setText(range + " m");
	}
}
