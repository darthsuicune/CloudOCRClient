package com.abbyy.cloudocr.optionsfragments;

import java.net.MalformedURLException;
import java.util.HashMap;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.abbyy.cloudocr.CloudClient;
import com.abbyy.cloudocr.ConnectionLoader;
import com.abbyy.cloudocr.R;
import com.abbyy.cloudocr.Task;

public abstract class ProcessOptionsFragment extends Fragment {
	private static final int LOADER_LAUNCH_TASK = 1;
	protected final String BASE_URL = "http://cloud.ocrsdk.com/";
	protected String mTaskId;
	CloudClient mClient;

	HashMap<String, String> mOptions;

	public abstract boolean saveDefaultOptions();
	public abstract boolean loadDefaultOptions();
	abstract void addLanguage(String language);
	abstract String createURL();
	abstract void setViews();
	
	

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		setHasOptionsMenu(true);
		setViews();
		loadDefaultOptions();
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.create_task, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_process:
			saveDefaultOptions();
			// launchTask();
			Toast.makeText(getActivity(), createURL(), Toast.LENGTH_LONG)
					.show();
			break;
		}
		return true;
	}

	public void launchTask(String filePath) {
		if(!filePath.equals("")){
			
		}
		getActivity().getSupportLoaderManager().restartLoader(
				LOADER_LAUNCH_TASK, null, new ConnectionHelper());
	}

	private void createTaskInBackground() {
		Intent intent = new Intent();
		getActivity().startService(intent);
	}

	public class ConnectionHelper implements LoaderCallbacks<Task> {
		@Override
		public Loader<Task> onCreateLoader(int id, Bundle args) {
			try {
//				switch (id) {
//				case LOADER_LAUNCH_TASK:
//					break;
//				}
				if(mClient == null){
					mClient = new CloudClient(getActivity(), createURL());
				}
				return new ConnectionLoader(getActivity(), mClient);
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		public void onLoadFinished(Loader<Task> loader,
				Task response) {
			createTaskInBackground();
			loader.abandon();
		}

		@Override
		public void onLoaderReset(Loader<Task> loader) {
		}
	}
}