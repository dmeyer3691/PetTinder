package com.pettinder;

import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;


public class SettingsActivity extends ActionBarActivity {

	Button viewProfileButton, discoveryPreferencesButton, appSettingsButton, shareButton;
	Intent viewProfileIntent, discoveryPreferencesIntent, appSettingsIntent;
	
	// onClickListeners    
	View.OnClickListener viewProfile = (new View.OnClickListener() {
        public void onClick(View v) {
        	startActivity(viewProfileIntent);
        }
    });
	View.OnClickListener discoveryPreferences = (new View.OnClickListener() {
		public void onClick(View v){
			startActivity(discoveryPreferencesIntent);
		}
	});
	View.OnClickListener appSettings = (new View.OnClickListener() {
		public void onClick(View v){
			startActivity(appSettingsIntent);		}
	});
	View.OnClickListener share = (new View.OnClickListener() {
		public void onClick(View v){
			//todo
		}
	});
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        // define intents
        viewProfileIntent = new Intent(this, MyProfileActivity.class);
        discoveryPreferencesIntent = new Intent(this, DiscoveryPreferencesActivity.class);
        appSettingsIntent = new Intent(this, AppSettingsActivity.class);
        
        // define buttons
        viewProfileButton = (Button) findViewById(R.id.Button00);
        viewProfileButton.setOnClickListener(viewProfile);
        discoveryPreferencesButton = (Button) findViewById(R.id.Button01);
        discoveryPreferencesButton.setOnClickListener(discoveryPreferences);
        appSettingsButton = (Button) findViewById(R.id.Button02);
        appSettingsButton.setOnClickListener(appSettings);
        shareButton = (Button) findViewById(R.id.Button03);
        shareButton.setOnClickListener(share);
        //take this out once if we have sharing functionality
        shareButton.setVisibility(View.INVISIBLE);
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
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return super.onOptionsItemSelected(item);
    }   
}