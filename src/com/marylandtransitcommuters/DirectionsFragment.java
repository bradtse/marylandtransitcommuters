package com.marylandtransitcommuters;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * The fragment that allows the user to select whether they want AM (inbound) or
 * PM (outbound) times to be shown.
 */
public class DirectionsFragment extends TransitFragment {
	public static final String TAG = "direction";
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		TextView text = (TextView) rootView.findViewById(R.id.fragment_header);
		text.setText(R.string.direction_header);
		super.onActivityCreated(savedInstanceState);
	}
	
	@Override
	public void setServiceType(Intent intent) {
		intent.putExtra(TransitService.Type.KEY, TransitService.Type.DIRECTIONS);
	}

	@Override
	public void setAdapter() {
		mList.setAdapter(new ArrayAdapter<String>(
				context, android.R.layout.simple_list_item_1, 
				profile.getDirsList()));
	}
	
	public void selectItem(int position) {
		Log.d(MainActivity.TAG, "Item selected: " + String.valueOf(position));
				
		profile.setIndex(position, TransitService.Type.DIRECTIONS);
		
		replaceFragment(new StopsFragment(), TAG, StopsFragment.TAG);
	}
}
