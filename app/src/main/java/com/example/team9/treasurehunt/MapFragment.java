package com.example.team9.treasurehunt;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.team9.treasurehunt.ApiClient.ApiClient;
import com.example.team9.treasurehunt.ApiClient.VolleyErrorHelper;
import com.example.team9.treasurehunt.ApiResources.HotOrCold;
import com.example.team9.treasurehunt.GoogleAPI.GoogleApiHelper;
import com.example.team9.treasurehunt.GoogleAPI.GoogleApiHelperSingleton;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

/**
 * Created by Zilvinas / Adam
 * volley/api integration - Edward
 * Hot-or-cold animation - Zilvinas
 */

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MapFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MapFragment extends Fragment implements OnMapReadyCallback, LocationListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    GoogleMap mGoogleMap;
    MapView mapView;
    SupportMapFragment fragment;
    SupportMapFragment mapFrag;
    LocationRequest mLocationRequest;
    //GoogleApiClient mGoogleApiClient;
    GoogleApiHelper googleApiHelper;
    Location mLastLocation;
    Marker mCurrLocationMarker;
    double distance = 0; //test value

    public MapFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MapFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MapFragment newInstance(String param1, String param2) {
        MapFragment fragment = new MapFragment();
        return fragment;
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getActivity().setTitle("Map");



        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }




        setHasOptionsMenu(true);

    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        //hide refresh button for map, can't refresh map...
        menu.findItem(R.id.action_refresh).setVisible(false);

        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.activity_maps, container, false);


        mapView = (MapView) view.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);

        // needed to get the map to display immediately
        mapView.onResume();

        //i don't actually think this is necessary because we use getMapAsync
        //do we need this as well?
//        try {
//            MapsInitializer.initialize(getActivity().getApplicationContext());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }




        //its possible we don't need to get the map every time
        //if its already been created once and we save the MapFragment instance
        //surely we don't need to create the map again?
        //however it doesn't play nice with centering on the users location
        //so i'm leaving it for now

        //start our map view and centre on the users current location
        //set location on first load as last known location
        mapView.getMapAsync(this);
        Button button = (Button) view.findViewById(R.id.btn);
        button.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                setHotOrCold();

            }
        });
        return view;
    }

    private void playHotOrColdAnimation(){
        if(distance<=0.5) {
            int colorNumber = (int) (distance * 550) + 80;
            View overlay;
            overlay = getView().findViewById(R.id.transparentOverlay);

            //int i = Double.valueOf((colorNumber * (350f / 510)) * (colorNumber / 256f)).intValue();

            overlay.setBackgroundColor(Color.rgb(0, 0, colorNumber));
            AlphaAnimation animation1 = new AlphaAnimation(0.0f, 0.50f);
            animation1.setDuration(2000);
            animation1.setStartOffset(500);
            animation1.setFillAfter(true);
            overlay.startAnimation(animation1);
            animation1.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    int colorNumber = (int) (distance * 550) + 80;
                    View overlay;
                    overlay = getView().findViewById(R.id.transparentOverlay);
                    overlay.setBackgroundColor(Color.rgb(0, 0, colorNumber));
                    AlphaAnimation animation2 = new AlphaAnimation(0.5f, 0f);
                    animation2.setStartOffset(2500);
                    animation2.setDuration(1000);
                    animation2.setFillAfter(true);
                    overlay.startAnimation(animation2);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
        }



        else if(distance>0.5 && distance<=1){
            int colorNumber = (int) ((distance-0.5) * 550) + 80;
            View overlay;
            overlay = getView().findViewById(R.id.transparentOverlay);
            overlay.setBackgroundColor(Color.rgb(colorNumber, 0, 0));
            AlphaAnimation animation1 = new AlphaAnimation(0f, 0.5f);
            animation1.setDuration(2000);
            animation1.setStartOffset(500);
            animation1.setFillAfter(true);
            overlay.startAnimation(animation1);
            animation1.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    int colorNumber = (int) ((distance-0.5) * 550) + 80;
                    View overlay;
                    overlay = getView().findViewById(R.id.transparentOverlay);
                    overlay.setBackgroundColor(Color.rgb(colorNumber, 0, 0));
                    AlphaAnimation animation2 = new AlphaAnimation(0.5f, 0f);
                    animation2.setStartOffset(2500);
                    animation2.setDuration(1000);
                    animation2.setFillAfter(true);
                    overlay.startAnimation(animation2);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
        }
    }



    @Override
    public void onResume() {
        super.onResume();
        if(googleApiHelper != null) {
            googleApiHelper.addLocationListener(this);
        }
    }

    private void setUpMapIfNeeded() {

        if (mGoogleMap == null) {
            MapFragment fragment = new MapFragment();
            mapFrag.getMapAsync(this);
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        //stop location updates when Activity is no longer active
        //if(googleApiHelper != null) {
        //    googleApiHelper.removeLocationListener();
        //}
    }

    private void startMapApi(){
        //Initialize Google Play Services

        //check we have the neccesary permissions

        //if user ir running android API greater than version 22
        //we check permissions at run time, and only start map the map
        //if they have them
        //if api version is 22 or less, the user must have selected permissions
        //on install. so we can launch the map without checking permissions.
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED || android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {

            Log.d("STATE", android.os.Build.VERSION.SDK_INT + "");

            if (googleApiHelper == null) {
                ///build new googleApiHelper
                googleApiHelper = GoogleApiHelperSingleton.getInstance(getActivity()).getGoogleApiHelper();
            }
            //setup location listener as OnLocationChanged method in this fragment to update map
            googleApiHelper.addLocationListener(this);
            mGoogleMap.setMyLocationEnabled(true);


            Log.d("STATE", "started map");
        } else {
            //if user does not have the correct permission
            //we ask them for location permission
            checkLocationPermission();
        }

    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the MapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap)
    {
        mGoogleMap = googleMap;
        mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        Log.d("STATE", "onMapRead");


        startMapApi();

    }



    @Override
    public void onLocationChanged(Location location)
    {
        mLastLocation = location;
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }

        //Place current location marker
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());

        //move map camera
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,15));

    }

    //its this fragments job to check / ask for permissions to set up our googleAPI instance

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(getActivity())
                        .setTitle("Location Permission Needed")
                        .setMessage("This app needs the Location permission, please accept to use location functionality")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(getActivity(),
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION );
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION );
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.

                    startMapApi();


                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(getActivity(), "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    private void setHotOrCold(){
        LatLng latLng;
        //get current coordinates of user
        Location currentLocation = GoogleApiHelperSingleton.getInstance(getContext()).getGoogleApiHelper().getLastKnownLocation();
        if(currentLocation == null){
            //do not have location permissions
            Log.d("STATE", "no api client");
            showHotOrColdFailureError();
            return;
        }else {
            latLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
        }



        ApiClient.getHotOrCold(latLng, getContext() , new Response.Listener<HotOrCold>() {

            @Override
            public void onResponse(HotOrCold hotOrCold) {
                //successfully got hot or cold response
                Log.d("STATE", "success");
                Log.d("HOT_OR_COLD", "hot_or_cold:" + hotOrCold.getHotOrCold());

                //set our hot or cold value
                distance = hotOrCold.getHotOrCold();

                //play hot or cold animation
                playHotOrColdAnimation();

            }
        },new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                //failed to get hot or cold response

                showServerConnectionError(error);
            }
        });
    }

    private void showHotOrColdFailureError(){
        if(getActivity() != null) {

            Toast.makeText(getActivity(),
                    "Could not determine your location, please try again in a bit!",
                    Toast.LENGTH_SHORT).show();
        }

    }

    private void showServerConnectionError(VolleyError volleyError){
        //if the api calls ends whilst this fragment is not opened then we
        //have some problems where the context is null, getActivity()
        //also doesn't fix the problem but i feel like it should...
        if(getActivity() != null) {
            String message;
            message = VolleyErrorHelper.getServerConnectionError(volleyError, true);

            Toast.makeText(getActivity(),
                    message,
                    Toast.LENGTH_SHORT).show();
        }

    }
}
