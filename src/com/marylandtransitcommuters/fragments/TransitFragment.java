package com.marylandtransitcommuters.fragments;

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
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.widget.SearchView;
import com.marylandtransitcommuters.MainActivity;
import com.marylandtransitcommuters.R;
import com.marylandtransitcommuters.receiver.TransitReceiver;
import com.marylandtransitcommuters.service.TransitService;

import dataobjects.TransitData;

/**
 * Parent class for each fragment
 */
public abstract class TransitFragment extends SherlockFragment implements TransitReceiver.Receiver	{
	protected Context context;
	protected View rootView;
	protected ListView mList;
	protected TransitData data;
	protected SearchView search;
	private boolean alive = false;
	private TransitReceiver mReceiver;
	private ProgressDialog progressDialog;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		if (savedInstanceState == null) {
			context = getActivity();
			data = TransitData.getInstance();
			setupProgressDialog();
			setHasOptionsMenu(true); // Force redraw of action bar menu
//		}
	}
	
	private void setupProgressDialog() {
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
		
//		if (savedInstanceState == null) {
			mList = (ListView) rootView.findViewById(R.id.fragment_list);
			setupReceiver();
			startIntentService();
			
			alive = true;
//		} 
	}
	
	private void setupReceiver() {
		mReceiver = new TransitReceiver(new Handler());
		mReceiver.setReceiver(this);
	}
	
	private void startIntentService() {
		Intent intent  = new Intent(context, TransitService.class);
		intent.putExtra(TransitReceiver.RECEIVER, mReceiver);
		setServiceType(intent);
		context.startService(intent);
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		MenuItem searchItem = menu.findItem(R.id.menu_search);
		search = (SearchView) searchItem.getActionView();
		
		search.setIconifiedByDefault(false);
	}
	/**
	 * Add the appropriate service type to the intent 
	 * @param intent the intent to add the service type to
	 */
	public abstract void setServiceType(Intent intent);
	
	@Override
	public void onReceiveResult(int resultCode, Bundle resultData) {
		switch (resultCode) {
			case TransitService.START:
				progressDialog.show();
				break;
			case TransitService.FINISH:
				mList.setOnItemClickListener(new ListItemClickListener());
				setAdapter();
				progressDialog.dismiss();
				break;
			default:
				Log.d(MainActivity.LOG_TAG, "onReceiveResult() should never reach default case");
		}
	}
	
	/**
	 * Set up the ListView
	 */
	public abstract void setAdapter();

    class ListItemClickListener implements ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			selectItem(position);
		}
    }
    
    /**
     * Does something when item in ListView is selected
     * @param position index of item selected
     */
    public abstract void selectItem(int position);
    
    /**
     * Replaces the current fragment with the new one provided
     * Uses hide() and add() instead of replace() in order to maintain state of the
     * fragment that is replaced.
     * @param newFragment the new fragment that will replace the current one
     * @param currFragTag the tag of the current fragment
     * @param newFragTag the tag of the new fragment
     * FIXME I think this is causing a memory leak
     */
    public void replaceFragment(Fragment newFragment, String currFragTag, String newFragTag) {
    	FragmentManager fm = getFragmentManager();
    	Fragment newFrag = fm.findFragmentByTag(newFragTag);
    	Fragment currFrag = fm.findFragmentByTag(currFragTag);
    	
		FragmentTransaction ft = fm.beginTransaction();
		ft.setCustomAnimations(R.animator.slide_in_right, R.animator.slide_out_left, 
							   R.animator.slide_in_left, R.animator.slide_out_right);
		
		if (newFrag != null) {
			ft.remove(newFrag);
		} 
		
		ft.hide(currFrag);
		ft.add(R.id.content_frame, newFragment, newFragTag);
		
		ft.addToBackStack(null);
		ft.commit();
    }
}
