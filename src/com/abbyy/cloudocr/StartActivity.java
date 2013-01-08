package com.abbyy.cloudocr;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.abbyy.cloudocr.compat.ActionBarActivity;
import com.abbyy.cloudocr.compat.ActionBarHelper;

public class StartActivity extends ActionBarActivity implements OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.start);
		prepareButtons();
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onClick(View v) {
		Intent intent = new Intent(this, CreateTaskActivity.class);
		switch (v.getId()) {
		case R.id.process_image:
			intent.putExtra(CreateTaskActivity.EXTRA_PROCESS_MODE,
					CreateTaskActivity.EXTRA_PROCESS_IMAGE);
			break;
		case R.id.process_multiple:
			intent.putExtra(CreateTaskActivity.EXTRA_PROCESS_MODE,
					CreateTaskActivity.EXTRA_PROCESS_MULTIPLE);
			break;
		case R.id.process_business_card:
			intent.putExtra(CreateTaskActivity.EXTRA_PROCESS_MODE,
					CreateTaskActivity.EXTRA_PROCESS_BUSINESS_CARD);
			break;
		case R.id.process_text_field:
			intent.putExtra(CreateTaskActivity.EXTRA_PROCESS_MODE,
					CreateTaskActivity.EXTRA_PROCESS_TEXT_FIELD);
			break;
		case R.id.process_barcode_field:
			intent.putExtra(CreateTaskActivity.EXTRA_PROCESS_MODE,
					CreateTaskActivity.EXTRA_PROCESS_BARCODE_FIELD);
			break;
		case R.id.process_checkmark_field:
			intent.putExtra(CreateTaskActivity.EXTRA_PROCESS_MODE,
					CreateTaskActivity.EXTRA_PROCESS_CHECKMARK_FIELD);
			break;
		case R.id.process_fields:
			intent.putExtra(CreateTaskActivity.EXTRA_PROCESS_MODE,
					CreateTaskActivity.EXTRA_PROCESS_FIELDS);
			break;
		}
		startActivity(intent);
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
}
