package com.abbyy.cloudocr;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.abbyy.cloudocr.compat.CompatTab;
import com.abbyy.cloudocr.compat.CompatTabListener;
import com.abbyy.cloudocr.compat.TabCompatActivity;
import com.abbyy.cloudocr.compat.TabHelper;
import com.abbyy.cloudocr.fragments.ActiveTasksFragment;
import com.abbyy.cloudocr.fragments.CompletedTasksFragment;

/**
 * Main entry point for the application. It has two tabs, showing the list of
 * tasks active and already completed. By default it remembers the last tab
 * used.
 * 
 * It uses a compattab activity in order to provide tabs for both pre and
 * post-honeycomb implementations.
 * 
 * TODO: Add setting to change the behaviour and show always one of the tabs.
 * 
 * @author Denis Lapuente
 * 
 */
public class MainActivity extends TabCompatActivity {
	// Tabs used in the application
	private static final int TAB_ACTIVE = 0;
	private static final int TAB_COMPLETED = 1;

	private SharedPreferences prefs;

	/**
	 * On the first run we launch a task for inserting the languages into the
	 * database. Then we just set the layout and prepare the tabs for use.
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		prefs = PreferenceManager.getDefaultSharedPreferences(this);
		if (prefs.getBoolean(SettingsActivity.IS_FIRST_RUN, true)) {
			if (makeFirstRun()) {
				prefs.edit().putBoolean(SettingsActivity.IS_FIRST_RUN, false)
						.commit();
			}
		}
		setContentView(R.layout.main_activity);
		setTabs();
	}

	/**
	 * We create the options menu and pass the call to the next receivers
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
	
		return super.onCreateOptionsMenu(menu);
	}

	/**
	 * We manage the related activity options
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_settings:
			Intent intent = new Intent(this, SettingsActivity.class);
			startActivity(intent);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	/**
	 * Convenience method for adding the tabs.
	 */
	private void setTabs() {
		TabHelper tabHelper = getTabHelper();

		int defaultTab = prefs.getInt(SettingsActivity.DEFAULT_TAB, TAB_ACTIVE);

		createTab(tabHelper, R.string.tab_active_tasks, new TabListener(this,
				ActiveTasksFragment.class));

		createTab(tabHelper, R.string.tab_completed_tasks, new TabListener(
				this, CompletedTasksFragment.class));

		tabHelper.setActiveTab(defaultTab);
	}

	/**
	 * Convenience method for creating each tab
	 * 
	 * @param tabHelper
	 *            The tab helper that will manage the tabs independent of the
	 *            implementation
	 * @param textResourceId
	 *            Text Resource Id to be used on the tab and tag
	 * @param listener
	 *            Listener of the tab, that will act when we change tabs.
	 */
	private void createTab(TabHelper tabHelper, int textResourceId,
			TabListener listener) {

		CompatTab tab = tabHelper.newTab(getString(textResourceId));

		tab.setText(textResourceId);
		tab.setTabListener(listener);

		tabHelper.addTab(tab);
	}

	/**
	 * Implementation of the tab listener. It allows for creation of tabs that
	 * switch fragments dynamically.
	 * 
	 * @author Denis Lapuente
	 * 
	 */
	class TabListener implements CompatTabListener {
		private TabCompatActivity mActivity;
		private Class<? extends Fragment> mFragment;

		/**
		 * The creation of the listener requires the activity we are creating
		 * the tabs for and the fragment it will implement
		 * 
		 * @param activity
		 *            The activity host for the tabs
		 * @param fragment
		 *            The fragment we are associating to the tab
		 */
		protected TabListener(TabCompatActivity activity,
				Class<? extends Fragment> fragment) {
			mActivity = activity;
			mFragment = fragment;
		}

		/**
		 * When the tab is unselected, we detach the fragment from the activity.
		 * We also remove the options menu from the fragment.
		 * 
		 * WARNING! The commit is done after this is called, so there is no need
		 * to do it here
		 */
		@Override
		public void onTabUnselected(CompatTab tab, FragmentTransaction ft) {
			Fragment fragment = tab.getFragment();
			if (fragment != null) {
				ft.detach(fragment);
				fragment.setHasOptionsMenu(false);
			}
		}

		/**
		 * When the tab is selected, if it had previously a fragment, we just
		 * add the fragment. If it did not have a fragment, we create a new one
		 * and add it. We also set the options menu for the fragment.
		 * 
		 * In case the option is active (active by default), we set the new tab
		 * as the default one
		 */
		@Override
		public void onTabSelected(CompatTab tab, FragmentTransaction ft) {
			Fragment fragment = tab.getFragment();
			if (fragment == null) {
				fragment = Fragment.instantiate(mActivity, mFragment.getName());
				tab.setFragment(fragment);
				ft.add(android.R.id.tabcontent, fragment, tab.getTag());
			} else {
				ft.attach(fragment);
			}
			fragment.setHasOptionsMenu(true);

			if (prefs.getBoolean(SettingsActivity.PREFERENCE_SAVE_TAB_DEFAULT,
					true)) {
				saveCurrentTabAsDefault(tab);
			}
		}

		@Override
		public void onTabReselected(CompatTab tab, FragmentTransaction ft) {
			// Nothing to do here.
		}

	}

	/**
	 * Self-explanatory. Saves the current tab as the default tab in the options
	 * 
	 * @param tab
	 */
	private void saveCurrentTabAsDefault(CompatTab tab) {
		int currentTab;
		if (tab.getTag().equals(getString(R.string.tab_active_tasks))) {
			currentTab = TAB_ACTIVE;
		} else {
			currentTab = TAB_COMPLETED;
		}

		prefs.edit().putInt(SettingsActivity.DEFAULT_TAB, currentTab).commit();
	}

	/**
	 * Convenience method for just displaying a message. Any other action to do
	 * on the first run should be performed here.
	 * 
	 * @return true if the actions were performed correctly. false otherwise
	 */
	private boolean makeFirstRun() {
		Toast.makeText(this, R.string.first_run_message, Toast.LENGTH_SHORT)
				.show();
		return true;
	}
}