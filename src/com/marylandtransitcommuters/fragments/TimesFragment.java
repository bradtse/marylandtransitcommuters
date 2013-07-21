package com.marylandtransitcommuters.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.marylandtransitcommuters.R;
import com.marylandtransitcommuters.service.TransitService;

public class TimesFragment extends TransitFragment {
	public static final String TAG = "times";
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.times_fragment_layout, container, false);
		return rootView;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		TextView text = (TextView) rootView.findViewById(R.id.times_fragment_header);
		text.setText(R.string.times_header);
		super.onActivityCreated(savedInstanceState);
	}
	
	@Override
	public void setServiceType(Intent intent) {
		intent.putExtra(TransitService.DataType.KEY, TransitService.DataType.TIMES);
	}

	@Override
	public void setAdapter() {
		mList.setAdapter(new ArrayAdapter<String>(
					context, R.layout.transit_listview_row, 
					data.getTimesList()) 
		{
			@Override
			public View getView(int pos, View convertView, ViewGroup parent) {
				View view = super.getView(pos, convertView, parent);
				return view;
			}
			@Override
			public boolean isEnabled(int position) {
				return false;
			}
		});
		mList.setEmptyView((TextView) rootView.findViewById(R.id.empty));
	}

	@Override
	public void selectItem(int position) {
		return;
	}
}