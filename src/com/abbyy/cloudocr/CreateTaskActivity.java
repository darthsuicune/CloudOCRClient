package com.abbyy.cloudocr;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.ActionBar.OnNavigationListener;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import com.abbyy.cloudocr.compat.ActionBarActivity;
import com.abbyy.cloudocr.database.TasksContract;
import com.abbyy.cloudocr.optionsfragments.ProcessBusinessCardOptionsFragment;
import com.abbyy.cloudocr.optionsfragments.ProcessImageOptionsFragment;
import com.abbyy.cloudocr.optionsfragments.ProcessOptionsFragment;

/**
 * This activity is called when the user is in LANDSCAPE MODE if the previous
 * activity was the "StartActivity". It is always called from StartActivity in
 * PORTRAIT MODE
 * 
 * It is in charge of 3 different actions: Entry point from image-sharing.
 * Showing the right fragment to the user when processing a task. Managing the
 * fragment lifecycle.
 * 
 * This activity must receive a parameter in the EXTRA_PROCESS_MODE extras. The
 * process modes are available in the SettingsActivity class as global
 * constants.
 * 
 * This activity also serves as the entry point for the activity when handling
 * an ACTION_SEND
 * 
 * @author Denis Lapuente
 * 
 */
public class CreateTaskActivity extends ActionBarActivity {
	public static final String EXTRA_PROCESS_MODE = "Process mode";

	private int mProcessingMode = -1;
	private Uri mImageToProcess = null;

	private SharedPreferences prefs;

	// Generic fragment we are going to show. It is an abstract class not
	// possible to instantiate, but serves as the stub for all possible options,
	// that will extend this fragment.
	private ProcessOptionsFragment mProcessOptionsFragment;

	/**
	 * Entry point to the activity. We preparate the view, and do nothing else
	 * in case the activity is being restarted. If it's a new instance, it
	 * handles the intent and prepares the fragment (if this is being restarted,
	 * everything should be already set).
	 * 
	 * On the first run we launch a task in case the first run is done through
	 * this entry point.
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		prefs = PreferenceManager.getDefaultSharedPreferences(this);
		if (prefs.getBoolean(SettingsActivity.IS_FIRST_RUN, true)) {
			insertLanguages();
			prefs.edit().putBoolean(SettingsActivity.IS_FIRST_RUN, false)
					.commit();
		}
		setContentView(R.layout.create_task);
		handleIncomingIntent();
		if (savedInstanceState != null) {
			if (savedInstanceState.containsKey(EXTRA_PROCESS_MODE)) {
				mProcessingMode = savedInstanceState.getInt(EXTRA_PROCESS_MODE);
			}
			return;
		}
		setOptionsFragment();
	}

	/**
	 * We save the Uri to the image we are going to process upon saving the
	 * state of the activity
	 */
	@Override
	public void onSaveInstanceState(Bundle outState) {
		if (mProcessingMode != -1) {
			outState.putInt(EXTRA_PROCESS_MODE, mProcessingMode);
		}
		super.onSaveInstanceState(outState);
	}

	/**
	 * The options menu will be handled by the fragments, not the activity.
	 * Returning false means we haven't handled the menu.
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return false;
	}

	/**
	 * The home button (logo) will be used on normal navigation through the
	 * activity. It will return up from the navigation list to the start
	 * activity class.
	 * 
	 * If it is handled, it returns true. If it is not, it passes the signal to
	 * further listeners
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			NavUtils.navigateUpTo(this, new Intent(this, StartActivity.class));
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * This method handles the intent the activity receives, if any.
	 * 
	 * It processes the extra parameters, and checks if it is coming from an
	 * activity inside this application or if it is coming from an external
	 * intent, and acts based on it. It prepares the image to process if it has
	 * received one
	 */
	private void handleIncomingIntent() {
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			// Check if it is an external intent
			String action = getIntent().getAction();
			if (action != null && action.equals(Intent.ACTION_SEND)) {
				String type = getIntent().getType();
				// Check for a valid image
				if (type != null && type.startsWith("image")) {
					mProcessingMode = SettingsActivity.PROCESSING_MODE_IMAGE;
					mImageToProcess = (Uri) getIntent().getParcelableExtra(
							Intent.EXTRA_STREAM);
					// In case its an invalid file type, show an error and exit
					if (mImageToProcess == null) {
						Toast.makeText(this, R.string.error_loading_image,
								Toast.LENGTH_LONG).show();
						this.finish();
					}
					// If it is a valid image, prepare the action bar for
					// processing and end
					setSpinnerMode(true);
					return;
				}
				// If it is not an external intent, the processing mode will be
				// in the extras
			} else {
				mProcessingMode = extras.getInt(EXTRA_PROCESS_MODE);
			}
		}
		// In case we don't receive a valid image through the intent, prepare
		// the action bar for normal processing
		setSpinnerMode(false);
	}

	/**
	 * This method chooses the right processing fragment to put in place, based
	 * on the parameters received previously.
	 * 
	 * If the processing mode is not set or doesn't correspond to our prepared
	 * methods, it returns
	 * 
	 * TODO: Currently a lot of options are commented, due to be brought to
	 * availability later.
	 */
	private void setOptionsFragment() {
		if (mProcessingMode == -1) {
			return;
		}
		switch (mProcessingMode) {
		// case SettingsActivity.PROCESSING_MODE_BARCODE_FIELD:
		// mProcessOptionsFragment = new
		// ProcessBarcodeFieldOptionsFragment();
		// break;
		case SettingsActivity.PROCESSING_MODE_BUSINESS_CARD:
			mProcessOptionsFragment = new ProcessBusinessCardOptionsFragment();
			break;
		// case SettingsActivity.PROCESSING_MODE_CHECKMARK_FIELD:
		// mProcessOptionsFragment = new
		// ProcessCheckmarkFieldOptionsFragment();
		// break;
		// case SettingsActivity.PROCESSING_MODE_FIELDS:
		// mProcessOptionsFragment = new ProcessFieldsOptionsFragment();
		// break;
		case SettingsActivity.PROCESSING_MODE_IMAGE:
			mProcessOptionsFragment = new ProcessImageOptionsFragment();
			break;
		// case SettingsActivity.PROCESSING_MODE_MULTIPLE:
		// mProcessOptionsFragment = new ProcessMultipleOptionsFragment();
		// break;
		// case SettingsActivity.PROCESSING_MODE_TEXT_FIELD:
		// mProcessOptionsFragment = new ProcessTextFieldOptionsFragment();
		// break;

		default:
			return;
		}

		// Insert the fragment in place with its parameters.
		Bundle args = new Bundle();
		if (mImageToProcess != null) {
			args.putString(ProcessOptionsFragment.ARG_FILE_PATH,
					mImageToProcess.toString());
		}
		mProcessOptionsFragment.setArguments(args);

		FragmentTransaction transaction = getSupportFragmentManager()
				.beginTransaction();

		transaction.replace(R.id.create_task_options, mProcessOptionsFragment);
		transaction.commit();
	}

	/**
	 * This method prepares the action bar for correct use. It sets a spinner
	 * that allows for changing processing mode when needed and prepares the
	 * action bar where available.
	 * 
	 * @param withSpinner
	 *            Parameter to be set if we want to show the spinner or not
	 */
	@SuppressLint("NewApi")
	private void setSpinnerMode(boolean withSpinner) {
		// In previous to Honeycomb, we prepare the spinner view as a separate
		// spinner.
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
			if (withSpinner) {
				// Spinner for changing type of processing in case the user
				// doesn't select it previously (such as an external call)
				Spinner processSpinnerView;
				processSpinnerView = (Spinner) findViewById(R.id.create_task_process_spinner);

				processSpinnerView.setAdapter(getSpinnerAdapter());
				processSpinnerView
						.setOnItemSelectedListener(new OnItemSelectedListener() {
							@Override
							public void onItemSelected(
									AdapterView<?> adapterView, View view,
									int itemPosition, long itemId) {
								mProcessingMode = itemPosition;
								setOptionsFragment();

							}

							@Override
							public void onNothingSelected(
									AdapterView<?> adapterView) {
								return;
							}
						});
			} else {
				findViewById(R.id.create_task_process_spinner).setVisibility(
						View.GONE);
			}

			// In post HoneyComb, we do it on the action bar.
		} else {
			ActionBar actionBar = getActionBar();

			if (withSpinner) {
				actionBar.setDisplayHomeAsUpEnabled(false);
				actionBar.setDisplayShowTitleEnabled(false);
				actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);

				actionBar.setListNavigationCallbacks(getSpinnerAdapter(),
						new OnNavigationListener() {
							@Override
							public boolean onNavigationItemSelected(
									int itemPosition, long itemId) {
								mProcessingMode = itemPosition;
								setOptionsFragment();
								return true;
							}
						});
			} else {
				actionBar.setDisplayHomeAsUpEnabled(true);
				actionBar.setDisplayShowTitleEnabled(true);
			}
		}
	}

	/**
	 * Convenience method for preparing the spinner adapter and its contents.
	 * 
	 * @return The spinner adapter (Currently an ArrayAdapter)
	 */
	private SpinnerAdapter getSpinnerAdapter() {

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, getResources()
						.getStringArray(R.array.process_type));
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		return adapter;
	}

	/**
	 * This method launchs an async task that inserts all the required languages
	 * into the database. It makes it out of the main thread so doesn't hinder
	 * performance
	 */
	private void insertLanguages() {
		Toast.makeText(this, R.string.first_run_message, Toast.LENGTH_SHORT)
				.show();
		new FirstRunTask().execute(null, null);
	}

	/**
	 * The async task only loads the languages and finishes gracefully.
	 * 
	 * @author lapuente
	 * 
	 */
	public class FirstRunTask extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			ContentResolver cr = getContentResolver();
			String[] languagesList = getResources().getStringArray(
					R.array.languages);
			for (int i = 0; i < languagesList.length; i++) {
				ContentValues values = new ContentValues();
				values.put(TasksContract.LanguagesTable.LANGUAGE,
						languagesList[i]);
				cr.insert(TasksContract.CONTENT_LANGUAGES, values);
			}
			return null;
		}

	}
}
