package com.marylandtransitcommuters.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

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
//		if (savedInstanceState == null) {
			mRootView = inflater.inflate(R.layout.fragment_layout_times, container, false);
			TextView text = (TextView) mRootView.findViewById(R.id.fragment_header_time);
			text.setText(R.string.times_header);
			
			mList = (ListView) mRootView.findViewById(R.id.fragment_list);
			setupBreadcrumbs();
//		}
		
		return mRootView;
	}
	
	protected void setupBreadcrumbs() {
		String route = mData.getRouteShortName() + " " + mData.getRouteLongName();
		String direction = mData.getDirectionHeadsign();
		String startStop = mData.getStartStopName();
		String finalStop = mData.getFinalStopName();
		
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
	public void setAdapter() {
		mAdapter = new CustomSimpleAdapter(
					mContext, 
					mData.getTimesList(),
					R.layout.times_list_row,
					new String[] {Time.ARRIVAL_TIME},
					new int[] {R.id.time_list_item}
					)
		{

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
		MenuItem searchItem = menu.findItem(R.id.menu_search);
		mSearchView = (SearchView) searchItem.getActionView();
		
		mSearchView.setVisibility(View.GONE);
	}

	@Override
	public void selectItem(int position) {
		return;
	}
}