package com.abbyy.cloudocr.optionsfragments;

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

import com.abbyy.cloudocr.AsyncConnectionLoader;
import com.abbyy.cloudocr.R;

public abstract class ProcessOptionsFragment extends Fragment {
	protected final String BASE_URL = "http://cloud.ocrsdk.com/";
	protected String mTaskId;
	private static final int LOADER_LAUNCH_TASK = 1;
	
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
		switch(item.getItemId()){
		case R.id.menu_process:
			saveDefaultOptions();
//			launchTask();
			Toast.makeText(getActivity(), createURL(), Toast.LENGTH_LONG).show();
			break;
		}
		return true;
	}
	
	public Bundle createArgs() {
		Bundle args = new Bundle();
		args.putString(AsyncConnectionLoader.ARGUMENT_URL, createURL());
		return args;
	}

	public void launchTask() {
		getActivity().getSupportLoaderManager().initLoader(LOADER_LAUNCH_TASK,
				createArgs(), new ConnectionHelper());
	}
	
	private class ConnectionHelper implements LoaderCallbacks<String>{

		@Override
		public Loader<String> onCreateLoader(int id, Bundle args) {
			Loader<String> loader = null;
			switch(id){
			case LOADER_LAUNCH_TASK:
				loader = new AsyncConnectionLoader(getActivity(), args);
				break;
			}
			return loader;
		}

		@Override
		public void onLoadFinished(Loader<String> loader, String result) {
			switch(loader.getId()){
			case LOADER_LAUNCH_TASK:
				if(result != null){
					parseData(result);
				} else {
					Toast.makeText(getActivity(), R.string.error_uploading, Toast.LENGTH_LONG).show();
				}
				break;
			}
			loader.abandon();
		}

		@Override
		public void onLoaderReset(Loader<String> loader) {
			loader.reset();
		}
	}
	
	private void parseData(String data){
		parseResponse();
		createTaskInBackground();
	}
	
	private void parseResponse(){
		
	}
	
	private void createTaskInBackground(){
		Intent intent = new Intent();
		getActivity().startService(intent);
	}
}