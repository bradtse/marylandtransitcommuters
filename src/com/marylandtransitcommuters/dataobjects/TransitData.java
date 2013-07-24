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
	private static TransitData instance;
	
	private Route route;
	private Direction direction;
	private FinalStop finalStop;
	private StartStop startStop;
	private Time time;

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
			case FINALSTOPS:
				this.finalStop = new FinalStop(data);
				break;
			case STARTSTOPS:
				this.startStop = new StartStop(data);
				break;
			case TIMES:
				this.time = new Time(data);
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
	 * Final Stop methods
	 */

	public void setFinalStop(String stopId) {
		finalStop.selectStop(stopId);
	}
	
	public String getFinalStopId() {
		return (finalStop == null) ? null : finalStop.getStopId();
	}
	
	public String getFinalStopName() {
		return (finalStop == null) ? null : finalStop.getStopName();
	}
	
	public ArrayList<HashMap<String, String>> getFinalStopsList() {
		return (finalStop == null) ? null : finalStop.getStopsList();
	}
	
	/*
	 * Start Stop methods
	 */
	
	public void setStartStop(String stopId) {
		startStop.selectStop(stopId);
	}
	
	public String getStartStopId() {
		return (startStop == null) ? null : startStop.getStopId();
	}
	
	public String getStartStopName() {
		return (startStop == null) ? null : startStop.getStopName();
	}
	
	public ArrayList<HashMap<String, String>> getStartStopsList() {
		return (startStop == null) ? null : startStop.getStopsList();
	}
	
	/*
	 * Time methods
	 */

	public ArrayList<HashMap<String, String>> getTimesList() {
		return (time == null) ? null : time.getTimesList();
	}
}
