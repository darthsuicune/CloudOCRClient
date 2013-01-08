package com.abbyy.cloudocr;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.abbyy.cloudocr.compat.ActionBarActivity;
import com.abbyy.cloudocr.optionsfragments.ProcessBarcodeFieldOptionsFragment;
import com.abbyy.cloudocr.optionsfragments.ProcessBusinessCardOptionsFragment;
import com.abbyy.cloudocr.optionsfragments.ProcessCheckmarkFieldOptionsFragment;
import com.abbyy.cloudocr.optionsfragments.ProcessFieldsOptionsFragment;
import com.abbyy.cloudocr.optionsfragments.ProcessImageOptionsFragment;
import com.abbyy.cloudocr.optionsfragments.ProcessMultipleOptionsFragment;
import com.abbyy.cloudocr.optionsfragments.ProcessOptionsFragment;
import com.abbyy.cloudocr.optionsfragments.ProcessTextFieldOptionsFragment;

public class CreateTaskActivity extends ActionBarActivity {
	public static final String EXTRA_PROCESS_MODE = "Process mode";
	
	public static final int EXTRA_PROCESS_IMAGE = 0;
	public static final int EXTRA_PROCESS_MULTIPLE = 1;
	public static final int EXTRA_PROCESS_BUSINESS_CARD = 2;
	public static final int EXTRA_PROCESS_TEXT_FIELD = 3;
	public static final int EXTRA_PROCESS_BARCODE_FIELD = 4;
	public static final int EXTRA_PROCESS_CHECKMARK_FIELD = 5;
	public static final int EXTRA_PROCESS_FIELDS = 6;
	
	private ProcessOptionsFragment mProcessOptionsFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		addOptionsFragment();
		setFragmentOptions();
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		new MenuInflater(this).inflate(R.menu.process, menu);
//		getActionBarHelper().getMenuInflater(new MenuInflater(this)).inflate(R.menu.process, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		return super.onOptionsItemSelected(item);
	}

	private void addOptionsFragment(){

		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
		Bundle extras = getIntent().getExtras();
		if(extras != null){
			switch(extras.getInt(EXTRA_PROCESS_MODE)){
			case EXTRA_PROCESS_BARCODE_FIELD:
				mProcessOptionsFragment = new ProcessBarcodeFieldOptionsFragment();
				break;
			case EXTRA_PROCESS_BUSINESS_CARD:
				mProcessOptionsFragment = new ProcessBusinessCardOptionsFragment();
				break;
			case EXTRA_PROCESS_CHECKMARK_FIELD:
				mProcessOptionsFragment = new ProcessCheckmarkFieldOptionsFragment();
				break;
			case EXTRA_PROCESS_FIELDS:
				mProcessOptionsFragment = new ProcessFieldsOptionsFragment();
				break;
			case EXTRA_PROCESS_IMAGE:
				mProcessOptionsFragment = new ProcessImageOptionsFragment();
				break;
			case EXTRA_PROCESS_MULTIPLE:
				mProcessOptionsFragment = new ProcessMultipleOptionsFragment();
				break;
			case EXTRA_PROCESS_TEXT_FIELD:
				mProcessOptionsFragment = new ProcessTextFieldOptionsFragment();
				break;
			}
		}
		transaction.add(R.id.create_task_options, mProcessOptionsFragment);
		transaction.commit();
	}
	
	private void setFragmentOptions(){
		
	}
}
