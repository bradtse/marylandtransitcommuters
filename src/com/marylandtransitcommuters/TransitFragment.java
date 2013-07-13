package com.marylandtransitcommuters;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockFragment;

public abstract class TransitFragment extends SherlockFragment implements TransitReceiver.Receiver	{
	private Context context;
	private View rootView;
	private ListView mList;
	private TransitReceiver mReceiver;
	private ProgressDialog pd;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		context = getActivity();
		
		// Set up the progress dialog
		pd = new ProgressDialog(context);
		pd.setTitle("Retrieving data from server");
		pd.setMessage("Please wait");
		
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
		mList = (ListView) rootView.findViewById(R.id.fragment_list);
	
		mReceiver = new TransitReceiver(new Handler());
		mReceiver.setReceiver(this);
		
		// Start a new service that contacts the server and gets the list of 
		// routes
		Intent intent  = new Intent(context, TransitService.class);
		intent.putExtra(TransitService.TYPE, TransitService.ROUTES);
		intent.putExtra("receiver", mReceiver);
		context.startService(intent);
	}
	
	@Override
	public void onReceiveResult(int resultCode, Bundle resultData) {
		// TODO Auto-generated method stub
		
	}

}
