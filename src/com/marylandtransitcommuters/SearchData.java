package com.marylandtransitcommuters;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;

import android.util.Log;

/*
 * Singleton containing info on the current search being done
 */
public final class SearchData {
	public static final String KEY = "profile";
	private static SearchData instance = null;
	private int direction = -1;
	private int routeIndex = -1;
	private int stopIndex = -1;
	private JSONArray routesData = null;
	private JSONArray stopsData = null;
	private JSONArray timesData = null;
//	private DateTime[] timeList;
	
	private SearchData() {
	}
	
	public static SearchData getInstance() {
		if (instance == null) {
			instance = new SearchData();
		}
		return instance;
	}

	public void setRouteIndex(int index) {
		this.routeIndex = index;
	}
	
	public String getRouteId() {
		if (routesData != null) {
			try {
				return routesData.getJSONObject(routeIndex).getString("route_id");
			} catch (JSONException e) {
				e.printStackTrace();
			}
		} 
		return null;
	}
	
	public void putRoutesData(JSONArray json) {
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
	
	public void putStopsData(JSONArray json) {
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
	
	public void setStopIndex(int index) {
		this.stopIndex = index;
	}
	
	public String getStopId() {
		if (stopsData != null) {
			try {
				return stopsData.getJSONObject(stopIndex).getString("stop_id");
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	public void putTimesData(JSONArray json) {
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
