package com.abbyy.cloudocr;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

public class ProcessFragment extends Fragment implements OnClickListener{

	private Spinner mProcessTypeSpinner;
	private Button mAddLanguagesButton;
	private Button mSendButton;

	private SharedPreferences prefs;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		return inflater.inflate(R.layout.process_fragment, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
		prepareViews();
		setDefaultOptions();
		super.onActivityCreated(savedInstanceState);
	}

	private void prepareViews() {
	}

	private void setDefaultOptions() {
	}
	
	private void showProgress(final boolean show){
		
	}
	
	private SpinnerAdapter getProcessSpinner(){
		return null;
	}

	@Override
	public void onClick(View v) {
	}
}
