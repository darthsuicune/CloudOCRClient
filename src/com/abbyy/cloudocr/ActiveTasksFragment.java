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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.abbyy.cloudocr.database.TasksContract;

public class ActiveTasksFragment extends ListFragment {
	private static final int LOADER_ACTIVE_CURSOR = 1;
	private static final int LOADER_ACTIVE_INTERNET = 2;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.completed_tasks_fragment, container,
				false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		setHasOptionsMenu(true);
		getActivity().getSupportLoaderManager().initLoader(
				LOADER_ACTIVE_CURSOR, null, new CursorLoaderHelper());
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);

	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.activity_main, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_refresh:
			getActivity().getSupportLoaderManager().restartLoader(
					LOADER_ACTIVE_INTERNET, null, new ConnectionLoaderHelper());
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
			case LOADER_ACTIVE_CURSOR:
				String[] projection = {

				};
				String selection = "";
				String[] selectionArgs = {};
				String sortOrder = "";
				loader = new CursorLoader(getActivity(),
						TasksContract.CONTENT_TASKS, projection, selection,
						selectionArgs, sortOrder);
				break;
			}
			return loader;
		}

		@Override
		public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
			switch (loader.getId()) {
			case LOADER_ACTIVE_CURSOR:
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
			case LOADER_ACTIVE_INTERNET:
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
				getActivity().getSupportLoaderManager().restartLoader(LOADER_ACTIVE_CURSOR, null, new CursorLoaderHelper());
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
