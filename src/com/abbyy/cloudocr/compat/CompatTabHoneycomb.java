package com.abbyy.cloudocr.compat;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.app.Fragment;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class CompatTabHoneycomb extends CompatTab {
	ActionBar.Tab mTab;
	
	protected CompatTabHoneycomb(Activity activity, String tag){
		super(activity, tag);
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
	public CompatTab setFragment(Fragment fragment) {
		//TODO
		return this;
	}

	@Override
	public CompatTab setTabListener(CompatTabListener callback) {
//		mTab.setTabListener(callback);
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
	
	public ActionBar.Tab getTab(){
		return mTab;
	}

	@Override
	public Fragment getFragment() {
		//TODO
		return null;
	}

	@Override
	public CompatTabListener getCallback() {
		return null;
		//TODO
	}

}
