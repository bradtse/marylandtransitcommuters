package com.marylandtransitcommuters.fragments;

import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.marylandtransitcommuters.MainActivity;
import com.marylandtransitcommuters.R;
import com.marylandtransitcommuters.adapters.CustomSimpleAdapter;
import com.marylandtransitcommuters.dataobjects.FinalStop;
import com.marylandtransitcommuters.service.TransitService;

public class FinalStopsFragment extends TransitFragment {
	public static final String TAG = "finalstops";
		
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		Log.d(MainActivity.LOG_TAG, "FinalStopsFragment onCreateView()");
		mRootView = inflater.inflate(R.layout.fragment_layout_finalstops, container, false);
		super.onCreateView(inflater, container, savedInstanceState);
		return mRootView;
	}
	
	protected void setupBreadcrumbs() {
		String route = mTransitData.getRouteShortName() + " " + mTransitData.getRouteLongName();
		String direction = mTransitData.getDirectionHeadsign();
		String startStop = mTransitData.getStartStopName();
		
		TextView routeText = (TextView) mRootView.findViewById(R.id.info_route_data);
		TextView dirText = (TextView) mRootView.findViewById(R.id.info_direction_data);
		TextView startText = (TextView) mRootView.findViewById(R.id.info_start_stop_data);

		routeText.setText(route);
		dirText.setText(direction);
		startText.setText(startStop);
	}
	
	@Override
	public void setIntentServiceType(Intent intent) {
		intent.putExtra(TransitService.DataType.KEY, TransitService.DataType.FINALSTOPS);
	}

	@Override
	public void setListViewAdapter() {
		mAdapter = new CustomSimpleAdapter(
					mContext, 
					mTransitData.getFinalStopsList(),
					R.layout.fragment_list_row, 
					new String[] {FinalStop.STOP_NAME},
					new int[] {R.id.transit_list_item}
					) 
		{
			
			@Override
			public View getView(int pos, View convertView, ViewGroup parent) {
				View view = super.getView(pos, convertView, parent);
				TextView tv = (TextView) view.findViewById(R.id.transit_list_item);

				Map<String, String> map = (Map<String, String>) mData.get(pos);
				String stopName = map.get(FinalStop.STOP_NAME);
				int color = getResources().getColor(R.color.glue_color);
				
				if (stopName.contains(FinalStop.GLUE)) {
					int start = stopName.indexOf(FinalStop.GLUE);
					int end = start + FinalStop.GLUE.length();
					SpannableString result = new SpannableString(stopName);
				
					result.setSpan(new StyleSpan(android.graphics.Typeface.BOLD_ITALIC), 
								start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
					result.setSpan(new RelativeSizeSpan(0.8f), start, end, 
								Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
					result.setSpan(new ForegroundColorSpan(color), start, end,
								Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
					tv.setText(result);
				} 
				
				return view;
			}
		};
		
		mList.setAdapter(mAdapter);
	}

	@Override
	public void selectItem(int index) {
		Map<String, String> map = (Map<String, String>) mAdapter.getItem(index);
		String stopId = map.get(FinalStop.STOP_ID);
		String stopName = map.get(FinalStop.STOP_NAME);
		
		mTransitData.setFinalStop(stopId, stopName);
		
		mCallback.showFragment(TAG, TimesFragment.TAG, new TimesFragment(), true);
	}
	
	@Override
	public void onCreateOptionsMenu(final Menu menu, final MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		mSearchView.setQueryHint("Filter by stop name");
	}
}
