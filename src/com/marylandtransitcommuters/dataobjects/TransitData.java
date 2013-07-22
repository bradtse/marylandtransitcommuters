package com.marylandtransitcommuters.dataobjects;

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

import com.marylandtransitcommuters.MainActivity;
import com.marylandtransitcommuters.service.TransitService;

/**
 * Singleton containing info on the current search being done. Also contains
 * a bunch of methods that allow you to add/get data.
 * FIXME I want to update this so that each piece of data is its own object
 */
public final class TransitData {
	public static final String STOP_NAME = "stop_name";
	public static final String ARRIVAL_TIME = "arrival_time";
	public static final String ARRIVAL_TIME_SECONDS = "arrival_time_seconds";
	public static final String STOP_ID = "stop_id";
	private static TransitData instance;
	
	private String stopId;
	private Route route;
	private Direction direction;
	private JSONArray stopsData;
	private JSONArray timesData;

	private TransitData() {}
	
	/**
	 * Singleton getter
	 * @return the singleton instance
	 */
	public static TransitData getInstance() {
		if (instance == null) {
			instance = new TransitData();
		}
		return instance;
	}
	
	/**
	 * Stores data in the singleton in the form of a JSONArray
	 * @param type the type of the data that is passed in
	 * @param data the JSONArray containing the data
	 */
	public void setData(TransitService.DataType type, JSONArray data) {
		switch (type) {
			case ROUTES:
				this.route = new Route(data);
				break;
			case DIRECTIONS:
				this.direction = new Direction(data);
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
	
	/*
	 * Route methods
	 */
	
	public void setRoute(String routeId) {
		route.selectRoute(routeId);
	}
	
	public String getRouteId() {
		return (route == null) ? null : route.getRouteId();
	}
	
	public String getRouteShortName() {
		return (route == null) ? null : route.getShortName();
	}
	
	public String getRouteLongName() {
		return (route == null) ? null : route.getLongName();
	}
	
	public ArrayList<HashMap<String, String>> getRoutesList() {
		return (route == null) ? null : route.getRoutesList();
	}

	/*
	 * Direction methods
	 */
	
	public void setDirection(String dirId) {
		direction.selectDirection(dirId);
	}
	
	public String getDirectionId() {
		return (direction == null) ? null : direction.getDirectionId();
	}
	
	public String getDirectionHeadsign() {
		return (direction == null) ? null : direction.getHeadsign();
	}
	
	public ArrayList<HashMap<String, String>> getDirectionsList() {
		return (direction == null) ? null : direction.getDirectionsList();
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

	/**
	 * A helper function that returns a formatted string of time until the next arrival.
	 * Ex: "2 hrs and 3 mins"
	 * @param currTimeSecs the current time in seconds
	 * @param arrivalTimeSecs the bus's arrival time in seconds
	 * @return a formatted period of time until arrival
	 * FIXME not handling all cases properly
	 */
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
	
	/**
	 * Returns a human readable clock time from the arrival time in seconds
	 * @param arrivalTimeSecs the arrival time in seconds
	 * @return a string of the clock time
	 * FIXME not handling all cases properly
	 */
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
