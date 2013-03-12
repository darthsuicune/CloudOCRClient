package com.abbyy.cloudocr.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.net.Uri;
import android.os.Environment;
import android.util.Log;

public class FileManager {
	public static final int MEDIA_TYPE_IMAGE = 1;
	public static final int MEDIA_TYPE_VIDEO = 2;

	public static Uri getOutputMediaFileUri(int type) {
		return Uri.fromFile(getOutputMediaFile(type));
	}

	public static File getOutputMediaFile(int type) {
		if (!Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			return null;
		}

		File mediaStorageDir = getDefaultMediaFolder();
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.GERMANY)
				.format(new Date());
		File mediaFile;
		if (type == MEDIA_TYPE_IMAGE) {
			mediaFile = new File(mediaStorageDir.getPath() + File.separator
					+ "IMG_" + timeStamp + ".jpg");
		} else if (type == MEDIA_TYPE_VIDEO) {
			mediaFile = new File(mediaStorageDir.getPath() + File.separator
					+ "VID_" + timeStamp + ".mp4");
		} else {
			return null;
		}

		return mediaFile;
	}

	public static Uri getOutputDownloadFileUri(String filename,
			String downloadFormat) {
		return Uri.fromFile(getOutputDownloadFile(filename, downloadFormat));
	}

	public static File getOutputDownloadFile(String filename,
			String downloadFormat) {
		if (!Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			return null;
		}
		File storageDir = getDefaultMediaFolder();
		return new File(storageDir.getPath() + File.separator + filename 
				+ downloadFormat);
	}

	private static File getDefaultMediaFolder() {
		File mediaStorageDir = new File(
				Environment
						.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
				"ABBYY");
		if (!mediaStorageDir.exists()) {
			if (!mediaStorageDir.mkdirs()) {
				Log.d("ABBYY Cloud OCR Client", "Failed to create folder");
			}
		}
		return mediaStorageDir;
	}
	
	public static void exportFile(InputStream inputStream, File targetFile){
		try {
			FileOutputStream outputStream = new FileOutputStream(targetFile);
			byte[] buffer = new byte[1024];
			int bufferLength = 0;
			while((bufferLength = inputStream.read(buffer)) > 0){
				outputStream.write(buffer, 0, bufferLength);
			}
			inputStream.close();
			outputStream.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
