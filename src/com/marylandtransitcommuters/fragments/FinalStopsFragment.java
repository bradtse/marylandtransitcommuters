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
import com.marylandtransitcommuters.dataobjects.FinalStop;
import com.marylandtransitcommuters.service.TransitService;

public class FinalStopsFragment extends TransitFragment {
	public static final String TAG = "finalstops";
		
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		TextView text = (TextView) rootView.findViewById(R.id.fragment_header);
		text.setText(R.string.final_stop_header);
		
		super.onActivityCreated(savedInstanceState);
	}
	
	@Override
	public void setServiceType(Intent intent) {
		intent.putExtra(TransitService.DataType.KEY, TransitService.DataType.FINALSTOPS);
	}

	@Override
	public void setAdapter() {
		adapter = new CustomSimpleAdapter(
					context, 
					data.getFinalStopsList(),
					R.layout.fragment_list_row, 
					new String[] {FinalStop.STOP_NAME},
					new int[] {R.id.transit_list_item}
					) 
		{
			
			@Override
			public View getView(int pos, View convertView, ViewGroup parent) {
				View view = super.getView(pos, convertView, parent);
				TextView tv = (TextView) view.findViewById(R.id.transit_list_item);

				Map<String, String> map = (Map<String, String>) adapter.getItem(pos);
				String stopName = map.get(FinalStop.STOP_NAME);
				
				if (stopName.contains(FinalStop.GLUE)) {
					int start = stopName.indexOf(FinalStop.GLUE);
					int end = start + FinalStop.GLUE.length();
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
		String stopId = map.get(FinalStop.STOP_ID);
		String stopName = map.get(FinalStop.STOP_NAME);
		String stopSeq = map.get(FinalStop.STOP_SEQ);
		
		data.setFinalStop(stopId, stopName, stopSeq);
		
		replaceFragment(new StartStopsFragment(), TAG, StartStopsFragment.TAG);
	}
	
	@Override
	public void onCreateOptionsMenu(final Menu menu, final MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		search.setQueryHint("Filter by stop name");
	}
}
