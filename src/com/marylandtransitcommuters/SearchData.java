package com.marylandtransitcommuters;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.joda.time.DateTime;
import org.joda.time.Seconds;
import org.json.JSONArray;
import org.json.JSONException;

import android.util.Log;

/*
 * Singleton containing info on the current search being done
 */
public final class SearchData {
	public static final String KEY = "profile";
	public static final String ROUTE_SHORT_NAME = "route_short_name";
	public static final String ROUTE_LONG_NAME = "route_long_name";
	public static final String STOP_NAME = "stop_name";
	public static final String ARRIVAL_TIME = "arrival_time";
	public static final String ARRIVAL_TIME_SECONDS = "arrival_time_seconds";
	private static SearchData instance;
	private int direction = -1;
	private int routeIndex = -1;
	private int stopIndex = -1;
	private JSONArray routesData;
	private JSONArray stopsData;
	private JSONArray timesData;
	private final Pattern pattern = Pattern.compile("^.*\\S-\\S.*$");

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
	
	public ArrayList<HashMap<String, String>> getRoutesList() {
		ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
		for (int i = 0; i < routesData.length(); i++) {
			HashMap<String, String> map = new HashMap<String, String>();
			String shortName = getShortName(i);
			String longName = getLongName(i);
			map.put(ROUTE_SHORT_NAME, shortName);
			map.put(ROUTE_LONG_NAME, longName);
			list.add(map);
		}
		return list;
	}
	
	private String getShortName(int index) {
		String shortName = null;
		try {
			shortName = routesData.getJSONObject(index).getString(ROUTE_SHORT_NAME);
			if (shortName == "null") {
				shortName = "N/A";
			} else if (shortName.charAt(0) == '0') {
				shortName = shortName.substring(1);
			}
		} catch (JSONException e) {
			Log.d(MainActivity.TAG, e.getMessage());
		}
		return shortName;
	}
	
	private String getLongName(int index) {
		String longName = null;
		try {
			longName = routesData.getJSONObject(index).getString(ROUTE_LONG_NAME);
			Matcher matcher = pattern.matcher(longName);
			if (matcher.matches()) {
				longName = longName.replaceAll("-", " - ");
			}
			longName = longName.replaceAll("-", "to");
		} catch (JSONException e) {
			Log.d(MainActivity.TAG, e.getMessage());
		}
		return longName;
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

	public String[] getTimesList() {
		ArrayList<String> list = new ArrayList<String>();
		DateTime dt = new DateTime();
		int currTimeSecs = dt.getSecondOfDay();
		for (int i = 0; i < timesData.length(); i++) {
			try {
				StringBuilder result = new StringBuilder();
				String arrivalTime = timesData.getJSONObject(i).getString(ARRIVAL_TIME);
				result.append(arrivalTime + " : ");
				int arrivalTimeSecs = Integer.parseInt(timesData.getJSONObject(i).getString(ARRIVAL_TIME_SECONDS));
				if (arrivalTimeSecs < currTimeSecs) {
					continue;
				}
				Seconds sec = Seconds.seconds(arrivalTimeSecs);
				sec.minus(currTimeSecs);

				result.append(sec.toStandardMinutes().toString());
				list.add(result.toString());
			} catch (JSONException e) {
				Log.d(MainActivity.TAG, e.getMessage());
			}
		}
		String[] finArr = new String[list.size()];
		return list.toArray(finArr);
	}
}
