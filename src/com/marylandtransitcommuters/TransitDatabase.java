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
		private static final int DATABASE_VERSION = 1;
		
		private final Context mHelperContext;
		private SQLiteDatabase mDatabase;

		/**
		 * TransitSqlHelper constructor
		 * @param context The context of the app
		 */
		public TransitSqlHelper(Context context) {
			super(context, TransitContract.DATABASE_NAME, null, DATABASE_VERSION);
			Log.d(MainActivity.BRAD, "TransitSqlHelper constructor");
			mHelperContext = context;
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			Log.d(MainActivity.BRAD, "TransitSqlHelper onCreate()");
			mDatabase = db;
			createDatabase();
			Log.d(MainActivity.BRAD, "Leaving TransitSqlHelper onCreate()");
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// TODO Auto-generated method stub
		}
		
		/*
		 * HELPER FUNCTIONS 
		 */
		
		/** 
		 * Create the database with the GTFS data
		 */
		private void createDatabase() {
			/* Create all of the tables for the database */
			for(String s : TransitContract.SQL_CREATE_TABLE_ARRAY) {
				mDatabase.execSQL(s);
			}
			
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
		
		/** 
		 * Parse data files and load them into the database 
		 * XXX Support all data files
		 */
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
					long id = insertRoutesRow(line);
					if (id < 0) {
						Log.e(MainActivity.BRAD, 
							  "Unable to add row of data for route id: " + line[0]);
					}
				}
			} finally {
				reader.close();
			}
		}
		
		/** 
		 * Insert a row of routes data to database 
		 * @param line the array of strings to insert
		 */
		private long insertRoutesRow(String[] line) {
			ContentValues values = new ContentValues();
//			int i = 0;
//			for(String s : TransitContract.Routes.COLUMN_ARRAY) {
//				values.put(s, line[i]);
//				i++;
//			}
			values.put(TransitContract.Routes.KEY_ROUTE_ID, line[0]);
			values.put(TransitContract.Routes.KEY_AGENCY_ID, line[1]);
			values.put(TransitContract.Routes.KEY_SHORT_NAME, line[2]);
			values.put(TransitContract.Routes.KEY_LONG_NAME, line[3]);
			values.put(TransitContract.Routes.KEY_DESCRIPTION, line[4]);
			values.put(TransitContract.Routes.KEY_ROUTE_TYPE, Integer.parseInt(line[5]));
			values.put(TransitContract.Routes.KEY_URL, line[6]);
			values.put(TransitContract.Routes.KEY_COLOR, line[7]);
			values.put(TransitContract.Routes.KEY_TEXT_COLOR, line[8]);

			return mDatabase.insert(TransitContract.Routes.TABLE_NAME, null, values);
		}
		
		/**
		 * Add a row of trips data to database
		 */
		private long insertStopsRow(String[] line) {
			ContentValues values = new ContentValues();
			int i = 0;
			for(String s : TransitContract.Stops.COLUMN_ARRAY) {
				values.put(s, line[i]);
				i++;
			}
			return mDatabase.insert(TransitContract.Stops.TABLE_NAME, null, values);
		}
	}
	
}
