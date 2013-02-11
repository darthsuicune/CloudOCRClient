package com.abbyy.cloudocr.optionsfragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.abbyy.cloudocr.AsyncConnectionLoader;
import com.abbyy.cloudocr.R;

public class ProcessTextFieldOptionsFragment extends ProcessOptionsFragment {

	private String createURL() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Bundle createArgs() {
		Bundle args = new Bundle();
		args.putString(AsyncConnectionLoader.ARGUMENT_URL, createURL());
		return args;
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
