package com.abbyy.cloudocr.ocr;

public class Language {
	String mName;
	boolean availableForOCR;
	boolean availableForICR;
	boolean availableForBCR;
	
	Language(String name, boolean ocr, boolean icr, boolean bcr){
		mName = name;
		availableForBCR = bcr;
		availableForICR = icr;
		availableForOCR = ocr;
	}
}
