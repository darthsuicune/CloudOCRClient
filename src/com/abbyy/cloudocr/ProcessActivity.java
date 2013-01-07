package com.abbyy.cloudocr;

import android.content.res.Configuration;
import android.os.Bundle;

public class ProcessActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
			this.finish();
		}
		super.onCreate(savedInstanceState);
	}

}
