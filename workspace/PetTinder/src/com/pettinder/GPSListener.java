package com.pettinder;


import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseUser;

import android.app.Activity;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;


public class GPSListener implements LocationListener {

	
	private Activity activity;
    private LocationManager lm;
    private int numberOfUpdates;
    ParseUser currentUser;
    private final String TAG = "GPSListener";
    
    public static final int MAX_NUMBER_OF_UPDATES = 10;

    public GPSListener(Activity activity, LocationManager lm) {
        this.activity = activity;
        this.lm = lm;
        Parse.initialize(activity, "bl9sFBxmrkDhNWSDxnlvbLIbeFrQ9kHUGEbBRI4a", "tCzPn6RbPx2ZJUmGc7AMb2eBoetXgO02A4jefTHp");
        currentUser = ParseUser.getCurrentUser();
    }
	
	@Override
	public void onLocationChanged(Location loc) {
		
		if (numberOfUpdates < MAX_NUMBER_OF_UPDATES) {
            numberOfUpdates++;

            Log.w("LAT", String.valueOf(loc.getLatitude()));
            Log.w("LONG", String.valueOf(loc.getLongitude()));
            Log.w("ACCURACY", String.valueOf(loc.getAccuracy() + " m"));
            Log.w("PROVIDER", String.valueOf(loc.getProvider()));
            Log.w("SPEED", String.valueOf(loc.getSpeed() + " m/s"));
            Log.w("ALTITUDE", String.valueOf(loc.getAltitude()));
            Log.w("BEARING", String.valueOf(loc.getBearing() + " degrees east of true north"));

            String message = "Location  null";

            if (loc != null) {
                message = "Current location is:  Latitude = "
                        + loc.getLatitude() + ", Longitude = "
                        + loc.getLongitude();
                // lm.removeUpdates(this);
                
         	    Toast.makeText(activity, "Location Info Sent", Toast.LENGTH_SHORT).show();
         	    currentUser.put("Longitude", loc.getLongitude());
         	    currentUser.put("Latitude", loc.getLatitude());
         	    Log.d(TAG, Double.toString(loc.getLongitude()));
         	    Log.d(TAG, Double.toString(loc.getLatitude()));
         	    try {
					currentUser.save();
				} catch (ParseException e) {
					Log.d(TAG,"Could not save location to Parse");
					e.printStackTrace();
				}
            }
            Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
        } else {
            lm.removeUpdates(this);
        }
	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
        Toast.makeText(activity, "Gps Disabled", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onProviderEnabled(String arg0) {
		// TODO Auto-generated method stub
        Toast.makeText(activity, "Gps Enabled", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
		// TODO Auto-generated method stub
	}
}