package com.marylandtransitcommuters.util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.marylandtransitcommuters.MainActivity;

public final class Cloner {
	
	private Cloner() {}
	
	/**
	 * Deep clones the JSONArray passed in
	 * @param json The JSONArray to clone
	 * @param keys The keys to clone
	 * @return JSONArray clone
	 */
	public static JSONArray deepCloneJSON(JSONArray json, String[] keys) {
		JSONArray clone = new JSONArray();
		
		for (int i = 0; i < json.length(); i++) {
			try {
				JSONObject original = json.getJSONObject(i);
				JSONObject copy = new JSONObject(original, keys);
				clone.put(copy);			
			} catch (JSONException e) {
				Log.d(MainActivity.LOG_TAG, "deepClone() failed: " + e.getMessage());
			}
		}
		
		return clone;
	}
}
