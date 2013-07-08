package com.marylandtransitcommuters;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;

/**
 * Used for long-running tasks
 */
public class TransitService extends IntentService {
	public static final int START = 0;
	public static final int FINISH = 1;
	public static final int ROUTES = 0;
	public static final int STOPS  = 1;
	public static final int TIMES = 2;
	public static final String TYPE = "type";
	
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
		
		restHelper(intent.getIntExtra(TYPE, -1));
		
		mReceiver.send(FINISH, Bundle.EMPTY);
	}
	
	private void restHelper(int type) {
		try {
			SearchData profile = SearchData.getInstance();
			JSONObject data = new JSONObject();
			data.put(ROUTEID, profile.getRouteId());
			data.put(DIRID, profile.getDirection());
			data.put(STOPID, profile.getStopId());
			switch(type) {
				case ROUTES:
					data.put(TYPE, "routes");
					profile.putRoutesData(Rest.post(data));
					break;
				case STOPS:
					data.put(TYPE, "stops");
					profile.putStopsData(Rest.post(data));
					break;
				case TIMES:
					data.put(TYPE, "times");
					profile.putTimesData(Rest.post(data));
					break;
				default:
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
