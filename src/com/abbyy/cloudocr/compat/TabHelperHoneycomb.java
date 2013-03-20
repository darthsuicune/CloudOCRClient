package com.abbyy.cloudocr.compat;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

/**
 * Implementation of the tab helper for honeycomb. As everything is now managed
 * by the action bar, it is quite straightforward.
 * 
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class TabHelperHoneycomb extends TabHelper {
	ActionBar mActionBar;

	protected TabHelperHoneycomb(TabCompatActivity activity) {
		super(activity);
	}

	/**
	 * Upon set up we set the action bar and activate the tabs navigation mode.
	 */
	@Override
	protected void setUp() {
		if (mActionBar == null) {
			mActionBar = mActivity.getActionBar();
			mActionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		}
	}

	/**
	 * Implementation on how to add the tab. We make sure that the fragment is
	 * not attached as we don't want it attached as soon as we add the tab.
	 */
	@Override
	public void addTab(CompatTab tab) {
		Fragment fragment = mActivity.getSupportFragmentManager()
				.findFragmentByTag(tab.getTag());
		if (fragment != null && !fragment.isDetached()) {
			FragmentTransaction ft = mActivity.getSupportFragmentManager()
					.beginTransaction();
			ft.detach(fragment);
			ft.commit();
		}
		//Finally add the tab to the list and to the action bar.
		mActionBar.addTab((ActionBar.Tab) tab.getTab());
		mTabList.add(tab);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		int position = mActionBar.getSelectedTab().getPosition();
		outState.putInt("tab_position", position);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		int position = savedInstanceState.getInt("tab_position");
		mActionBar.setSelectedNavigationItem(position);
	}

	@Override
	public void setActiveTab(int position) {
		if (mActionBar != null) {
			mActionBar.setSelectedNavigationItem(position);
		}
	}
}
