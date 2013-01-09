package com.abbyy.cloudocr;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

import com.abbyy.cloudocr.compat.ActionBarActivity;

public class SettingsActivity extends ActionBarActivity {

	public static Bundle getDefaultOptions(Context context, int processingMode) {
		Bundle options = new Bundle();
		switch (processingMode) {
		case CreateTaskActivity.EXTRA_PROCESS_BARCODE_FIELD:
			break;
		case CreateTaskActivity.EXTRA_PROCESS_BUSINESS_CARD:
			break;
		case CreateTaskActivity.EXTRA_PROCESS_CHECKMARK_FIELD:
			break;
		case CreateTaskActivity.EXTRA_PROCESS_FIELDS:
			break;
		case CreateTaskActivity.EXTRA_PROCESS_IMAGE:
			break;
		case CreateTaskActivity.EXTRA_PROCESS_MULTIPLE:
			break;
		case CreateTaskActivity.EXTRA_PROCESS_TEXT_FIELD:
			break;
		}
		return options;
	}

	public static void saveDefaultOptions(Context context, int processingMode) {
		SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
		switch (processingMode) {
		case CreateTaskActivity.EXTRA_PROCESS_BARCODE_FIELD:
			break;
		case CreateTaskActivity.EXTRA_PROCESS_BUSINESS_CARD:
			break;
		case CreateTaskActivity.EXTRA_PROCESS_CHECKMARK_FIELD:
			break;
		case CreateTaskActivity.EXTRA_PROCESS_FIELDS:
			break;
		case CreateTaskActivity.EXTRA_PROCESS_IMAGE:
			break;
		case CreateTaskActivity.EXTRA_PROCESS_MULTIPLE:
			break;
		case CreateTaskActivity.EXTRA_PROCESS_TEXT_FIELD:
			break;
		}
		editor.commit();
	}
}
