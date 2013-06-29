package com.marylandtransitcommuters;

import org.joda.time.DateTime;

public class SearchProfile {
	public static final String KEY = "profile";
	private String routeId;
	private int time;
	private String stopId;
	private DateTime[] timeList;
	
	public SearchProfile() {
		
	}
}
