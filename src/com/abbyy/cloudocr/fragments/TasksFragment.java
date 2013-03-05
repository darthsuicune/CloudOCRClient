package com.abbyy.cloudocr.fragments;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.abbyy.cloudocr.R;
import com.abbyy.cloudocr.SettingsActivity;
import com.abbyy.cloudocr.TaskDetailsActivity;
import com.abbyy.cloudocr.TasksManagerService;
import com.abbyy.cloudocr.database.TasksContract;

public abstract class TasksFragment extends ListFragment implements
		LoaderCallbacks<Cursor> {

	protected static final int LOADER_ACTIVE_TASKS = 1;
	protected static final int LOADER_COMPLETED_TASKS = 2;

	abstract void setAdapter();

	abstract void removeTask(String taskId);

	boolean isLandscape;

	protected SimpleCursorAdapter mAdapter;

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

	// TODO landscape
	private void showTaskDetails(String taskId) {
		if (isLandscape) {
			Toast.makeText(getActivity(), taskId, Toast.LENGTH_LONG).show();
		} else {
			Intent intent = new Intent(getActivity(), TaskDetailsActivity.class);
			intent.putExtra(TaskDetailsActivity.EXTRA_TASK_ID, taskId);
			startActivity(intent);
		}
	}

	void downloadTasks(boolean active) {
		Intent service = new Intent(getActivity(), TasksManagerService.class);
		if (active) {
			service.putExtra(TasksManagerService.EXTRA_ACTION,
					TasksManagerService.ACTION_UPDATE_ACTIVE_TASKS);
		} else {
			service.putExtra(TasksManagerService.EXTRA_ACTION,
					TasksManagerService.ACTION_UPDATE_COMPLETED_TASKS);
		}
		getActivity().startService(service);
	}

	void openSettings() {
		Intent intent = new Intent(getActivity(), SettingsActivity.class);
		startActivity(intent);
	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		Loader<Cursor> loader = null;
		String[] projection = { TasksContract.TasksTable._ID,
				TasksContract.TasksTable.TASK_ID,
				TasksContract.TasksTable.REGISTRATION_TIME,
				TasksContract.TasksTable.ESTIMATED_PROCESSING_TIME,
				TasksContract.TasksTable.STATUS,
				TasksContract.TasksTable.FILES_COUNT };

		String[] selectionArgs = { getActivity().getString(
				R.string.status_completed) };

		String sortOrder = null;

		switch (id) {
		case LOADER_ACTIVE_TASKS:
			String activeSelection = TasksContract.TasksTable.STATUS + "!=?";

			loader = new CursorLoader(getActivity(),
					TasksContract.CONTENT_TASKS, projection, activeSelection,
					selectionArgs, sortOrder);
			break;
		case LOADER_COMPLETED_TASKS:
			String completedSelection = TasksContract.TasksTable.STATUS + "=?";

			loader = new CursorLoader(getActivity(),
					TasksContract.CONTENT_TASKS, projection,
					completedSelection, selectionArgs, sortOrder);
			break;
		}
		return loader;
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
		mAdapter.swapCursor(cursor);
		switch (loader.getId()) {
		case LOADER_ACTIVE_TASKS:
			if (cursor.getCount() == 0) {
				cancelNotification();
			}
		}
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		mAdapter.swapCursor(null);
	}

	protected void cancelNotification() {
		NotificationManager nm = (NotificationManager) getActivity()
				.getSystemService(Activity.NOTIFICATION_SERVICE);
		nm.cancel(TasksManagerService.NOTIFICATION_TAG,
				TasksManagerService.NOTIFICATION_ID);
	}

	class TasksAdapter extends SimpleCursorAdapter {
		boolean isActive;

		public TasksAdapter(Context context, int layout, Cursor c,
				String[] from, int[] to, int flags, boolean isActive) {
			super(context, layout, c, from, to, flags);
			this.isActive = isActive;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View row = convertView;
			if (row == null) {
				LayoutInflater inflater = (LayoutInflater) getActivity()
						.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
				if (isActive) {
					row = inflater
							.inflate(R.layout.active_entry, parent, false);
				} else {
					row = inflater.inflate(R.layout.completed_entry, parent,
							false);
				}
			}
			ImageView button = (ImageView) row
					.findViewById(R.id.task_list_item_delete_task);
			final TextView taskIdView = (TextView) row
					.findViewById(R.id.task_list_item_task_id);

			button.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					removeTask(taskIdView.getText().toString());
				}
			});
			return super.getView(position, row, parent);
		}
	}
}
