package com.marylandtransitcommuters;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/** 
 * Defines the contract between the Transit Provider and its clients. All of 
 * the constants are also used for the transit database.
 * 
 * I basically copied the schema defined by Google, which can be found at
 * https://developers.google.com/transit/gtfs/reference. 
 * 
 * If you are unclear about the purpose of any of the tables or columns, see the
 * above reference for clarification.
 */
public final class TransitContract {
	public static final String AUTHORITY = "com.marylandtransitcommuters.TransitProvider";
	public static final String SCHEME = "content://";
	public static final String SLASH = "/";
	public static final String DATABASE_NAME = "transit.db"; 
	
	/* An array list of all the table names */
	public static final String[] TABLE_ARRAY = {
//		Agency.TABLE_NAME,
//		CalendarDates.TABLE_NAME,
//		Calendar.TABLE_NAME,
		Routes.TABLE_NAME,
//		Shapes.TABLE_NAME,
		Stops.TABLE_NAME,
//		StopTimes.TABLE_NAME,
		Trips.TABLE_NAME
	};
	
	/* An array list of all the SQL create table statements */
	public static final String[] SQL_CREATE_TABLE_ARRAY = {
		Agency.CREATE_TABLE,
		CalendarDates.CREATE_TABLE,
		Calendar.CREATE_TABLE,
		Routes.CREATE_TABLE,
		Shapes.CREATE_TABLE,
		Stops.CREATE_TABLE,
		StopTimes.CREATE_TABLE,
		Trips.CREATE_TABLE
	};
	
	/**
	 * Array of resource ids for each GTFS data file that will be loaded into 
	 * database
	 */
	public static final int[] RAW_IDS = {
//		R.raw.agency,
//		R.raw.calendar_dates,
//		R.raw.calendar,
//		R.raw.routes,
//		R.raw.shapes,
//		R.raw.stops,
//		R.raw.stop_times,
//		R.raw.trips,
	};
	
	/* Do not allow this class to be instantiated */
	private TransitContract() {}
	
	/** 
	 * Agency table contract
	 * <P>Defines: The transit agencies that provided data for the feed.<P>
	 */
	public static final class Agency implements BaseColumns {
		/* Do not allow this class to be instantiated */
		private Agency() {}
		
		/**
		 * Table name
		 */
		public static final String TABLE_NAME = "Agency";
		
		/*
		 * Table columns
		 */
		
		/**
	     * Column name for the agency ID
	     * <P>Type: TEXT</P>
	     */
		public static final String KEY_AGENCY_ID = "AgencyId";
		
		/**
	     * Column name for the agency name
	     * <P>Type: TEXT</P>
	     */
		public static final String KEY_NAME = "Name";
		
		/**
	     * Column name for the agency URL
	     * <P>Type: TEXT</P>
	     */
		public static final String KEY_URL = "Url";
		
		/**
	     * Column name for the agency time zone
	     * <P>Type: TEXT</P>
	     */
		public static final String KEY_TIMEZONE = "Timezone";
		
		/**
	     * Column name for the agency language
	     * <P>Type: TEXT</P>
	     */
		public static final String KEY_LANG = "Language";
		
		/**
	     * Column name for the agency phone number
	     * <P>Type: TEXT</P>
	     */
		public static final String KEY_PHONE = "PhoneNumber";
		
		/**
	     * Column name for the agency fare URL
	     * <P>Type: TEXT</P>
	     */
		public static final String KEY_FARE_URL = "FareUrl";
		
		/*
         * URI definitions
         */

		/**
		 * The content style URI
		 */
		public static final Uri CONTENT_URI = Uri.parse(SCHEME + AUTHORITY + SLASH + TABLE_NAME);
		
		/**
		 * The content URI base for a single row. An ID must be appended.
		 */
		public static final Uri CONTENT_ID_URI_BASE = Uri.parse(SCHEME + AUTHORITY + SLASH + TABLE_NAME + SLASH);
		
        /**
         * The default sort order for this table
         */
        public static final String DEFAULT_SORT_ORDER = KEY_AGENCY_ID + " ASC";
        
		/*
         * MIME type definitions
         */

        /**
         * The MIME type of {@link #CONTENT_URI} providing rows
         */
        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + 
        										"/vnd.com.marylandtransitcommuters.agency";

        /**
         * The MIME type of a {@link #CONTENT_URI} single row
         */
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + 
        										"/vnd.com.marylandtransitcommuters.agency";
		
		/**
         * SQL Statement to create the routes table
         */
        public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " ("
        										  + _ID + " INTEGER PRIMARY KEY,"
        										  + KEY_AGENCY_ID + " TEXT,"
        										  + KEY_NAME + " TEXT,"
        										  + KEY_URL + " TEXT,"
        										  + KEY_TIMEZONE + " TEXT,"
        										  + KEY_LANG + " TEXT," 
        										  + KEY_PHONE + " TEXT,"
        										  + KEY_FARE_URL + " TEXT"
        										  + ");";
        
        /**
         * SQL statement to delete the table
         */
        public static final String DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
        
        /**
         * Array of all the columns. Makes for cleaner code
         */
        public static final String[] KEY_ARRAY = {
			KEY_AGENCY_ID,
			KEY_NAME,
			KEY_URL,
			KEY_TIMEZONE,
			KEY_LANG,
			KEY_PHONE,
			KEY_FARE_URL
        };
	}
	
	/**
	 *  CalendarDates table contract
	 *  <P>Defines: Exceptions for the service IDs defined in the Calendar table. <P>
	 */
	public static final class CalendarDates implements BaseColumns {
		private CalendarDates() {}
		
		/**
		 * Table name
		 */
		public static final String TABLE_NAME = "CalendarDates";
		
		/*
		 * Table columns
		 */
		
		/**
	     * Column name for the service ID
	     * <P>Type: TEXT</P>
	     */
		public static final String KEY_SERVICE_ID = "ServiceId";
		
		/**
	     * Column name for the date the service is down
	     * <P>Type: TEXT</P>
	     */
		public static final String KEY_DATE = "Date";
		
		/**
	     * Column name for the service exception type
	     * <P>Type: INTEGER</P>
	     */
		public static final String KEY_EXCEPTION_TYPE = "ExceptionType";
		
		/*
         * URI definitions
         */

		/**
		 * The content style URI
		 */
		public static final Uri CONTENT_URI = Uri.parse(SCHEME + AUTHORITY + SLASH + TABLE_NAME);
		
		/**
		 * The content URI base for a single row. An ID must be appended.
		 */
		public static final Uri CONTENT_ID_URI_BASE = Uri.parse(SCHEME + AUTHORITY + SLASH + TABLE_NAME + SLASH);
		
        /**
         * The default sort order for this table
         */
        public static final String DEFAULT_SORT_ORDER = KEY_SERVICE_ID+ " ASC";
        
		/*
         * MIME type definitions
         */

        /**
         * The MIME type of {@link #CONTENT_URI} providing rows
         */
        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + 
        										"/vnd.com.marylandtransitcommuters.calendardates";

        /**
         * The MIME type of a {@link #CONTENT_URI} single row
         */
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + 
        										"/vnd.com.marylandtransitcommuters.calendardates";
		
		/**
         * SQL Statement to create the routes table
         */
        public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " ("
        										  + _ID + " INTEGER PRIMARY KEY,"
        										  + KEY_SERVICE_ID + " TEXT,"
        										  + KEY_DATE + " TEXT,"
        										  + KEY_EXCEPTION_TYPE + " INTEGER"
        										  + ");";
        
        /**
         * SQL statement to delete the table
         */
        public static final String DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
        
        /**
         * Array of all the columns. Makes for cleaner code
         */
        public static final String[] KEY_ARRAY = {
			KEY_SERVICE_ID,
			KEY_DATE,
			KEY_EXCEPTION_TYPE
        };
		
	}

	/**
	 * Calendar table contract
	 * <P>Defines: Dates for service IDs using a weekly schedule. Specify when service 
	 * starts and ends, as well as days of the week where service is available.<P>
	 */
	public static final class Calendar implements BaseColumns {
		private Calendar() {}
		
		/**
		 * Table name
		 */
		public static final String TABLE_NAME = "Calendar";
		
		/*
		 * Table columns
		 */
		
		/**
	     * Column name for the service ID
	     * <P>Type: TEXT</P>
	     */
		public static final String KEY_SERVICE_ID = "ServiceId";
		
		/**
	     * Column name for availability on Mondays
	     * <P>Type: INTEGER</P>
	     */
		public static final String KEY_MONDAY = "Monday";
		
		/**
	     * Column name for availability on Tuesdays
	     * <P>Type: INTEGER</P>
	     */
		public static final String KEY_TUESDAY = "Tuesday";
		
		/**
	     * Column name for availability on Wednesdays
	     * <P>Type: INTEGER</P>
	     */
		public static final String KEY_WEDNESDAY = "Wednesday";
		
		/**
	     * Column name for availability on Thursdays
	     * <P>Type: INTEGER</P>
	     */
		public static final String KEY_THURSDAY = "Thursday";
		
		/**
	     * Column name for availability on Fridays
	     * <P>Type: INTEGER</P>
	     */
		public static final String KEY_FRIDAY = "Friday";
		
		/**
	     * Column name for availability on Saturdays
	     * <P>Type: INTEGER</P>
	     */
		public static final String KEY_SATURDAY = "Saturday";
		
		/**
	     * Column name for availability on Sundays
	     * <P>Type: INTEGER</P>
	     */
		public static final String KEY_SUNDAY = "Sunday";
		
		/**
	     * Column name for the service start date
	     * <P>Type: TEXT</P>
	     */
		public static final String KEY_START_DATE = "StartDate";
		
		/**
	     * Column name for the service end date
	     * <P>Type: TEXT</P>
	     */
		public static final String KEY_END_DATE = "EndDate";
		
		/*
         * URI definitions
         */

		/**
		 * The content style URI
		 */
		public static final Uri CONTENT_URI = Uri.parse(SCHEME + AUTHORITY + SLASH + TABLE_NAME);
		
		/**
		 * The content URI base for a single row. An ID must be appended.
		 */
		public static final Uri CONTENT_ID_URI_BASE = Uri.parse(SCHEME + AUTHORITY + SLASH + TABLE_NAME + SLASH);
		
        /**
         * The default sort order for this table
         */
        public static final String DEFAULT_SORT_ORDER = KEY_SERVICE_ID + " ASC";
        
		/*
         * MIME type definitions
         */

        /**
         * The MIME type of {@link #CONTENT_URI} providing rows
         */
        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + 
        										"/vnd.com.marylandtransitcommuters.calendar";

        /**
         * The MIME type of a {@link #CONTENT_URI} single row
         */
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + 
        										"/vnd.com.marylandtransitcommuters.calendar";
		
		/**
         * SQL Statement to create the routes table
         */
        public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " ("
        										  + _ID + " INTEGER PRIMARY KEY,"
        										  + KEY_SERVICE_ID + " TEXT,"
        										  + KEY_MONDAY + " INTEGER,"
        										  + KEY_TUESDAY + " INTEGER,"
        										  + KEY_THURSDAY + " INTEGER,"
        										  + KEY_FRIDAY + " INTEGER," 
        										  + KEY_SATURDAY + " INTEGER,"
        										  + KEY_SUNDAY + " INTEGER,"
        										  + KEY_START_DATE + " TEXT," 
        										  + KEY_END_DATE + " TEXT" 
        										  + ");";
        
        /**
         * SQL statement to delete the table
         */
        public static final String DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
        
        /**
         * Array of all the columns. Makes for cleaner code
         */
        public static final String[] KEY_ARRAY = {
			KEY_SERVICE_ID,
			KEY_MONDAY,
			KEY_TUESDAY,
			KEY_WEDNESDAY,
			KEY_THURSDAY,
			KEY_FRIDAY,
			KEY_SATURDAY,
			KEY_SUNDAY,
			KEY_START_DATE,
			KEY_END_DATE
        };
	}
	
	/**
	 * Routes table contract
	 * <P>Defines: Transit routes. A route is a group of trips that are displayed
	 * to riders as a single service.<P>
	 */
	public static final class Routes implements BaseColumns {		
		private Routes() {}
		
		/**
		 * Table name
		 */
		public static final String TABLE_NAME = "Routes";
		
		/*
		 * Table columns
		 */
		
		/**
	     * Column name for the route ID
	     * <P>Type: TEXT</P>
	     */
		public static final String KEY_ROUTE_ID = "RouteId";
		
		/**
	     * Column name for the agency ID
	     * <P>Type: TEXT</P>
	     */
		public static final String KEY_AGENCY_ID = "AgencyId";
		
		/**
	     * Column name for the route short name
	     * <P>Type: TEXT</P>
	     */
		public static final String KEY_SHORT_NAME = "ShortName";
		
		/**
	     * Column name for the route long name
	     * <P>Type: TEXT</P>
	     */
		public static final String KEY_LONG_NAME = "LongName";
		
		/**
	     * Column name for description of the route
	     * <P>Type: TEXT</P>
	     */
		public static final String KEY_DESCRIPTION = "Description";
		
		/**
	     * Column name for the route type
	     * <P>Type: INTEGER</P>
	     */
		public static final String KEY_ROUTE_TYPE = "RouteType";
		
		/**
	     * Column name for the route URL
	     * <P>Type: TEXT</P>
	     */
		public static final String KEY_URL = "Url";
		
		/**
	     * Column name for the route color
	     * <P>Type: TEXT</P>
	     */
		public static final String KEY_COLOR = "Color";
		
		/**
	     * Column name for the color to use for the text 
	     * <P>Type: TEXT</P>
	     */
		public static final String KEY_TEXT_COLOR = "TextColor";
		
		/*
         * URI definitions
         */
		
		/**
		 * The content style URI
		 */
		public static final Uri CONTENT_URI = Uri.parse(SCHEME + AUTHORITY + SLASH + TABLE_NAME);
		
		/**
		 * The content URI base for a single row. An ID must be appended.
		 */
		public static final Uri CONTENT_ID_URI_BASE = Uri.parse(SCHEME + AUTHORITY + SLASH + TABLE_NAME + SLASH);
		
        /**
         * The default sort order for this table
         */
        public static final String DEFAULT_SORT_ORDER = KEY_SHORT_NAME + " ASC";
        
        /*
         * MIME type definitions
         */

        /**
         * The MIME type of {@link #CONTENT_URI} providing rows
         */
        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + 
        										"/vnd.com.marylandtransitcommuters.routes";

        /**
         * The MIME type of a {@link #CONTENT_URI} single row
         */
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + 
        										"/vnd.com.marylandtransitcommuters.routes";
        
        /* Table manipulation statements */
        
        /**
         * SQL Statement to create the routes table
         */
        public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " ("
        										  + _ID + " INTEGER PRIMARY KEY,"
        										  + KEY_ROUTE_ID + " TEXT,"
        										  + KEY_AGENCY_ID + " TEXT,"
        										  + KEY_SHORT_NAME + " TEXT,"
        										  + KEY_LONG_NAME + " TEXT,"
        										  + KEY_DESCRIPTION + " TEXT,"
        										  + KEY_ROUTE_TYPE + " INTEGER," 
        										  + KEY_URL + " TEXT,"
        										  + KEY_COLOR + " TEXT,"
        										  + KEY_TEXT_COLOR + " TEXT" 
        										  + ");";
        
        /**
         * SQL statement to delete the table
         */
        public static final String DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
        
        /**
         * Array of all the columns. Makes for cleaner code
         */
        public static final String[] KEY_ARRAY = {
			KEY_ROUTE_ID,
			KEY_AGENCY_ID,
			KEY_SHORT_NAME,
			KEY_LONG_NAME,
			KEY_DESCRIPTION,
			KEY_ROUTE_TYPE,
			KEY_URL,
			KEY_COLOR,
			KEY_TEXT_COLOR
        };
	}
	
	/**
	 * Shapes table contract
	 * <P>Defines: Rules for drawing lines on a map to represent a transit 
	 * organization's routes.<p>
	 */
	public static final class Shapes implements BaseColumns {
		private Shapes() {}
		
		/**
		 * Table name
		 */
		public static final String TABLE_NAME = "Shapes";
		
		/*
		 * Table columns
		 */
		
		/**
	     * Column name for the shape ID
	     * <P>Type: TEXT</P>
	     */
		public static final String KEY_SHAPE_ID = "ShapeId";
		
		/**
	     * Column name for the shape point's latitude
	     * <P>Type: REAL</P>
	     */
		public static final String KEY_LATITUDE = "Latitude";
		
		/**
	     * Column name for the shape point's longitude
	     * <P>Type: REAL</P>
	     */
		public static final String KEY_LONGITUDE = "Longitude";
		
		/**
	     * Column name for the sequence order along the shape
	     * <P>Type: INTEGER</P>
	     */
		public static final String KEY_ORDER = "SequenceOrder";
		
		/**
	     * Column name for the distance traveled from the first shape point
	     * <P>Type: REAL</P>
	     */
		public static final String KEY_DISTANCE = "DistanceTraveled";
		
		/*
         * URI definitions
         */

		/**
		 * The content style URI
		 */
		public static final Uri CONTENT_URI = Uri.parse(SCHEME + AUTHORITY + SLASH + TABLE_NAME);
		
		/**
		 * The content URI base for a single row. An ID must be appended.
		 */
		public static final Uri CONTENT_ID_URI_BASE = Uri.parse(SCHEME + AUTHORITY + SLASH + TABLE_NAME + SLASH);
		
        /**
         * The default sort order for this table
         */
        public static final String DEFAULT_SORT_ORDER = KEY_SHAPE_ID + " ASC";
        
		/*
         * MIME type definitions
         */

        /**
         * The MIME type of {@link #CONTENT_URI} providing rows
         */
        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + 
        										"/vnd.com.marylandtransitcommuters.shapes";

        /**
         * The MIME type of a {@link #CONTENT_URI} single row
         */
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + 
        										"/vnd.com.marylandtransitcommuters.shapes";
		
		/**
         * SQL Statement to create the routes table
         */
        public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " ("
        										  + _ID + " INTEGER PRIMARY KEY,"
        										  + KEY_SHAPE_ID + " TEXT,"
        										  + KEY_LATITUDE + " REAL,"
        										  + KEY_LONGITUDE + " REAL,"
        										  + KEY_ORDER + " INTEGER,"
        										  + KEY_DISTANCE + " REAL" 
        										  + ");";
        
        /**
         * SQL statement to delete the table
         */
        public static final String DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
        
        /**
         * Array of all the columns. Makes for cleaner code
         */
        public static final String[] KEY_ARRAY = {
			KEY_SHAPE_ID,
			KEY_LATITUDE,
			KEY_LONGITUDE,
			KEY_ORDER,
			KEY_DISTANCE
        };		
	}
	
	/**
	 * Stops table contract
	 * <P>Defines: Individual locations where vehicles pick up or drop off 
	 * passengers.<P>
	 */
	public static final class Stops implements BaseColumns {
		private Stops() {}
		
		/**
		 * Table name
		 */
		public static final String TABLE_NAME = "Stops";
		
		/*
		 * Table columns
		 */
		
		/**
	     * Column name for the stop ID
	     * <P>Type: TEXT</P>
	     */
		public static final String KEY_STOP_ID = "StopId";
		
		/**
	     * Column name for the stop code
	     * <P>Type: TEXT</P>
	     */
		public static final String KEY_CODE = "Code";
		
		/**
	     * Column name for the stop name
	     * <P>Type: TEXT</P>
	     */
		public static final String KEY_NAME = "Name";
		
		/**
	     * Column name for the stop description
	     * <P>Type: TEXT</P>
	     */
		public static final String KEY_DESC = "Description";
		
		/**
	     * Column name for the stop's latitude
	     * <P>Type: REAL</P>
	     */
		public static final String KEY_LATITUDE = "Latitude";
		
		/**
	     * Column name for the stop's longitude
	     * <P>Type: REAL</P>
	     */
		public static final String KEY_LONGITUDE = "Longitude";
		
		/**
	     * Column name for the stop zone ID
	     * <P>Type: TEXT</P>
	     */
		public static final String KEY_ZONE_ID = "ZoneId";
		
		/**
	     * Column name for the stop URL
	     * <P>Type: TEXT</P>
	     */
		public static final String KEY_URL = "Url";
		
		/**
	     * Column name for the stop type
	     * <P>Type: INTEGER</P>
	     */
		public static final String KEY_STOP_TYPE = "StopType";
		
		/**
	     * Column name for the stop parent station
	     * <P>Type: INTEGER</P>
	     */
		public static final String KEY_PARENT = "ParentStation";
		
		/**
	     * Column name for whether or not the stop supports wheelchair boarding
	     * <P>Type: INTEGER</P>
	     */
		public static final String KEY_WHEELCHAIR = "Wheelchair";
		
		/*
         * URI definitions
         */
		
		/**
		 * The content style URI
		 */
		public static final Uri CONTENT_URI = Uri.parse(SCHEME + AUTHORITY + SLASH + TABLE_NAME);
		
		/**
		 * The content URI base for a single row. An ID must be appended.
		 */
		public static final Uri CONTENT_ID_URI_BASE = Uri.parse(SCHEME + AUTHORITY + SLASH + TABLE_NAME + SLASH);
		
        /**
         * The default sort order for this table
         */
        public static final String DEFAULT_SORT_ORDER = KEY_STOP_ID + " ASC";
        
		/*
         * MIME type definitions
         */

        /**
         * The MIME type of {@link #CONTENT_URI} providing rows
         */
        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + 
        										"/vnd.com.marylandtransitcommuters.stops";

        /**
         * The MIME type of a {@link #CONTENT_URI} single row
         */
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + 
        										"/vnd.com.marylandtransitcommuters.stops";
		
		/**
         * SQL Statement to create the routes table
         */
        public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " ("
        										  + _ID + " INTEGER PRIMARY KEY,"
        										  + KEY_STOP_ID + " TEXT,"
        										  + KEY_CODE + " TEXT,"
        										  + KEY_NAME + " TEXT,"
        										  + KEY_DESC + " TEXT,"
        										  + KEY_LATITUDE + " REAL,"
        										  + KEY_LONGITUDE + " REAL," 
        										  + KEY_ZONE_ID + " TEXT,"
        										  + KEY_URL + " TEXT,"
        										  + KEY_STOP_TYPE + " INTEGER," 
        										  + KEY_PARENT + " INTEGER," 
        										  + KEY_WHEELCHAIR + " INTEGER" 
        										  + ");";
        
        /**
         * SQL statement to delete the table
         */
        public static final String DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
        
        /**
         * Array of all the columns. Makes for cleaner code
         */
        public static final String[] KEY_ARRAY = {
        	KEY_STOP_ID,
			KEY_CODE,
			KEY_NAME,
			KEY_DESC,
			KEY_LATITUDE,
			KEY_LONGITUDE,
			KEY_ZONE_ID,
			KEY_URL,
			KEY_STOP_TYPE,
			KEY_PARENT,
			KEY_WHEELCHAIR,
        };
	}
	
	/**
	 * StopTimes table contract
	 * <P>Defines: Times that a vehicle arrives at and departs from individual
	 * stops for each trip.<P>
	 */
	public static final class StopTimes implements BaseColumns {
		private StopTimes() {}
		
		/**
		 * Table name
		 */
		public static final String TABLE_NAME = "StopTimes";
		
		/*
		 * Table columns
		 */
		
		/**
	     * Column name for trip ID
	     * <P>Type: TEXT</P>
	     */
		public static final String KEY_TRIP_ID = "TripId";
		
		/**
	     * Column name for the arrival time
	     * <P>Type: TEXT</P>
	     */
		public static final String KEY_ARRIVAL_TIME = "ArrivalTime";
		
		/**
	     * Column name for the departure time
	     * <P>Type: TEXT</P>
	     */
		public static final String KEY_DEPARTURE_TIME = "DepartureTime";
		
		/**
	     * Column name for the stop ID
	     * <P>Type: TEXT</P>
	     */
		public static final String KEY_STOP_ID = "StopId";
		
		/**
	     * Column name for the stop sequence number
	     * <P>Type: INTEGER</P>
	     */
		public static final String KEY_STOP_SEQ = "StopOrder";
		
		/**
	     * Column name for the text that appears on the sign for the stop
	     * <P>Type: TEXT</P>
	     */
		public static final String KEY_HEADSIGN = "HeadsignText";
		
		/**
	     * Column name for the pickup type
	     * <P>Type: INTEGER</P>
	     */
		public static final String KEY_PICKUP_TYPE = "PickupType";
		
		/**
	     * Column name for the drop-off type
	     * <P>Type: INTEGER</P>
	     */
		public static final String KEY_DROPOFF_TYPE= "DropoffType";
		
		/**
	     * Column name for the distance traveled from the first shape point
	     * <P>Type: REAL</P>
	     */
		public static final String KEY_DISTANCE = "DistanceTraveled";
		
		/*
         * URI definitions
         */

		/**
		 * The content style URI
		 */
		public static final Uri CONTENT_URI = Uri.parse(SCHEME + AUTHORITY + SLASH + TABLE_NAME);
		
		/**
		 * The content URI base for a single row. An ID must be appended.
		 */
		public static final Uri CONTENT_ID_URI_BASE = Uri.parse(SCHEME + AUTHORITY + SLASH + TABLE_NAME + SLASH);
		
        /**
         * The default sort order for this table
         */
        public static final String DEFAULT_SORT_ORDER = KEY_TRIP_ID + " ASC";
        
		/*
         * MIME type definitions
         */

        /**
         * The MIME type of {@link #CONTENT_URI} providing rows
         */
        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + 
        										"/vnd.com.marylandtransitcommuters.stoptimes";

        /**
         * The MIME type of a {@link #CONTENT_URI} single row
         */
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + 
        										"/vnd.com.marylandtransitcommuters.stoptimes";
		
		/**
         * SQL Statement to create the routes table
         */
        public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " ("
        										  + _ID + " INTEGER PRIMARY KEY,"
        										  + KEY_TRIP_ID + " TEXT,"
        										  + KEY_ARRIVAL_TIME + " TEXT,"
        										  + KEY_DEPARTURE_TIME + " TEXT,"
        										  + KEY_STOP_ID + " TEXT,"
        										  + KEY_STOP_SEQ + " INTEGER," 
        										  + KEY_HEADSIGN + " TEXT,"
        										  + KEY_PICKUP_TYPE + " INTEGER,"
        										  + KEY_DROPOFF_TYPE + " INTEGER," 
        										  + KEY_DISTANCE + " REAL" 
        										  + ");";
        
        /**
         * SQL statement to delete the table
         */
        public static final String DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
        
        /**
         * Array of all the columns. Makes for cleaner code
         */
        public static final String[] KEY_ARRAY = {
        	KEY_TRIP_ID,
        	KEY_ARRIVAL_TIME,
        	KEY_DEPARTURE_TIME,
        	KEY_STOP_ID,
        	KEY_STOP_SEQ,
        	KEY_HEADSIGN,
        	KEY_PICKUP_TYPE,
        	KEY_DROPOFF_TYPE,
        	KEY_DISTANCE
        };
	}
	
	/**
	 * Trips table contract
	 * <P>Defines: Trips for each route. A trip is a sequence of two or more 
	 * stops that occurs at a specific time.<P>
	 */
	public static final class Trips implements BaseColumns {
		private Trips() {}
		
		/**
		 * Table name
		 */
		public static final String TABLE_NAME = "Trips";
		
		/*
		 * Table columns
		 */
		
		/**
	     * Column name for the route ID
	     * <P>Type: TEXT</P>
	     */
		public static final String KEY_ROUTE_ID = "RouteId";
		
		/**
	     * Column name for the service ID
	     * <P>Type: TEXT</P>
	     */
		public static final String KEY_SERVICE_ID = "ServiceId";
		
		/**
	     * Column name for the trip ID
	     * <P>Type: TEXT</P>
	     */
		public static final String KEY_TRIP_ID = "TripId";
		
		/**
	     * Column name for the trip headsign
	     * <P>Type: TEXT</P>
	     */
		public static final String KEY_HEADSIGN = "HeadsignText";
		
		/**
	     * Column name for the trip short name
	     * <P>Type: TEXT</P>
	     */
		public static final String KEY_SHORT_NAME = "ShortName";
		
		/**
	     * Column name for the direction ID
	     * <P>Type: TEXT</P>
	     */
		public static final String KEY_DIRECTION_ID = "DirectionId";
		
		/**
	     * Column name for the block ID
	     * <P>Type: TEXT</P>
	     */
		public static final String KEY_BLOCK_ID = "BlockId";
		
		
		/**
	     * Column name for the shape ID
	     * <P>Type: TEXT</P>
	     */
		public static final String KEY_SHAPE_ID = "ShapeId";
		
		/**
	     * Column name for wheelchair accessibility
	     * <P>Type: TEXT</P>
	     */
		public static final String KEY_WHEELCHAIR = "Wheelchair";
		
		/*
         * URI definitions
         */

		/**
		 * The content style URI
		 */
		public static final Uri CONTENT_URI = Uri.parse(SCHEME + AUTHORITY + SLASH + TABLE_NAME);
		
		/**
		 * The content URI base for a single row. An ID must be appended.
		 */
		public static final Uri CONTENT_ID_URI_BASE = Uri.parse(SCHEME + AUTHORITY + SLASH + TABLE_NAME + SLASH);
		
        /**
         * The default sort order for this table
         */
        public static final String DEFAULT_SORT_ORDER = KEY_ROUTE_ID + " ASC";
        
		/*
         * MIME type definitions
         */

        /**
         * The MIME type of {@link #CONTENT_URI} providing rows
         */
        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + 
        										"/vnd.com.marylandtransitcommuters.trips";

        /**
         * The MIME type of a {@link #CONTENT_URI} single row
         */
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + 
        										"/vnd.com.marylandtransitcommuters.trips";
		
		/**
         * SQL Statement to create the routes table
         */
        public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " ("
        										  + _ID + " INTEGER PRIMARY KEY,"
        										  + KEY_ROUTE_ID + " TEXT,"
        										  + KEY_SERVICE_ID + " TEXT,"
        										  + KEY_TRIP_ID + " TEXT,"
        										  + KEY_HEADSIGN + " TEXT,"
        										  + KEY_SHORT_NAME + " TEXT," 
        										  + KEY_DIRECTION_ID + " TEXT,"
        										  + KEY_BLOCK_ID + " TEXT,"
        										  + KEY_SHAPE_ID + " TEXT" 
        										  + ");";
        
        /**
         * SQL statement to delete the table
         */
        public static final String DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
        
        /**
         * Array of all the columns. Makes for cleaner code
         */
        public static final String[] KEY_ARRAY = {
			KEY_ROUTE_ID,
			KEY_SERVICE_ID,
			KEY_TRIP_ID,
			KEY_HEADSIGN,
			KEY_SHORT_NAME,
			KEY_DIRECTION_ID,
			KEY_BLOCK_ID,
			KEY_SHAPE_ID
        };
	}
}
