package com.abbyy.cloudocr.compat;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.app.Fragment;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class CompatTabHoneycomb extends CompatTab implements ActionBar.TabListener{
	ActionBar.Tab mTab;
	Fragment mFragment;
	
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
	public CompatTabListener getCallback() {
		return null;
		//TODO
	}

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		
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
}
