package com.marylandtransitcommuters;

import java.util.ArrayDeque;
import java.util.Iterator;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.widget.SearchView;
import com.commonsware.cwac.merge.MergeAdapter;
import com.marylandtransitcommuters.fragments.RoutesFragment;
import com.marylandtransitcommuters.fragments.TransitFragment;
import com.marylandtransitcommuters.fragments.TransitFragment.ReplaceFragmentListener;

/**
 * The main activity
 */
public class MainActivity extends SherlockFragmentActivity implements ReplaceFragmentListener {
	public static final String LOG_TAG = "BRAD";

	private CharSequence mTitle;
	private CharSequence mDrawerTitle;
	private String[] mDrawerItems;
	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;
	private ArrayDeque<String> mFragTags;
	private String mCurrFragTag;
		
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

        setupNavigationDrawer();   
    }
    
    /**
     * Takes care of setting up all the elements needed for a properly functioning
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
    
    private MergeAdapter setupMergeAdapter() {
    	MergeAdapter m = new MergeAdapter();
    	
    	m.addView(createHeader("CATEGORY"));
    	m.addView(createDivider());

    	ArrayAdapter<String> a = new ArrayAdapter<String>(this, R.layout.drawer_list_text, 
        												mDrawerItems);
    	m.addAdapter(a);
    	
    	m.addView(createHeader("FAVORITES"));
    	m.addView(createDivider());

    	return m;
    }
    
    private TextView createDivider() {
    	TextView tv = (TextView) getLayoutInflater().inflate(R.layout.divider, null);
    	return tv;
    }
    
    private TextView createHeader(String s) {
		TextView tv = (TextView) getLayoutInflater().inflate(R.layout.drawer_header, null);
    	tv.setText(s);
    	return tv;
    }
    
    @Override
    public void replaceFragment(String newFragTag, Fragment newFrag, 
    						 boolean addToBackStack, boolean clearFrags) {

    	// Removes all of the existing fragments
		if (clearFrags == true && mCurrFragTag != null) {
			removeAllFragments();
    	}

    	FragmentManager fm = getSupportFragmentManager();
    	FragmentTransaction ft = fm.beginTransaction();
    	ft.setCustomAnimations(R.animator.slide_in_right, R.animator.slide_out_left, 
				   R.animator.slide_in_left, R.animator.slide_out_right);

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

    	ft.commit();
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
        Iterator<String> it = mFragTags.iterator();
        while (it.hasNext()) {
        	fragment = (TransitFragment) fm.findFragmentByTag(it.next());
        	ft.remove(fragment);
        }

        ft.commit();
        mCurrFragTag = null;
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
    	// Since it is a mergeadapter, must account for other views
    	position = position - 2;

    	// Indicates the item has been selected
//    	mDrawerList.setItemChecked(position, true);
    	// Then set the action bar title to the item that was selected
    	setTitle(mDrawerItems[position]);
    	
    	if (position == 0) {
        	replaceFragment(RoutesFragment.TAG, new RoutesFragment(), false, true);
        	mFragTags = new ArrayDeque<String>();
    	} else if (position == 1) {
    		
    	}
    	
    	// Then close the drawer
    	mDrawerLayout.closeDrawer(mDrawerList);
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