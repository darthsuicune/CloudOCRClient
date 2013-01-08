package com.abbyy.cloudocr;

import com.abbyy.cloudocr.database.TasksContract;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.net.Uri;

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
	
	public Task(String taskId, String status, String registrationTime, String statusChangeTime, int filesCount, int credits,
			int estimatedProcessingTime, String description, String resultUrl, String error, boolean fromDb){
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
		isInDb = fromDb;
	}

	public void updateTask(String taskId, String status, String registrationTime, String statusChangeTime, int filesCount, int credits,
			int estimatedProcessingTime, String description, String resultUrl, String error){
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
	
	public boolean writeTaskToDb(ContentResolver cr){
		Uri uri = TasksContract.CONTENT_TASKS;
		ContentValues values = new ContentValues();
		values.put(TasksContract.TasksTable.CREDITS, mCredits);
		values.put(TasksContract.TasksTable.DESCRIPTION, mDescription);
		values.put(TasksContract.TasksTable.ERROR, mError);
		values.put(TasksContract.TasksTable.ESTIMATED_PROCESSING_TIME, mEstimatedProcessingTime);
		values.put(TasksContract.TasksTable.FILES_COUNT, mFilesCount);
		values.put(TasksContract.TasksTable.REGISTRATION_TIME, mRegistrationTime);
		values.put(TasksContract.TasksTable.RESULT_URL, mResultUrl);
		values.put(TasksContract.TasksTable.STATUS, mStatus);
		values.put(TasksContract.TasksTable.STATUS_CHANGE_TIME, mStatusChangeTime);
		values.put(TasksContract.TasksTable.TASK_ID, mTaskId);
		
		if(isInDb){
			String where = TasksContract.TasksTable.TASK_ID + "=?";
			String[] selectionArgs = {
					mTaskId
			};
			int count = cr.update(uri, values, where, selectionArgs);
			if(count < 1){
				return false;
			}
		} else {
			if(cr.insert(uri, values) != null){
				isInDb = true;
			}
		}
		return isInDb;
	}
}
