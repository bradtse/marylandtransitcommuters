package com.marylandtransitcommuters;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

/**
 * Content Provider for the transit data
 */
public class TransitProvider extends ContentProvider {	
	private TransitDatabase mTransitDatabase;
	
	/*
	 * UriMatcher constants
	 */
	private static final int GET_ROUTES_LIST = 0;
	private static final int GET_ROUTE = 1;
	private static final int GET_STOPS_LIST = 2;
	private static final int GET_STOP = 3;
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
			case GET_ROUTES_LIST:
				return TransitContract.Routes.CONTENT_TYPE;
			case GET_ROUTE:
				return TransitContract.Routes.CONTENT_ITEM_TYPE;
			case GET_STOPS_LIST:
				return TransitContract.Stops.CONTENT_TYPE;
			case GET_STOP:
				return TransitContract.Stops.CONTENT_ITEM_TYPE;
			default:
				throw new IllegalArgumentException("Unknown URI " + uri);
		}
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection, 
						String[] selectionArgs, String sortOrder) {
		Log.d(MainActivity.BRAD, "TransitProvider query()");
		switch(sUriMatcher.match(uri)) {
			case GET_ROUTES_LIST:
				return mTransitDatabase.getList(TransitContract.Routes.TABLE_NAME, 
											  	projection, selection, 
											  	selectionArgs, sortOrder);
			case GET_ROUTE:
				return mTransitDatabase.getRow(TransitContract.Routes.TABLE_NAME, 
											   projection, uri);
			case GET_STOPS_LIST:
				return mTransitDatabase.getList(TransitContract.Stops.TABLE_NAME,
												projection, selection,
												selectionArgs, sortOrder);
			case GET_STOP:
				return mTransitDatabase.getRow(TransitContract.Stops.TABLE_NAME,
											   projection, uri);
			default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
		}
	}

	@Override
	public int delete(Uri arg0, String arg1, String[] arg2) {
		throw new UnsupportedOperationException("Delete not supported for database");
	}

	/*
	 * Not sure if I will implement this because for this app no data should ever
	 * be inserted into the database unless the GTFS data is updated, which as of
	 * now will be handled manually by me
	 */
	@Override
	public Uri insert(Uri arg0, ContentValues arg1) {
		throw new UnsupportedOperationException("Insert not supported for database");
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
		matcher.addURI(TransitContract.AUTHORITY, TransitContract.Routes.TABLE_NAME, GET_ROUTES_LIST);
		/* Specific route */
		matcher.addURI(TransitContract.AUTHORITY, TransitContract.Routes.TABLE_NAME + "/#", GET_ROUTE);
		/* Stops list */
		matcher.addURI(TransitContract.AUTHORITY, TransitContract.Stops.TABLE_NAME, GET_STOPS_LIST);
		/* Specific stop */
		matcher.addURI(TransitContract.AUTHORITY, TransitContract.Stops.TABLE_NAME + "/#", GET_STOP);
		
		return matcher;
	}

}
