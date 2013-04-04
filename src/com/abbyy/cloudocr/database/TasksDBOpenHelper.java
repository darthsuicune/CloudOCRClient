package com.abbyy.cloudocr.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.abbyy.cloudocr.R;

/**
 * Implementation of the SQLiteOpenHelper that creates the database. When
 * updating the database the onUpgrade will be performed. Due to restrictions
 * with SQLite, modifying the fields of a table is not supported, so when
 * modifying a table structure we must drop the database and re-create it from
 * scratch
 * 
 * @author Denis Lapuente
 * 
 */
public class TasksDBOpenHelper extends SQLiteOpenHelper {
	// Helper constants
	private final String key = " INTEGER PRIMARY KEY AUTOINCREMENT, ";
	private final String text = " TEXT, ";
	
	private Context mContext;

	/**
	 * Constructor, to use in the provider. The null parameter is a
	 * cursorfactory, for which we are using the default one.
	 * 
	 * @param context
	 *            The context is required to create the database.
	 */
	TasksDBOpenHelper(Context context) {
		super(context, TasksContract.DB_NAME, null, TasksContract.DB_VERSION);
		mContext = context; 
	}

	/**
	 * Open the database for writing in case it is not already opened and create
	 * the database.
	 * 
	 * Then insert the first elements.
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
				+ TasksContract.TasksTable.FROM_DEVICE + text
				+ TasksContract.TasksTable.ERROR + " TEXT" + ")");

		db.execSQL("CREATE TABLE " + TasksContract.LanguagesTable.TABLE_NAME
				+ "(" + TasksContract.LanguagesTable._ID + key
				+ TasksContract.LanguagesTable.LANGUAGE + " TEXT NOT NULL, "
				+ TasksContract.LanguagesTable.BCR + text
				+ TasksContract.LanguagesTable.OCR + text
				+ TasksContract.LanguagesTable.ICR + " TEXT" + ")");
		
		String[] languagesList = mContext.getResources().getStringArray(
				R.array.languages);
		for (int i = 0; i < languagesList.length; i++) {
			ContentValues values = new ContentValues();
			values.put(TasksContract.LanguagesTable.LANGUAGE,
					languagesList[i]);
			db.insert(TasksContract.LanguagesTable.TABLE_NAME, null, values);
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Nothing to do here
	}
}
