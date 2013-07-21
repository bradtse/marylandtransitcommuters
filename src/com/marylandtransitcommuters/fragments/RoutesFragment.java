package com.marylandtransitcommuters.fragments;


import java.util.ArrayList;
import java.util.HashMap;

import adapters.CustomAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.widget.SearchView.OnQueryTextListener;
import com.marylandtransitcommuters.MainActivity;
import com.marylandtransitcommuters.R;
import com.marylandtransitcommuters.service.TransitService;

import dataobjects.Route;
import dataobjects.TransitData;

/**
 * The fragment showing the list of all available routes
 */
public class RoutesFragment extends TransitFragment {
	public static final String TAG = "routes";
	
	private CustomAdapter adapter;
    
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		Log.d(MainActivity.LOG_TAG, "Got into onActivityCreated of Routes");
		TextView text = (TextView) rootView.findViewById(R.id.fragment_header);
		text.setText(R.string.routes_header);
		
		super.onActivityCreated(savedInstanceState);
	}
	
	@Override
	public void setServiceType(Intent intent) {
		intent.putExtra(TransitService.DataType.KEY, TransitService.DataType.ROUTES);
	}

	@Override
	public void setAdapter() {
		Log.d(MainActivity.LOG_TAG, "Attempting to set adapter");
		adapter = new CustomAdapter(
				context, 
				data.getRoutesList(),
				R.layout.routes_listview_row,
				new String[] {Route.SHORT_NAME, Route.LONG_NAME},
				new int[] {R.id.route_short_name, R.id.route_long_name}
				);
	
		mList.setAdapter(adapter);
		Log.d(MainActivity.LOG_TAG, "Finished setting adapter");
	}

	@Override
	public void selectItem(int index) {
		Log.d(MainActivity.LOG_TAG, "Item selected: " + String.valueOf(index));
		
		HashMap<String, String> map = (HashMap<String, String>) adapter.getItem(index);
		String routeId = map.get(Route.ROUTE_ID);
		Log.d(MainActivity.LOG_TAG, "RouteID: " + routeId);
		
		data.selectRoute(routeId);
		
		replaceFragment(new DirectionsFragment(), TAG, DirectionsFragment.TAG);
	}
	
	@Override
	public void onCreateOptionsMenu(final Menu menu, final MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);

		search.setQueryHint("Filter by route # or name");
		search.setOnQueryTextListener(new OnQueryTextListener() {
			
			@Override
			public boolean onQueryTextSubmit(String query) {
				return false;
			}
			
			@Override
			public boolean onQueryTextChange(String newText) {
				adapter.getFilter().filter(newText);
				return false;
			}
		});
	}
}
