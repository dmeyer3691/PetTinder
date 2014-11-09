package com.pettinder;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseObject;
import com.parse.ParseUser;

import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;



public class MyProfileActivity extends ActionBarActivity {

	Intent settingsIntent, editProfileIntent;
	TextView petName, petAge, distance, petBio, aboutPet;
	ImageView petPic;
	ParseUser currentUser;
	ParseObject petProfile;
	private final String TAG = "MyProfileActivity";
    
	private void getParseUserData(){


		if (currentUser.has("myPetProfile")){

			if(petProfile.has("petName")){
				petName.setText(petProfile.getString("petName")+", ");
				aboutPet.setText("About " + petProfile.getString("petName"));
				Log.d(TAG, petProfile.getString("petName"));
			}
			if(petProfile.has("petAge")){
				petAge.setText(petProfile.getString("petAge"));
			}
			if(petProfile.has("petZip")){
				//getLocation of pet profile, calculate how far away from user currently, display answer
				//distance.setText( (String) petProfile.get("location"));
			}
			if(petProfile.has("petBio")){
				petBio.setText(petProfile.getString("petBio"));
			}
			Log.d(TAG, "Profile Loaded");

		} else {
			Log.d(TAG, "no profile available");
		}
		
	}
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);
        Parse.initialize(this, "bl9sFBxmrkDhNWSDxnlvbLIbeFrQ9kHUGEbBRI4a", "tCzPn6RbPx2ZJUmGc7AMb2eBoetXgO02A4jefTHp");
        
        //define intents
        settingsIntent = new Intent(this, SettingsActivity.class);
        editProfileIntent = new Intent(this, EditProfileActivity.class);
        petName = (TextView) findViewById(R.id.petName);
        petAge = (TextView) findViewById(R.id.petAge);
        distance = (TextView) findViewById(R.id.distance);
        petBio = (TextView) findViewById(R.id.profileBio);
        petPic = (ImageView) findViewById(R.id.petPic);
        aboutPet = (TextView) findViewById(R.id.bio);
        currentUser = ParseUser.getCurrentUser();
        
        try {
			petProfile = currentUser.getParseObject("myPetProfile").fetchIfNeeded();
			Log.d(TAG, "retrieved petProfile");

		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.d(TAG,"error fetching");
		}
        

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
    	if ((currentUser != null) && ParseFacebookUtils.isLinked(currentUser)) {
        	//user logged in through facebook
        	getParseUserData();
		} else {
			Log.d(TAG,"user not logged in");
		}
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
        getMenuInflater().inflate(R.menu.my_profile_menu, menu);
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
        }else if (id == R.id.edit_profile) {
        	startActivity(editProfileIntent);
        	return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
    
}
