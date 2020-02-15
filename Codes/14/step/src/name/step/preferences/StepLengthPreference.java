package name.step.preferences;

import name.step.R;
import android.content.Context;
import android.preference.EditTextPreference;
import android.util.AttributeSet;

/**
 * An {@link EditTextPreference} that only allows float values.
 */
public class StepLengthPreference extends EditMeasurementPreference {

	public StepLengthPreference(Context context) {
		super(context);
	}
	public StepLengthPreference(Context context, AttributeSet attr) {
		super(context, attr);
	}
	public StepLengthPreference(Context context, AttributeSet attr, int defStyle) {
		super(context, attr, defStyle);
	}

	protected void initPreferenceDetails() {
		mTitleResource = R.string.step_length_setting_title;
		mMetricUnitsResource = R.string.centimeters;
		mImperialUnitsResource = R.string.inches;
	}
}

