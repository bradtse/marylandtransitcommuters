package com.marylandtransitcommuters;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Base64;
import android.util.Log;

public class Rest {
//	private static final String WEBSITE = "http://android:test@bradleytse.com/testing/transitapi.php";
	private static final String WEBSITE = "http://bradleytse.com/transit/gtfsapi.php";
	private URL url;
	private String query;
	
	public Rest(String q) {
		try {
			url = new URL(WEBSITE);
		} catch (MalformedURLException e) {
			Log.d(MainActivity.TAG, "URL Fail: " + e.getMessage());
		}
		query = q;
	}
	
	public JSONArray get() {
//		// Create new URL and Json objects, then pass it along to the post helper
//		String charset = "UTF-8";
//		query = String.format("param1=%s&param2=%s",
//							  URLEncoder.encode("param1", charset),
//							  URLEncoder.encode("param2", charset));
//
//		HttpURLConnection urlConnection = (HttpURLConnection) new URL(Surl + "?" + query).openConnection();                        
//		urlConnection.connect();
//
//		 Set up properties
		return null;
	}
	
	/* I think I am going to use post for queries also */
	public JSONArray post() {
		InputStream in = null;
		BufferedWriter bw = null;
		StringBuilder response = new StringBuilder(); // final response
		HttpURLConnection conn = null;
		JSONArray json = null;
		int responseCode = 0;
		
		try {
			String jsonQuery = new JSONObject().put("select", query).toString();
			// Does not actually do any network IO
			conn = (HttpURLConnection) url.openConnection();
//			String basicAuth = "Basic " + new String(Base64.encode(url.getUserInfo().getBytes(), 0));
			
			// Sets up POST settings
			conn.setDoOutput(true);
//			conn.setRequestProperty("Authorization", basicAuth);
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setRequestProperty("Accept", "application/json");
			conn.setRequestMethod("POST");
			
			// This actually opens the connection and then sends POST and headers
			bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));

			// Send the message
			bw.write(jsonQuery);
			bw.flush();
			bw.close(); 
			Log.d(MainActivity.TAG, "BRAD");

			// Get server response
			in = conn.getInputStream();
			responseCode = conn.getResponseCode();

			// Convert to string
			Scanner s = new Scanner(in, "UTF-8");
			while(s.hasNext() != false) {
				response.append(s.next());
			}
			
			json = new JSONArray(response.toString());
		} catch (IOException e) {
			Log.d(MainActivity.TAG, "Ok " + e.getMessage());
		} catch (JSONException e) {
			Log.d(MainActivity.TAG, response.toString());
			Log.d(MainActivity.TAG, e.getMessage());
		} finally {
			Log.d(MainActivity.TAG, String.valueOf(responseCode));
			conn.disconnect(); // close connection
		}
		Log.d(MainActivity.TAG, json.toString());
		return json;
	}
}
