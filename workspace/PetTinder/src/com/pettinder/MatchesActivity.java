package com.pettinder;

import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;



public class MatchesActivity extends ActionBarActivity {

	Intent settingsIntent, connectionIntent;
	Button connectionButton, searchButton;
	
	View.OnClickListener connection = (new View.OnClickListener() {
		public void onClick(View v){
			startActivity(connectionIntent);
		}
	});
	
	View.OnClickListener search = (new View.OnClickListener() {
		public void onClick(View v){
			//todo
		}
	});
	
	
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matches);
        //define intents
        settingsIntent = new Intent(this, SettingsActivity.class);
        connectionIntent = new Intent(this, ConnectionActivity.class);
        // Grab list of matches for current user from AWS and display as buttons with pictures
        connectionButton = (Button) findViewById(R.id.button1);
        connectionButton.setOnClickListener(connection);
        searchButton = (Button) findViewById(R.id.searchButton);
        searchButton.setOnClickListener(search);
        // add matches picture to button
        Drawable img = getResources().getDrawable(R.drawable.loudnoises);
        img.setBounds(0, 0, 100, 100);
        connectionButton.setCompoundDrawables(null, null, img, null);
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
        	startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
    
}
