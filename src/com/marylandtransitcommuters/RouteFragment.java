package com.marylandtransitcommuters;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.actionbarsherlock.app.SherlockFragment;

public class RouteFragment extends SherlockFragment {
	private Context context;
	private View rootView;
	
	public RouteFragment() {};
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = getActivity();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_routes, container, false);

		return rootView;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		ListView mRouteList = (ListView) rootView.findViewById(R.id.routes_list);
		
		Log.d(MainActivity.BRAD, "Initialize route list");
		
		String[] mProjection = {
				TransitContract.Routes._ID,
				TransitContract.Routes.COLUMN_NAME_SHORT_NAME,
				TransitContract.Routes.COLUMN_NAME_LONG_NAME
		};
		
	    Cursor mCursor = context.getContentResolver().query(
	    		TransitContract.Routes.CONTENT_URI,
	    		mProjection,
	    		null,
	    		null,
	    		TransitContract.Routes.DEFAULT_SORT_ORDER
	    );
	    
	    Log.d(MainActivity.BRAD, "mCursor.getCount() = " + mCursor.getCount());
	    
	    // Specify the columns we want to display in the result
	    String[] from = new String[] { 
	    		TransitContract.Routes.COLUMN_NAME_SHORT_NAME,
	    		TransitContract.Routes.COLUMN_NAME_LONG_NAME
	    };
	
	    // Specify the corresponding layout elements where we want the columns to go
	    int[] to = new int[] { 
	    		R.id.short_name,
	    		R.id.long_name
	    };
	    
	    Log.d(MainActivity.BRAD, "Attempting to create SimpleCursorAdapter");
	    SimpleCursorAdapter mCursorAdapter = new SimpleCursorAdapter(
	    		context, 
	    		R.layout.routes_list,
	    		mCursor, 
	    		from, 
	    		to, 
	    		0
	    );
	    
	    mRouteList.setAdapter(mCursorAdapter);
	    
	    Log.d(MainActivity.BRAD, "Successfully created and set SimpleCursorAdapter");
//	    
//	    // Define the on-click listener for the route items
//	    mRouteList.setOnItemClickListener(new RouteItemClickListener());
		
	}
	
    /* The click listener for ListView of routes */
    private class RouteItemClickListener implements ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			selectRoute(id);
		}
    }
    
    /* Helper function to replace the current fragment */
    private void selectRoute(long id) {
		Log.d(MainActivity.BRAD, "Item selected: " + String.valueOf(id));
		Fragment fragment = new TimeFragment();
		Bundle args = new Bundle();
		args.putString(TransitContract.Routes._ID, String.valueOf(id));
		fragment.setArguments(args);
		Log.d(MainActivity.BRAD, "Got past creation of fragment");
		FragmentManager fragmentManager = getFragmentManager();
		FragmentTransaction fragmentTrans = fragmentManager.beginTransaction();
		fragmentTrans.replace(R.id.content_frame, fragment).commit();
		Log.d(MainActivity.BRAD, "Got past replacing of fragment");
    }
}
