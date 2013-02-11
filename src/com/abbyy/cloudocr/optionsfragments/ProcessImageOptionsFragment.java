package com.abbyy.cloudocr.optionsfragments;

import java.util.ArrayList;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.abbyy.cloudocr.R;

public class ProcessImageOptionsFragment extends ProcessOptionsFragment {
	private ArrayList<String> languagesList;
	private String profile;
	private String exportFormat;
	private String description;

	String createURL() {
		return BASE_URL + "processImage?" + addOptionsToURL();
	}
	
	private String addOptionsToURL(){
		return null;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.process_fragment, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		languagesList = new ArrayList<String>();
	}

	@Override
	public boolean saveDefaultOptions() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean loadDefaultOptions() {
		// TODO Auto-generated method stub
		return false;
	}

}
