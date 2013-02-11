package com.abbyy.cloudocr.optionsfragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.abbyy.cloudocr.R;

public class ProcessBarcodeFieldOptionsFragment extends ProcessOptionsFragment {

	@Override
	String createURL() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.process_fragment, container, false);
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
