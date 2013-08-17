package com.marylandtransitcommuters;

import android.app.Application;

/**
 * Keeps track of a few application wide variables
 */
public class TransitApplication extends Application {
	
	private boolean isAlive = false;
	
	public void setAlive () {
		this.isAlive = true;
	}
	
	public boolean isAlive() {
		return isAlive;
	}
	
}
