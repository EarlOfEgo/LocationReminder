import com.htwgkonstanz.locationreminder.R;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

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
		range = settings.getInt("RANGE", 10);

		rangeSeekBar = (SeekBar) findViewById(R.id.pv_specifyRangeSeekBar);
		rangeSeekBar.setOnSeekBarChangeListener(this);
		rangeText = (TextView) findViewById(R.id.pv_range_count);
		
		updateDisplayedInformations();
		
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
	}

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
		range = (progress + 5) * 2;
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
}
