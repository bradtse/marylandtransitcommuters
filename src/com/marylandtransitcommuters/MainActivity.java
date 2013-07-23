package com.marylandtransitcommuters;

import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
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
import com.marylandtransitcommuters.fragments.RoutesFragment;

/**
 * The main activity
 */
public class MainActivity extends SherlockFragmentActivity {
	public static final String LOG_TAG = "BRAD";

	private CharSequence mTitle;
	private CharSequence mDrawerTitle;
	private String[] mDrawerItems;
	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;
		
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTitle = mDrawerTitle = getTitle();
        mDrawerItems = getResources().getStringArray(R.array.items);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        setupNavDrawer();   
        if (savedInstanceState == null) {
        	addRoutesFragment();
        }
    }
    
    /**
     * Takes care of setting up all the elements needed for a properly functioning
     * navigation drawer
     */
    private void setupNavDrawer() {
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        mDrawerList.setAdapter(new ArrayAdapter<String>(this, R.layout.drawer_list_text, 
        												mDrawerItems));
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
        
        // Enable Action Bar app icon to toggle nav drawer
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
               
        mDrawerToggle = getActionBarDrawerToggle();
        mDrawerLayout.setDrawerListener(mDrawerToggle);
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
    	mDrawerList.setItemChecked(position, true);
    	setTitle(mDrawerItems[position]);
    	mDrawerLayout.closeDrawer(mDrawerList);
    }
    
    /**
     * Returns a new ActionBarDrawerToggle that ties together the the proper 
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
  
    /**
     *  Initialize the main frame layout with the routes fragment
     */
    private void addRoutesFragment() {
		Fragment routesFragment = new RoutesFragment();
		FragmentManager fm = getSupportFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();
		ft.add(R.id.content_frame, routesFragment, RoutesFragment.TAG);
		ft.commit();
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
        
		MenuItem searchItem = menu.findItem(R.id.menu_search);
		SearchView search = (SearchView) searchItem.getActionView();
		
		// Forces the SearchView to stay open
		search.setIconifiedByDefault(false);
		
		// Set the text color for the SearchView
		AutoCompleteTextView searchText = (AutoCompleteTextView) search.findViewById(R.id.abs__search_src_text);
		searchText.setHintTextColor(Color.WHITE);
		searchText.setTextColor(Color.WHITE);
		
        return super.onCreateOptionsMenu(menu);
    }
    
    /* Callback function when invalidateOptionsMenu() is called */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
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
	    	default:
	    		return super.onOptionsItemSelected(item);
    	}
    }
}
