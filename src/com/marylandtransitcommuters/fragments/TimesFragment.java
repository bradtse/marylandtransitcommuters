package com.marylandtransitcommuters.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.widget.SearchView;
import com.marylandtransitcommuters.R;
import com.marylandtransitcommuters.adapters.CustomSimpleAdapter;
import com.marylandtransitcommuters.dataobjects.Time;
import com.marylandtransitcommuters.service.TransitService;

public class TimesFragment extends TransitFragment {
	public static final String TAG = "times";
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_layout_times, container, false);
		
		setupInfoTextViews();
		
		return rootView;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		TextView text = (TextView) rootView.findViewById(R.id.fragment_header);
		text.setText(R.string.times_header);
		super.onActivityCreated(savedInstanceState);
	}
	
	private void setupInfoTextViews() {
		String route = "Route: " + data.getRouteShortName() + " " + data.getRouteLongName();
		String direction = "Direction: " + data.getDirectionHeadsign();
		String startStop = "Start Stop: " + data.getStartStopName();
		String finalStop = "Final Stop: " + data.getFinalStopName();
		
		TextView routeText = (TextView) rootView.findViewById(R.id.times_route);
		TextView dirText = (TextView) rootView.findViewById(R.id.times_direction);
		TextView startText = (TextView) rootView.findViewById(R.id.times_start_stop);
		TextView finalText = (TextView) rootView.findViewById(R.id.times_final_stop);

		routeText.setText(route);
		dirText.setText(direction);
		startText.setText(startStop);
		finalText.setText(finalStop);
	}
	
	@Override
	public void setServiceType(Intent intent) {
		intent.putExtra(TransitService.DataType.KEY, TransitService.DataType.TIMES);
	}

	@Override
	public void setAdapter() {
		adapter = new CustomSimpleAdapter(
					context, 
					data.getTimesList(),
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
		
		mList.setEmptyView((TextView) rootView.findViewById(R.id.empty));
	}
	
	@Override 
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		// Keeps a reference to the SearchView for subclasses to use
		MenuItem searchItem = menu.findItem(R.id.menu_search);
		search = (SearchView) searchItem.getActionView();
		
		search.setVisibility(View.GONE);
	}

	@Override
	public void selectItem(int position) {
		return;
	}
}