package com.abbyy.cloudocr.optionsfragments;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter.CursorToStringConverter;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.abbyy.cloudocr.R;
import com.abbyy.cloudocr.SettingsActivity;
import com.abbyy.cloudocr.TasksManagerService;
import com.abbyy.cloudocr.database.TasksContract;
import com.abbyy.cloudocr.utils.CloudClient;
import com.abbyy.cloudocr.utils.LanguageHelper;

public class ProcessImageOptionsFragment extends ProcessOptionsFragment
		implements OnClickListener {
	private static final int LOADER_AUTOCOMPLETE = 1;

	private static final int ACTIVITY_GET_FILE = 1;

	private static final int CODE_EXPORT_FORMAT = 1;
	private static final int CODE_PROFILE = 2;

	private LanguageHelper mLanguageHelper;

	private String mProfile;
	private String mExportFormat;
	private String mDescription;
	private String mFilePath;

	private Spinner mExportFormatView;
	private Spinner mProfileView;
	private EditText mDescriptionView;
	private Button mManageLanguagesView;
	private TextView mFileViewHint;
	private ImageView mFileView;

	// Necessary for autocompletion
	private AutoCompleteTextView mAddLanguagesView;
	private SimpleCursorAdapter mAutoCompleteAdapter;
	private String mLanguage;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if(container == null) {
			return null;
		}
		return inflater.inflate(R.layout.process_image_fragment, container,
				false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		prefs = getActivity().getSharedPreferences(
				SettingsActivity.PREFERENCES_PROCESS_IMAGE,
				Activity.MODE_PRIVATE);
		mLanguageHelper = new LanguageHelper(getActivity().getResources()
				.getStringArray(R.array.languages));
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_process:
			launchTask();
			break;
		default:
			return super.onOptionsItemSelected(item);
		}
		return true;
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case ACTIVITY_GET_FILE:
			switch (resultCode) {
			case Activity.RESULT_OK:
				addFile(data.getData().toString());
				break;
			}
			break;
		}
	}

	@Override
	public void launchTask() {
		if (mFilePath != null) {
			mClient.setFilePath(mFilePath);
		}
		if (mClient.getFilePath() == null) {
			Toast.makeText(getActivity(), R.string.error_no_file_selected,
					Toast.LENGTH_LONG).show();
		} else {
			saveDefaultOptions();
			Intent service = new Intent(getActivity(),
					TasksManagerService.class);
			service.putExtra(TasksManagerService.EXTRA_ACTION,
					TasksManagerService.ACTION_CREATE_NEW_TASK);
			service.putExtra(TasksManagerService.EXTRA_NEW_TASK_OPTIONS,
					getOptions());
			getActivity().startService(service);
		}
	}

	@Override
	public void addFile(String filePath) {
		mFilePath = filePath;
		if (mFileView != null) {
			mFileViewHint.setVisibility(View.GONE);
			mFileView.setVisibility(View.VISIBLE);
			mFileView.setImageURI(Uri.parse(filePath));
		}
	}

	@Override
	public boolean saveDefaultOptions() {
		Bundle bundle = getOptions();
		SharedPreferences.Editor editor = prefs.edit();
		editor.putString(CloudClient.ARGUMENT_EXPORT_FORMAT,
				bundle.getString(CloudClient.ARGUMENT_EXPORT_FORMAT));
		editor.putString(CloudClient.ARGUMENT_PROFILE,
				bundle.getString(CloudClient.ARGUMENT_PROFILE));
		editor.putString(CloudClient.ARGUMENT_DESCRIPTION,
				bundle.getString(CloudClient.ARGUMENT_DESCRIPTION));
		editor.putString(CloudClient.ARGUMENT_LANGUAGE,
				bundle.getString(CloudClient.ARGUMENT_LANGUAGE));
		return editor.commit();
	}

	@Override
	public boolean loadDefaultOptions() {
		mExportFormat = prefs.getString(CloudClient.ARGUMENT_EXPORT_FORMAT, "");
		mProfile = prefs.getString(CloudClient.ARGUMENT_PROFILE, "");
		mDescription = prefs.getString(CloudClient.ARGUMENT_DESCRIPTION, "");
		mLanguageHelper.createLanguages(prefs.getString(
				CloudClient.ARGUMENT_LANGUAGE, ""));

		return true;
	}

	public Bundle getOptions() {
		mDescription = mDescriptionView.getText().toString();
		Bundle bundle = new Bundle();
		bundle.putString(CloudClient.ARGUMENT_EXPORT_FORMAT, mExportFormat);
		bundle.putString(CloudClient.ARGUMENT_PROFILE, mProfile);
		bundle.putString(CloudClient.ARGUMENT_DESCRIPTION, mDescription);
		bundle.putString(CloudClient.ARGUMENT_LANGUAGE,
				mLanguageHelper.getLanguages());
		return bundle;
	}

	@Override
	boolean setViews() {
		mExportFormatView = (Spinner) getActivity().findViewById(
				R.id.option_export_format);
		mProfileView = (Spinner) getActivity()
				.findViewById(R.id.option_profile);
		mDescriptionView = (EditText) getActivity().findViewById(
				R.id.option_description);
		mAddLanguagesView = (AutoCompleteTextView) getActivity().findViewById(
				R.id.option_add_languages);
		mManageLanguagesView = (Button) getActivity().findViewById(
				R.id.option_button_manage_language);
		mFileView = (ImageView) getActivity().findViewById(
				R.id.option_file_path);
		mFileViewHint = (TextView) getActivity().findViewById(
				R.id.option_file_path_hint);

		if(mExportFormatView == null){
			return false;
		}
		mExportFormatView.setAdapter(getSpinnerAdapter(CODE_EXPORT_FORMAT));
		mExportFormatView
				.setOnItemSelectedListener(getOnItemSelectedListener(CODE_EXPORT_FORMAT));

		mProfileView.setAdapter(getSpinnerAdapter(CODE_PROFILE));
		mProfileView
				.setOnItemSelectedListener(getOnItemSelectedListener(CODE_PROFILE));

		mManageLanguagesView.setOnClickListener(this);

		mFileViewHint.setOnClickListener(this);
		mFileView.setOnClickListener(this);

		prepareLanguageAutoComplete();
		return true;
	}

	private void prepareLanguageAutoComplete() {
		// Prepare the adapter for the language auto complete field
		String[] from = { TasksContract.LanguagesTable.LANGUAGE };
		int[] to = { android.R.id.text1 };
		mAutoCompleteAdapter = new SimpleCursorAdapter(getActivity(),
				android.R.layout.simple_spinner_dropdown_item, null, from, to,
				0);

		mAddLanguagesView.setAdapter(mAutoCompleteAdapter);
		mAddLanguagesView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> view, View v, int position,
					long id) {
				TextView languageView = (TextView) v;
				if (mLanguageHelper.addLanguage(languageView.getText()
						.toString())) {
					Toast.makeText(
							getActivity(),
							languageView.getText().toString() + " "
									+ getString(R.string.added),
							Toast.LENGTH_SHORT).show();
					mAddLanguagesView.setText("");
				}
			}
		});
		mAddLanguagesView.setThreshold(0);

		// Set the text watcher
		mAddLanguagesView.addTextChangedListener(new TextWatcher() {
			@Override
			public void afterTextChanged(Editable s) {
				if (!s.equals("")) {
					mLanguage = s.toString();
					getActivity().getSupportLoaderManager().restartLoader(
							LOADER_AUTOCOMPLETE, null,
							new LanguageLoaderHelper());
				} else {
					mLanguage = "";
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int count,
					int after) {
			}

		});

		// Add the right language to the field
		mAutoCompleteAdapter
				.setCursorToStringConverter(new CursorToStringConverter() {
					@Override
					public CharSequence convertToString(Cursor cursor) {
						return cursor.getString(cursor
								.getColumnIndex(TasksContract.LanguagesTable.LANGUAGE));
					}
				});
	}

	private void getFile() {
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
		intent.setType("image/*");
		startActivityForResult(intent, ACTIVITY_GET_FILE);
	}

	private void manageLanguages() {
		Toast.makeText(getActivity(), mLanguageHelper.getLanguages(),
				Toast.LENGTH_SHORT).show();

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

	private class LanguageLoaderHelper implements LoaderCallbacks<Cursor> {

		@Override
		public Loader<Cursor> onCreateLoader(int id, Bundle args) {
			Uri uri = TasksContract.CONTENT_LANGUAGES;
			String selection = TasksContract.LanguagesTable.LANGUAGE
					+ " LIKE ?";
			String[] selectionArgs = { "%" + mLanguage + "%" };
			Loader<Cursor> loader = new CursorLoader(getActivity(), uri, null,
					selection, selectionArgs, null);
			return loader;
		}

		@Override
		public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
			mAutoCompleteAdapter.swapCursor(cursor);
		}

		@Override
		public void onLoaderReset(Loader<Cursor> loader) {
			mAutoCompleteAdapter.swapCursor(null);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.option_file_path:
		case R.id.option_file_path_hint:
			getFile();
			break;
		case R.id.option_button_manage_language:
			manageLanguages();
			break;
		}

	}
}