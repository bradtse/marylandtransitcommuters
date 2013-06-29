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
		public static final String COLUMN_NAME_ID = "AgencyId";
		
		/**
	     * Column name for the agency name
	     * <P>Type: TEXT</P>
	     */
		public static final String COLUMN_NAME_NAME = "Name";
		
		/**
	     * Column name for the agency URL
	     * <P>Type: TEXT</P>
	     */
		public static final String COLUMN_NAME_URL = "Url";
		
		/**
	     * Column name for the agency time zone
	     * <P>Type: TEXT</P>
	     */
		public static final String COLUMN_NAME_TIMEZONE = "Timezone";
		
		/**
	     * Column name for the agency language
	     * <P>Type: TEXT</P>
	     */
		public static final String COLUMN_NAME_LANG = "Language";
		
		/**
	     * Column name for the agency phone number
	     * <P>Type: TEXT</P>
	     */
		public static final String COLUMN_NAME_PHONE = "PhoneNumber";
		
		/**
	     * Column name for the agency fare URL
	     * <P>Type: TEXT</P>
	     */
		public static final String COLUMN_NAME_FARE_URL = "FareUrl";
		
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
		public static final String COLUMN_NAME_SERVICE_ID = "ServiceId";
		
		/**
	     * Column name for the date the service is down
	     * <P>Type: TEXT</P>
	     */
		public static final String COLUMN_NAME_DATE = "Date";
		
		/**
	     * Column name for the service exception type
	     * <P>Type: INTEGER</P>
	     */
		public static final String COLUMN_NAME_EXCEPTION_TYPE = "ExceptionType";
		
		/*
         * URI definitions
         */
		
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
		public static final String COLUMN_NAME_SERVICE_ID = "ServiceId";
		
		/**
	     * Column name for availability on Mondays
	     * <P>Type: INTEGER</P>
	     */
		public static final String COLUMN_NAME_MONDAY = "Monday";
		
		/**
	     * Column name for availability on Tuesdays
	     * <P>Type: INTEGER</P>
	     */
		public static final String COLUMN_NAME_TUESDAY = "Tuesday";
		
		/**
	     * Column name for availability on Wednesdays
	     * <P>Type: INTEGER</P>
	     */
		public static final String COLUMN_NAME_WEDNESDAY = "Wednesday";
		
		/**
	     * Column name for availability on Thursdays
	     * <P>Type: INTEGER</P>
	     */
		public static final String COLUMN_NAME_THURSDAY = "Thursday";
		
		/**
	     * Column name for availability on Fridays
	     * <P>Type: INTEGER</P>
	     */
		public static final String COLUMN_NAME_FRIDAY = "Friday";
		
		/**
	     * Column name for availability on Saturdays
	     * <P>Type: INTEGER</P>
	     */
		public static final String COLUMN_NAME_SATURDAY = "Saturday";
		
		/**
	     * Column name for availability on Sundays
	     * <P>Type: INTEGER</P>
	     */
		public static final String COLUMN_NAME_SUNDAY = "Sunday";
		
		/**
	     * Column name for the service start date
	     * <P>Type: TEXT</P>
	     */
		public static final String COLUMN_NAME_START_DATE = "StartDate";
		
		/**
	     * Column name for the service end date
	     * <P>Type: TEXT</P>
	     */
		public static final String COLUMN_NAME_END_DATE = "EndDate";
		
		/*
         * URI definitions
         */
		
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
		public static final String COLUMN_NAME_ROUTE_ID = "RouteId";
		
		/**
	     * Column name for the agency ID
	     * <P>Type: TEXT</P>
	     */
		public static final String COLUMN_NAME_AGENCY_ID = "AgencyId";
		
		/**
	     * Column name for the route short name
	     * <P>Type: TEXT</P>
	     */
		public static final String COLUMN_NAME_SHORT_NAME = "ShortName";
		
		/**
	     * Column name for the route long name
	     * <P>Type: TEXT</P>
	     */
		public static final String COLUMN_NAME_LONG_NAME = "LongName";
		
		/**
	     * Column name for description of the route
	     * <P>Type: TEXT</P>
	     */
		public static final String COLUMN_NAME_DESCRIPTION = "Description";
		
		/**
	     * Column name for the route type
	     * <P>Type: INTEGER</P>
	     */
		public static final String COLUMN_NAME_ROUTE_TYPE = "RouteType";
		
		/**
	     * Column name for the route URL
	     * <P>Type: TEXT</P>
	     */
		public static final String COLUMN_NAME_URL = "Url";
		
		/**
	     * Column name for the route color
	     * <P>Type: TEXT</P>
	     */
		public static final String COLUMN_NAME_COLOR = "Color";
		
		/**
	     * Column name for the color to use for the text 
	     * <P>Type: TEXT</P>
	     */
		public static final String COLUMN_NAME_TEXT_COLOR = "TextColor";
		
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
        public static final String DEFAULT_SORT_ORDER = COLUMN_NAME_SHORT_NAME + " ASC";
        
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
        										  + COLUMN_NAME_ROUTE_ID + " TEXT,"
        										  + COLUMN_NAME_AGENCY_ID + " TEXT,"
        										  + COLUMN_NAME_SHORT_NAME + " TEXT,"
        										  + COLUMN_NAME_LONG_NAME + " TEXT,"
        										  + COLUMN_NAME_DESCRIPTION + " TEXT,"
        										  + COLUMN_NAME_ROUTE_TYPE + " INTEGER," 
        										  + COLUMN_NAME_URL + " TEXT,"
        										  + COLUMN_NAME_COLOR + " TEXT,"
        										  + COLUMN_NAME_TEXT_COLOR + " TEXT" 
        										  + ");";
        
        /**
         * SQL statement to delete the table
         */
        public static final String DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
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
		public static final String COLUMN_NAME_SHAPE_ID = "ShapeId";
		
		/**
	     * Column name for the shape point's latitude
	     * <P>Type: TEXT</P>
	     */
		public static final String COLUMN_NAME_LATITUDE = "Latitude";
		
		/**
	     * Column name for the shape point's longitude
	     * <P>Type: TEXT</P>
	     */
		public static final String COLUMN_NAME_LONGITUDE = "Longitude";
		
		/**
	     * Column name for the sequence order along the shape
	     * <P>Type: INTEGER</P>
	     */
		public static final String COLUMN_NAME_ORDER = "SequenceOrder";
		
		/**
	     * Column name for the distance traveled from the first shape point
	     * <P>Type: REAL</P>
	     */
		public static final String COLUMN_NAME_DISTANCE = "DistanceTraveled";
		
		/*
         * URI definitions
         */
		
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
		public static final String COLUMN_NAME_STOP_ID = "StopId";
		
		/**
	     * Column name for the stop code
	     * <P>Type: TEXT</P>
	     */
		public static final String COLUMN_NAME_CODE = "Code";
		
		/**
	     * Column name for the stop name
	     * <P>Type: TEXT</P>
	     */
		public static final String COLUMN_NAME_NAME = "Name";
		
		/**
	     * Column name for the stop description
	     * <P>Type: TEXT</P>
	     */
		public static final String COLUMN_NAME_DESCRIPTION = "Description";
		
		/**
	     * Column name for the stop's latitude
	     * <P>Type: TEXT</P>
	     */
		public static final String COLUMN_NAME_LATITUDE = "Latitude";
		
		/**
	     * Column name for the stop's longitude
	     * <P>Type: TEXT</P>
	     */
		public static final String COLUMN_NAME_LONGITUDE = "Longitude";
		
		/**
	     * Column name for the stop zone ID
	     * <P>Type: TEXT</P>
	     */
		public static final String COLUMN_NAME_ZONE_ID = "ZoneId";
		
		/**
	     * Column name for the stop URL
	     * <P>Type: TEXT</P>
	     */
		public static final String COLUMN_NAME_URL = "Url";
		
		/**
	     * Column name for the stop type
	     * <P>Type: INTEGER</P>
	     */
		public static final String COLUMN_NAME_STOP_TYPE = "StopType";
		
		/**
	     * Column name for the stop parent station
	     * <P>Type: INTEGER</P>
	     */
		public static final String COLUMN_NAME_PARENT = "ParentStation";
		
		/**
	     * Column name for whether or not the stop supports wheelchair boarding
	     * <P>Type: INTEGER</P>
	     */
		public static final String COLUMN_NAME_WHEELCHAIR = "Wheelchair";
		
		/*
         * URI definitions
         */
		
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
		public static final String COLUMN_NAME_TRIP_ID = "TripId";
		
		/**
	     * Column name for the arrival time
	     * <P>Type: TEXT</P>
	     */
		public static final String COLUMN_NAME_ARRIVAL_TIME = "ArrivalTime";
		
		/**
	     * Column name for the departure time
	     * <P>Type: TEXT</P>
	     */
		public static final String COLUMN_NAME_DEPARTURE_TIME = "DepartureTime";
		
		/**
	     * Column name for the stop ID
	     * <P>Type: TEXT</P>
	     */
		public static final String COLUMN_NAME_STOP_ID = "StopId";
		
		/**
	     * Column name for the stop sequence number
	     * <P>Type: INTEGER</P>
	     */
		public static final String COLUMN_NAME_STOP_SEQ = "StopOrder";
		
		/**
	     * Column name for the text that appears on the sign for the stop
	     * <P>Type: TEXT</P>
	     */
		public static final String COLUMN_NAME_HEADSIGN = "HeadsignText";
		
		/**
	     * Column name for the pickup type
	     * <P>Type: INTEGER</P>
	     */
		public static final String COLUMN_NAME_PICKUP_TYPE = "PickupType";
		
		/**
	     * Column name for the drop-off type
	     * <P>Type: INTEGER</P>
	     */
		public static final String COLUMN_NAME_DROPOFF_TYPE= "DropoffType";
		
		/**
	     * Column name for the distance traveled from the first shape point
	     * <P>Type: REAL</P>
	     */
		public static final String COLUMN_NAME_DISTANCE = "DistanceTraveled";
		
		/*
         * URI definitions
         */
	
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
		public static final String COLUMN_NAME_ROUTE_ID = "RouteId";
		
		/**
	     * Column name for the service ID
	     * <P>Type: TEXT</P>
	     */
		public static final String COLUMN_NAME_SERVICE_ID = "ServiceId";
		
		/**
	     * Column name for the trip ID
	     * <P>Type: TEXT</P>
	     */
		public static final String COLUMN_NAME_TRIP_ID = "TripId";
		
		/**
	     * Column name for the trip headsign
	     * <P>Type: TEXT</P>
	     */
		public static final String COLUMN_NAME_HEADSIGN = "HeadsignText";
		
		/**
	     * Column name for the trip short name
	     * <P>Type: TEXT</P>
	     */
		public static final String COLUMN_NAME_SHORT_NAME = "ShortName";
		
		/**
	     * Column name for the direction ID
	     * <P>Type: TEXT</P>
	     */
		public static final String COLUMN_NAME_DIRECTION_ID = "DirectionId";
		
		/**
	     * Column name for the block ID
	     * <P>Type: TEXT</P>
	     */
		public static final String COLUMN_NAME_BLOCK_ID = "BlockId";
		
		
		/**
	     * Column name for the shape ID
	     * <P>Type: TEXT</P>
	     */
		public static final String COLUMN_NAME_DISTANCE = "ShapeId";
		
		/**
	     * Column name for wheelchair accessibility
	     * <P>Type: TEXT</P>
	     */
		public static final String COLUMN_NAME_WHEELCHAIR = "Wheelchair";
		
		/*
         * URI definitions
         */
	
	}
}
