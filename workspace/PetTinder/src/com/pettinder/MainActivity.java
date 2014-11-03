package com.pettinder;



import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.cognito.CognitoSyncManager;
import com.amazonaws.mobileconnectors.cognito.Dataset;
import com.amazonaws.regions.Regions;
import com.facebook.AppEventsLogger;

import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;

import android.content.Context;
import android.content.Intent;

import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;



public class MainActivity extends ActionBarActivity implements LocationListener{

	
	Button button1, button2;
    Intent discoveryIntent, settingsIntent;
    private TextView latituteField;
    private TextView longitudeField;
    private LocationManager locationManager;
    private String provider;
    
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
 	
    
	
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
        Log.d("Checkpoint 3", "Called onCreate");
    }

    
    @Override
    protected void onStart(){
    	super.onStart();
    	
    	

        Log.d("Checkpoint 3", "Called onStart");
    }
    
    @Override
    protected void onRestart(){
    	super.onRestart();
        Log.d("Checkpoint 3", "Called onRestart");
    }
    
    @Override
    protected void onResume(){
    	super.onResume();
    	locationManager.requestLocationUpdates(provider, 400, 1, this);
        Log.d("Checkpoint 3", "Called onResume");
        


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
        		//dataset.synchronize(this, syncCallback);
		*/
    }
    	 
    @Override
    protected void onPause(){
    	super.onPause();
    	locationManager.removeUpdates(this);
        Log.d("Checkpoint 3", "Called onPause");

        // Logs 'app deactivate' App Event. Facebook
        AppEventsLogger.deactivateApp(this);
    }
    
    @Override
    protected void onStop(){
    	
    	super.onStop();

        Log.d("Checkpoint 3", "Called onStop");
    }

    @Override
    protected void onDestroy(){
    	super.onDestroy();
        Log.d("Checkpoint 3", "Called onDestroy");
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
