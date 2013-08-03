package com.marylandtransitcommuters.database;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import com.marylandtransitcommuters.MainActivity;

/**
 * Content Provider for the transit data
 */
public class TransitProvider extends ContentProvider {	
	private TransitDatabase mTransitDatabase;
	
	private static final UriMatcher sUriMatcher = buildUriMatcher();
	
	/*
	 * UriMatcher constants
	 */
	private static final int GET_AGENCY_LIST = 0;
	private static final int GET_AGENCY = 1;
	private static final int GET_CALENDAR_DATES_LIST = 2;
	private static final int GET_CALENDAR_DATE = 3;
	private static final int GET_CALENDAR_LIST = 4;
	private static final int GET_CALENDAR = 5;
	private static final int GET_ROUTES_LIST = 6;
	private static final int GET_ROUTE = 7;
	private static final int GET_SHAPES_LIST = 8;
	private static final int GET_SHAPE = 9;
	private static final int GET_STOPS_LIST = 10;
	private static final int GET_STOP = 11;
	private static final int GET_STOP_TIMES_LIST = 12;
	private static final int GET_STOP_TIME = 13;
	private static final int GET_TRIPS_LIST = 14;
	private static final int GET_TRIP = 15;

	@Override
	public boolean onCreate() {
		Log.d(MainActivity.LOG_TAG, "TransitProvider onCreate()");
		mTransitDatabase = new TransitDatabase(getContext());
		return true;
	}
	
	@Override
	public String getType(Uri uri) {
		switch(sUriMatcher.match(uri)) {
			case GET_AGENCY_LIST:
				return TransitContract.Agency.CONTENT_TYPE;
			case GET_AGENCY:
				return TransitContract.Agency.CONTENT_ITEM_TYPE;
			case GET_CALENDAR_DATES_LIST:
				return TransitContract.CalendarDates.CONTENT_TYPE;
			case GET_CALENDAR_DATE:
				return TransitContract.CalendarDates.CONTENT_ITEM_TYPE;
			case GET_CALENDAR_LIST:
				return TransitContract.Calendar.CONTENT_TYPE;
			case GET_CALENDAR:
				return TransitContract.Calendar.CONTENT_ITEM_TYPE;
			case GET_ROUTES_LIST:
				return TransitContract.Routes.CONTENT_TYPE;
			case GET_ROUTE:
				return TransitContract.Routes.CONTENT_ITEM_TYPE;
			case GET_SHAPES_LIST:
				return TransitContract.Shapes.CONTENT_TYPE;
			case GET_SHAPE:
				return TransitContract.Shapes.CONTENT_ITEM_TYPE;
			case GET_STOPS_LIST:
				return TransitContract.Stops.CONTENT_TYPE;
			case GET_STOP:
				return TransitContract.Stops.CONTENT_ITEM_TYPE;
			case GET_STOP_TIMES_LIST:
				return TransitContract.StopTimes.CONTENT_TYPE;
			case GET_STOP_TIME:
				return TransitContract.StopTimes.CONTENT_ITEM_TYPE;
			case GET_TRIPS_LIST:
				return TransitContract.Trips.CONTENT_TYPE;
			case GET_TRIP:
				return TransitContract.Trips.CONTENT_ITEM_TYPE;
			default:
				throw new IllegalArgumentException("Unsupported URI " + uri);
		}
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection, 
						String[] selectionArgs, String sortOrder) {
		Log.d(MainActivity.LOG_TAG, "TransitProvider query()");
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
                throw new IllegalArgumentException("Unsupported URI: " + uri);
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
		
		matcher.addURI(TransitContract.AUTHORITY, TransitContract.Agency.TABLE_NAME, GET_AGENCY_LIST);
		matcher.addURI(TransitContract.AUTHORITY, TransitContract.Agency.TABLE_NAME + "/#", GET_AGENCY);
		matcher.addURI(TransitContract.AUTHORITY, TransitContract.CalendarDates.TABLE_NAME, GET_CALENDAR_DATES_LIST);
		matcher.addURI(TransitContract.AUTHORITY, TransitContract.CalendarDates.TABLE_NAME + "/#", GET_CALENDAR_DATE);
		matcher.addURI(TransitContract.AUTHORITY, TransitContract.Calendar.TABLE_NAME, GET_CALENDAR_LIST);
		matcher.addURI(TransitContract.AUTHORITY, TransitContract.Calendar.TABLE_NAME + "/#", GET_CALENDAR);
		matcher.addURI(TransitContract.AUTHORITY, TransitContract.Routes.TABLE_NAME, GET_ROUTES_LIST);
		matcher.addURI(TransitContract.AUTHORITY, TransitContract.Routes.TABLE_NAME + "/#", GET_ROUTE);
		matcher.addURI(TransitContract.AUTHORITY, TransitContract.Shapes.TABLE_NAME, GET_SHAPES_LIST);
		matcher.addURI(TransitContract.AUTHORITY, TransitContract.Shapes.TABLE_NAME + "/#", GET_SHAPE);
		matcher.addURI(TransitContract.AUTHORITY, TransitContract.Stops.TABLE_NAME, GET_STOPS_LIST);
		matcher.addURI(TransitContract.AUTHORITY, TransitContract.Stops.TABLE_NAME + "/#", GET_STOP);
		matcher.addURI(TransitContract.AUTHORITY, TransitContract.StopTimes.TABLE_NAME, GET_STOP_TIMES_LIST);
		matcher.addURI(TransitContract.AUTHORITY, TransitContract.StopTimes.TABLE_NAME + "/#", GET_STOP_TIME);
		matcher.addURI(TransitContract.AUTHORITY, TransitContract.Trips.TABLE_NAME, GET_TRIPS_LIST);
		matcher.addURI(TransitContract.AUTHORITY, TransitContract.Trips.TABLE_NAME + "/#", GET_TRIP);
		
		return matcher;
	}
}
