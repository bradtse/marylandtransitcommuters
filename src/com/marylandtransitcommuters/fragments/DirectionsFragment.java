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
import com.marylandtransitcommuters.dataobjects.Direction;
import com.marylandtransitcommuters.service.TransitService;


/**
 * The fragment that allows the user to select which direction they are 
 * traveling
 */
public class DirectionsFragment extends TransitFragment {
	public static final String TAG = "direction";
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		Log.d(MainActivity.LOG_TAG, "DirectionsFragment onCreateView()");
		mRootView = inflater.inflate(R.layout.fragment_layout_directions, container, false);
		super.onCreateView(inflater, container, savedInstanceState);
		return mRootView;
	}
	
	@Override
	protected void setupBreadcrumbs() {
		String route = mTransitData.getRouteShortName() + " " + mTransitData.getRouteLongName();
		TextView routeText = (TextView) mRootView.findViewById(R.id.info_route_data);
		routeText.setText(route);
	}
	
	@Override
	public void setIntentServiceType(Intent intent) {
		intent.putExtra(TransitService.DataType.KEY, TransitService.DataType.DIRECTIONS);
	}

	@Override
	public void setListViewAdapter() {
		mAdapter = new CustomSimpleAdapter(
				mContext, 
				mTransitData.getDirectionsList(),
				R.layout.fragment_listview_row, 
				new String[] {Direction.TRIP_HEADSIGN},
				new int[] {R.id.transit_list_item}
				)
		{
			
			@Override
			public View getView(int pos, View convertView, ViewGroup parent) {
				View view = super.getView(pos, convertView, parent);
				TextView tv = (TextView) view.findViewById(R.id.transit_list_item);
				
				@SuppressWarnings("unchecked")
				Map<String, String> map = (Map<String, String>) mData.get(pos);
				String headsign = map.get(Direction.TRIP_HEADSIGN);
				int color = getResources().getColor(R.color.glue_color);
				
				if (headsign.contains(Direction.GLUE)) {
					int start = headsign.indexOf(Direction.GLUE);
					int end = start + Direction.GLUE.length();
					SpannableString result = new SpannableString(headsign);
				
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
		@SuppressWarnings("unchecked")
		Map<String, String> map = (Map<String, String>) mAdapter.getItem(index);
		String directionId = map.get(Direction.DIR_ID);
		String headSign = map.get(Direction.TRIP_HEADSIGN);

		mTransitData.selectDirection(directionId, headSign);
		
		mCallback.replaceFragment(StartStopsFragment.TAG, new StartStopsFragment(), true, false, true, true);
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		mSearchView.setQueryHint("Filter by direction name");	
	}
}
