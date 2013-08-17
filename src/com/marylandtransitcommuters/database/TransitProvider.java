package com.marylandtransitcommuters.database;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import com.marylandtransitcommuters.MainActivity;
import com.marylandtransitcommuters.database.TransitContract.Favorites;

/**
 * Content Provider for the transit data
 */
public class TransitProvider extends ContentProvider {	
	private TransitDatabase mTransitDatabase;
	
	private static final UriMatcher sUriMatcher = buildUriMatcher();
	
	/*
	 * UriMatcher constants
	 */
	private static final int FAVORITE = 0;

	@Override
	public boolean onCreate() {
		Log.d(MainActivity.LOG_TAG, "TransitProvider onCreate()");
		mTransitDatabase = new TransitDatabase(getContext());
		return true;
	}
	
	@Override
	public String getType(Uri uri) {
		switch(sUriMatcher.match(uri)) {
			case FAVORITE:
				return TransitContract.Favorites.CONTENT_ITEM_TYPE;
			default:
				throw new IllegalArgumentException("Unsupported URI " + uri);
		}
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection, 
						String[] selectionArgs, String sortOrder) {
		Log.d(MainActivity.LOG_TAG, "TransitProvider query()");
		switch(sUriMatcher.match(uri)) {
			case FAVORITE:
				return mTransitDatabase.query(uri, TransitContract.Favorites.TABLE_NAME, 
								projection, selection, selectionArgs, sortOrder);
			default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
		}
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		switch(sUriMatcher.match(uri)) {
			case FAVORITE:
				return mTransitDatabase.delete(uri, Favorites.TABLE_NAME, selection, selectionArgs);
			default:
				throw new IllegalArgumentException("Unsupported URI: " + uri);
		}
	}

	@Override
	public Uri insert(Uri uri, ContentValues cv) {
		switch(sUriMatcher.match(uri)) {
			case FAVORITE:
				Log.d(MainActivity.LOG_TAG, "Inserting into favorites table");
				return mTransitDatabase.insert(uri, TransitContract.Favorites.TABLE_NAME, cv);
			default:
				throw new IllegalArgumentException("Unsupported URI: " + uri);
		}
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
		
		matcher.addURI(TransitContract.AUTHORITY, TransitContract.Favorites.TABLE_NAME, FAVORITE);
		
		return matcher;
	}
}
