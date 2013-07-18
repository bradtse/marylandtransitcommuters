package com.marylandtransitcommuters;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class TimesFragment extends TransitFragment {
	public static final String TAG = "times";
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		TextView text = (TextView) rootView.findViewById(R.id.fragment_header);
		text.setText(R.string.times_header);
		super.onActivityCreated(savedInstanceState);
	}
	@Override
	public void setServiceType(Intent intent) {
		intent.putExtra(TransitService.Type.KEY, TransitService.Type.TIMES);
	}

	@Override
	public void setAdapter() {
		mList.setAdapter(new ArrayAdapter<String>(
					context, android.R.layout.simple_list_item_1, 
					profile.getTimesList()));
		mList.setOnItemClickListener(null); // turn off 
		mList.setEmptyView((TextView) rootView.findViewById(R.id.empty));
	}

	@Override
	public void selectItem(int position) {
		return;
	}
}