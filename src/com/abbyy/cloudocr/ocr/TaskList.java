package com.abbyy.cloudocr.ocr;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.content.Context;
import android.util.Xml;

import com.abbyy.cloudocr.R;

public class TaskList {
	private ArrayList<Task> taskList;
	private Context mContext;

	public TaskList(Context context, InputStream response) {
		mContext = context;
		taskList = new ArrayList<Task>();
		parseResponse(response);
	}
	
	private void parseResponse(InputStream response){
		XmlPullParser parser = Xml.newPullParser();
		try {
			parser.setInput(response, null);
			readData(parser);
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * This method is in charge of parsing the XML data received.
	 * 
	 * @param parser
	 *            XmlPullParser
	 * @throws XmlPullParserException
	 * @throws IOException
	 */
	private void readData(XmlPullParser parser) throws XmlPullParserException,
			IOException {
		parser.nextTag();
		parser.require(XmlPullParser.START_TAG, null,
				mContext.getString(R.string.tag_response));
		while (parser.next() != XmlPullParser.END_TAG) {
			if (parser.getEventType() != XmlPullParser.START_TAG) {
				continue;
			}
			String name = parser.getName();
			if (name.equals(mContext.getString(R.string.tag_task))) {
				new Task(parser.getAttributeValue(null,
							mContext.getString(R.string.field_id)), 
						parser.getAttributeValue(null,
								mContext.getString(R.string.field_status)), 
						parser.getAttributeValue(null,
								mContext.getString(R.string.field_registration_time)),
						parser.getAttributeValue(null,
						mContext.getString(R.string.field_status_change_time)),
						Integer.parseInt(parser.getAttributeValue(null,
								mContext.getString(R.string.field_files_count))),
				Integer.parseInt(parser.getAttributeValue(null,
						mContext.getString(R.string.field_credits))),
						Integer.parseInt(parser
								.getAttributeValue(
										null,
										mContext.getString(R.string.field_estimated_processing_time))),
				parser.getAttributeValue(null,
						mContext.getString(R.string.field_description)),
				parser.getAttributeValue(null,
						mContext.getString(R.string.field_result_url)),
				parser.getAttributeValue(null, mContext.getString(R.string.field_error)));

			} else if(name.equals(mContext.getString(R.string.tag_error))){
			
			}
		}
	}
}
