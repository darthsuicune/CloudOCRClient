package com.abbyy.cloudocr.optionsfragments;

import java.net.MalformedURLException;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.abbyy.cloudocr.ConnectionLoader;
import com.abbyy.cloudocr.R;
import com.abbyy.cloudocr.utils.CloudClient;

public abstract class ProcessOptionsFragment extends Fragment implements LoaderCallbacks<Void> {
	protected String mTaskId;
	CloudClient mClient;
	
	SharedPreferences prefs;

	public abstract void addFile(String filePath);
	public abstract boolean saveDefaultOptions();
	public abstract boolean loadDefaultOptions();
	abstract void setViews();
	abstract void launchTask();

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		mClient = new CloudClient();
		setHasOptionsMenu(true);
		setViews();
		loadDefaultOptions();
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.create_task, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_process:
			launchTask();
			break;
		}
		return true;
	}

	private void createTaskInBackground() {
		Intent intent = new Intent();
		getActivity().startService(intent);
	}

		@Override
		public Loader<Void> onCreateLoader(int id, Bundle args) {
			try {
				return new ConnectionLoader(getActivity(), mClient);
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		public void onLoadFinished(Loader<Void> loader,
				Void response) {
			createTaskInBackground();
		}

		@Override
		public void onLoaderReset(Loader<Void> loader) {
		}
}