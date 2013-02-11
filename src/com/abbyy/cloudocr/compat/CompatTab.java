package com.abbyy.cloudocr.compat;

import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;

public abstract class CompatTab {
	final TabCompatActivity mActivity;
	final String mTag;

	protected CompatTab(TabCompatActivity activity, String tag){
		mActivity = activity;
		mTag = tag;
	}
	public abstract CompatTab setText(int resId);
	public abstract CompatTab setIcon(int resId);
	public abstract CompatTab setFragment(Fragment fragment);
	public abstract CompatTab setTabListener(CompatTabListener callback);
	
	public abstract Object getTab();
	
	public abstract CharSequence getText();
	public abstract Drawable getIcon();
	public abstract Fragment getFragment();
	public abstract CompatTabListener getCallback();
	
	public String getTag() {
        return mTag;
    }
}
