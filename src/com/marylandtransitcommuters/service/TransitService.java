package com.marylandtransitcommuters.service;

import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.marylandtransitcommuters.MainActivity;
import com.marylandtransitcommuters.receiver.TransitReceiver;
import com.marylandtransitcommuters.rest.RestHelper;

import dataobjects.Route;
import dataobjects.TransitData;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.util.Log;

/**
 * Used for contacting the server and retrieving data
 */
public class TransitService extends IntentService {
	public static final int START = 0;
	public static final int FINISH = 1;
	
	public enum DataType {
		ROUTES, STOPS, TIMES, DIRECTIONS;
		public static final String KEY = "type";
	}
	
	public TransitService() {
		super("TransitIntentService");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		final ResultReceiver mReceiver = intent.getParcelableExtra(TransitReceiver.RECEIVER);
		mReceiver.send(START, Bundle.EMPTY);
		serviceHelper((DataType) intent.getSerializableExtra(DataType.KEY));
		mReceiver.send(FINISH, Bundle.EMPTY);
	}
	
	/**
	 * Creates a JSON object from the data in the SearchInstance and passes
	 * it to the post method of the RestHelper. The server handles extracting
	 * the necessary data for the given type.
	 * @param type the type of the query being sent to the server
	 */
	private void serviceHelper(DataType type) {
		try {
			Log.d(MainActivity.LOG_TAG, "Inside serviceHelper()");
			TransitData profile = TransitData.getInstance();
			JSONObject data = new JSONObject();
			
			data.put(Route.ROUTE_ID, profile.getRouteId());
			data.put(TransitData.DIR_ID, profile.getDirectionId());
			data.put(TransitData.STOP_ID, profile.getStopId());
			data.put(DataType.KEY, type.name().toLowerCase(Locale.US));
			
			Log.d(MainActivity.LOG_TAG, data.toString());
			
			JSONArray results = RestHelper.post(data);
			profile.setData(type, results);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
}
