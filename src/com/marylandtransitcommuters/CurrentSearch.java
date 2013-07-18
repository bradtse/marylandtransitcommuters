package com.marylandtransitcommuters;

import java.util.ArrayList;
import java.util.HashMap;

import org.joda.time.DateTime;
import org.joda.time.LocalTime;
import org.joda.time.Period;
import org.joda.time.Seconds;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;
import org.json.JSONArray;
import org.json.JSONException;

import android.util.Log;

/*
 * Singleton containing info on the current search being done
 */
public final class CurrentSearch {
	public static final String KEY = "profile";
	public static final String ROUTE_SHORT_NAME = "route_short_name";
	public static final String ROUTE_LONG_NAME = "route_long_name";
	public static final String TRIP_HEADSIGN = "trip_headsign";
	public static final String STOP_NAME = "stop_name";
	public static final String ARRIVAL_TIME = "arrival_time";
	public static final String ARRIVAL_TIME_SECONDS = "arrival_time_seconds";
	public static final String STOP_ID = "stop_id";
	public static final String ROUTE_ID = "route_id";
	public static final String DIR_ID = "direction_id";
	private static CurrentSearch instance;
	private int directionIndex = -1;
	private int routeIndex = -1;
	private int stopIndex = -1;
	private JSONArray routesData;
	private JSONArray directionsData;
	private JSONArray stopsData;
	private JSONArray timesData;

	private CurrentSearch() {}
	
	public static CurrentSearch getInstance() {
		if (instance == null) {
			instance = new CurrentSearch();
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
			case DIRECTIONS:
				this.directionIndex = index;
			default:	
		}
	}
	
	public void setData(JSONArray json, TransitService.Type type) {
		switch(type) {
			case ROUTES:
				this.routesData = json;
				break;
			case DIRECTIONS:
				this.directionsData = json;
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
				return routesData.getJSONObject(routeIndex).getString(ROUTE_ID);
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
			longName = longName.replaceAll("\\s*-\\s*", " to ");
			longName = longName.replaceAll(" TO ", " to ");
		} catch (JSONException e) {
			Log.d(MainActivity.TAG, e.getMessage());
		}
		return longName;
	}
	
	public String getDirectionId() {
		if (directionsData != null) {
			try {
				return directionsData.getJSONObject(directionIndex).getString(DIR_ID);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		} 
		return null;
	}
	
	public String[] getDirsList() {
		ArrayList<String> list = new ArrayList<String>();
		for (int i = 0; i < directionsData.length(); i++) {
			try {
				list.add(directionsData.getJSONObject(i).getString(TRIP_HEADSIGN));
			} catch (JSONException e) {
				Log.d(MainActivity.TAG, e.getMessage());
			}
		}
		String[] empty = new String[list.size()];
		return list.toArray(empty);
	}

	public String[] getStopsList() {
		ArrayList<String> list = new ArrayList<String>();
		for (int i = 0; i < stopsData.length(); i++) {
			try {
				list.add(stopsData.getJSONObject(i).getString(STOP_NAME));
			} catch (JSONException e) {
				Log.d(MainActivity.TAG, e.getMessage());
			}
		}
		String[] empty = new String[list.size()];
		return list.toArray(empty);
	}
	
	public String getStopId() {
		if (stopsData != null) {
			try {
				return stopsData.getJSONObject(stopIndex).getString(STOP_ID);
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
				int arrivalTimeSecs = Integer.parseInt(timesData.getJSONObject(i).getString(ARRIVAL_TIME_SECONDS));
				if (arrivalTimeSecs < currTimeSecs) {
					continue;
				}

				StringBuilder result = new StringBuilder();
				result.append(timeUntilArrival(currTimeSecs, arrivalTimeSecs));
				result.append(" until " );
				result.append(clockTime(arrivalTimeSecs));
				
				list.add(result.toString());
			} catch (JSONException e) {
				Log.d(MainActivity.TAG, e.getMessage());
			}
		}
		String[] empty = new String[list.size()];
		return list.toArray(empty);
	}

	private String timeUntilArrival(int currTimeSecs, int arrivalTimeSecs) {
		Seconds seconds = Seconds.seconds(arrivalTimeSecs - currTimeSecs);
		Period period = new Period(seconds);
		PeriodFormatter dhm = new PeriodFormatterBuilder()
				.appendDays()
				.appendSuffix(" day", " days")
				.appendSeparator(" and ")
				.appendHours()
				.appendSuffix(" hour", " hours")
				.appendSeparator(" and " )
				.appendMinutes()
				.appendSuffix(" minute", " minutes")
				.toFormatter();
		return dhm.print(period.normalizedStandard());
	}
	
	private String clockTime(int arrivalTimeSecs) {
		Seconds seconds = Seconds.seconds(arrivalTimeSecs);
		int hours = seconds.toStandardHours().getHours() % 24;
//		if (hours >= 24) {
//			hours = hours - 24;
//		}
		int minutes = seconds.toStandardMinutes().getMinutes() % 60;
		LocalTime lt = new LocalTime(hours, minutes);
		DateTimeFormatter fmt = DateTimeFormat.forPattern("hh:mm");
		return fmt.print(lt);
	}
}
