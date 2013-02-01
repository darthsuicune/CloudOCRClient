package com.abbyy.cloudocr;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
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

		String[] from = {};
		int[] to = {};
		mAdapter = new SimpleCursorAdapter(getActivity(),
				R.layout.completed_entry, null, from, to,
				SimpleCursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
		setListAdapter(mAdapter);
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		String taskId = ((TextView) v.findViewById(0)).getText().toString();
		showTaskDetails(taskId);
	}

	// TODO
	private void showTaskDetails(String taskId) {
		if (isLandscape) {

		} else {
			Intent intent = new Intent();
			intent.putExtra("", taskId);
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
					LOADER_ACTIVE_INTERNET, null,
					new ConnectionLoaderHelper());
		} else {
			getActivity().getSupportLoaderManager().restartLoader(
					LOADER_COMPLETED_INTERNET, null,
					new ConnectionLoaderHelper());
		}
	}

	class CursorLoaderHelper implements LoaderCallbacks<Cursor> {

		@Override
		public Loader<Cursor> onCreateLoader(int id, Bundle args) {
			Loader<Cursor> loader = null;
			switch (id) {
			case LOADER_ACTIVE_CURSOR:
				String[] activeProjection = {

				};
				String activeSelection = "";
				String[] activeSelectionArgs = {};
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

	class ConnectionLoaderHelper implements LoaderCallbacks<InputStream> {

		@Override
		public Loader<InputStream> onCreateLoader(int id, Bundle args) {
			Loader<InputStream> loader = null;
			switch (id) {
			case LOADER_ACTIVE_INTERNET:
				loader = new AsyncInputStreamLoader(getActivity(), args);
				break;
			case LOADER_COMPLETED_INTERNET:
				loader = new AsyncInputStreamLoader(getActivity(), args);
				break;
			}
			return loader;
		}

		@Override
		public void onLoadFinished(Loader<InputStream> loader,
				InputStream stream) {
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
		public void onLoaderReset(Loader<InputStream> loader) {
		}

		private void parseResponse(InputStream stream) throws IOException {
			String result = new BufferedReader(new InputStreamReader(stream))
					.readLine();
			Log.d("TEST", result);
		}
	}
}