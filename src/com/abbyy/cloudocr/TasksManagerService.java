package com.abbyy.cloudocr;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;

import org.xmlpull.v1.XmlPullParserException;

import android.app.Activity;
import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.abbyy.cloudocr.database.TasksContract;
import com.abbyy.cloudocr.utils.CloudClient;
import com.abbyy.cloudocr.utils.XMLParser;

public class TasksManagerService extends IntentService {
	private static final String SERVICE_NAME = "ABBYY Tasks Manager Service";
	public static final String NOTIFICATION_TAG = "cloudOCRnotification";
	public static final int NOTIFICATION_ID = 1;
	public static final int REQUEST_ACTIVE_TASKS = 1;

	public static final String EXTRA_ACTION = "action";
	public static final String EXTRA_CREATE = "create";
	public static final String EXTRA_FILE_PATH = "filePath";
	public static final String EXTRA_TASK_ID = "taskId";
	public static final String EXTRA_NEW_TASK_OPTIONS = "taskOptions";

	public static final int ACTION_DELETE_ACTIVE_TASK = 1;
	public static final int ACTION_DELETE_COMPLETED_TASK = 2;
	public static final int ACTION_UPDATE_ACTIVE_TASKS = 3;
	public static final int ACTION_UPDATE_COMPLETED_TASKS = 4;
	public static final int ACTION_CREATE_NEW_TASK = 5;

	public static final int CREATE_IMAGE = 1;
	public static final int CREATE_BUSINESS_CARD = 2;

	private CloudClient mClient;
	private String mURL;
	private Bundle mArgs;

	public TasksManagerService() {
		super(SERVICE_NAME);
		mClient = new CloudClient(this);
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		Bundle extras = intent.getExtras();
		if (extras != null) {
			mArgs = intent.getExtras();
			boolean needsConnection = false;
			switch (extras.getInt(EXTRA_ACTION)) {
			case ACTION_CREATE_NEW_TASK:
				needsConnection = createNewTask(extras.getInt(EXTRA_CREATE));
				mArgs.putAll(extras.getBundle(EXTRA_NEW_TASK_OPTIONS));
				break;
			case ACTION_DELETE_ACTIVE_TASK:
				needsConnection = deleteTask(extras.getString(EXTRA_TASK_ID),
						true);
				break;
			case ACTION_DELETE_COMPLETED_TASK:
				needsConnection = deleteTask(extras.getString(EXTRA_TASK_ID),
						true);
				break;
			case ACTION_UPDATE_ACTIVE_TASKS:
				needsConnection = refreshTasks(true);
				break;
			case ACTION_UPDATE_COMPLETED_TASKS:
				needsConnection = refreshTasks(false);
				break;
			}
			if (mArgs.containsKey(EXTRA_FILE_PATH)) {
				mClient.setFilePath(mArgs.getString(EXTRA_FILE_PATH));
				Log.d("Image", "Processing " + mClient.mFilePath);
			}
			if (needsConnection) {
				try {
					mClient.setUrl(mURL, mArgs);
					parseResponse(mClient.makePetition());
				} catch (MalformedURLException e) {
					e.printStackTrace();
				}
			}
		} else {
		}
	}

	private boolean createNewTask(int create) {
		switch (create) {
		case R.string.process_business_card:
			mURL = CloudClient.PROCESS_BUSINESS_CARD;
			break;
		case R.string.process_image:
			mURL = CloudClient.PROCESS_IMAGE;
			break;
		}
		return true;
	}

	private boolean refreshTasks(boolean isActive) {
		if (isActive) {
			mURL = CloudClient.GET_TASK_LIST;
		} else {
			mURL = CloudClient.GET_FINISHED_TASK_LIST;
		}
		return true;
	}

	private boolean deleteTask(String taskId, boolean isActive) {
		if (isActive) {
			mArgs.putString(CloudClient.ARGUMENT_TASK_ID, taskId);
			mURL = CloudClient.DELETE_TASK;
			return true;
		} else {
			String where = TasksContract.TasksTable.TASK_ID + "=?";
			String[] selectionArgs = { taskId };
			getContentResolver().delete(TasksContract.CONTENT_TASKS, where,
					selectionArgs);

		}
		return false;
	}

	private void parseResponse(String response) {
		Log.d("Response", response);
		if (response == null) {
			return;
		}
		XMLParser parser = new XMLParser(response);
		ArrayList<HashMap<String, String>> result = null;
		ArrayList<HashMap<String, String>> error = null;
		try {
			result = parser.parseData(getString(R.string.tag_response),
					getString(R.string.tag_task));
		} catch (XmlPullParserException e) {
			try {
				error = parser.parseData(getString(R.string.tag_error),
						getString(R.string.tag_message));
			} catch (XmlPullParserException e1) {
				result = null;
				e1.printStackTrace();
			} catch (IOException e1) {
				result = null;
				e1.printStackTrace();
			}
		} catch (IOException e) {
			result = null;
			e.printStackTrace();
		}
		if (result != null) {
			for (int i = 0; i < result.size(); i++) {
				new Task(getBaseContext(), result.get(i)).writeTaskToDb();
				updateNotificationStatus();
			}
		} else if (error != null) {
			Log.d("ERROR", "Error upon download: " + error);
			// Parse error message and do something.
		}
	}

	protected void updateNotificationStatus() {

		NotificationManager nm = (NotificationManager) getSystemService(Activity.NOTIFICATION_SERVICE);
		NotificationCompat.Builder builder = new NotificationCompat.Builder(
				this);

		Intent intent = new Intent(this, MainActivity.class);
		PendingIntent pendingIntent = PendingIntent.getActivity(this,
				REQUEST_ACTIVE_TASKS, intent, Intent.FLAG_ACTIVITY_NEW_TASK);

		builder.setSmallIcon(R.drawable.abbyy_logo)
				.setContentTitle(getString(R.string.notification_title))
				.setContentText(getString(R.string.notification_message))
				.setContentIntent(pendingIntent).setAutoCancel(false)
				.setOngoing(true);

		nm.notify(NOTIFICATION_TAG, NOTIFICATION_ID, builder.build());
	}
}