package com.abbyy.cloudocr.utils;

import java.util.ArrayList;

public class LanguageHelper {
	private ArrayList<String> mAvailableLanguages;
	private ArrayList<String> mLanguagesList;

	public LanguageHelper(String[] languages) {
		mAvailableLanguages = new ArrayList<String>();
		for (int i = 0; i < languages.length; i++) {
			mAvailableLanguages.add(languages[i]);
		}
		mLanguagesList = new ArrayList<String>();
	}

	public boolean addLanguage(String language) {
		if (mAvailableLanguages.contains(language)
				&& !mLanguagesList.contains(language)) {
			mLanguagesList.add(language);
			return true;
		} else {
			return false;
		}
	}

	public void removeLanguage(String language) {
		if (mLanguagesList.contains(language)) {
			mLanguagesList.remove(language);
		}

	}

	public String getLanguages() {
		String languages = "";
		for (int i = 0; i < mLanguagesList.size(); i++) {
			if (i > 0) {
				languages = languages.concat(",");
			}
			languages = languages.concat(mLanguagesList.get(i));
		}
		return languages;
	}

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
