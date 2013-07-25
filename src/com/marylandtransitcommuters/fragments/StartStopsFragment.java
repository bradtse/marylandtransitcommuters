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
	public void onActivityCreated(Bundle savedInstanceState) {
		TextView text = (TextView) rootView.findViewById(R.id.fragment_header);
		text.setText(R.string.start_stop_header);
		super.onActivityCreated(savedInstanceState);
	}
	
	@Override
	public void setServiceType(Intent intent) {
		intent.putExtra(TransitService.DataType.KEY, TransitService.DataType.STARTSTOPS);
	}

	@Override
	public void setAdapter() {
		adapter = new CustomSimpleAdapter(
					context, 
					data.getStartStopsList(),
					R.layout.fragment_list_row,
					new String[] {StartStop.STOP_NAME},
					new int[] {R.id.transit_list_item}
					) 
		{
			@Override
			public View getView(int pos, View convertView, ViewGroup parent) {
				View view = super.getView(pos, convertView, parent);
				TextView tv = (TextView) view.findViewById(R.id.transit_list_item);
				
				Map<String, String> map = (Map<String, String>) adapter.getItem(pos);
				String stopName = map.get(StartStop.STOP_NAME);
				
				if (stopName.contains("&")) {
					int start = stopName.indexOf(StartStop.GLUE);
					int end = start + StartStop.GLUE.length();
					SpannableString result = new SpannableString(stopName);
				
					result.setSpan(new StyleSpan(android.graphics.Typeface.BOLD_ITALIC), 
								start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
					result.setSpan(new RelativeSizeSpan(0.8f), start, end, 
								Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
					result.setSpan(new ForegroundColorSpan(0xFFED4035), start, end,
								Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
					tv.setText(result);
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
		String stopId = map.get(StartStop.STOP_ID);
		String stopName = map.get(StartStop.STOP_NAME);
		
		data.selectStartStop(stopId, stopName);
		
		replaceFragment(new TimesFragment(), TAG, TimesFragment.TAG);
	}
	
	@Override
	public void onCreateOptionsMenu(final Menu menu, final MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		search.setQueryHint("Filter by stop name");
	}
}
