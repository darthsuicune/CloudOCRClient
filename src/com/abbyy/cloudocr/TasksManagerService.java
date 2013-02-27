package com.abbyy.cloudocr;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;

public class TasksManagerService extends IntentService {
	private static final String SERVICE_NAME = "ABBYY Tasks Manager Service";
	
	private static final String EXTRA_ACTION = "action";
	private static final String EXTRA_TASK_ID = "taskId";
	private static final String EXTRA_NEW_TASK_OPTIONS = "taskOptions";
	
	private static final int ACTION_DELETE_TASK = 1;
	private static final int ACTION_UPDATE_ACTIVE_TASKS = 2;
	private static final int ACTION_UPDATE_COMPLETED_TASKS = 3;
	private static final int ACTION_CREATE_NEW_TASK = 4;
	
	

	public TasksManagerService() {
		super(SERVICE_NAME);
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		Bundle extras = intent.getExtras();
		if (extras != null) {
			switch(extras.getInt(EXTRA_ACTION)){
			case ACTION_CREATE_NEW_TASK:
				Bundle options = extras.getBundle(EXTRA_NEW_TASK_OPTIONS);
				createNewTask(options);
				break;
			case ACTION_DELETE_TASK:
				String taskId = extras.getString(EXTRA_TASK_ID);
				deleteTask(taskId);
				break;
			case ACTION_UPDATE_ACTIVE_TASKS:
				refreshTasks(false);
				break;
			case ACTION_UPDATE_COMPLETED_TASKS:
				refreshTasks(true);
				break;
			}
		}
	}
	
	private void refreshTasks(boolean completed){
		if(completed){
			
		} else {
			
		}
		
	}
	private void deleteTask(String taskId){
		
	}
	private void createNewTask(Bundle options){
		
	}
}