package com.abbyy.cloudocr;

import android.os.Bundle;

public class ProcessActivity extends ActionBarActivity {
	
	public static final String EXTRA_PROCESS_MODE = "Process mode";
	
	public static final int EXTRA_PROCESS_IMAGE = 0;
	public static final int EXTRA_PROCESS_MULTIPLE = 1;
	public static final int EXTRA_PROCESS_BUSINESS_CARD = 2;
	public static final int EXTRA_PROCESS_TEXT_FIELD = 3;
	public static final int EXTRA_PROCESS_BARCODE_FIELD = 4;
	public static final int EXTRA_PROCESS_CHECKMARK_FIELD = 5;
	public static final int EXTRA_PROCESS_FIELDS = 6;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.process_activity);
	}

}
