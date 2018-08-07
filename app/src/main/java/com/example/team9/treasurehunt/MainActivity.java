package com.example.team9.treasurehunt;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.example.team9.treasurehunt.ApiClient.ApiClient;
import com.example.team9.treasurehunt.ApiClient.VolleyErrorHelper;
import com.example.team9.treasurehunt.ApiResources.CollectableTreasure;
import com.example.team9.treasurehunt.GoogleAPI.GoogleApiHelperSingleton;
import com.example.team9.treasurehunt.Runtime.SessionPlayer;
import com.google.android.gms.maps.model.LatLng;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by Mantas
 * saving fragment instances - Edward
 * treasure collection fragment intgration - Edward
 * treasure collection API integration - Edward
 * overall app theme styling - Rob
 */
public class MainActivity extends AppCompatActivity {

    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;

    BottomNavigationView navigation;

    private Class currentFragment;
    private Class previousFragment;
    private int currentNavigationLayoutId = -1;

    //qr code scanner object
    private IntentIntegrator qrScan;

    private AlertDialog collectingAlertDialog;

    //keeps track of previously created fragments
    //we will have one reference per fragment layout
    //we will map the id of the layout to its fragment instance
    //this is fine for us because as we are only using a small number of fragments (4)
    //and all fragment layouts are only used by one fragment class each
    Map<Integer, Fragment> fragments = new HashMap<Integer, Fragment>();


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {


        /* Bottom NAV functionality method
        case: R.id.button pressed { do something}
        intent - to trigger activities
         */
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            /*************************************************************************
             Info:

             https://developer.android.com/reference/android/app/FragmentTransaction.html
            ***********************************************************/

            Intent intent = null;
            Class fragmentClass = null;

            switch (item.getItemId()) {

                case R.id.navigation_map:
                    // assign Class variable to selected class(fragment) and get it with the newInstance or something like that
                    fragmentClass = MapFragment.class;
                    break;

                case R.id.navigation_clue:
                    fragmentClass = CluesFragment.class;
                    break;

                case R.id.navigation_qrScanner:
                    //special case to launch qr scanner
                    qrScan.initiateScan();
                    return true;

                case R.id.navigation_leaderboard:
                    fragmentClass = LeaderboardFragment.class;
                    break;

                case R.id.navigation_more:
                    fragmentClass = SettingsFragment.class;
                    break;
            }

            if (fragmentClass != null) {
                replaceFragment(fragmentClass, item.getItemId());
                currentNavigationLayoutId = item.getItemId();
                return true;
            } else {
                return false;
            }

        }


    };

    private void replaceFragment(Class fragmentClass, int fragmentId) {

        Fragment fragment = null;
        //if an instance of this fragment already exists then get it
        if(fragments.containsKey(fragmentId)) {
            fragment = fragments.get(fragmentId);
        }else{
            //no instance exists so create it
            try {
                fragment = (Fragment) fragmentClass.newInstance();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

            //store the instance we created in our fragment instance map
            fragments.put(fragmentId, fragment);
        }

        //we want to hide the action bar only when the map fragment is open
//        if(fragmentClass == MapFragment.class){
//            getSupportActionBar().hide();
//        }else{
//            getSupportActionBar().show();
//        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content, fragment, "TAG").commit();



    }


    //go to the correct fragment we were previously on on resume
    @Override
    protected void onResume() {
        super.onResume();
        if(currentNavigationLayoutId != -1) {
            View view = navigation.findViewById(currentNavigationLayoutId);
            view.performClick();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //getSupportActionBar().hide();

        //intializing scan object
        qrScan = new IntentIntegrator(this);


 //       mTextMessage = (TextView) findViewById(R.id.message);

        navigation = (BottomNavigationView) findViewById(R.id.navigation);


        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        //navigation.getIt
        askForPermissions();


        //this isn't very nice, should just be able to call onNavigationItemSelected directly to do this?
        //however that doesn't play the selection animation...
        //go to map fragment at launch
        View view = navigation.findViewById(R.id.navigation_map);
        view.performClick();



        Bundle b = getIntent().getExtras();
        //get the player object we passed through from joinActivity

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.top_menu , menu); //.xml file name
        return super.onCreateOptionsMenu(menu);
    }


     // Permission method
    @TargetApi(25)

    private void askForPermissions() {
        int hasWriteContactsPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        if (hasWriteContactsPermission != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA},
                    REQUEST_CODE_ASK_PERMISSIONS);
        }
    }




//QR SCANNING / TREASURE COLLECTING

    //Getting the scan results
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            //if qrcode has nothing in it
            if (result.getContents() == null) {
                Toast.makeText(this, "Result Not Found", Toast.LENGTH_LONG).show();
            } else {
                Log.d("SCAN", result.getContents());
                //if qr contains data
//                try {

                    //nah don't think we want to convert to json

//                    //converting the data to json
//                    //      JSONObject obj = new JSONObject(result.getContents());
//                    JSONObject obj = new JSONObject();
//                    obj.put("value", result.getContents());
//
//
//                    //attempt to collect treasure with result of qr scan
                    collectTreasure(result.getContents());

                    Toast.makeText(this, result.getContents(), Toast.LENGTH_LONG).show();

                    //setting values to textviews
                    //textViewName.setText(obj.getString("value"));

//                } catch (JSONException e) {
//                    e.printStackTrace();
//                    //if control comes here
//                    //that means the encoded format not matches
//                    //in this case you can display whatever data is available on the qrcode
//                    //to a toast
//                    Toast.makeText(this, result.getContents(), Toast.LENGTH_LONG).show();
//                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void collectTreasure(String qrCode){
        LatLng latLng;
        //get current coordinates of user
        Location currentLocation = GoogleApiHelperSingleton.getInstance(getApplicationContext()).getGoogleApiHelper().getLastKnownLocation();
        if(currentLocation == null){
            //do not have location permissions
            Log.d("STATE", "no api client");
            showFailedToCollectTreasureDialog("Could not determine your location, please try again in a bit!");
            return;
        }else {
            latLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
        }



        collectingAlertDialog = new AlertDialog.Builder(MainActivity.this).create();
        //alertDialog.setTitle("Success!");
        collectingAlertDialog.setMessage("attempting to collect treasure...");
        collectingAlertDialog.show();

        ApiClient.collectTreasure(latLng, qrCode, getApplicationContext() , new Response.Listener<CollectableTreasure>() {

            @Override
            public void onResponse(CollectableTreasure treasure) {
                //successfully found treasure!
                Log.d("STATE", "success");

                showCollectSuccessful(treasure);
            }
        },new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                //failed to collect treasure

                showFailedToCollectTreasureDialog(VolleyErrorHelper.getServerConnectionError(error, true));
            }
        });
    }

    private void showCollectSuccessful(CollectableTreasure treasure){
        collectingAlertDialog.dismiss();

        String message = "";
        if(treasure.getOrder() == (SessionPlayer.getInstance(getApplicationContext()).getNumberOfTreasuresToCollection()) - 1){
            //team has found their last treasure
            message = "Congratulations you've found your last location! Keep an eye on the leaderboard to track your competition, or logout to join a new hunt!";
        }else {
            message = treasure.getFoundTime();
        }

        AlertDialog alertDialog;
        alertDialog = new AlertDialog.Builder(MainActivity.this).create();
        alertDialog.setTitle("Success!");
        alertDialog.setMessage(message);
        alertDialog.show();
    }
    private void showFailedToCollectTreasureDialog(String message){
        collectingAlertDialog.dismiss();

        AlertDialog alertDialog;
        alertDialog = new AlertDialog.Builder(MainActivity.this).create();
        alertDialog.setTitle("Fail!");
        alertDialog.setMessage(message);
        alertDialog.show();

    }

}
