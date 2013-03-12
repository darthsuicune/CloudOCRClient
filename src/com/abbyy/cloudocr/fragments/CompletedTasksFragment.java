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

public class CompletedTasksFragment extends TasksFragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.completed_tasks_fragment, container,
				false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		getActivity().getSupportLoaderManager().restartLoader(LOADER_COMPLETED_TASKS, null, this);
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.completed_tasks, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_refresh:
			downloadTasks(false);
			break;
		case R.id.completed_tasks_clean_list:
			removeFullList();
			break;
		case R.id.menu_settings:
			openSettings();
			break;
		default:
			break;
		}
		return true;
	}

	private void removeFullList() {
		for (int i = 0; i < getListView().getCount(); i++) {
			View v = getListAdapter().getView(i, null, null);
			String taskId = ((TextView) v.findViewById(R.id.task_list_item_task_id)).getText().toString();
			removeTask(taskId);
		}
	}

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

	@Override
	void removeTask(String taskId) {
		Intent service = new Intent(getActivity(), TasksManagerService.class);
		service.putExtra(TasksManagerService.EXTRA_ACTION,
				TasksManagerService.ACTION_DELETE_COMPLETED_TASK);
		service.putExtra(TasksManagerService.EXTRA_TASK_ID, taskId);
		getActivity().startService(service);
	}
}
