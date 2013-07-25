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
 * a bunch of methods that allow you to add/get data. Acts as the interface
 * to the each of the objects.
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
	
	public void selectRoute(String routeId, String shortName, String longName) {
		route.setRouteInfo(routeId, shortName, longName);
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
	
	public void selectDirection(String dirId, String headSign) {
		direction.setDirectionInfo(dirId, headSign);
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

	public void setFinalStop(String stopId, String stopName, String stopSequence) {
		finalStop.setStopInfo(stopId, stopName, stopSequence);
	}
	
	public String getFinalStopId() {
		return (finalStop == null) ? null : finalStop.getStopId();
	}
	
	public String getFinalStopName() {
		return (finalStop == null) ? null : finalStop.getStopName();
	}
	
	public String getFinalStopSeq() {
		return (finalStop == null) ? null : finalStop.getStopSequence();
	}
	
	public ArrayList<HashMap<String, String>> getFinalStopsList() {
		return (finalStop == null) ? null : finalStop.getStopsList();
	}
	
	/*
	 * Start Stop methods
	 */
	
	public void selectStartStop(String stopId, String shortName) {
		startStop.setStopInfo(stopId, shortName);
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
