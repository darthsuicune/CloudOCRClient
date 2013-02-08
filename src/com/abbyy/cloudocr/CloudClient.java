package com.abbyy.cloudocr;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

public class CloudClient {
	private URL mUrl;
	private String mFilePath;

	public CloudClient(URL url, String filePath) {
		mUrl = url;
		mFilePath = filePath;
	}

	public void setURL(URL url) {
		mUrl = url;
	}

	public void setFilePath(String filePath) {
		mFilePath = filePath;
	}

	public URL getURL() {
		return mUrl;
	}

	public String getFilePath() {
		return mFilePath;
	}

	public String connect() {
		if (mUrl == null) {
			return null;
		}
		
		DefaultHttpClient httpClient = createHttpClient();

		try {
			HttpUriRequest request;

			if (isGet()) {
				request = new HttpGet(mUrl.toExternalForm());
			} else {
				HttpPost postRequest =new HttpPost(mUrl.toExternalForm()); 
				if (mFilePath != null) {
					addFile(postRequest);
				}
				request = postRequest;
			}

			HttpResponse response = httpClient.execute(request);

			BufferedReader reader = new BufferedReader(new InputStreamReader(
					response.getEntity().getContent()));
			return reader.readLine();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	private void addFile(HttpPost request) throws FileNotFoundException,
			IOException {
		InputStreamEntity entity = null;
		entity = new InputStreamEntity(
				new FileInputStream(new File(mFilePath)), -1);
		entity.setContentType("application/octet-stream");
		entity.setChunked(true);
		BufferedHttpEntity bufferedEntity = new BufferedHttpEntity(entity);
		request.setEntity(bufferedEntity);
	}

	private DefaultHttpClient createHttpClient() {
		DefaultHttpClient client = new DefaultHttpClient(setHttpParams());
		setupCredentials(client);
		return client;
	}

	private HttpParams setHttpParams() {
		HttpParams httpParams = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParams, 10000);
		HttpConnectionParams.setSoTimeout(httpParams, 15000);
		return httpParams;
	}

	private void setupCredentials(DefaultHttpClient client) {
		String appId = "BasicAndroidCloudOCRClient";
		String password = "5QQQ0U/Wx+mnkIT51ZLiHREF";

		CredentialsProvider credentials = client.getCredentialsProvider();
		credentials.setCredentials(AuthScope.ANY,
				new UsernamePasswordCredentials(appId, password));
		client.setCredentialsProvider(credentials);
	}

	private boolean isGet() {
		return mUrl.getPath().contains("processDocument")
				|| mUrl.getPath().contains("getTaskStatus")
				|| mUrl.getPath().contains("deleteTask")
				|| mUrl.getPath().contains("listTasks")
				|| mUrl.getPath().contains("listFinishedTasks");
	}
}
