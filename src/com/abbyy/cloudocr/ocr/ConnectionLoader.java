package com.abbyy.cloudocr.ocr;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

public class ConnectionLoader extends AsyncTaskLoader<String> {
	private final String SERVER = "ocrsdk.com";
	private final String PROTOCOL = "http";
	private final int PORT = 80;
	private OCRConfig mConfig;
	private String mPath;
	

	public ConnectionLoader(Context context, OCRConfig config, String path) {
		super(context);
		mConfig = config;
		mPath = path;
	}

	@Override
	public String loadInBackground() {
		DefaultHttpClient httpClient = new DefaultHttpClient();
		CredentialsProvider credentials = httpClient.getCredentialsProvider();
		credentials.setCredentials(AuthScope.ANY,
				new UsernamePasswordCredentials(mConfig.getAppId(), mConfig.getAppPwd()));
		httpClient.setCredentialsProvider(credentials);
		HttpPost request = new HttpPost(mConfig.getURL().toExternalForm());
		try {
			InputStreamEntity requestEntity = new InputStreamEntity(new FileInputStream(new File(mPath)), -1);
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		
		try {
			httpClient.execute(request);
		} catch (ClientProtocolException e) {
			Log.d("ERROR", e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			Log.d("ERROR", e.getMessage());
			e.printStackTrace();
		}
		
		return null;
	}

	@Override
	protected void onForceLoad() {
		forceLoad();
		super.onForceLoad();
	}
}
