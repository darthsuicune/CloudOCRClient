package com.abbyy.cloudocr.database;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;

import com.abbyy.cloudocr.R;

/**
 * The Content PRovider for the database access. It is not required, as the
 * database will not be shared with other applications (for now), but it can be
 * used in the future.
 * 
 * Aside, using a content provider allows for automatic content observation,
 * which would lead to less work on the lists.
 */
public class TasksProvider extends ContentProvider {
	TasksDBOpenHelper mDbHelper;

	private static final int URI_TASK = 1;
	private static final int URI_TASK_ID = 2;
	private static final int URI_LANGUAGE = 3;
	private static final int URI_LANGUAGE_ID = 4;

	static UriMatcher sUriMatcher;
	static {
		sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		sUriMatcher.addURI(TasksContract.CONTENT_NAME,
				TasksContract.TasksTable.TABLE_NAME, URI_TASK);
		sUriMatcher.addURI(TasksContract.CONTENT_NAME,
				TasksContract.TasksTable.TABLE_NAME + "/#", URI_TASK_ID);
		sUriMatcher.addURI(TasksContract.CONTENT_NAME,
				TasksContract.LanguagesTable.TABLE_NAME, URI_LANGUAGE);
		sUriMatcher
				.addURI(TasksContract.CONTENT_NAME,
						TasksContract.LanguagesTable.TABLE_NAME + "/#",
						URI_LANGUAGE_ID);
	}

	/**
	 * On creation of the provider we set the Database helper. Returns if it was
	 * created correctly.
	 */
	@Override
	public boolean onCreate() {
		mDbHelper = new TasksDBOpenHelper(getContext());
		return mDbHelper != null;
	}

	/**
	 * Required for the content provider. It must return a line of the type:
	 * vnd.android.cursor.(item|dir)/<content_name>.<table_name>, based on the
	 * received uri.
	 */
	@Override
	public String getType(Uri uri) {
		switch (sUriMatcher.match(uri)) {
		case URI_TASK:
			return ContentResolver.CURSOR_DIR_BASE_TYPE
					+ TasksContract.CONTENT_NAME + "."
					+ TasksContract.TasksTable.TABLE_NAME;
		case URI_TASK_ID:
			return ContentResolver.CURSOR_ITEM_BASE_TYPE
					+ TasksContract.CONTENT_NAME + "."
					+ TasksContract.TasksTable.TABLE_NAME;
		case URI_LANGUAGE:
			return ContentResolver.CURSOR_DIR_BASE_TYPE
					+ TasksContract.CONTENT_NAME + "."
					+ TasksContract.LanguagesTable.TABLE_NAME;
		case URI_LANGUAGE_ID:
			return ContentResolver.CURSOR_ITEM_BASE_TYPE
					+ TasksContract.CONTENT_NAME + "."
					+ TasksContract.LanguagesTable.TABLE_NAME;
		default:
			throw new IllegalArgumentException(getContext().getString(
					R.string.illegal_uri)
					+ uri.toString());
		}
	}

	/**
	 * Insert method for the provider. It manages insertions into tables, then
	 * notifies all the listeners.
	 */
	@Override
	public Uri insert(Uri uri, ContentValues values) {
		String table = getTable(uri);

		long id = mDbHelper.getWritableDatabase().insert(table, null, values);
		Uri result = null;
		if (id != -1) {
			result = ContentUris.withAppendedId(uri, id);
		}
		getContext().getContentResolver().notifyChange(uri, null);
		getContext().getContentResolver().notifyChange(result, null);
		return result;
	}

	/**
	 * Query method for the provider. Manages the queries performed by the
	 * clients of the provider and sets the notification uri for the cursor, so
	 * it gets notifications when available.
	 */
	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		String table = getTable(uri);

		String groupBy = null;
		String having = null;

		Cursor cursor = mDbHelper.getReadableDatabase().query(table,
				projection, selection, selectionArgs, groupBy, having,
				sortOrder);
		cursor.setNotificationUri(getContext().getContentResolver(), uri);
		return cursor;
	}

	/**
	 * Update method for the provider. Manages the updating of existing rows
	 * from the database. Notifies changes when applicable.
	 */
	@Override
	public int update(Uri uri, ContentValues values, String where,
			String[] selectionArgs) {
		String table = getTable(uri);

		int count = mDbHelper.getWritableDatabase().update(table, values,
				where, selectionArgs);

		if (count > 0) {
			getContext().getContentResolver().notifyChange(uri, null);
		}
		return count;
	}

	/**
	 * Delete method for the provider. Manages the deletion of existing rows
	 * from the database. Notifies changes when applicable.
	 */
	@Override
	public int delete(Uri uri, String where, String[] selectionArgs) {
		String table = getTable(uri);

		int count = mDbHelper.getWritableDatabase().delete(table, where,
				selectionArgs);

		if (count > 0) {
			getContext().getContentResolver().notifyChange(uri, null);
		}
		return count;
	}

	/**
	 * Convenience method for choosing tables in the above methods. Used only
	 * for cleaner reading and centralizing programming effort.
	 * 
	 * @param uri
	 *            The uri to get the table name from
	 * @return A String with the correct table name.
	 */
	private String getTable(Uri uri) {
		switch (sUriMatcher.match(uri)) {
		case URI_TASK_ID:
		case URI_TASK:
			return TasksContract.TasksTable.TABLE_NAME;
		case URI_LANGUAGE_ID:
		case URI_LANGUAGE:
			return TasksContract.LanguagesTable.TABLE_NAME;
		default:
			throw new IllegalArgumentException(getContext().getString(
					R.string.illegal_uri)
					+ uri.toString());
		}
	}
}