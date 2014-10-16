package com.pettinder;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ImageButton;

public class DiscoveryActivity extends ActionBarActivity {
private int profileNum = 0;

	// Fetch the next discovery profile, updating the layout
	private void getProfile() {
		// This is a placeholder system for the sample profiles
		// Fetch profile pic, name, breed
		ImageView profilePic = (ImageView) findViewById(R.id.discoveryImage);
		TextView name = (TextView) findViewById(R.id.discoveryName);
		TextView breed = (TextView) findViewById(R.id.discoveryBreed);
		switch (profileNum++ % 4) {
				case 0:
					profilePic.setImageResource(R.drawable.loudnoises);
					name.setText("Spot");
					breed.setText("Dogbreed");
					break;
				case 1:
					profilePic.setImageResource(R.drawable.blanket);
					name.setText("Pup");
					breed.setText("Adorable");
					break;
				case 2:
					profilePic.setImageResource(R.drawable.forehead);
					name.setText("Billy");
					breed.setText("Teddy Bear");
					break;
				case 3:
					profilePic.setImageResource(R.drawable.poofy);
					name.setText("Fluffy");
					breed.setText("Something really fluffy");
					break;
				default:
		}
	}
	
	// Processes the user's selection for the current discovery profile
	private void handleDiscoverySelection(boolean selection) {
		if(selection) { // That is, user picked yes
			// Handle yes from user
		}
		getProfile();
	}
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discovery);
        
        // Obtain the first profile
        getProfile();
        
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
        		// Fetch rest of profile (or whatever that button does)
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
            return true;
        }else if(id == R.id.matches){
        	return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
}