package com.abbyy.cloudocr.utils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Xml;

/**
 * Basic XML Parser that returns an ArrayList of HashMap<String,String>. Each
 * entry of the hashmap is an attribute of the item, related to its value. The
 * Arraylist adds one item for each item on the XML
 * 
 * WARNING: This is not a generic XML parser. This parser is only thought to
 * parse XML streams in the form
 * 
 * <starttag> 
 * <tag attr1="value1" attr2="value2"></tag> (also <tag />) 
 * <tag... /> 
 * </starttag>
 * 
 * This parser will not retrieve values inside the tags or explore tags
 * recursively.
 * 
 * @author Denis Lapuente
 * 
 */
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
	 * This method will load the parser and initialize its values. Then start
	 * the parsing to finally close the input stream.
	 * 
	 * @param startTag
	 *            The tag which marks the start of the file
	 * @param tag
	 *            The inner tag of each element
	 * @return ArrayList with the resulting parsed elements
	 * @throws XmlPullParserException
	 *             Exception thrown when an XML requirement is not met
	 * @throws IOException
	 *             Exception with issues with the Input Stream
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

	/**
	 * This method will parse the XML result and return it as an ArrayList of
	 * different HashMaps containing the attributes and its related values.
	 * 
	 * @param parser the initialized XmlPullParser
	 * @return ArrayList with the resulting parsed elements
	 * @throws XmlPullParserException
	 *             Exception thrown when an XML requirement is not met
	 * @throws IOException
	 *             Exception with issues with the Input Stream
	 */
	private ArrayList<HashMap<String, String>> parse(XmlPullParser parser)
			throws XmlPullParserException, IOException {
		ArrayList<HashMap<String, String>> result = new ArrayList<HashMap<String, String>>();
		parser.nextTag();
		parser.require(XmlPullParser.START_TAG, null, mStartTag);
		while (parser.next() != XmlPullParser.END_TAG) {
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
				if (parser.next() != XmlPullParser.END_TAG) {
					break;
				}
			}
		}
		return result;
	}
}
