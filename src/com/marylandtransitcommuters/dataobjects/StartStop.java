package com.marylandtransitcommuters.dataobjects;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.marylandtransitcommuters.MainActivity;

public class StartStop {
	public static final String STOP_NAME = "stop_name";
	public static final String STOP_ID = "stop_id";
	public static final String STOP_SEQ = "stop_sequence";
	public static final String STOP_KEY = "start_stop_id";
	public static final String SEQ_KEY = "start_stop_seq";
	public static final String GLUE = "&";
	private static final Pattern P = Pattern.compile("((f/?s ?)|(opp ?)|(mid ?))?([news]/?b)?$", Pattern.CASE_INSENSITIVE);

	private JSONArray rawData;
	private JSONArray prettyData;
	private String stopId;
	private String stopName;
	private String stopSequence;
	private ArrayList<HashMap<String, String>> startStopsList;
	
	public StartStop() {}

	public void setData(JSONArray data) {
		this.rawData = data;
		this.prettyData = prettify(data);
		this.startStopsList = createStopsList();
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
	
	private JSONArray prettify(JSONArray data) {
		
		for (int i = 0; i < data.length(); i++) {
			try {
				JSONObject route = data.getJSONObject(i);
				String stop_name = prettyRouteName(route.getString(STOP_NAME));
				route.put(STOP_NAME, stop_name);
				data.put(i, route);
			} catch (JSONException e) {
				Log.d(MainActivity.LOG_TAG, "prettify() failed: " + e.getMessage());
			}	
		}
		
		return data;
	}
	
	private String prettyRouteName(String name) {
		Matcher m = P.matcher(name);
		if (m.find()) {
			name = name.substring(0, m.start());
		}
		return name;
	}
}
