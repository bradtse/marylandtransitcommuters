package com.marylandtransitcommuters.fragments;


import java.util.ArrayList;
import java.util.HashMap;

import android.content.Intent;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.marylandtransitcommuters.MainActivity;
import com.marylandtransitcommuters.R;
import com.marylandtransitcommuters.TransitData;
import com.marylandtransitcommuters.service.TransitService;

/**
 * The fragment showing the list of all available routes
 */
public class RoutesFragment extends TransitFragment {
	public static final String TAG = "routes";
    
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
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
		final ArrayList<HashMap<String, String>> list = profile.getRoutesList();
		mList.setAdapter(new SimpleAdapter(
					context, 
					list,
					R.layout.routes_listview_row,
					new String[] {TransitData.ROUTE_SHORT_NAME, TransitData.ROUTE_LONG_NAME},
					new int[] {R.id.route_short_name, R.id.route_long_name}
					) 
		{
			@Override 
			public View getView(int pos, View convertView, ViewGroup parent) {
				View view = super.getView(pos, convertView, parent);
				TextView tv = (TextView) view.findViewById(R.id.route_long_name);
				String longName = list.get(pos).get(TransitData.ROUTE_LONG_NAME);
				
				if (longName.contains(" to ")) {
					int index = longName.indexOf(" to ") + 1;
					SpannableString result = new SpannableString(longName);
				
					result.setSpan(new StyleSpan(android.graphics.Typeface.BOLD_ITALIC), 
								index, index+2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
					result.setSpan(new RelativeSizeSpan(0.8f), index, index+2, 
								Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
					result.setSpan(new ForegroundColorSpan(0xFFED4035), index, index+2,
								Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
					tv.setText(result);
				} 
				return view;
			}
		});
	}

	@Override
	public void selectItem(int index) {
		Log.d(MainActivity.LOG_TAG, "Item selected: " + String.valueOf(index));
		
		profile.setRouteId(index);
		
		replaceFragment(new DirectionsFragment(), TAG, DirectionsFragment.TAG);
	}
}
