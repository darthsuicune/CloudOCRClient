package com.abbyy.cloudocr.optionsfragments;

import java.io.File;
import java.io.FileNotFoundException;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

/**
 * Fragment that shows the different options for processing a business card.
 * 
 * Will show on screen the Output format wanted for the output, a preview area
 * for the image to send, a space for the languages and another one for the
 * description of the file
 * 
 * @author Denis Lapuente
 * 
 */
public class ProcessBusinessCardOptionsFragment extends ProcessOptionsFragment
		implements OnClickListener, OnItemSelectedListener,
		LoaderCallbacks<Cursor> {
	// Constants for the inner workings.
	private static final int LOADER_AUTOCOMPLETE = 1;
	private static final int ACTIVITY_GET_FILE = 1;
	private static final int ACTIVITY_TAKE_PICTURE = 2;

	// Instance of the language helper, to work with languages.
	private LanguageHelper mLanguageHelper;

	// Constants that hold the variables
	private String mExportFormat;
	private String mDescription;
	private Uri mFileUri;

	// Global variables for easy access to the visual components
	private Spinner mExportFormatView;
	private EditText mDescriptionView;
	private Button mManageLanguagesView;
	private TextView mFileViewHint;
	private ImageView mFileView;

	// Used for autocompletion
	private AutoCompleteTextView mAddLanguagesView;
	private SimpleCursorAdapter mAutoCompleteAdapter;
	private String mLanguage;

	/**
	 * Method called by the system when loading the fragment view. If the
	 * container is empty we don't need to return anything as the fragment is
	 * off screen. If it is on screen, we inflate the corresponding layout.
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (container == null) {
			return null;
		}
		return inflater.inflate(R.layout.process_business_card_fragment,
				container, false);
	}

	/**
	 * After the view is set, and the activity is created, we set the different
	 * options and standard arguments.
	 */
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		prefs = getActivity().getSharedPreferences(
				SettingsActivity.PREFERENCES_PROCESS_BUSINESS_CARD,
				Activity.MODE_PRIVATE);
		mLanguageHelper = new LanguageHelper(getActivity().getResources()
				.getStringArray(R.array.languages));
		// This call to super will set the views, options menu and default
		// options.
		super.onActivityCreated(savedInstanceState);

		// If the arguments for the fragment are not null, we set them.
		if (getArguments() != null && getArguments().containsKey(ARG_FILE_PATH)) {
			addFile(Uri.parse(getArguments().getString(ARG_FILE_PATH)));
		}
		// If the image set on the preview area was saved, set it again. It is
		// important to do it in this order. If not, a saved image from the user
		// might get overwritten by the saved image from the first call
		if (savedInstanceState != null
				&& savedInstanceState.containsKey(ARG_FILE_PATH)) {
			addFile(Uri.parse(savedInstanceState.getString(ARG_FILE_PATH)));

		}
	}

	/**
	 * When saving the instance state, we save the file so it gets correctly
	 * restored later.
	 * 
	 * TODO: Save rest of the options
	 */
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		if (mFileUri != null) {
			outState.putString(ARG_FILE_PATH, mFileUri.toString());
		}
	}

	/**
	 * When we come back from a different activity, we might need to do some
	 * actions. This are parsed and performed here.
	 */

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		// When we retrieve a picture from the gallery
		case ACTIVITY_GET_FILE:
			// Do nothing
			break;
		// When we get a picture with the camera we need to add it to the
		// gallery
		case ACTIVITY_TAKE_PICTURE:
			galleryAddPic();
			break;
		}
		// In any case, with a correct result and with retrieved data, we should
		// update the picture
		switch (resultCode) {
		case Activity.RESULT_OK:
			if (data != null) {
				addFile(data.getData());
			} else {
				if (mFileUri != null) {
					setPreview();
				}
			}
		}
	}

	/**
	 * Convenience method for setting the file related to the preview area of
	 * the image
	 */

	@Override
	public void addFile(Uri filePath) {
		mFileUri = filePath;
		setPreview();
	}

	/**
	 * Convenience method for setting the preview area correctly. If the area is
	 * not visible (fragment off screen) we just return. If the area is visible,
	 * we remove the hint view, activate this one, and set up a thumbnail to
	 * insert into place to avoid out of memory errors.
	 */
	private void setPreview() {
		if (mFileView == null) {
			return;
		}
		try {
			mFileViewHint.setVisibility(View.GONE);
			mFileView.setVisibility(View.VISIBLE);
			mFileView.setImageBitmap(getSmallImage());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Convenience method for creating a thumbnail out of a full scaled image.
	 * It creates a scaled image of the file and returns its bitmap.
	 * 
	 * @return scaled bitmap containing the preview image.
	 * @throws FileNotFoundException
	 */
	private Bitmap getSmallImage() throws FileNotFoundException {
		// Create a BitmapFactory Options to scale the image down
		BitmapFactory.Options options = new BitmapFactory.Options();
		// This parameter avoids the next call image from being loaded into
		// memory
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeStream(getActivity().getContentResolver()
				.openInputStream(mFileUri));

		// Obtain a sampling ratio
		int outHeight = options.outHeight;
		int outWidth = options.outWidth;
		options.inSampleSize = getSampleSize(outHeight, outWidth);
		// Now we can decode the image and load the small sample size into
		// memory
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeStream(getActivity().getContentResolver()
				.openInputStream(mFileUri));
	}

	/**
	 * Convenience method for calculating the required sampling size to create
	 * the thumbnail
	 * 
	 * @param originalHeight
	 *            original height of the image
	 * @param originalWidth
	 *            original width of the image
	 * @return an int with the sampling size.
	 */
	private int getSampleSize(int originalHeight, int originalWidth) {
		int requiredHeight = mFileView.getHeight();
		int requiredWidth = mFileView.getWidth();
		if (requiredHeight == 0) {
			return 4;
		}
		int sampleSize = 1;
		if (requiredHeight < originalHeight || requiredWidth < originalWidth) {
			final int heightRatio = Math.round((float) requiredHeight
					/ (float) originalHeight);
			final int widthRatio = Math.round((float) requiredWidth
					/ (float) originalWidth);
			sampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
		}
		return sampleSize;
	}

	/**
	 * Method that launches the processing task. Requires that the file uri is
	 * set to something different than null (that is, that an image exists)
	 */
	@Override
	public void launchTask() {
		if (mFileUri == null) {
			Toast.makeText(getActivity(), R.string.error_no_file_selected,
					Toast.LENGTH_LONG).show();
			return;
		}
		saveDefaultOptions();
		Intent service = new Intent(getActivity(), TasksManagerService.class);
		service.putExtra(TasksManagerService.EXTRA_FILE_PATH,
				mFileUri.toString());
		service.putExtra(TasksManagerService.EXTRA_ACTION,
				TasksManagerService.ACTION_CREATE_NEW_TASK);
		service.putExtra(TasksManagerService.EXTRA_CREATE,
				R.string.process_business_card);
		service.putExtra(TasksManagerService.EXTRA_NEW_TASK_OPTIONS,
				getOptions());
		getActivity().startService(service);
	}

	/**
	 * Saves the default options for the fragment for further use
	 */
	@Override
	public boolean saveDefaultOptions() {
		Bundle bundle = getOptions();
		SharedPreferences.Editor editor = prefs.edit();
		editor.putString(CloudClient.ARGUMENT_EXPORT_FORMAT,
				bundle.getString(CloudClient.ARGUMENT_EXPORT_FORMAT));
		editor.putString(CloudClient.ARGUMENT_DESCRIPTION,
				bundle.getString(CloudClient.ARGUMENT_DESCRIPTION));
		editor.putString(CloudClient.ARGUMENT_LANGUAGE,
				bundle.getString(CloudClient.ARGUMENT_LANGUAGE));
		return editor.commit();
	}

	/**
	 * Loads previously saved default options
	 */
	@Override
	public boolean loadDefaultOptions() {
		mExportFormat = prefs.getString(CloudClient.ARGUMENT_EXPORT_FORMAT, "");
		mDescription = prefs.getString(CloudClient.ARGUMENT_DESCRIPTION, "");
		mLanguageHelper.createLanguages(prefs.getString(
				CloudClient.ARGUMENT_LANGUAGE, ""));

		return true;
	}

	/**
	 * Convenience method that creates a bundle with the options currently
	 * defined.
	 * 
	 * @return a bundle with the options currently defined.
	 */
	public Bundle getOptions() {
		mDescription = mDescriptionView.getText().toString();
		Bundle bundle = new Bundle();
		bundle.putString(CloudClient.ARGUMENT_EXPORT_FORMAT, mExportFormat);
		bundle.putString(CloudClient.ARGUMENT_DESCRIPTION, mDescription);
		bundle.putString(CloudClient.ARGUMENT_LANGUAGE,
				mLanguageHelper.getLanguages());
		return bundle;
	}

	/**
	 * Upon creation of the fragments, the view variables are defined. If the
	 * view is not available (they return null) then we don't need to further
	 * process anything as the fragment is not being shown
	 */
	@Override
	boolean setViews() {
		mExportFormatView = (Spinner) getActivity().findViewById(
				R.id.option_export_format);

		if (mExportFormatView == null) {
			return false;
		}
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

		mExportFormatView.setAdapter(getExportFormatsAdapter());
		mExportFormatView.setOnItemSelectedListener(this);

		mManageLanguagesView.setOnClickListener(this);

		mFileViewHint.setOnClickListener(this);
		mFileView.setOnClickListener(this);

		prepareLanguageAutoComplete();
		return true;
	}

	/**
	 * Actions required to set the autocomplete text for the languages
	 * correctly. We create an adapter for the view, configure the looks of the
	 * adapter, set the text changed listener for autocompletion and finally add
	 * a cursor to string converter to convert the retrieved cursor into its
	 * corresponding languages
	 */
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
							ProcessBusinessCardOptionsFragment.this);
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

	/**
	 * TODO: DO IT!!!
	 */
	private void manageLanguages() {
		Toast.makeText(getActivity(), mLanguageHelper.getLanguages(),
				Toast.LENGTH_SHORT).show();
	}

	/**
	 * Mandatory override when an item on the export format spinner is selected
	 */
	@Override
	public void onItemSelected(AdapterView<?> adapterView, View v,
			int position, long id) {
		String[] exportFormats = getActivity().getResources().getStringArray(
				R.array.business_card_export_formats);

		mExportFormat = exportFormats[position];
	}

	/**
	 * Returns the SpinnerAdapter for the export formats spinner
	 * 
	 * @return SpinnerAdapter for the export formats.
	 */
	private SpinnerAdapter getExportFormatsAdapter() {
		ArrayAdapter<String> adapter = null;
		adapter = new ArrayAdapter<String>(getActivity(),
				android.R.layout.simple_spinner_item, getActivity()
						.getResources().getStringArray(
								R.array.business_card_export_formats));
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		return adapter;
	}

	/**
	 * Mandatory override to use the loaders interface. Creates the cursor for
	 * the languages to allow for easy autocompletion
	 */
	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		Uri uri = TasksContract.CONTENT_LANGUAGES;
		String selection = TasksContract.LanguagesTable.LANGUAGE + " LIKE ?";
		String[] selectionArgs = { "%" + mLanguage + "%" };
		Loader<Cursor> loader = new CursorLoader(getActivity(), uri, null,
				selection, selectionArgs, null);
		return loader;
	}

	/**
	 * Once the cursor has been loaded we only need to swap the adapter cursor
	 */
	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
		mAutoCompleteAdapter.swapCursor(cursor);
	}

	/**
	 * When the loader is being reset, we only empty the adapter, as it is no
	 * longer needed
	 */
	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		mAutoCompleteAdapter.swapCursor(null);
	}

	/**
	 * Mandatory override for the onclicklisteners. Handles the different on
	 * click options.
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.option_file_path:
		case R.id.option_file_path_hint:
			getPicture();
			break;
		case R.id.option_button_manage_language:
			manageLanguages();
			break;
		}
	}

	/**
	 * Method in charge of creating the way of getting a picture.
	 * 
	 * TODO: Add image capture from the camera.
	 */
	private void getPicture() {
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
		intent.setType("image/*");
		startActivityForResult(intent, ACTIVITY_GET_FILE);
		// Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		// mFileUri = FileManager
		// .getOutputMediaFileUri(FileManager.MEDIA_TYPE_IMAGE);
		// if (mFileUri == null) {
		// return;
		// }
		// intent.putExtra(MediaStore.EXTRA_OUTPUT, mFileUri);
		// startActivityForResult(intent, ACTIVITY_TAKE_PICTURE);
	}

	/**
	 * Convenience method for adding the taken picture to the gallery. It runs
	 * on a separate thread as we don't want to disturb the normal processing
	 * for this task.
	 */
	private void galleryAddPic() {
		new Thread(new GalleryAddThread(getActivity(), mFileUri.toString()))
				.start();
	}

	/**
	 * External thread for adding a taken picture to the gallery Its call
	 * requires the context in order to send later a broadcast with the intent.
	 */
	private class GalleryAddThread implements Runnable {
		Context mContext;
		String mFilePath;

		public GalleryAddThread(Context context, String filePath) {
			mContext = context;
			mFilePath = filePath;
		}

		@Override
		public void run() {
			Intent mediaScanIntent = new Intent(
					Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
			File f = new File(mFilePath);
			Uri contentUri = Uri.fromFile(f);
			mediaScanIntent.setData(contentUri);
			mContext.sendBroadcast(mediaScanIntent);
			return;
		}
	}

	/**
	 * Mandatory useless override
	 */
	@Override
	public void onNothingSelected(AdapterView<?> v) {
	}
}
