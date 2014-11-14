package com.pettinder;

import java.io.FileNotFoundException;
import java.io.IOException;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

import android.support.v7.app.ActionBarActivity;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


public class MyProfileActivity extends ActionBarActivity {

	Intent settingsIntent, editProfileIntent;
	TextView petName, petAge, petBio, petBreed, aboutPet;
	ImageView petPic;
	ParseUser currentUser;
	ParseObject petProfile;
	ParseFile profilePicture;
	BitmapDrawable imageBitmap;
	private final String TAG = "MyProfileActivity";
	
	
	private void getParseUserData(){


		if (currentUser.has("myPetProfile") && (petProfile!= null)){

			if(petProfile.has("petName")){
				petName.setText(petProfile.getString("petName")+", ");
				aboutPet.setText("About " + petProfile.getString("petName"));
				Log.d(TAG, petProfile.getString("petName"));
			}
			if(petProfile.has("petAge")){
				petAge.setText(petProfile.getString("petAge"));
			}
			if(petProfile.has("petBio")){
				petBio.setText(petProfile.getString("petBio"));
			}
			if(petProfile.has("petBreed")){
				String breed = petProfile.getString("petBreed");
				petBreed.setText(breed);
			}
			if(petProfile.has("profilePicture")){
				profilePicture = petProfile.getParseFile("profilePicture");
				byte[] data;
				try {
					data = profilePicture.getData();
					imageBitmap = new BitmapDrawable(this.getResources(), BitmapFactory.decodeByteArray(data, 0, data.length));
					petPic.setImageDrawable(imageBitmap);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					Log.d(TAG, "Error loading image");
					e.printStackTrace();
				}
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
        petBreed = (TextView) findViewById(R.id.petBreed);
        petBio = (TextView) findViewById(R.id.profileBio);
        petPic = (ImageView) findViewById(R.id.petPic);
        aboutPet = (TextView) findViewById(R.id.bio);
        currentUser = ParseUser.getCurrentUser();
        

        
        try {
        	if (currentUser.has("myPetProfile")){
    			petProfile = currentUser.getParseObject("myPetProfile").fetchIfNeeded();
        	}
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
    		try {
            	if (currentUser.has("myPetProfile")){
        			petProfile = currentUser.getParseObject("myPetProfile").fetchIfNeeded();
            	}
    			Log.d(TAG, "retrieved petProfile");

    		} catch (ParseException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    			Log.d(TAG,"error fetching");
    		}
    		
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
