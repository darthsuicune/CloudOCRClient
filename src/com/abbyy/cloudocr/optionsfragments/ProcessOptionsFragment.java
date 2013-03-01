package com.abbyy.cloudocr.optionsfragments;

import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.abbyy.cloudocr.R;
import com.abbyy.cloudocr.utils.CloudClient;

public abstract class ProcessOptionsFragment extends Fragment {

	protected String mTaskId;
	CloudClient mClient;
	SharedPreferences prefs;

	public void addFile(Uri uri){
		//Stub for those who need to use it
	}
	public abstract boolean saveDefaultOptions();

	public abstract boolean loadDefaultOptions();

	abstract boolean setViews();

	abstract void launchTask();

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		mClient = new CloudClient();
		if(setViews()){
			setHasOptionsMenu(true);
			loadDefaultOptions();
		} else {
			setHasOptionsMenu(false);
		}
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
			return true;
		default:
			return super.onOptionsItemSelected(item);

		}
	}
}