package com.marylandtransitcommuters;

import java.util.ArrayList;

import org.joda.time.DateTime;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

/*
 * Singleton containing info on the current search being done
 */
public final class SearchData {
	public static final String KEY = "profile";
	private static SearchData instance = null;
	private int direction;
	private String routeId;
	private String stopId;
	private JSONArray routesData;
	private JSONArray stopsData;
	private JSONArray timesData;
//	private DateTime[] timeList;
	
	private SearchData() {
	}
	
	public static SearchData getInstance() {
		if (instance == null) {
			instance = new SearchData();
		}
		return instance;
	}

	public void setRouteId(String routeId) {
		this.routeId = routeId;
	}
	
	public String getRouteId() {
		return this.routeId;
	}
	
	public void setRouteJSON(JSONArray json) {
		this.routesData = json;
	}
	
	public String[] getRoutesCol(String key) {
		int i = 0;
		int length = routesData.length();
		ArrayList<String> keyList = new ArrayList<String>();
		for (i = 0; i < length; i++) {
			try {
				keyList.add(routesData.getJSONObject(i).getString(key));
			} catch (JSONException e) {
				Log.d(MainActivity.TAG, e.getMessage());
			}
		}
		String[] finArr = new String[keyList.size()];
		finArr = keyList.toArray(finArr);
		return finArr;
	}
	
	public void setDirection(int direction) {
		this.direction = direction;
	}
	
	public int getDirection() {
		return this.direction;
	}
	
	public void setStopsJSON(JSONArray json) {
		this.stopsData = json;
	}
	
	public String[] getStopsCol(String key) {
		int i = 0;
		int length = stopsData.length();
		ArrayList<String> keyList = new ArrayList<String>();
		for (i = 0; i < length; i++) {
			try {
				keyList.add(stopsData.getJSONObject(i).getString(key));
			} catch (JSONException e) {
				Log.d(MainActivity.TAG, e.getMessage());
			}
		}
		String[] finArr = new String[keyList.size()];
		finArr = keyList.toArray(finArr);
		return finArr;
	}
	
	public void setStopId(String stopId) {
		this.stopId = stopId;
	}
	
	public String getStopId() {
		return this.stopId;
	}
	
	public void setTimesJSON(JSONArray json) {
		this.timesData = json;
	}
	
	public String[] getTimesCol(String key) {
		int i = 0;
		int length = timesData.length();
		ArrayList<String> keyList = new ArrayList<String>();
		for (i = 0; i < length; i++) {
			try {
				keyList.add(timesData.getJSONObject(i).getString(key));
			} catch (JSONException e) {
				Log.d(MainActivity.TAG, e.getMessage());
			}
		}
		String[] finArr = new String[keyList.size()];
		finArr = keyList.toArray(finArr);
		return finArr;
	}
	
}
