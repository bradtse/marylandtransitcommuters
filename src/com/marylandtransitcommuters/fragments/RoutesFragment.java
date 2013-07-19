package com.marylandtransitcommuters.fragments;


import java.util.ArrayList;
import java.util.HashMap;

import com.marylandtransitcommuters.MainActivity;
import com.marylandtransitcommuters.R;
import com.marylandtransitcommuters.SearchInstance;
import com.marylandtransitcommuters.R.id;
import com.marylandtransitcommuters.R.layout;
import com.marylandtransitcommuters.R.string;
import com.marylandtransitcommuters.service.TransitService;
import com.marylandtransitcommuters.service.TransitService.DataType;

import android.content.Intent;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleAdapter;
import android.widget.TextView;

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
					new String[] {SearchInstance.ROUTE_SHORT_NAME, SearchInstance.ROUTE_LONG_NAME},
					new int[] {R.id.route_short_name, R.id.route_long_name}
					) 
		{
			@Override 
			public View getView(int pos, View convertView, ViewGroup parent) {
				View view = super.getView(pos, convertView, parent);
				TextView tv = (TextView) view.findViewById(R.id.route_long_name);
				String longName = list.get(pos).get(SearchInstance.ROUTE_LONG_NAME);
				if (longName.contains(" to ")) {
					SpannableStringBuilder result = new SpannableStringBuilder(longName);
					int index = longName.indexOf(" to ");
					SpannableString to = new SpannableString("to");
					to.setSpan(new StyleSpan(android.graphics.Typeface.BOLD_ITALIC), 
								0, to.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
					to.setSpan(new RelativeSizeSpan(0.8f), 0, to.length(), 
								Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
					to.setSpan(new ForegroundColorSpan(0xFFED4035), 0, to.length(),
								Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
					result.replace(index+1, index+3, to);
					tv.setText(result);
				} 
				return view;
			}
		});
	}

	@Override
	public void selectItem(int index) {
		Log.d(MainActivity.TAG, "Item selected: " + String.valueOf(index));
		
		profile.setRouteId(index);
		
		replaceFragment(new DirectionsFragment(), TAG, DirectionsFragment.TAG);
	}
}
