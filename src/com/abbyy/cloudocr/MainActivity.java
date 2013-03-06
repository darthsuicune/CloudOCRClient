package com.abbyy.cloudocr;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.widget.Toast;

import com.abbyy.cloudocr.compat.CompatTab;
import com.abbyy.cloudocr.compat.CompatTabListener;
import com.abbyy.cloudocr.compat.TabCompatActivity;
import com.abbyy.cloudocr.compat.TabHelper;
import com.abbyy.cloudocr.database.TasksContract;
import com.abbyy.cloudocr.fragments.ActiveTasksFragment;
import com.abbyy.cloudocr.fragments.CompletedTasksFragment;

public class MainActivity extends TabCompatActivity{
	private static final int TAB_ACTIVE = 0;
	private static final int TAB_COMPLETED = 1;
	
	private SharedPreferences prefs;
	
	private boolean mTwoPane;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if(findViewById(R.id.main_activity_second_fragment) != null){
			mTwoPane = true;
		}
		prefs = PreferenceManager.getDefaultSharedPreferences(this);
		if(prefs.getBoolean(SettingsActivity.IS_FIRST_RUN, true)){
			insertLanguages();
			prefs.edit().putBoolean(SettingsActivity.IS_FIRST_RUN, false).commit();
		}
		setContentView(R.layout.main_activity);
		setTabs();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return true;
	}

	private void setTabs() {
		TabHelper tabHelper = getTabHelper();
		
		int defaultTab = prefs.getInt(SettingsActivity.DEFAULT_TAB,
				TAB_ACTIVE);

		createTab(tabHelper, getString(R.string.tab_active_tasks),
				R.string.tab_active_tasks, new TabListener(this,
						ActiveTasksFragment.class));

		createTab(tabHelper, getString(R.string.tab_completed_tasks),
				R.string.tab_completed_tasks, new TabListener(this,
						CompletedTasksFragment.class));

		tabHelper.setActiveTab(defaultTab);
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

		prefs.edit().putInt(SettingsActivity.DEFAULT_TAB, currentTab).commit();
	}

	private void insertLanguages() {
		Toast.makeText(this, R.string.first_run_message, Toast.LENGTH_SHORT).show();
		FirstRunTask task = new FirstRunTask();
		task.execute(null, null);
	}
	
	public class FirstRunTask extends AsyncTask<Void, Void, Void>{

		@Override
		protected Void doInBackground(Void... params) {
			ContentResolver cr = getContentResolver();
			String[] languagesList = getResources().getStringArray(R.array.languages);
			for(int i = 0; i < languagesList.length; i++){
				ContentValues values = new ContentValues();
				values.put(TasksContract.LanguagesTable.LANGUAGE, languagesList[i]);
				cr.insert(TasksContract.CONTENT_LANGUAGES, values);
			}
			return null;
		}
		
	}
}