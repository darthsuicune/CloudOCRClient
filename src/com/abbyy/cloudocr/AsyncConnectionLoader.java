package com.abbyy.cloudocr;

import java.net.MalformedURLException;
import java.net.URL;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.AsyncTaskLoader;

public class AsyncConnectionLoader extends AsyncTaskLoader<String> {
	public static final String ARGUMENT_URL = "url";
	public static final String ARGUMENT_FILE_PATH = "url";

	private URL mUrl;
	private String mFilePath;

	private static String test = "<response><task id=\"c3187247-7e81-4d12-8767-bc886c1ab878\" registrationTime=\"2012-02-16T06:42:09Z\" statusChangeTime=\"2012-02-16T06:42:09Z\"  status=\"Queued\" filesCount=\"1\"  credits=\"0\" estimatedProcessingTime=\"1\" description=\"Image.JPG to .pdf\" />    </response>";

	public AsyncConnectionLoader(Context context, Bundle args) {
		super(context);
		if (args != null) {
			try {
				mUrl = new URL(args.getString(ARGUMENT_URL));
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
			mFilePath = args.getString(ARGUMENT_FILE_PATH);
		}
	}

	@Override
	public String loadInBackground() {
		// return new CloudClient(mUrl, mFilePath).connect();
		return test;
	}

	/**
	 * The implementation of the AsyncTaskLoader for the support package has a
	 * bug which requires the forceLoad() on the StartLoading, as it will not
	 * start on its own. This allows for further control of the loadings.
	 */
	@Override
	protected void onStartLoading() {
		forceLoad();
	}
}
