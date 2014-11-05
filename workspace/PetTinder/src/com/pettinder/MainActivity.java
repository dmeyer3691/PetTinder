package com.pettinder;



import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.cognito.CognitoSyncManager;
import com.amazonaws.mobileconnectors.cognito.Dataset;
import com.amazonaws.regions.Regions;
import com.facebook.AppEventsLogger;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseObject;
import com.parse.ParseUser;

import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;

import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;



public class MainActivity extends FragmentActivity implements LocationListener{

	private static final String TAG = "MainActivity";
	
	
	Button button1, button2, loginButton, logoutButton;
    Intent discoveryIntent, settingsIntent;
    private TextView latituteField;
    private TextView longitudeField;
    private LocationManager locationManager;
    private String provider;
    private UiLifecycleHelper uiHelper;
	private Dialog progressDialog;
    
 // onClickListeners    
 	View.OnClickListener destroy = (new View.OnClickListener() {
         public void onClick(View v) {
             finish();
         }
     });
 	View.OnClickListener discovery = (new View.OnClickListener() {
 		public void onClick(View v){		 			
 			startActivity(discoveryIntent);
 		}
 	});
 	
 	/*
 	private void onSessionStateChange(Session session, SessionState state, Exception exception) {
 	    if (state.isOpened()) {
 	        Log.d(TAG, "Logged in...");
 	    } else if (state.isClosed()) {
 	        Log.d(TAG, "Logged out...");
 	    }
 	}
 	
 	private Session.StatusCallback callback = new Session.StatusCallback() {
 	    @Override
 	    public void call(Session session, SessionState state, Exception exception) {
 	        onSessionStateChange(session, state, exception);
 	    }
 	};
	*/
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Parse.initialize(this, "bl9sFBxmrkDhNWSDxnlvbLIbeFrQ9kHUGEbBRI4a", "tCzPn6RbPx2ZJUmGc7AMb2eBoetXgO02A4jefTHp");
		ParseFacebookUtils.initialize(getString(R.string.facebook_app_id));

		/*
        //test
        ParseObject testObject = new ParseObject("TestObject");
        testObject.put("foo", "bar");
        testObject.saveInBackground();
		 */
        /*
        if (savedInstanceState == null) {
        	Log.d(TAG, "savedState=null");
            // Add the fragment on initial activity setup
            mainFragment = new MainFragment();
            getSupportFragmentManager()
            .beginTransaction()
            .add(android.R.id.content, mainFragment)
            .commit();
        } else {
            // Or set the fragment from restored state info
            mainFragment = (MainFragment) getSupportFragmentManager()
            .findFragmentById(android.R.id.content);
        }
        */
        setContentView(R.layout.activity_main);

        
        /*
        uiHelper = new UiLifecycleHelper(this, callback);
        uiHelper.onCreate(savedInstanceState);
        */
        //define intents
        discoveryIntent = new Intent(this, DiscoveryActivity.class);
        settingsIntent = new Intent(this, SettingsActivity.class);
        
        
    	// register buttons
    	button1 = (Button) findViewById(R.id.button1);
    	button1.setOnClickListener(destroy);
        button2 = (Button) findViewById(R.id.button2);
        button2.setOnClickListener(discovery);
        latituteField = (TextView) findViewById(R.id.TextView02);
        longitudeField = (TextView) findViewById(R.id.TextView04);
        loginButton = (Button) findViewById(R.id.facebookLoginButton);
		loginButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onLoginButtonClicked();
			}
		});
		logoutButton = (Button) findViewById(R.id.facebookLogoutButton);
		logoutButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onLogoutButtonClicked();
			}
		});
		

		// Check if there is a currently logged in user
		// and it's linked to a Facebook account.
		ParseUser currentUser = ParseUser.getCurrentUser();
		if ((currentUser != null) && ParseFacebookUtils.isLinked(currentUser)) {
			// Go to the user info activity
			Log.d(TAG,"User already logged in to facebook");
		}

        // Get the location manager
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        // Define the criteria how to select the locatioin provider -> use
        // default
        Criteria criteria = new Criteria();
        provider = locationManager.getBestProvider(criteria, false);
        Location location = locationManager.getLastKnownLocation(provider);

        // Initialize the location fields
        if (location != null) {
        	System.out.println("Provider " + provider + " has been selected.");
        	onLocationChanged(location);
        } else {
        	latituteField.setText("Location not available");
        	longitudeField.setText("Location not available");
        }

        Log.d(TAG, "Called onCreate");
    }

    @Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		ParseFacebookUtils.finishAuthentication(requestCode, resultCode, data);
	}
    
    private void onLogoutButtonClicked() {
		// Log the user out
		ParseUser.logOut();
		Log.d(TAG,"user logged out of facebook");
    }
    
    private void onLoginButtonClicked() {
		MainActivity.this.progressDialog = 
				ProgressDialog.show(MainActivity.this, "", "Logging in...", true);
		
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
				} else if (user.isNew()) {
					Log.d(TAG, "User signed up and logged in through Facebook!");
					
				} else {
					Log.d(TAG, "User logged in through Facebook!");
					
				}
			}
		});
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
    	/*
    	Session session = Session.getActiveSession();
        if (session != null &&
               (session.isOpened() || session.isClosed()) ) {
            onSessionStateChange(session, session.getState(), null);
        }

        uiHelper.onResume();
    	 */
    	locationManager.requestLocationUpdates(provider, 400, 1, this);
        Log.d(TAG, "Called onResume");
        


        // Logs 'install' and 'app activate' App Events. Facebook
        AppEventsLogger.activateApp(this);
        
        //AWS
        /*
        //initialize cognito client
        CognitoCachingCredentialsProvider cognitoProvider = new CognitoCachingCredentialsProvider(
        	    myActivity.getContext(), // get the context for the current activity
        	    "951970281312",
        	    "us-east-1:6032b334-1a37-4866-a6b9-af66fd72e2eb",
        	    "YOUR UNAUTHENTICATED ARN HERE",
        	    "arn:aws:iam::951970281312:role/Cognito_PetTinderUsersAuth_DefaultRole",
        	    Regions.US_EAST_1
        	);
        
        //store and sync 
        CognitoSyncManager syncClient = new CognitoSyncManager(
        		   myActivity.getContext(), 
        		   "us-east-1:6032b334-1a37-4866-a6b9-af66fd72e2eb", 
        		   Regions.US_EAST_1,
        		   cognitoProvider);
        		   
        		//Dataset dataset = syncClient.openOrCreateDataset("myDataset");
        		//dataset.put("myKey", "myValue");
        */		//dataset.synchronize(this, syncCallback);
		
    }
    	 
    @Override
    protected void onPause(){
    	super.onPause();
    	/*
        uiHelper.onPause();
    	 */
    	locationManager.removeUpdates(this);
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
    	/*
        uiHelper.onDestroy();
		*/
        Log.d(TAG, "Called onDestroy");
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
        	startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onLocationChanged(Location location) {
      int lat = (int) (location.getLatitude());
      int lng = (int) (location.getLongitude());
      latituteField.setText(String.valueOf(lat));
      longitudeField.setText(String.valueOf(lng));
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
      // TODO Auto-generated method stub

    }

    @Override
    public void onProviderEnabled(String provider) {
      Toast.makeText(this, "Enabled new provider " + provider,
          Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onProviderDisabled(String provider) {
      Toast.makeText(this, "Disabled provider " + provider,
          Toast.LENGTH_SHORT).show();
    }
    
    
}
