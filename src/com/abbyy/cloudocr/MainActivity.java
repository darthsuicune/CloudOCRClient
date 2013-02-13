package com.abbyy.cloudocr;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;

import com.abbyy.cloudocr.compat.CompatTab;
import com.abbyy.cloudocr.compat.CompatTabListener;
import com.abbyy.cloudocr.compat.TabCompatActivity;
import com.abbyy.cloudocr.compat.TabHelper;

public class MainActivity extends TabCompatActivity {
	private static final int TAB_ACTIVE = 0;
	private static final int TAB_COMPLETED = 1;

	private SharedPreferences prefs;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		prefs = PreferenceManager.getDefaultSharedPreferences(this);
		setContentView(R.layout.main_activity);
		setTabs();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return true;
	}

	private void setTabs() {
		TabHelper tabHelper = getTabHelper();

		createTab(tabHelper, getString(R.string.tab_active_tasks),
				R.string.tab_active_tasks, new TabListener(this,
						ActiveTasksFragment.class));

		createTab(tabHelper, getString(R.string.tab_completed_tasks),
				R.string.tab_completed_tasks, new TabListener(this,
						CompletedTasksFragment.class));

		tabHelper.setActiveTab(prefs.getInt(SettingsActivity.DEFAULT_TAB,
				TAB_ACTIVE));
	}

	private void createTab(TabHelper tabHelper, String tag, int textResourceId,
			TabListener listener) {

		CompatTab tab = tabHelper.newTab(tag);

		tab.setText(textResourceId);
		tab.setTabListener(listener);

		tabHelper.addTab(tab);
	}

	class TabListener implements CompatTabListener {
		private TabCompatActivity mActivity;
		private Class<? extends Fragment> mClass;

		protected TabListener(TabCompatActivity activity,
				Class<? extends Fragment> cls) {
			mActivity = activity;
			mClass = cls;
		}

		@Override
		public void onTabUnselected(CompatTab tab, FragmentTransaction ft) {
			Fragment fragment = tab.getFragment();
			if (fragment != null) {
				ft.detach(fragment);
			}
			fragment.setHasOptionsMenu(false);
		}

		@Override
		public void onTabSelected(CompatTab tab, FragmentTransaction ft) {
			Fragment fragment = tab.getFragment();
			if (fragment == null) {
				fragment = Fragment.instantiate(mActivity, mClass.getName());
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
		}

	}

	private void saveCurrentTabAsDefault(CompatTab tab) {
		int currentTab;
		if (tab.getTag().equals(getString(R.string.tab_active_tasks))) {
			currentTab = TAB_ACTIVE;
		} else {
			currentTab = TAB_COMPLETED;
		}

		prefs.edit().putInt(SettingsActivity.DEFAULT_TAB, currentTab);
	}
}