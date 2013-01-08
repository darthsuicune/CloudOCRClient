package com.abbyy.cloudocr.compat;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

public class TabHelperEclair extends TabHelper {
	private TabHost mTabHost;
	private Activity mActivity;

	protected TabHelperEclair(Activity activity){
		super(activity);
		mActivity = activity;
	}
	
	@Override
	protected void setUp() {
		if (mTabHost == null) {
			mTabHost = (TabHost) mActivity.findViewById(android.R.id.tabhost);
			mTabHost.setup();
		}
	}

	@Override
	public void addTab(CompatTab tab) {
		TabSpec spec = mTabHost.newTabSpec(tab.getText().toString()).setIndicator(tab.getText());
		mTabHost.addTab(spec);
	}

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // Save and restore the selected tab for rotations/restarts.
        outState.putString("tab", mTabHost.getCurrentTabTag());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            mTabHost.setCurrentTabByTag(savedInstanceState.getString("tab"));
        }
    }

}
