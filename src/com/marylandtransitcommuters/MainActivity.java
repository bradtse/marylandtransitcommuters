package com.marylandtransitcommuters;

import java.util.ArrayDeque;
import java.util.Iterator;

import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.widget.SearchView;
import com.commonsware.cwac.merge.MergeAdapter;
import com.marylandtransitcommuters.database.TransitContract.Favorites;
import com.marylandtransitcommuters.dataobjects.TransitData;
import com.marylandtransitcommuters.fragments.RoutesFragment;
import com.marylandtransitcommuters.fragments.TimesFragment;
import com.marylandtransitcommuters.fragments.TransitFragment;
import com.marylandtransitcommuters.fragments.TransitFragment.ReplaceFragmentListener;

/**
 * The main activity
 */
public class MainActivity extends SherlockFragmentActivity implements ReplaceFragmentListener, LoaderCallbacks<Cursor> {
	public static final String LOG_TAG = "BRAD";

	private CharSequence mTitle;
	private CharSequence mDrawerTitle;
	private String[] mDrawerItems;
	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;
	private ArrayDeque<String> mFragTags;
	private String mCurrFragTag;
	private SimpleCursorAdapter mCursorAdapter;
	private boolean mCommitDelayed;
	private FragmentTransaction ft;
	private ImageView mFrameImage;
		
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	Log.d(LOG_TAG, "MainActivity onCreate()");
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Stores a bunch of resources that we'll need later
        mTitle = mDrawerTitle = getTitle();
        mDrawerItems = getResources().getStringArray(R.array.items);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        mFrameImage = (ImageView) findViewById(R.id.frame_layout_image);
        
        setupNavigationDrawer();   
        getSupportLoaderManager().initLoader(0, null, this);

        if (savedInstanceState == null) {
        	mFragTags = new ArrayDeque<String>();
        }
    }
    
    /**
     * Takes care of setting up all of the elements needed for a properly functioning
     * navigation drawer
     */
    private void setupNavigationDrawer() {
    	// Add drawer shadow
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        // Attach the adapter to the drawer ListView
        mDrawerList.setAdapter(setupMergeAdapter());
        // Attach a list item click listener
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
        
        // Enable Action Bar app icon to toggle nav drawer
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
               
        // Add a listener for when the drawer is toggled
        mDrawerToggle = getActionBarDrawerToggle();
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        
//        mDrawerList.performItemClick(mDrawerList, 2, mDrawerList.getItemIdAtPosition(2));
        // Start with the drawer open
        mDrawerLayout.openDrawer(Gravity.LEFT);
    }
    
    /*
     * Sets up the merge adapter that is used to populate the navigation drawer's
     * ListView
     */
    private MergeAdapter setupMergeAdapter() {
    	MergeAdapter m = new MergeAdapter();
    	
    	m.addView(createHeader("CATEGORY"));
    	m.addView(createDivider());

    	ArrayAdapter<String> a = new ArrayAdapter<String>(this, 
    													  R.layout.drawer_categories_row,
    												      R.id.item_text,
    												      mDrawerItems);
    	
    	m.addAdapter(a);
    	
    	m.addView(createHeader("FAVORITES"));
    	m.addView(createDivider());
    	
    	mCursorAdapter = new SimpleCursorAdapter(this, 
												R.layout.drawer_favorites_row,
												null,
												new String[] {Favorites.KEY_ROUTE_SHORT_NAME,
    														  Favorites.KEY_DIRECTION_HEADSIGN,
    														  Favorites.KEY_START_STOP_NAME,
    														  Favorites.KEY_FINAL_STOP_NAME},
												new int[] {R.id.route_name,
    													   R.id.direction_name,
    													   R.id.start_stop_name,
    													   R.id.final_stop_name},
												0);

    	m.addAdapter(mCursorAdapter);

    	return m;
    }
    
    private TextView createDivider() {
    	TextView tv = (TextView) getLayoutInflater().inflate(R.layout.drawer_divider, null);
    	return tv;
    }
    
    private TextView createHeader(String s) {
		TextView tv = (TextView) getLayoutInflater().inflate(R.layout.drawer_header, null);
    	tv.setText(s);
    	return tv;
    }
    
    @Override
    public void replaceFragment(String newFragTag, Fragment newFrag, 
    							boolean addToBackStack, boolean clearFrags, 
    							boolean animate, boolean commit) {
    	// Removes all of the existing fragments
		if (clearFrags == true && mCurrFragTag != null) {
			removeAllFragments();
    	}

    	FragmentManager fm = getSupportFragmentManager();
    	ft = fm.beginTransaction();
    	
    	if (animate == true) {
	    	ft.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, 
	    						   R.anim.slide_in_left, R.anim.slide_out_right);
//	    	ft.setCustomAnimations(R.anim.fade_in, R.anim.fade_out, 
//	    						   R.anim.fade_in, R.anim.fade_out);
    	}

    	// If there is a current fragment in the frame layout then hide it
    	if (mCurrFragTag != null) {
    		mFragTags.push(mCurrFragTag);
			TransitFragment currentFragment = (TransitFragment) fm.findFragmentByTag(mCurrFragTag);
    		ft.hide(currentFragment);
    	}
    	
    	// Add the new fragment to the fragment frame layout 
		ft.add(R.id.content_frame, newFrag, newFragTag);
		
		if (addToBackStack == true) {
			ft.addToBackStack(null);
		}

		if (commit == true) {
			ft.commit();
		} else {
			mCommitDelayed = true;
		}
    	mCurrFragTag = newFragTag;
    }
    
    /*
     * Removes all of the current fragments in the frame layout
     */
    private void removeAllFragments() {
    	FragmentManager fm = getSupportFragmentManager();
    	fm.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
    	FragmentTransaction ft = fm.beginTransaction();
    	
    	// Remove the fragment that is currently showing
    	TransitFragment fragment = (TransitFragment) fm.findFragmentByTag(mCurrFragTag);
    	ft.remove(fragment);
    	
    	// Remove all fragments on the back stack
        while (mFragTags.size() != 0) {
        	fragment = (TransitFragment) fm.findFragmentByTag(mFragTags.pop());
        	ft.remove(fragment);
        }

        ft.commit();
        mCurrFragTag = null;
        
        if (mCommitDelayed == false) {
	        mFrameImage.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onBackPressed() {
    	if (mFragTags.size() > 0) {
    		mCurrFragTag = mFragTags.pop();
    	}
    	super.onBackPressed();
    }

    @Override
    protected void onRestoreInstanceState(Bundle inState) {
    	Log.d(MainActivity.LOG_TAG, "MainActivity onRestoreInstanceState()");
    	super.onRestoreInstanceState(inState);
    	// Restores my stack of fragment tags
    	mFragTags = (ArrayDeque<String>) inState.getSerializable("frags");
    	mCurrFragTag = (String) inState.getSerializable("currfrag");
    	
    	if (mCurrFragTag != null) {
    		mFrameImage.setVisibility(View.GONE);
    	}

    	hideFragmentsOnBackStack();
    }
    
	/* 
	 * Hides each fragment on the back stack since for some reason they
	 * automatically unhide themselves
	 */
    private void hideFragmentsOnBackStack() {
    	FragmentManager fm = getSupportFragmentManager();
    	FragmentTransaction ft = fm.beginTransaction();
        Iterator<String> it = mFragTags.iterator();
        while (it.hasNext()) {
        	TransitFragment fragment = (TransitFragment) fm.findFragmentByTag(it.next());
        	ft.hide(fragment);
        }
        ft.commit();
    }
    
    @Override
    protected void onSaveInstanceState(Bundle outState) {
    	Log.d(MainActivity.LOG_TAG, "MainActivity onSaveInstanceState()");
    	super.onSaveInstanceState(outState);
    	// Keep track of the stack of fragment tags
    	outState.putSerializable("frags", mFragTags);
    	outState.putSerializable("currfrag", mCurrFragTag);
    }
   
    /**
     * The click listener for the ListView in the nav drawer 
     */
    private class DrawerItemClickListener implements ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			selectItem(position);
		}		
    }
    
    /**
     * Handles when nav drawer item is selected
     */
    private void selectItem(int position) {
    	
    	if (isBetween(position, 2, 1 + mDrawerItems.length)){
	    	position = position - 2;
	    	// Then set the action bar title to the item that was selected
	    	setTitle(mDrawerItems[position]);
	     	if (position == 0) {
	        	replaceFragment(RoutesFragment.TAG, new RoutesFragment(), false, true, true, false);
	    	} else if (position == 1) {
	    		
	    	}    	
    	} else if (isBetween(position, 4 + mDrawerItems.length, 3 + mDrawerItems.length + mCursorAdapter.getCount())) {
	    	position = position - 4 - mDrawerItems.length;
	    	Cursor c = mCursorAdapter.getCursor();
	    	Log.d(LOG_TAG, "Cursor size: " + c.getCount());
	    	c.moveToPosition(position);

	    	String routeId = c.getString(c.getColumnIndex(Favorites.KEY_ROUTE_ID));
	    	String routeShortName = c.getString(c.getColumnIndex(Favorites.KEY_ROUTE_SHORT_NAME));
	    	String routeLongName = c.getString(c.getColumnIndex(Favorites.KEY_ROUTE_LONG_NAME));
	    	String directionId = c.getString(c.getColumnIndex(Favorites.KEY_DIRECTION_ID));
	    	String directionHeadsign = c.getString(c.getColumnIndex(Favorites.KEY_DIRECTION_HEADSIGN));
	    	String startStopId = c.getString(c.getColumnIndex(Favorites.KEY_START_STOP_ID));
	    	String startStopName = c.getString(c.getColumnIndex(Favorites.KEY_START_STOP_NAME));
	    	String startStopSeq = c.getString(c.getColumnIndex(Favorites.KEY_START_STOP_SEQ));
	    	String finalStopId = c.getString(c.getColumnIndex(Favorites.KEY_FINAL_STOP_ID));
	    	String finalStopName = c.getString(c.getColumnIndex(Favorites.KEY_FINAL_STOP_NAME));

	    	TransitData data = TransitData.getInstance();
	    	data.selectRoute(routeId, routeShortName, routeLongName);
	    	data.selectDirection(directionId, directionHeadsign);
	    	data.selectStartStop(startStopId, startStopName, startStopSeq);
	    	data.selectFinalStop(finalStopId, finalStopName);
	    	
	    	replaceFragment(TimesFragment.TAG, new TimesFragment(), false, true, true, false);
    	}
    	
    	// Indicates the item has been selected
//    	mDrawerList.setItemChecked(position, true);

    	mDrawerLayout.closeDrawer(mDrawerList);
    }
    
    private boolean isBetween(int x, int lower, int upper) {
    	return lower <= x && x <= upper;
    }
    
    /**
     * Returns a new ActionBarDrawerToggle that ties together the proper 
     * interactions between the sliding drawer and the action bar app icon
     * @return A new ActionBarDrawerToggle object
     */
    private ActionBarDrawerToggle getActionBarDrawerToggle() {
    	return new ActionBarDrawerToggle(this, mDrawerLayout, R.drawable.ic_drawer_white,	
    									R.string.drawer_open, R.string.drawer_close) {
        	public void onDrawerClosed(View view) {
        		getSupportActionBar().setTitle(mTitle);
        		supportInvalidateOptionsMenu(); // Redraw options menu
        		if (mCommitDelayed == true) {
        			mFrameImage.setVisibility(View.GONE);
        			ft.commit();
        			getSupportFragmentManager().executePendingTransactions();
        			mCommitDelayed = false;
        		}
        	}
        	
        	public void onDrawerOpened(View drawerView) {
        		getSupportActionBar().setTitle(mDrawerTitle);
        		supportInvalidateOptionsMenu(); 
        	}
        };
    }
    
    @Override
    public void setTitle(CharSequence title) {
    	mTitle = title;
    	getSupportActionBar().setTitle(mTitle);
    }

 
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
    	super.onPostCreate(savedInstanceState);
    	// Sync the toggle state after onRestoreInstanceState has occurred
    	mDrawerToggle.syncState();	
    }
    
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
    	super.onConfigurationChanged(newConfig);
    	mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getSupportMenuInflater().inflate(R.menu.main, menu);
       
        // Get the SearchView menu item
		MenuItem searchItem = menu.findItem(R.id.menu_search);
		SearchView search = (SearchView) searchItem.getActionView();
		
		// Make the text for the SearchView white instead of the default grey
		AutoCompleteTextView searchText = (AutoCompleteTextView) search.findViewById(R.id.abs__search_src_text);
		searchText.setHintTextColor(Color.WHITE);
		searchText.setTextColor(Color.WHITE);
		
        return super.onCreateOptionsMenu(menu);
    }
    
    /* 
     * Callback function when invalidateOptionsMenu() is called 
     */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
    	// Hides the SearchView when the drawer is open
    	boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
    	menu.findItem(R.id.menu_search).setVisible(!drawerOpen);
    	return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	// Let ActionBarDrawerToggle attempt to handle home/up action
//    	if (mDrawerToggle.onOptionsItemSelected(item)) {
//    		return true;
//    	}
    	switch(item.getItemId()) {
    		// A workaround until ABS implements onOptionsItemSelected 
	    	case android.R.id.home:
	    		if (mDrawerLayout.isDrawerOpen(mDrawerList)) {
	    			mDrawerLayout.closeDrawer(mDrawerList);
	    		} else {
	    			mDrawerLayout.openDrawer(mDrawerList);
	    		}
	    		return true;
	    	case R.id.action_settings:
	    		Toast.makeText(this, "Yay settings!", Toast.LENGTH_SHORT).show();
	    		return true;
	    	case R.id.email_developer:
	    		sendEmail();
	    		return true;
	    	default:
	    		return super.onOptionsItemSelected(item);
    	}
    }
    
    /*
     * Helper class to send an email to the developer
     */
    private void sendEmail() {
		Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
//		emailIntent.setType("message/rfc822");
		emailIntent.setData(Uri.parse("mailto:" + "bradleytse@gmail.com"));
		emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[] {"bradleytse@gmail.com"});
		emailIntent.putExtra(Intent.EXTRA_SUBJECT, "MTA Commute");
		startActivity(Intent.createChooser(emailIntent, "Send email via:"));
    }

    /*
     * LoaderCallback methods
     */
    
 	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
 		Log.d(LOG_TAG, "Loader intialized");
 		Uri uri= Favorites.CONTENT_URI;
 		String[] projection = {Favorites._ID, 
 							   Favorites.KEY_ROUTE_ID,
 							   Favorites.KEY_ROUTE_SHORT_NAME,
 							   Favorites.KEY_ROUTE_LONG_NAME,
 							   Favorites.KEY_DIRECTION_ID,
 							   Favorites.KEY_DIRECTION_HEADSIGN,
 							   Favorites.KEY_START_STOP_ID,
 							   Favorites.KEY_START_STOP_NAME,
 							   Favorites.KEY_START_STOP_SEQ,
 							   Favorites.KEY_FINAL_STOP_ID,
 							   Favorites.KEY_FINAL_STOP_NAME};
 		return new CursorLoader(this, uri, projection, null, null, Favorites.DEFAULT_SORT_ORDER);
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
		Log.d(LOG_TAG, "onLoadFinished()");
		mCursorAdapter.swapCursor(data);
	}

	@Override
	public void onLoaderReset(Loader<Cursor> arg0) {
		Log.d(LOG_TAG, "onLoadReset()");
		mCursorAdapter.changeCursor(null);
	}   

    /*
     * Overrides for debugging the activity lifecycle
     */
    
    @Override
    protected void onRestart() {
    	Log.d(LOG_TAG, "MainActivity onRestart()");
    	super.onRestart();
    }
    
    @Override
    protected void onStart() {
    	Log.d(LOG_TAG, "MainActivity onStart()");
    	super.onStart();
    }
    
    @Override
    protected void onResume() {
    	Log.d(LOG_TAG, "MainActivity onResume()");
    	super.onResume();
    }
    
    @Override
    protected void onPause() {
    	Log.d(LOG_TAG, "MainActivity onPause()");
    	super.onPause();
    }
    
    @Override
    protected void onStop() {
    	Log.d(LOG_TAG, "MainActivity onStop()");
    	super.onStop();
    }
    
    @Override
    protected void onDestroy() {
    	Log.d(LOG_TAG, "MainActvity onDestory()");
    	super.onDestroy();
    }
}