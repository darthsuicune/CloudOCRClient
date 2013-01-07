package com.abbyy.cloudocr.ocr;

import android.os.Bundle;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;

public class ConnectionLoaderHelper implements LoaderCallbacks<String> {
	
	public final static int LOADER_SEND = 1;
	public final static int LOADER_RESPONSE = 2;
	public final static int LOADER_RESULT = 3;

	@Override
	public Loader<String> onCreateLoader(int id, Bundle args) {
		switch(id){
		case LOADER_SEND:
			break;
		case LOADER_RESPONSE:
			break;
		case LOADER_RESULT:
			break;
		}
		return null;
	}

	@Override
	public void onLoadFinished(Loader<String> loader, String result) {
		
	}

	@Override
	public void onLoaderReset(Loader<String> loader) {
		loader.reset();
	}

}
