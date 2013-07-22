package com.marylandtransitcommuters.fragments;

import java.util.Map;

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
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.widget.SearchView.OnQueryTextListener;
import com.marylandtransitcommuters.MainActivity;
import com.marylandtransitcommuters.R;
import com.marylandtransitcommuters.dataobjects.Direction;
import com.marylandtransitcommuters.service.TransitService;


/**
 * The fragment that allows the user to select which direction they are 
 * traveling
 */
public class DirectionsFragment extends TransitFragment {
	public static final String TAG = "direction";
	
	private SimpleAdapter adapter;
	
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
		adapter = new SimpleAdapter(
				context, 
				data.getDirectionsList(),
				R.layout.fragment_listview_row, 
				new String[] {Direction.TRIP_HEADSIGN},
				new int[] {R.id.transit_list_item}
				)
		{
			
			@Override
			public View getView(int pos, View convertView, ViewGroup parent) {
				View view = super.getView(pos, convertView, parent);
				TextView tv = (TextView) view.findViewById(R.id.transit_list_item);
				
				Map<String, String> map = (Map<String, String>) adapter.getItem(pos);
				String headsign = map.get(Direction.TRIP_HEADSIGN);
				
				if (headsign.contains(Direction.GLUE)) {
					int start = headsign.indexOf(Direction.GLUE);
					int end = start + Direction.GLUE.length();
					SpannableString result = new SpannableString(headsign);
				
					result.setSpan(new StyleSpan(android.graphics.Typeface.BOLD_ITALIC), 
								start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
					result.setSpan(new RelativeSizeSpan(0.8f), start, end, 
								Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
					result.setSpan(new ForegroundColorSpan(0xFFED4035), start, end,
								Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
					tv.setText(result);
				} 
				
				return view;
			}
		};
		
		mList.setAdapter(adapter);
	}
	
	public void selectItem(int index) {
		Log.d(MainActivity.LOG_TAG, "Item selected: " + String.valueOf(index));
		
		Map<String, String> map = (Map<String, String>) adapter.getItem(index);
		String directionId = map.get(Direction.DIR_ID);
				
		data.setDirection(directionId);
		
		replaceFragment(new StopsFragment(), TAG, StopsFragment.TAG);
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		
		search.setQueryHint("Filter by direction name");	
		search.setOnQueryTextListener(new OnQueryTextListener() {

			@Override
			public boolean onQueryTextSubmit(String query) {
				return false;
			}

			@Override
			public boolean onQueryTextChange(String newText) {
//				adapter.getFilter().filter(newText);
				return false;
			}
		});
	}
}
