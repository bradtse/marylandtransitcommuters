package com.marylandtransitcommuters.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.widget.SearchView;
import com.actionbarsherlock.widget.SearchView.OnQueryTextListener;
import com.marylandtransitcommuters.MainActivity;
import com.marylandtransitcommuters.R;
import com.marylandtransitcommuters.TransitApplication;
import com.marylandtransitcommuters.adapters.CustomSimpleAdapter;
import com.marylandtransitcommuters.dataobjects.TransitData;
import com.marylandtransitcommuters.receiver.TransitReceiver;
import com.marylandtransitcommuters.service.TransitService;


/**
 * Parent class for each fragment
 */
public abstract class TransitFragment extends SherlockFragment implements TransitReceiver.Receiver, OnClickListener	{
	protected Context mContext;
	protected View mRootView;
	protected TransitData mTransitData;
	protected SearchView mSearchView;
	protected CustomSimpleAdapter mAdapter;
	protected ReplaceFragmentListener mCallback;
	protected TextView mResults;
	protected ListView mList;
	protected SpanHolder mSpanHolder;
	private TransitReceiver mReceiver;
	private RelativeLayout mProgressLayout;
	private RelativeLayout mRetryLayout;
	public Boolean mClicked;
	
	static class SpanHolder {
		StyleSpan styleSpan;
		RelativeSizeSpan sizeSpan; 
		ForegroundColorSpan colorSpan;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		Log.d(MainActivity.LOG_TAG, "TransitFragment onCreate()");
		super.onCreate(savedInstanceState);
		
		// Initialize components
		mClicked = false;
		mContext = getActivity();
		mTransitData = TransitData.getInstance();
		setupTransitReceiver();

		// Forces onCreateOptionsMenu to be called
		setHasOptionsMenu(true); 

		// Initialize span holder which should help improve getView()'s performance
		mSpanHolder = new SpanHolder();
		mSpanHolder.styleSpan = new StyleSpan(android.graphics.Typeface.BOLD_ITALIC);
		mSpanHolder.sizeSpan = new RelativeSizeSpan(0.8f);
		mSpanHolder.colorSpan = new ForegroundColorSpan(getResources().getColor(R.color.glue_color));

		if (savedInstanceState == null) {
			Log.d(MainActivity.LOG_TAG, "TransitFragment onCreate() savedInstanceState is null");
			startIntentService();
		} 
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		Log.d(MainActivity.LOG_TAG, "TransitFragment onCreateView()");
		mList = (ListView) mRootView.findViewById(R.id.fragment_list);
		mProgressLayout = (RelativeLayout) mRootView.findViewById(R.id.progress);
		mRetryLayout = (RelativeLayout) mRootView.findViewById(R.id.retry_layout);
		mResults = (TextView) mRootView.findViewById(R.id.result_count);
		mRootView.findViewById(R.id.retry_button).setOnClickListener(this);
		setupBreadcrumbs();

		// Start the bus animation. Had to use this workaround for it to work
		// on Android 2.2
		ImageView img = (ImageView) mRootView.findViewById(R.id.progress_bus);
        final AnimationDrawable animation = (AnimationDrawable) img.getBackground();
        img.post(new Runnable() {
        	public void run() {
        		animation.start();
        	}
        });

		if (savedInstanceState != null) {
			Log.d(MainActivity.LOG_TAG, "onCreateView savedInstanceState not null");
			mProgressLayout.setVisibility(View.GONE);
			setupFragment();
		}
		return null;
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		Log.d(MainActivity.LOG_TAG, "Fragment onCreateOptionsMenu()");
		mSearchView = (SearchView) menu.findItem(R.id.menu_search).getActionView();
		mSearchView.setVisibility(View.VISIBLE);
		setSearchViewTextListener();
	}

	// This is the callback that is called when the TransitService completes its work
	@Override
	public void onReceiveResult(int resultCode, Bundle resultData) {
		switch (resultCode) {
			case TransitService.FINISH:
				setupFragment();
				mProgressLayout.setVisibility(View.GONE);
				break;
			case TransitService.FAIL:
				mProgressLayout.setVisibility(View.GONE);
				mRetryLayout.setVisibility(View.VISIBLE);
			default:
				Log.d(MainActivity.LOG_TAG, "onReceiveResult() should never reach default case");
		}
	}
	
	@Override
	public void onClick(View v) {
		switch(v.getId()) {
			case R.id.retry_button:
				startIntentService();
				mProgressLayout.setVisibility(View.VISIBLE);
				mRetryLayout.setVisibility(View.GONE);
				break;
			default:
				break;
		}
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		
		// Ensures that the parent activity implements ReplaceFragmentListener
		try {
			mCallback = (ReplaceFragmentListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString() + " must implement OnDestoryListener");
		}
	}

	/*
	 * Current workaround that removes animations when the fragments are removed
	 * from the backstack. This doesn't work completely because it still shows the 
	 * fragments for a split second.
	 */
	@Override
	public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
		TransitApplication app = (TransitApplication) getActivity().getApplication();
		if (app.isFragmentAnimationsEnabled() == false) {
			Animation a = new Animation() {};
			a.setDuration(0);
			return a;
		}
		return super.onCreateAnimation(transit, enter, nextAnim);
	}
	
	public void enableList() {
		mList.setEnabled(true);
	}
    
    /*
     * Abstract methods/interfaces
     */
    
	/**
	 * Adds the appropriate service type to the intent as an extra. Abstract method. 
	 * @param intent The intent to declare the type for
	 */
	protected abstract void setIntentServiceType(Intent intent);
	
	/**
	 * Sets up the TextViews that shows which items were selected in the previous
	 * fragments
	 */
	protected abstract void setupBreadcrumbs(); 

	/**
	 * Creates and attaches the adapter to the fragment's ListView. The adapter
	 * is created using the data received back from the server.
	 * 
	 * This is an abstract method that must be implemented by subclasses. 
	 */
	public abstract void setListViewAdapter();

    /**
     * Handles what should happen when an item in the ListView is clicked by 
     * the user. In our case, this really means storing info about the item
     * that was selected, and then alerting the MainActivity to start the 
     * next appropriate fragment. This is an abstract method that must be implemented
     * by any subclasses.
     * @param position Index of the item selected in the ListView
     */
    public abstract void selectItem(int position);
	
	/**
	 *  Interface that allows fragments to communicate with the MainActivity
	 */
	public interface ReplaceFragmentListener {
		/**
		 * Callback used by the Fragments to let the MainActivity know it needs
		 * to replace the current fragment in the frame layout
		 * @param newFragTag The new fragment's tag
		 * @param newFrag The new fragment instance
		 * @param addToBackStack Whether or not to add this transaction to the backstack
		 * @param clearFrags Whether or not to clear all of the current fragments
		 */
		public void replaceFragment(String newFragTag, Fragment newFrag, 
								 	boolean addToBackStack, boolean clearFrags, 
								 	boolean animate, boolean commit);
	}
	

    /*
     * Helper methods
     */

	/**
	 * Sets up a callback receiver to be used by the TransitService
	 */
	private void setupTransitReceiver() {
		mReceiver = new TransitReceiver(new Handler());
		mReceiver.setReceiver(this);
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
	 * Sets up the different parts of the fragment after the data has been received
	 * back from the server.
	 */
	public void setupFragment() {
		Log.d(MainActivity.LOG_TAG, "Starting to set up fragment");

		setListViewAdapter();
		setSearchViewTextListener();
		mResults.setText(String.valueOf(mAdapter.getCount()) + " results");
		mList.setOnItemClickListener(new ListItemClickListener());

		Log.d(MainActivity.LOG_TAG, "Finished setting up fragment");
	}

	/**
	 * Attaches the adapter's filter to the Search View to allow filtering of the ListView
	 */
	private void setSearchViewTextListener() {
		if (mSearchView != null && mAdapter != null) {
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
	}

	/**
	 * ListView item click listener
	 */
    public class ListItemClickListener implements ListView.OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
				mList.setEnabled(false);

				// Clears the SearchView text and closes the keyboard
				mSearchView.setQuery("", false);
				mSearchView.clearFocus();
			
				selectItem(position);
		}
    }
    
    /*
     * Fragment Lifecycle debugging
     */

	@Override
	public void onPause() {
		Log.d(MainActivity.LOG_TAG, "TransitFragment onPause()");
		super.onPause();
	}

	@Override
	public void onStop() {
		Log.d(MainActivity.LOG_TAG, "TransitFragment onStop()");
		super.onStop();
	}

	@Override
	public void onDestroy() {
		Log.d(MainActivity.LOG_TAG, "TransitFragment onDestroy()");
		super.onDestroy();
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		Log.d(MainActivity.LOG_TAG, "TransitFragment onSaveInstanceState()");
		super.onSaveInstanceState(outState);
	}
}