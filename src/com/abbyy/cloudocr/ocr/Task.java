package com.abbyy.cloudocr.ocr;

import java.net.MalformedURLException;
import java.net.URL;

public class Task {
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

	public URL getResultURL() {
		try {
			return new URL(mResultUrl);
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return null;
		}

	}
}
