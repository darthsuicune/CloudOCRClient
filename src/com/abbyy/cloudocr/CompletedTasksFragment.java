package com.abbyy.cloudocr;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

public class CompletedTasksFragment extends TasksFragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.completed_tasks_fragment, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		loadTasks(false);
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
	
	private void removeFullList(){
		for(int i = 0; i < getListView().getCount(); i++){
			View v = (View) getListView().getItemAtPosition(i);
			String taskId = v.findViewById(0).toString(); // TODO
			this.removeTaskFromList(taskId);
		}
	}
}
