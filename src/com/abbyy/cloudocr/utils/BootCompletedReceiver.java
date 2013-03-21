package com.abbyy.cloudocr.utils;

import com.abbyy.cloudocr.TasksManagerService;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * This class implements a receiver that would start the service upon a restart
 * of the device.
 * 
 * Requires the permission BOOT_COMPLETED
 * 
 * @author Denis Lapuente
 * 
 */
public class BootCompletedReceiver extends BroadcastReceiver {

	/**
	 * Method called when the broadcast signal is received. The signal it will
	 * react to is specified in the AndroidManifest.xml file.
	 * 
	 * It sends the TasksManagerService an empty Intent so it can check for
	 * active tasks.
	 */
	@Override
	public void onReceive(Context context, Intent intent) {
		Intent service = new Intent(context, TasksManagerService.class);
		context.startService(service);
	}
}