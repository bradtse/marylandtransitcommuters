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

	public TransitService() {
		super("TransitIntentService");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		final ResultReceiver mReceiver = intent.getParcelableExtra("receiver");
		mReceiver.send(START, Bundle.EMPTY);
		
		restHelper(intent.getIntExtra("type", -1));
		
		mReceiver.send(FINISH, Bundle.EMPTY);
	}
	
	private void restHelper(int type) {
		SearchData profile = SearchData.getInstance();
		switch(type) {
			case 0:
				profile.setRouteJSON(new Rest("SELECT * FROM Routes ORDER BY ShortName").post());
				break;
			case 1:
				String query = String.format("SELECT T3.stop_id, T2.trip_id, T2.stop_sequence, T1.shape_id, 
					    T3.stop_lat, T3.stop_lon, T3.stop_name 
					    FROM 
					    trips AS T1
					    JOIN
					    stop_times AS T2
					        ON T1.trip_id=T2.trip_id AND route_id = 6122 AND direction_id = 0
					    JOIN 
					    stops AS T3
					        ON T2.stop_id=T3.stop_id
					    GROUP BY T3.stop_id
					    ORDER BY T2.stop_sequence");
				profile.setRouteJSON(new Rest(""))
				return;
			default:
		}
	}
}
