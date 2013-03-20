package com.abbyy.cloudocr.compat;

import java.util.ArrayList;

import android.os.Build;
import android.os.Bundle;

/**
 * Abstraction layer for the Tab Helper. It will provide a common interface for
 * the distinct implementations of the tab helpers for HC and GB
 * 
 */
public abstract class TabHelper {
	protected TabCompatActivity mActivity;
	protected ArrayList<CompatTab> mTabList;

	/**
	 * Build a tab helper that will manage the tabs. Only used through the
	 * "super" method.
	 * 
	 * WARNING! Do not use in code. Use createInstance(TabCompatActivity)
	 * instead.
	 * 
	 * @param activity
	 *            The activity which holds the tabs (and the tab helper)
	 * 
	 */
	protected TabHelper(TabCompatActivity activity) {
		mActivity = activity;
		mTabList = new ArrayList<CompatTab>();
	}

	/**
	 * The actual public use for building a TabHelper should be the
	 * createInstance method
	 * 
	 * @param activity
	 *            The Activity that holds the helper and the tabs
	 * @return A compatible tab helper for the version of the device
	 */
	public static TabHelper createInstance(TabCompatActivity activity) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			return new TabHelperHoneycomb(activity);
		} else {
			return new TabHelperEclair(activity);
		}
	}

	/**
	 * Method for creating a new tab. This should be used for creating a new
	 * tab, and it will decide which kind of tab the device requires.
	 * 
	 * WARNING! This method only creates the tab. In order to add it to the
	 * layout, you should call addTab(CompatTab)
	 * 
	 * @param tag
	 *            Tag for creating a new tab.
	 * @return A new compatible tab for the version of the device.
	 */
	public CompatTab newTab(String tag) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			return new CompatTabHoneycomb(mActivity, tag);
		} else {
			return new CompatTabEclair(mActivity, tag);
		}
	}

	/**
	 * Adds the tab to the layout defined by the version.
	 * 
	 * @param tab
	 *            The tab to add.
	 */
	public abstract void addTab(CompatTab tab);

	/**
	 * Sets the tab passed as a parameter as the active one. Not required for
	 * user clicks, but for automatic change of tabs (for example, on a fresh
	 * start through the options of the application)
	 * 
	 * @param position
	 *            The tab that should be made active.
	 */
	public abstract void setActiveTab(int position);

	/**
	 * Abstract method that needs to be implemented in order to set up the
	 * different types of tab helpers.
	 */
	protected abstract void setUp();

	protected abstract void onSaveInstanceState(Bundle outState);

	protected abstract void onRestoreInstanceState(Bundle savedInstanceState);
}
