package com.marylandtransitcommuters;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

public class TransitProvider extends ContentProvider {	
	private TransitDatabase mTransitDatabase;
	
	/*
	 * UriMatcher stuff
	 */
	private static final int GET_ROUTE_LIST = 0;
	private static final int GET_ROUTE = 1;
	private static final UriMatcher sUriMatcher = buildUriMatcher();

	@Override
	public boolean onCreate() {
		Log.d(MainActivity.BRAD, "TransitProvider onCreate()");
		mTransitDatabase = new TransitDatabase(getContext());
		return true;
	}
	
	@Override
	public String getType(Uri uri) {
		switch(sUriMatcher.match(uri)) {
			case GET_ROUTE_LIST:
				return TransitContract.Routes.CONTENT_TYPE;
			case GET_ROUTE:
				return TransitContract.Routes.CONTENT_ITEM_TYPE;
			default:
				throw new IllegalArgumentException("Unknown URI " + uri);
		}
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection, 
						String[] selectionArgs, String sortOrder) {
		Log.d(MainActivity.BRAD, "TransitProvider query()");
		switch(sUriMatcher.match(uri)) {
			case GET_ROUTE_LIST:
				return mTransitDatabase.query(TransitContract.Routes.TABLE_NAME, 
											  projection, selection, 
											  selectionArgs, sortOrder);
			case GET_ROUTE:
				// TODO
                throw new UnsupportedOperationException("GET_ROUTE not implemented");
			default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
		}
	}
	

	@Override
	public int delete(Uri arg0, String arg1, String[] arg2) {
		// TODO Auto-generated method stub
		return 0;
	}

	/*
	 * Not sure if I will implement this because for this app no data should ever
	 * be inserted into the database unless the GTFS data is updated, which as of
	 * now will be handled manually by me
	 */
	@Override
	public Uri insert(Uri arg0, ContentValues arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int update(Uri arg0, ContentValues arg1, String arg2, String[] arg3) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	/*
	 * HELPER FUNCTIONS
	 */
	
	/**
	 * Builds the necessary UriMatcher
	 * @return UriMatcher
	 */
	private static UriMatcher buildUriMatcher() {
		UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
		
		/* Complete route list */
		matcher.addURI(TransitContract.AUTHORITY, TransitContract.Routes.TABLE_NAME, GET_ROUTE_LIST);
		/* Get specific route */
		matcher.addURI(TransitContract.AUTHORITY, TransitContract.Routes.TABLE_NAME + "/#", GET_ROUTE);
		
		return matcher;
	}

}
