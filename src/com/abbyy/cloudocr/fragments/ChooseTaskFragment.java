package com.abbyy.cloudocr.fragments;

import com.abbyy.cloudocr.CreateTaskActivity;
import com.abbyy.cloudocr.R;
import com.abbyy.cloudocr.SettingsActivity;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

public class ChooseTaskFragment extends Fragment implements OnClickListener {
	private boolean isLandscape;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (container == null) {
			return null;
		}
		return inflater.inflate(R.layout.start, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		isLandscape = getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
		super.onActivityCreated(savedInstanceState);
		prepareButtons();
	}

	private void prepareButtons() {
		Button processImageButton = (Button) getActivity().findViewById(
				R.id.process_image);
//		Button processMultipleImagesButton = (Button) getActivity()
//				.findViewById(R.id.process_multiple);
		Button processBusinessCardButton = (Button) getActivity().findViewById(
				R.id.process_business_card);
//		Button processTextFieldButton = (Button) getActivity().findViewById(
//				R.id.process_text_field);
//		Button processBarcodeFieldButton = (Button) getActivity().findViewById(
//				R.id.process_barcode_field);
//		Button processCheckmarkFieldButton = (Button) getActivity()
//				.findViewById(R.id.process_checkmark_field);
//		Button processFieldsButton = (Button) getActivity().findViewById(
//				R.id.process_fields);

		processImageButton.setOnClickListener(this);
//		processMultipleImagesButton.setOnClickListener(this);
		processBusinessCardButton.setOnClickListener(this);
//		processTextFieldButton.setOnClickListener(this);
//		processBarcodeFieldButton.setOnClickListener(this);
//		processCheckmarkFieldButton.setOnClickListener(this);
//		processFieldsButton.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		if (isLandscape) {

		} else {
			
		}
		
		Intent intent = new Intent(getActivity(), CreateTaskActivity.class);
		switch (v.getId()) {
		case R.id.process_image:
			intent.putExtra(CreateTaskActivity.EXTRA_PROCESS_MODE,
					SettingsActivity.PROCESSING_MODE_IMAGE);
			break;
//		case R.id.process_multiple:
//			intent.putExtra(CreateTaskActivity.EXTRA_PROCESS_MODE,
//					SettingsActivity.PROCESSING_MODE_MULTIPLE);
//			break;
		case R.id.process_business_card:
			intent.putExtra(CreateTaskActivity.EXTRA_PROCESS_MODE,
					SettingsActivity.PROCESSING_MODE_BUSINESS_CARD);
			break;
//		case R.id.process_text_field:
//			intent.putExtra(CreateTaskActivity.EXTRA_PROCESS_MODE,
//					SettingsActivity.PROCESSING_MODE_TEXT_FIELD);
//			break;
//		case R.id.process_barcode_field:
//			intent.putExtra(CreateTaskActivity.EXTRA_PROCESS_MODE,
//					SettingsActivity.PROCESSING_MODE_BARCODE_FIELD);
//			break;
//		case R.id.process_checkmark_field:
//			intent.putExtra(CreateTaskActivity.EXTRA_PROCESS_MODE,
//					SettingsActivity.PROCESSING_MODE_CHECKMARK_FIELD);
//			break;
//		case R.id.process_fields:
//			intent.putExtra(CreateTaskActivity.EXTRA_PROCESS_MODE,
//					SettingsActivity.PROCESSING_MODE_FIELDS);
//			break;
		}
		startActivity(intent);
	}
}
