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
	private int time;
	private String routeId;
	private String stopId;
	private JSONArray routesData;
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
		routesData = json;
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
	
	public void setTime(int time) {
		this.time = time;
	}
}
