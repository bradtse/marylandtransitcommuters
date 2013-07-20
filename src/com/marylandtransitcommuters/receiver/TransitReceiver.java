package com.marylandtransitcommuters.receiver;

import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;

/**
 * Callback interface for the IntentService to let the main UI thread know it finished.
 * Any class that wishes to receive a result from the IntentService should
 * implement Receiver.
 */
public class TransitReceiver extends ResultReceiver {
	public static final String RECEIVER = "receiver";
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
