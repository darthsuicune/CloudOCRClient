package com.abbyy.cloudocr.compat;

import android.support.v4.app.FragmentTransaction;

public interface CompatTabListener {

	public void onTabUnselected(CompatTab mLastTab, FragmentTransaction ft);

	public void onTabSelected(CompatTab newTab, FragmentTransaction ft);

	public void onTabReselected(CompatTab newTab, FragmentTransaction ft);

}
