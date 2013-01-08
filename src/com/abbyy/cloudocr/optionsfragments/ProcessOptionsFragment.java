package com.abbyy.cloudocr.optionsfragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;

public abstract class ProcessOptionsFragment extends Fragment {
	protected String mTaskId;
	public abstract String createURL();
	public abstract Bundle createArgs();
	public abstract boolean setDefaultOptions(Bundle options);
}
