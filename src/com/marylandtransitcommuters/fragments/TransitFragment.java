package com.marylandtransitcommuters.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
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
	protected Context mContext;
	protected View mRootView;
	protected ListView mList;
	protected TransitData mData;
	protected SearchView mSearchView;
	protected CustomSimpleAdapter mAdapter;
	protected ReplaceFragmentListener mCallback;
	private TransitReceiver mReceiver;
	private ProgressDialog mProgDialog;
	private RelativeLayout mProgressLayout;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		Log.d(MainActivity.LOG_TAG, "TransitFragment onCreate()");
		super.onCreate(savedInstanceState);
		
		mContext = getActivity();
		mData = TransitData.getInstance();
		setupTransitReceiver();
		setupProgressDialog();

		if (savedInstanceState == null) {
			Log.d(MainActivity.LOG_TAG, "TransitFragment onCreate() savedInstanceState is null");
			startIntentService();
		} 
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		mList = (ListView) mRootView.findViewById(R.id.fragment_list);
		mProgressLayout = (RelativeLayout) mRootView.findViewById(R.id.progress);
		setupBreadcrumbs();

		if (savedInstanceState != null) {
			mProgressLayout.setVisibility(View.GONE);
			setupFragment();
		}

		return null;
	}
	
	/**
	 * Sets up a callback receiver
	 */
	private void setupTransitReceiver() {
		mReceiver = new TransitReceiver(new Handler());
		mReceiver.setReceiver(this);
	}
	
	/**
	 * Sets up a progress dialog
	 */
	private void setupProgressDialog() {
		mProgDialog = new ProgressDialog(mContext);
		mProgDialog.setTitle("Retrieving data from server");
		mProgDialog.setMessage("Please wait");
	}
	
	/**
	 * Starts the new intent service 
	 */
	private void startIntentService() {
		Log.d(MainActivity.LOG_TAG, "Starting new IntentService");
		Intent intent = new Intent(mContext, TransitService.class);
		intent.putExtra(TransitReceiver.RECEIVER, mReceiver);
		setIntentServiceType(intent);
		mContext.startService(intent);
	}
	
	/**
	 * Add the appropriate service type to the intent 
	 * @param intent the intent to add the service type to
	 */
	protected abstract void setIntentServiceType(Intent intent);
	
	/**
	 * Sets up the TextViews that contain the previous fragment's selections
	 */
	protected abstract void setupBreadcrumbs(); 

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		
		// Ensures that the parent activity implements the interface
		try {
			mCallback = (ReplaceFragmentListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString() + " must implement OnDestoryListener");
		}
	}
	
	/**
	 *  Interface to allow fragments to communicate with its Activity
	 */
	public interface ReplaceFragmentListener {
		/**
		 * Callback used by the Fragments to let the MainActivity know it needs
		 * to replace the current fragment
		 * @param currentFragTag
		 * @param newFragTag
		 * @param newFrag
		 * @param addToBackStack
		 */
		public void showFragment(String currentFragTag, String newFragTag, 
								       Fragment newFrag, boolean addToBackStack);
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		// Keeps a reference to the SearchView for subclasses to use
		MenuItem searchItem = menu.findItem(R.id.menu_search);
		mSearchView = (SearchView) searchItem.getActionView();

		// Attaches the adapter's filter to to the SearchView
		mSearchView.setOnQueryTextListener(new OnQueryTextListener() {

			@Override
			public boolean onQueryTextSubmit(String query) {
				return false;
			}

			@Override
			public boolean onQueryTextChange(String newText) {
				mAdapter.getFilter().filter(newText);
				return false;
			}
		});

	}

	// This is the callback that the Transit Service communicates with
	@Override
	public void onReceiveResult(int resultCode, Bundle resultData) {
		switch (resultCode) {
			case TransitService.FINISH:
				mProgressLayout.setVisibility(View.GONE);
				setupFragment();
				break;
			default:
				Log.d(MainActivity.LOG_TAG, "onReceiveResult() should never reach default case");
		}
	}
	
	/**
	 * Sets up the fragment after the data comes back
	 */
	public void setupFragment() {
		// The TransitService has retrieved the data and stored it in our
		// TransitData singleton, so we can use that data to create the adapter
		setAdapter();
		
		// Forces onCreateOptionsMenu to be called in each fragment
		setHasOptionsMenu(true); 

		// Adds the result count 
		TextView text = (TextView) mRootView.findViewById(R.id.result_count);
		text.setText(String.valueOf(mAdapter.getCount()) + " results");

		// Attach the ListView click listener
		mList.setOnItemClickListener(new ListItemClickListener());
	}
	
	/**
	 * Set up the adapter for the ListView from the data in our TransitData singleton
	 */
	public abstract void setAdapter();

	/**
	 * Click listener for the ListView
	 */
    class ListItemClickListener implements ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// Clears the SearchView text
	    	mSearchView.setQuery("", false);
	    	mSearchView.clearFocus();
			selectItem(position);
		}
    }
    
    /**
     * Takes appropriate action when item in ListView is selected
     * @param position index of item selected
     */
    public abstract void selectItem(int position);
}
