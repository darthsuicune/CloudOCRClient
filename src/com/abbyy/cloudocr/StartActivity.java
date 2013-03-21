package com.abbyy.cloudocr;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;

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

	@SuppressLint("NewApi")
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
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}

	/**
	 * We inflate the general activity menu, then pass it to the next receiver
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.start, menu);
		return super.onCreateOptionsMenu(menu);
	}

	/**
	 * The home button (logo) will be used on normal navigation through the
	 * activity. It will return up from the navigation list to the start
	 * activity class.
	 * 
	 * If it is handled, it returns true. If it is not, it passes the signal to
	 * further listeners
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			NavUtils.navigateUpTo(this, new Intent(this, StartActivity.class));
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
