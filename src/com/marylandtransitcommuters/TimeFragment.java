package com.marylandtransitcommuters;

import android.content.Intent;
import android.widget.ArrayAdapter;

public class TimeFragment extends TransitFragment {
	
	@Override
	public void setServiceType(Intent intent) {
		intent.putExtra(TransitService.Type.KEY, TransitService.Type.TIMES);
	}

	@Override
	public void setAdapter() {
		mList.setAdapter(new ArrayAdapter<String>(
					context, android.R.layout.simple_list_item_1, 
					profile.getTimesCol("arrival_time")));
	}

	@Override
	public void selectItem(int position) {
		return;
	}
}