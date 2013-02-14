package com.abbyy.cloudocr;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
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
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.content.Context;
import android.util.Xml;

public class CloudClient {
	private URL mUrl;
	private String mFilePath;
	private Context mContext;

	public CloudClient(Context context, String url) throws MalformedURLException {
		mContext = context;
		mUrl = new URL(url);
	}
	public CloudClient(Context context, String url, String filePath) throws MalformedURLException{
		mContext = context;
		mUrl = new URL(url);
		mFilePath = filePath;
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
				HttpPost postRequest = new HttpPost(mUrl.toExternalForm());
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

	/**
	 * This method will load the parser
	 * 
	 * @param parser
	 *            XmlPullParser
	 * @throws XmlPullParserException
	 * @throws IOException
	 */
	public void parseResponse(String response)
			throws XmlPullParserException, IOException {
		XmlPullParser parser = Xml.newPullParser();
		parser.setInput(new ByteArrayInputStream(response.getBytes()), null);
		readData(parser);
	}

	/**
	 * This method is in charge of parsing the XML data received.
	 * 
	 * @param parser
	 *            XmlPullParser
	 * @throws XmlPullParserException
	 * @throws IOException
	 */
	private void readData(XmlPullParser parser)
			throws XmlPullParserException, IOException {
		parser.nextTag();
		parser.require(XmlPullParser.START_TAG, null,
				mContext.getString(R.string.tag_response));
		while (parser.next() != XmlPullParser.END_TAG) {
			if (parser.getEventType() != XmlPullParser.START_TAG) {
				continue;
			}
			String name = parser.getName();
			if (name.equals(mContext.getString(R.string.tag_task))) {
				String taskId = parser.getAttributeValue(null, mContext.getString(R.string.field_id));
				String status = parser.getAttributeValue(null, mContext.getString(R.string.field_status));
				String registrationTime = parser.getAttributeValue(null, mContext.getString(R.string.field_registration_time));
				String statusChangeTime = parser.getAttributeValue(null, mContext.getString(R.string.field_status_change_time));
				String filesCount = parser.getAttributeValue(null, mContext.getString(R.string.field_files_count));
				String credits = parser.getAttributeValue(null, mContext.getString(R.string.field_credits));
				String estimatedProcessingTime = parser.getAttributeValue(null, mContext.getString(R.string.field_estimated_processing_time));
				String description = parser.getAttributeValue(null, mContext.getString(R.string.field_description));
				String resultUrl = parser.getAttributeValue(null, mContext.getString(R.string.field_result_url));
				String error = parser.getAttributeValue(null, mContext.getString(R.string.field_error));
				
				Task task = new Task(
						taskId,
						status,
						registrationTime,
						statusChangeTime,
						Integer.parseInt(filesCount),
						Integer.parseInt(credits),
						Integer.parseInt(estimatedProcessingTime),
						description,
						resultUrl,
						error,
						false);
				task.writeTaskToDb(mContext);
			} else if (name.equals(mContext.getString(R.string.tag_error))) {
			}
		}
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
