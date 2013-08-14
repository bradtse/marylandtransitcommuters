package com.marylandtransitcommuters.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.widget.SearchView;
import com.marylandtransitcommuters.MainActivity;
import com.marylandtransitcommuters.R;
import com.marylandtransitcommuters.adapters.CustomSimpleAdapter;
import com.marylandtransitcommuters.dataobjects.Time;
import com.marylandtransitcommuters.service.TransitService;

public class TimesFragment extends TransitFragment {
	public static final String TAG = "times";
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		Log.d(MainActivity.LOG_TAG, "TimesFragment onCreateView()");
		mRootView = inflater.inflate(R.layout.fragment_layout_times, container, false);
		super.onCreateView(inflater, container, savedInstanceState);
		return mRootView;
	}
	
	protected void setupBreadcrumbs() {
		String route = mTransitData.getRouteShortName() + " " + mTransitData.getRouteLongName();
		String direction = mTransitData.getDirectionHeadsign();
		String startStop = mTransitData.getStartStopName();
		String finalStop = mTransitData.getFinalStopName();
		
		TextView routeText = (TextView) mRootView.findViewById(R.id.info_route_data);
		TextView dirText = (TextView) mRootView.findViewById(R.id.info_direction_data);
		TextView startText = (TextView) mRootView.findViewById(R.id.info_start_stop_data);
		TextView finalText = (TextView) mRootView.findViewById(R.id.info_final_stop_data);

		routeText.setText(route);
		dirText.setText(direction);
		startText.setText(startStop);
		finalText.setText(finalStop);
	}
	
	@Override
	public void setIntentServiceType(Intent intent) {
		intent.putExtra(TransitService.DataType.KEY, TransitService.DataType.TIMES);
	}

	@Override
	public void setListViewAdapter() {
		mAdapter = new CustomSimpleAdapter(
					mContext, 
					mTransitData.getTimesList(),
					R.layout.fragment_times_listview_row,
					new String[] {Time.ARRIVAL_TIME},
					new int[] {R.id.time_list_item}
					)
		{

			// Disable clicking for this ListView
			@Override
			public boolean isEnabled(int position) {
				return false;
			}
		};
		
		mList.setAdapter(mAdapter);
		mList.setEmptyView((TextView) mRootView.findViewById(R.id.empty));
	}
	
	@Override 
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.time, menu);
		
		// Hide the SearchView
		MenuItem searchItem = menu.findItem(R.id.menu_search);
		mSearchView = (SearchView) searchItem.getActionView();    
		mSearchView.setVisibility(View.GONE);
	}
	
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	switch(item.getItemId()) {
    		case R.id.favorite:
	    		Toast.makeText(mContext, "Added to favorites!", Toast.LENGTH_SHORT).show();
	    		return true;
    		case R.id.refresh:
	    		Toast.makeText(mContext, "Refresh!", Toast.LENGTH_SHORT).show();
	    		return true;
	    	default:
	    		return super.onOptionsItemSelected(item);
    	}
    }

	// Clicking is disabled for this fragment's ListView
	@Override
	public void selectItem(int position) {}
}