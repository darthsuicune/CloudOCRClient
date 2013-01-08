package com.abbyy.cloudocr.optionsfragments;

import com.abbyy.cloudocr.R;
import com.abbyy.cloudocr.R.layout;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ProcessFieldsOptionsFragment extends ProcessOptionsFragment {

	@Override
	public String createURL() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Bundle createArgs() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.process_fragment, container, false);
	}

	@Override
	public boolean setDefaultOptions(Bundle options) {
		// TODO Auto-generated method stub
		return false;
	}

}
