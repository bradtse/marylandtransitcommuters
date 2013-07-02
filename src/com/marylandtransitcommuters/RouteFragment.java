package com.marylandtransitcommuters;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.actionbarsherlock.app.SherlockFragment;

/**
 * The fragment showing the list of all available routes
 */
public class RouteFragment extends SherlockFragment {
	private Context context;
	private View rootView;
	private String response = "";
	private ListView mRouteList;
	
	public RouteFragment() {};
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = getActivity();
		Log.d(MainActivity.TAG, "Fragment onCreate()");
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_routes, container, false);
		return rootView;
	}

	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		mRouteList = (ListView) rootView.findViewById(R.id.routes_list);
		
		Log.d(MainActivity.TAG, "fragment onActivityCreated()");
		
		// Create new URL and Json objects, then pass it along to the post helper
		try { 
			URL url = new URL("http://bradleytse.com/sqlquery.php");
			JSONObject json = new JSONObject().put("query", "SELECT * FROM Routes");
			Log.d(MainActivity.TAG, "Json: " + json.toString());
			post(url, json.toString());
		} catch (MalformedURLException e) {
			response = "Bad URL";
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void post(URL url, String payload) {
		new NewThread(url, payload).execute();
	}
	
	private class NewThread extends AsyncTask<Void, Void, String[]> {
		private URL url;
		private String msg;
		
		public NewThread(URL u, String payload) {
			url = u;
			msg = payload;
		}

		@Override
		protected String[] doInBackground(Void... arg0) {
			InputStream in;
			BufferedWriter bw = null;
			StringBuilder fin = new StringBuilder();
			HttpURLConnection conn = null;
			String[] finArr = null;
			
			try {
				// Does not actually do any network IO
				conn = (HttpURLConnection) url.openConnection();
				Log.d(MainActivity.TAG, "CHECK");

				// Set up properties
				conn.setDoOutput(true);
				conn.setRequestProperty("Content-Type", "application/json");
				conn.setRequestProperty("Accept", "application/json");
				conn.setRequestMethod("POST");
				
				try {
					// This actually opens the connection and then sends POST and headers
					bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
				} catch (IOException e) {
					Log.d(MainActivity.TAG, e.getCause().toString());
				}
				
				try {
					// Send the message
					bw.write(msg);
					bw.flush();
				} catch (IOException e) {
					Log.d(MainActivity.TAG, "bad write");
				}
				
				try {
					// Get server response
					in = conn.getInputStream();
					// Convert to string
					Scanner s = new Scanner(in, "UTF-8");
					while(s.hasNext() != false) {
						fin.append(s.next());
//						Log.d(MainActivity.BRAD, s.next());
					}
				} catch (IOException e) {
					Log.d(MainActivity.TAG, "bad input");
				} 

			} catch (IOException e) {
				Log.d(MainActivity.TAG, e.getMessage());
			} finally {
				try {
					bw.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} // close writer
				conn.disconnect(); // close connection
			}
			try {
				JSONArray jArray = new JSONArray(fin.toString());
				int i = 0;
				ArrayList<String> routes = new ArrayList<String>();
				for (i = 0; i < jArray.length(); i++) {
					JSONObject jObj = (JSONObject) jArray.getJSONObject(i);
					routes.add(jObj.getString("ShortName"));
				}
				finArr = new String[routes.size()];
				finArr = routes.toArray(finArr);
				Log.d(MainActivity.TAG, finArr.toString());
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				Log.d(MainActivity.TAG, e.getMessage());
			}
			return finArr;
		}
		
		@Override
		protected void onPostExecute(String[] result){
			mRouteList.setAdapter(new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, result));
		}
		
	}

	
    /** 
     * The click listener for the ListView of routes 
     */
    private class RouteItemClickListener implements ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			selectRoute(id);
		}
    }
    
    /*
     * HELPER FUNCTIONS
     */
    
    /** 
     * Helper function to replace the current fragment with the time fragment
     */
    private void selectRoute(long id) {
		Log.d(MainActivity.TAG, "Item selected: " + String.valueOf(id));
		
		Bundle args = new Bundle();
		args.putString(TransitContract.Routes._ID, String.valueOf(id));
//		args.putSerializable(key, value)
		
		Fragment fragment = new TimeFragment();
		fragment.setArguments(args);
		
		FragmentManager fragmentManager = getFragmentManager();
		FragmentTransaction fragmentTrans = fragmentManager.beginTransaction();
		
		fragmentTrans.replace(R.id.content_frame, fragment);
		fragmentTrans.addToBackStack(null);
		fragmentTrans.commit();
    }
}
