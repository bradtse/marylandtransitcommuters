package com.marylandtransitcommuters;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockFragment;

public abstract class TransitFragment extends SherlockFragment implements TransitReceiver.Receiver	{
	Context context;
	View rootView;
	ListView mList;
	SearchData profile;
	private TransitReceiver mReceiver;
	private ProgressDialog progressDialog;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		context = getActivity();
		profile = SearchData.getInstance();
		
		// Set up the progress dialog
		progressDialog = new ProgressDialog(context);
		progressDialog.setTitle("Retrieving data from server");
		progressDialog.setMessage("Please wait");
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
		mList = (ListView) rootView.findViewById(R.id.fragment_list);

		mReceiver = new TransitReceiver(new Handler());
		mReceiver.setReceiver(this);
		
		Intent intent  = new Intent(context, TransitService.class);
		intent.putExtra("receiver", mReceiver);
		setServiceType(intent);
		context.startService(intent);
	}
	
	/**
	 * Add the appropriate service type to the intent 
	 * @param intent the intent to add the service type to
	 */
	public abstract void setServiceType(Intent intent);
	
	@Override
	public void onReceiveResult(int resultCode, Bundle resultData) {
		switch(resultCode) {
			case TransitService.START:
				progressDialog.show();
				break;
			case TransitService.FINISH:
				setAdapter();
				progressDialog.dismiss();
				break;
			default:
		}
	}
	
	/**
	 * Set up the ListView
	 */
	public abstract void setAdapter();

    class ItemClickListener implements ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			selectItem(position);
		}
    }
    
    /**
     * Action to take when item in list is selected
     * @param position index of item selected
     */
    public abstract void selectItem(int position);
    
    public void replaceFragment(Fragment fragment) {
    	FragmentManager fragmentManager = getFragmentManager();
		FragmentTransaction fragmentTrans = fragmentManager.beginTransaction();
		fragmentTrans.replace(R.id.content_frame, fragment);
		fragmentTrans.addToBackStack(null);
		fragmentTrans.commit();
    }
}
