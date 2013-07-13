package com.marylandtransitcommuters;

import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;

/**
 * Allows the IntentService to let the main ui thread know it finished through
 * a callback
 */
public class TransitReceiver extends ResultReceiver {
	private Receiver mReceiver;
	
	public TransitReceiver(Handler handler) {
		super(handler);
	}
	
	public void setReceiver(Receiver receiver) {
		mReceiver = receiver;
	}
	
	public interface Receiver {
		public void onReceiveResult(int resultCode, Bundle resultData);
	}
	
	@Override
	protected void onReceiveResult(int resultCode, Bundle resultData) {
		if (mReceiver != null) {
			mReceiver.onReceiveResult(resultCode, resultData);
		}
	}
}
