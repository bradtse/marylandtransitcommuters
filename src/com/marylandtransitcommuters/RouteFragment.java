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
public class RouteFragment extends SherlockFragment implements TransitResultReceiver.Receiver {
	private Context context;
	private View rootView;
	private ListView mRouteList;
	private TransitResultReceiver mReceiver;
	private ProgressDialog pd;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = getActivity();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_routes, container, false);
		return rootView;
	}

	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		mRouteList = (ListView) rootView.findViewById(R.id.routes_list);
	
		mReceiver = new TransitResultReceiver(new Handler());
		mReceiver.setReceiver(this);
		
		// Start a new service that contacts the server
		Intent intent  = new Intent(context, TransitService.class);
		intent.putExtra("type", 0);
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
				CurrentSearch profile = CurrentSearch.getInstance();
				mRouteList.setAdapter(new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, 
							profile.getRoutesCol("ShortName")));
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
			selectRoute(id);
		}
    }
    
    /*
     * HELPER FUNCTIONS
     */
    	
    /** 
     * Helper function to replace the current fragment with the time fragment
     */
    private void selectRoute(long id) {
		Log.d(MainActivity.TAG, "Item selected: " + String.valueOf(id));
		
		CurrentSearch profile = CurrentSearch.getInstance();
		profile.setRouteId(String.valueOf(id));
		
		Fragment fragment = new TimeFragment();
		
		FragmentManager fragmentManager = getFragmentManager();
		FragmentTransaction fragmentTrans = fragmentManager.beginTransaction();
		
		fragmentTrans.replace(R.id.content_frame, fragment);
		fragmentTrans.addToBackStack(null);
		fragmentTrans.commit();
    }
}
