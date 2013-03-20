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

/**
 * Implementation of the Tab Helper for Eclair - Gingerbread (2.1 - 2.3.*).
 * Implements the OnTabChangeListener of the tab host to allow for the changes
 * to actually happen. It is implemented on a tab host level in Ec - GB
 * 
 */
public class TabHelperEclair extends TabHelper implements
		TabHost.OnTabChangeListener {
	private HashMap<String, CompatTab> mTabList = new HashMap<String, CompatTab>();
	private TabHost mTabHost;
	private CompatTab mLastTab;

	protected TabHelperEclair(TabCompatActivity activity) {
		super(activity);
	}

	/**
	 * The tab host needs to be set up after getting it through findViewById. We
	 * also add the tab change listener to it.
	 * 
	 * This method MUST be called before adding any tabs.
	 */
	@Override
	protected void setUp() {
		if (mTabHost == null) {
			mTabHost = (TabHost) mActivity.findViewById(android.R.id.tabhost);
			mTabHost.setup();
			mTabHost.setOnTabChangedListener(this);
		}
	}

	/**
	 * Implementation for the addTab method. It adds the tab to the tabHost and
	 * puts an icon when available. We remove always the fragment in case it was
	 * already attached, as we don't want to add it as we add the tab, but to
	 * add it when the tab is selected.
	 * 
	 * This method must be called AFTER calling setUp()
	 */
	@Override
	public void addTab(CompatTab tab) {
		String tag = tab.getTag();
		// A Tab Spec holds the icon, text and content of the tab in pre-HC
		TabSpec spec;

		if (tab.getIcon() != null) {
			spec = mTabHost.newTabSpec(tag).setIndicator(tab.getText(),
					tab.getIcon());
		} else {
			spec = mTabHost.newTabSpec(tag).setIndicator(tab.getText());
		}

		// A Tab Spec must have a content. We currently only create an empty
		// view, as the content is managed by our fragment.
		spec.setContent(new TabChangeFactory(mActivity));

		// Check to see if we already have a fragment for this tab, probably
		// from a previously saved state. If so, deactivate it, because our
		// initial state is that a tab isn't shown.
		Fragment fragment = mActivity.getSupportFragmentManager()
				.findFragmentByTag(tag);
		tab.setFragment(fragment);

		if (fragment != null && !fragment.isDetached()) {
			FragmentTransaction ft = mActivity.getSupportFragmentManager()
					.beginTransaction();
			ft.detach(fragment);
			ft.commit();
		}

		// Finally add the tab to our list and to the host
		mTabList.put(tag, tab);
		mTabHost.addTab(spec);
	}

	/**
	 * Convenience method for setting the active tab.
	 */
	@Override
	public void setActiveTab(int position) {
		mTabHost.setCurrentTab(position);
	}

	// Default methods for saving/restoring the instance state
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

	/**
	 * Implementation of the OnTabChanged interface from the TabHost. This will
	 * manage the tab changes by replacing the fragments. For this, we just get
	 * the callbacks within each tab and call them.
	 */
	@Override
	public void onTabChanged(String tabId) {
		CompatTab newTab = mTabList.get(tabId);
		FragmentTransaction ft = mActivity.getSupportFragmentManager()
				.beginTransaction();

		if (mLastTab != newTab) {
			if (mLastTab != null) {
				if (mLastTab.getFragment() != null) {
					// Pass the unselected event back to the tab's
					// CompatTabListener
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

	/**
	 * Mandatory implementation to set the tab content. We just create an empty
	 * View as the content will be managed by the fragments.
	 * 
	 */
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
