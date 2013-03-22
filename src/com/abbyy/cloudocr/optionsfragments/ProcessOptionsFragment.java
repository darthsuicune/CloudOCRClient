package com.abbyy.cloudocr.optionsfragments;

import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.abbyy.cloudocr.R;

/**
 * Basic stub with the basic implementation and required methods for the options
 * fragment for each processing type
 * 
 * @author Denis Lapuente
 * 
 */
public abstract class ProcessOptionsFragment extends Fragment {
	public static final String ARG_FILE_PATH = "filePath";

	String mTaskId;
	SharedPreferences prefs;

	/**
	 * This method won't be used by every subfragment, but will be used by the
	 * calling activity if the subfragment is of the required type. In case the
	 * child doesn't use it is safely avoidable
	 * 
	 * @param uri
	 */
	public void addFile(Uri uri) {
	}

	/**
	 * Methods required to be implemented by the children.
	 */
	public abstract boolean saveDefaultOptions();

	public abstract boolean loadDefaultOptions();

	abstract boolean setViews();

	abstract void launchTask();

	/**
	 * Basic implementation that every fragment must do. They are allowed to
	 * perform their own actions too.
	 */
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		// If the views are correctly set (that is, the fragment is on screen),
		// we activate their menu actions. If not, we deactivate them
		if (setViews()) {
			setHasOptionsMenu(true);
			loadDefaultOptions();
		} else {
			setHasOptionsMenu(false);
		}
	}

	/**
	 * Basic menu options for all the fragments.
	 */
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.create_task, menu);
	}

	/**
	 * Basic processing for all the fragments.
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_process:
			launchTask();
			return true;
		default:
			return super.onOptionsItemSelected(item);

		}
	}
}