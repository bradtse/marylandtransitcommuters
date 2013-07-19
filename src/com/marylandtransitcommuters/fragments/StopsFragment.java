package com.marylandtransitcommuters.fragments;

import com.marylandtransitcommuters.MainActivity;
import com.marylandtransitcommuters.R;
import com.marylandtransitcommuters.R.id;
import com.marylandtransitcommuters.R.layout;
import com.marylandtransitcommuters.R.string;
import com.marylandtransitcommuters.service.TransitService;
import com.marylandtransitcommuters.service.TransitService.DataType;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class StopsFragment extends TransitFragment {
	public static final String TAG = "stops";

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		TextView text = (TextView) rootView.findViewById(R.id.fragment_header);
		text.setText(R.string.stops_header);
		super.onActivityCreated(savedInstanceState);
	}
	
	@Override
	public void setServiceType(Intent intent) {
		intent.putExtra(TransitService.DataType.KEY, TransitService.DataType.STOPS);
	}

	@Override
	public void setAdapter() {
		mList.setAdapter(new ArrayAdapter<String>(
					context, R.layout.transit_listview_row, 
					profile.getStopsList()) 
		{
			@Override
			public View getView(int pos, View convertView, ViewGroup parent) {
				View view = super.getView(pos, convertView, parent);
				return view;
			}
		});
	}

	@Override
	public void selectItem(int index) {
		Log.d(MainActivity.TAG, "Item selected: " + String.valueOf(index));
		
		profile.setStopId(index);
		
		replaceFragment(new TimesFragment(), TAG, TimesFragment.TAG);
	}
}
