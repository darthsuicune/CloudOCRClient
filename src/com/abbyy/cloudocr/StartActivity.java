package com.abbyy.cloudocr;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import com.abbyy.cloudocr.compat.ActionBarActivity;
import com.abbyy.cloudocr.fragments.ChooseTaskFragment;

/**
 * This activity only shows the links to the different processing modes when
 * creating a new task. It is only a helper activity that will only be shown on
 * portrait mode and only on small screens.
 * 
 * @author Denis Lapuente
 * 
 */
public class StartActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.start_activity);

		// If an instance already existed, the fragment will also exist and will
		// be automatically attached
		if (savedInstanceState != null) {
			return;
		}

		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		ft.replace(R.id.start_fragment_container, new ChooseTaskFragment())
				.commit();
	}
}
