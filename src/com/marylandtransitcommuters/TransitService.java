package com.marylandtransitcommuters;

import java.util.Locale;

import org.json.JSONException;
import org.json.JSONObject;

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
	
	public enum Type {
		ROUTES, STOPS, TIMES, DIRECTIONS;
		public static final String KEY = "type";
	}
	
	public TransitService() {
		super("TransitIntentService");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		final ResultReceiver mReceiver = intent.getParcelableExtra("receiver");
		mReceiver.send(START, Bundle.EMPTY);
		
		restHelper((Type) intent.getSerializableExtra(Type.KEY));
		
		mReceiver.send(FINISH, Bundle.EMPTY);
	}
	
	private void restHelper(Type type) {
		try {
			CurrentSearch profile = CurrentSearch.getInstance();
			JSONObject data = new JSONObject();
			data.put(CurrentSearch.ROUTE_ID, profile.getRouteId());
			data.put(CurrentSearch.DIR_ID, profile.getDirectionId());
			data.put(CurrentSearch.STOP_ID, profile.getStopId());
			data.put(Type.KEY, type.name().toLowerCase(Locale.US));
			Log.d(MainActivity.TAG, data.toString());
			profile.setData(RestHelper.post(data), type);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
}
