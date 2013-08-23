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
	public static final String ARRIVAL_TIME_SECONDS = "arrival_time_seconds";
	
	private JSONArray rawData;
	private ArrayList<HashMap<String, String>> timesList;
	
	public Time() {}
	
	public void setData(JSONArray data) {
		this.rawData = data;
		this.timesList = createTimesList();
	}
	
	public ArrayList<HashMap<String, String>> getTimesList() {
		return timesList;
	}
	
	public void updateTimesList() {
		refreshList(this.timesList);
	}
	
	// This doesn't seem like the best method, but it should work for now
	private void refreshList(ArrayList<HashMap<String, String>> data) {
		int currentTime = new DateTime().getSecondOfDay();
		for (HashMap<String, String> map : data) {
			// This map should always only contain 1 value
			int arrivalTime = Integer.parseInt(map.get("time"));
			
			if (arrivalTime < currentTime) {
				data.remove(map);
			}

			map.put(ARRIVAL_TIME_SECONDS, getBusTimeMsg(arrivalTime, currentTime));
		}
	}
	
	/**
	 * Returns a list of times for the current route, direction, and stop
	 * @return the String array containing a list of times
	 * FIXME
	 */
	private ArrayList<HashMap<String, String>> createTimesList() {
		ArrayList<HashMap<String, String>> timesList = new ArrayList<HashMap<String, String>>();
		int currentTime = new DateTime().getSecondOfDay();
		
		for (int i = 0; i < rawData.length(); i++) {
			try {
				HashMap<String, String> busTime = new HashMap<String, String>();
				JSONObject json = rawData.getJSONObject(i);
				int arrivalTime = Integer.parseInt(json.getString(ARRIVAL_TIME_SECONDS));
				
				if (arrivalTime < currentTime) {
					continue;
				}
				
				busTime.put(ARRIVAL_TIME_SECONDS, getBusTimeMsg(arrivalTime, currentTime));
				busTime.put("time", String.valueOf(arrivalTime));
				timesList.add(busTime);
			} catch (JSONException e) {
				Log.d(MainActivity.LOG_TAG, e.getMessage());
			}
		}
		
		return timesList;
	}
	
	/**
	 * Returns the final string for the bus time. I mainly created this helper method
	 * for testing purposes.
	 * @return A String in the form of "<period> until <clocktime>"
	 */
	public String getBusTimeMsg(int arrival, int current) {
		return new StringBuilder().append(timeUntilArrival(current, arrival))
				                  .append(clockTime(arrival))
				                  .toString();
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
		return seconds.getSeconds() < 60 ? 
				   "Arriving now at " : 
			       dhm.print(period.normalizedStandard()) + " until ";
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
		
		return (hours < 12) ? fmt.print(lt) + " AM" : fmt.print(lt) + " PM"; 
	}
}
