package com.abbyy.cloudocr;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

public class AsyncStringLoader extends AsyncTaskLoader<String> {
	
	private String mUrl;

	private static String response = "<response><task id=\"c3187247-7e81-4d12-8767-bc886c1ab878\" registrationTime=\"2012-02-16T06:42:09Z\" statusChangeTime=\"2012-02-16T06:42:09Z\"  status=\"Queued\" filesCount=\"1\"  credits=\"0\" estimatedProcessingTime=\"1\" description=\"Image.JPG to .pdf\" />    </response>";
	
	public AsyncStringLoader(Context context, String url) {
		super(context);
		mUrl = url;
	}

	@Override
	public String loadInBackground() {

		return response;
	}

}
