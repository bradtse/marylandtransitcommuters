package com.marylandtransitcommuters.util;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.marylandtransitcommuters.MainActivity;

import android.util.Log;

/**
 * Helper class to send data to the server via POST method
 */
public final class RestHelper {
//	private static final String WEBSITE = "http://android:test@bradleytse.com/testing/transitapi.php";
	private static final String WEBSITE = "http://bradleytse.com/transit/gtfsapi.php";
	
	private RestHelper() {}
	
	/**
	 * Sends data to query the server and returns the response in JSONArray form
	 * @param data the data needed to query the database
	 * @return a JSONArray containing the response from the server, or null on failure
	 * FIXME Handle what happens when null returns
	 */
	public static JSONArray post(JSONObject data) {
		JSONArray json = null;
		boolean success = true;
		int failCount = 0; 
		
		do {
			// Only retry a max of 10 times
			if (failCount > 10) {
				return null;
			}
			
			HttpURLConnection conn = setupConnection(WEBSITE);
			if (conn == null) {
				success = false;
				failCount++;
				continue;
			}
			
			int rc = sendData(data, conn);
			if (rc == -1) {
				success = false;
				failCount++;
				continue;
			}
			
			if (getResponseCode(conn) == -1) {
				success = false;
				failCount++;
				continue;
			}
				
			json = responseToJSON(conn);
			if (json == null) {
				success = false;
				failCount++;
				continue;
			}
			
			conn.disconnect();
			
			success = true;
		} while (success == false);
		
		return json;
	}
	
	/**
	 * Sets up the connection to the provided url. Does not actually connect yet.
	 * @param url the url string to setup the connection to
	 * @return an Http URL connection, null on failure
	 */
	private static HttpURLConnection setupConnection(String stringURL) {
		HttpURLConnection conn = null;
		try {
			URL url = new URL(stringURL);
			conn = (HttpURLConnection) url.openConnection();
			
			// Sets up POST settings
//			String basicAuth = "Basic " + new String(Base64.encode(url.getUserInfo().getBytes(), 0));
			conn.setDoOutput(true);
//			conn.setRequestProperty("Authorization", basicAuth);
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setRequestProperty("Accept", "application/json");
			conn.setRequestMethod("POST");
			conn.connect();
		} catch (IOException e) {
			Log.d(MainActivity.LOG_TAG, "setupConnection failed: " + e.getMessage());
		}
		return conn;
	}
	
	/**
	 * Opens the connection and sends the data
	 * @param data the JSON object to send
	 * @param conn the connection to send the data to
	 * @return 0 on success, -1 on failure
	 */
	private static int sendData(JSONObject data, HttpURLConnection conn) {
		int rc = 0;
		try {
			OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream());
			BufferedWriter bw = new BufferedWriter(out);
			bw.write(data.toString());
			bw.flush();
			bw.close(); 
		} catch (IOException e) {
			Log.d(MainActivity.LOG_TAG, "sendData failed: " + e.getMessage());
			rc = -1;
		}
		return rc;
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
			Log.d(MainActivity.LOG_TAG, "getResponseCode failed: " + e.getMessage());
		}
		return -1;
	}
	
	/**
	 * Converts the server response to a JSON array
	 * @param conn the connection to get the response from
 	 * @return a JSON Array representation of the server response, null on failure
	 */
	private static JSONArray responseToJSON(HttpURLConnection conn) {
		JSONArray json = null;
		try {
			Scanner s = new Scanner(conn.getInputStream(), "UTF-8").useDelimiter("\\A");
			String str = s.hasNext() ? s.next() : "";
			json = new JSONArray(str);
		} catch (JSONException e) {
			Log.d(MainActivity.LOG_TAG, "responseToJSON failed (JSONException): " + e.getMessage());
		} catch (IOException e) {
			Log.d(MainActivity.LOG_TAG, "responseToJSON failed (IOException): " + e.getMessage());
		}
		return json;
	}
}
