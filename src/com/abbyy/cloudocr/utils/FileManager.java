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

/**
 * Helper class to create and modify files from the external storage folder. It
 * is dependant on the implementation, so unless using Gingerbread and no SD
 * card is available, it should work.
 * 
 * @author Denis Lapuente
 * 
 */
public class FileManager {
	// Subfolder constant to create the folder in the external directory.
	// Changeable to anything really, just used to save the pictures taken and
	// the downloads made.
	private static final String FOLDER_NAME = "ABBYY";

	public static final int MEDIA_TYPE_IMAGE = 1;
	public static final int MEDIA_TYPE_VIDEO = 2;

	/**
	 * Gets the uri from the output media file. It also creates the file when
	 * needed.
	 * 
	 * @param type
	 *            Constant MEDIA_TYPE to differentiate between images and
	 *            videos.
	 * @return Uri pointing to the file.
	 */
	public static Uri getOutputMediaFileUri(int type) {
		return Uri.fromFile(getOutputMediaFile(type));
	}

	/**
	 * Gets a file object identified by its timestamp. Creates it in the default
	 * media folder for the application
	 * 
	 * @param type
	 *            Type of file we are using (image/video)
	 * @return File object where the target should be saved.
	 */
	public static File getOutputMediaFile(int type) {
		// If the external storage is not mounted return null.
		if (!Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			return null;
		}

		// If the media Storage dir can't be created, return null
		File mediaStorageDir = getDefaultMediaFolder();
		if (mediaStorageDir == null) {
			return null;
		}

		// Else create a standard file for the correct type and return it.
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
				Locale.GERMANY).format(new Date());
		switch (type) {
		case MEDIA_TYPE_IMAGE:
			return new File(mediaStorageDir.getPath() + File.separator + "IMG_"
					+ timeStamp + ".jpg");
		case MEDIA_TYPE_VIDEO:
			return new File(mediaStorageDir.getPath() + File.separator + "VID_"
					+ timeStamp + ".mp4");
		default:
			return null;
		}
	}

	/**
	 * Retrives the Uri from a file to be downloaded, given its filename and
	 * download format.
	 * 
	 * @param filename
	 *            The filename where the file should be saved (no path, only
	 *            filename)
	 * @param downloadFormat
	 *            format for the filename. Obtained through the Service.
	 * @return Uri pointing to the file where the download will be saved
	 */
	public static Uri getOutputDownloadFileUri(String format) {

		return Uri.fromFile(getOutputDownloadFile(format));
	}

	/**
	 * Retrieves the file object where the downloaded file will be placed.
	 * 
	 * @param filename
	 *            The filename where the file should be saved (no path, only
	 *            filename)
	 * @param downloadFormat
	 *            format for the filename. Obtained through the Service.
	 * @return File object where the download will be saved
	 */
	public static File getOutputDownloadFile(String format) {
		if (!Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			return null;
		}
		File storageDir = getDefaultDownloadFolder();

		if (storageDir == null) {
			return null;
		}

		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
				Locale.GERMANY).format(new Date());
		String fileName = "RESULT_" + timeStamp;
		return new File(storageDir.getPath() + File.separator + fileName
				+ format);
	}

	/**
	 * Convenience method to retrieve the default media folder for the
	 * application. It retrieves the default media folder for pictures and then
	 * creates a inner subfolder.
	 * 
	 * @return File object pointing to the application media folder.
	 */
	private static File getDefaultMediaFolder() {
		File mediaStorageDir = new File(
				Environment
						.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
				FOLDER_NAME);
		if (!mediaStorageDir.exists()) {
			if (!mediaStorageDir.mkdirs()) {
				Log.d("ABBYY Cloud OCR Client", "Failed to create folder");
				return null;
			}
		}
		return mediaStorageDir;
	}

	/**
	 * Convenience method to retrieve the default media folder for the
	 * application. It retrieves the default downloads folder and creates an
	 * internal subfolder
	 * 
	 * @return File object pointing to the application media folder.
	 */
	private static File getDefaultDownloadFolder() {
		File mediaStorageDir = new File(
				Environment
						.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
				FOLDER_NAME);
		if (!mediaStorageDir.exists()) {
			if (!mediaStorageDir.mkdirs()) {
				Log.d("ABBYY Cloud OCR Client", "Failed to create folder");
				return null;
			}
		}
		return mediaStorageDir;
	}

	/**
	 * Method that given an InputStream (connection) and a target File object,
	 * exports the contents of the InputStream into the target file. Used to
	 * download files from Internet
	 * 
	 * @param inputStream
	 * @param targetFile
	 */
	public static void exportFile(InputStream inputStream, File targetFile) {
		try {
			FileOutputStream outputStream = new FileOutputStream(targetFile);
			byte[] buffer = new byte[1024];
			int bufferLength = 0;
			while ((bufferLength = inputStream.read(buffer)) > 0) {
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
