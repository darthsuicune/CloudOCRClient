package com.abbyy.cloudocr.compat;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;

public class CompatTabEclair extends CompatTab {
	private Activity mActivity;
	
	private CharSequence mText;
	private Drawable mIcon;
	private CompatTabListener mCallback;
	private Fragment mFragment;

	protected CompatTabEclair(Activity activity, String tag){
		super(activity, tag);
		mActivity = activity;
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
	public CompatTab setFragment(Fragment fragment) {
		mFragment = fragment;
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
	public Fragment getFragment() {
		return this.mFragment;
	}

}
