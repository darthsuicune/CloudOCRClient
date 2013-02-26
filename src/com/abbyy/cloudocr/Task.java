package com.abbyy.cloudocr;

import java.util.HashMap;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.abbyy.cloudocr.database.TasksContract;

public class Task {
	private Context mContext;
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

	public Task(Context context, HashMap<String, String> data) {
		mContext = context;
		mTaskId = data.get(context.getString(R.string.field_id));
		mStatus = data.get(context.getString(R.string.field_status));
		mRegistrationTime = data.get(context.getString(R.string.field_registration_time));
		mStatusChangeTime = data.get(context.getString(R.string.field_status_change_time));
		mFilesCount = Integer.parseInt(data.get(context.getString(R.string.field_files_count)));
		mCredits = Integer.parseInt(data.get(context.getString(R.string.field_credits)));
		mEstimatedProcessingTime = Integer.parseInt(data.get(context.getString(R.string.field_estimated_processing_time)));
		mDescription = data.get(context.getString(R.string.field_description));
		mResultUrl = data.get(context.getString(R.string.field_result_url));
		mError = data.get(context.getString(R.string.field_error));
	}

	public boolean writeTaskToDb() {
		isInDb = checkDb();

		Uri uri = TasksContract.CONTENT_TASKS;

		if (isInDb) {
			String where = TasksContract.TasksTable.TASK_ID + "=?";
			String[] selectionArgs = { mTaskId };
			int count = mContext.getContentResolver().update(uri, setValues(),
					where, selectionArgs);
			if (count < 1) {
				return false;
			}
		} else {
			mContext.getContentResolver().insert(uri, setValues());
		}
		return isInDb;
	}

	private boolean checkDb() {
		Uri uri = TasksContract.CONTENT_TASKS;
		String selection = TasksContract.TasksTable.TASK_ID + "=?";
		String[] selectionArgs = { mTaskId };

		Cursor cursor = mContext.getContentResolver().query(uri, null, selection,
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
