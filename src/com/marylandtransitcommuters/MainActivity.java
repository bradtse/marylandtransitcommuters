package com.marylandtransitcommuters;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

/**
 * The main activity
 */
public class MainActivity extends SherlockFragmentActivity {
	/* Logging tag */
	public static final String TAG = "BRAD";

	/* 
	 * Objects that are accessible to the whole activity 
	 */
	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;
	private String[] mDrawerItems;
	private CharSequence mTitle;
	private CharSequence mDrawerTitle;
		
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTitle = mDrawerTitle = getTitle();
        mDrawerItems = getResources().getStringArray(R.array.items);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        setUpNavDrawer();       
        addRouteFragment();
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
    	// Pass any configuration changes to the drawer toggle
    	mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getSupportMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }
    
    /* Callback function after we call invalidateOptionsMenu() */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
    	// Hide action bar items when nav drawer is open
    	boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
    	menu.findItem(R.id.action_settings).setVisible(!drawerOpen);
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
	    	default:
	    		return super.onOptionsItemSelected(item);
    	}
    }
  
    @Override
    public void setTitle(CharSequence title) {
    	mTitle = title;
    	getSupportActionBar().setTitle(mTitle);
    }
    
    /*
     * HELPER FUNCTIONS
     */
    
    /**
     *  Initialize the main frame layout with the route fragment
     */
    private void addRouteFragment() {
		Fragment fragment = new RouteFragment();
		FragmentTransaction fragmentTrans = getSupportFragmentManager().beginTransaction();
		fragmentTrans.replace(R.id.content_frame, fragment).commit();
    }
    
    /**
     * Takes care of setting up all the elements needed for a properly functioning
     * nav drawer
     */
    private void setUpNavDrawer() {
        // Set a custom shadow for the drawer
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        
        // Add items to the nav drawer from the array groups
        mDrawerList.setAdapter(new ArrayAdapter<String>(this, R.layout.drawer_list_text, 
        												mDrawerItems));
        // Register a callback to be invoked when item in nav drawer is clicked
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
        
        // Enable Action Bar app icon to toggle nav drawer
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
               
        mDrawerToggle = getActionBarDrawerToggle();
        
        // Set the drawer toggle as the DrawerListener
        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }
    
    /**
     * Returns a new ActionBarDrawerToggle that ties together the the proper 
     * interactions between the sliding drawer and the action bar app icon
     * @return A new ActionBarDrawerToggle object
     */
    private ActionBarDrawerToggle getActionBarDrawerToggle() {
    	return new ActionBarDrawerToggle(this, mDrawerLayout, R.drawable.ic_drawer,	
    									R.string.drawer_open, R.string.drawer_close) {
        	// Called when a drawer has settled in a completely closed state
        	public void onDrawerClosed(View view) {
        		getSupportActionBar().setTitle(mTitle);
        		supportInvalidateOptionsMenu(); // Redraw options menu
        	}
        	
        	// Called when a drawer has settled in a completely open state 
        	public void onDrawerOpened(View drawerView) {
        		getSupportActionBar().setTitle(mDrawerTitle);
        		supportInvalidateOptionsMenu(); // Redraw options menu
        	}
        };
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
     * Handles selection of nav drawer item
     */
    private void selectItem(int position) {
    	mDrawerList.setItemChecked(position, true);
    	setTitle(mDrawerItems[position]);
    	mDrawerLayout.closeDrawer(mDrawerList);
    }
}
