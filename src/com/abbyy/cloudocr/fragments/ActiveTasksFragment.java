package com.abbyy.cloudocr.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.abbyy.cloudocr.CreateTaskActivity;
import com.abbyy.cloudocr.R;
import com.abbyy.cloudocr.SettingsActivity;
import com.abbyy.cloudocr.StartActivity;
import com.abbyy.cloudocr.TasksManagerService;
import com.abbyy.cloudocr.database.TasksContract;

public class ActiveTasksFragment extends TasksFragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.active_tasks_fragment, container,
				false);
		v.findViewById(R.id.create_new_task).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						launchNewTask();
					}
				});
		return v;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		getActivity().getSupportLoaderManager().restartLoader(
				LOADER_ACTIVE_TASKS, null, this);
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.active_tasks, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_refresh:
			downloadTasks(true);
			return true;
		case R.id.menu_settings:
			openSettings();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	void setAdapter() {
		String[] from = { TasksContract.TasksTable.TASK_ID,
				TasksContract.TasksTable.ESTIMATED_PROCESSING_TIME };
		int[] to = { R.id.task_list_item_task_id,
				R.id.task_list_item_estimated_time };

		mAdapter = new TasksAdapter(getActivity(), R.layout.completed_entry,
				null, from, to, 0, true);

		setListAdapter(mAdapter);

	}

	private void launchNewTask() {
		if (isLandscape) {
			ChooseTaskFragment fragment = new ChooseTaskFragment();
			FragmentTransaction transaction = getActivity()
					.getSupportFragmentManager().beginTransaction();
			transaction.disallowAddToBackStack();
			transaction.replace(R.id.main_activity_second_fragment,
					fragment);
			transaction.commit();
		} else {
			Intent intent = new Intent(getActivity(), StartActivity.class);
			intent.putExtra(CreateTaskActivity.EXTRA_PROCESS_MODE,
					SettingsActivity.PROCESSING_MODE_IMAGE);
			startActivity(intent);
		}
	}

	@Override
	void removeTask(String taskId) {
		Intent service = new Intent(getActivity(), TasksManagerService.class);
		service.putExtra(TasksManagerService.EXTRA_ACTION,
				TasksManagerService.ACTION_DELETE_ACTIVE_TASK);
		service.putExtra(TasksManagerService.EXTRA_TASK_ID, taskId);
		getActivity().startService(service);
	}
}
