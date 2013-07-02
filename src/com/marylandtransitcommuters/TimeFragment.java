package com.marylandtransitcommuters;

import com.actionbarsherlock.app.SherlockFragment;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

/**
 * The fragment that allows the user to select whether they want AM (inbound) or
 * PM (outbound) times to be shown.
 */
public class TimeFragment extends SherlockFragment implements OnClickListener {
	private Context context;
	private View rootView;
	private Cursor mCursor;
	private int time; // AM = 0, PM = 1
	private String routeId;
	
	public TimeFragment() {}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = getActivity();
		
		// The column we want to query for
		String[] mProjection = {TransitContract.Routes.KEY_ROUTE_ID};

		// Append the _ID to the base uri
		String id = getArguments().getString(TransitContract.Routes._ID);
		Uri data = Uri.withAppendedPath(TransitContract.Routes.CONTENT_URI, id);
		
		mCursor = context.getContentResolver().query(
			data,
			mProjection,
			null,
			null,
			null
		);
		
		if (mCursor == null) {
			Log.d(MainActivity.TAG, "Row not found");
		} else {
			mCursor.moveToFirst();
			int index = mCursor.getColumnIndex(TransitContract.Routes.KEY_ROUTE_ID);	
			routeId = mCursor.getString(index);
			Toast.makeText(context, routeId, Toast.LENGTH_SHORT).show();
		}
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_time, container, false);
		
		/* Allows the fragment to handle the button clicks instead of the main activity */
		Button am = (Button) rootView.findViewById(R.id.am_button);
		am.setOnClickListener(this);
		Button pm = (Button) rootView.findViewById(R.id.pm_button);
		pm.setOnClickListener(this);
		
		return rootView;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	/* 
	 * Handles the click event on the AM and PM buttons
	 */
	@Override
	public void onClick(View view) {
		switch(view.getId()) {
			case R.id.am_button:
				time = 0;
				break;
			case R.id.pm_button:
				time = 1;
				break;
			default:
				throw new IllegalArgumentException("Should only be called via the buttons");
		}
		
		/* 
		 * Starts the stops fragment
		 */
		
	}
}
