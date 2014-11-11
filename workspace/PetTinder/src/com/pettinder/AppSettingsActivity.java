package com.pettinder;

import org.json.JSONException;
import org.json.JSONObject;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.model.GraphUser;
import com.facebook.widget.ProfilePictureView;
import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;

import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class AppSettingsActivity extends ActionBarActivity {

	Intent settingsIntent, mainIntent;
	Button logoutButton, deleteButton;
	private static final String TAG = "AppSettingsActivity";
	ParseUser currentUser;
	ProfilePictureView profilePic;
	TextView profileName;
	View.OnClickListener delete = (new View.OnClickListener() {
		public void onClick(View v){
			// todo
			
		}
	});
	
	View.OnClickListener logout = (new View.OnClickListener() {
		public void onClick(View v){
			logout();
		}
	});
	
	public void logout() {
		ParseUser.logOut();
		//To make sure actually clearing facebook session tokens and truly logged out
		com.facebook.Session fbs = com.facebook.Session.getActiveSession();
		if (fbs == null) {
			fbs = new com.facebook.Session(this);
			com.facebook.Session.setActiveSession(fbs);
		}
		fbs.closeAndClearTokenInformation();
		Log.d(TAG,"user logged out of facebook");
		startActivity(mainIntent);
	}
    
	private void makeMeRequest() {
        Request request = Request.newMeRequest(ParseFacebookUtils.getSession(),
                new Request.GraphUserCallback() {
                    @Override
                    public void onCompleted(GraphUser user, Response response) {
                        if (user != null) { 
                            // Create a JSON object to hold the profile info
                            JSONObject userProfile = new JSONObject();
                            try {                   
                                // Populate the JSON object 
                                userProfile.put("facebookId", user.getId());
                                Log.d(TAG, user.getId());
                                userProfile.put("name", user.getName());
                                                       
                                // Now add the data to the UI elements
                                try {
                 		        	Log.d(TAG,userProfile.getString("facebookId"));
                 		            if (userProfile.getString("facebookId") != null) {
                 		                String facebookId = userProfile.get("facebookId")
                 		                        .toString();
                 		                profilePic.setProfileId(facebookId);
                 		                Log.d(TAG, "Profile Pic Set");
                 		            } else {
                 		                // Show the default, blank user profile picture
                 		                profilePic.setProfileId(null);
                 		            }
                 		            // Set additional UI elements
                 		            if(userProfile.getString("name") != null) {
                 		            	profileName.setText("Signed in as " + userProfile.getString("name"));
                 		            } else {
                 		            	profileName.setText("Not currently signed in");
                 		            }
                 		        } catch (JSONException e) {
                 		            // handle error
                 		        	Log.d(TAG,"JSON exception");
                 		        }
                                
                            } catch (JSONException e) {
                                Log.d(TAG, "Error parsing returned user data.");
                            }
        
                        } else if (response.getError() != null) {
                            // handle error
                        }                  
                    }

					             
                });
        request.executeAsync();
     
    }
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_settings);
        //define intents
        settingsIntent = new Intent(this, SettingsActivity.class);
        mainIntent = new Intent(this, MainActivity.class);
        deleteButton = (Button) findViewById(R.id.delete_account_button);
        deleteButton.setOnClickListener(delete);
        logoutButton = (Button) findViewById(R.id.logout_button);
        logoutButton.setOnClickListener(logout);
        // Check if there is a currently logged in user
 		// and it's linked to a Facebook account.
 		currentUser = ParseUser.getCurrentUser();
		profilePic = (ProfilePictureView) findViewById(R.id.facebookProfilePic);
		profileName = (TextView) findViewById(R.id.facebookProfileName);
 		if ((currentUser != null) && ParseFacebookUtils.isLinked(currentUser)) {
 		       makeMeRequest();
 		} else {
 			logoutButton.setVisibility(View.INVISIBLE);
 		}
 		Log.d(TAG, "Called OnCreate");
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
    	super.onDestroy();
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
    	
    	//shoulnd't need this
        //getMenuInflater().inflate(R.menu.view_profile_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
    	
    	
    	//shoulnd't need this
    	/*
        int id = item.getItemId();
        if (id == R.id.settings) {
        	startActivity(settingsIntent);
            return true;
        }
        */
        return super.onOptionsItemSelected(item);
    }
    
    
}
