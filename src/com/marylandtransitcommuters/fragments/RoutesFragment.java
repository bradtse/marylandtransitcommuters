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
		rootView = inflater.inflate(R.layout.fragment_layout_routes, 
									container, false);
		return rootView;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		TextView text = (TextView) rootView.findViewById(R.id.fragment_header_route);
		text.setText(R.string.routes_header);
		
		super.onActivityCreated(savedInstanceState);
	}
	
	@Override
	public void setServiceType(Intent intent) {
		intent.putExtra(TransitService.DataType.KEY, TransitService.DataType.ROUTES);
	}

	@Override
	public void setAdapter() {
		adapter = new CustomSimpleAdapter(
				context, 
				data.getRoutesList(),
				R.layout.routes_list_row,
				new String[] {Route.SHORT_NAME, Route.LONG_NAME},
				new int[] {R.id.route_short_name, R.id.route_long_name}
				) 
		{
	
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				View view = super.getView(position, convertView, parent);
				TextView tv = (TextView) view.findViewById(R.id.route_long_name);
				
				Map<String, String> map = (Map<String, String>) adapter.getItem(position);
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
	
		mList.setAdapter(adapter);
	}

	@Override
	public void selectItem(int index) {
		Log.d(MainActivity.LOG_TAG, "Item selected: " + String.valueOf(index));
		
		Map<String, String> map = (Map<String, String>) adapter.getItem(index);
		String routeId = map.get(Route.ROUTE_ID);
		String shortName = map.get(Route.SHORT_NAME);
		String longName = map.get(Route.LONG_NAME);
		
		data.selectRoute(routeId, shortName, longName);
			
		replaceFragment(new DirectionsFragment(), TAG, DirectionsFragment.TAG);
	}
	
	@Override
	public void onCreateOptionsMenu(final Menu menu, final MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		search.setQueryHint("Filter by route # or name");
	}
}
