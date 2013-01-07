package com.abbyy.cloudocr;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

public class MainFragment extends Fragment implements OnClickListener{
	
	private static final int ACTIVITY_GET_IMAGE = 1;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.main_fragment, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		prepareImageView();
		setButtons();
		super.onActivityCreated(savedInstanceState);
	}
	
	private void prepareImageView(){
		ImageView preview = (ImageView) getActivity().findViewById(R.id.preview);
	}
	
	private void setButtons(){
		Button takePictureButton = (Button) getActivity().findViewById(R.id.take_picture);
		Button getFromGalleryButton = (Button) getActivity().findViewById(R.id.get_from_gallery);
		
		takePictureButton.setOnClickListener(this);
		getFromGalleryButton.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		Intent intent = new Intent();
		switch(v.getId()){
		case R.id.take_picture:
			intent.setAction(Intent.ACTION_GET_CONTENT);
			intent.setType("image/*");
			break;
		case R.id.get_from_gallery:
			intent.setAction(Intent.ACTION_GET_CONTENT);
			intent.setType("image/*");
			break;
		}
		startActivityForResult(intent, ACTIVITY_GET_IMAGE);
	}

}
