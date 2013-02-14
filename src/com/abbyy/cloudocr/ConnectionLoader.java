package com.abbyy.cloudocr;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParserException;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

public class ConnectionLoader extends AsyncTaskLoader<Task> {

	private ArrayList<Task> mTasksList = null;

	private CloudClient mClient;

	public ConnectionLoader(Context context, CloudClient client)
			throws MalformedURLException {
		super(context);
		mClient = client;
	}

	/**
	 * The implementation of the AsyncTaskLoader for the support package has a
	 * bug which requires the forceLoad() on the StartLoading, as it will not
	 * start on its own. This allows for further control of the loadings.
	 */
	@Override
	protected void onStartLoading() {
		if(mTasksList == null){
			forceLoad();
		}
	}

	@Override
	public Task loadInBackground() {

		try {
			// mClient.parseResponse(mClient.connect());
			mClient
					.parseResponse("<response><task id=\"c3387247-7e81-4d12-8767-bc886c1ab878\""
							+ " registrationTime=\"2012-02-16T06:42:09Z\" statusChangeTime=\"2012-02-16T06:42:09Z\""
							+ " status=\"Queued\" filesCount=\"1\"  credits=\"0\" estimatedProcessingTime=\"1\""
							+ " description=\"Image.JPG to .pdf\" />    </response>");
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
