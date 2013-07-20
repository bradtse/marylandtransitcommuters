package com.marylandtransitcommuters.fragments;

import com.marylandtransitcommuters.MainActivity;
import com.marylandtransitcommuters.R;
import com.marylandtransitcommuters.TransitData;
import com.marylandtransitcommuters.R.id;
import com.marylandtransitcommuters.R.layout;
import com.marylandtransitcommuters.R.string;
import com.marylandtransitcommuters.service.TransitService;
import com.marylandtransitcommuters.service.TransitService.DataType;

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
		intent.putExtra(TransitService.DataType.KEY, TransitService.DataType.DIRECTIONS);
	}

	@Override
	public void setAdapter() {
		final String[] list = profile.getDirectionsList();
		mList.setAdapter(new ArrayAdapter<String>(context, 
						 R.layout.transit_listview_row, list) {
			@Override
			public View getView(int pos, View convertView, ViewGroup parent) {
				View view = super.getView(pos, convertView, parent);
				TextView tv = (TextView) view.findViewById(R.id.transit_list_item);
				String direction = list[pos];
				
				if (direction.contains("to ")) {
					int index = direction.indexOf("to ");
					SpannableString result = new SpannableString(direction);
				
					result.setSpan(new StyleSpan(android.graphics.Typeface.BOLD_ITALIC), 
								index, index+2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
					result.setSpan(new RelativeSizeSpan(0.8f), index, index+2, 
								Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
					result.setSpan(new ForegroundColorSpan(0xFFED4035), index, index+2,
								Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
					tv.setText(result);
				} 
				return view;
			}
		});
	}
	
	public void selectItem(int index) {
		Log.d(MainActivity.LOG_TAG, "Item selected: " + String.valueOf(index));
				
		profile.setDirectionId(index);
		
		replaceFragment(new StopsFragment(), TAG, StopsFragment.TAG);
	}
}
