package com.marylandtransitcommuters.service;

import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.marylandtransitcommuters.MainActivity;
import com.marylandtransitcommuters.dataobjects.Direction;
import com.marylandtransitcommuters.dataobjects.FinalStop;
import com.marylandtransitcommuters.dataobjects.Route;
import com.marylandtransitcommuters.dataobjects.StartStop;
import com.marylandtransitcommuters.dataobjects.TransitData;
import com.marylandtransitcommuters.receiver.TransitReceiver;
import com.marylandtransitcommuters.util.RestHelper;


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
		ROUTES, STARTSTOPS, FINALSTOPS, TIMES, DIRECTIONS;
		public static final String KEY = "type";
	}
	
	public TransitService() {
		super("TransitIntentService");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		ResultReceiver mReceiver = intent.getParcelableExtra(TransitReceiver.RECEIVER);
		
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
			TransitData transitObject = TransitData.getInstance();
			JSONObject data = new JSONObject();
			
			// Adds all of the data that will be sent to the server. Some of the 
			// fields might be empty sometimes but TransitData handles null 
			// cases already and the server ignores any empty fields
			data.put(Route.KEY, transitObject.getRouteId());
			data.put(Direction.KEY, transitObject.getDirectionId());
			data.put(StartStop.STOP_KEY,  transitObject.getStartStopId());
			data.put(StartStop.SEQ_KEY, transitObject.getStartStopSeq());
			data.put(FinalStop.STOP_KEY, transitObject.getFinalStopId());
			data.put(DataType.KEY, type.name().toLowerCase(Locale.US));
			
//			Log.d(MainActivity.LOG_TAG, data.toString());
			
			JSONArray results = RestHelper.post(data);
			transitObject.setData(type, results);
		} catch (JSONException e) {
			Log.d(MainActivity.LOG_TAG, "serviceHelper() failed: " + e.getMessage());
		}
	}
}
