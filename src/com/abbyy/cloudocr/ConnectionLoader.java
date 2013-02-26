package com.abbyy.cloudocr;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;

import org.xmlpull.v1.XmlPullParserException;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.abbyy.cloudocr.utils.CloudClient;
import com.abbyy.cloudocr.utils.XMLParser;

public class ConnectionLoader extends AsyncTaskLoader<Void> {
	private Context mContext;
	private CloudClient mClient;
	private String startTag;
	private String tag;

	public ConnectionLoader(Context context, CloudClient client)
			throws MalformedURLException {
		super(context);
		mContext = context;
		mClient = client;
	}

	/**
	 * The implementation of the AsyncTaskLoader for the support package has a
	 * bug which requires the forceLoad() on the StartLoading, as it will not
	 * start on its own. This allows for further control of the loadings.
	 */
	@Override
	protected void onStartLoading() {
		if (tag == null) {
			forceLoad();
		}
	}

	@Override
	public Void loadInBackground() {
		try {
			startTag = mContext.getString(R.string.tag_response);
			tag = mContext.getString(R.string.tag_task);
			String response = mClient.makePetition();

			XMLParser xmlParser = new XMLParser(response);
			ArrayList<HashMap<String, String>> tasks = xmlParser.parseData(
					startTag, tag);
			if (tasks != null) {
				for (int i = 0; i < tasks.size(); i++) {
					new Task(mContext, tasks.get(i)).writeTaskToDb();
				}
			}
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
