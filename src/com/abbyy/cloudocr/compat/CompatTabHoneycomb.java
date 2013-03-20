package com.abbyy.cloudocr.compat;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

/**
 * Implementation of the Compatibility Tab for Honeycomb (3.*) and later
 * versions. It holds an actual Action Bar tab, its assigned fragment and a
 * compatibility listener. It needs to implement also the Action bar tab
 * listener in order to make it work with the action bar.
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class CompatTabHoneycomb extends CompatTab implements
		ActionBar.TabListener {
	ActionBar.Tab mTab;
	Fragment mFragment;
	CompatTabListener mCompatListener;

	protected CompatTabHoneycomb(TabCompatActivity activity, String tag) {
		super(activity, tag);

		// This constructor needs to set the Action bar tab as a true action bar
		// tab. In this way, we ensure compatibility with the best option for
		// post-HC devices.
		mTab = activity.getActionBar().newTab();
	}

	@Override
	public CompatTab setText(int resId) {
		mTab.setText(resId);
		return this;
	}

	@Override
	public CompatTab setIcon(int resId) {
		mTab.setIcon(resId);
		return this;
	}

	@Override
	public CompatTab setTabListener(CompatTabListener callback) {
		mCompatListener = callback;
		mTab.setTabListener(this);
		return this;
	}

	@Override
	public CharSequence getText() {
		return mTab.getText();
	}

	@Override
	public Drawable getIcon() {
		return mTab.getIcon();
	}

	public ActionBar.Tab getTab() {
		return mTab;
	}

	@Override
	public CompatTabListener getCallback() {
		return mCompatListener;
	}

	@Override
	public CompatTab setFragment(Fragment fragment) {
		mFragment = fragment;
		return this;
	}

	@Override
	public Fragment getFragment() {
		return mFragment;
	}

	@Override
	public void onTabSelected(Tab tab, android.app.FragmentTransaction ft) {

		FragmentTransaction transaction = mActivity.getSupportFragmentManager()
				.beginTransaction();

		transaction.disallowAddToBackStack();
		mCompatListener.onTabSelected(this, transaction);
		transaction.commit();
	}

	@Override
	public void onTabUnselected(Tab tab, android.app.FragmentTransaction ft) {

		FragmentTransaction transaction = mActivity.getSupportFragmentManager()
				.beginTransaction();

		transaction.disallowAddToBackStack();
		mCompatListener.onTabUnselected(this, transaction);
		transaction.commit();
	}

	@Override
	public void onTabReselected(Tab tab, android.app.FragmentTransaction ft) {

		FragmentTransaction transaction = mActivity.getSupportFragmentManager()
				.beginTransaction();
		transaction.disallowAddToBackStack();
		mCompatListener.onTabReselected(this, transaction);
		transaction.commit();
	}
}
