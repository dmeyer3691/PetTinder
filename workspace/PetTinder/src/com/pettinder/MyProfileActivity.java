package com.pettinder;

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
	TextView petName, distance, petBio;
	ImageView petPic;
	ParseUser currentUser;
	ParseObject petProfile;
	private final String TAG = "MyProfileActivity";
    
	private void getParseUserData(){
		if (currentUser.has("myPetProfile")){
			petProfile = (ParseObject) currentUser.get("myPetProfile");
			Log.d(TAG, "retrieved petProfile");
			if(petProfile.has("petName")){
				petName.setText( (String) petProfile.get("petName"));
			}
			if(petProfile.has("location")){
				//getLocation of pet profile, calculate how far away from user currently, display answer
				//distance.setText( (String) petProfile.get("location"));
			}
			if(petProfile.has("petBio")){
				petBio.setText( (String) petProfile.get("petBio"));
			}
		} else {
			Log.d(TAG, "no profile available");
		}
		
	}
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);
        //define intents
        settingsIntent = new Intent(this, SettingsActivity.class);
        editProfileIntent = new Intent(this, EditProfileActivity.class);
        petName = (TextView) findViewById(R.id.petName);
        distance = (TextView) findViewById(R.id.distance);
        petBio = (TextView) findViewById(R.id.profileBio);
        petPic = (ImageView) findViewById(R.id.petPic);
        currentUser = ParseUser.getCurrentUser();
        
        if ((currentUser != null) && ParseFacebookUtils.isLinked(currentUser)) {
        	//user logged in through facebook
        	getParseUserData();
		} else {
			Log.d(TAG,"user not logged in");
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
        getMenuInflater().inflate(R.menu.view_profile_menu, menu);
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
