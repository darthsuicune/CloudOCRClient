package com.abbyy.cloudocr.fragments;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.abbyy.cloudocr.R;
import com.abbyy.cloudocr.SettingsActivity;
import com.abbyy.cloudocr.TaskDetailsActivity;
import com.abbyy.cloudocr.database.TasksContract;

public class TaskDetailsFragment extends Fragment {
	private static final int LOADER_TASK_INFO = 0;
	private String mTaskId;

	public TaskDetailsFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.task_details_fragment, container,
				false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		loadTaskData();
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.task_details, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_settings:
			Intent intent = new Intent(getActivity(), SettingsActivity.class);
			startActivity(intent);
			break;
		}
		return true;
	}

	private void loadTaskData() {
		if (mTaskId == null) {
			mTaskId = getArguments().getString(
					TaskDetailsActivity.EXTRA_TASK_ID);

			getActivity().getSupportLoaderManager().initLoader(
					LOADER_TASK_INFO, null, new TaskInfoHelper());
		} else {
			getActivity().getSupportLoaderManager().restartLoader(
					LOADER_TASK_INFO, null, new TaskInfoHelper());
		}
	}

	private class TaskInfoHelper implements LoaderCallbacks<Cursor> {

		@Override
		public Loader<Cursor> onCreateLoader(int id, Bundle args) {
			Loader<Cursor> loader = null;
			Uri uri = TasksContract.CONTENT_TASKS;
			String selection = TasksContract.TasksTable.TASK_ID + "=?";
			String[] selectionArgs = { mTaskId };
			switch (id) {
			case LOADER_TASK_INFO:
				loader = new CursorLoader(getActivity(), uri, null, selection,
						selectionArgs, null);
				break;
			}
			return loader;
		}

		@Override
		public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
			populateScreen(cursor);
		}

		@Override
		public void onLoaderReset(Loader<Cursor> loader) {
		}

	}

	private void populateScreen(Cursor cursor) {
		if (cursor != null) {
			if (cursor.moveToFirst()) {
				DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd'T'HH:mm:ss'Z'",
						Locale.GERMANY);
				
				TextView taskIdView = (TextView) getActivity().findViewById(R.id.task_details_task_id);
				TextView descriptionView = (TextView) getActivity().findViewById(R.id.task_details_description);
				TextView statusView = (TextView) getActivity().findViewById(R.id.task_details_status);
				TextView registrationTimeView = (TextView) getActivity().findViewById(R.id.task_details_registration_time);
				TextView statusChangeTimeView = (TextView) getActivity().findViewById(R.id.task_details_status_change_time);
				TextView filesCountView = (TextView) getActivity().findViewById(R.id.task_details_files_count);
				TextView creditsView = (TextView) getActivity().findViewById(R.id.task_details_credits);
				TextView estimatedProcessingTimeView = (TextView) getActivity().findViewById(R.id.task_details_estimated_processing_time);
				TextView resultUrlView = (TextView) getActivity().findViewById(R.id.task_details_result_url);
				TextView errorMessageView = (TextView) getActivity().findViewById(R.id.task_details_error);
				
				String description = cursor.getString(cursor.getColumnIndex(TasksContract.TasksTable.DESCRIPTION));
				String status = cursor.getString(cursor.getColumnIndex(TasksContract.TasksTable.STATUS));
				String registrationTime = cursor.getString(cursor.getColumnIndex(TasksContract.TasksTable.REGISTRATION_TIME));
				String statusChangeTime = cursor.getString(cursor.getColumnIndex(TasksContract.TasksTable.STATUS_CHANGE_TIME));
				String filesCount = cursor.getString(cursor.getColumnIndex(TasksContract.TasksTable.FILES_COUNT));
				String credits = cursor.getString(cursor.getColumnIndex(TasksContract.TasksTable.CREDITS));
				String estimatedProcessingTime = cursor.getString(cursor.getColumnIndex(TasksContract.TasksTable.ESTIMATED_PROCESSING_TIME));
				String resultUrl = cursor.getString(cursor.getColumnIndex(TasksContract.TasksTable.RESULT_URL));
				String errorMessage = cursor.getString(cursor.getColumnIndex(TasksContract.TasksTable.ERROR));
				
				Date registrationDate = null;
				Date statusChangeDate = null;
				try {
					registrationDate = dateFormat.parse(registrationTime);
					statusChangeDate = dateFormat.parse(statusChangeTime);
				} catch (ParseException e) {
					e.printStackTrace();
				}
				
				taskIdView.setText(getActivity().getString(R.string.details_task_id) + mTaskId);
				descriptionView.setText(getActivity().getString(R.string.details_description) + description);
				statusView.setText(getActivity().getString(R.string.details_status) + status);
				registrationTimeView.setText(getActivity().getString(R.string.details_registration_time) + registrationDate);
				filesCountView.setText(getActivity().getString(R.string.details_files_count) + filesCount);
				creditsView.setText(getActivity().getString(R.string.details_credits) + credits);
				estimatedProcessingTimeView.setText(getActivity().getString(R.string.details_estimated_processing_time) + estimatedProcessingTime);
				statusChangeTimeView.setText(getActivity().getString(R.string.details_status_change_time) + statusChangeDate);
				if(resultUrl != null && !TextUtils.isEmpty(resultUrl)){
					resultUrlView.setText(getActivity().getString(R.string.details_result_url) + resultUrl);
				}
				if(errorMessage != null && !TextUtils.isEmpty(errorMessage)){
					errorMessageView.setText(getActivity().getString(R.string.details_error) + errorMessage);
				}
				
				
			}
		}
	}
}
