package com.abbyy.cloudocr;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuItem;

import com.abbyy.cloudocr.compat.ActionBarActivity;
import com.abbyy.cloudocr.fragments.TaskDetailsFragment;

/**
 * Activity that shows the details of a task after being clicked on the tasks
 * lists. It will just show the fragment when needed.
 * 
 * REQUIRES: EXTRA_TASK_ID, as the taskId to show.
 * 
 * @author Denis Lapuente
 * 
 */
public class TaskDetailsActivity extends ActionBarActivity {
	// This Activity requires the Task ID as an extra.
	public static final String EXTRA_TASK_ID = "taskId";

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.task_details_activity);

		Bundle extras = getIntent().getExtras();
		if (extras != null && savedInstanceState == null) {
			//Fragment that will contain the information
			TaskDetailsFragment fragment;
			fragment = new TaskDetailsFragment();
			fragment.setArguments(extras);
			fragment.setHasOptionsMenu(true);
			
			//In newer versions we activate the Home as Up action.
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
				getActionBar().setDisplayHomeAsUpEnabled(true);
			}
			FragmentTransaction ft = getSupportFragmentManager()
					.beginTransaction();
			ft.replace(R.id.task_details_container, fragment);
			ft.commit();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.task_details, menu);
		return super.onCreateOptionsMenu(menu);
	}

	/**
	 * Manage the options selected on the action bar / menu
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		// Represents the logo icon. Upon press, would return to previous
		// activity/view. Only used on portrait.
		case android.R.id.home:
			finish();
			return true;
		case R.id.menu_settings:
			Intent intent = new Intent(this, SettingsActivity.class);
			startActivity(intent);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
}
