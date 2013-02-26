package com.abbyy.cloudocr.optionsfragments;

import java.net.MalformedURLException;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.Loader;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.abbyy.cloudocr.MainActivity;
import com.abbyy.cloudocr.R;
import com.abbyy.cloudocr.SettingsActivity;
import com.abbyy.cloudocr.utils.CloudClient;
import com.abbyy.cloudocr.utils.ConnectionLoader;

public abstract class ProcessOptionsFragment extends Fragment implements
		LoaderCallbacks<Void> {
	public static final String NOTIFICATION_TAG = "cloudOCRnotification";
	public static final int NOTIFICATION_ID = 1;
	public static final int REQUEST_ACTIVE_TASKS = 1;

	protected String mTaskId;
	CloudClient mClient;
	SharedPreferences prefs;

	public abstract void addFile(String filePath);

	public abstract boolean saveDefaultOptions();

	public abstract boolean loadDefaultOptions();

	abstract void setViews();

	abstract void launchTask();

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		mClient = new CloudClient();
		setHasOptionsMenu(true);
		setViews();
		loadDefaultOptions();
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.create_task, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_process:
			launchTask();
			break;
		}
		return true;
	}

	private void updateNotificationStatus() {

		NotificationManager nm = (NotificationManager) getActivity()
				.getSystemService(Activity.NOTIFICATION_SERVICE);

		NotificationCompat.Builder builder = new NotificationCompat.Builder(
				getActivity());

		Intent intent = new Intent(getActivity(), MainActivity.class);

		PendingIntent pendingIntent = PendingIntent.getActivity(getActivity(),
				REQUEST_ACTIVE_TASKS, intent, Intent.FLAG_ACTIVITY_NEW_TASK);

		builder.setSmallIcon(R.drawable.abbyy_logo)
				.setContentTitle(
						getActivity().getString(R.string.notification_title))
				.setContentText(
						getActivity().getString(R.string.notification_message));

		builder.setContentIntent(pendingIntent);

		nm.notify(NOTIFICATION_TAG, NOTIFICATION_ID, builder.build());
	}

	@Override
	public Loader<Void> onCreateLoader(int id, Bundle args) {
		try {
			return new ConnectionLoader(getActivity(), mClient);
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public void onLoadFinished(Loader<Void> loader, Void response) {
		if (prefs.getBoolean(SettingsActivity.SHOW_NOTIFICATION, true)) {
			updateNotificationStatus();
		}
	}

	@Override
	public void onLoaderReset(Loader<Void> loader) {
	}
}