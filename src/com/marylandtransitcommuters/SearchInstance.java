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
import org.json.JSONObject;

import com.marylandtransitcommuters.service.TransitService;

import android.util.Log;

/**
 * Singleton containing info on the current search being done. Also contains
 * a bunch of methods that allow you to add/get data.
 */
public final class SearchInstance {
	public static final String ROUTE_SHORT_NAME = "route_short_name";
	public static final String ROUTE_LONG_NAME = "route_long_name";
	public static final String TRIP_HEADSIGN = "trip_headsign";
	public static final String STOP_NAME = "stop_name";
	public static final String ARRIVAL_TIME = "arrival_time";
	public static final String ARRIVAL_TIME_SECONDS = "arrival_time_seconds";
	public static final String STOP_ID = "stop_id";
	public static final String ROUTE_ID = "route_id";
	public static final String DIR_ID = "direction_id";
	
	private static SearchInstance instance;
	private String routeId;
	private String routeShortName;
	private String directionId;
	private String stopId;
	private JSONArray routesData;
	private JSONArray directionsData;
	private JSONArray stopsData;
	private JSONArray timesData;

	private SearchInstance() {}
	
	public static SearchInstance getInstance() {
		if (instance == null) {
			instance = new SearchInstance();
		}
		return instance;
	}
	
	public void setData(TransitService.DataType type, JSONArray json) {
		switch(type) {
			case ROUTES:
				this.routesData = json;
				break;
			case DIRECTIONS:
				formatData(json);
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
	
	/**
	 * Cleans up the trip_headsign column 
	 * @param json the JSON to format
	 * @return the formatted JSON array
	 */
	private void formatData(JSONArray json) {
		for (int i = 0; i < json.length(); i++) {
			String headsign = null;
			JSONObject temp = null;
			try {
				temp = json.getJSONObject(i);
				headsign = temp.getString(TRIP_HEADSIGN);
				if (headsign.contains(routeShortName) == true) {
					headsign = headsign.replaceFirst(routeShortName, "to");
					temp.put(TRIP_HEADSIGN, headsign);
					json.put(i, temp);
				}
			} catch (JSONException e) {
				Log.d(MainActivity.TAG, "formatData() failed: " + e.getMessage());
			}		
		}
	}
	
	/*
	 * Route methods
	 */
	
	public void setRouteId(int index) {
		try {
			routeShortName = routesData.getJSONObject(index).getString(ROUTE_SHORT_NAME);
			routeId = routesData.getJSONObject(index).getString(ROUTE_ID);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	public String getRouteId() {
		return routeId;
	}
	
	public ArrayList<HashMap<String, String>> getRoutesList() {
		ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
		for (int i = 0; i < routesData.length(); i++) {
			HashMap<String, String> map = new HashMap<String, String>();
			map.put(ROUTE_SHORT_NAME, getShortName(i));
			map.put(ROUTE_LONG_NAME, getLongName(i));
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
	
	/*
	 * Direction methods
	 */
	
	public void setDirectionId(int index) {
		try {
			directionId = directionsData.getJSONObject(index).getString(DIR_ID);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	public String getDirectionId() {
		return directionId;
	}
	
	public String[] getDirectionsList() {
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
	
	/*
	 * Stop methods
	 */

	public void setStopId(int index) {
		try {
			stopId = stopsData.getJSONObject(index).getString(STOP_ID);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	public String getStopId() {
		return stopId;
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
	
	/*
	 * Time methods
	 */

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
				.appendSuffix(" hr", " hrs")
				.appendSeparator(" and " )
				.appendMinutes()
				.appendSuffix(" min", " mins")
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
