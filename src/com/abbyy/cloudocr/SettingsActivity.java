package com.abbyy.cloudocr;

import android.annotation.TargetApi;
import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

import com.abbyy.cloudocr.fragments.PreferencesFragment;

/**
 * Temporal stub for the settings activity. It also contains the constants used
 * wide on the application
 * 
 * @author Denis Lapuente
 * 
 */
public class SettingsActivity extends PreferenceActivity {
	public static final int PROCESSING_MODE_IMAGE = 0;
	public static final int PROCESSING_MODE_BUSINESS_CARD = 1;
	public static final int PROCESSING_MODE_MULTIPLE = 2;
	public static final int PROCESSING_MODE_TEXT_FIELD = 3;
	public static final int PROCESSING_MODE_BARCODE_FIELD = 4;
	public static final int PROCESSING_MODE_CHECKMARK_FIELD = 5;
	public static final int PROCESSING_MODE_FIELDS = 6;

	public static final String DEFAULT_TAB = "defaultTab";
	public static final String PREFERENCE_SAVE_TAB_DEFAULT = "saveTabDefault";
	public static final String PREFERENCES_PROCESS_IMAGE = "processImageOptions";
	public static final String PREFERENCES_PROCESS_BUSINESS_CARD = "processBusinessCardOptions";

	public static final String PREFERENCE_SHOW_ONLY_FROM_DEVICE = "preference_show_only_from_device";

	public static final String IS_FIRST_RUN = "isFirstRun";
	public static final String SHOW_NOTIFICATION = "showNotification";

	/**
	 * 
	 * Gets the default options for a particular processing mode.
	 * 
	 * @param context
	 * @param processingMode
	 * @return Bundle with the default options for the given processing mode
	 */
	public static Bundle getDefaultOptions(Context context, int processingMode) {
		Bundle options = new Bundle();
		switch (processingMode) {
		case PROCESSING_MODE_IMAGE:
			break;
		case PROCESSING_MODE_MULTIPLE:
			break;
		case PROCESSING_MODE_BUSINESS_CARD:
			break;
		case PROCESSING_MODE_TEXT_FIELD:
			break;
		case PROCESSING_MODE_BARCODE_FIELD:
			break;
		case PROCESSING_MODE_CHECKMARK_FIELD:
			break;
		case PROCESSING_MODE_FIELDS:
			break;
		}
		return options;
	}

	/**
	 * 
	 * Saves the default options for a particular processing mode.
	 * 
	 * @param context
	 * @param processingMode
	 * @param options
	 */
	public static void saveDefaultOptions(Context context, int processingMode,
			Bundle options) {
		SharedPreferences.Editor editor = PreferenceManager
				.getDefaultSharedPreferences(context).edit();
		switch (processingMode) {
		case PROCESSING_MODE_IMAGE:
			break;
		case PROCESSING_MODE_MULTIPLE:
			break;
		case PROCESSING_MODE_BUSINESS_CARD:
			break;
		case PROCESSING_MODE_TEXT_FIELD:
			break;
		case PROCESSING_MODE_BARCODE_FIELD:
			break;
		case PROCESSING_MODE_CHECKMARK_FIELD:
			break;
		case PROCESSING_MODE_FIELDS:
			break;
		}
		editor.commit();
	}

	// Currently this activity doesn't have any settings to display, so it just
	// finishes
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settings_activity);
		if (Build.VERSION.SDK_INT > Build.VERSION_CODES.HONEYCOMB) {
			loadPreferencesHoneycomb();
		} else {
			loadPreferencesFroyo();
		}
		// this.finish();
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void loadPreferencesHoneycomb() {
		Fragment fragment = Fragment.instantiate(getApplicationContext(),
				PreferencesFragment.class.getName());

		getFragmentManager().beginTransaction()
				.replace(R.id.settings_fragment_container, fragment).commit();
	}

	private void loadPreferencesFroyo() {
		
	}
}
