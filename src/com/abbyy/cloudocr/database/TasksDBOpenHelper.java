package com.abbyy.cloudocr.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class TasksDBOpenHelper extends SQLiteOpenHelper {

	public TasksDBOpenHelper(Context context) {
		super(context, TasksContract.DB_NAME, null, TasksContract.DB_VERSION);
	}

	
	/**
	 * Open the database for writing in case it is not already opened and create the database.
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
		if (db.isReadOnly()) {
			db = getWritableDatabase();
		}

		db.execSQL(
				"CREATE TABLE " + TasksContract.TasksTable.TABLE_NAME + " (" +
				TasksContract.TasksTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT" +
				TasksContract.TasksTable.TASK_ID + "TEXT NOT NULL,  " +
				TasksContract.TasksTable.REGISTRATION_TIME + "TEXT, " +
				TasksContract.TasksTable.STATUS_CHANGE_TIME + "TEXT, " +
				TasksContract.TasksTable.STATUS + "TEXT, " +
				TasksContract.TasksTable.CREDITS + "TEXT, " +
				TasksContract.TasksTable.DESCRIPTION + "TEXT, " +
				TasksContract.TasksTable.ESTIMATED_PROCESSING_TIME + "TEXT, " +
				TasksContract.TasksTable.FILES_COUNT + "TEXT, " +
				TasksContract.TasksTable.RESULT_URL + "TEXT, " +
				TasksContract.TasksTable.ERROR + "TEXT" +
				")"
		);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Nothing to do here
	}

}
