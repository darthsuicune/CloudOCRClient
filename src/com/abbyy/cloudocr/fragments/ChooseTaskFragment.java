package com.abbyy.cloudocr.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.abbyy.cloudocr.CreateTaskActivity;
import com.abbyy.cloudocr.R;
import com.abbyy.cloudocr.SettingsActivity;
import com.abbyy.cloudocr.optionsfragments.ProcessBusinessCardOptionsFragment;
import com.abbyy.cloudocr.optionsfragments.ProcessImageOptionsFragment;
import com.abbyy.cloudocr.optionsfragments.ProcessOptionsFragment;

/**
 * Easy fragment for displaying the different processing types available.
 * 
 * @author Denis Lapuente
 * 
 */
public class ChooseTaskFragment extends Fragment implements OnClickListener {

	/**
	 * Called when the view for the fragment is created. If the container is
	 * null, we don't need any view at all. If it is not null, we inflte the
	 * corresponding layout.
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (container == null) {
			return null;
		}
		return inflater.inflate(R.layout.start, container, false);
	}

	/**
	 * Called when the activity is created (or the fragment is created if the
	 * activity was all there. Always executed AFTER onCreateView.
	 * 
	 * We just prepare the things we will be using later.
	 */
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		prepareButtons();
	}

	/**
	 * Convenience method for getting the buttons ready. When new buttons are
	 * added, just uncommenting them would work
	 */
	private void prepareButtons() {
		Button processImageButton = (Button) getActivity().findViewById(
				R.id.process_image);

		// If the button is null, the view is not available and then we don't
		// need to set its values
		if (processImageButton == null) {
			return;
		}
		Button processBusinessCardButton = (Button) getActivity().findViewById(
				R.id.process_business_card);
		// Button processMultipleImagesButton = (Button) getActivity()
		// .findViewById(R.id.process_multiple);
		// Button processTextFieldButton = (Button) getActivity().findViewById(
		// R.id.process_text_field);
		// Button processBarcodeFieldButton = (Button)
		// getActivity().findViewById(
		// R.id.process_barcode_field);
		// Button processCheckmarkFieldButton = (Button) getActivity()
		// .findViewById(R.id.process_checkmark_field);
		// Button processFieldsButton = (Button) getActivity().findViewById(
		// R.id.process_fields);

		processImageButton.setOnClickListener(this);
		processBusinessCardButton.setOnClickListener(this);
		// processMultipleImagesButton.setOnClickListener(this);
		// processTextFieldButton.setOnClickListener(this);
		// processBarcodeFieldButton.setOnClickListener(this);
		// processCheckmarkFieldButton.setOnClickListener(this);
		// processFieldsButton.setOnClickListener(this);
	}

	/**
	 * Manages the click on the buttons. It will create the corresponding
	 * fragment and insert it into its place. If it requires a new Activity, it
	 * will call it. If it doesn't require it, it will replace this fragment
	 * with the new one
	 */
	@Override
	public void onClick(View v) {
		if (isMainActivity()) {
			ProcessOptionsFragment fragment;
			switch (v.getId()) {
			case R.id.process_business_card:
				fragment = new ProcessBusinessCardOptionsFragment();
				break;
			case R.id.process_image:
			default:
				fragment = new ProcessImageOptionsFragment();
				break;
			}
			FragmentTransaction ft = getFragmentManager().beginTransaction();
			ft.replace(R.id.main_activity_second_fragment, fragment).commit();
		} else {
			Intent intent = new Intent(getActivity(), CreateTaskActivity.class);
			switch (v.getId()) {
			case R.id.process_image:
				intent.putExtra(CreateTaskActivity.EXTRA_PROCESS_MODE,
						SettingsActivity.PROCESSING_MODE_IMAGE);
				break;
			case R.id.process_business_card:
				intent.putExtra(CreateTaskActivity.EXTRA_PROCESS_MODE,
						SettingsActivity.PROCESSING_MODE_BUSINESS_CARD);
				break;
			// case R.id.process_multiple:
			// intent.putExtra(CreateTaskActivity.EXTRA_PROCESS_MODE,
			// SettingsActivity.PROCESSING_MODE_MULTIPLE);
			// break;
			// case R.id.process_text_field:
			// intent.putExtra(CreateTaskActivity.EXTRA_PROCESS_MODE,
			// SettingsActivity.PROCESSING_MODE_TEXT_FIELD);
			// break;
			// case R.id.process_barcode_field:
			// intent.putExtra(CreateTaskActivity.EXTRA_PROCESS_MODE,
			// SettingsActivity.PROCESSING_MODE_BARCODE_FIELD);
			// break;
			// case R.id.process_checkmark_field:
			// intent.putExtra(CreateTaskActivity.EXTRA_PROCESS_MODE,
			// SettingsActivity.PROCESSING_MODE_CHECKMARK_FIELD);
			// break;
			// case R.id.process_fields:
			// intent.putExtra(CreateTaskActivity.EXTRA_PROCESS_MODE,
			// SettingsActivity.PROCESSING_MODE_FIELDS);
			// break;
			}
			startActivity(intent);
		}
	}

	/**
	 * Convenience method to check if we are on the main activity or not.
	 * 
	 * @return
	 */
	private boolean isMainActivity() {
		return getActivity().getClass().getName().contains("Main");
	}
}
