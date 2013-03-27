package com.abbyy.cloudocr;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.abbyy.cloudocr.database.TasksContract;

/**
 * Holder class with helper methods to retain the information of every single
 * task. It includes different methods of creation. It also includes helper
 * methods.
 * 
 * @author Denis Lapuente
 * 
 */
public class Task {
	DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd'T'HH:mm:ss'Z'",
			Locale.GERMANY);

	private Context mContext;
	private boolean isInDb;

	public String mTaskId;
	public String mStatus;
	public String mRegistrationTime;
	public Date mRegistrationDate;
	public String mStatusChangeTime;
	public Date mStatusChangeDate;
	public String mFilesCount;
	public String mCredits;
	public String mEstimatedProcessingTime;
	public String mDescription;
	public String mResultUrl;
	public String mError;

	/**
	 * Parameter constructor. Requires the context and a single parameter for
	 * each property
	 * 
	 * @param context
	 * @param taskId
	 * @param status
	 * @param registrationTime
	 * @param statusChangeTime
	 * @param filesCount
	 * @param credits
	 * @param estimatedProcessingTime
	 * @param description
	 * @param resultUrl
	 * @param error
	 * @param isInDb
	 * @param fileName
	 * @param resultType
	 */
	public Task(Context context, String taskId, String status,
			String registrationTime, String statusChangeTime,
			String filesCount, String credits, String estimatedProcessingTime,
			String description, String resultUrl, String error, boolean isInDb) {
		mContext = context;
		this.isInDb = isInDb;
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

	/**
	 * HashMap constructor. Receives a hashmap with all the parameters.
	 * 
	 * @param context
	 * @param data
	 * @param fileName
	 * @param exportFormat
	 */
	public Task(Context context, HashMap<String, String> data) {
		mContext = context;
		mTaskId = data.get(context.getString(R.string.field_id));
		mStatus = data.get(context.getString(R.string.field_status));
		mRegistrationTime = data.get(context
				.getString(R.string.field_registration_time));
		mStatusChangeTime = data.get(context
				.getString(R.string.field_status_change_time));
		mFilesCount = data.get(context.getString(R.string.field_files_count));
		mCredits = data.get(context.getString(R.string.field_credits));
		if (data.get(context
				.getString(R.string.field_estimated_processing_time)) != null) {
			mEstimatedProcessingTime = data.get(context
					.getString(R.string.field_estimated_processing_time));
		}
		mDescription = data.get(context.getString(R.string.field_description));
		if (data.get(context.getString(R.string.field_result_url)) != null) {
			mResultUrl = data.get(context.getString(R.string.field_result_url));
		}
		if (data.get(context.getString(R.string.field_error)) != null) {
			mError = data.get(context.getString(R.string.field_error));
		}
	}

	/**
	 * Cursor constructor. Creates the object receiving the data from the
	 * database.
	 * 
	 * @param cursor
	 */
	public Task(Cursor cursor) {
		if (cursor != null) {
			if (cursor.moveToFirst()) {
				mDescription = cursor.getString(cursor
						.getColumnIndex(TasksContract.TasksTable.DESCRIPTION));
				mStatus = cursor.getString(cursor
						.getColumnIndex(TasksContract.TasksTable.STATUS));
				mRegistrationTime = cursor
						.getString(cursor
								.getColumnIndex(TasksContract.TasksTable.REGISTRATION_TIME));
				mStatusChangeTime = cursor
						.getString(cursor
								.getColumnIndex(TasksContract.TasksTable.STATUS_CHANGE_TIME));
				mFilesCount = cursor.getString(cursor
						.getColumnIndex(TasksContract.TasksTable.FILES_COUNT));
				mCredits = cursor.getString(cursor
						.getColumnIndex(TasksContract.TasksTable.CREDITS));
				mEstimatedProcessingTime = cursor
						.getString(cursor
								.getColumnIndex(TasksContract.TasksTable.ESTIMATED_PROCESSING_TIME));
				mResultUrl = cursor.getString(cursor
						.getColumnIndex(TasksContract.TasksTable.RESULT_URL));
				mError = cursor.getString(cursor
						.getColumnIndex(TasksContract.TasksTable.ERROR));

				try {
					mRegistrationDate = dateFormat.parse(mRegistrationTime);
					mStatusChangeDate = dateFormat.parse(mStatusChangeTime);
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * Convenience method for inserting the data into the database.
	 * 
	 * @return
	 */
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

	/**
	 * Convenience method for checking if a specific task is in the database.
	 * 
	 * @return true if the taskId is already on the database.
	 */
	private boolean checkDb() {
		Uri uri = TasksContract.CONTENT_TASKS;
		String selection = TasksContract.TasksTable.TASK_ID + "=?";
		String[] selectionArgs = { mTaskId };

		Cursor cursor = mContext.getContentResolver().query(uri, null,
				selection, selectionArgs, null);
		int count = cursor.getCount();
		cursor.close();
		return count >= 1;
	}

	/**
	 * Convenience method for preparing the values to insert. Avoids inserting
	 * null values.
	 * 
	 * @return
	 */
	private ContentValues setValues() {
		ContentValues values = new ContentValues();
		values.put(TasksContract.TasksTable.TASK_ID, mTaskId);
		if (mCredits != null) {
			values.put(TasksContract.TasksTable.CREDITS, mCredits);
		}
		if (mDescription != null) {
			values.put(TasksContract.TasksTable.DESCRIPTION, mDescription);
		}
		if (mError != null) {
			values.put(TasksContract.TasksTable.ERROR, mError);
		}
		if (mEstimatedProcessingTime != null) {
			values.put(TasksContract.TasksTable.ESTIMATED_PROCESSING_TIME,
					mEstimatedProcessingTime);
		}
		if (mFilesCount != null) {
			values.put(TasksContract.TasksTable.FILES_COUNT, mFilesCount);
		}
		if (mRegistrationTime != null) {
			values.put(TasksContract.TasksTable.REGISTRATION_TIME,
					mRegistrationTime);
		}
		if (mResultUrl != null) {
			values.put(TasksContract.TasksTable.RESULT_URL, mResultUrl);
		}
		if (mStatus != null) {
			values.put(TasksContract.TasksTable.STATUS, mStatus);
		}
		if (mStatusChangeTime != null) {
			values.put(TasksContract.TasksTable.STATUS_CHANGE_TIME,
					mStatusChangeTime);
		}
		return values;
	}

	/**
	 * Convenience method that returns if the task is active or not.
	 * 
	 * @return true if the task is in any active status. false if not.
	 */
	public boolean isActive() {
		return mStatus.equals(mContext.getString(R.string.status_in_progress))
				|| mStatus.equals(mContext.getString(R.string.status_queued))
				|| mStatus
						.equals(mContext.getString(R.string.status_submitted));
	}
}
