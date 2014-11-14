package com.pettinder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Button;

import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;


public class DiscoveryActivity extends ActionBarActivity {

    Intent matchesIntent, settingsIntent, viewProfileIntent;
    
    private Map<String, Boolean> choices;
    private ParseUser currentUser;
    private List<ParseUser> potentialMatches;
    

    private static final String TAG = "DiscoveryActivity";
    private String currentId = "";
    
    // Fetch the next discovery profile, updating the layout
    private void getProfile() {
    	// This is a placeholder system for the sample profiles
    	// Fetch profile pic, name, breed
    	ImageView profilePic = (ImageView) findViewById(R.id.discoveryImage);
    	TextView name = (TextView) findViewById(R.id.discoveryName);
    	TextView breed = (TextView) findViewById(R.id.discoveryBreed);
    	
    	if (choices.containsKey(currentId)){
    		getProfile();
    		return;
    	}
    	
    	if (potentialMatches.size() > 0) {
    		currentId = potentialMatches.remove(0).getString("username");
    	} else {
    		profilePic.setImageResource(R.drawable.mystery_doge);
			name.setText("No pets left in the area!");
			breed.setText("");
			currentId = "";
			ImageButton yes = (ImageButton) findViewById(R.id.discoveryYes);
			ImageButton no = (ImageButton) findViewById(R.id.discoveryNo);
			yes.setVisibility(View.GONE);
			no.setVisibility(View.GONE);   
    	}
    	
    	
    	//Set profilePic, name, breed based on user ID
    	ParseObject nextMatch = new ParseObject("user");
    	ParseQuery<ParseObject> query = ParseQuery.getQuery("user");
        try {
        	nextMatch = query.get(currentId);
		} catch (ParseException e) {
			Log.d(TAG, "Error: Could not retrieve potential match");
		}
    	 
        //Get user's pet profile
        ParseObject petProfile = null;
        if (nextMatch.has("myPetProfile")){
        	try {
				petProfile = currentUser.getParseObject("myPetProfile").fetchIfNeeded();
			} catch (ParseException e) {
				Log.d(TAG, "Error: Could not retrieve potential match's pet profile");
			}
        }
        
        //Set view data for pet
        ParseFile profilePicture = petProfile.getParseFile("profilePicture");
		byte[] data;
		try {
			data = profilePicture.getData();
			BitmapDrawable imageBitmap = new BitmapDrawable(this.getResources(), BitmapFactory.decodeByteArray(data, 0, data.length));
			profilePic.setImageDrawable(imageBitmap);
		} catch (ParseException e) {
			Log.d(TAG, "Error loading image");
			e.printStackTrace();
		}
        name.setText(petProfile.getString("petName"));
    	breed.setText(petProfile.getString("petBreed"));
    	
    }
     	
    // Processes the user's selection for the current discovery profile
    private void handleDiscoverySelection(boolean liked) {

    	String id = currentId;
    	if (!choices.containsKey(id)) choices.put(id, liked); 
    	getProfile();
    }
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discovery);
        //define intents
        matchesIntent = new Intent(this, MatchesActivity.class);
        settingsIntent = new Intent(this, SettingsActivity.class);
        viewProfileIntent = new Intent(this, ViewProfileActivity.class);   

        
        //Check for the currently logged in user and that it is linked to a Facebook account
    	currentUser = ParseUser.getCurrentUser();
		if ((currentUser == null) || !ParseFacebookUtils.isLinked(currentUser)) {
			Log.d(TAG,"Error: Cannot retrieve user");
		}
		
		//Initialize choices to store data, setting its object ID to the current user's username
        if (currentUser.has("choices")){
        	choices = currentUser.getMap("choices");
        } else {
        	choices = new HashMap<String,Boolean>();
        	currentUser.put("choices", choices);
        }
        
        //get the preferred radius
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        int radius = Integer.parseInt(sp.getString("prefDiscoveryRange","10"));
        
        //Set up list of profiles and obtain the first profile
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        try {
			potentialMatches = query.find();
		} catch (ParseException e) {
			Log.d(TAG,"Error: cannot retrieve nearby users");
		}
        
        for(ParseUser user : potentialMatches){
        	double user_lat = currentUser.getNumber("Latitude").doubleValue();
        	double user_long = currentUser.getNumber("Longitude").doubleValue();
        	double match_lat = user.getNumber("Latitude").doubleValue();
        	double match_long = user.getNumber("Longitude").doubleValue();
        	if (user_lat - match_lat <= radius && user_long - match_long <= radius){
        		if (!currentUser.getString("username").equals(user.getString("username")))
        				potentialMatches.add(user);
        	}
        		
        }

        
        getProfile();
        // Register buttons
        ImageButton noButton = (ImageButton) findViewById(R.id.discoveryNo);
        noButton.setOnClickListener(new ImageButton.OnClickListener() {
        	public void onClick(View v) {
        		handleDiscoverySelection(false);
        	}
        });
        
        ImageButton yesButton = (ImageButton) findViewById(R.id.discoveryYes);
        yesButton.setOnClickListener(new ImageButton.OnClickListener() {
          	public void onClick(View v) {
           		handleDiscoverySelection(true);
           	}
        });
        
        ImageButton moreButton = (ImageButton) findViewById(R.id.discoveryMore);
        moreButton.setOnClickListener(new ImageButton.OnClickListener() {
           	public void onClick(View v) {
           		
           		
           		//probably to intent.putExtra(something) to know which profile to load in ViewProfileActivity
           		startActivity(viewProfileIntent);
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
    	currentUser.saveInBackground();
    }
    
    @Override
    protected void onStop(){
    	super.onStop();
    }

    @Override
    protected void onDestroy(){
    	super.onDestroy();
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.discovery_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.settings) {
        	startActivity(settingsIntent);
            return true;
        }else if(id == R.id.matches){
        	startActivity(matchesIntent);
        	return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
    
}
