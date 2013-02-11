package com.abbyy.cloudocr.compat;

import java.util.HashMap;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.TabHost;
import android.widget.TabHost.TabContentFactory;
import android.widget.TabHost.TabSpec;

public class TabHelperEclair extends TabHelper implements
		TabHost.OnTabChangeListener {
	private HashMap<String, CompatTab> mTabList = new HashMap<String, CompatTab>();
	private TabHost mTabHost;
	private CompatTab mLastTab;

	protected TabHelperEclair(TabCompatActivity activity) {
		super(activity);
	}

	@Override
	protected void setUp() {
		if (mTabHost == null) {
			mTabHost = (TabHost) mActivity.findViewById(android.R.id.tabhost);
			mTabHost.setup();
			mTabHost.setOnTabChangedListener(this);
		}
	}

	@Override
	public void addTab(CompatTab tab) {
		String tag = tab.getTag();
        TabSpec spec;

        if (tab.getIcon() != null) {
            spec = mTabHost.newTabSpec(tag).setIndicator(tab.getText(), tab.getIcon());
        } else {
            spec = mTabHost.newTabSpec(tag).setIndicator(tab.getText());
        }

        spec.setContent(new TabChangeFactory(mActivity));

        // Check to see if we already have a fragment for this tab, probably
        // from a previously saved state.  If so, deactivate it, because our
        // initial state is that a tab isn't shown.

        Fragment fragment = mActivity.getSupportFragmentManager().findFragmentByTag(tag);
        tab.setFragment(fragment);

        if (fragment != null && !fragment.isDetached()) {
            FragmentTransaction ft = mActivity.getSupportFragmentManager().beginTransaction();
            ft.detach(fragment);
            ft.commit();
        }

        mTabList.put(tag, tab);
        mTabHost.addTab(spec);
	}

	@Override
	public void setActiveTab(int position) {
		mTabHost.setCurrentTab(position);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// Save and restore the selected tab for rotations/restarts.
		outState.putString("tab", mTabHost.getCurrentTabTag());
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		if (savedInstanceState != null) {
			mTabHost.setCurrentTabByTag(savedInstanceState.getString("tab"));
		}
	}

	@Override
	public void onTabChanged(String tabId) {
		CompatTab newTab = mTabList.get(tabId);
        FragmentTransaction ft = mActivity.getSupportFragmentManager().beginTransaction();

        if (mLastTab != newTab) {
            if (mLastTab != null) {
                if (mLastTab.getFragment() != null) {
                    // Pass the unselected event back to the tab's CompatTabListener
                    mLastTab.getCallback().onTabUnselected(mLastTab, ft);
                }
            }
            if (newTab != null) {
                // Pass the selected event back to the tab's CompatTabListener
                newTab.getCallback().onTabSelected(newTab, ft);
            }

            mLastTab = newTab;
        } else {
            // Pass the re-selected event back to the tab's CompatTabListener
            newTab.getCallback().onTabReselected(newTab, ft);
        }

        ft.commit();
        mActivity.getSupportFragmentManager().executePendingTransactions();
	}

	private class TabChangeFactory implements TabContentFactory {
		private final Context mContext;

        public TabChangeFactory(Context context) {
            mContext = context;
        }

        @Override
        public View createTabContent(String tag) {
            View v = new View(mContext);
            v.setMinimumWidth(0);
            v.setMinimumHeight(0);
            return v;
        }
	}
}
