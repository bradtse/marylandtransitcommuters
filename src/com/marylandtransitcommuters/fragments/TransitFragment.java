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
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.widget.SearchView;
import com.actionbarsherlock.widget.SearchView.OnQueryTextListener;
import com.marylandtransitcommuters.MainActivity;
import com.marylandtransitcommuters.R;
import com.marylandtransitcommuters.adapters.CustomSimpleAdapter;
import com.marylandtransitcommuters.dataobjects.TransitData;
import com.marylandtransitcommuters.receiver.TransitReceiver;
import com.marylandtransitcommuters.service.TransitService;


/**
 * Parent class for each fragment
 */
public abstract class TransitFragment extends SherlockFragment implements TransitReceiver.Receiver	{
	protected Context context;
	protected View rootView;
	protected ListView mList;
	protected TransitData data;
	protected SearchView search;
	protected CustomSimpleAdapter adapter;
	private boolean alive = false;
	private TransitReceiver mReceiver;
	private ProgressDialog progressDialog;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		Log.d(MainActivity.LOG_TAG, "TransitFragment onCreate()");
		super.onCreate(savedInstanceState);
		
		if (savedInstanceState == null) {
			Log.d(MainActivity.LOG_TAG, "TransitFragment onCreate() savedInstanceState is null");
			context = getActivity();
			data = TransitData.getInstance();
			setupProgressDialog();
		}
	}
	
	/**
	 * Sets up the progress dialog
	 */
	private void setupProgressDialog() {
		progressDialog = new ProgressDialog(context);
		progressDialog.setTitle("Retrieving data from server");
		progressDialog.setMessage("Please wait");
	}
	
	@Override
	public void onStop() {
		Log.d(MainActivity.LOG_TAG, "TransitFragment onStop()");
		super.onStop();
	}
	
	@Override
	public void onDestroyView() {
		Log.d(MainActivity.LOG_TAG, "TransitFragment onDestoryView()");
		super.onDestroyView();
	}
	
	@Override
	public void onDestroy() {
		Log.d(MainActivity.LOG_TAG, "TransitFragment onDestory()");
		super.onDestroy();
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		Log.d(MainActivity.LOG_TAG, "TransitFragment onActivityCreated()");
		super.onActivityCreated(savedInstanceState);
	
		if (savedInstanceState == null) {
			Log.d(MainActivity.LOG_TAG, "TransitFragment onActivityCreated() savedInstanceState is null");
			mList = (ListView) rootView.findViewById(R.id.fragment_list);
			setupReceiver();
			startIntentService();
		} 
	}
	
	/**
	 * Sets up the callback receiver
	 */
	private void setupReceiver() {
		mReceiver = new TransitReceiver(new Handler());
		mReceiver.setReceiver(this);
	}
	
	/**
	 * Starts the new intent service 
	 */
	private void startIntentService() {
		Log.d(MainActivity.LOG_TAG, "Starting new IntentService");
		Intent intent  = new Intent(context, TransitService.class);
		intent.putExtra(TransitReceiver.RECEIVER, mReceiver);
		setIntentServiceType(intent);
		context.startService(intent);
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		// Keeps a reference to the SearchView for subclasses to use
		MenuItem searchItem = menu.findItem(R.id.menu_search);
		search = (SearchView) searchItem.getActionView();
		
		// Sets up the SearchViews filter
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
	}
	
	/**
	 * Add the appropriate service type to the intent 
	 * @param intent the intent to add the service type to
	 */
	public abstract void setIntentServiceType(Intent intent);
	
	@Override
	public void onReceiveResult(int resultCode, Bundle resultData) {
		switch (resultCode) {
			case TransitService.START:
				progressDialog.show();
				break;
			case TransitService.FINISH:
				progressDialog.dismiss();
				setupFragment();
				break;
			default:
				Log.d(MainActivity.LOG_TAG, "onReceiveResult() should never reach default case");
		}
	}
	
	/**
	 * Sets up the fragment after the data comes back
	 */
	private void setupFragment() {
		Log.d(MainActivity.LOG_TAG, "Received data from server... setting up fragment");
		mList.setOnItemClickListener(new ListItemClickListener());
		setAdapter();
		
		// Forces the options menu to be redrawn so I can re-setup the SearchView
		setHasOptionsMenu(true); 
		
		// Adds the result count 
		TextView text = (TextView) rootView.findViewById(R.id.result_count);
		text.setText(String.valueOf(adapter.getCount()) + " results");
	}
	
	/**
	 * Set up the adapter for the ListView
	 */
	public abstract void setAdapter();

    class ListItemClickListener implements ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			Log.d(MainActivity.LOG_TAG, "Item " + position + " was selected...resetting SearchView");
			resetSearchView();
			selectItem(position);
		}
    }
    
    /**
     * Resets the SearchView for the next fragment, which includes removing the
     * text and minimizing the soft keyboard.
     */
    public void resetSearchView() {
    	search.setQuery("", false);
    }
    
    /**
     * Takes appropriate action when item in ListView is selected
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
    	Log.d(MainActivity.LOG_TAG, "Replacing " + currFragTag + " with " + newFragTag);
    	FragmentManager fm = getFragmentManager();
    	Fragment newFrag = fm.findFragmentByTag(newFragTag);
    	Fragment currFrag = fm.findFragmentByTag(currFragTag);
    	
		FragmentTransaction ft = fm.beginTransaction();
		ft.setCustomAnimations(R.animator.slide_in_right, R.animator.slide_out_left, 
							   R.animator.slide_in_left, R.animator.slide_out_right);
				
		ft.hide(currFrag);
		ft.add(R.id.content_frame, newFragment, newFragTag);
	
		
		ft.addToBackStack(null);
		ft.commit();
    }
}
