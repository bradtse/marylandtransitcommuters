package com.marylandtransitcommuters.dataobjects;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.marylandtransitcommuters.MainActivity;
import com.marylandtransitcommuters.util.Cloner;

public class Route {
	public static final String ROUTE_ID = "route_id";
	public static final String SHORT_NAME = "route_short_name";
	public static final String LONG_NAME = "route_long_name";
	public static final String KEY = ROUTE_ID;
	private static final String[] KEYS = {ROUTE_ID, SHORT_NAME, LONG_NAME};
	
	private JSONArray rawData; // The raw data response from the server
	private JSONArray prettyData; // Prettier data. Mostly everything will use this.
	private String routeId; // dataset unique
	private String shortName;
	private String longName;
	
	/**
	 * Constructor
	 * @param data The route data returned by the server
	 */
	public Route(JSONArray data) {
		this.rawData = data;
		this.prettyData = prettify(data);
	}

	/**
	 * Select a route. This method should be used when a route is selected
	 * from the ListView of routes.
	 * @param routeId The route_id of the route that was selected
	 */
	public void selectRoute(String routeId) {
		this.routeId = routeId;
		setNames();
	}
	
	/**
	 * Returns the raw data
	 * @return A JSONArray
	 */
	public JSONArray getRawData() {
		return rawData;
	}

	/**
	 * Returns the route id of the selected route
	 * @return A String of the route id
	 */
	public String getRouteId() {
		return routeId;
	}
	
	/**
	 * Returns the short name of the selected route
	 * @return A String of the route's short name
	 */
	public String getShortName() {
		return shortName;
	}
	
	/**
	 * Returns the long name of the selected route
	 * @return A string of the route's long name
	 */
	public String getLongName() {
		return longName;
	}
	
	/**
	 * This method is created specifically to work with the SimpleAdapter that the
	 * RoutesFragment is using. The SimpleAdapter requires an ArrayList of HashMaps, where each
	 * HashMap correlates to a row in the ListView. Each HashMap contains a key->value
	 * pair for each piece of data that you wish to display. 
	 * @return an ArrayList of HashMaps 
	 */
	public ArrayList<HashMap<String, String>> getRoutesList() {
		ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
		
		for (int i = 0; i < prettyData.length(); i++) {
			try {
				HashMap<String, String> map = new HashMap<String, String>();
				JSONObject route = prettyData.getJSONObject(i);
				
				String id = route.getString(ROUTE_ID);
				String sn = route.getString(SHORT_NAME);
				String ln = route.getString(LONG_NAME);
				
				map.put(ROUTE_ID, id);
				map.put(SHORT_NAME, sn);
				map.put(LONG_NAME, ln);
				list.add(map);
			} catch (JSONException e) {
				Log.d(MainActivity.LOG_TAG, "getRoutesList failed: " + e.getMessage());
			}	
		}
		
		return list;
	}
	
	/**
	 * Returns a new copy of the raw data with some of its fields formatted to
	 * aid in user friendliness. 
	 * @param data The data to prettify
	 * @return A prettified copy of the route's raw data
	 */
	private JSONArray prettify(JSONArray data) {
		JSONArray pretty = Cloner.deepCloneJSON(data, KEYS);
		
		for (int i = 0; i < pretty.length(); i++) {
			try {
				JSONObject route = pretty.getJSONObject(i);
				String sn = route.getString(SHORT_NAME);
				String ln = route.getString(LONG_NAME);
				
				String prettyShort = prettifyShortName(sn);
				String prettyLong = prettifyLongName(ln);
				
				route.put(SHORT_NAME, prettyShort);
				route.put(LONG_NAME, prettyLong);
				
				pretty.put(i, route);
			} catch (JSONException e) {
				Log.d(MainActivity.LOG_TAG, "prettify() failed: " + e.getMessage());
			}	
		}
		
		return pretty;
	}
	
	/**
	 * Prettifys the provided route's short name 
	 * @param sn The short name to prettify
	 * @return The brand new pretty string
	 */
	private String prettifyShortName(String sn) {
		String pretty = null;
		
		if (sn == "null") {
			pretty = "N/A";
		} else if (sn.charAt(0) == '0') {
			pretty = sn.substring(1);
		} else {
			pretty = sn;
		}
		
		return pretty;
	}
	
	/**
	 * Prettifys the provided route's long name
	 * @param ln The long name to prettify
	 * @return The brand new pretty string
	 */
	private String prettifyLongName(String ln) {
		String pretty = null;
		
		pretty = ln.replaceAll("\\s*-\\s*", " to ");
		pretty = pretty.replaceAll(" TO ", " to ");
		
		return pretty;
	}
	
	/**
	 * Stores useful information about the route that was selected
	 */
	private void setNames() {
		try {
			for (int i=0; i < prettyData.length(); i++) {
				JSONObject route = prettyData.getJSONObject(i);
				String id = route.getString(ROUTE_ID);
				String sn = route.getString(SHORT_NAME);
				String ln = route.getString(LONG_NAME);
				
				if (id.equals(routeId)) {
					this.shortName = sn;
					this.longName = ln;
					break;
				}
			}
		} catch (JSONException e) {
			Log.d(MainActivity.LOG_TAG, "setNames() failed: " + e.getMessage());
		}
	}
}
