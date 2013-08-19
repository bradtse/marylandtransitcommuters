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
import org.json.JSONObject;

import android.util.Log;

import com.marylandtransitcommuters.MainActivity;

public class Time {
	public static final String ARRIVAL_TIME = "arrival_time";
	public static final String ARRIVAL_TIME_SECONDS = "arrival_time_seconds";
	private static final String[] KEYS = {ARRIVAL_TIME, ARRIVAL_TIME_SECONDS};
	
	private JSONArray rawData;
	private JSONArray prettyData;
	
	public Time() {}
	
	public Time(JSONArray data) {
		this.rawData = data;
		this.prettyData = data;
	}
	
	public void setData(JSONArray data) {
		this.rawData = data;
		this.prettyData = data;
	}
	
	public ArrayList<HashMap<String, String>> getTimesList() {
		return createTimesList();
	}
	
	/**
	 * Returns a list of times for the current route, direction, and stop
	 * @return the String array containing a list of times
	 * FIXME
	 */
	private ArrayList<HashMap<String, String>> createTimesList() {
		ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
		DateTime dt = new DateTime();
		int current = dt.getSecondOfDay();
		
		for (int i = 0; i < rawData.length(); i++) {
			try {
				HashMap<String, String> map = new HashMap<String, String>();
				JSONObject json = rawData.getJSONObject(i);
				String secs = json.getString(ARRIVAL_TIME_SECONDS);
				int arrival = Integer.parseInt(secs);
				
				if (arrival < current) {
					continue;
				}
				
				String result = getBusTime(arrival, current);
				map.put(ARRIVAL_TIME, result);
				list.add(map);
			} catch (JSONException e) {
				Log.d(MainActivity.LOG_TAG, e.getMessage());
			}
		}
		
		return list;
	}
	
	/**
	 * Returns the final string for the bus. I mainly created this helper method
	 * for testing purposes.
	 * @return A String in the form of "<period> until <clocktime>"
	 */
	public String getBusTime(int arrival, int current) {
		StringBuilder time = new StringBuilder();

		String remaining = timeUntilArrival(current, arrival);
		String clockTime = clockTime(arrival);
		
		if (remaining.isEmpty()) {
			time.append("Arriving now at ");
		} else {
			time.append(remaining);
			time.append(" until " );
		}
		
		time.append(clockTime);
		
//		Log.d(MainActivity.LOG_TAG, time.toString());
		
		return time.toString();
	}
	
	/**
	 * A helper function that returns a formatted string of the time until the next arrival.
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
				.appendSeparator(" ")
				.appendHours()
				.appendSuffix(" hr", " hrs")
				.appendSeparator(" " )
				.appendMinutes()
				.appendSuffix(" min", " mins")
				.toFormatter();
		return dhm.print(period.normalizedStandard());
	}	
	
	/**
	 * Returns a human readable clock time from the arrival time in seconds
	 * @param arrival the arrival time in seconds
	 * @return a string of the clock time
	 * FIXME not handling all cases properly
	 */
	private String clockTime(int arrival) {
		Seconds seconds = Seconds.seconds(arrival);
		int hours = seconds.toStandardHours().getHours() % 24;
		int minutes = seconds.toStandardMinutes().getMinutes() % 60;
		
		LocalTime lt = new LocalTime(hours, minutes);
		DateTimeFormatter fmt = DateTimeFormat.forPattern("h:mm");
		
		String clockTime = fmt.print(lt);
		return (hours < 12) ? clockTime + " AM" : clockTime + " PM"; 
	}
}
