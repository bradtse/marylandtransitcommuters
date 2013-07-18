package com.marylandtransitcommuters;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class StopFragment extends TransitFragment {

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		TextView text = (TextView) rootView.findViewById(R.id.fragment_header);
		text.setText("Choose your stop:");
		super.onActivityCreated(savedInstanceState);
	}
	
	@Override
	public void setServiceType(Intent intent) {
		intent.putExtra(TransitService.Type.KEY, TransitService.Type.STOPS);
	}

	@Override
	public void setAdapter() {
		mList.setAdapter(new ArrayAdapter<String>(
					context, android.R.layout.simple_list_item_1, 
					profile.getStopsCol(SearchData.STOP_NAME)));
		mList.setOnItemClickListener(new ItemClickListener());
	}

	@Override
	public void selectItem(int position) {
		Log.d(MainActivity.TAG, "Item selected: " + String.valueOf(position));
		
		profile.setIndex(position, TransitService.Type.STOPS);
		
		replaceFragment(new TimeFragment());
	}
}
