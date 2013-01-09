package com.abbyy.cloudocr;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

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

	public static final int EXTRA_PROCESS_IMAGE = 0;
	public static final int EXTRA_PROCESS_MULTIPLE = 1;
	public static final int EXTRA_PROCESS_BUSINESS_CARD = 2;
	public static final int EXTRA_PROCESS_TEXT_FIELD = 3;
	public static final int EXTRA_PROCESS_BARCODE_FIELD = 4;
	public static final int EXTRA_PROCESS_CHECKMARK_FIELD = 5;
	public static final int EXTRA_PROCESS_FIELDS = 6;

	private int mProcessingMode = -1;

	private ProcessOptionsFragment mProcessOptionsFragment;
	private Spinner mProcessSpinnerView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setViews();
		handleIncomingIntent();
		addOptionsFragment();
		setFragmentOptions();
		super.onCreate(savedInstanceState);
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

	private void handleIncomingIntent() {
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			String action = getIntent().getAction();
			String type = getIntent().getType();
			if (action.equals(Intent.ACTION_SEND)) {
				mProcessingMode = 1;
				if (type.equalsIgnoreCase("image/*")) {

				}
			} else {
				mProcessingMode = extras.getInt(EXTRA_PROCESS_MODE);
			}
		}
	}

	private void addOptionsFragment() {
		if (mProcessingMode != -1) {
			switch (mProcessingMode) {
			case EXTRA_PROCESS_BARCODE_FIELD:
				mProcessOptionsFragment = new ProcessBarcodeFieldOptionsFragment();
				break;
			case EXTRA_PROCESS_BUSINESS_CARD:
				mProcessOptionsFragment = new ProcessBusinessCardOptionsFragment();
				break;
			case EXTRA_PROCESS_CHECKMARK_FIELD:
				mProcessOptionsFragment = new ProcessCheckmarkFieldOptionsFragment();
				break;
			case EXTRA_PROCESS_FIELDS:
				mProcessOptionsFragment = new ProcessFieldsOptionsFragment();
				break;
			case EXTRA_PROCESS_IMAGE:
				mProcessOptionsFragment = new ProcessImageOptionsFragment();
				break;
			case EXTRA_PROCESS_MULTIPLE:
				mProcessOptionsFragment = new ProcessMultipleOptionsFragment();
				break;
			case EXTRA_PROCESS_TEXT_FIELD:
				mProcessOptionsFragment = new ProcessTextFieldOptionsFragment();
				break;
			}
		}
		FragmentTransaction transaction = getSupportFragmentManager()
				.beginTransaction();
		transaction.add(R.id.create_task_options, mProcessOptionsFragment);
		transaction.commit();
	}

	private void setFragmentOptions() {
		Bundle args = SettingsActivity.getDefaultOptions(this, mProcessingMode);
		mProcessOptionsFragment.setDefaultOptions(args);
	}

	private void setActionBar() {

	}

	private void setSpinner() {
		mProcessSpinnerView.setAdapter(getSpinnerAdapter());
	}

	private SpinnerAdapter getSpinnerAdapter() {
		SpinnerAdapter adapter = null;
		return adapter;
	}
}
