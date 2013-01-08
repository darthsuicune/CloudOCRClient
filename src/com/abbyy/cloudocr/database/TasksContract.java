package com.abbyy.cloudocr.database;

import android.net.Uri;
import android.provider.BaseColumns;

public class TasksContract {
	public static final String DB_NAME = "grades";
	public static final int DB_VERSION = 1;	
	
	private static final String CONTENT_NAME = "com.abbyy.cloudocr.TasksProvider";
	public static final Uri CONTENT_TASKS = Uri.parse("" + CONTENT_NAME);

	private TasksContract(){
	}
	
	public static class TasksTable implements BaseColumns{
		public static final String _ID = "_id";
		public static final String TASK_ID = "taskid";
		public static final String STATUS = "taskstatus";
		public static final String REGISTRATION_TIME = "registrationtime";
		public static final String STATUS_CHANGE_TIME = "statuschangetime";
		public static final String FILES_COUNT = "filescount";
		public static final String CREDITS = "credits";
		public static final String ESTIMATED_PROCESSING_TIME = "estimatedprocessingtime";
		public static final String DESCRIPTION = "description";
		public static final String RESULT_URL = "resulturl";
		public static final String ERROR = "error";
		
		public static final String TABLE_NAME = "tasks";
		public static final int _COUNT = 11;
		public static final String DEFAULT_ORDER = _ID + " DESC";
		
	}
}
