package com.marylandtransitcommuters.dataobjects;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.marylandtransitcommuters.MainActivity;

public class StartStop {
	public static final String STOP_NAME = "stop_name";
	public static final String STOP_ID = "stop_id";
	public static final String KEY = "start_stop_id";
	public static final String GLUE = "&";

	private JSONArray rawData;
	private JSONArray prettyData;
	private String stopId;
	private String stopName;
	private ArrayList<HashMap<String, String>> startStopsList;
	
	public StartStop(JSONArray data) {
		this.rawData = data;
		this.prettyData = data;
		this.startStopsList = createStopsList();
	}
	
	public void setStopInfo(String stopId, String stopName) {
		this.stopId = stopId;
		this.stopName = stopName;
	}
	
	public JSONArray getRawData() {
		return rawData;
	}
	
	public String getStopId() {
		return stopId;
	}
	
	public String getStopName() {
		return stopName;
	}
	
	public ArrayList<HashMap<String, String>> getStopsList() {
		return startStopsList;
	}
	
	private ArrayList<HashMap<String, String>> createStopsList() {
		ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
		
		for (int i = 0; i < prettyData.length(); i++) {
			try {
				HashMap<String, String> map = new HashMap<String, String>();
				JSONObject stop = prettyData.getJSONObject(i);
				
				String id = stop.getString(STOP_ID);
				String sn = stop.getString(STOP_NAME);
				
				map.put(STOP_ID, id);
				map.put(STOP_NAME, sn);
				list.add(map);
			} catch (JSONException e) {
				Log.d(MainActivity.LOG_TAG, "getStopsList() failed: " + e.getMessage());
			}	
		}
		
		return list;
	}
}
