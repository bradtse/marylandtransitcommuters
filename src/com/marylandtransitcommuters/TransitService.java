package com.marylandtransitcommuters;

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
	
	// This needs to be redone properly
	private final String ROUTEQUERY = "SELECT * FROM routes ORDER BY route_short_name";
	private final String STOPQUERY = "SELECT T3.stop_id, T2.trip_id, T2.stop_sequence, "
								   + "T1.shape_id, T3.stop_lat, T3.stop_lon, T3.stop_name "
								   + "FROM "
								   + "trips AS T1 "
								   + "JOIN "
								   + "stop_times AS T2 "
								   + "ON T1.trip_id=T2.trip_id AND route_id = 6122 AND direction_id = 0 "
								   + "JOIN "
								   + "stops AS T3 "
								   + "ON T2.stop_id=T3.stop_id "
								   + "GROUP BY T3.stop_id "
								   + "ORDER BY T2.stop_sequence";
	private final String TIMEQUERY = "SELECT T2.arrival_time, T2.departure_time "
			 					   + "FROM "
			 					   + "trips as T1 "
			 					   + "JOIN "
			 					   + "stop_times as T2 "
			 					   + "on T1.trip_id=T2.trip_id AND route_id = 6122 AND direction_id = 0 "
			 					   + "JOIN "
			 					   + "stops as T3 "
			 					   + "on T2.stop_id = T3.stop_id AND T2.stop_id = 11785 "
			 					   + "ORDER BY T2.arrival_time";

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
		SearchData profile = SearchData.getInstance();
		switch(type) {
			case ROUTES:
				profile.setRouteJSON(new Rest(ROUTEQUERY).post());
				break;
			case STOPS:
				profile.setStopsJSON(new Rest(STOPQUERY).post());
				break;
			case TIMES:
				profile.setTimesJSON(new Rest(TIMEQUERY).post());
				break;
			default:
		}
	}
}
