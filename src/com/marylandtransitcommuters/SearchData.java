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
	private static SearchData instance;
	private int direction = -1;
	private int routeIndex = -1;
	private int stopIndex = -1;
	private JSONArray routesData;
	private JSONArray stopsData;
	private JSONArray timesData;
	
	private SearchData() {}
	
	public static SearchData getInstance() {
		if (instance == null) {
			instance = new SearchData();
		}
		return instance;
	}

	public void setIndex(int index, TransitService.Type type) {
		switch(type) {
			case ROUTES:
				this.routeIndex = index;
				break;
			case STOPS:
				this.stopIndex = index;
				break;
			case DIRECTION:
				this.direction = index;
			default:	
		}
	}
	
	public void setData(JSONArray json, TransitService.Type type) {
		switch(type) {
			case ROUTES:
				this.routesData = json;
				break;
			case STOPS:
				this.stopsData = json;
				break;
			case TIMES:
				this.timesData = json;
				break;
			default:
		}
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
	
	public String[] getRoutesCol(String key) {
		ArrayList<String> keyList = new ArrayList<String>();
		for (int i = 0; i < routesData.length(); i++) {
			try {
				keyList.add(routesData.getJSONObject(i).getString(key));
			} catch (JSONException e) {
				Log.d(MainActivity.TAG, e.getMessage());
			}
		}
		String[] finArr = new String[keyList.size()];
		return keyList.toArray(finArr);
	}
	
	public int getDirection() {
		return this.direction;
	}

	public String[] getStopsCol(String key) {
		ArrayList<String> keyList = new ArrayList<String>();
		for (int i = 0; i < stopsData.length(); i++) {
			try {
				keyList.add(stopsData.getJSONObject(i).getString(key));
			} catch (JSONException e) {
				Log.d(MainActivity.TAG, e.getMessage());
			}
		}
		String[] finArr = new String[keyList.size()];
		return keyList.toArray(finArr);
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

	public String[] getTimesCol(String key) {
		ArrayList<String> keyList = new ArrayList<String>();
		for (int i = 0; i < timesData.length(); i++) {
			try {
				keyList.add(timesData.getJSONObject(i).getString(key));
			} catch (JSONException e) {
				Log.d(MainActivity.TAG, e.getMessage());
			}
		}
		String[] finArr = new String[keyList.size()];
		return keyList.toArray(finArr);
	}
}
