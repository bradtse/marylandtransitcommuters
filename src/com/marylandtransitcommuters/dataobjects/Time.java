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

public class Time {
	public static final String ARRIVAL_TIME = "arrival_time";
	public static final String ARRIVAL_TIME_SECONDS = "arrival_time_seconds";
	private static final String[] KEYS = {ARRIVAL_TIME, ARRIVAL_TIME_SECONDS};
	
	private JSONArray rawData;
	private JSONArray prettyData;
	
	public Time(JSONArray data) {
		this.rawData = data;
		this.prettyData = data;
	}
	
	/**
	 * Returns a list of times for the current route, direction, and stop
	 * @return the String array containing a list of times
	 * FIXME
	 */
	public ArrayList<HashMap<String, String>> getTimesList() {
		ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
		DateTime dt = new DateTime();
		int currTimeSecs = dt.getSecondOfDay();
		for (int i = 0; i < rawData.length(); i++) {
			try {
				HashMap<String, String> map = new HashMap<String, String>();
				int arrivalTimeSecs = Integer.parseInt(rawData.getJSONObject(i).getString(ARRIVAL_TIME_SECONDS));
				if (arrivalTimeSecs < currTimeSecs) {
					continue;
				}

				StringBuilder result = new StringBuilder();
				result.append(timeUntilArrival(currTimeSecs, arrivalTimeSecs));
				result.append(" until " );
				result.append(clockTime(arrivalTimeSecs));
				
				map.put(ARRIVAL_TIME, result.toString());
				
				list.add(map);
			} catch (JSONException e) {
				Log.d(MainActivity.LOG_TAG, e.getMessage());
			}
		}
		
		return list;
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