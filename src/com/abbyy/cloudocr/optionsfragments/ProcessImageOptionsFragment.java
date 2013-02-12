package com.abbyy.cloudocr.optionsfragments;

import java.util.ArrayList;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import com.abbyy.cloudocr.R;
import com.abbyy.cloudocr.SettingsActivity;

public class ProcessImageOptionsFragment extends ProcessOptionsFragment {
	private static final int CODE_EXPORT_FORMAT = 1;
	private static final int CODE_PROFILE = 2;

	private static final String EXPORT_FORMAT = "exportFormat";
	private static final String PROFILE = "profile";
	private static final String DESCRIPTION = "description";
	private static final String LANGUAGES = "language";

	private ArrayList<String> mLanguagesList;
	private String mProfile;
	private String mExportFormat;
	private String mDescription;

	private Spinner mExportFormatView;
	private Spinner mProfileView;
	private EditText mDescriptionView;
	private Button mAddLanguagesView;

	String createURL() {
		return BASE_URL + addOptionsToURL();
	}

	private String addOptionsToURL() {
		Bundle options = getOptions();
		String result = "processImage?";

		String exportFormat = options.getString(EXPORT_FORMAT);
		if (!exportFormat.equals("")) {
			result = result.concat(EXPORT_FORMAT + "=" + exportFormat + "&");
		}

		String profile = options.getString(PROFILE);
		if (!profile.equals("")) {
			result = result.concat(PROFILE + "=" + profile + "&");
		}

		String description = options.getString(DESCRIPTION);
		if (!description.equals("")) {
			result = result.concat(DESCRIPTION + "=\"" + description + "\"&");
		}

		String languages = options.getString(LANGUAGES);
		if (!languages.equals("")) {
			result = result.concat(LANGUAGES + "=" + languages);
		}
		return result;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.process_image_fragment, container,
				false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public boolean saveDefaultOptions() {
		Bundle bundle = getOptions();
		SharedPreferences.Editor editor = getActivity().getSharedPreferences(
				SettingsActivity.PREFERENCES_PROCESS_IMAGE,
				Activity.MODE_PRIVATE).edit();
		editor.putString(EXPORT_FORMAT, bundle.getString(EXPORT_FORMAT));
		editor.putString(PROFILE, bundle.getString(PROFILE));
		editor.putString(DESCRIPTION, bundle.getString(DESCRIPTION));
		editor.putString(LANGUAGES, createLanguages());
		return editor.commit();
	}

	@Override
	public boolean loadDefaultOptions() {
		SharedPreferences prefs = getActivity().getSharedPreferences(
				SettingsActivity.PREFERENCES_PROCESS_IMAGE,
				Activity.MODE_PRIVATE);
		mExportFormat = prefs.getString(EXPORT_FORMAT, "");
		mProfile = prefs.getString(PROFILE, "");
		mDescription = prefs.getString(DESCRIPTION, "");
		createLanguages(prefs.getString(LANGUAGES, ""));

		mAddLanguagesView.setOnClickListener(new AddLanguagesListener(this,
				mLanguagesList));

		return true;
	}

	public Bundle getOptions() {
		mDescription = mDescriptionView.getText().toString();
		Bundle bundle = new Bundle();
		bundle.putString(EXPORT_FORMAT, mExportFormat);
		bundle.putString(PROFILE, mProfile);
		bundle.putString(DESCRIPTION, mDescription);
		bundle.putString(LANGUAGES, createLanguages());
		return bundle;
	}

	@Override
	void setViews() {
		mExportFormatView = (Spinner) getActivity().findViewById(
				R.id.option_export_format);
		mProfileView = (Spinner) getActivity()
				.findViewById(R.id.option_profile);
		mDescriptionView = (EditText) getActivity().findViewById(
				R.id.option_description);
		mAddLanguagesView = (Button) getActivity().findViewById(
				R.id.option_add_languages);

		mExportFormatView.setAdapter(getSpinnerAdapter(CODE_EXPORT_FORMAT));
		mExportFormatView
				.setOnItemSelectedListener(getOnItemSelectedListener(CODE_EXPORT_FORMAT));

		mProfileView.setAdapter(getSpinnerAdapter(CODE_PROFILE));
		mProfileView
				.setOnItemSelectedListener(getOnItemSelectedListener(CODE_PROFILE));
	}

	private OnItemSelectedListener getOnItemSelectedListener(final int code) {
		OnItemSelectedListener listener = new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> adapterView, View v,
					int position, long id) {
				switch (code) {
				case CODE_EXPORT_FORMAT:
					String[] exportFormats = getActivity().getResources()
							.getStringArray(R.array.export_formats);

					mExportFormat = exportFormats[position];
					break;
				case CODE_PROFILE:
					String[] profiles = getActivity().getResources()
							.getStringArray(R.array.profiles);
					mProfile = profiles[position];
					break;
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> v) {
			}
		};
		return listener;
	}

	private SpinnerAdapter getSpinnerAdapter(int code) {
		ArrayAdapter<String> adapter = null;
		switch (code) {
		case CODE_EXPORT_FORMAT:
			adapter = new ArrayAdapter<String>(getActivity(),
					android.R.layout.simple_spinner_item, getActivity()
							.getResources().getStringArray(
									R.array.export_formats));
			break;
		case CODE_PROFILE:
			adapter = new ArrayAdapter<String>(getActivity(),
					android.R.layout.simple_spinner_item, getActivity()
							.getResources().getStringArray(R.array.profiles));
			break;
		}
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		return adapter;
	}

	private String createLanguages() {
		String languages = "";
		for (int i = 0; i < mLanguagesList.size(); i++) {
			if (i > 0) {
				languages = languages.concat(",");
			}
			languages = languages.concat(mLanguagesList.get(i));
		}
		return languages;
	}

	private void createLanguages(String languages) {
		if(mLanguagesList == null){
			mLanguagesList = new ArrayList<String>();
		}

		if (!languages.equals("")) {
			if (!languages.contains(",")) {
				mLanguagesList.add(languages);
			} else {
				String language = languages.substring(
						languages.lastIndexOf(',') + 1, languages.length());
				mLanguagesList.add(language);
				int index = languages.lastIndexOf(',');
				if (index > 0) {
					createLanguages(languages.substring(0, index));
				}
			}
		}
	}

	@Override
	void addLanguage(String language) {
		if (mLanguagesList == null) {
			mLanguagesList = new ArrayList<String>();
		}
		mLanguagesList.add(language);
	}
}