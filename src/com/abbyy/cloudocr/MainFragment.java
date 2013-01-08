package com.abbyy.cloudocr;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

public class MainFragment extends Fragment implements OnClickListener{

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.main_fragment, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		setButtons();
	}
	
	private void setButtons(){
		Button processImageButton = (Button) getActivity().findViewById(R.id.process_image);
		Button processMultipleImagesButton = (Button) getActivity().findViewById(R.id.process_multiple);
		Button processBusinessCardButton = (Button) getActivity().findViewById(R.id.process_business_card);
		Button processTextFieldButton = (Button) getActivity().findViewById(R.id.process_text_field);
		Button processBarcodeFieldButton = (Button) getActivity().findViewById(R.id.process_barcode_field);
		Button processCheckmarkFieldButton = (Button) getActivity().findViewById(R.id.process_checkmark_field);
		Button processFieldsButton = (Button) getActivity().findViewById(R.id.process_fields);
		
		processImageButton.setOnClickListener(this);
		processMultipleImagesButton.setOnClickListener(this);
		processBusinessCardButton.setOnClickListener(this);
		processTextFieldButton.setOnClickListener(this);
		processBarcodeFieldButton.setOnClickListener(this);
		processCheckmarkFieldButton.setOnClickListener(this);
		processFieldsButton.setOnClickListener(this);
	}

	@Override
	public void onClick(View view) {
		Intent intent = new Intent(getActivity(), ProcessActivity.class);
		switch(view.getId()){
		case R.id.process_image:
			intent.putExtra(ProcessActivity.EXTRA_PROCESS_MODE, ProcessActivity.EXTRA_PROCESS_IMAGE);
			break;
		case R.id.process_multiple:
			intent.putExtra(ProcessActivity.EXTRA_PROCESS_MODE, ProcessActivity.EXTRA_PROCESS_MULTIPLE);
			break;
		case R.id.process_business_card:
			intent.putExtra(ProcessActivity.EXTRA_PROCESS_MODE, ProcessActivity.EXTRA_PROCESS_BUSINESS_CARD);
			break;
		case R.id.process_text_field:
			intent.putExtra(ProcessActivity.EXTRA_PROCESS_MODE, ProcessActivity.EXTRA_PROCESS_TEXT_FIELD);
			break;
		case R.id.process_barcode_field:
			intent.putExtra(ProcessActivity.EXTRA_PROCESS_MODE, ProcessActivity.EXTRA_PROCESS_BARCODE_FIELD);
			break;
		case R.id.process_checkmark_field:
			intent.putExtra(ProcessActivity.EXTRA_PROCESS_MODE, ProcessActivity.EXTRA_PROCESS_CHECKMARK_FIELD);
			break;
		case R.id.process_fields:
			intent.putExtra(ProcessActivity.EXTRA_PROCESS_MODE, ProcessActivity.EXTRA_PROCESS_FIELDS);
			break;
		}
		startActivity(intent);
	}
}
