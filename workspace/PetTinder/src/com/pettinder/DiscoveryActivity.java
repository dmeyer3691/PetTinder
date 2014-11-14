package com.pettinder;

import java.util.HashMap;
import java.util.LinkedList;
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
    private ParseObject petProfile;
    private ParseUser currentUser;
    private List<ParseUser> potentialMatches, allUsers;
    private ImageView profilePic;
    private TextView name, breed;
    

    private static final String TAG = "DiscoveryActivity";
    private String currentId = "";
    
    // Fetch the next discovery profile, updating the layout
    private void getProfile() {
    	// This is a placeholder system for the sample profiles
    	// Fetch profile pic, name, breed

    	
    	
    	
    	currentId = potentialMatches.remove(0).getString("username");
    	
    	//Set profilePic, name, breed based on user ID
    	ParseObject nextMatch = new ParseObject("user");
    	ParseQuery<ParseObject> query = ParseQuery.getQuery("user");
        try {
        	nextMatch = query.get(currentId);
		} catch (ParseException e) {
			Log.d(TAG, "Error: Could not retrieve potential match line 78");
		}
    	 
        //Get user's pet profile
        ParseObject petProfile = null;
        if (nextMatch.has("myPetProfile")){
        	try {
				petProfile = nextMatch.getParseObject("myPetProfile").fetchIfNeeded();
				if(petProfile!=null){
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
			} catch (ParseException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				Log.d(TAG, "Error: Could not retrieve potential match's pet profile");
			}
        }
        
        
    	
    }
     	
 // Processes the user's selection for the current discovery profile
    private void handleDiscoverySelection(boolean liked) {

	    if (!choices.containsKey(currentId)) choices.put(currentId, liked);
	    if (liked){
		    ParseQuery<ParseObject> query = ParseQuery.getQuery("user");
		    ParseObject temp=null;
	        try {
	        	temp = query.get(currentId);
		        Map<String,Boolean> likedUserChoices = temp.getMap("choices");
		        String currentUsername = currentUser.getString("username");
			    if (likedUserChoices.containsKey(currentUsername) && likedUserChoices.get(currentUsername)){
			    	temp.put("User_1", currentId);
			    	temp.put("User_2", currentUsername);
			    	temp.save();
			    	temp = new ParseObject("matches");
			    	temp.put("User_1", currentUsername);
			    	temp.put("User_2", currentId);
			    	temp.save();
			    }
	        } catch (ParseException e) {
	        	Log.d(TAG, "Error: Could not retrieve potential match line 128");
	        }

	    }
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
        potentialMatches = new LinkedList<ParseUser>();
        
    	profilePic = (ImageView) findViewById(R.id.discoveryImage);
    	name = (TextView) findViewById(R.id.discoveryName);
    	breed = (TextView) findViewById(R.id.discoveryBreed);
        
        //Check for the currently logged in user and that it is linked to a Facebook account
    	currentUser = ParseUser.getCurrentUser();
		if ((currentUser == null) || !ParseFacebookUtils.isLinked(currentUser)) {
			Log.d(TAG,"Error: Cannot retrieve user");
		}
		
		//Initialize choices to store data, setting its object ID to the current user's username
        if (currentUser.has("choices")){
        	choices = currentUser.getMap("choices");
        	if (choices == null){
        		choices = new HashMap<String, Boolean>();
        		currentUser.put("choices", choices);
        	}
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
			allUsers = query.find();
		} catch (ParseException e) {
			Log.d(TAG,"Error: cannot retrieve nearby users");
		}
        
        for(ParseUser user : allUsers){
        	double user_lat = currentUser.getNumber("Latitude").doubleValue();
        	double user_long = currentUser.getNumber("Longitude").doubleValue();
        	double match_lat = user.getNumber("Latitude").doubleValue();
        	double match_long = user.getNumber("Longitude").doubleValue();
        	if (user_lat - match_lat <= radius && user_long - match_long <= radius){
        		if (!currentUser.getString("username").equals(user.getString("username")))
        			if (petProfile!=null) {
        				try {
    						petProfile.fetchIfNeeded();
    						potentialMatches.add(user);
            				Log.d(TAG, "match added");
    					} catch (ParseException e) {
    						// TODO Auto-generated catch block
    						e.printStackTrace();
    					}
        				
        			}
        			
        			
        	}
        		
        }

        if (potentialMatches.size() > 0) {
            getProfile();
    	} else {
    		profilePic.setImageResource(R.drawable.mystery_doge);
			name.setText("No pets left in the area!");
			breed.setText("");
			currentId = "";
			ImageButton yes = (ImageButton) findViewById(R.id.discoveryYes);
			ImageButton no = (ImageButton) findViewById(R.id.discoveryNo);
			ImageButton profile = (ImageButton) findViewById(R.id.discoveryMore);
			yes.setVisibility(View.GONE);
			no.setVisibility(View.GONE);
			profile.setVisibility(View.GONE);
    	}
        
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
