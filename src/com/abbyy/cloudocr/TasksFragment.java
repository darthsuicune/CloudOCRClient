package com.abbyy.cloudocr;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.content.Intent;
import android.content.res.Configuration;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Xml;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.abbyy.cloudocr.database.TasksContract;

public abstract class TasksFragment extends ListFragment {

	final static int LOADER_ACTIVE_INTERNET = 1;
	final static int LOADER_ACTIVE_CURSOR = 2;
	final static int LOADER_COMPLETED_INTERNET = 3;
	final static int LOADER_COMPLETED_CURSOR = 4;

	private boolean isLandscape;
	private CursorAdapter mAdapter;
	private Handler mHandler;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		isLandscape = getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
		setAdapter();
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		String taskId = ((TextView) v.findViewById(R.id.task_list_item_task_id))
				.getText().toString();
		showTaskDetails(taskId);
	}

	private void setAdapter() {
		String[] from = { TasksContract.TasksTable.TASK_ID,
				TasksContract.TasksTable.REGISTRATION_TIME, };
		int[] to = { R.id.task_list_item_task_id,
				R.id.task_list_item_registration_time };

		mHandler = new Handler();
		getActivity().getContentResolver().registerContentObserver(
				TasksContract.CONTENT_TASKS, true, new TasksContentObserver());

		mAdapter = new SimpleCursorAdapter(getActivity(),
				R.layout.completed_entry, null, from, to,
				SimpleCursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);

		setListAdapter(mAdapter);

	}

	// TODO landscape
	private void showTaskDetails(String taskId) {
		if (isLandscape) {

		} else {
			Toast.makeText(getActivity(), taskId, Toast.LENGTH_LONG).show();
			// Intent intent = new Intent(getActivity(),
			// TaskDetailsActivity.class);
			// intent.putExtra(TaskDetailsActivity.EXTRA_TASK_ID, taskId);
			// startActivity(intent);
		}
	}

	void loadTasks(boolean active) {
		if (active) {
			getActivity().getSupportLoaderManager().restartLoader(
					LOADER_ACTIVE_CURSOR, null, new CursorLoaderHelper());
		} else {
			getActivity().getSupportLoaderManager().restartLoader(
					LOADER_COMPLETED_CURSOR, null, new CursorLoaderHelper());
		}

	}

	void downloadTasks(boolean active) {
		if (active) {
			getActivity().getSupportLoaderManager().restartLoader(
					LOADER_ACTIVE_INTERNET, null, new ConnectionLoaderHelper());
		} else {
			getActivity().getSupportLoaderManager().restartLoader(
					LOADER_COMPLETED_INTERNET, null,
					new ConnectionLoaderHelper());
		}
	}

	// TODO
	void launchNewTask() {
		if (isLandscape) {

		} else {
			Intent intent = new Intent(getActivity(), StartActivity.class);
			startActivity(intent);
		}
	}

	void openSettings() {
		Intent intent = new Intent(getActivity(), SettingsActivity.class);
		startActivity(intent);
	}

	void removeTaskFromList(String taskId) {
		Uri uri = TasksContract.CONTENT_TASKS;
		String where = TasksContract.TasksTable.TASK_ID + "=?";
		String[] selectionArgs = { taskId };
		getActivity().getContentResolver().delete(uri, where, selectionArgs);
	}

	class CursorLoaderHelper implements LoaderCallbacks<Cursor> {

		@Override
		public Loader<Cursor> onCreateLoader(int id, Bundle args) {
			Loader<Cursor> loader = null;
			String[] projection = { TasksContract.TasksTable._ID,
					TasksContract.TasksTable.TASK_ID,
					TasksContract.TasksTable.REGISTRATION_TIME,
					TasksContract.TasksTable.STATUS,
					TasksContract.TasksTable.FILES_COUNT };

			switch (id) {
			case LOADER_ACTIVE_CURSOR:
				String activeSelection = TasksContract.TasksTable.STATUS
						+ "!=?";
				String[] activeSelectionArgs = { getActivity().getString(
						R.string.status_completed) };
				String activeSortOrder = null;

				loader = new CursorLoader(getActivity(),
						TasksContract.CONTENT_TASKS, projection,
						activeSelection, activeSelectionArgs, activeSortOrder);
				break;
			case LOADER_COMPLETED_CURSOR:
				String completedSelection = TasksContract.TasksTable.STATUS
						+ "=?";
				String[] completedSelectionArgs = { getActivity().getString(
						R.string.status_completed) };
				String completedSortOrder = null;

				loader = new CursorLoader(getActivity(),
						TasksContract.CONTENT_TASKS, projection,
						completedSelection, completedSelectionArgs,
						completedSortOrder);
				break;
			}
			return loader;
		}

		@Override
		public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
			switch (loader.getId()) {
			case LOADER_ACTIVE_CURSOR:
				mAdapter.swapCursor(cursor);
				break;
			case LOADER_COMPLETED_CURSOR:
				mAdapter.swapCursor(cursor);
				break;
			default:
				break;
			}
		}

		@Override
		public void onLoaderReset(Loader<Cursor> loader) {
		}

	}

	class ConnectionLoaderHelper implements LoaderCallbacks<String> {

		@Override
		public Loader<String> onCreateLoader(int id, Bundle args) {
			Loader<String> loader = null;
			switch (id) {
			case LOADER_ACTIVE_INTERNET:
				loader = new AsyncConnectionLoader(getActivity(), args);
				break;
			case LOADER_COMPLETED_INTERNET:
				loader = new AsyncConnectionLoader(getActivity(), args);
				break;
			}
			return loader;
		}

		@Override
		public void onLoadFinished(Loader<String> loader, String response) {
			try {
				parseResponse(response);
			} catch (IOException e) {
				e.printStackTrace();
			}
			switch (loader.getId()) {
			case LOADER_ACTIVE_INTERNET:
				// loadTasks(true);
				break;
			case LOADER_COMPLETED_INTERNET:
				// loadTasks(false);
				break;
			}
			loader.abandon();
		}

		@Override
		public void onLoaderReset(Loader<String> loader) {
		}
	}

	private void parseResponse(String response) throws IOException {
		XmlPullParser parser = Xml.newPullParser();
		try {
			parser.setInput(new ByteArrayInputStream(response.getBytes()), null);
			readData(parser);
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * This method is in charge of parsing the XML data received.
	 * 
	 * @param parser
	 *            XmlPullParser
	 * @throws XmlPullParserException
	 * @throws IOException
	 */
	private void readData(XmlPullParser parser) throws XmlPullParserException,
			IOException {
		parser.nextTag();
		parser.require(XmlPullParser.START_TAG, null,
				getActivity().getString(R.string.tag_response));
		while (parser.next() != XmlPullParser.END_TAG) {
			if (parser.getEventType() != XmlPullParser.START_TAG) {
				continue;
			}
			String name = parser.getName();
			if (name.equals(getActivity().getString(R.string.tag_task))) {
				Task newTask = new Task(
						parser.getAttributeValue(null,
								getActivity().getString(R.string.field_id)),
						parser.getAttributeValue(null,
								getActivity().getString(R.string.field_status)),
						parser.getAttributeValue(
								null,
								getActivity().getString(
										R.string.field_registration_time)),
						parser.getAttributeValue(
								null,
								getActivity().getString(
										R.string.field_status_change_time)),
						Integer.parseInt(parser.getAttributeValue(
								null,
								getActivity().getString(
										R.string.field_files_count))),
						Integer.parseInt(parser
								.getAttributeValue(null, getActivity()
										.getString(R.string.field_credits))),
						Integer.parseInt(parser
								.getAttributeValue(
										null,
										getActivity()
												.getString(
														R.string.field_estimated_processing_time))),
						parser.getAttributeValue(
								null,
								getActivity().getString(
										R.string.field_description)), parser
								.getAttributeValue(null, getActivity()
										.getString(R.string.field_result_url)),
						parser.getAttributeValue(null,
								getActivity().getString(R.string.field_error)),
						false);

				handleNewTask(newTask);
			} else if (name.equals(getActivity().getString(R.string.tag_error))) {
				finish(false);
			}
		}

	}

	private void handleNewTask(Task task) {
		task.writeTaskToDb(getActivity().getContentResolver());

		if (task.mEstimatedProcessingTime > 0) {
			setAlarm();
		}
		finish(true);
	}

	private void setAlarm() {

	}

	private void finish(boolean isOK) {
	}

	private class TasksContentObserver extends ContentObserver {

		public TasksContentObserver() {
			super(mHandler);
		}

		@Override
		public void onChange(boolean selfChange) {
			Toast.makeText(getActivity(), "CONTENT CHANGED!!!",
					Toast.LENGTH_LONG).show();
		}

	}
}
