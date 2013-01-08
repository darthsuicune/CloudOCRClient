package com.abbyy.cloudocr.compat;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class TabHelperHoneycomb extends TabHelper {
	ActionBar mActionBar;
	Activity mActivity;
	
	protected TabHelperHoneycomb(Activity activity){
		super(activity);
		mActivity = activity;
		mActionBar = activity.getActionBar();
	}
	
	@Override
	protected void setUp() {
		if(mActionBar == null){
			mActionBar = mActivity.getActionBar();
			mActionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		}
	}

	@Override
	public void addTab(CompatTab tab) {
		mActionBar.addTab((ActionBar.Tab) tab.getTab());
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
}
