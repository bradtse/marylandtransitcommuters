package com.marylandtransitcommuters.fragments;

import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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
import com.marylandtransitcommuters.dataobjects.StartStop;
import com.marylandtransitcommuters.service.TransitService;

public class StartStopsFragment extends TransitFragment {
	public static final String TAG = "startstops";

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		Log.d(MainActivity.LOG_TAG, "StartStopsFragment onCreateView()");
		mRootView = inflater.inflate(R.layout.fragment_layout_startstops, 
									container, false);
		TextView text = (TextView) mRootView.findViewById(R.id.fragment_header_start);
		text.setText(R.string.start_stop_header);
		
		super.onCreateView(inflater, container, savedInstanceState);
			
		if (savedInstanceState != null) {
			setupFragment();
			mVisible = savedInstanceState.getBoolean("mAlive");
			if (mVisible == false) {
				FragmentManager fm = getFragmentManager();
		    	Fragment currFrag = fm.findFragmentByTag(TAG);
				FragmentTransaction ft = fm.beginTransaction();
				ft.hide(currFrag);
				ft.commit();
			}
		}
		
		return mRootView;
	}
	
	protected void setupBreadcrumbs() {
		String route = mData.getRouteShortName() + " " + mData.getRouteLongName();
		String direction = mData.getDirectionHeadsign();
		
		TextView routeText = (TextView) mRootView.findViewById(R.id.info_route_data);
		TextView dirText = (TextView) mRootView.findViewById(R.id.info_direction_data);

		routeText.setText(route);
		dirText.setText(direction);
	}
	
	@Override
	public void setIntentServiceType(Intent intent) {
		intent.putExtra(TransitService.DataType.KEY, TransitService.DataType.STARTSTOPS);
	}

	@Override
	public void setAdapter() {
		mAdapter = new CustomSimpleAdapter(
					mContext, 
					mData.getStartStopsList(),
					R.layout.fragment_list_row,
					new String[] {StartStop.STOP_NAME},
					new int[] {R.id.transit_list_item}
					) 
		{
			@Override
			public View getView(int pos, View convertView, ViewGroup parent) {
				View view = super.getView(pos, convertView, parent);
				TextView tv = (TextView) view.findViewById(R.id.transit_list_item);
				
				@SuppressWarnings("unchecked")
				Map<String, String> map = (Map<String, String>) mAdapter.getItem(pos);
				String stopName = map.get(StartStop.STOP_NAME);
				
				if (stopName.contains("&")) {
					int start = stopName.indexOf(StartStop.GLUE);
					int end = start + StartStop.GLUE.length();
					SpannableString result = new SpannableString(stopName);
					int color = getResources().getColor(R.color.glue_color);
				
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
//		Log.d(MainActivity.LOG_TAG, "Item selected: " + String.valueOf(index));
		
		@SuppressWarnings("unchecked")
		Map<String, String> map = (Map<String, String>) mAdapter.getItem(index);
		String stopId = map.get(StartStop.STOP_ID);
		String stopName = map.get(StartStop.STOP_NAME);
		String stopSeq = map.get(StartStop.STOP_SEQ);
		
		mData.selectStartStop(stopId, stopName, stopSeq);
		
		mCallback.performTransaction(TAG, FinalStopsFragment.TAG, new FinalStopsFragment(), true);
	}
	
	@Override
	public void onCreateOptionsMenu(final Menu menu, final MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		mSearchView.setQueryHint("Filter by stop name");
	}
}
