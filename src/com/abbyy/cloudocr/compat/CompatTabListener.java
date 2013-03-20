package com.abbyy.cloudocr.compat;

import android.support.v4.app.FragmentTransaction;

/**
 * This interface is made to allow the compat tabs to behave transparently
 * independent from their internal implementations.
 * 
 */
public interface CompatTabListener {

	public void onTabUnselected(CompatTab mLastTab, FragmentTransaction ft);

	public void onTabSelected(CompatTab newTab, FragmentTransaction ft);

	public void onTabReselected(CompatTab newTab, FragmentTransaction ft);

}
