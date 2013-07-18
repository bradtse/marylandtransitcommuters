package com.marylandtransitcommuters;


import java.util.ArrayList;
import java.util.HashMap;

import android.content.Intent;
import android.graphics.Color;
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
public class RouteFragment extends TransitFragment {
    
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		TextView text = (TextView) rootView.findViewById(R.id.fragment_header);
		text.setText("Choose your route:");
		super.onActivityCreated(savedInstanceState);
	}
	
	@Override
	public void setServiceType(Intent intent) {
		intent.putExtra(TransitService.Type.KEY, TransitService.Type.ROUTES);
	}

	@Override
	public void setAdapter() {
		final ArrayList<HashMap<String, String>> list = profile.getRoutesList();
		mList.setAdapter(new SimpleAdapter(
					context, 
					list,
					R.layout.routes_listview_row,
					new String[] {SearchData.ROUTE_SHORT_NAME, SearchData.ROUTE_LONG_NAME},
					new int[] {R.id.route_short_name, R.id.route_long_name}
					) {
			@Override 
			public View getView(int pos, View convertView, ViewGroup parent) {
				View view = super.getView(pos, convertView, parent);
				TextView tv = (TextView) view.findViewById(R.id.route_long_name);
				String longName = list.get(pos).get(SearchData.ROUTE_LONG_NAME);
				if (longName.contains(" to ")) {
					SpannableStringBuilder result = new SpannableStringBuilder(longName);
					int index = longName.indexOf(" to ");
					SpannableString to = new SpannableString("to");
					to.setSpan(new StyleSpan(android.graphics.Typeface.BOLD_ITALIC), 
								0, to.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
					to.setSpan(new RelativeSizeSpan(0.8f), 0, to.length(), 
								Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
					to.setSpan(new ForegroundColorSpan(Color.RED), 0, to.length(),
								Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
					result.replace(index+1, index+3, to);
					tv.setText(result);
				} 
				return view;
			}
		});
		mList.setOnItemClickListener(new ItemClickListener());		
	}

	@Override
	public void selectItem(int position) {
		Log.d(MainActivity.TAG, "Item selected: " + String.valueOf(position));
		
		profile.setIndex(position, TransitService.Type.ROUTES);
		
		replaceFragment(new DirectionFragment());
	}
}
