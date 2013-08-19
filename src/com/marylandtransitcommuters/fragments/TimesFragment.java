package com.marylandtransitcommuters.fragments;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.widget.SearchView;
import com.marylandtransitcommuters.MainActivity;
import com.marylandtransitcommuters.R;
import com.marylandtransitcommuters.adapters.CustomSimpleAdapter;
import com.marylandtransitcommuters.database.TransitContract.Favorites;
import com.marylandtransitcommuters.dataobjects.Time;
import com.marylandtransitcommuters.service.TransitService;

public class TimesFragment extends TransitFragment {
	public static final String TAG = "times";
	private static final int OFF = 0;
	private static final int ON = 1;
	private static final String mSelection = Favorites.KEY_ROUTE_ID + " = ?"
  										  + " AND "
										  + Favorites.KEY_DIRECTION_ID + " = ?"
										  + " AND "
										  + Favorites.KEY_START_STOP_ID + " = ?"
										  + " AND "
										  + Favorites.KEY_FINAL_STOP_ID + " = ?";
	private String[] mSelectionArgs;
	private boolean mIsFavorite = false;
	private MenuItem mFavoritesIcon;
	
	@Override
	public void onCreate(Bundle savedBundleInstanceState) {
		super.onCreate(savedBundleInstanceState);

		String[] selectionArgs = {mTransitData.getRouteId(),
							      mTransitData.getDirectionId(),
								  mTransitData.getStartStopId(),
								  mTransitData.getFinalStopId()};
		mSelectionArgs = selectionArgs;
		
		new QueryFavoritesTable().execute();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		Log.d(MainActivity.LOG_TAG, "TimesFragment onCreateView()");
		mRootView = inflater.inflate(R.layout.fragment_layout_times, container, false);
		super.onCreateView(inflater, container, savedInstanceState);
		return mRootView;
	}
	
	@Override
	protected void setupBreadcrumbs() {
		String route = mTransitData.getRouteShortName() + " " + mTransitData.getRouteLongName();
		String direction = mTransitData.getDirectionHeadsign();
		String startStop = mTransitData.getStartStopName();
		String finalStop = mTransitData.getFinalStopName();
		
		TextView routeText = (TextView) mRootView.findViewById(R.id.info_route_data);
		TextView dirText = (TextView) mRootView.findViewById(R.id.info_direction_data);
		TextView startText = (TextView) mRootView.findViewById(R.id.info_start_stop_data);
		TextView finalText = (TextView) mRootView.findViewById(R.id.info_final_stop_data);

		routeText.setText(route);
		dirText.setText(direction);
		startText.setText(startStop);
		finalText.setText(finalStop);
	}
	
	@Override
	public void setIntentServiceType(Intent intent) {
		intent.putExtra(TransitService.DataType.KEY, TransitService.DataType.TIMES);
	}
	
	@Override
	public void setListViewAdapter() {
		mAdapter = new CustomSimpleAdapter(
					mContext, 
					mTransitData.getTimesList(),
					R.layout.fragment_times_listview_row,
					new String[] {Time.ARRIVAL_TIME},
					new int[] {R.id.time_list_item}
					)
		{

			// Disable clicking for this ListView
			@Override
			public boolean isEnabled(int position) {
				return false;
			}
		};
		
		mList.setAdapter(mAdapter);
		mList.setEmptyView((TextView) mRootView.findViewById(R.id.empty));
	}
	
	private void setFavoritesState(int state) {
		switch(state) {
			case OFF:
				mIsFavorite = false;
				if (mFavoritesIcon != null) {
					mFavoritesIcon.setIcon(R.drawable.ic_action_star);
				}
				break;
			case ON:
				mIsFavorite = true;
				if (mFavoritesIcon != null) {
					mFavoritesIcon.setIcon(R.drawable.ic_action_star_gold);
				}
				break;
			default: 
				throw new IllegalArgumentException("Unknown favorites state");
		}
	}

	// Clicking is disabled for this fragment's ListView
	@Override
	public void selectItem(int position) {}

	@Override 
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		Log.d(MainActivity.LOG_TAG, "onCreateOptionsMenu()");
		inflater.inflate(R.menu.time, menu);
		MenuItem searchItem = menu.findItem(R.id.menu_search);
		mFavoritesIcon = menu.findItem(R.id.favorite);

		// Hide the SearchView
		mSearchView = (SearchView) searchItem.getActionView();    
		mSearchView.setVisibility(View.GONE);

		if (mIsFavorite == true) {
			setFavoritesState(ON);
		}
	}
	
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	switch(item.getItemId()) {
    		case R.id.favorite:
    			toggleFavorite();
	    		return true;
    		case R.id.refresh:
    			setListViewAdapter();
	    		Toast.makeText(mContext, "Refreshed!", Toast.LENGTH_SHORT).show();
	    		return true;
	    	default:
	    		return super.onOptionsItemSelected(item);
    	}
    }
	
	private void toggleFavorite() {
		if (mIsFavorite == false) {
			Toast.makeText(mContext, "Added to favorites!", Toast.LENGTH_SHORT).show();
			new InsertFavoritesTableRow().execute();
		} else {
			Toast.makeText(mContext, "Removed from faovrites!", Toast.LENGTH_SHORT).show();
			new DeleteFavoritesTableRow().execute();
		}
	}
	
	
	/*
	 * Asynctask that queries the favorites table
	 */
	private class QueryFavoritesTable extends AsyncTask<Void , Integer, Boolean> {

		@Override
		protected Boolean doInBackground(Void...v) {
			Cursor cursor = mContext.getContentResolver().query(
										Favorites.CONTENT_URI, 
										new String[] {Favorites._ID}, 
										mSelection, 
										mSelectionArgs,
										Favorites.DEFAULT_SORT_ORDER);
			
			return !cursor.moveToFirst() ? false : true;
		}
		
		@Override
		protected void onPostExecute(Boolean isFavorite) {
			if (isFavorite) {
				setFavoritesState(ON);
			} else {
				setFavoritesState(OFF);
			}
		}
	}

	/*
	 * Asynctask that inserts into the favorites table
	 */
	private class InsertFavoritesTableRow extends AsyncTask<Void , Integer, Void> {

		@Override
		protected Void doInBackground(Void...v) {
			ContentValues cv = new ContentValues();
			
			cv.put(Favorites.KEY_ROUTE_ID, mTransitData.getRouteId());
			cv.put(Favorites.KEY_ROUTE_SHORT_NAME, mTransitData.getRouteShortName());
			cv.put(Favorites.KEY_ROUTE_LONG_NAME, mTransitData.getRouteLongName());
			cv.put(Favorites.KEY_DIRECTION_ID, mTransitData.getDirectionId());
			cv.put(Favorites.KEY_DIRECTION_HEADSIGN, mTransitData.getDirectionHeadsign());
			cv.put(Favorites.KEY_START_STOP_ID, mTransitData.getStartStopId());
			cv.put(Favorites.KEY_START_STOP_NAME, mTransitData.getStartStopName());
			cv.put(Favorites.KEY_START_STOP_SEQ, mTransitData.getStartStopSeq());
			cv.put(Favorites.KEY_FINAL_STOP_ID, mTransitData.getFinalStopId());
			cv.put(Favorites.KEY_FINAL_STOP_NAME, mTransitData.getFinalStopName());
	
			mContext.getContentResolver().insert(Favorites.CONTENT_URI, cv);

			return null;
		}
		
		@Override
		protected void onPostExecute(Void v) {
			setFavoritesState(ON);
		}
	}

	/*
	 * Asynctask that deletes a row from the favorites table
	 */
	private class DeleteFavoritesTableRow extends AsyncTask<Void , Integer, Void> {

		@Override
		protected Void doInBackground(Void...v) {
			mContext.getContentResolver().delete(Favorites.CONTENT_URI, mSelection, mSelectionArgs);
			return null;
		}
		
		@Override
		protected void onPostExecute(Void v) {
			setFavoritesState(OFF);
		}
	}
}