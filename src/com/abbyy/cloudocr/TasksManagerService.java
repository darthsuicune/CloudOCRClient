package com.abbyy.cloudocr;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;

import org.xmlpull.v1.XmlPullParserException;

import android.app.Activity;
import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.abbyy.cloudocr.database.TasksContract;
import com.abbyy.cloudocr.utils.CloudClient;
import com.abbyy.cloudocr.utils.FileManager;
import com.abbyy.cloudocr.utils.XMLParser;

/**
 * This service is in charge of binding every part of the application.
 * 
 * It manages the orders from the activities to the server. It parses the server
 * responses and inserts the new data into the database. It manages the
 * downloads of the results. It manages the notifications shown to the user.
 * 
 * EXTRA_ACTION: String with the kind of action we are performing. Available
 * actions start with ACTION_
 * 
 * EXTRA_CREATE: Int with the R.string.process* relative process type
 * 
 * EXTRA_FILE_PATH: String containing the file path to the origin when uploading
 * a file, as well as the file path for downloading.
 * 
 * EXTRA_TASK_ID: String containing the task id when needed.
 * 
 * EXTRA_NEW_TASK_OPTIONS: Bundle with the options to create a new task.
 * 
 * EXTRA_URL: String with the URL where the file should be downloaded
 * 
 * EXTRA_EXPORT_FORMAT: String with the export format.
 * 
 * @author Denis Lapuente
 * 
 */
public class TasksManagerService extends IntentService {
	// Service name.
	public static final String SERVICE_NAME = "ABBYY Tasks Manager Service";
	// Notification constants
	public static final String NOTIFICATION_TAG = "cloudOCRnotification";
	public static final int TASKS_NOTIFICATION = 1;
	public static final int DOWNLOAD_NOTIFICATION = 1;

	// Constant for the notification intent.
	public static final int REQUEST_ACTIVE_TASKS = 1;
	public static final int REQUEST_SHOW_RESULT = 2;

	// EXTRA constants.
	public static final String EXTRA_ACTION = "action";
	public static final String EXTRA_CREATE = "create";
	public static final String EXTRA_FILE_PATH = "filePath";
	public static final String EXTRA_TASK_ID = "taskId";
	public static final String EXTRA_NEW_TASK_OPTIONS = "taskOptions";
	public static final String EXTRA_URL = "url";
	public static final String EXTRA_EXPORT_FORMAT = "exportFormat";

	// Values for the EXTRA_ACTION extra.
	public static final int ACTION_DELETE_ACTIVE_TASK = 1;
	public static final int ACTION_DELETE_COMPLETED_TASK = 2;
	public static final int ACTION_UPDATE_ACTIVE_TASKS = 3;
	public static final int ACTION_UPDATE_COMPLETED_TASKS = 4;
	public static final int ACTION_CREATE_NEW_TASK = 5;
	public static final int ACTION_DOWNLOAD_RESULT = 6;

	private CloudClient mClient;
	private String mURL;
	private Bundle mArgs;

	/**
	 * Required constructor with service name.
	 */
	public TasksManagerService() {
		super(SERVICE_NAME);
		mClient = new CloudClient(this);
	}

	/**
	 * Required override. Handles each new call to the service. If the service
	 * is already running, it just queues the new intent. If it is not running,
	 * starts it up.
	 * 
	 * It is in charge of handling the type of action the intent is supposed to
	 * perform.
	 */
	@Override
	protected void onHandleIntent(Intent intent) {
		Bundle extras = intent.getExtras();
		// Parse the action and create the corresponding parameters in each
		// corresponding method
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
			case ACTION_DOWNLOAD_RESULT:
				downloadResult();
				return;
			}

			// Add the file when needed
			if (mArgs.containsKey(EXTRA_FILE_PATH)) {
				mClient.setFilePath(mArgs.getString(EXTRA_FILE_PATH));
			}
			// Connect (and parse the response) when needed
			if (needsConnection) {
				try {
					mClient.setUrl(mURL, mArgs);
					parseResponse(mClient.makePetition());
				} catch (MalformedURLException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * Convenience method for creating a new task. It sets the URL to the
	 * related URL
	 * 
	 * @param create
	 * @return
	 */
	private boolean createNewTask(int create) {
		switch (create) {
		case R.string.process_business_card:
			mURL = CloudClient.ACTION_PROCESS_BUSINESS_CARD;
			return true;
		case R.string.process_image:
			mURL = CloudClient.ACTION_PROCESS_IMAGE;
			return true;
		default:
			return false;
		}
	}

	/**
	 * Convenience method for refreshing the tasks lists. It sets the URL to the
	 * correct URL
	 * 
	 * @param isActive
	 * @return
	 */
	private boolean refreshTasks(boolean isActive) {
		if (isActive) {
			mURL = CloudClient.ACTION_GET_TASK_LIST;
		} else {
			mURL = CloudClient.ACTION_GET_FINISHED_TASK_LIST;
		}
		return true;
	}

	/**
	 * Convenience method for deleting tasks.
	 * 
	 * TODO: Not working properly. Needs rethinking and redoing.
	 * 
	 * @param taskId
	 * @param isActive
	 * @return
	 */
	private boolean deleteTask(String taskId, boolean isActive) {
		if (isActive) {
			mArgs.putString(CloudClient.ARGUMENT_TASK_ID, taskId);
			mURL = CloudClient.ACTION_DELETE_TASK;
			return true;
		} else {
			String where = TasksContract.TasksTable.TASK_ID + "=?";
			String[] selectionArgs = { taskId };
			getContentResolver().delete(TasksContract.CONTENT_TASKS, where,
					selectionArgs);
			return false;
		}
	}

	/**
	 * Convenience method for downloading the results. Shows a notification that
	 * sticks and does nothing and then changes it to an actual working
	 * notification that opens the file.
	 */
	private void downloadResult() {
		try {
			mClient.setDownloadUrl(mArgs.getString(EXTRA_URL));
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return;
		}

		File exportFile = FileManager.getOutputDownloadFile(
				mArgs.getString(EXTRA_FILE_PATH),
				mArgs.getString(EXTRA_EXPORT_FORMAT));
		if (exportFile != null) {
			showDownloadNotification(true, Uri.fromFile(exportFile));
			InputStream inputStream = mClient.makePetition();
			FileManager.exportFile(inputStream, exportFile);
			showDownloadNotification(false, Uri.fromFile(exportFile));
		}
	}

	/**
	 * Convenience method for parsing a response from the server. On a correct
	 * download, it will create the tasks sent in the response and add them to
	 * the database, or update them when they were already created.
	 * 
	 * On an error, it will parse again the error to show some information
	 * 
	 * TODO: Manage error
	 * 
	 * @param response
	 *            The inputStream from the connection for parsing.
	 */
	private void parseResponse(InputStream response) {
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
				Task task = new Task(getBaseContext(), result.get(i),
						mArgs.getString(EXTRA_FILE_PATH),
						mArgs.getString(EXTRA_EXPORT_FORMAT));
				if (task.isActive()) {
					updateNotificationStatus();
				}
				task.writeTaskToDb();
			}
		} else if (error != null) {
			Log.d("ERROR", "Error upon download: " + error);
			// Do something.
		}
	}

	/**
	 * Convenience method for showing the notification for the download.
	 * 
	 * It shows a notification while the download is in progress. Then changes
	 * text to show something different when the download is complete.
	 * 
	 * When the download is complete, we add an action to the notification to
	 * show the file
	 * 
	 * TODO: Change messages.
	 * 
	 * @param inProgress
	 *            boolean showing if the download is in progress or already
	 *            finished
	 * @param fileUri
	 *            the Uri containing the downloaded file
	 */
	private void showDownloadNotification(boolean inProgress, Uri fileUri) {
		NotificationManager nm = (NotificationManager) getSystemService(Activity.NOTIFICATION_SERVICE);
		NotificationCompat.Builder builder = new NotificationCompat.Builder(
				this);

		builder.setSmallIcon(R.drawable.abbyy_logo)
				.setContentTitle(
						getString(R.string.downloading_notification_title))
				.setContentText(
						getString(R.string.downloading_notification_message))
				.setAutoCancel(false).setOngoing(true);

		if (!inProgress) {
			Intent intent = new Intent(Intent.ACTION_VIEW, fileUri);
			PendingIntent pendingIntent = PendingIntent.getActivity(this,
					REQUEST_SHOW_RESULT, intent, 0);
			builder.setContentIntent(pendingIntent)
					.setAutoCancel(true)
					.setOngoing(false)
					.setContentTitle(
							getString(R.string.downloaded_notification_title))
					.setContentText(
							getString(R.string.downloaded_notification_message));
		} else {
			// On versions prior to honeycomb, the pending intent is required
			// in order to create a notification
			if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
				PendingIntent pendingIntent = PendingIntent.getActivity(this,
						REQUEST_SHOW_RESULT, new Intent(), 0);
				builder.setContentIntent(pendingIntent);
			}
		}
		nm.notify(DOWNLOAD_NOTIFICATION, builder.build());
	}

	/**
	 * Set the notification status for the work in progress.
	 */
	protected void updateNotificationStatus() {
		NotificationManager nm = (NotificationManager) getSystemService(Activity.NOTIFICATION_SERVICE);
		NotificationCompat.Builder builder = new NotificationCompat.Builder(
				this);

		Intent intent = new Intent(this, MainActivity.class);
		PendingIntent pendingIntent = PendingIntent.getActivity(this,
				REQUEST_ACTIVE_TASKS, intent, Intent.FLAG_ACTIVITY_NEW_TASK);

		builder.setSmallIcon(R.drawable.abbyy_logo)
				.setContentTitle(getString(R.string.processing_notification_title))
				.setContentText(getString(R.string.processing_notification_message))
				.setContentIntent(pendingIntent).setAutoCancel(false)
				.setOngoing(true);

		nm.notify(NOTIFICATION_TAG, TASKS_NOTIFICATION, builder.build());
	}
}