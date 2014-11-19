package com.pettinder;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationListener;
import android.location.LocationManager;
import android.widget.Toast;


public class GPSManager {
	private Activity activity;
    private LocationManager mlocManager;
    private LocationListener gpsListener;
    Intent discoveryIntent;

    public GPSManager(Activity activity) {
        this.activity = activity;
        discoveryIntent = new Intent(activity, DiscoveryActivity.class);
    }

    public void start() {
        mlocManager = (LocationManager) activity
                .getSystemService(Context.LOCATION_SERVICE);

        if (mlocManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            setUp();
            findLoc();
            activity.startActivity(discoveryIntent);
        } else {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                    activity);
            alertDialogBuilder
                    .setMessage("GPS is disabled in your device. For PetTinder to work it must be enabled. Enable it?")
                    .setCancelable(false)
                    .setPositiveButton("Enable GPS",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                        int id) {
                                    Intent callGPSSettingIntent = new Intent(
                                            android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                    activity.startActivity(callGPSSettingIntent);
                                }
                            });
            alertDialogBuilder.setNegativeButton("Cancel",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                        	//not sure if we should destroy app on cancel
                        	activity.finish();
                        	// or simply start them in a loop that makes them enable it
                        	/*
                            dialog.cancel();
                            Toast.makeText(activity, "GPS must be enabled", Toast.LENGTH_SHORT).show(); 
                            start();
                            */
                        }
                    });
            AlertDialog alert = alertDialogBuilder.create();
            alert.show();
        }
    }

    public void setUp() {
        gpsListener = new GPSListener(activity, mlocManager);
    }

    public void findLoc() {
        mlocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1, 1,
                gpsListener);

        if (mlocManager.getLastKnownLocation(LocationManager.GPS_PROVIDER) == null)
            Toast.makeText(activity, "LAST Location null", Toast.LENGTH_SHORT)
                    .show();
        else {
            gpsListener.onLocationChanged(mlocManager
                    .getLastKnownLocation(LocationManager.GPS_PROVIDER));
        }
    }
}