package com.abbyy.cloudocr.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.abbyy.cloudocr.R;
import com.abbyy.cloudocr.StartActivity;
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
		loadTasks(true);
		
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
			break;
		case R.id.menu_settings:
			openSettings();
			break;
		default:
			break;
		}
		return true;
	}

	@Override
	void setAdapter() {
		String[] from = { TasksContract.TasksTable.TASK_ID,
				TasksContract.TasksTable.REGISTRATION_TIME, };
		int[] to = { R.id.task_list_item_task_id,
				R.id.task_list_item_registration_time };

		mAdapter = new TasksAdapter(getActivity(), R.layout.completed_entry,
				null, from, to, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);

		setListAdapter(mAdapter);

	}

	// TODO
	private void launchNewTask() {
		if (isLandscape) {

		} else {
			Intent intent = new Intent(getActivity(), StartActivity.class);
			startActivity(intent);
		}
	}
}
