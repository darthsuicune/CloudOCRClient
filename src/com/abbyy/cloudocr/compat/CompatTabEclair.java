package com.abbyy.cloudocr.compat;

import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;

/**
 * Concrete implementation of the abstraction layer for the tabs. It implements
 * a tab for Eclair (2.1) until Gingerbread (2.3.*).
 * 
 * It is a very basic implementation which holds all the important things used
 * by the tabs. Contains the setters and getters.
 * 
 */
public class CompatTabEclair extends CompatTab {
	private CharSequence mText;
	private Drawable mIcon;
	private Fragment mFragment;
	private CompatTabListener mCallback;

	protected CompatTabEclair(TabCompatActivity activity, String tag) {
		super(activity, tag);
	}

	@Override
	public CompatTab setText(int resId) {
		mText = mActivity.getResources().getText(resId);
		return this;
	}

	@Override
	public CompatTab setIcon(int resId) {
		mIcon = mActivity.getResources().getDrawable(resId);
		return this;
	}

	@Override
	public CompatTab setTabListener(CompatTabListener callback) {
		mCallback = callback;
		return this;
	}

	@Override
	public CompatTab getTab() {
		return this;
	}

	@Override
	public CharSequence getText() {
		return this.mText;
	}

	@Override
	public Drawable getIcon() {
		return this.mIcon;
	}

	@Override
	public CompatTabListener getCallback() {
		return this.mCallback;
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
