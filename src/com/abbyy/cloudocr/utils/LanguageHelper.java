package com.abbyy.cloudocr.utils;

import java.util.ArrayList;

/**
 * Utility class to work with languages
 * 
 * @author Denis Lapuente
 * 
 */
public class LanguageHelper {
	private ArrayList<String> mAvailableLanguages;
	private ArrayList<String> mLanguagesList;

	/**
	 * Constructor. Creates the current languages list and the Available
	 * languages list. This second one is initialized with all possible values.
	 * In order for this to be correctly done, and to avoid a memory overhead,
	 * it must be called with resource containing the languages:
	 * 
	 * new LanguageHelper(getStringArray(R.array.languages))
	 * 
	 * @param languages
	 */
	public LanguageHelper(String[] languages) {
		mAvailableLanguages = new ArrayList<String>();
		for (int i = 0; i < languages.length; i++) {
			mAvailableLanguages.add(languages[i]);
		}
		mLanguagesList = new ArrayList<String>();
	}

	/**
	 * Adds a language if it is not yet added and it is available.
	 * 
	 * @param language
	 *            The string with the language to add
	 * @return true if added, false otherwise
	 */
	public boolean addLanguage(String language) {
		if (mAvailableLanguages.contains(language)
				&& !mLanguagesList.contains(language)) {
			mLanguagesList.add(language);
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Removes a language from the language list.
	 * 
	 * @param language
	 *            The language to remove.
	 */
	public void removeLanguage(String language) {
		if (mLanguagesList.contains(language)) {
			mLanguagesList.remove(language);
		}

	}

	/**
	 * This method returns the languages which are currently being used, in a
	 * comma separated format, useable as a parameter for the cloud calls
	 * 
	 * @return String in comma-separated format with the list of used languages
	 */
	public String getLanguages() {
		// If nothing is yet on the list, english is used as default.
		if (mLanguagesList.size() == 0) {
			addLanguage("English");
			return "English";
		}

		// In any other case, we create the list with the whole list of
		// languages
		String languages = "";
		for (int i = 0; i < mLanguagesList.size(); i++) {
			if (i > 0) {
				languages = languages.concat(",");
			}
			languages = languages.concat(mLanguagesList.get(i));
		}
		return languages;
	}

	/**
	 * Gets a multiple language String and adds every language to the languages
	 * list.
	 * 
	 * @param languages
	 *            String in comma-separated format with the list of used
	 *            languages
	 * @return ArrayList containing the languages used 
	 */
	public ArrayList<String> createLanguages(String languages) {
		if (!languages.equals("")) {
			if (!languages.contains(",")) {
				mLanguagesList.add(languages);
			} else {
				String language = languages.substring(
						languages.lastIndexOf(',') + 1, languages.length());
				mLanguagesList.add(language);
				int index = languages.lastIndexOf(',');
				if (index > 0) {
					createLanguages(languages.substring(0, index));
				}
			}
		}
		return mLanguagesList;
	}
}
