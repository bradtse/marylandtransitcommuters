package com.marylandtransitcommuters.dataobjects;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.marylandtransitcommuters.MainActivity;

public class FinalStop {
	public static final String STOP_NAME = "stop_name";
	public static final String STOP_ID = "stop_id";
	public static final String STOP_SEQ = "stop_sequence";
	public static final String KEY = "final_stop_id";
	public static final String GLUE = "&";

	private JSONArray rawData;
	private JSONArray prettyData;
	private String stopId;
	private String stopName;
	private String stopSequence;
	
	public FinalStop(JSONArray data) {
		this.rawData = data;
		this.prettyData = data;
	}
	
	public void setStopInfo(String stopId, String stopName, String stopSequence) {
		this.stopId = stopId;
		this.stopName = stopName;
		this.stopSequence = stopSequence;
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
	
	public String getStopSequence() {
		return stopSequence;
	}
	
	public ArrayList<HashMap<String, String>> getStopsList() {
		ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
		
		for (int i = 0; i < prettyData.length(); i++) {
			try {
				HashMap<String, String> map = new HashMap<String, String>();
				JSONObject stop = prettyData.getJSONObject(i);
				
				String id = stop.getString(STOP_ID);
				String sn = stop.getString(STOP_NAME);
				String ss = stop.getString(STOP_SEQ);
				
				map.put(STOP_ID, id);
				map.put(STOP_NAME, sn);
				map.put(STOP_SEQ, ss);
				list.add(map);
			} catch (JSONException e) {
				Log.d(MainActivity.LOG_TAG, "getStopsList() failed: " + e.getMessage());
			}	
		}
		
		return list;
	}
}
