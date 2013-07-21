package com.marylandtransitcommuters.fragments;


import java.util.ArrayList;
import java.util.HashMap;

import adapters.CustomAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import com.marylandtransitcommuters.MainActivity;
import com.marylandtransitcommuters.R;
import com.marylandtransitcommuters.TransitData;
import com.marylandtransitcommuters.service.TransitService;

/**
 * The fragment showing the list of all available routes
 */
public class RoutesFragment extends TransitFragment {
	public static final String TAG = "routes";
	
	private EditText filterText;
	private CustomAdapter adapter;
	private ArrayList<HashMap<String, String>> routesList;
    
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		TextView text = (TextView) rootView.findViewById(R.id.fragment_header);
		text.setText(R.string.routes_header);
		
		filterText = (EditText) rootView.findViewById(R.id.search_box);
		filterText.setHint("Filter by route # or name");
		filterText.addTextChangedListener(filterTextWatcher);
		
		super.onActivityCreated(savedInstanceState);
	}
	
	private TextWatcher filterTextWatcher = new TextWatcher() {

		@Override
		public void afterTextChanged(Editable s) {			
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {			
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			adapter.getFilter().filter(s);
		}
		
	};
	
	@Override
	public void setServiceType(Intent intent) {
		intent.putExtra(TransitService.DataType.KEY, TransitService.DataType.ROUTES);
	}

	@Override
	public void setAdapter() {
		routesList = data.getRoutesList();
		adapter = new CustomAdapter(
				context, 
				routesList,
				R.layout.routes_listview_row,
				new String[] {TransitData.ROUTE_SHORT_NAME, TransitData.ROUTE_LONG_NAME},
				new int[] {R.id.route_short_name, R.id.route_long_name}
				);
		
		mList.setAdapter(adapter);
	}

	@Override
	public void selectItem(int index) {
		Log.d(MainActivity.LOG_TAG, "Item selected: " + String.valueOf(index));
		
		data.setRouteId(index);
		
		replaceFragment(new DirectionsFragment(), TAG, DirectionsFragment.TAG);
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		filterText.removeTextChangedListener(filterTextWatcher);
	}
}
