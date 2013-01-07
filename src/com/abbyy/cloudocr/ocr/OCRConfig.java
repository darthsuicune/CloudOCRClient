package com.abbyy.cloudocr.ocr;

import java.net.URL;
import java.util.ArrayList;

import android.content.Context;

public class OCRConfig {

	private String mAppId;
	private String mAppPwd;
	private ArrayList<Language> mUsedLanguages;
	private ArrayList<Language> mAvailableLanguages;
	private String mExportFormat;
	private String mProcessType;
	
	/**
	 * Basic constructor. Requires adding the AppId and password separately.
	 */
	public OCRConfig(){
		setAvailableLanguages();
	}
	
	/**
	 * Full constructor with application identifier and password
	 * 
	 * @param appId
	 * @param appPwd
	 */
	public OCRConfig(String appId, String appPwd){
		mAppId = appId;
		mAppPwd = appPwd;
		setAvailableLanguages();
	}
	
	protected String getAppId(){
		return mAppId;
	}
	protected String getAppPwd(){
		return mAppPwd;
	}
	/**
	 * Set the application Id obtained upon CloudOCR registration
	 * @param appId
	 */
	public void setAppId(String appId){
		mAppId = appId;
	}
	
	/**
	 * Set the application password obtained upon CloudOCR registration
	 * @param appPwd
	 */
	public void setAppPwd(String appPwd){
		mAppPwd = appPwd;
	}

	/**
	 * 
	 * Try to add a language, passed as a String. The list of encoded languages can be retrieved by the @getAvailableLanguages() method
	 * 
	 * IMPORTANT: The use of more than 4-5 languages will negatively impact the performance and quality
	 * 
	 * @param language String with encoded language
	 * @return true if the language was added. False if it is not supported
	 */
	public boolean addLanguage(Language language){
		if(language != null){
			if(isValidLanguage(language)){
				if(mUsedLanguages == null){
					mUsedLanguages = new ArrayList<Language>();
				}
				mUsedLanguages.add(language);
			}
			if(language != null && !mUsedLanguages.contains(language)){
				mUsedLanguages.add(language);
			}
		} else {
			
		}
		return false;
	}
	
	public boolean addLanguage(String languageAsString){
		if(languageAsString != null){
			Language language = getLanguage(languageAsString);
			if(language != null){
				mUsedLanguages.add(getLanguage(languageAsString));
				return true;
			}
		}
		return false;
	}
	
	public Language getLanguage(String languageAsString){
		for(int i = 0; i < mAvailableLanguages.size(); i++){
			if(mAvailableLanguages.get(i).mName.equals(languageAsString)){
				return mAvailableLanguages.get(i);
			}
		}
		return null;
	}
	
	/**
	 * 
	 * Add an array of languages directly.
	 * IMPORTANT: The use of more than 4-5 languages will negatively impact the performance and quality
	 * @param languagesList
	 * @return
	 */
	public int addAllLanguages(ArrayList<Language> languagesList){
		int count = 0;
		for(int i = 0; i < languagesList.size(); i++){
			
		}
		return count;
	}
	
	/**
	 * Tries to remove a language from the currently used languages list.
	 * 
	 * @param language String with encoded language
	 * @return true if the language was removed. False if not.
	 */
	public boolean removeLanguage(Language language){
		if(language != null){
			if(isValidLanguage(language) && isUsedLanguage(language)){
				mUsedLanguages.remove(language);
				return true;
			}
		}
		return false;
	}
	
	public boolean isValidLanguage(String language) {
		if(language != null){
			for(int i = 0; i < mAvailableLanguages.size(); i++){
				if(language.equals(mAvailableLanguages.get(i))){
					return true;
				}
			}
		}
		return false;
	}
	
	public boolean isValidLanguage(Language language) {
		if(language != null && mAvailableLanguages.contains(language)){
			return true;
		}
		return false;
	}
	
	public boolean isUsedLanguage(String language) {
		if(language != null){
			for(int i = 0; i < mUsedLanguages.size(); i++){
				if(language.equals(mUsedLanguages.get(i))){
					return true;
				}
			}
		}
		return false;
	}
	
	public boolean isUsedLanguage(Language language) {
		if(language != null && mUsedLanguages.contains(language)){
			return true;
		}
		return false;
	}
	
	public void resetDefaultLanguage(){
		mUsedLanguages.clear();
		mUsedLanguages.add(getLanguage("English"));
	}
	
	public ArrayList<Language> getAvailableLanguages() {
		return mAvailableLanguages;
	}
	
	public ArrayList<Language> getUsedLanguages() {
		return mUsedLanguages;
	}
	
	protected URL getURL(){
		URL url = null;
		return url;
	}
	
	private void setAvailableLanguages(){
		mUsedLanguages = new ArrayList<Language>();
		mAvailableLanguages = new ArrayList<Language>();
		mAvailableLanguages.add(new Language("Abkhaz", true, false, false));
		mAvailableLanguages.add(new Language("Adyghe", true, false, false));
		mAvailableLanguages.add(new Language("Afrikaans", true, true, false));
		mAvailableLanguages.add(new Language("Agul", true, false, false));
		mAvailableLanguages.add(new Language("Albanian", true, true, false));
		mAvailableLanguages.add(new Language("Altaic", true, false, false));
		mAvailableLanguages.add(new Language("Arabic", true, false, false));
		mAvailableLanguages.add(new Language("ArmenianEastern", true, false, false));
		mAvailableLanguages.add(new Language("ArmenianGrabar", true, false, false));
		mAvailableLanguages.add(new Language("ArmenianWestern", true, false, false));
		mAvailableLanguages.add(new Language("Awar", true, false, false));
		mAvailableLanguages.add(new Language("Aymara", true, true, false));
		mAvailableLanguages.add(new Language("AzeriCyrillic", true, false, false));
		mAvailableLanguages.add(new Language("AzeriLatin", true, true, false));
		mAvailableLanguages.add(new Language("Bashkir", true, false, false));
		mAvailableLanguages.add(new Language("Basque", true, true, false));
		mAvailableLanguages.add(new Language("Belarusian", true, false, false));
		mAvailableLanguages.add(new Language("Bemba", true, true, false));
		mAvailableLanguages.add(new Language("Blackfoot", true, true, false));
		mAvailableLanguages.add(new Language("Breton", true, true, false));
		mAvailableLanguages.add(new Language("Bugotu", true, true, false));
		mAvailableLanguages.add(new Language("Bulgarian", true, true, false));
		mAvailableLanguages.add(new Language("Buryat", true, true, false));
		mAvailableLanguages.add(new Language("Catalan", true, false, false));
		mAvailableLanguages.add(new Language("Chamorro", true, true, false));
		mAvailableLanguages.add(new Language("Chechen", true, false, false));
		mAvailableLanguages.add(new Language("ChinesePRC", true, false, true));
		mAvailableLanguages.add(new Language("ChineseTaiwan", true, false, true));
		mAvailableLanguages.add(new Language("Chukcha", true, false, false));
		mAvailableLanguages.add(new Language("Chuvash", true, false, false));
		mAvailableLanguages.add(new Language("CMC7", true, false, false));
		mAvailableLanguages.add(new Language("Corsican", true, true, false));
		mAvailableLanguages.add(new Language("CrimeanTatar", true, true, false));
		mAvailableLanguages.add(new Language("Croatian", true, true, false));
		mAvailableLanguages.add(new Language("Crow", true, true, false));
		mAvailableLanguages.add(new Language("Czech", true, true, true));
		mAvailableLanguages.add(new Language("Danish", true, true, true));
		mAvailableLanguages.add(new Language("Dargwa", true, false, false));
		mAvailableLanguages.add(new Language("Digits", true, true, false));
		mAvailableLanguages.add(new Language("Dungan", true, false, false));
		mAvailableLanguages.add(new Language("Dutch", true, true, true));
		mAvailableLanguages.add(new Language("DutchBelgian", true, true, false));
		mAvailableLanguages.add(new Language("E13B", true, false, false));
		mAvailableLanguages.add(new Language("English", true, true, true));
		mAvailableLanguages.add(new Language("EskimoCyrillic", true, false, false));
		mAvailableLanguages.add(new Language("EskimoLatin", true, false, false));
		mAvailableLanguages.add(new Language("Esperanto", true, false, false));
		mAvailableLanguages.add(new Language("Estonian", true, true, true));
		mAvailableLanguages.add(new Language("Even", true, true, false));
		mAvailableLanguages.add(new Language("Evenki", true, true, false));
		mAvailableLanguages.add(new Language("Faeroese", true, false, false));
		mAvailableLanguages.add(new Language("Fijian", true, true, false));
		mAvailableLanguages.add(new Language("Finnish", true, true, true));
		mAvailableLanguages.add(new Language("French", true, true, true));
		mAvailableLanguages.add(new Language("Frisian", true, true, false));
		mAvailableLanguages.add(new Language("Friulian", true, true, false));
		mAvailableLanguages.add(new Language("GaelicScottish", true, true, false));
		mAvailableLanguages.add(new Language("Gagauz", true, false, false));
		mAvailableLanguages.add(new Language("Galician", true, true, false));
		mAvailableLanguages.add(new Language("Ganda", true, true, false));
		mAvailableLanguages.add(new Language("German", true, true, true));
		mAvailableLanguages.add(new Language("GermanLuxembourg", true, true, false));
		mAvailableLanguages.add(new Language("GermanNewSpelling", true, true, false));
		mAvailableLanguages.add(new Language("Greek", true, true, true));
		mAvailableLanguages.add(new Language("Guarani", true, true, false));
		mAvailableLanguages.add(new Language("Hani", true, true, false));
		mAvailableLanguages.add(new Language("Hausa", true, false, false));
		mAvailableLanguages.add(new Language("Hawaiian", true, true, false));
		mAvailableLanguages.add(new Language("Hebrew", true, false, false));
		mAvailableLanguages.add(new Language("Hungarian", true, true, false));
		mAvailableLanguages.add(new Language("Icelandic", true, false, false));
		mAvailableLanguages.add(new Language("Ido", true, true, false));
		mAvailableLanguages.add(new Language("Indonesian", true, true, true));
		mAvailableLanguages.add(new Language("Ingush", true, false, false));
		mAvailableLanguages.add(new Language("Interlingua", true, true, false));
		mAvailableLanguages.add(new Language("Irish", true, true, false));
		mAvailableLanguages.add(new Language("Italian", true, true, true));
		mAvailableLanguages.add(new Language("Japanese", true, false, true));
		mAvailableLanguages.add(new Language("Kabardian", true, false, false));
		mAvailableLanguages.add(new Language("Kalmyk", true, false, false));
		mAvailableLanguages.add(new Language("KarachayBalkar", true, true, false));
		mAvailableLanguages.add(new Language("Karakalpak", true, false, false));
		mAvailableLanguages.add(new Language("Kasub", true, true, false));
		mAvailableLanguages.add(new Language("Kawa", true, true, false));
		mAvailableLanguages.add(new Language("Kazakh", true, true, false));
		mAvailableLanguages.add(new Language("Khakas", true, false, false));
		mAvailableLanguages.add(new Language("Khanty", true, false, false));
		mAvailableLanguages.add(new Language("Kikuyu", true, false, false));
		mAvailableLanguages.add(new Language("Kirgiz", true, true, false));
		mAvailableLanguages.add(new Language("Kongo", true, true, false));
		mAvailableLanguages.add(new Language("Korean", true, false, true));
		mAvailableLanguages.add(new Language("KoreanHangul", true, false, false));
		mAvailableLanguages.add(new Language("Koryak", true, false, false));
		mAvailableLanguages.add(new Language("Kpelle", true, true, false));
		mAvailableLanguages.add(new Language("Kumyk", true, true, false));
		mAvailableLanguages.add(new Language("Kurdish", true, true, false));
		mAvailableLanguages.add(new Language("Lak", true, false, false));
		mAvailableLanguages.add(new Language("Lappish", true, true, false));
		mAvailableLanguages.add(new Language("Latin", true, true, false));
		mAvailableLanguages.add(new Language("Latvian", true, true, false));
		mAvailableLanguages.add(new Language("LatvianGothic", true, false, false));
		mAvailableLanguages.add(new Language("Lezgin", true, false, false));
		mAvailableLanguages.add(new Language("Lithuanian", true, true, false));
		mAvailableLanguages.add(new Language("Luba", true, true, false));
		mAvailableLanguages.add(new Language("Macedonian", true, false, false));
		mAvailableLanguages.add(new Language("Malagasy", true, true, false));
		mAvailableLanguages.add(new Language("Malay", true, false, false));
		mAvailableLanguages.add(new Language("Malinke", true, true, false));
		mAvailableLanguages.add(new Language("Maltese", true, false, false));
		mAvailableLanguages.add(new Language("Mansi", true, false, false));
		mAvailableLanguages.add(new Language("Maori", true, true, false));
		mAvailableLanguages.add(new Language("Mari", true, false, false));
		mAvailableLanguages.add(new Language("Maya", true, true, false));
		mAvailableLanguages.add(new Language("Miao", true, true, false));
		mAvailableLanguages.add(new Language("Minankabaw", true, true, false));
		mAvailableLanguages.add(new Language("Mohawk", true, true, false));
		mAvailableLanguages.add(new Language("Mongol", true, true, false));
		mAvailableLanguages.add(new Language("Mordvin", true, true, false));
		mAvailableLanguages.add(new Language("Nahuatl", true, true, false));
		mAvailableLanguages.add(new Language("Nenets", true, true, false));
		mAvailableLanguages.add(new Language("Nivkh", true, true, false));
		mAvailableLanguages.add(new Language("Nogay", true, true, false));
		mAvailableLanguages.add(new Language("Norwegian", true, true, true));
		mAvailableLanguages.add(new Language("NorwegianBokmal", true, true, true));
		mAvailableLanguages.add(new Language("NorwegianNynorsk", true, true, true));
		mAvailableLanguages.add(new Language("Nyanja", true, true, false));
		mAvailableLanguages.add(new Language("Occidental", true, false, false));
		mAvailableLanguages.add(new Language("Ojibway", true, true, false));
		mAvailableLanguages.add(new Language("OldEnglish", true, true, false));
		mAvailableLanguages.add(new Language("OldFrench", true, true, false));
		mAvailableLanguages.add(new Language("OldGerman", true, true, false));
		mAvailableLanguages.add(new Language("OldItalian", true, true, false));
		mAvailableLanguages.add(new Language("OldSlavonic", true, false, false));
		mAvailableLanguages.add(new Language("OldSpanish", true, true, false));
		mAvailableLanguages.add(new Language("Ossetic", true, false, false));
		mAvailableLanguages.add(new Language("Papiamento", true, true, false));
		mAvailableLanguages.add(new Language("PidginEnglish", true, true, false));
		mAvailableLanguages.add(new Language("Polish", true, true, true));
		mAvailableLanguages.add(new Language("PortugueseBrazilian", true, true, true));
		mAvailableLanguages.add(new Language("PortugueseStandard", true, true, true));
		mAvailableLanguages.add(new Language("Provencal", true, false, false));
		mAvailableLanguages.add(new Language("Quechua", true, false, false));
		mAvailableLanguages.add(new Language("RhaetoRomanic", true, true, false));
		mAvailableLanguages.add(new Language("Romanian", true, true, false));
		mAvailableLanguages.add(new Language("RomanianMoldavia", true, true, false));
		mAvailableLanguages.add(new Language("Romany", true, true, false));
		mAvailableLanguages.add(new Language("Ruanda", true, true, false));
		mAvailableLanguages.add(new Language("Rundi", true, true, false));
		mAvailableLanguages.add(new Language("RussianOldSpelling", true, false, false));
		mAvailableLanguages.add(new Language("Russian", true, true, true));
		mAvailableLanguages.add(new Language("Samoan", true, true, false));
		mAvailableLanguages.add(new Language("Selkup", true, true, false));
		mAvailableLanguages.add(new Language("SerbianCyrillic", true, true, false));
		mAvailableLanguages.add(new Language("SerbianLatin", true, true, false));
		mAvailableLanguages.add(new Language("Shona", true, false, false));
		mAvailableLanguages.add(new Language("Sioux", true, true, false));
		mAvailableLanguages.add(new Language("Slovak", true, true, false));
		mAvailableLanguages.add(new Language("Slovenian", true, true, false));
		mAvailableLanguages.add(new Language("Somali", true, true, false));
		mAvailableLanguages.add(new Language("Sorbian", true, false, false));
		mAvailableLanguages.add(new Language("Sotho", true, true, false));
		mAvailableLanguages.add(new Language("Spanish", true, true, true));
		mAvailableLanguages.add(new Language("Sunda", true, false, false));
		mAvailableLanguages.add(new Language("Swahili", true, true, false));
		mAvailableLanguages.add(new Language("Swazi", true, true, false));
		mAvailableLanguages.add(new Language("Swedish", true, true, true));
		mAvailableLanguages.add(new Language("Tabassaran", true, false, false));
		mAvailableLanguages.add(new Language("Tagalog", true, true, false));
		mAvailableLanguages.add(new Language("Tahitian", true, true, false));
		mAvailableLanguages.add(new Language("Tajik", true, true, false));
		mAvailableLanguages.add(new Language("Tatar", true, false, false));
		mAvailableLanguages.add(new Language("Thai", true, false, false));
		mAvailableLanguages.add(new Language("Tinpo", true, true, false));
		mAvailableLanguages.add(new Language("Tongan", true, true, false));
		mAvailableLanguages.add(new Language("Tswana", true, true, false));
		mAvailableLanguages.add(new Language("Tun", true, true, false));
		mAvailableLanguages.add(new Language("Turkish", true, true, false));
		mAvailableLanguages.add(new Language("Turkmen", true, false, false));
		mAvailableLanguages.add(new Language("Tuvin", true, true, false));
		mAvailableLanguages.add(new Language("Udmurt", true, false, false));
		mAvailableLanguages.add(new Language("UighurCyrillic", true, false, false));
		mAvailableLanguages.add(new Language("UighurLatin", true, true, false));
		mAvailableLanguages.add(new Language("Ukrainian", true, true, true));
		mAvailableLanguages.add(new Language("UzbekCyrillic", true, false, false));
		mAvailableLanguages.add(new Language("UzbekLatin", true, true, false));
		mAvailableLanguages.add(new Language("Vietnamese", true, false, false));
		mAvailableLanguages.add(new Language("Visayan", true, true, false));
		mAvailableLanguages.add(new Language("Welsh", true, false, false));
		mAvailableLanguages.add(new Language("Wolof", true, true, false));
		mAvailableLanguages.add(new Language("Xhosa", true, true, false));
		mAvailableLanguages.add(new Language("Yakut", true, false, false));
		mAvailableLanguages.add(new Language("Yiddish", true, false, false));
		mAvailableLanguages.add(new Language("Zapotec", true, true, false));
		mAvailableLanguages.add(new Language("Zulu", true, false, false));
	}
}
