package com.abbyy.cloudocr;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import com.abbyy.cloudocr.compat.ActionBarActivity;
import com.abbyy.cloudocr.fragments.TaskDetailsFragment;

public class TaskDetailsActivity extends ActionBarActivity {
	public static final String EXTRA_TASK_ID = "taskId";
	TaskDetailsFragment mFragment;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		//In landscape mode, this should be handled by the MainActivity
		
		if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
			finish();
		}
		
		setContentView(R.layout.task_details_activity);
		
		mFragment = new TaskDetailsFragment();
		Bundle extras = getIntent().getExtras();
		if(extras != null){
			mFragment.setArguments(extras);
			mFragment.setHasOptionsMenu(true);
			FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
			ft.replace(R.id.task_details_container, mFragment);
			ft.commit();
		}
	}

	@Override
	protected void onStop() {
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		ft.remove(mFragment);
		ft.commit();
		super.onStop();
	}
}
