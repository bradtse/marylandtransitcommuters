package com.marylandtransitcommuters.fragments;


import java.util.ArrayList;
import java.util.HashMap;

import adapters.CustomAdapter;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.widget.SearchView;
import com.actionbarsherlock.widget.SearchView.OnQueryTextListener;
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
	private SearchView search;
    
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		TextView text = (TextView) rootView.findViewById(R.id.fragment_header);
		text.setText(R.string.routes_header);
		
//		search = (SearchView) rootView.findViewById(R.id.menu_search);
//		search.setQueryHint("Filter by route # or name");
		
//		filterText = (EditText) rootView.findViewById(R.id.search_box);
//		filterText.setHint("Filter by route # or name");
//		filterText.addTextChangedListener(filterTextWatcher);
		
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
		
		HashMap<String, String> map = (HashMap<String, String>) adapter.getItem(index);
		String routeId = map.get(TransitData.ROUTE_ID);
		Log.d(MainActivity.LOG_TAG, "RouteID: " + routeId);
		
		data.setRouteId(routeId);
		
		replaceFragment(new DirectionsFragment(), TAG, DirectionsFragment.TAG);
	}
	
	@Override
	public void onPrepareOptionsMenu(Menu menu) {
		
		super.onPrepareOptionsMenu(menu);
    }	
	@Override
	public void onCreateOptionsMenu(final Menu menu, final MenuInflater inflater) {
		menu.clear();
		Log.d(MainActivity.LOG_TAG, "Got called!!");
		inflater.inflate(R.menu.main, menu);
		MenuItem searchItem = menu.findItem(R.id.menu_search);
		search = (SearchView) searchItem.getActionView();
		
		search.setIconifiedByDefault(false);
		search.setBackgroundColor(Color.TRANSPARENT);
		search.setQueryHint("Filter by route # or name");
//		MenuItem item = menu.add("Seach");
//		item.setIcon(R.drawable.ic_action_search);
//		item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
//		SearchView sv = new SearchView(context);
		search.setOnQueryTextListener(new OnQueryTextListener() {
			
			@Override
			public boolean onQueryTextSubmit(String query) {
				return false;
			}
			
			@Override
			public boolean onQueryTextChange(String newText) {
				adapter.getFilter().filter(newText);
				return false;
			}
		});
//		item.setActionView(sv);
	}
	
//	@Override
//	public void onDestroy() {
//		super.onDestroy();
//		filterText.removeTextChangedListener(filterTextWatcher);
//	}
}
