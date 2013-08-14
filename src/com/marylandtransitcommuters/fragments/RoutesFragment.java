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
import com.marylandtransitcommuters.dataobjects.Route;
import com.marylandtransitcommuters.service.TransitService;


/**
 * The fragment showing the list of all available routes
 */
public class RoutesFragment extends TransitFragment {
	public static final String TAG = "routes";
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		Log.d(MainActivity.LOG_TAG, "RoutesFragment onCreateView()");
		mRootView = inflater.inflate(R.layout.fragment_layout_routes, container, false);	
		super.onCreateView(inflater, container, savedInstanceState);	
		return mRootView;
	}
	
	@Override
	public void setIntentServiceType(Intent intent) {
		intent.putExtra(TransitService.DataType.KEY, TransitService.DataType.ROUTES);
	}

	@Override
	public void setListViewAdapter() {
		mAdapter = new CustomSimpleAdapter(
				mContext, 
				mTransitData.getRoutesList(),
				R.layout.fragment_routes_listview_row,
				new String[] {Route.SHORT_NAME, Route.LONG_NAME},
				new int[] {R.id.route_short_name, R.id.route_long_name}
				) 
		{
	
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				View view = super.getView(position, convertView, parent);
				TextView tv = (TextView) view.findViewById(R.id.route_long_name);
				
				@SuppressWarnings("unchecked")
				Map<String, String> map = (Map<String, String>) mData.get(position);
				String longName = map.get(Route.LONG_NAME);
				int color = getResources().getColor(R.color.glue_color);
				
				if (longName.contains(" to ")) {
					int index = longName.indexOf(" to ") + 1;
					SpannableString colored = new SpannableString(longName);
				
					colored.setSpan(new StyleSpan(android.graphics.Typeface.BOLD_ITALIC), 
								index, index+2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
					colored.setSpan(new RelativeSizeSpan(0.8f), index, index+2, 
								Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
					colored.setSpan(new ForegroundColorSpan(color), index, index+2,
								Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
					tv.setText(colored);
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
		String routeId = map.get(Route.ROUTE_ID);
		String shortName = map.get(Route.SHORT_NAME);
		String longName = map.get(Route.LONG_NAME);
		
		mTransitData.selectRoute(routeId, shortName, longName);
			
		mCallback.replaceFragment(DirectionsFragment.TAG, new DirectionsFragment(), true, false);
	}
	
	@Override
	public void onCreateOptionsMenu(final Menu menu, final MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		mSearchView.setQueryHint("Filter by route # or name");
	}

	@Override
	protected void setupBreadcrumbs() {}
}
