package com.marylandtransitcommuters.fragments;

import android.app.Activity;
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
	protected boolean mVisible;
	protected ReplaceFragmentListener mCallback;
	private TransitReceiver mReceiver;
	private ProgressDialog mProgDialog;
	
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
		setupBreadcrumbs();
		return null;
	}
	
	/**
	 * Sets up the callback receiver
	 */
	private void setupTransitReceiver() {
		mReceiver = new TransitReceiver(new Handler());
		mReceiver.setReceiver(this);
	}
	
	/**
	 * Sets up the progress dialog
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
		Intent intent  = new Intent(mContext, TransitService.class);
		intent.putExtra(TransitReceiver.RECEIVER, mReceiver);
		setIntentServiceType(intent);
		mContext.startService(intent);
	}
	
	/**
	 * Helper function to hide a fragment
	 * @param fragTag The fragment tag to hide
	 */
	protected void hideFragment(String fragTag) {
		FragmentManager fm = getFragmentManager();
    	Fragment currFrag = fm.findFragmentByTag(fragTag);
		FragmentTransaction ft = fm.beginTransaction();
		ft.setCustomAnimations(R.animator.slide_in_right, R.animator.slide_out_left, 
				   R.animator.slide_in_left, R.animator.slide_out_right);
		ft.hide(currFrag);
		ft.commit();
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
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putBoolean("mAlive", mVisible);
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		
		// Ensures that the activity implements the interface
		try {
			mCallback = (ReplaceFragmentListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString() + " must implement OnDestoryListener");
		}
	}
	
	public interface ReplaceFragmentListener {
		public void performTransaction(String currentFragTag, String newFragTag, Fragment newFrag, boolean addToBackStack);
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		// Keeps a reference to the SearchView for subclasses to use
		MenuItem searchItem = menu.findItem(R.id.menu_search);
		mSearchView = (SearchView) searchItem.getActionView();

		// Sets up the SearchViews filter
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

	@Override
	public void onReceiveResult(int resultCode, Bundle resultData) {
		switch (resultCode) {
			case TransitService.START:
				mProgDialog.show();
				break;
			case TransitService.FINISH:
				mProgDialog.dismiss();
				setupFragment();
				break;
			default:
				Log.d(MainActivity.LOG_TAG, "onReceiveResult() should never reach default case");
		}
	}
	
	/**
	 * Sets up the fragment after the data comes back
	 */
	protected void setupFragment() {
		Log.d(MainActivity.LOG_TAG, "Setting up fragment");
		mList.setOnItemClickListener(new ListItemClickListener());
		setAdapter();
		
		setHasOptionsMenu(true); 

		// Adds the result count 
		TextView text = (TextView) mRootView.findViewById(R.id.result_count);
		text.setText(String.valueOf(mAdapter.getCount()) + " results");
	}
	
	/**
	 * Set up the adapter for the ListView
	 */
	public abstract void setAdapter();

	/**
	 * Click listener for the ListView
	 */
    class ListItemClickListener implements ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			Log.d(MainActivity.LOG_TAG, "Item " + position + " was selected...");
			// Clears the SearchView text
	    	mSearchView.setQuery("", false);
			selectItem(position);
		}
    }
    
    /**
     * Takes appropriate action when item in ListView is selected
     * @param position index of item selected
     */
    public abstract void selectItem(int position);
   
	/**
	 * Fragment is visible
	 */
	public void setVisible() {
		mVisible = true;
	}
	
	/**
	 * Fragment is not visible
	 */
	public void setInvisible() {
		mVisible = false;
	}
}
