package com.abbyy.cloudocr.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class TasksDBOpenHelper extends SQLiteOpenHelper {
	private final String key = " INTEGER PRIMARY KEY AUTOINCREMENT, ";
	private final String text = " TEXT, ";

	public TasksDBOpenHelper(Context context) {
		super(context, TasksContract.DB_NAME, null, TasksContract.DB_VERSION);
	}

	/**
	 * Open the database for writing in case it is not already opened and create
	 * the database.
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
		if (db.isReadOnly()) {
			db = getWritableDatabase();
		}

		db.execSQL("CREATE TABLE " + TasksContract.TasksTable.TABLE_NAME + " ("
				+ TasksContract.TasksTable._ID + key
				+ TasksContract.TasksTable.TASK_ID + " TEXT NOT NULL, "
				+ TasksContract.TasksTable.REGISTRATION_TIME + text
				+ TasksContract.TasksTable.STATUS_CHANGE_TIME + text
				+ TasksContract.TasksTable.STATUS + text
				+ TasksContract.TasksTable.CREDITS + text
				+ TasksContract.TasksTable.DESCRIPTION + text
				+ TasksContract.TasksTable.ESTIMATED_PROCESSING_TIME + text
				+ TasksContract.TasksTable.FILES_COUNT + text
				+ TasksContract.TasksTable.RESULT_URL + text
				+ TasksContract.TasksTable.FILENAME + text
				+ TasksContract.TasksTable.ERROR + " TEXT"
				+ ")");

		db.execSQL("CREATE TABLE " + TasksContract.LanguagesTable.TABLE_NAME + "("
				+ TasksContract.LanguagesTable._ID + key
				+ TasksContract.LanguagesTable.LANGUAGE + " TEXT NOT NULL, "
				+ TasksContract.LanguagesTable.BCR + text
				+ TasksContract.LanguagesTable.OCR + text
				+ TasksContract.LanguagesTable.ICR + " TEXT"
				+ ")");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Nothing to do here
	}

}
