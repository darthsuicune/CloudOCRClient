package com.abbyy.cloudocr.optionsfragments;

import java.io.InputStream;

import com.abbyy.cloudocr.AsyncInputStreamLoader;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;

public abstract class ProcessOptionsFragment extends Fragment {
	protected String mTaskId;
	private static final int LOADER_LAUNCH_TASK = 1;

	public abstract String createURL();
	public abstract Bundle createArgs();
	public abstract boolean setDefaultOptions(Bundle options);

	public void launchTask() {
		this.createURL();

		getActivity().getSupportLoaderManager().initLoader(LOADER_LAUNCH_TASK,
				createArgs(), new ConnectionHelper());
	}
	
	private class ConnectionHelper implements LoaderCallbacks<InputStream>{

		@Override
		public Loader<InputStream> onCreateLoader(int id, Bundle args) {
			Loader<InputStream> loader = null;
			switch(id){
			case LOADER_LAUNCH_TASK:
				loader = new AsyncInputStreamLoader(getActivity(), args);
				break;
			}
			return loader;
		}

		@Override
		public void onLoadFinished(Loader<InputStream> loader, InputStream result) {
			switch(loader.getId()){
			case LOADER_LAUNCH_TASK:
				parseData(result);
				break;
			}
			loader.abandon();
		}

		@Override
		public void onLoaderReset(Loader<InputStream> loader) {
			loader.reset();
		}
	}
	
	private void parseData(InputStream stream){
		
	}
}