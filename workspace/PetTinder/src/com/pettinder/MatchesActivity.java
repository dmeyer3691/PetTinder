package com.pettinder;

import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;



public class MatchesActivity extends ActionBarActivity implements View.OnClickListener {

	Button connectionButton, searchButton;
	View matchesList;
	int numMatches;
	
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.searchButton:
			break;
		default: // One of the connection buttons
			Intent connectionIntent = new Intent(this, ConnectionActivity.class);
			for (int i = 0; i < ((ViewGroup) matchesList).getChildCount(); i++) {
				if (v.getId() == ((ViewGroup) matchesList).getChildAt(i).getId()){
					// Attach some identifier for the match to the intent
					connectionIntent.putExtra("placeholderKey",  -1);
				}
			}
			startActivity(connectionIntent);
			break;
		}
	}
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Drawable img;
        Button cButton;
        setContentView(R.layout.activity_matches);
        matchesList = findViewById(R.id.matchesList);
        // Set listener for search button
        searchButton = (Button) findViewById(R.id.searchButton);
        searchButton.setOnClickListener(this);
        // Assemble sample connection button
        connectionButton = (Button) findViewById(R.id.button1);
        connectionButton.setOnClickListener(this);
        img = getResources().getDrawable(R.drawable.loudnoises);
        img.setBounds(0, 0, 100, 100);
        connectionButton.setCompoundDrawables(null, null, img, null);
        // Grab list of matches for current user from AWS and display as buttons with pictures
        numMatches = 0;
        for (int i = 0; i < numMatches; i++) {
        	// Create new button, add into matchesList view
        	cButton = new Button(this);
        	cButton.setOnClickListener(this);
        	img = getResources().getDrawable(R.drawable.loudnoises);
            img.setBounds(0, 0, 100, 100);
            cButton.setCompoundDrawables(null, null, img, null);
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
        	Intent settingsIntent = new Intent(this, SettingsActivity.class);
        	startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
    
}
