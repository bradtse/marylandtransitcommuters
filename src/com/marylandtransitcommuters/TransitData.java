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
public final class TransitData {
	public static final String ROUTE_SHORT_NAME = "route_short_name";
	public static final String ROUTE_LONG_NAME = "route_long_name";
	public static final String TRIP_HEADSIGN = "trip_headsign";
	public static final String STOP_NAME = "stop_name";
	public static final String ARRIVAL_TIME = "arrival_time";
	public static final String ARRIVAL_TIME_SECONDS = "arrival_time_seconds";
	public static final String STOP_ID = "stop_id";
	public static final String ROUTE_ID = "route_id";
	public static final String DIR_ID = "direction_id";	
	private static TransitData instance;
	
	private String routeId;
	private String routeShortName;
	private String directionId;
	private String stopId;
	private JSONArray routesData;
	private JSONArray directionsData;
	private JSONArray stopsData;
	private JSONArray timesData;

	private TransitData() {}
	
	/**
	 * Singleton getter
	 * @return the singleton instance
	 */
	public static TransitData getInstance() {
		return (instance == null) ? (instance = new TransitData()) : instance;
	}
	
	/**
	 * Stores data in the singleton in the form of a JSONArray
	 * @param type the type of the data that is passed in
	 * @param data the JSONArray containing the data
	 */
	public void setData(TransitService.DataType type, JSONArray data) {
		switch(type) {
			case ROUTES:
				this.routesData = data;
				break;
			case DIRECTIONS:
				this.directionsData = fixDirectionsData(data);
				break;
			case STOPS:
				this.stopsData = data;
				break;
			case TIMES:
				this.timesData = data;
				break;
			default:
				Log.d(MainActivity.LOG_TAG, "setData() should never use default case");
		}
	}
	
	/**
	 * Removes unnecessary text from the direction's trip_headsign column
	 * @param data the JSONArray containing the data that needs to be fixed
	 * @return the fixed JSONArray
	 * FIXME not handling all cases properly. Ex: Route 3X and 40
	 */
	private JSONArray fixDirectionsData(JSONArray data) {
		for (int i = 0; i < data.length(); i++) {
			String headsign = null;
			JSONObject temp = null;
			try {
				temp = data.getJSONObject(i);
				headsign = temp.getString(TRIP_HEADSIGN);
				if (headsign.contains(routeShortName) == true) {
					headsign = headsign.replaceFirst(routeShortName, "to");
					temp.put(TRIP_HEADSIGN, headsign);
					data.put(i, temp);
				}
			} catch (JSONException e) {
				Log.d(MainActivity.LOG_TAG, "formatData() failed: " + e.getMessage());
			}		
		}
		return data;
	}
	
	/*
	 * Route methods
	 */
	
	/**
	 * Sets the RouteId that is associated with the route/item that was selected
	 * @param index the index of the route/item that was selected from the ListView
	 */
	public void setRouteId(int index) {
		try {
			routeShortName = routesData.getJSONObject(index).getString(ROUTE_SHORT_NAME);
			routeId = routesData.getJSONObject(index).getString(ROUTE_ID);
		} catch (JSONException e) {
			Log.d(MainActivity.LOG_TAG, "setRouteId() failed: " + e.getMessage());
		}
	}
	
	public String getRouteId() {
		return routeId;
	}
	
	/**
	 * This method is created specifically to work with the SimpleAdapter that the
	 * RoutesFragment is using. The SimpleAdapter requires an ArrayList of HashMaps, where each
	 * HashMap correlates to a row in the ListView. Each HashMap contains a key->value
	 * pair for each piece of data that you wish to display. In this case I have two
	 * key->value pairs per HashMap, which correlates to the route's short and long name.
	 * @return an ArrayList of HashMaps containing the short and long names for
	 * every route
	 */
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
	
	/**
	 * Returns the route's short name that correlates with the index parameter
	 * @param index the index of the route we are interested in
	 * @return the route's short name
	 */
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
			Log.d(MainActivity.LOG_TAG, e.getMessage());
		}
		return shortName;
	}
	
	/**
	 * Returns the route's long name that correlates with the index parameter
	 * @param index the index of the route we are interested in
	 * @return the route's long name
	 */
	private String getLongName(int index) {
		String longName = null;
		try {
			longName = routesData.getJSONObject(index).getString(ROUTE_LONG_NAME);
			longName = longName.replaceAll("\\s*-\\s*", " to ");
			longName = longName.replaceAll(" TO ", " to ");
		} catch (JSONException e) {
			Log.d(MainActivity.LOG_TAG, e.getMessage());
		}
		return longName;
	}
	
	/*
	 * Direction methods
	 */
	
	/**
	 * Sets the DirectionId that is associated with the direction/item that was selected
	 * @param index the index of the direction/item that was selected from the ListView
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
	
	/**
	 * Returns a list of directions for the current route
	 * @return the String array containing a list of directions
	 */
	public String[] getDirectionsList() {
		ArrayList<String> list = new ArrayList<String>();
		for (int i = 0; i < directionsData.length(); i++) {
			try {
				list.add(directionsData.getJSONObject(i).getString(TRIP_HEADSIGN));
			} catch (JSONException e) {
				Log.d(MainActivity.LOG_TAG, e.getMessage());
			}
		}
		String[] empty = new String[list.size()];
		return list.toArray(empty);
	}
	
	/*
	 * Stop methods
	 */

	/**
	 * Sets the StopId that is associated with the stop/item that was selected
	 * @param index the index of the stop/item that was selected from the ListView
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
	
	/**
	 * Returns a list of stops for the current route
	 * @return the String array containing a list of stops
	 */
	public String[] getStopsList() {
		ArrayList<String> list = new ArrayList<String>();
		for (int i = 0; i < stopsData.length(); i++) {
			try {
				list.add(stopsData.getJSONObject(i).getString(STOP_NAME));
			} catch (JSONException e) {
				Log.d(MainActivity.LOG_TAG, e.getMessage());
			}
		}
		String[] empty = new String[list.size()];
		return list.toArray(empty);
	}
	
	/*
	 * Time methods
	 */

	/**
	 * Returns a list of times for the current route, direction, and stop
	 * @return the String array containing a list of times
	 * FIXME
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
				Log.d(MainActivity.LOG_TAG, e.getMessage());
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
