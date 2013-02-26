package com.abbyy.cloudocr.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import android.os.Bundle;
import android.util.Log;

public class CloudClient {
	private static final String BASE_URL = "http://cloud.ocrsdk.com/";

	public static final String GET_TASK_LIST = "listTasks";
	public static final String GET_FINISHED_TASK_LIST = "listFinishedTasks";
	public static final String PROCESS_IMAGE = "processImage";
	public static final String PROCESS_SUBMIT_IMAGE = "submitImage";
	public static final String PROCESS_DOCUMENT = "processDocument";
	public static final String PROCESS_BUSINESS_CARD = "processBusinessCard";
	public static final String PROCESS_TEXT_FIELD = "processTextField";
	public static final String PROCESS_BARCODE_FIELD = "processBarcodeField";
	public static final String PROCESS_CHECKMARK_FIELD = "processCheckmarkField";
	public static final String PROCESS_FIELDS = "processFields";
	public static final String GET_TASK_STATUS = "getTaskStatus";
	public static final String DELETE_TASK = "deleteTask";

	public static final String ARGUMENT_LANGUAGE = "language";
	public static final String ARGUMENT_PROFILE = "profile";
	public static final String ARGUMENT_TEXT_TYPE = "textType";
	public static final String ARGUMENT_IMAGE_SOURCE = "imageSource";
	public static final String ARGUMENT_CORRECT_ORIENTATION = "correctOrientation";
	public static final String ARGUMENT_CORRECT_SKEW = "correctSkew";
	public static final String ARGUMENT_EXPORT_FORMAT = "exportFormat";
	public static final String ARGUMENT_XML_WRITE_RECOGNITION_VARIANTS = "xml:writeRecognitionVariants";
	public static final String ARGUMENT_DESCRIPTION = "description";
	public static final String ARGUMENT_PDF_PASSWORD = "pdfPassword";
	public static final String ARGUMENT_TASK_ID = "taskId";
	public static final String ARGUMENT_REGION = "region";
	public static final String ARGUMENT_LETTER_SET = "letterSet";
	public static final String ARGUMENT_REG_EXP = "regExp";
	public static final String ARGUMENT_ONE_TEXT_LINE = "oneTextLine";
	public static final String ARGUMENT_ONE_WORD_PER_TEXT_LINE = "oneWordPerTextLine";
	public static final String ARGUMENT_MARKING_TYPE = "markingType";
	public static final String ARGUMENT_PLACEHOLDERS_COUNT = "placeHoldersCount";
	public static final String ARGUMENT_WRITING_STYLE = "writingStyle";
	public static final String ARGUMENT_BARCODE_TYPE = "barcodeType";
	public static final String ARGUMENT_CONTAINS_BINARY_DATA = "containsBinaryData";
	public static final String ARGUMENT_CHECKMARK_TYPE = "checkmarkType";
	public static final String ARGUMENT_CORRECTION_ALLOWED = "correctionAllowed";

	private URL mUrl;
	private String mFilePath;
	private String mProcessType;

	public CloudClient() {
	}

	public void setUrl(String processType, Bundle args)
			throws MalformedURLException {
		mProcessType = processType;
		String url = BASE_URL + processType;
		if (args != null) {
			url = url.concat(createArgs(args));
		}
		mUrl = new URL(url);
	}

	public void setFilePath(String filePath) {
		mFilePath = filePath;
	}

	public String getFilePath() {
		return mFilePath;
	}

	public String makePetition() {
		String test = "<response><task id=\"c3187247-7e81-4d12-8767-bc886c1ab878\""
				+ " registrationTime=\"2012-02-16T06:42:09Z\" statusChangeTime=\"2012-02-16T06:42:09Z\""
				+ " status=\"Queued\" filesCount=\"1\"  credits=\"0\" estimatedProcessingTime=\"1\""
				+ " description=\"Image.JPG to .pdf\" />    </response>";
		return test;
//		if (mUrl == null) {
//			return null;
//		}
//
//		DefaultHttpClient httpClient = createHttpClient();
//
//		try {
//			HttpUriRequest request;
//
//			if (isGet()) {
//				request = new HttpGet(mUrl.toExternalForm());
//			} else {
//				HttpPost postRequest = new HttpPost(mUrl.toExternalForm());
//				if (mFilePath != null) {
//					addFile(postRequest);
//				}
//				request = postRequest;
//			}
//
//			HttpResponse response = httpClient.execute(request);
//			return new BufferedReader(new InputStreamReader(response
//					.getEntity().getContent())).readLine();
//		} catch (ClientProtocolException e) {
//			logError(e.getLocalizedMessage());
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		return null;
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

	private void logError(String message) {
		Log.d("Connection error", message);
	}

	private boolean isGet() {
		return mProcessType.equals("processDocument")
				|| mProcessType.equals("getTaskStatus")
				|| mProcessType.equals("deleteTask")
				|| mProcessType.equals("listTasks")
				|| mProcessType.equals("listFinishedTasks");
	}

	private String createArgs(Bundle args) {
		String result = "";
		if (args.containsKey(ARGUMENT_DESCRIPTION)) {
			result = result.concat(ARGUMENT_DESCRIPTION + "="
					+ args.getString(ARGUMENT_DESCRIPTION) + "&");
		}
		if (args.containsKey(ARGUMENT_WRITING_STYLE)) {
			result = result.concat(ARGUMENT_WRITING_STYLE + "="
					+ args.getString(ARGUMENT_WRITING_STYLE) + "&");
		}
		if (args.containsKey(ARGUMENT_BARCODE_TYPE)) {
			result = result.concat(ARGUMENT_BARCODE_TYPE + "="
					+ args.getString(ARGUMENT_BARCODE_TYPE) + "&");
		}
		if (args.containsKey(ARGUMENT_ONE_TEXT_LINE)) {
			result = result.concat(ARGUMENT_ONE_TEXT_LINE + "="
					+ args.getString(ARGUMENT_ONE_TEXT_LINE) + "&");
		}
		if (args.containsKey(ARGUMENT_CORRECTION_ALLOWED)) {
			result = result.concat(ARGUMENT_CORRECTION_ALLOWED + "="
					+ args.getString(ARGUMENT_CORRECTION_ALLOWED) + "&");
		}
		if (args.containsKey(ARGUMENT_LETTER_SET)) {
			result = result.concat(ARGUMENT_LETTER_SET + "="
					+ args.getString(ARGUMENT_LETTER_SET) + "&");
		}

		if (args.containsKey(ARGUMENT_ONE_WORD_PER_TEXT_LINE)) {
			result = result.concat(ARGUMENT_ONE_WORD_PER_TEXT_LINE + "="
					+ args.getString(ARGUMENT_ONE_WORD_PER_TEXT_LINE) + "&");
		}

		if (args.containsKey(ARGUMENT_MARKING_TYPE)) {
			result = result.concat(ARGUMENT_MARKING_TYPE + "="
					+ args.getString(ARGUMENT_MARKING_TYPE) + "&");
		}

		if (args.containsKey(ARGUMENT_PLACEHOLDERS_COUNT)) {
			result = result.concat(ARGUMENT_PLACEHOLDERS_COUNT + "="
					+ args.getString(ARGUMENT_PLACEHOLDERS_COUNT) + "&");
		}

		if (args.containsKey(ARGUMENT_EXPORT_FORMAT)) {
			result = result.concat(ARGUMENT_EXPORT_FORMAT + "="
					+ args.getString(ARGUMENT_EXPORT_FORMAT) + "&");
		}

		if (args.containsKey(ARGUMENT_IMAGE_SOURCE)) {
			result = result.concat(ARGUMENT_IMAGE_SOURCE + "="
					+ args.getString(ARGUMENT_IMAGE_SOURCE) + "&");
		}

		if (args.containsKey(ARGUMENT_TASK_ID)) {
			result = result.concat(ARGUMENT_TASK_ID + "="
					+ args.getString(ARGUMENT_TASK_ID) + "&");
		}

		if (args.containsKey(ARGUMENT_TEXT_TYPE)) {
			result = result.concat(ARGUMENT_TEXT_TYPE + "="
					+ args.getString(ARGUMENT_TEXT_TYPE) + "&");
		}

		if (args.containsKey(ARGUMENT_REG_EXP)) {
			result = result.concat(ARGUMENT_REG_EXP + "="
					+ args.getString(ARGUMENT_REG_EXP) + "&");
		}

		if (args.containsKey(ARGUMENT_REGION)) {
			result = result.concat(ARGUMENT_REGION + "="
					+ args.getString(ARGUMENT_REGION) + "&");
		}

		if (args.containsKey(ARGUMENT_CHECKMARK_TYPE)) {
			result = result.concat(ARGUMENT_CHECKMARK_TYPE + "="
					+ args.getString(ARGUMENT_CHECKMARK_TYPE) + "&");
		}

		if (args.containsKey(ARGUMENT_CONTAINS_BINARY_DATA)) {
			result = result.concat(ARGUMENT_CONTAINS_BINARY_DATA + "="
					+ args.getString(ARGUMENT_CONTAINS_BINARY_DATA) + "&");
		}

		if (args.containsKey(ARGUMENT_CORRECT_ORIENTATION)) {
			result = result.concat(ARGUMENT_CORRECT_ORIENTATION + "="
					+ args.getString(ARGUMENT_CORRECT_ORIENTATION) + "&");
		}

		if (args.containsKey(ARGUMENT_CORRECT_SKEW)) {
			result = result.concat(ARGUMENT_CORRECT_SKEW + "="
					+ args.getString(ARGUMENT_CORRECT_SKEW) + "&");
		}

		if (args.containsKey(ARGUMENT_XML_WRITE_RECOGNITION_VARIANTS)) {
			result = result.concat(ARGUMENT_XML_WRITE_RECOGNITION_VARIANTS
					+ "="
					+ args.getString(ARGUMENT_XML_WRITE_RECOGNITION_VARIANTS)
					+ "&");
		}

		if (args.containsKey(ARGUMENT_PDF_PASSWORD)) {
			result = result.concat(ARGUMENT_PDF_PASSWORD + "="
					+ args.getString(ARGUMENT_PDF_PASSWORD) + "&");
		}

		if (args.containsKey(ARGUMENT_PROFILE)) {
			result = result.concat(ARGUMENT_PROFILE + "="
					+ args.getString(ARGUMENT_PROFILE) + "&");
		}

		if (args.containsKey(ARGUMENT_LANGUAGE)) {
			result = result.concat(ARGUMENT_LANGUAGE + "="
					+ args.getString(ARGUMENT_LANGUAGE));
		}
		return result;
	}
}
