package com.abbyy.cloudocr;

import android.os.Bundle;

import com.abbyy.cloudocr.compat.TabCompatActivity;
import com.abbyy.cloudocr.compat.TabHelper;

public class MainActivity extends TabCompatActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.main_activity);
		
		setTabs();
		super.onCreate(savedInstanceState);
	}
	
	private void setTabs(){
		TabHelper tabHelper = getTabHelper();
		tabHelper.addTab(tabHelper.newTab(getString(R.string.tab_active_tasks)));
		tabHelper.addTab(tabHelper.newTab(getString(R.string.tab_completed_tasks)));
	}

}
