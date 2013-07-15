package com.marylandtransitcommuters;

import java.util.Locale;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;

/**
 * Used for contacting the server and retrieving data
 */
public class TransitService extends IntentService {
	public static final int START = 0;
	public static final int FINISH = 1;
	
	public enum Type {
		ROUTES, STOPS, TIMES, DIRECTION;
		public static final String KEY = "type";
	}
	
	public static final String ROUTEID = "route_id";
	public static final String DIRID = "direction_id";
	public static final String STOPID = "stop_id";

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
			SearchData profile = SearchData.getInstance();
			JSONObject data = new JSONObject();
			data.put(ROUTEID, profile.getRouteId());
			data.put(DIRID, profile.getDirection());
			data.put(STOPID, profile.getStopId());
			data.put(Type.KEY, type.name().toLowerCase(Locale.US));
			profile.setData(Rest.post(data), type);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
}
