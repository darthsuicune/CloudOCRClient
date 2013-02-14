package com.abbyy.cloudocr.optionsfragments;

import java.util.ArrayList;

import android.view.View;
import android.view.View.OnClickListener;

public class LanguageHelper implements OnClickListener {
	private ProcessOptionsFragment mFragment;
	private ArrayList<String> mLanguages;
	
	public LanguageHelper(ProcessOptionsFragment fragment, ArrayList<String> languages){
		mFragment = fragment;
		if(languages == null) {
			mLanguages = new ArrayList<String>();
		} else {
			mLanguages = languages;
		}
	}

	@Override
	public void onClick(View v) {
		prepareDialog();
		showDialog();
		retrieveLanguages();
	}

	private void prepareDialog() {
		// TODO Auto-generated method stub
		
	}

	private void showDialog() {
		// TODO Auto-generated method stub
		
	}

	private void retrieveLanguages() {
		// TODO Auto-generated method stub
		mFragment.addLanguage("item");
	}
}
