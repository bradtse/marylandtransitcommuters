package com.marylandtransitcommuters;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;

/**
 * The fragment that allows the user to select whether they want AM (inbound) or
 * PM (outbound) times to be shown.
 */
public class DirectionFragment extends SherlockFragment {
	private Context context;
	private View rootView;
	private ListView mList;
		
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = getActivity();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_layout, container, false);
		return rootView;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		TextView text = (TextView) rootView.findViewById(R.id.fragment_header);
		text.setText("Choose your direction:");
		
		mList = (ListView) rootView.findViewById(R.id.fragment_list);
		String[] direction = getResources().getStringArray(R.array.direction);
		
		mList.setAdapter(new ArrayAdapter<String>(
				context, android.R.layout.simple_list_item_1, 
				direction));
		mList.setOnItemClickListener(new ItemClickListener());
	}
	
    class ItemClickListener implements ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			selectItem(position);
		}
    }

	public void selectItem(int position) {
		Log.d(MainActivity.TAG, "Item selected: " + String.valueOf(position));
		
		SearchData profile = SearchData.getInstance();
		profile.setIndex(position, TransitService.Type.DIRECTION);
		
		/* 
		 * Starts the stops fragment
		 */
		Fragment fragment = new StopFragment();
		
		FragmentManager fragmentManager = getFragmentManager();
		FragmentTransaction fragmentTrans = fragmentManager.beginTransaction();
		
		fragmentTrans.replace(R.id.content_frame, fragment);
		fragmentTrans.addToBackStack(null);
		fragmentTrans.commit();	
	}
}
