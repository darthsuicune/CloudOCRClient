package com.abbyy.cloudocr;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.abbyy.cloudocr.database.TasksContract;

public class CompletedTasksFragment extends ListFragment {
	private static final int LOADER_COMPLETED_CURSOR = 1;
	private static final int LOADER_COMPLETED_INTERNET = 2;
	private CursorAdapter mAdapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		setHasOptionsMenu(true);
		return inflater.inflate(R.layout.completed_tasks_fragment, container,
				false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		String[] from = {};
		int[] to = {};
		mAdapter = new SimpleCursorAdapter(getActivity(),
				R.layout.completed_entry, null, from, to,
				SimpleCursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
		setListAdapter(mAdapter);
		getActivity().getSupportLoaderManager().initLoader(LOADER_COMPLETED_CURSOR,
				null, new CursorLoaderHelper());
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);

	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.activity_main, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_refresh:
			getActivity().getSupportLoaderManager().restartLoader(
					LOADER_COMPLETED_INTERNET, null, new ConnectionLoaderHelper());
			break;
		default:
			break;
		}
		return true;
	}

	private class CursorLoaderHelper implements LoaderCallbacks<Cursor> {

		@Override
		public Loader<Cursor> onCreateLoader(int id, Bundle args) {
			Loader<Cursor> loader = null;
			switch (id) {
			case LOADER_COMPLETED_CURSOR:
				String selection = TasksContract.TasksTable.STATUS + "=?";
				String[] selectionArgs = { getActivity().getString(
						R.string.status_completed) };
				String sortOrder = null;
				loader = new CursorLoader(getActivity(),
						TasksContract.CONTENT_TASKS, null, selection,
						selectionArgs, sortOrder);
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
			default:
				break;
			}
		}

		@Override
		public void onLoaderReset(Loader<Cursor> loader) {
		}

	}

	private class ConnectionLoaderHelper implements
			LoaderCallbacks<InputStream> {

		@Override
		public Loader<InputStream> onCreateLoader(int id, Bundle args) {
			Loader<InputStream> loader = null;
			switch (id) {
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
			case LOADER_COMPLETED_INTERNET:
				try {
					parseResponse(stream);
				} catch (IOException e) {
					e.printStackTrace();
				}
				getActivity().getSupportLoaderManager().restartLoader(LOADER_COMPLETED_CURSOR, null, new CursorLoaderHelper());
				break;
			}
		}

		@Override
		public void onLoaderReset(Loader<InputStream> loader) {
		}
		
		private void parseResponse(InputStream stream) throws IOException{
			String result = new BufferedReader(new InputStreamReader(stream)).readLine();
			Log.d("TEST",result);
		}

	}
}
