package com.abbyy.cloudocr.compat;

import android.os.Bundle;

/**
 * This activity will be used for tab compatibility. Contains a tab helper that
 * allows for using a tab helper independently of the internal implementation.
 * 
 */
public class TabCompatActivity extends ActionBarActivity {

	private TabHelper mTabHelper;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mTabHelper = TabHelper.createInstance(this);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		mTabHelper.onSaveInstanceState(outState);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		mTabHelper.onRestoreInstanceState(savedInstanceState);
	}

	/**
	 * Returns the {@link TabHelper} for this activity.
	 */
	protected TabHelper getTabHelper() {
		mTabHelper.setUp();
		return mTabHelper;
	}
}
