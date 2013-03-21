package com.abbyy.cloudocr.database;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * The contract class implements constants useful for the communication with the database.
 * 
 * @author Denis Lapuente
 *
 */
public class TasksContract {
	public static final String DB_NAME = "tasks";
	public static final int DB_VERSION = 1;

	static final String CONTENT_NAME = "com.abbyy.cloudocr.database.TasksProvider";
	public static final Uri CONTENT_TASKS = Uri.parse("content://"
			+ CONTENT_NAME + "/" + TasksTable.TABLE_NAME);
	public static final Uri CONTENT_LANGUAGES = Uri.parse("content://"
			+ CONTENT_NAME + "/" + LanguagesTable.TABLE_NAME);

	private TasksContract() {
	}

	public static class TasksTable implements BaseColumns {
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
		public static final String FILENAME = "filename";
		public static final String RESULT_TYPE = "resulttype";

		public static final String TABLE_NAME = "tasks";
		public static final String DEFAULT_ORDER = _ID + " DESC";

	}

	public static class LanguagesTable implements BaseColumns {
		public static final String LANGUAGE = "language";
		public static final String OCR = "ocr";
		public static final String ICR = "icr";
		public static final String BCR = "bcr";

		public static final String TABLE_NAME = "languages";
		public static final String DEFAULT_ORDER = _ID + " DESC";

	}
}
