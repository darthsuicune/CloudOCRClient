package com.abbyy.cloudocr.fragments;

import java.net.MalformedURLException;

import android.app.Activity;
import android.content.Context;
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.abbyy.cloudocr.CloudClient;
import com.abbyy.cloudocr.ConnectionLoader;
import com.abbyy.cloudocr.R;
import com.abbyy.cloudocr.SettingsActivity;
import com.abbyy.cloudocr.Task;
import com.abbyy.cloudocr.TaskDetailsActivity;
import com.abbyy.cloudocr.database.TasksContract;

public abstract class TasksFragment extends ListFragment {
	private final static int LOADER_ACTIVE_TASK = 1;
	private final static int LOADER_COMPLETED_TASK = 2;
	private final static int LOADER_ACTIVE_DOWNLOAD = 3;
	private final static int LOADER_COMPLETED_DOWNLOAD = 4;
	
	abstract void setAdapter();

	boolean isLandscape;

	protected CursorAdapter mAdapter;
	
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
			 Intent intent = new Intent(getActivity(),
			 TaskDetailsActivity.class);
			 intent.putExtra(TaskDetailsActivity.EXTRA_TASK_ID, taskId);
			 startActivity(intent);
		}
	}

	void loadTasks(boolean active) {
		if (active) {
			getActivity().getSupportLoaderManager().initLoader(LOADER_ACTIVE_TASK,
					null, new CursorLoaderHelper());
		} else {
			getActivity().getSupportLoaderManager().initLoader(
					LOADER_COMPLETED_TASK, null, new CursorLoaderHelper());
		}

	}

	void downloadTasks(boolean active) {
		if (active) {
			getActivity().getSupportLoaderManager().restartLoader(
					LOADER_ACTIVE_DOWNLOAD, null, new ConnectionHelper());
		} else {
			getActivity().getSupportLoaderManager().restartLoader(
					LOADER_COMPLETED_DOWNLOAD, null, new ConnectionHelper());
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

			String[] selectionArgs = { getActivity().getString(
					R.string.status_completed) };

			String sortOrder = null;

			switch (id) {
			case LOADER_ACTIVE_TASK:
				String activeSelection = TasksContract.TasksTable.STATUS
						+ "!=?";

				loader = new CursorLoader(getActivity(),
						TasksContract.CONTENT_TASKS, projection,
						activeSelection, selectionArgs, sortOrder);
				break;
			case LOADER_COMPLETED_TASK:
				String completedSelection = TasksContract.TasksTable.STATUS
						+ "=?";

				loader = new CursorLoader(getActivity(),
						TasksContract.CONTENT_TASKS, projection,
						completedSelection, selectionArgs, sortOrder);
				break;
			}
			return loader;
		}

		@Override
		public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
			switch (loader.getId()) {
			case LOADER_ACTIVE_TASK:
				mAdapter.swapCursor(cursor);
				break;
			case LOADER_COMPLETED_TASK:
				mAdapter.swapCursor(cursor);
				break;
			default:
				break;
			}
		}

		@Override
		public void onLoaderReset(Loader<Cursor> loader) {
			mAdapter.swapCursor(null);
		}

	}

	public class ConnectionHelper implements LoaderCallbacks<Task> {
		@Override
		public Loader<Task> onCreateLoader(int id, Bundle args) {
			String url = "http://cloud.ocrsdk.com/getTaskList";
			try {
				switch (id) {
				case LOADER_ACTIVE_DOWNLOAD:
					break;
				case LOADER_COMPLETED_DOWNLOAD:
					break;
				}
				return new ConnectionLoader(getActivity(), new CloudClient(
						getActivity(), url));
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		public void onLoadFinished(Loader<Task> loader, Task response) {
		}

		@Override
		public void onLoaderReset(Loader<Task> loader) {
		}
	}

	class TasksAdapter extends SimpleCursorAdapter {

		public TasksAdapter(Context context, int layout, Cursor c,
				String[] from, int[] to, int flags) {
			super(context, layout, c, from, to, flags);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View row = convertView;
			if (row == null) {
				LayoutInflater inflater = (LayoutInflater) getActivity()
						.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
				row = inflater.inflate(R.layout.completed_entry, parent, false);
			}
			ImageView button = (ImageView) row
					.findViewById(R.id.task_list_item_delete_task);
			final TextView taskIdView = (TextView) row.findViewById(R.id.task_list_item_task_id);

			button.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					removeTaskFromList(taskIdView.getText().toString());
				}
			});
			return super.getView(position, row, parent);
		}
	}
}
