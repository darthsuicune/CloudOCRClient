package com.abbyy.cloudocr;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ResultFragment extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		return inflater.inflate(R.layout.results_fragment, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
			prepareLandscape();
		} else {
			preparePortrait();
		}
		super.onActivityCreated(savedInstanceState);
	}
	
	private void prepareLandscape(){
		
	}
	
	private void preparePortrait(){
		
	}

}
