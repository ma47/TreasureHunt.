package com.example.team9.treasurehunt.GoogleAPI;

/**
 * Created by Edward on 25/04/2017.
 */

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.OnMapReadyCallback;


/**
 * Created by Mantas on 10/7/2015.
 *  -Modified by Edward
 */
public class GoogleApiHelper implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {
    private static final String TAG = GoogleApiHelper.class.getSimpleName();
    private static final int PLAY_SERVICES_REQUEST_CODE = 10022;

    private Context context;
    //Activity activity;

    GoogleApiClient mGoogleApiClient;
    //LocationServicesHelper locationServicesHelper;


    LocationListener locationListener = null;

    public GoogleApiHelper(Context context) {
        this.context = context;

//        checkPlayServices();
//        checkLocationServices();
        buildGoogleApiClient();
        connect();
        //locationServicesHelper = new LocationServicesHelper(this);
        //geofenceHelper = new GeofenceHelper(context);
    }

    public void connect() {
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }

    public void disconnect() {
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    public boolean isConnected() {
        if (mGoogleApiClient != null) {
            return mGoogleApiClient.isConnected();
        } else {
            return false;
        }
    }

    private void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(context)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();


    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d(TAG, "onConnectionSuspended: googleApiClient.connect()");
        mGoogleApiClient.connect();
    }


    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed: connectionResult.toString() = " + connectionResult.toString());
    }



    @Override
    public void onLocationChanged(Location location) {
        Log.d(TAG, "onLocationChanged: location => " + location.getLongitude() + "," + location.getLatitude());

        if (locationListener != null) {
            Log.d(TAG, "Location listener forward");
            locationListener.onLocationChanged(location);
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);

            //fire on location changed event for current lcoation if the location listener is set
            //on first map connection
            Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            if(location != null && locationListener != null) {
                locationListener.onLocationChanged(location);
            }
        }
    }

    public Location getLastKnownLocation() {
        Location location = null;


        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
           //api client running
            if (ContextCompat.checkSelfPermission(context,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                //have location permission
                location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            }

        }
        return location;
    }

    public void addLocationListener(LocationListener locationListener) {
        this.locationListener = locationListener;
        Location location = getLastKnownLocation();

        //it's possible GoogleApi connected before the locationListener was set
        //in which case fire the on location changed event for current location here too
        //but only if googleApiClient is set and connected
        if(location != null){
            this.locationListener.onLocationChanged(location);
        }
    }
    public void removeLocationListener() {
        this.locationListener = null;
    }

    //    private boolean checkPlayServices() {
//        //Initialize Google Play Services
//        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            if (ContextCompat.checkSelfPermission(context,
//                    Manifest.permission.ACCESS_FINE_LOCATION)
//                    == PackageManager.PERMISSION_GRANTED) {
//                //Location Permission already granted
//
//                return true;
//
//            } else {
//                //Request Location Permission
//                checkLocationPermission();
//                return true;
//            }
//        }
//        else {
//
//            return true;
//        }
//
//
//    }
//

//    public void checkLocationServices() {
//        // Get Location Manager and check for GPS & Network location services
//        LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
//        if (!lm.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
//                !lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
//            // Build the alert dialog
//            /*AlertDialog.Builder builder = new AlertDialog.Builder(context);
//            builder.setTitle("Location Services Not Active");
//            builder.setMessage("Please enable Location Services and GPS");
//            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                public void onClick(DialogInterface dialogInterface, int i) {
//                    // Show location settings when the user acknowledges the alert dialog
//                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
//                    context.startActivity(intent);
//                }
//            });
//            Dialog alertDialog = builder.create();
//            alertDialog.setCanceledOnTouchOutside(false);
//            alertDialog.show();*/
//            Log.d(TAG, "Location Service Not Enabled");
//        }
//    }




//    public void updateGeofenceApi() {
//        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
//            geofenceHelper.update(mGoogleApiClient);
//        }
//    }





//    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
//    private void checkLocationPermission() {
//        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
//                != PackageManager.PERMISSION_GRANTED) {
//
//            // Should we show an explanation?
//            if (ActivityCompat.shouldShowRequestPermissionRationale(activity,
//                    Manifest.permission.ACCESS_FINE_LOCATION)) {
//
//                // Show an explanation to the user *asynchronously* -- don't block
//                // this thread waiting for the user's response! After the user
//                // sees the explanation, try again to request the permission.
//                new AlertDialog.Builder(context)
//                        .setTitle("Location Permission Needed")
//                        .setMessage("This app needs the Location permission, please accept to use location functionality")
//                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialogInterface, int i) {
//                                //Prompt the user once explanation has been shown
//                                ActivityCompat.requestPermissions(activity,
//                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
//                                        MY_PERMISSIONS_REQUEST_LOCATION );
//                            }
//                        })
//                        .create()
//                        .show();
//
//
//            } else {
//                // No explanation needed, we can request the permission.
//                ActivityCompat.requestPermissions(activity,
//                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
//                        MY_PERMISSIONS_REQUEST_LOCATION );
//            }
//        }
//    }


//    @Override
//    public void onPause() {
//        super.onPause();
//
//        //stop location updates when Activity is no longer active
//        if (mGoogleApiClient != null) {
//            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
//        }
//    }
}
