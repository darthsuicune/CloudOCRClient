package com.abbyy.cloudocr.compat;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;

public abstract class CompatTab {
	final Activity mActivity;
	final String mTag;

	protected CompatTab(Activity activity, String tag){
		mActivity = activity;
		mTag = tag;
	}
	public abstract CompatTab setText(int resId);
	public abstract CompatTab setIcon(int resId);
	public abstract CompatTab setTabListener(CompatTabListener callback);
	public abstract CompatTab setFragment(Fragment fragment);
	
	public abstract Object getTab();
	
	public abstract CharSequence getText();
	public abstract Drawable getIcon();
	public abstract CompatTabListener getCallback();
	public abstract Fragment getFragment();
	
	public String getTag() {
        return mTag;
    }
}
