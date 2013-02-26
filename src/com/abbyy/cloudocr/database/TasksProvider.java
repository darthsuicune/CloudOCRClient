package com.abbyy.cloudocr.database;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;

import com.abbyy.cloudocr.R;

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

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		String table = null;
		switch (sUriMatcher.match(uri)) {
		case URI_TASK_ID:
		case URI_TASK:
			table = TasksContract.TasksTable.TABLE_NAME;

			getContext().getContentResolver().notifyChange(
					TasksContract.CONTENT_TASKS, null);
			break;
		case URI_LANGUAGE_ID:
		case URI_LANGUAGE:
			table = TasksContract.LanguagesTable.TABLE_NAME;

			break;
		default:
			throw new IllegalArgumentException(getContext().getString(
					R.string.illegal_uri)
					+ uri.toString());
		}

		long id = mDbHelper.getWritableDatabase().insert(table, null, values);
		Uri result = null;
		if (id != -1) {
			result = ContentUris.withAppendedId(uri, id);
		}
		getContext().getContentResolver().notifyChange(uri, null);
		getContext().getContentResolver().notifyChange(result, null);
		return result;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		String table = "";
		switch (sUriMatcher.match(uri)) {
		case URI_TASK_ID:
		case URI_TASK:
			table = TasksContract.TasksTable.TABLE_NAME;
			break;
		case URI_LANGUAGE_ID:
		case URI_LANGUAGE:
			table = TasksContract.LanguagesTable.TABLE_NAME;
			break;
		default:
			throw new IllegalArgumentException(getContext().getString(
					R.string.illegal_uri)
					+ uri.toString());
		}

		String groupBy = null;
		String having = null;

		Cursor cursor = mDbHelper.getReadableDatabase().query(table,
				projection, selection, selectionArgs, groupBy, having,
				sortOrder);
		cursor.setNotificationUri(getContext().getContentResolver(), uri);
		return cursor;
	}

	@Override
	public int update(Uri uri, ContentValues values, String where,
			String[] selectionArgs) {
		int count = 0;
		switch (sUriMatcher.match(uri)) {
		case URI_TASK_ID:
		case URI_TASK:
			count = mDbHelper.getWritableDatabase().update(
					TasksContract.TasksTable.TABLE_NAME, values, where,
					selectionArgs);
			break;
		case URI_LANGUAGE_ID:
		case URI_LANGUAGE:
			count = mDbHelper.getWritableDatabase().update(
					TasksContract.LanguagesTable.TABLE_NAME, values, where,
					selectionArgs);
			break;
		default:
			throw new IllegalArgumentException(getContext().getString(
					R.string.illegal_uri)
					+ uri.toString());
		}
		if (count > 0) {
			getContext().getContentResolver().notifyChange(uri, null);
		}
		return count;
	}

	@Override
	public int delete(Uri uri, String where, String[] selectionArgs) {
		int count = 0;
		switch (sUriMatcher.match(uri)) {
		case URI_TASK_ID:
		case URI_TASK:
			count = mDbHelper.getWritableDatabase().delete(
					TasksContract.TasksTable.TABLE_NAME, where, selectionArgs);
			break;
		case URI_LANGUAGE_ID:
		case URI_LANGUAGE:
			count = mDbHelper.getWritableDatabase().delete(
					TasksContract.LanguagesTable.TABLE_NAME, where,
					selectionArgs);
			break;
		default:
			throw new IllegalArgumentException(getContext().getString(
					R.string.illegal_uri)
					+ uri.toString());
		}

		if (count > 0) {
			getContext().getContentResolver().notifyChange(uri, null);
		}
		return count;
	}

}