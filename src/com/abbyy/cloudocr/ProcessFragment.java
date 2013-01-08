package com.abbyy.cloudocr;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.ActionBar.OnNavigationListener;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

public class ProcessFragment extends Fragment implements OnClickListener{

	private Spinner mProcessTypeSpinner;
	private Button mProcessButton;

	private int mProcessMode = 0;
	
	public static final int LOADER_CONNECT = 1;
	
	private SharedPreferences prefs;
	
	private ProcessOptionsFragment mProcessOptionsFragment;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.process_fragment, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		Bundle extras = getActivity().getIntent().getExtras();
		if(extras != null){
			mProcessMode = extras.getInt(ProcessActivity.EXTRA_PROCESS_MODE);
		}
		prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
		prepareViews();
		loadOptions();
		setDefaultOptions();
		super.onActivityCreated(savedInstanceState);
	}

	private void prepareViews() {
		mProcessTypeSpinner = (Spinner) getActivity().findViewById(R.id.spinner_process_type);
		mProcessButton = (Button) getActivity().findViewById(R.id.process_button);
		
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){
			mProcessTypeSpinner.setVisibility(View.GONE);
			mProcessButton.setVisibility(View.GONE);
			setActionBar();
		}else{
			mProcessTypeSpinner.setVisibility(View.VISIBLE);
			mProcessTypeSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

				@Override
				public void onItemSelected(AdapterView<?> parent, View view,
						int pos, long id) {
					changeProcessMode(pos);
				}

				@Override
				public void onNothingSelected(AdapterView<?> parent) { 
					//Nothing to do here
				}
			});
			mProcessTypeSpinner.setSelection(mProcessMode);
		}
		
		mProcessButton.setOnClickListener(this);
	}

	private void changeProcessMode(int newMode){
		mProcessMode = newMode;
		Toast.makeText(getActivity(), "" + newMode, Toast.LENGTH_LONG).show();
		loadOptions();
	}
	
	private void loadOptions(){
		FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
		
		switch(mProcessMode){
		case ProcessActivity.EXTRA_PROCESS_IMAGE:
			
			break;
		case ProcessActivity.EXTRA_PROCESS_MULTIPLE:
			break;
		case ProcessActivity.EXTRA_PROCESS_BUSINESS_CARD:
			break;
		case ProcessActivity.EXTRA_PROCESS_TEXT_FIELD:
			break;
		case ProcessActivity.EXTRA_PROCESS_BARCODE_FIELD:
			break;
		case ProcessActivity.EXTRA_PROCESS_CHECKMARK_FIELD:
			break;
		case ProcessActivity.EXTRA_PROCESS_FIELDS:
			break;
		}
		transaction.replace(R.id.process_options, mProcessOptionsFragment);
		transaction.commit();
	}

	private void setDefaultOptions() {
	}
	
	private void showProgress(final boolean show){
		
	}
	
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setActionBar(){
		ActionBar actionBar = getActivity().getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
		actionBar.setListNavigationCallbacks(getProcessSpinner(), new OnNavigationListener() {
			@Override
			public boolean onNavigationItemSelected(int itemPosition, long itemId) {
				changeProcessMode(itemPosition);
				return true;
			}
		});
	}
	
	private SpinnerAdapter getProcessSpinner(){
		return null;
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.process_button:
			sendRequest();
			break;
		}
	}
	
	private void sendRequest(){
		
	}
	
	private void parseResponse(String response){
		
	}
	
	private class ConnectionLoaderHelper implements LoaderCallbacks<String>{

		@Override
		public Loader<String> onCreateLoader(int id, Bundle args) {
			Loader<String> loader = null;
			switch(id){
			case LOADER_CONNECT:
				loader = new AsyncStringLoader(getActivity(), mProcessOptionsFragment.createURL());
				break;
			}
			return loader;
		}

		@Override
		public void onLoadFinished(Loader<String> loader, String result) {
			switch(loader.getId()){
			case LOADER_CONNECT:
				parseResponse(result);
				break;
			}
		}

		@Override
		public void onLoaderReset(Loader<String> loader) {
			// TODO Auto-generated method stub
			
		}
	}
}
