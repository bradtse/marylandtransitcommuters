package com.marylandtransitcommuters;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockFragment;

public class StopFragment extends SherlockFragment {
	private View rootView;
	private Context context;
	private ListView mStopList;
	private Cursor mCursor;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = getActivity();
		
		// The column we want to query for
		String[] mProjection = {
				TransitContract.Stops.KEY_STOP_ID
		};

		// Append the _ID to the base uri
		String id = getArguments().getString(TransitContract.Routes._ID);
		Uri data = Uri.withAppendedPath(TransitContract.Routes.CONTENT_URI, id);
		
		mCursor = context.getContentResolver().query(
			data,
			mProjection,
			null,
			null,
			null
		);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_time, container, false);
		
		return rootView;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		mStopList = (ListView) rootView.findViewById(R.id.routes_list);
		
		Log.d(MainActivity.BRAD, "Initialize stop list");

	    // Specify the corresponding layout elements where we want the columns to go
	    int[] to = {R.id.short_name, R.id.long_name};
	    
	    // Specify the columns we want to display in the ListView
	    String[] from = { 
	    		TransitContract.Routes.KEY_SHORT_NAME,
	    		TransitContract.Routes.KEY_LONG_NAME
	    };
	    
	    Log.d(MainActivity.BRAD, "Attempting to create SimpleCursorAdapter");
	    
	    SimpleCursorAdapter mCursorAdapter = new SimpleCursorAdapter(
	    		context, 
	    		R.layout.routes_list_row,
	    		mCursor, 
	    		from, 
	    		to, 
	    		0
	    );
	    
	    mStopList.setAdapter(mCursorAdapter);
	    
	    Log.d(MainActivity.BRAD, "Successfully created and set SimpleCursorAdapter");
	    
	    // Define the on-click listener for the route items
	    mStopList.setOnItemClickListener(new StopItemClickListener());
	}
	
	/** 
     * The click listener for the ListView of routes 
     */
    private class StopItemClickListener implements ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			return;
		}
    }
}
