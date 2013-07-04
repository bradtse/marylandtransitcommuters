package com.marylandtransitcommuters;

import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockFragment;

public class StopFragment extends SherlockFragment implements TransitResultReceiver.Receiver{
	private View rootView;
	private Context context;
	private ListView mStopList;
	private Cursor mCursor;
	
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
		
		mStopList = (ListView) rootView.findViewById(R.id.routes_list);
		
		Log.d(MainActivity.TAG, "Initialize stop list");

		
	}
	
	@Override
	public void onReceiveResult(int resultCode, Bundle resultData) {
//		switch(resultCode) {
//			case TransitService.START:
//				pd = new ProgressDialog(context);
//				pd.setTitle("Getting data from server");
//				pd.setMessage("Please wait");
//				pd.show();
//				break;
//			case TransitService.FINISH:
//				
//				break;
//			default:
//
//		}
	}
	
	/** 
     * The click listener for the ListView of routes 
     */
    private class StopItemClickListener implements ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			return;
		}
    }
}
