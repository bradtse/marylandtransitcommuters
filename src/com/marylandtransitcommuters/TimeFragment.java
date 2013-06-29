package com.marylandtransitcommuters;

import com.actionbarsherlock.app.SherlockFragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class TimeFragment extends SherlockFragment {
	public TimeFragment() {}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_time, container, false);
		String id = getArguments().getString(TransitContract.Routes._ID);
		TextView textView = (TextView) rootView.findViewById(R.id.time);
		textView.setText(id);
		return rootView;
	}

}
