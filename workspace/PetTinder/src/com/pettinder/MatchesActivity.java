package com.pettinder;

import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Toast;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.ParseQuery;
import com.parse.FindCallback;
import java.util.ArrayList;
import java.util.List;

/*
 * Code adapted from:
 * https://www.sinch.com/tutorials/android-messaging-tutorial-using-sinch-and-parse/
 */
public class MatchesActivity extends ActionBarActivity {
	private static final String TAG = "MatchesActivity";
	Button connectionButton, searchButton;
	ListView matchesListView;
	ArrayList<String> matches;

	public void openConversation(ArrayList<String> matches, int pos) {
	    ParseQuery<ParseUser> query = ParseUser.getQuery();
	    query.whereEqualTo("objectId", matches.get(pos));
	    query.findInBackground(new FindCallback<ParseUser>() {
	       public void done(List<ParseUser> user, ParseException e) {
	           if (e == null) {
	        	   Intent intent = new Intent(getApplicationContext(), MessagingActivity.class);
	               intent.putExtra("RECIPIENT_ID", user.get(0).getObjectId());
	               startActivity(intent);
	           } else {
	               Toast.makeText(getApplicationContext(),
	                   "Error finding that user",
	                       Toast.LENGTH_SHORT).show();
	           }
	       }
	    });
	}
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matches);
        Parse.initialize(this, "bl9sFBxmrkDhNWSDxnlvbLIbeFrQ9kHUGEbBRI4a", "tCzPn6RbPx2ZJUmGc7AMb2eBoetXgO02A4jefTHp");
        
        // Retrieve and list the user's matches
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Matches");
        query.whereEqualTo("User_1", ParseUser.getCurrentUser().getString("objectId"));
        query.include("User_2");
        // query.include("User_2.myPetProfile");
        query.findInBackground(new FindCallback<ParseObject>() {
        	public void done(List<ParseObject> matchList, com.parse.ParseException e) {
        		if (e == null) {
    				// The list of usernames (IDs), used for messaging
        	        matches = new ArrayList<String>();
    				// The list of human-readable pet names, only used by matchesArrayAdapter
        	        List<String> names = new ArrayList<String>();
        	        // Each iteration adds a username / pet name to the respective list
        			for (int i=0; i<matchList.size(); i++) {
        				ParseObject match = matchList.get(i).getParseObject("User_2"); //fetch?
        				matches.add(match.getString("objectId"));
        				// Obtain pet name
        				if (match.has("myPetProfile")){
        					try {
        						ParseObject petProfile = match.getParseObject("myPetProfile").fetchIfNeeded();
        						if (petProfile != null) names.add(petProfile.getString("petName"));
        						else names.add("Unknown Name");
        					} catch (ParseException ex) {
        						Log.d(TAG, "Error: Could not retrieve match's name");
        						names.add("Unknown Name");
        					}
        				}
        			}
        			// Display the list of matches
        			ArrayAdapter<String> matchesArrayAdapter =
        					new ArrayAdapter<String>(getApplicationContext(),
        							R.layout.match_list_item, names);
        	        matchesListView = (ListView) findViewById(R.id.matchesListView);
        			matchesListView.setAdapter(matchesArrayAdapter);
        			
        			// Set click listener for the match list
        			matchesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        				@Override
        				public void onItemClick(AdapterView<?> a, View v, int i, long l) {
        					openConversation(matches, i);
        				}
        			});
        			
        		} else {
        			Toast.makeText(getApplicationContext(),
        	                "Error loading user list",
        	                    Toast.LENGTH_LONG).show();
        		}
        	}
        });
    }

    
    @Override
    protected void onStart(){
    	super.onStart();
    }
    
    @Override
    protected void onRestart(){
    	super.onRestart();
    }
    
    @Override
    protected void onResume(){
    	super.onResume();
    }
    
    @Override
    protected void onPause(){
    	super.onPause();
    }
    
    @Override
    protected void onStop(){
    	super.onStop();
    }

    @Override
    protected void onDestroy(){
        stopService(new Intent(this, MessageService.class));
    	super.onDestroy();
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.matches_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.settings) {
        	Intent settingsIntent = new Intent(this, SettingsActivity.class);
        	startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
    
}
