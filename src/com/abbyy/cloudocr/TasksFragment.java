package com.abbyy.cloudocr;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
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

import com.abbyy.cloudocr.database.TasksContract;

public class TasksFragment extends ListFragment {

	final static int LOADER_ACTIVE_INTERNET = 1;
	final static int LOADER_ACTIVE_CURSOR = 2;
	final static int LOADER_COMPLETED_INTERNET = 3;
	final static int LOADER_COMPLETED_CURSOR = 4;

	boolean isLandscape;
	CursorAdapter mAdapter;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		isLandscape = getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
		setAdapter();
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		String taskId = ((TextView) v.findViewById(0)).getText().toString(); //TODO replace 0 for ID
		showTaskDetails(taskId);
	}
	
	private void setAdapter(){
		String[] from = {};
		int[] to = {};
		mAdapter = new SimpleCursorAdapter(getActivity(),
				R.layout.completed_entry, null, from, to,
				SimpleCursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
		setListAdapter(mAdapter);
	}

	// TODO
	private void showTaskDetails(String taskId) {
		if (isLandscape) {

		} else {
			Intent intent = new Intent(getActivity(), TaskDetailsActivity.class);
			intent.putExtra(TaskDetailsActivity.EXTRA_TASK_ID, taskId);
			startActivity(intent);
		}
	}

	void loadTasks(boolean active) {
		if (active) {
			getActivity().getSupportLoaderManager().initLoader(
					LOADER_ACTIVE_CURSOR, null, new CursorLoaderHelper());
		} else {
			getActivity().getSupportLoaderManager().initLoader(
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

	void launchNewTask() {
		if (isLandscape) {

		} else {
			Intent intent = new Intent(getActivity(), StartActivity.class);
			startActivity(intent);
		}
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
			switch (id) {
			case LOADER_ACTIVE_CURSOR:
				String[] activeProjection = { TasksContract.TasksTable._ID,
						TasksContract.TasksTable.TASK_ID,
						TasksContract.TasksTable.REGISTRATION_TIME,
						TasksContract.TasksTable.STATUS,
						TasksContract.TasksTable.FILES_COUNT };
				String activeSelection = TasksContract.TasksTable.STATUS + "!=";
				String[] activeSelectionArgs = { getActivity().getString(
						R.string.status_completed) };
				String activeSortOrder = "";
				loader = new CursorLoader(getActivity(),
						TasksContract.CONTENT_TASKS, activeProjection,
						activeSelection, activeSelectionArgs, activeSortOrder);
				break;
			case LOADER_COMPLETED_CURSOR:
				String completedSelection = TasksContract.TasksTable.STATUS
						+ "=?";
				String[] completedSelectionArgs = { getActivity().getString(
						R.string.status_completed) };
				String completedSortOrder = null;
				loader = new CursorLoader(getActivity(),
						TasksContract.CONTENT_TASKS, null, completedSelection,
						completedSelectionArgs, completedSortOrder);
				break;
			}
			return loader;
		}

		@Override
		public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
			switch (loader.getId()) {
			case LOADER_COMPLETED_CURSOR:
				mAdapter.swapCursor(cursor);
				break;
			case LOADER_ACTIVE_CURSOR:
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
		public void onLoadFinished(Loader<String> loader,
				String stream) {
			switch (loader.getId()) {
			case LOADER_ACTIVE_INTERNET:
				try {
					parseResponse(stream);
				} catch (IOException e) {
					e.printStackTrace();
				}
				getActivity().getSupportLoaderManager().restartLoader(
						LOADER_ACTIVE_CURSOR, null, new CursorLoaderHelper());
				break;
			case LOADER_COMPLETED_INTERNET:
				try {
					parseResponse(stream);
				} catch (IOException e) {
					e.printStackTrace();
				}
				break;
			}
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
	private void readData(XmlPullParser parser)
			throws XmlPullParserException, IOException {
		parser.nextTag();
		parser.require(XmlPullParser.START_TAG, null, getActivity()
				.getString(R.string.tag_response));
		while (parser.next() != XmlPullParser.END_TAG) {
			if (parser.getEventType() != XmlPullParser.START_TAG) {
				continue;
			}
			String name = parser.getName();
			if (name.equals(getActivity().getString(R.string.tag_task))) {
				Task newTask = new Task(
						parser.getAttributeValue(null, getActivity().getString(R.string.field_id)),
						parser.getAttributeValue(null, getActivity().getString(R.string.field_status)),
						parser.getAttributeValue(null, getActivity().getString(R.string.field_registration_time)),
						parser.getAttributeValue(null, getActivity().getString(R.string.field_status_change_time)),
						Integer.parseInt(parser.getAttributeValue(null, getActivity().getString(R.string.field_files_count))),
						Integer.parseInt(parser.getAttributeValue(null, getActivity().getString(R.string.field_credits))),
						Integer.parseInt(parser.getAttributeValue(null, getActivity().getString(R.string.field_estimated_processing_time))),
						parser.getAttributeValue(null, getActivity().getString(R.string.field_description)),
						parser.getAttributeValue(null, getActivity().getString(R.string.field_result_url)),
						parser.getAttributeValue(null, getActivity().getString(R.string.field_error)),
						false);
				
				newTask.writeTaskToDb(getActivity().getContentResolver());
				
				if (Integer.parseInt(parser.getAttributeValue(
						null,
						getActivity().getString(
								R.string.field_estimated_processing_time))) > 0) {
					setAlarm();
				}
				finish(true);
			} else if (name.equals(getActivity().getString(
					R.string.tag_error))) {
				finish(false);
			}
		}
		
		
	}
	
	private void setAlarm(){
		
	}
	
	private void finish(boolean isOK){
	}
}
