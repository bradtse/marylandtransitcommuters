package com.marylandtransitcommuters.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.util.Log;

import com.marylandtransitcommuters.MainActivity;

/**
 * Rather than implementing the SQLiteOpenHelper inside of TransitProvider,
 * I decided to implement it within this class. The purpose of the class is
 * to correctly handle the different types of database queries. 
 */
public class TransitDatabase {	
	private TransitSqlHelper mTransitHelper;
	private Context mContext;
	
	/**
	 * TransitDatabase constructor
	 * @param context The apps context
	 */
	public TransitDatabase(Context context) {
		Log.d(MainActivity.LOG_TAG, "TransitDatabase constructor");
		mTransitHelper = new TransitSqlHelper(context);
		mContext = context;
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
		String selection = "((" + TransitContract.Routes._ID + " = ?))";
		String[] selectionArgs = {id};
		return query(uri, table, projection, selection, selectionArgs, null);
	}
	
    /**
     * Performs a database query.
     * @param projection The columns to return
     * @param selection The selection clause
     * @param selectionArgs Selection arguments for "?" components in the selection
     * @return A Cursor over all rows matching the query
     */
    public Cursor query(Uri uri, String table, String[] projection, String selection, 
    					 String[] selectionArgs, String sortOrder) {
    	Log.d(MainActivity.LOG_TAG, "Transitdatabase query (Database creation)");

        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
        builder.setTables(table);

        // This is also where the database is created for the first time if it 
        // hasn't already been created (calls onCreate() of SQLiteOpenHelper)
        Cursor cursor = builder.query(mTransitHelper.getReadableDatabase(),
                projection, selection, selectionArgs, null, null, sortOrder);

        if (cursor == null) {
        	Log.d(MainActivity.LOG_TAG, "Returned cursor was null");
            return null;
        } 

        if (!cursor.moveToFirst()) {
        	Log.d(MainActivity.LOG_TAG, "Cursor was empty");
        }
       
        cursor.setNotificationUri(mContext.getContentResolver(), uri);
        
        return cursor;
    }
    
    /**
     * Inserts the ContentValues into the table
     * @return The uri of the newly inserted row
     */
    public Uri insert(Uri uri, String table, ContentValues cv) {
    	SQLiteDatabase db = mTransitHelper.getWritableDatabase();
    	try {
	    	long rowId = db.insertOrThrow(table, null, cv);
	    	if (rowId > 0 ) {
	    		mContext.getContentResolver().notifyChange(uri, null);
	    		Uri newUri = Uri.withAppendedPath(uri, String.valueOf(rowId));
	    		return newUri;
	    	} else { 
	    		throw new SQLException();
	    	}
    	} catch (SQLException e) {
    		Log.d(MainActivity.LOG_TAG, "Insert failed: " + e.getMessage());
    	} finally {
    		db.close();
    	}
    	return null;
    }
    
    /**
     * Deletes from the table based upon the arguments provided.
     * @return The number of rows deleted
     */
    public int delete(Uri uri, String table, String selection, String[] selectionArgs) {
    	SQLiteDatabase db = mTransitHelper.getWritableDatabase();
    	int rowsDeleted = db.delete(table, selection, selectionArgs);
    	mContext.getContentResolver().notifyChange(uri, null);
    	return rowsDeleted;
    }
	
	/**
	 * Helper class that opens/creates the database
	 */
	private static class TransitSqlHelper extends SQLiteOpenHelper {
		
		public TransitSqlHelper(Context context) {
			super(context, TransitContract.DATABASE_NAME, null, TransitContract.DATABASE_VERSION);
			Log.d(MainActivity.LOG_TAG, "TransitSqlHelper constructor");
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			Log.d(MainActivity.LOG_TAG, "TransitSqlHelper onCreate()");
			for (String s : TransitContract.SQL_CREATE_TABLE_ARRAY) {
				db.execSQL(s);
			}
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// TODO Auto-generated method stub
		}
	}
}
