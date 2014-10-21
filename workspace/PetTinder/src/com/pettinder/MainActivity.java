package com.pettinder;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;

import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.DialogFragment;
import android.app.Activity;
import android.app.Dialog;
//import android.app.DialogFragment;
import android.content.Intent;
import android.content.IntentSender;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;



public class MainActivity extends ActionBarActivity implements
	GooglePlayServicesClient.ConnectionCallbacks,
	GooglePlayServicesClient.OnConnectionFailedListener {

	
	Button button1, button2;
    Intent discoveryIntent, settingsIntent;
    LocationClient mLocationClient;
    Location location;

    // onClickListeners    
	View.OnClickListener destroy = (new View.OnClickListener() {
        public void onClick(View v) {
            finish();
        }
    });
	View.OnClickListener discovery = (new View.OnClickListener() {
		public void onClick(View v){		
			/*
	    	if(mLocationClient.isConnected()){
	    		//get location
		    	Log.d("MainActivity", "Connected");
	    		location = mLocationClient.getLastLocation();
	    		Log.d("MainActivity", location.toString());
	    	}
	    	*/
	    	Log.d("MainActivity", "Not Connected");
			
			startActivity(discoveryIntent);
		}
	});
	
	
	
	
	
	//
	//
	//Start stuff to allow location services 
	//
	//
	
	
	   // Global constants
 /*
  * Define a request code to send to Google Play services
  * This code is returned in Activity.onActivityResult
  */
 private final static int
         CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;

 // Define a DialogFragment that displays the error dialog
 public static class ErrorDialogFragment extends DialogFragment {
     // Global field to contain the error dialog
     private Dialog mDialog;
     // Default constructor. Sets the dialog field to null
     public ErrorDialogFragment() {
         super();
         mDialog = null;
     }
     // Set the dialog to display
     public void setDialog(Dialog dialog) {
         mDialog = dialog;
     }
     // Return a Dialog to the DialogFragment.
     @Override
     public Dialog onCreateDialog(Bundle savedInstanceState) {
         return mDialog;
     }
 }

 /*
  * Handle results returned to the FragmentActivity
  * by Google Play services
  */
 @Override
 protected void onActivityResult(
         int requestCode, int resultCode, Intent data) {
     // Decide what to do based on the original request code
     switch (requestCode) {
         case CONNECTION_FAILURE_RESOLUTION_REQUEST :
         /*
          * If the result code is Activity.RESULT_OK, try
          * to connect again
          */
             switch (resultCode) {
                 case Activity.RESULT_OK :
                 /*
                  * Try the request again
                  */
                 break;
             }
     }
  }
 private boolean servicesConnected() {
     // Check that Google Play services is available
     int resultCode =
             GooglePlayServicesUtil.
                     isGooglePlayServicesAvailable(this);
     // If Google Play services is available
     if (ConnectionResult.SUCCESS == resultCode) {
         // In debug mode, log the status
         Log.d("Location Updates",
                 "Google Play services is available.");
         // Continue
         return true;
     // Google Play services was not available for some reason.
     // resultCode holds the error code.
     } else {
         // Get the error dialog from Google Play services
         Dialog errorDialog = GooglePlayServicesUtil.getErrorDialog(
                 resultCode,
                 this,
                 CONNECTION_FAILURE_RESOLUTION_REQUEST);

         // If Google Play services can provide an error dialog
         if (errorDialog != null) {
             // Create a new DialogFragment for the error dialog
             ErrorDialogFragment errorFragment =
                     new ErrorDialogFragment();
             // Set the dialog in the DialogFragment
             errorFragment.setDialog(errorDialog);
             // Show the error dialog in the DialogFragment
             errorFragment.show(getSupportFragmentManager(), "Location Updates");

         }
         return false;
     }
 }
	
	
	/*
     * Called by Location Services when the request to connect the
     * client finishes successfully. At this point, you can
     * request the current location or start periodic updates
     */
    @Override
    public void onConnected(Bundle dataBundle) {
        // Display the connection status
        Toast.makeText(this, "Connected", Toast.LENGTH_SHORT).show();
        
        

    }
    /*
     * Called by Location Services if the connection to the
     * location client drops because of an error.
     */
    @Override
    public void onDisconnected() {
        // Display the connection status
        Toast.makeText(this, "Disconnected. Please re-connect.",
                Toast.LENGTH_SHORT).show();
    }
    /*
     * Called by Location Services if the attempt to
     * Location Services fails.
     */
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        /*
         * Google Play services can resolve some errors it detects.
         * If the error has a resolution, try sending an Intent to
         * start a Google Play services activity that can resolve
         * error.
         */
        if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(
                        this,
                        CONNECTION_FAILURE_RESOLUTION_REQUEST);
                /*
                 * Thrown if Google Play services canceled the original
                 * PendingIntent
                 */
            } catch (IntentSender.SendIntentException e) {
                // Log the error
                e.printStackTrace();
            }
        } else {
            /*
             * If no resolution is available, display a dialog to the
             * user with the error.
             */
            //showErrorDialog(connectionResult.getErrorCode());
        }
    }
	

	//
	//
    //
    //End stuff to make location services work
    //
    //
	//
	
	
	
	
	
	
	
	
	
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //define intents
        discoveryIntent = new Intent(this, DiscoveryActivity.class);
        settingsIntent = new Intent(this, SettingsActivity.class);
        
        //location
        mLocationClient = new LocationClient(this, this, this);

    	// register buttons
    	button1 = (Button) findViewById(R.id.button1);
    	button1.setOnClickListener(destroy);
        button2 = (Button) findViewById(R.id.button2);
        button2.setOnClickListener(discovery);
        Log.d("Checkpoint 3", "Called onCreate");
    }

    
    @Override
    protected void onStart(){
    	super.onStart();
    	
    	//if google play services available
    	if (servicesConnected()){
    		   		
    		// connect location client
        	mLocationClient.connect();
        	
        	/*
    		try {
    			Thread.sleep(4000);
    		} catch (InterruptedException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
        	*/
        	
        	if(mLocationClient.isConnected()){
        		//get location
        		location = mLocationClient.getLastLocation();
        		Log.d("MainActivity", location.toString());
        	}
        	Log.d("MainActivity", "Not Connected");
    		
    	}

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
    	if(mLocationClient.isConnected()){
    		//get location
    		location = mLocationClient.getLastLocation();
    		Log.d("MainActivity", location.toString());
    	}
    	Log.d("MainActivity", "Not Connected");
    	
    	
        Log.d("Checkpoint 3", "Called onResume");
    }
    
    @Override
    protected void onPause(){
    	super.onPause();
        Log.d("Checkpoint 3", "Called onPause");
    }
    
    @Override
    protected void onStop(){
    	super.onStop();
        mLocationClient.disconnect();

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
	
    
    
}
