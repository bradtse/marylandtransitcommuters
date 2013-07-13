package com.marylandtransitcommuters;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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
import com.actionbarsherlock.app.SherlockFragment;

/**
 * The fragment showing the list of all available routes
 */
public class RouteFragment extends SherlockFragment implements TransitReceiver.Receiver {
	private Context context;
	private View rootView;
	private ListView mRouteList;
	private TransitReceiver mReceiver;
	private ProgressDialog pd;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = getActivity();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_listview, container, false);
		return rootView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		mRouteList = (ListView) rootView.findViewById(R.id.fragment_list);
	
		mReceiver = new TransitReceiver(new Handler());
		mReceiver.setReceiver(this);
		
		// Start a new service that contacts the server and gets the list of 
		// routes
		Intent intent  = new Intent(context, TransitService.class);
		intent.putExtra(TransitService.TYPE, TransitService.ROUTES);
		intent.putExtra("receiver", mReceiver);
		context.startService(intent);
	}
	
	/**
	 * This gets called when the service finishes
	 */
	@Override
	public void onReceiveResult(int resultCode, Bundle resultData) {
		switch(resultCode) {
			case TransitService.START:
				pd = new ProgressDialog(context);
				pd.setTitle("Getting data from server");
				pd.setMessage("Please wait");
				pd.show();
				break;
			case TransitService.FINISH:
				SearchData profile = SearchData.getInstance();
				String[] routes = profile.getRoutesCol("route_short_name");
				mRouteList.setAdapter(new ArrayAdapter<String>(
							context, android.R.layout.simple_list_item_1, 
							routes));
				mRouteList.setOnItemClickListener(new RouteItemClickListener());
				pd.dismiss();
				break;
			default:
			
		}
	}
	
    /** 
     * The click listener for the ListView of routes 
     */
    private class RouteItemClickListener implements ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			selectItem(position);
		}
    }
    
    /*
     * HELPER FUNCTIONS
     */
    	
    /** 
     * Helper function to replace the current fragment with the time fragment
     */
    private void selectItem(int position) {
		Log.d(MainActivity.TAG, "Item selected: " + String.valueOf(position));
		
		SearchData data = SearchData.getInstance();
		data.setRouteIndex(position);
		
		Fragment fragment = new DirectionFragment();	
		FragmentManager fragmentManager = getFragmentManager();
		FragmentTransaction fragmentTrans = fragmentManager.beginTransaction();
		
		fragmentTrans.replace(R.id.content_frame, fragment);
		fragmentTrans.addToBackStack(null);
		fragmentTrans.commit();
    }
}
