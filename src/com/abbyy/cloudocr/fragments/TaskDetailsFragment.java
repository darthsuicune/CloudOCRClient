package com.abbyy.cloudocr.fragments;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.abbyy.cloudocr.R;
import com.abbyy.cloudocr.TaskDetailsActivity;
import com.abbyy.cloudocr.database.TasksContract;

public class TaskDetailsFragment extends ListFragment {
	private static final int LOADER_TASK_INFO = 0;
	private String mTaskId;

	private CursorAdapter mAdapter;

	public TaskDetailsFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.task_details_fragment, container,
				false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		loadTaskData();
		setAdapter();
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.task_details, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {

		}
		return true;
	}

	private void loadTaskData() {
		if (mTaskId == null) {
			mTaskId = getArguments().getString(
					TaskDetailsActivity.EXTRA_TASK_ID);
		}

		getActivity().getSupportLoaderManager().initLoader(LOADER_TASK_INFO,
				null, new TaskInfoHelper());
	}

	private void setAdapter() {
		String[] from = { TasksContract.TasksTable.TASK_ID };
		int[] to = { R.id.task_details_task_id };

		mAdapter = new SimpleCursorAdapter(getActivity(), R.layout.task_details_item, null, from, to,
				SimpleCursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
		setListAdapter(mAdapter);
	}

	private class TaskInfoHelper implements LoaderCallbacks<Cursor> {

		@Override
		public Loader<Cursor> onCreateLoader(int id, Bundle args) {
			Loader<Cursor> loader = null;
			Uri uri = TasksContract.CONTENT_TASKS;
			String selection = TasksContract.TasksTable.TASK_ID + "=?"; 
			String[] selectionArgs = { mTaskId };
			switch (id) {
			case LOADER_TASK_INFO:
				loader = new CursorLoader(getActivity(), uri, null,
						selection, selectionArgs, null);
				break;
			}
			return loader;
		}

		@Override
		public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
			mAdapter.swapCursor(cursor);
		}

		@Override
		public void onLoaderReset(Loader<Cursor> loader) {
			mAdapter.swapCursor(null);
		}

	}
}
