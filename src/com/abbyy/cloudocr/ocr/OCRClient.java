package com.abbyy.cloudocr.ocr;

public abstract class OCRClient {
	
	public final static String STATUS_IN_PROGRESS = "InProgress";
	public final static String STATUS_QUEUED = "Queued";
	public final static String STATUS_COMPLETED = "Completed";
	
	public abstract void prepareRecognition();
	
	public abstract void setParameters();
	
	public abstract void connect();
	
	public abstract void getResponse();
	
	public abstract Object getResult();
	
	public String parseResponse(){
		return null;
	}
}
