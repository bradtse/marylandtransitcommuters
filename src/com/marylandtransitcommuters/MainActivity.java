package com.marylandtransitcommuters;

import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

public class MainActivity extends SherlockActivity {

	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;
	
	private String[] mItems;
	private CharSequence mTitle;
	private CharSequence mDrawerTitle;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        // Get needed resources and layouts
        mTitle = mDrawerTitle = getTitle();
        mItems = getResources().getStringArray(R.array.items);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        
        // Set a custom shadow for the drawer
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        
        // Add items to the nav drawer from the array groups
        mDrawerList.setAdapter(new ArrayAdapter<String>(this, 
        		R.layout.drawer_list_text, mItems));
        // Register a callback to be invoked when item in nav drawer is clicked
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
        
        // Enable Action Bar app icon to toggle nav drawer
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
               
        // ActionBarDrawerToggle ties together the the proper interactions
        // between the sliding drawer and the action bar app icon
        mDrawerToggle = new ActionBarDrawerToggle(
        		this,					/* Host activity */
        		mDrawerLayout,			/* Drawerlayout */
        		R.drawable.ic_drawer,	/* Drawer image */
        		R.string.drawer_open,	/* Open drawer description */
        		R.string.drawer_close	/* Close drawer description */
        		) {
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
        
        // Set the drawer toggle as the DrawerListener
        mDrawerLayout.setDrawerListener(mDrawerToggle);
     
        if (savedInstanceState == null) {
        	selectItem(0);
        }
        Toast.makeText(this, getIntent().getAction(), Toast.LENGTH_SHORT).show();
    }

    /*
     * When using the ActionBarDrawerToggle, you must call it during
     * onPostCreate() and onConfigurationChanged()...
     */ 
    
    /* Syncs the action bar drawer toggle */
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

    /* Initialize the content of the options menu */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getSupportMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }
    
    /* Called whenever we call invalidateOptionsMenu() */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
    	// Hide action bar items when nav draw is open
    	boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
    	menu.findItem(R.id.action_settings).setVisible(!drawerOpen);
    	return super.onPrepareOptionsMenu(menu);
    }
    
    /* Handle what happens when an item in the options menu is selected */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	// Let ActionBarDrawerToggle attempt to handle home/up action
//    	if (mDrawerToggle.onOptionsItemSelected(item)) {
//    		return true;
//    	}
    	
    	// Handle action buttons
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
    
    /* The click listener for ListView in the nav drawer */
    private class DrawerItemClickListener implements ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			selectItem(position);
		}		
    }
    
    /* Take action when item in nav drawer is selected */
    private void selectItem(int position) {
    	mDrawerList.setItemChecked(position, true);
    	setTitle(mItems[position]);
    	mDrawerLayout.closeDrawer(mDrawerList);
    }
    
    /* Change the title of the action bar when an item is selected */
    @Override
    public void setTitle(CharSequence title) {
    	mTitle = title;
    	getSupportActionBar().setTitle(mTitle);
    }
    
    /* Checks if external storage is writable */
    public boolean isExternalStorageWritable() {
    	String state = Environment.getExternalStorageState();
    	if (Environment.MEDIA_MOUNTED.equals(state)) {
    		return true;
    	}
    	return false;
    }
    
    /* Checks if external storage is readable */
    public boolean isExternalStorageReadable() {
    	String state = Environment.getExternalStorageState();
    	if (Environment.MEDIA_MOUNTED.equals(state) || 
    			Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
    		return true;
    	}
    	return false;
    }
}
