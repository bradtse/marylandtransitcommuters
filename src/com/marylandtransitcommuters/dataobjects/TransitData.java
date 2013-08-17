package com.marylandtransitcommuters.dataobjects;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;

import android.util.Log;

import com.marylandtransitcommuters.MainActivity;
import com.marylandtransitcommuters.service.TransitService;

/**
 * Singleton containing info on the current search being done. Also contains
 * a bunch of methods that allow you to add/get data. Acts as the interface
 * to the each of the objects containing data from the server.
 */
public final class TransitData {
	private static TransitData instance;
	
	private Route route;
	private Direction direction;
	private StartStop startStop;
	private FinalStop finalStop;
	private Time time;

	private TransitData() {
		this.route = new Route();
		this.direction = new Direction();
		this.startStop = new StartStop();
		this.finalStop = new FinalStop();
		this.time = new Time();
	}
	
	/**
	 * Singleton getter
	 * @return the singleton instance
	 */
	public static TransitData getInstance() {
		if (instance == null) {
			Log.d(MainActivity.LOG_TAG, "TransitData instance is null");
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
			case STARTSTOPS:
				this.startStop = new StartStop(data);
				break;
			case FINALSTOPS:
				this.finalStop = new FinalStop(data);
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
	 * Start Stop methods
	 */
	
	public void selectStartStop(String stopId, String shortName, String stopSeq) {
		startStop.setStopInfo(stopId, shortName, stopSeq);
	}
	
	public String getStartStopId() {
		return (startStop == null) ? null : startStop.getStopId();
	}
	
	public String getStartStopName() {
		return (startStop == null) ? null : startStop.getStopName();
	}
	
	public String getStartStopSeq() {
		return (startStop == null) ? null : startStop.getStopSequence();
	}
	
	public ArrayList<HashMap<String, String>> getStartStopsList() {
		return (startStop == null) ? null : startStop.getStopsList();
	}
	
	/*
	 * Final Stop methods
	 */

	public void selectFinalStop(String stopId, String stopName) {
		finalStop.setStopInfo(stopId, stopName);
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
	 * Time methods
	 */

	public ArrayList<HashMap<String, String>> getTimesList() {
		return (time == null) ? null : time.getTimesList();
	}
}
