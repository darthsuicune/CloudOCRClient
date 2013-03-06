package com.abbyy.cloudocr.utils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Log;
import android.util.Xml;

public class XMLParser {
	private InputStream mInput;
	private String mStartTag;
	private String mTag;

	public XMLParser(InputStream stream) {
		mInput = stream;
	}

	public XMLParser(String data) {
		mInput = new ByteArrayInputStream(data.getBytes());
	}

	/**
	 * This method will load the parser
	 * 
	 * @param parser
	 *            XmlPullParser
	 * @throws XmlPullParserException
	 * @throws IOException
	 */

	public ArrayList<HashMap<String, String>> parseData(String startTag,
			String tag) throws XmlPullParserException, IOException {
		mStartTag = startTag;
		mTag = tag;

		XmlPullParser parser = Xml.newPullParser();
		parser.setInput(new InputStreamReader(mInput));
		ArrayList<HashMap<String, String>> result = parse(parser);
		mInput.close();
		return result;
	}

	private ArrayList<HashMap<String, String>> parse(XmlPullParser parser)
			throws XmlPullParserException, IOException {
		ArrayList<HashMap<String, String>> result = new ArrayList<HashMap<String, String>>();
		parser.nextTag();
		parser.require(XmlPullParser.START_TAG, null, mStartTag);
		while (parser.next() != XmlPullParser.END_TAG) {
			Log.d("XML Parser - Name", parser.getName());
			Log.d("XML Parser - Pos", parser.getPositionDescription());
			if (parser.getEventType() != XmlPullParser.START_TAG) {
				continue;
			}
			String name = parser.getName();
			if (name.equals(mTag)) {
				HashMap<String, String> item = new HashMap<String, String>();
				for (int i = 0; i < parser.getAttributeCount(); i++) {
					String value = parser.getAttributeValue(null,
							parser.getAttributeName(i));
					item.put(parser.getAttributeName(i), value);
				}
				result.add(item);
				if(parser.next() != XmlPullParser.END_TAG){
					break;
				}
			}
		}
		return result;
	}
}