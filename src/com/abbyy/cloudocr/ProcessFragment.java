package com.abbyy.cloudocr;

import java.io.InputStream;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.ActionBar.OnNavigationListener;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import com.abbyy.cloudocr.ocr.Task;
import com.abbyy.cloudocr.optionsfragments.ProcessBarcodeFieldOptionsFragment;
import com.abbyy.cloudocr.optionsfragments.ProcessBusinessCardOptionsFragment;
import com.abbyy.cloudocr.optionsfragments.ProcessCheckmarkFieldOptionsFragment;
import com.abbyy.cloudocr.optionsfragments.ProcessFieldsOptionsFragment;
import com.abbyy.cloudocr.optionsfragments.ProcessImageOptionsFragment;
import com.abbyy.cloudocr.optionsfragments.ProcessMultipleOptionsFragment;
import com.abbyy.cloudocr.optionsfragments.ProcessOptionsFragment;
import com.abbyy.cloudocr.optionsfragments.ProcessTextFieldOptionsFragment;

public class ProcessFragment extends Fragment {

	private Spinner mProcessTypeSpinnerView;
	private Button mProcessButtonView;

	private int mProcessMode = 0;

	public static final int LOADER_CONNECT = 1;

	private SharedPreferences prefs;

	private ProcessOptionsFragment mProcessOptionsFragment;

	private Task mTask;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.process_fragment, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		Bundle extras = getActivity().getIntent().getExtras();
		if (extras != null) {
			mProcessMode = extras.getInt(ProcessActivity.EXTRA_PROCESS_MODE);
		}
		prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
		prepareViews();
		loadOptions();
		setDefaultOptions();
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.process, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_process:
			sendRequest();
			break;
		}
		return true;
	}

	private void prepareViews() {
		mProcessTypeSpinnerView = (Spinner) getActivity().findViewById(
				R.id.spinner_process_type);
		mProcessButtonView = (Button) getActivity().findViewById(
				R.id.process_button);

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			mProcessTypeSpinnerView.setVisibility(View.GONE);
			mProcessButtonView.setVisibility(View.GONE);
			setActionBar();
		} else {
			mProcessTypeSpinnerView.setVisibility(View.VISIBLE);
			mProcessTypeSpinnerView
					.setOnItemSelectedListener(new OnItemSelectedListener() {
						@Override
						public void onItemSelected(AdapterView<?> parent,
								View view, int pos, long id) {
							changeProcessMode(pos);
						}

						@Override
						public void onNothingSelected(AdapterView<?> parent) {
							// Nothing to do here
						}
					});
			mProcessTypeSpinnerView.setSelection(mProcessMode);
			mProcessButtonView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					sendRequest();
				}
			});
		}
	}

	/**
	 * On particular types, we should warn the user that changing the mode will
	 * destroy the current task information
	 */
	private void changeProcessMode(int newMode) {
		mProcessMode = newMode;
		loadOptions();
	}

	/**
	 * This method loads the fragment with the options corresponding to the
	 * selected processing mode
	 */
	private void loadOptions() {
		FragmentTransaction transaction = getActivity()
				.getSupportFragmentManager().beginTransaction();

		switch (mProcessMode) {
		case ProcessActivity.EXTRA_PROCESS_IMAGE:
			mProcessOptionsFragment = new ProcessImageOptionsFragment();
			break;
		case ProcessActivity.EXTRA_PROCESS_MULTIPLE:
			mProcessOptionsFragment = new ProcessMultipleOptionsFragment();
			break;
		case ProcessActivity.EXTRA_PROCESS_BUSINESS_CARD:
			mProcessOptionsFragment = new ProcessBusinessCardOptionsFragment();
			break;
		case ProcessActivity.EXTRA_PROCESS_TEXT_FIELD:
			mProcessOptionsFragment = new ProcessTextFieldOptionsFragment();
			break;
		case ProcessActivity.EXTRA_PROCESS_BARCODE_FIELD:
			mProcessOptionsFragment = new ProcessBarcodeFieldOptionsFragment();
			break;
		case ProcessActivity.EXTRA_PROCESS_CHECKMARK_FIELD:
			mProcessOptionsFragment = new ProcessCheckmarkFieldOptionsFragment();
			break;
		case ProcessActivity.EXTRA_PROCESS_FIELDS:
			mProcessOptionsFragment = new ProcessFieldsOptionsFragment();
			break;
		}
		transaction.replace(R.id.process_options, mProcessOptionsFragment);
		transaction.commit();
	}

	/**
	 * 
	 */
	private void setDefaultOptions() {
		Bundle options = new Bundle();
		if (!mProcessOptionsFragment.setDefaultOptions(options)) {
			Log.d("OPTIONS ERROR",
					"Error inserting options into fragment. Wrong options passed");
		}
	}

	private void showProgress(final boolean show) {

	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setActionBar() {
		ActionBar actionBar = getActivity().getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
		actionBar.setDisplayShowTitleEnabled(false);
		setHasOptionsMenu(true);
		actionBar.setListNavigationCallbacks(getProcessSpinner(),
				new OnNavigationListener() {
					@Override
					public boolean onNavigationItemSelected(int itemPosition,
							long itemId) {
						changeProcessMode(itemPosition);
						return true;
					}
				});
		actionBar.setSelectedNavigationItem(mProcessMode);
	}

	private SpinnerAdapter getProcessSpinner() {
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
				getActivity(), R.array.process_type,
				android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		return adapter;
	}

	private void sendRequest() {
		getActivity().getSupportLoaderManager().restartLoader(LOADER_CONNECT,
				mProcessOptionsFragment.createArgs(),
				new ConnectionLoaderHelper());
	}

	private void parseResponse(InputStream response) {
		createTaskList(response);

		if (mTask != null) {
			if (mTask.isCurrentTask(response)) {
				mTask.updateTask(response);
			} else {
				mTask = new Task(getActivity(), response);
			}
		} else {
			mTask = new Task(getActivity(), response);
		}

		processResponse();
	}

	// TODO
	private void processResponse() {
		Toast.makeText(getActivity(), mTask.mStatus, Toast.LENGTH_LONG).show();
		if (mTask.mStatus
				.equalsIgnoreCase(getString(R.string.status_completed))) {

			showProgress(false);
			mTask.getResultURL();

		} else if (mTask.mStatus
				.equalsIgnoreCase(getString(R.string.status_deleted))) {

		} else if (mTask.mStatus
				.equalsIgnoreCase(getString(R.string.status_in_progress))) {

			showProgress(true);

		} else if (mTask.mStatus
				.equalsIgnoreCase(getString(R.string.status_not_enough_credits))) {

		} else if (mTask.mStatus
				.equalsIgnoreCase(getString(R.string.status_processing_failed))) {

		} else if (mTask.mStatus
				.equalsIgnoreCase(getString(R.string.status_queued))) {

			showProgress(true);

		} else if (mTask.mStatus
				.equalsIgnoreCase(getString(R.string.status_submitted))) {

		} else {

		}
	}

	private class ConnectionLoaderHelper implements
			LoaderCallbacks<InputStream> {

		@Override
		public Loader<InputStream> onCreateLoader(int id, Bundle args) {
			Loader<InputStream> loader = null;
			switch (id) {
			case LOADER_CONNECT:
				loader = new AsyncInputStreamLoader(getActivity(), args);
				break;
			}
			return loader;
		}

		@Override
		public void onLoadFinished(Loader<InputStream> loader,
				InputStream result) {
			switch (loader.getId()) {
			case LOADER_CONNECT:
				if (result != null) {
					parseResponse(result);
				} else {
					failedConnection(result);
				}
				break;
			}
		}

		@Override
		public void onLoaderReset(Loader<InputStream> loader) {
			loader.reset();
		}
	}

	private void failedConnection(InputStream response) {

	}
}
