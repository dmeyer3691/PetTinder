package com.pettinder;

import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;



public class AppSettingsActivity extends ActionBarActivity {

	Intent settingsIntent;
	Button logoutButton, deleteButton;
	
	View.OnClickListener delete = (new View.OnClickListener() {
		public void onClick(View v){
			// todo
			
		}
	});
	
	View.OnClickListener logout = (new View.OnClickListener() {
		public void onClick(View v){
			// todo 
			
		}
	});
	
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_settings);
        //define intents
        settingsIntent = new Intent(this, SettingsActivity.class);
        deleteButton = (Button) findViewById(R.id.delete_account_button);
        deleteButton.setOnClickListener(delete);
        logoutButton = (Button) findViewById(R.id.logout_button);
        logoutButton.setOnClickListener(logout);
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
