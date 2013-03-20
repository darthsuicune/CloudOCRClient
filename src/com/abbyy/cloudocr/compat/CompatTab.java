package com.abbyy.cloudocr.compat;

import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;

/**
 * Abstraction layer for the tab compatibility with pre-honeycomb and
 * post-honeycomb. Represents a Tab. Contains its tag, text, icon (optional),
 * the fragment that will be displayed and the listener to activate when it is
 * selected
 * 
 */
public abstract class CompatTab {
	final TabCompatActivity mActivity;
	final String mTag;

	/**
	 * The constructor receives a TabCompatActivity which will hold the tab and
	 * a tag to assign to the tab
	 * 
	 * @param activity
	 *            The activity where the tab will be held
	 * @param tag
	 *            The tag for the tab
	 */
	protected CompatTab(TabCompatActivity activity, String tag) {
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
