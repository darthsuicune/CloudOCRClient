package com.abbyy.cloudocr;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.abbyy.cloudocr.compat.ActionBarActivity;

public class StartActivity extends ActionBarActivity implements OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.start);
		prepareButtons();
	}

	private void prepareButtons() {
		Button processImageButton = (Button) findViewById(R.id.process_image);
		Button processMultipleImagesButton = (Button) findViewById(R.id.process_multiple);
		Button processBusinessCardButton = (Button) findViewById(R.id.process_business_card);
		Button processTextFieldButton = (Button) findViewById(R.id.process_text_field);
		Button processBarcodeFieldButton = (Button) findViewById(R.id.process_barcode_field);
		Button processCheckmarkFieldButton = (Button) findViewById(R.id.process_checkmark_field);
		Button processFieldsButton = (Button) findViewById(R.id.process_fields);

		processImageButton.setOnClickListener(this);
		processMultipleImagesButton.setOnClickListener(this);
		processBusinessCardButton.setOnClickListener(this);
		processTextFieldButton.setOnClickListener(this);
		processBarcodeFieldButton.setOnClickListener(this);
		processCheckmarkFieldButton.setOnClickListener(this);
		processFieldsButton.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		Intent intent = new Intent(this, CreateTaskActivity.class);
		switch (v.getId()) {
		case R.id.process_image:
			intent.putExtra(CreateTaskActivity.EXTRA_PROCESS_MODE,
					SettingsActivity.PROCESSING_MODE_IMAGE);
			break;
		case R.id.process_multiple:
			intent.putExtra(CreateTaskActivity.EXTRA_PROCESS_MODE,
					SettingsActivity.PROCESSING_MODE_MULTIPLE);
			break;
		case R.id.process_business_card:
			intent.putExtra(CreateTaskActivity.EXTRA_PROCESS_MODE,
					SettingsActivity.PROCESSING_MODE_BUSINESS_CARD);
			break;
		case R.id.process_text_field:
			intent.putExtra(CreateTaskActivity.EXTRA_PROCESS_MODE,
					SettingsActivity.PROCESSING_MODE_TEXT_FIELD);
			break;
		case R.id.process_barcode_field:
			intent.putExtra(CreateTaskActivity.EXTRA_PROCESS_MODE,
					SettingsActivity.PROCESSING_MODE_BARCODE_FIELD);
			break;
		case R.id.process_checkmark_field:
			intent.putExtra(CreateTaskActivity.EXTRA_PROCESS_MODE,
					SettingsActivity.PROCESSING_MODE_CHECKMARK_FIELD);
			break;
		case R.id.process_fields:
			intent.putExtra(CreateTaskActivity.EXTRA_PROCESS_MODE,
					SettingsActivity.PROCESSING_MODE_FIELDS);
			break;
		}
		startActivity(intent);
	}
}
