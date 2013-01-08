package com.abbyy.cloudocr.compat;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;

public abstract class TabHelper {
	private Activity mActivity;
	
	protected TabHelper(Activity activity){
		mActivity = activity;
	}
	
	public static TabHelper createInstance(ActionBarActivity activity) {
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){
			return new TabHelperHoneycomb(activity);
		} else {
			return new TabHelperEclair(activity);
		}
	}
	
	public CompatTab newTab(String tag) {
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){
			return new CompatTabHoneycomb(mActivity, tag);
		} else {
			return new CompatTabEclair(mActivity, tag);
		}
	}
	
	public abstract void addTab(CompatTab tab);
	
	protected abstract void setUp();
	
	protected abstract void onSaveInstanceState(Bundle outState);

    protected abstract void onRestoreInstanceState(Bundle savedInstanceState);
}
