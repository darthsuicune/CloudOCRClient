package com.abbyy.cloudocr.compat;

import java.util.ArrayList;

import android.os.Build;
import android.os.Bundle;

public abstract class TabHelper {
	protected TabCompatActivity mActivity;
	protected ArrayList<CompatTab> mTabList;
	
	protected TabHelper(TabCompatActivity activity){
		mActivity = activity;
		mTabList = new ArrayList<CompatTab>();
	}
	
	public static TabHelper createInstance(TabCompatActivity activity) {
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
	
	public abstract void setActiveTab(int position);
	
	protected abstract void setUp();
	
	protected abstract void onSaveInstanceState(Bundle outState);

    protected abstract void onRestoreInstanceState(Bundle savedInstanceState);
}
