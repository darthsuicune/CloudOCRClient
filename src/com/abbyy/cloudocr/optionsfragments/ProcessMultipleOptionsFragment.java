package com.abbyy.cloudocr.optionsfragments;

import com.abbyy.cloudocr.R;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ProcessMultipleOptionsFragment extends ProcessOptionsFragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.process_image_fragment, container,
				false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public boolean saveDefaultOptions() {
		SharedPreferences.Editor editor = prefs.edit();
		// TODO Auto-generated method stub
		return editor.commit();
	}

	@Override
	public boolean loadDefaultOptions() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	boolean setViews() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	void launchTask() {
		
	}

	@Override
	public void addFile(String filePath) {
		// TODO Auto-generated method stub
		
	}

}
