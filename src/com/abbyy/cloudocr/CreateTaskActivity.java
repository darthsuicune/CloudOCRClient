package com.abbyy.cloudocr;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.ActionBar.OnNavigationListener;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
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
import com.abbyy.cloudocr.optionsfragments.ProcessBarcodeFieldOptionsFragment;
import com.abbyy.cloudocr.optionsfragments.ProcessBusinessCardOptionsFragment;
import com.abbyy.cloudocr.optionsfragments.ProcessCheckmarkFieldOptionsFragment;
import com.abbyy.cloudocr.optionsfragments.ProcessFieldsOptionsFragment;
import com.abbyy.cloudocr.optionsfragments.ProcessImageOptionsFragment;
import com.abbyy.cloudocr.optionsfragments.ProcessMultipleOptionsFragment;
import com.abbyy.cloudocr.optionsfragments.ProcessOptionsFragment;
import com.abbyy.cloudocr.optionsfragments.ProcessTextFieldOptionsFragment;

public class CreateTaskActivity extends ActionBarActivity {
	public static final String EXTRA_PROCESS_MODE = "Process mode";

	private int mProcessingMode = -1;

	private Uri mImageToProcess = null;

	private ProcessOptionsFragment mProcessOptionsFragment;
	private Spinner mProcessSpinnerView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		handleIncomingIntent();
		setViews();
		setOptionsFragment();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()){
		case android.R.id.home:
			finish();
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	private void handleIncomingIntent() {
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			String action = getIntent().getAction();
			if (action != null && action.equals(Intent.ACTION_SEND)) {
				String type = getIntent().getType();
				mProcessingMode = 1;
				if (type != null && type.startsWith("image")) {
					mImageToProcess = (Uri) getIntent().getParcelableExtra(
							Intent.EXTRA_STREAM);
					if (mImageToProcess == null) {
						Toast.makeText(this, R.string.error_loading_image,
								Toast.LENGTH_LONG).show();
						this.finish();
					}
				}
			} else {
				mProcessingMode = extras.getInt(EXTRA_PROCESS_MODE);
			}
		}
	}

	private void setViews() {
		setContentView(R.layout.create_task);
		mProcessSpinnerView = (Spinner) findViewById(R.id.create_task_process_spinner);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			mProcessSpinnerView.setVisibility(View.GONE);
			setActionBar();
		} else {
			mProcessSpinnerView.setVisibility(View.VISIBLE);
			setSpinner();
		}
	}

	private void setOptionsFragment() {
		if (mProcessingMode != -1) {
			switch (mProcessingMode) {
			case SettingsActivity.PROCESSING_MODE_BARCODE_FIELD:
				mProcessOptionsFragment = new ProcessBarcodeFieldOptionsFragment();
				break;
			case SettingsActivity.PROCESSING_MODE_BUSINESS_CARD:
				mProcessOptionsFragment = new ProcessBusinessCardOptionsFragment();
				break;
			case SettingsActivity.PROCESSING_MODE_CHECKMARK_FIELD:
				mProcessOptionsFragment = new ProcessCheckmarkFieldOptionsFragment();
				break;
			case SettingsActivity.PROCESSING_MODE_FIELDS:
				mProcessOptionsFragment = new ProcessFieldsOptionsFragment();
				break;
			case SettingsActivity.PROCESSING_MODE_IMAGE:
				mProcessOptionsFragment = new ProcessImageOptionsFragment();
				break;
			case SettingsActivity.PROCESSING_MODE_MULTIPLE:
				mProcessOptionsFragment = new ProcessMultipleOptionsFragment();
				break;
			case SettingsActivity.PROCESSING_MODE_TEXT_FIELD:
				mProcessOptionsFragment = new ProcessTextFieldOptionsFragment();
				break;
			}
		}
		FragmentTransaction transaction = getSupportFragmentManager()
				.beginTransaction();
		transaction.replace(R.id.create_task_options, mProcessOptionsFragment);
		transaction.commit();
		if(mImageToProcess != null){
			mProcessOptionsFragment.addFile(mImageToProcess.toString());
		}
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setActionBar() {
		ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setListNavigationCallbacks(getSpinnerAdapter(),
				new OnNavigationListener() {
					@Override
					public boolean onNavigationItemSelected(int itemPosition,
							long itemId) {
						mProcessingMode = itemPosition;
						setOptionsFragment();
						return true;
					}
				});
	}

	private void setSpinner() {
		mProcessSpinnerView.setAdapter(getSpinnerAdapter());
		mProcessSpinnerView
				.setOnItemSelectedListener(new OnItemSelectedListener() {
					@Override
					public void onItemSelected(AdapterView<?> adapterView,
							View view, int itemPosition, long itemId) {
						mProcessingMode = itemPosition;
						setOptionsFragment();

					}

					@Override
					public void onNothingSelected(AdapterView<?> adapterView) {
						return;
					}
				});
	}

	private SpinnerAdapter getSpinnerAdapter() {
		String[] strings = getResources().getStringArray(R.array.process_type);

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, strings);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		return adapter;
	}
}
