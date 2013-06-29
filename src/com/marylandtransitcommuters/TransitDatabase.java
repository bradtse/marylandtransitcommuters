package com.marylandtransitcommuters;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.util.Log;
import au.com.bytecode.opencsv.CSVReader;

/**
 * Rather than implementing the SQLiteOpenHelper inside of TransitProvider,
 * I decided to implement it within this class. The purpose of the class is
 * to correctly handle the different types of database queries. As of now
 * this class is kind of pointless.
 */
public class TransitDatabase {	
	private TransitSqlHelper mTransitHelper;
	
	/**
	 * TransitDatabase constructor
	 * @param context The apps context
	 */
	public TransitDatabase(Context context) {
		Log.d(MainActivity.BRAD, "TransitDatabase constructor");
		mTransitHelper = new TransitSqlHelper(context);
	}

	/**
	 * Helper function to retrieve a list from a table
	 * The params are the same as query.
	 */
	public Cursor getList(String table, String[] projection, String selection,
						  String[] selectionArgs, String sortOrder) {
		return query(table, projection, selection, selectionArgs, sortOrder);
	}
	
	/**
	 * Helper function to retrieve a single row from a table
	 * @param table The table to query
	 * @param projection The columns you want to return
	 * @param uri The uri containing the rowid of the row we want
	 * @return
	 */
	public Cursor getRow(String table, String[] projection, Uri uri) {
		String id = uri.getLastPathSegment();
		String selection = TransitContract.Routes._ID + " = ?";
		String[] selectionArgs = {id};
		return query(table, projection, selection, selectionArgs, null);
	}
	
    /**
     * Performs a database query.
     * @param projection The columns to return
     * @param selection The selection clause
     * @param selectionArgs Selection arguments for "?" components in the selection
     * @return A Cursor over all rows matching the query
     */
    public Cursor query(String table, String[] projection, String selection, 
    					 String[] selectionArgs, String sortOrder) {
    	Log.d(MainActivity.BRAD, "Transitdatabase query (Database creation)");
        /* The SQLiteBuilder provides a map for all possible columns requested to
         * actual columns in the database, creating a simple column alias mechanism
         * by which the ContentProvider does not need to know the real column names
         */
        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
        builder.setTables(table);

        // This is also where the database is created for the first time if it 
        // hasn't already been created (calls onCreate() of SQLiteOpenHelper)
        Cursor cursor = builder.query(mTransitHelper.getReadableDatabase(),
                projection, selection, selectionArgs, null, null, sortOrder);

        if (cursor == null) {
        	Log.d(MainActivity.BRAD, "Returned cursor was null");
            return null;
        } else if (!cursor.moveToFirst()) {
        	Log.d(MainActivity.BRAD, "Cursor was empty");
            cursor.close();
            return null;
        }
        Log.d(MainActivity.BRAD, "Leaving Transitdatabase query");
        return cursor;
    }
	
	/**
	 * Helper class that opens/creates the database
	 */
	private static class TransitSqlHelper extends SQLiteOpenHelper {
		private static final String DATABASE_NAME = "transit.db";
		private static final int DATABASE_VERSION = 1;
		
		private final Context mHelperContext;
		private SQLiteDatabase mDatabase;

		public TransitSqlHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
			Log.d(MainActivity.BRAD, "TransitSqlHelper constructor");
			mHelperContext = context;
		}

		/*
		 * Create the transit database with the tables and columns found in the 
		 * TransitContract. Also loads the database with the GTFS data.
		 */
		@Override
		public void onCreate(SQLiteDatabase db) {
			Log.d(MainActivity.BRAD, "TransitSqlHelper onCreate()");
			mDatabase = db;
			mDatabase.execSQL(TransitContract.Routes.CREATE_TABLE);
			loadDatabase();
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// TODO Auto-generated method stub
			
		}
		
		/*
		 * HELPER FUNCTIONS 
		 */
		
		/* Load database */
		private void loadDatabase() {
//			new Thread(new Runnable() {
//				public void run() {
//					try {
//						loadData();
//					} catch (IOException e) {
//						throw new RuntimeException(e);
//					}
//				}
//			}).start();
			try {
				Log.d(MainActivity.BRAD, "Loading GTFS data");
				loadData();
				Log.d(MainActivity.BRAD, "Done loading GTFS data");
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
		
		/* Parse data file and load into database */
		private void loadData() throws IOException {
			final Resources resources = mHelperContext.getResources();
			InputStream input = resources.openRawResource(R.raw.routes);
			CSVReader reader = new CSVReader(new InputStreamReader(input));
			
			try {
				String [] line = reader.readNext(); // Skip the first line of the file
				int numCols = line.length; // Get number of columns in file
				while ((line = reader.readNext()) != null ) {
					if (line.length < numCols) {
						continue;
					}
					
					long id = addRoutesRow(line);
					if (id < 0) {
						Log.e(MainActivity.BRAD, 
							  "Unable to add row of data for route id: " + line[0]);
					}
				}
			} finally {
				reader.close();
			}
		}
		
		/* Add a row of routes data to database */
		private long addRoutesRow(String[] line) {
			ContentValues values = new ContentValues();
			values.put(TransitContract.Routes.COLUMN_NAME_ROUTE_ID, line[0]);
			values.put(TransitContract.Routes.COLUMN_NAME_AGENCY_ID, line[1]);
			values.put(TransitContract.Routes.COLUMN_NAME_SHORT_NAME, line[2]);
			values.put(TransitContract.Routes.COLUMN_NAME_LONG_NAME, line[3]);
			values.put(TransitContract.Routes.COLUMN_NAME_DESCRIPTION, line[4]);
			values.put(TransitContract.Routes.COLUMN_NAME_ROUTE_TYPE, line[5]);
			values.put(TransitContract.Routes.COLUMN_NAME_URL, line[6]);
			values.put(TransitContract.Routes.COLUMN_NAME_COLOR, line[7]);
			values.put(TransitContract.Routes.COLUMN_NAME_TEXT_COLOR, line[8]);
			
			return mDatabase.insert(TransitContract.Routes.TABLE_NAME, null, values);
		}
		
		/* Add a row of trips data to database */
		private long addStopsRow(String[] line) {
			ContentValues values = new ContentValues();
			values.put(TransitContract.Routes.COLUMN_NAME_ROUTE_ID, line[0]);
			values.put(TransitContract.Routes.COLUMN_NAME_AGENCY_ID, line[1]);
			values.put(TransitContract.Routes.COLUMN_NAME_SHORT_NAME, line[2]);
			values.put(TransitContract.Routes.COLUMN_NAME_LONG_NAME, line[3]);
			values.put(TransitContract.Routes.COLUMN_NAME_DESCRIPTION, line[4]);
			values.put(TransitContract.Routes.COLUMN_NAME_ROUTE_TYPE, line[5]);
			values.put(TransitContract.Routes.COLUMN_NAME_URL, line[6]);
			values.put(TransitContract.Routes.COLUMN_NAME_COLOR, line[7]);
			values.put(TransitContract.Routes.COLUMN_NAME_TEXT_COLOR, line[8]);
			
			return mDatabase.insert(TransitContract.Routes.TABLE_NAME, null, values);
		}
	}
	
}
