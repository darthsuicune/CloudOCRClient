package com.abbyy.cloudocr.fragments;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentTransaction;
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

import com.abbyy.cloudocr.R;
import com.abbyy.cloudocr.SettingsActivity;
import com.abbyy.cloudocr.TaskDetailsActivity;
import com.abbyy.cloudocr.TasksManagerService;
import com.abbyy.cloudocr.database.TasksContract;

/**
 * Abstract class for the fragments that include a list of tasks. Includes the
 * implementation of most methods, which are common to all of them.
 * 
 * @author Denis Lapuente
 * 
 */
public abstract class TasksFragment extends ListFragment implements
		LoaderCallbacks<Cursor> {
	// The loaders we are using to load the tasks from the database
	protected static final int LOADER_ACTIVE_TASKS = 1;
	protected static final int LOADER_COMPLETED_TASKS = 2;

	// Abstract methods to be implemented by the child objects
	abstract void setAdapter();

	abstract void removeTask(String id);

	boolean isLandscape;

	SharedPreferences prefs;

	// Adapter for the list. Adapts the content of a cursor to the list format
	// as defined in the setAdapter() method
	TasksAdapter mAdapter;

	/**
	 * This is called when the activity is created or the fragment is added to
	 * the activity for the first time. We just create the landscape boolean and
	 * set the adapter for the list.
	 */
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
		PreferenceManager.setDefaultValues(getActivity(),
				R.xml.preference_fragment, false);
		super.onActivityCreated(savedInstanceState);
		isLandscape = getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
		setAdapter();
	}

	/**
	 * When we click on an item of the list, we show its details.
	 */
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		String taskId = ((TextView) v.findViewById(R.id.task_list_item_task_id))
				.getText().toString();
		showTaskDetails(taskId);
	}

	/**
	 * Convenience method for showing the details of a task, specified its ID.
	 * In portrait mode we will call a new activity. In landscape we will show
	 * it on a different fragment.
	 * 
	 * @param taskId
	 *            The id of the task to show.
	 */
	private void showTaskDetails(String taskId) {
		if (isLandscape) {
			TaskDetailsFragment fragment = new TaskDetailsFragment();
			Bundle args = new Bundle();
			args.putString(TaskDetailsActivity.EXTRA_TASK_ID, taskId);
			fragment.setArguments(args);
			fragment.setHasOptionsMenu(true);
			FragmentTransaction ft = getFragmentManager().beginTransaction();
			ft.replace(R.id.main_activity_second_fragment, fragment).commit();
		} else {
			Intent intent = new Intent(getActivity(), TaskDetailsActivity.class);
			intent.putExtra(TaskDetailsActivity.EXTRA_TASK_ID, taskId);
			startActivity(intent);
		}
	}

	/**
	 * Convenience method that launchs the update of the tasks. It calls the
	 * service, which should update the database. The loader updates the cursor
	 * automatically when the information in the database changes. The cursor
	 * updates the adapter which updates the list automatically.
	 * 
	 * @param active
	 */
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

	// Mandatory override methods for the loader callbacks.
	/**
	 * This method creates the loader to use by the loader manager. We ask to
	 * the database for the information we want to show on the projection, set
	 * the selection to the task ID passed as an argument in the args bundle and
	 * create the corresponding loader.
	 * 
	 * It orders the list by the last status change time.
	 */
	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		String[] projection = { TasksContract.TasksTable._ID,
				TasksContract.TasksTable.TASK_ID,
				TasksContract.TasksTable.REGISTRATION_TIME,
				TasksContract.TasksTable.ESTIMATED_PROCESSING_TIME,
				TasksContract.TasksTable.STATUS,
				TasksContract.TasksTable.FILES_COUNT };

		String sortOrder = TasksContract.TasksTable.STATUS_CHANGE_TIME
				+ " DESC";

		switch (id) {
		case LOADER_ACTIVE_TASKS:
			String activeSelection;
			if (prefs.getBoolean(
					SettingsActivity.PREFERENCE_SHOW_ONLY_FROM_DEVICE, true)) {
				activeSelection = TasksContract.TasksTable.STATUS + "!=?";
				String[] activeSelectionArgs = { getActivity().getString(
						R.string.status_completed) };
				return new CursorLoader(getActivity(),
						TasksContract.CONTENT_TASKS, projection,
						activeSelection, activeSelectionArgs, sortOrder);
			} else {
				activeSelection = TasksContract.TasksTable.STATUS + "!=? AND "
						+ TasksContract.TasksTable.FROM_DEVICE + "=1";
				String[] activeSelectionArgs = { getActivity().getString(
						R.string.status_completed) };
				return new CursorLoader(getActivity(),
						TasksContract.CONTENT_TASKS, projection,
						activeSelection, activeSelectionArgs, sortOrder);

			}
		case LOADER_COMPLETED_TASKS:
			String completedSelection;
			if (prefs.getBoolean(
					SettingsActivity.PREFERENCE_SHOW_ONLY_FROM_DEVICE, true)) {
				completedSelection = TasksContract.TasksTable.STATUS
						+ "=? AND " + TasksContract.TasksTable.FROM_DEVICE
						+ "=1";
				String[] completedSelectionArgs = { getActivity().getString(
						R.string.status_completed) };
				return new CursorLoader(getActivity(),
						TasksContract.CONTENT_TASKS, projection,
						completedSelection, completedSelectionArgs, sortOrder);
			} else {
				completedSelection = TasksContract.TasksTable.STATUS + "=?";
				String[] completedSelectionArgs = { getActivity().getString(
						R.string.status_completed) };
				return new CursorLoader(getActivity(),
						TasksContract.CONTENT_TASKS, projection,
						completedSelection, completedSelectionArgs, sortOrder);
			}

		default:
			return null;
		}
	}

	/**
	 * When the load is finished, both the first time and every single time the
	 * referenced data changes, this method is called. It will change the cursor
	 * in the adapter to reflect the changes. If we don't have any active task,
	 * we cancel automatically the notification being shown.
	 */
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

	/**
	 * When the cursor is no longer required, we set the adapter cursor to null
	 * to free system resources.
	 */
	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		mAdapter.swapCursor(null);
	}

	/**
	 * Convenience method for cancelling the working notification
	 */
	protected void cancelNotification() {
		NotificationManager nm = (NotificationManager) getActivity()
				.getSystemService(Activity.NOTIFICATION_SERVICE);
		nm.cancel(TasksManagerService.NOTIFICATION_TAG,
				TasksManagerService.TASKS_NOTIFICATION);
	}

	/**
	 * 
	 * Specific adapter to use on our lists. As we have a button that requires
	 * an action (currently only an image), we need to at least override the
	 * getView method to reflect this.
	 * 
	 * @author Denis Lapuente
	 * 
	 */
	class TasksAdapter extends SimpleCursorAdapter {
		boolean isActive;

		/**
		 * On the constructor we create a normal SimpleCursorAdapter and set a
		 * variable to see if this is the active tasks list or the completed
		 * tasks lists.
		 * 
		 * @param context
		 * @param layout
		 * @param c
		 * @param from
		 * @param to
		 * @param flags
		 * @param isActive
		 *            true if this is the Active tasks fragment. false if it is
		 *            the Completed tasks.
		 */
		public TasksAdapter(Context context, int layout, Cursor c,
				String[] from, int[] to, int flags, boolean isActive) {
			super(context, layout, c, from, to, flags);
			this.isActive = isActive;
		}

		/**
		 * This method is in charge of modifying each view from the list to add
		 * an action to the image.
		 */
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
