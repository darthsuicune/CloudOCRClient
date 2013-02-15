package com.abbyy.cloudocr;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import com.abbyy.cloudocr.compat.ActionBarActivity;
import com.abbyy.cloudocr.fragments.TaskDetailsFragment;

public class TaskDetailsActivity extends ActionBarActivity {
	public static final String EXTRA_TASK_ID = "taskId";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		//In landscape mode, this should be handled by the MainActivity
		
		if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
			finish();
		}
		
		setContentView(R.layout.task_details_activity);
		
		TaskDetailsFragment fragment = new TaskDetailsFragment();
		Bundle extras = getIntent().getExtras();
		if(extras != null){
			fragment.setArguments(extras);
			fragment.setHasOptionsMenu(true);
			FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
			ft.replace(R.id.task_details_container, fragment);
			ft.commit();
		}
	}
}
