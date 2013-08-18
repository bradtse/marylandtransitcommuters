package com.marylandtransitcommuters;

import android.app.Application;

/**
 * Keeps track of a few application wide variables
 */
public class TransitApplication extends Application {
	private static boolean sFragmentAnimations = true;
	
	public void enableFragmentAnimations() {
		sFragmentAnimations = true;
	}
	
	public void disableFragmentAnimations() {
		sFragmentAnimations = false;
	}
	
	public boolean isFragmentAnimationsEnabled() {
		return sFragmentAnimations;
	}
	
}
