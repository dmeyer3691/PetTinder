package com.pettinder;

import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;



public class ConnectionActivity extends ActionBarActivity {

	Intent settingsIntent, viewProfileIntent;

    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connection);
        //define intents
        settingsIntent = new Intent(this, SettingsActivity.class);
        viewProfileIntent = new Intent(this, ViewProfileActivity.class);
        //define buttons
       
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
        getMenuInflater().inflate(R.menu.connection_menu, menu);
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
        }else if (id == R.id.view_profile) {
        	startActivity(viewProfileIntent);
        	return true;
        }else if (id == R.id.remove_match) {
        	// todo
        	return true;
        }else if (id == R.id.report_match){
        	//todo
        	return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
    
}
