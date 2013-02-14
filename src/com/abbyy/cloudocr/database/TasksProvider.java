package com.abbyy.cloudocr.database;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

import com.abbyy.cloudocr.R;

public class TasksProvider extends ContentProvider {

	TasksDBOpenHelper mDbHelper;

	private static final int URI_TASK = 1;
	private static final int URI_TASK_ID = 2;

	static UriMatcher sUriMatcher;
	static {
		sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		sUriMatcher.addURI(TasksContract.CONTENT_NAME,
				TasksContract.TasksTable.TABLE_NAME, URI_TASK);
		sUriMatcher.addURI(TasksContract.CONTENT_NAME,
				TasksContract.TasksTable.TABLE_NAME + "/#", URI_TASK_ID);
	}

	@Override
	public boolean onCreate() {
		mDbHelper = new TasksDBOpenHelper(getContext());
		return mDbHelper != null;
	}

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
		default:
			throw new IllegalArgumentException(getContext().getString(
					R.string.illegal_uri)
					+ uri.toString());
		}
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		switch (sUriMatcher.match(uri)) {
		case URI_TASK_ID:
		case URI_TASK:
			break;
		default:
			throw new IllegalArgumentException(getContext().getString(
					R.string.illegal_uri)
					+ uri.toString());
		}

		SQLiteDatabase db = mDbHelper.getWritableDatabase();
		long id = db.insert(TasksContract.TasksTable.TABLE_NAME, null, values);
		Uri result = null;
		if (id != -1) {
			result = ContentUris.withAppendedId(uri, id);
		}
		getContext().getContentResolver().notifyChange(
				TasksContract.CONTENT_TASKS, null);
		getContext().getContentResolver().notifyChange(result, null);
		return result;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		switch (sUriMatcher.match(uri)) {
		case URI_TASK_ID:
		case URI_TASK:
			break;
		default:
			throw new IllegalArgumentException(getContext().getString(
					R.string.illegal_uri)
					+ uri.toString());
		}

		SQLiteDatabase db = mDbHelper.getReadableDatabase();

		SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
		builder.setTables(TasksContract.TasksTable.TABLE_NAME);
		String groupBy = null;
		String having = null;

		Cursor cursor = builder.query(db, projection, selection, selectionArgs,
				groupBy, having, sortOrder);
		cursor.setNotificationUri(getContext().getContentResolver(), uri);
		return cursor;
	}

	@Override
	public int update(Uri uri, ContentValues values, String where,
			String[] selectionArgs) {
		switch (sUriMatcher.match(uri)) {
		case URI_TASK_ID:

		case URI_TASK:
			break;
		default:
			throw new IllegalArgumentException(getContext().getString(
					R.string.illegal_uri)
					+ uri.toString());
		}

		SQLiteDatabase db = mDbHelper.getWritableDatabase();
		int count = db.update(TasksContract.TasksTable.TABLE_NAME, values,
				where, selectionArgs);

		if (count > 0) {
			getContext().getContentResolver().notifyChange(uri, null);
		}
		return count;
	}

	@Override
	public int delete(Uri uri, String where, String[] selectionArgs) {
		switch (sUriMatcher.match(uri)) {
		case URI_TASK_ID:

		case URI_TASK:
			break;
		default:
			throw new IllegalArgumentException(getContext().getString(
					R.string.illegal_uri)
					+ uri.toString());
		}

		SQLiteDatabase db = mDbHelper.getWritableDatabase();
		int count = db.delete(TasksContract.TasksTable.TABLE_NAME, where,
				selectionArgs);

		if (count > 0) {
			getContext().getContentResolver().notifyChange(uri, null);
		}
		return count;
	}

}