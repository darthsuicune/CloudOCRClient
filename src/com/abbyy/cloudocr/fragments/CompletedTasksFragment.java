package com.abbyy.cloudocr.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.abbyy.cloudocr.R;
import com.abbyy.cloudocr.TasksManagerService;
import com.abbyy.cloudocr.database.TasksContract;

/**
 * Concrete implementation of the abstract Tasks Fragment for the Completed
 * tasks. It will basically load the required tasks.
 */
public class CompletedTasksFragment extends TasksFragment {

	/**
	 * Method called when the view for the fragment its first created. If the
	 * container is null, the fragment is not in the current activity so we can
	 * just return a null view to save memory.
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (container == null) {
			return null;
		}
		return inflater.inflate(R.layout.completed_tasks_fragment, container,
				false);
	}

	/**
	 * Method called after onCreateView. Initializes the list with the contents
	 * from the database.
	 */
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		downloadTasks(false);
		getActivity().getSupportLoaderManager().restartLoader(
				LOADER_COMPLETED_TASKS, null, this);
	}

	/**
	 * Load the actions from the action bar / menu.
	 */
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.completed_tasks, menu);
	}

	/**
	 * Sets the actions to perform when a menu button / action bar button is
	 * clicked.
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_refresh:
			downloadTasks(false);
			return true;
		case R.id.completed_tasks_clean_list:
			removeFullList();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onResume() {
		getLoaderManager().restartLoader(LOADER_COMPLETED_TASKS, null, this);
		super.onResume();
	}

	/**
	 * Convenience method for deleting the whole list.
	 */
	private void removeFullList() {
		if (getListView() == null) {
			return;
		}
		for (int i = 0; i < getListView().getCount(); i++) {
			View v = getListAdapter().getView(i, null, null);
			String taskId = ((TextView) v
					.findViewById(R.id.task_list_item_task_id)).getText()
					.toString();
			removeTask(taskId);
		}
	}

	/**
	 * Create and set the adapter for the list. At the beginning is empty, which
	 * will be swapped later by the actual cursor when the loader call has
	 * ended.
	 */
	@Override
	void setAdapter() {
		String[] from = { TasksContract.TasksTable.TASK_ID,
				TasksContract.TasksTable.REGISTRATION_TIME, };
		int[] to = { R.id.task_list_item_task_id,
				R.id.task_list_item_registration_time };

		mAdapter = new TasksAdapter(getActivity(), R.layout.completed_entry,
				null, from, to, 0, false);

		setListAdapter(mAdapter);

	}

	/**
	 * Removes the task for a completed action.
	 * 
	 * TODO: isn't working actually... And I'm not sure it is that useful.
	 */
	@Override
	void removeTask(String taskId) {
		Intent service = new Intent(getActivity(), TasksManagerService.class);
		service.putExtra(TasksManagerService.EXTRA_ACTION,
				TasksManagerService.ACTION_DELETE_COMPLETED_TASK);
		service.putExtra(TasksManagerService.EXTRA_TASK_ID, taskId);
		getActivity().startService(service);
	}
}
