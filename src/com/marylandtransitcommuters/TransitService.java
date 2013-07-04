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
		CurrentSearch profile = CurrentSearch.getInstance();
		switch(type) {
			case 0:
				profile.setRouteJSON(new Rest("SELECT * FROM Routes ORDER BY RouteId").post());
			case 1:
				return;
			default:
		}
	}
}
