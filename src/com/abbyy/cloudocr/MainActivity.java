package com.abbyy.cloudocr;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;

import com.abbyy.cloudocr.compat.CompatTab;
import com.abbyy.cloudocr.compat.CompatTabListener;
import com.abbyy.cloudocr.compat.TabCompatActivity;
import com.abbyy.cloudocr.compat.TabHelper;

public class MainActivity extends TabCompatActivity {
	
	private TasksFragment mActiveTasksFragment;
	private TasksFragment mCompletedTasksFragment;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_activity);
		createFragments();
		setTabs();
	}
	
	private void createFragments(){
		mActiveTasksFragment = new ActiveTasksFragment();
		mCompletedTasksFragment = new CompletedTasksFragment();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return true;
	}

	private void setTabs() {
		TabHelper tabHelper = getTabHelper();
		
		CompatTab activeTasksTab = tabHelper
				.newTab(getString(R.string.tab_active_tasks));
		
		activeTasksTab.setFragment(mActiveTasksFragment);
		activeTasksTab.setText(R.string.tab_active_tasks);
		activeTasksTab.setTabListener(new TabListener(this, ActiveTasksFragment.class));
		
		CompatTab completedTasksTab = tabHelper
				.newTab(getString(R.string.tab_completed_tasks));
		
		completedTasksTab.setFragment(mCompletedTasksFragment);
		completedTasksTab.setText(R.string.tab_completed_tasks);
		completedTasksTab.setTabListener(new TabListener(this, CompletedTasksFragment.class));
		
		tabHelper.addTab(activeTasksTab);
		tabHelper.addTab(completedTasksTab);
	}
	
	class TabListener implements CompatTabListener {
		private TabCompatActivity mActivity;
		private Class<? extends Fragment> mClass;
		
		public TabListener(TabCompatActivity activity, Class<? extends Fragment> cls) {
            mActivity = activity;
            mClass = cls;
        }
		
		@Override
		public void onTabUnselected(CompatTab tab, FragmentTransaction ft) {
			tab.getFragment().setHasOptionsMenu(false);
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
            tab.getFragment().setHasOptionsMenu(true);
		}

		@Override
		public void onTabReselected(CompatTab tab, FragmentTransaction ft) {
		}
		
	}
}