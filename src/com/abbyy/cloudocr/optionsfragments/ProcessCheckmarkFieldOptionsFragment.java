package com.abbyy.cloudocr.optionsfragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.abbyy.cloudocr.AsyncInputStreamLoader;
import com.abbyy.cloudocr.R;

public class ProcessCheckmarkFieldOptionsFragment extends ProcessOptionsFragment {

	private String createURL() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Bundle createArgs() {
		Bundle args = new Bundle();
		args.putString(AsyncInputStreamLoader.ARGUMENT_URL, createURL());
		return args;
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
