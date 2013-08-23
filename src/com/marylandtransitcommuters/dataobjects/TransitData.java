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
		route = new Route();
		direction = new Direction();
		startStop = new StartStop();
		finalStop = new FinalStop();
		time = new Time();
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
	 * Singleton setter
	 */
	public static void setInstance(TransitData data) {
		instance = data;
	}
	
	/**
	 * Stores data in the singleton in the form of a JSONArray
	 * @param type the type of the data that is passed in
	 * @param data the JSONArray containing the data
	 */
	public void setData(TransitService.DataType type, JSONArray data) {
		switch (type) {
			case ROUTES:
				instance.route.setData(data);
				break;
			case DIRECTIONS:
				instance.direction.setData(data);
				break;
			case STARTSTOPS:
				instance.startStop.setData(data);
				break;
			case FINALSTOPS:
				instance.finalStop.setData(data);
				break;
			case TIMES:
				instance.time.setData(data);
				break;
			default:
				Log.d(MainActivity.LOG_TAG, "setData() should never use default case");
		}
	}
	
	/*
	 * Route methods
	 */
	
	public void selectRoute(String routeId, String shortName, String longName) {
		instance.route.setRouteInfo(routeId, shortName, longName);
	}
	
	public String getRouteId() {
		return instance.route.getRouteId();
	}
	
	public String getRouteShortName() {
		return instance.route.getShortName();
	}
	
	public String getRouteLongName() {
		return instance.route.getLongName();
	}
	
	public ArrayList<HashMap<String, String>> getRoutesList() {
		return instance.route.getRoutesList();
	}

	/*
	 * Direction methods
	 */
	
	public void selectDirection(String dirId, String headSign) {
		instance.direction.setDirectionInfo(dirId, headSign);
	}
	
	public String getDirectionId() {
		return instance.direction.getDirectionId();
	}
	
	public String getDirectionHeadsign() {
		return instance.direction.getHeadsign();
	}
	
	public ArrayList<HashMap<String, String>> getDirectionsList() {
		return instance.direction.getDirectionsList();
	}
		
	/*
	 * Start Stop methods
	 */
	
	public void selectStartStop(String stopId, String shortName, String stopSeq) {
		instance.startStop.setStopInfo(stopId, shortName, stopSeq);
	}
	
	public String getStartStopId() {
		return instance.startStop.getStopId();
	}
	
	public String getStartStopName() {
		return instance.startStop.getStopName();
	}
	
	public String getStartStopSeq() {
		return instance.startStop.getStopSequence();
	}
	
	public ArrayList<HashMap<String, String>> getStartStopsList() {
		return instance.startStop.getStopsList();
	}
	
	/*
	 * Final Stop methods
	 */

	public void selectFinalStop(String stopId, String stopName) {
		instance.finalStop.setStopInfo(stopId, stopName);
	}
	
	public String getFinalStopId() {
		return instance.finalStop.getStopId();
	}
	
	public String getFinalStopName() {
		return instance.finalStop.getStopName();
	}

	public ArrayList<HashMap<String, String>> getFinalStopsList() {
		return instance.finalStop.getStopsList();
	}

	/*
	 * Time methods
	 */

	public ArrayList<HashMap<String, String>> getTimesList() {
		return instance.time.getTimesList();
	}

	public void updateTimesList() {
		instance.time.updateTimesList();
	}
}
