package com.abbyy.cloudocr;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.abbyy.cloudocr.database.TasksContract;

public class Task {
	private boolean isInDb;

	public String mTaskId;
	public String mStatus;
	public String mRegistrationTime;
	public String mStatusChangeTime;
	public int mFilesCount;
	public int mCredits;
	public int mEstimatedProcessingTime;
	public String mDescription;
	public String mResultUrl;
	public String mError;

	public Task(String taskId, String status, String registrationTime,
			String statusChangeTime, int filesCount, int credits,
			int estimatedProcessingTime, String description, String resultUrl,
			String error, boolean fromDb) {
		
		updateTask(taskId, status, registrationTime, statusChangeTime,
				filesCount, credits, estimatedProcessingTime, description,
				resultUrl, error);
		isInDb = fromDb;
	}

	public void updateTask(String taskId, String status,
			String registrationTime, String statusChangeTime, int filesCount,
			int credits, int estimatedProcessingTime, String description,
			String resultUrl, String error) {
		mTaskId = taskId;
		mStatus = status;
		mRegistrationTime = registrationTime;
		mStatusChangeTime = statusChangeTime;
		mFilesCount = filesCount;
		mCredits = credits;
		mEstimatedProcessingTime = estimatedProcessingTime;
		mDescription = description;
		mResultUrl = resultUrl;
		mError = error;
	}

	public boolean writeTaskToDb(Context context) {
		isInDb = checkDb(context);

		Uri uri = TasksContract.CONTENT_TASKS;

		if (isInDb) {
			String where = TasksContract.TasksTable.TASK_ID + "=?";
			String[] selectionArgs = { mTaskId };
			int count = context.getContentResolver().update(uri, setValues(),
					where, selectionArgs);
			if (count < 1) {
				return false;
			}
		} else {
			Uri _uri = context.getContentResolver().insert(uri, setValues());
			if (_uri != null) {
			}
		}
		return isInDb;
	}

	private boolean checkDb(Context context) {
		Uri uri = TasksContract.CONTENT_TASKS;
		String selection = TasksContract.TasksTable.TASK_ID + "=?";
		String[] selectionArgs = { mTaskId };

		Cursor cursor = context.getContentResolver().query(uri, null, selection,
				selectionArgs, null);
		return cursor.getCount() == 1;
	}

	private ContentValues setValues() {
		ContentValues values = new ContentValues();
		values.put(TasksContract.TasksTable.CREDITS, mCredits);
		values.put(TasksContract.TasksTable.DESCRIPTION, mDescription);
		values.put(TasksContract.TasksTable.ERROR, mError);
		values.put(TasksContract.TasksTable.ESTIMATED_PROCESSING_TIME,
				mEstimatedProcessingTime);
		values.put(TasksContract.TasksTable.FILES_COUNT, mFilesCount);
		values.put(TasksContract.TasksTable.REGISTRATION_TIME,
				mRegistrationTime);
		values.put(TasksContract.TasksTable.RESULT_URL, mResultUrl);
		values.put(TasksContract.TasksTable.STATUS, mStatus);
		values.put(TasksContract.TasksTable.STATUS_CHANGE_TIME,
				mStatusChangeTime);
		values.put(TasksContract.TasksTable.TASK_ID, mTaskId);
		return values;
	}
}
