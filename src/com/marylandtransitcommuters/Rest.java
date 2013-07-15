package com.marylandtransitcommuters;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class Rest {
//	private static final String WEBSITE = "http://android:test@bradleytse.com/testing/transitapi.php";
	private static final String WEBSITE = "http://bradleytse.com/transit/gtfsapi.php";
	
	private Rest() {}
	
	/* I think I am going to use post for queries also */
	public static JSONArray post(JSONObject data) {
		HttpURLConnection conn = setupConnection(WEBSITE);
		sendData(data, conn);
		JSONArray json = responseToJSON(conn);
		int responseCode = getResponseCode(conn);
		conn.disconnect();
		
		Log.d(MainActivity.TAG, "Response code: " + String.valueOf(responseCode));
//		Log.d(MainActivity.TAG, json.toString());
		return json;
	}
	
	/**
	 * Sets up the connection to the provided url. Does not actually connect yet.
	 * @param url the url string to setup the connection to
	 * @return an Http URL connection, null on failure
	 */
	private static HttpURLConnection setupConnection(String stringURL) {
		HttpURLConnection conn = null;
		URL url = null;
		try {
			url = new URL(stringURL);
			conn = (HttpURLConnection) url.openConnection();
//			String basicAuth = "Basic " + new String(Base64.encode(url.getUserInfo().getBytes(), 0));
			// Sets up POST settings
			conn.setDoOutput(true);
//			conn.setRequestProperty("Authorization", basicAuth);
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setRequestProperty("Accept", "application/json");
			conn.setRequestMethod("POST");
		} catch (IOException e) {
			Log.d(MainActivity.TAG, "setupConnection failed: " + e.getMessage());
		}
		return conn;
	}
	
	/**
	 * Opens the connection and sends the data
	 * @param data the JSON object to send
	 * @param conn the connection to send the data to
	 */
	private static void sendData(JSONObject data, HttpURLConnection conn) {
		try {
			OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream());
			BufferedWriter bw = new BufferedWriter(out);
			bw.write(data.toString());
			bw.flush();
			bw.close(); 
		} catch (IOException e) {
			Log.d(MainActivity.TAG, "sendData failed: " + e.getMessage());
		}
	}
	
	/**
	 * Converts the server response to a JSON array
	 * @param conn the connection to get the response from
 	 * @return a JSON Array representation of the server response, null on failure
	 */
	private static JSONArray responseToJSON(HttpURLConnection conn) {
		StringBuilder response = new StringBuilder();
		JSONArray json = null;
		Scanner s;
		try {
			s = new Scanner(conn.getInputStream(), "UTF-8");
			while(s.hasNext() != false) {
				response.append(s.next());
			}
			json = new JSONArray(response.toString());
		} catch (JSONException e) {
			Log.d(MainActivity.TAG, "responseToJSON failed: " + e.getMessage());
		} catch (IOException e) {
			Log.d(MainActivity.TAG, "responseToJSON failed: " + e.getMessage());
		}
		return json;
	}
	
	/**
	 * Get the response code from the server
	 * @param conn the connection to get the response code from
	 * @return the response code from the server, -1 on failure
	 */
	private static int getResponseCode(HttpURLConnection conn) {
		try {
			return conn.getResponseCode();
		} catch (IOException e) {
			Log.d(MainActivity.TAG, "getResponseCode failed: " + e.getMessage());
		}
		return -1;
	}
}
