package com.marylandtransitcommuters.dataobjects;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.marylandtransitcommuters.MainActivity;
import com.marylandtransitcommuters.util.Cloner;

public class Direction {
	public static final String DIR_ID = "direction_id";
	public static final String TRIP_HEADSIGN = "trip_headsign";
	public static final String GLUE = "to";
	public static final String KEY = DIR_ID;
	private static final String[] KEYS = {DIR_ID, TRIP_HEADSIGN};
	private static final String ZERO = "0";
	private static final String ONE = "1";
	
	private JSONArray rawData; // The raw data directly from the server
	private JSONArray prettyData; // Prettier data. Mostly everything will use this.
	private String directionId;
	private String headsign;
	private ArrayList<HashMap<String, String>> dirsList;
	
	public Direction() {}
	
	/**
	 * Constructor
	 * @param data The direction data returned by the server
	 */
	public Direction(JSONArray data) {
		this.rawData = data;
		this.prettyData = prettify(data);
		this.dirsList = createDirectionsList();
	}
	
	public void setData(JSONArray data) {
		this.rawData = data;
		this.prettyData = prettify(data);
		this.dirsList = createDirectionsList();
	}
	
	public void setDirectionInfo(String dirId, String headSign) {
		this.directionId = dirId;
		this.headsign = headSign;
	}
	
	public JSONArray getRawData() {
		return rawData;
	}
	
	public String getDirectionId() {
		return directionId;
	}
	
	public String getHeadsign() {
		return headsign;
	}
	
	public ArrayList<HashMap<String, String>> getDirectionsList() {
		return dirsList;
	}
	
	private ArrayList<HashMap<String, String>> createDirectionsList() {
		ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
		
		for (int i = 0; i < prettyData.length(); i++) {
			try {
				HashMap<String, String> map = new HashMap<String, String>();
				JSONObject direction = prettyData.getJSONObject(i);
				
				String headsign = direction.getString(TRIP_HEADSIGN);
				String dirId = direction.getString(DIR_ID);
				
				map.put(TRIP_HEADSIGN, headsign);
				map.put(DIR_ID, dirId);
				
				list.add(map);
			} catch (JSONException e) {
				Log.d(MainActivity.LOG_TAG, e.getMessage());
			}
		}
		
		return list;
	}
	
	/**
	 * Removes unnecessary text from the direction's trip_headsign column
	 * @param data the JSONArray containing the data that needs to be fixed
	 * @return the fixed JSONArray
	 */
	private JSONArray prettify(JSONArray data) {
		TransitData curr = TransitData.getInstance();
		String shortName = curr.getRouteShortName();
		final String REGEX = "^.*\\s*" + shortName + "\\s*";
		
		JSONArray pretty = Cloner.deepCloneJSON(data, KEYS);

		for (int i = 0; i < pretty.length(); i++) {
			try {
				JSONObject direction = pretty.getJSONObject(i);
				String tempHeadsign = direction.getString(TRIP_HEADSIGN);
				String dirId = direction.getString(DIR_ID);
				
				tempHeadsign = tempHeadsign.replaceFirst(REGEX, GLUE + " ");
				
				if (dirId.equals(ZERO)) {
					tempHeadsign = "INBOUND ".concat(tempHeadsign);
				} else if (dirId.equals(ONE)) {
					tempHeadsign = "OUTBOUND ".concat(tempHeadsign);
				}
				
				direction.put(TRIP_HEADSIGN, tempHeadsign);
				
				pretty.put(i, direction);	
			} catch (JSONException e) {
				Log.d(MainActivity.LOG_TAG, "prettify() failed: " + e.getMessage());
			}
		}
		
		return pretty;
	}
}
