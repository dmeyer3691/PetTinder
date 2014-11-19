package com.pettinder;

import java.util.Arrays;
import java.util.List;

import com.facebook.AppEventsLogger;

import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;

import android.support.v7.app.ActionBarActivity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity {

	private static final String TAG = "MainActivity";
	
	Button loginButton;
    Intent settingsIntent;
	private Dialog progressDialog;
	private ParseUser currentUser;
	private GPSManager gps;
    
 	
 	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		ParseFacebookUtils.finishAuthentication(requestCode, resultCode, data);
	}
    
    private void onLoginButtonClicked() {
		MainActivity.this.progressDialog = ProgressDialog.show(MainActivity.this, "", "Logging in...", true);
		
		List<String> permissions = Arrays.asList("public_profile", "email");
		// NOTE: for extended permissions, like "user_about_me", your app must be reviewed by the Facebook team
		// (https://developers.facebook.com/docs/facebook-login/permissions/)
		
		ParseFacebookUtils.logIn(permissions, this, new LogInCallback() {
		
			@Override
			public void done(ParseUser user, ParseException error) {
				// TODO Auto-generated method stub
				MainActivity.this.progressDialog.dismiss();
				if (user == null) {
					Log.d(TAG, "Uh oh. The user cancelled the Facebook login.");
					Toast.makeText(getApplicationContext(), "Login Failed", Toast.LENGTH_LONG).show();
				} else {
					if (user.isNew()) {
						Log.d(TAG, "User signed up and logged in through Facebook!");

					} else {
						Log.d(TAG, "User logged in through Facebook!");
					}
					Toast.makeText(getApplicationContext(), "Logged In", Toast.LENGTH_LONG).show();
					gps.start();
				}
			}
		});
	}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Parse.initialize(this, "bl9sFBxmrkDhNWSDxnlvbLIbeFrQ9kHUGEbBRI4a", "tCzPn6RbPx2ZJUmGc7AMb2eBoetXgO02A4jefTHp");
		ParseFacebookUtils.initialize(getString(R.string.facebook_app_id));
		
        setContentView(R.layout.activity_main);
        
        //define intents
        settingsIntent = new Intent(this, SettingsActivity.class);
        
        gps = new GPSManager(MainActivity.this);
        
    	// register buttons
        loginButton = (Button) findViewById(R.id.facebookLoginButton);
		loginButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onLoginButtonClicked();
			}
		});
        Log.d(TAG, "Called onCreate");
    }
    
    @Override
    protected void onStart(){
    	super.onStart();
        Log.d(TAG, "Called onStart");
    }
    
    @Override
    protected void onRestart(){
    	super.onRestart();
        Log.d(TAG, "Called onRestart");
    }
    
    @Override
    protected void onResume(){
    	super.onResume();
    	// Check if there is a currently logged in user
		// and it's linked to a Facebook account.
		currentUser = ParseUser.getCurrentUser();
		if ((currentUser != null) && ParseFacebookUtils.isLinked(currentUser)) {
			Log.d(TAG,"User already logged in to facebook");
			Toast.makeText(getApplicationContext(), "Logged In", Toast.LENGTH_LONG).show();
			gps.start();
		}
        Log.d(TAG, "Called onResume");
        // Logs 'install' and 'app activate' App Events. Facebook
        AppEventsLogger.activateApp(this);
    }
    	 
    @Override
    protected void onPause(){
    	super.onPause();
        Log.d(TAG, "Called onPause");
        // Logs 'app deactivate' App Event. Facebook
        AppEventsLogger.deactivateApp(this);
    }
    
    @Override
    protected void onStop(){
    	super.onStop();
        Log.d(TAG, "Called onStop");
    }

    @Override
    protected void onDestroy(){
    	super.onDestroy();
        Log.d(TAG, "Called onDestroy");
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
    	/*
        int id = item.getItemId();
        if (id == R.id.action_settings) {
        	startActivity(settingsIntent);
            return true;
        }
        */
        return super.onOptionsItemSelected(item);
    }
}