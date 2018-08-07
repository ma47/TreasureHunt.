package com.example.team9.treasurehunt;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.team9.treasurehunt.ApiClient.ApiClient;
import com.example.team9.treasurehunt.ApiClient.VolleyErrorHelper;
import com.example.team9.treasurehunt.ApiResources.CollectableTreasure;
import com.example.team9.treasurehunt.GoogleAPI.GoogleApiHelperSingleton;
import com.example.team9.treasurehunt.Runtime.SessionPlayer;
import com.google.android.gms.maps.model.LatLng;



/**
 * Created by Mantas
 * logout + treasure collection - Edward
 */

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SettingsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingsFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private AlertDialog collectingAlertDialog;


    public SettingsFragment() {
        // Required empty public constructor
    }

    public static SettingsFragment newInstance(String param1, String param2) {
        SettingsFragment fragment = new SettingsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getActivity().setTitle("More");
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        setHasOptionsMenu(true);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        //hide refresh nothing to refresh on the settings page
        menu.findItem(R.id.action_refresh).setVisible(false);
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        Button aboutUsBtn = (Button)view.findViewById(R.id.aboutUs);
        Button logoutButton = (Button)view.findViewById(R.id.logout);
        Button feedBackButton = (Button)view.findViewById(R.id.feedback);
        Button collectTreasureButton = (Button)view.findViewById(R.id.collectTreasureButton);

        aboutUsBtn.setOnClickListener(this);
        logoutButton.setOnClickListener(this);
        feedBackButton.setOnClickListener(this);
        collectTreasureButton.setOnClickListener(this);

        return view;
    }
// Button functionality -- Go to webpage and contact us via email

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.aboutUs:
                Intent viewIntent =
                        new Intent("android.intent.action.VIEW",
                                Uri.parse("http://homepages.cs.ncl.ac.uk/2016-17/csc2022_team09/"));
                startActivity(viewIntent);
                break;

            case R.id.feedback:
                Intent feedbackpage = new Intent(SettingsFragment.this.getActivity(),FeedbackFragment.class);
                startActivity(feedbackpage);
                break;

            case R.id.logout:



                //build dialog to show on succesfull logout
                final AlertDialog successfulLogoutAlertDialog;
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                successfulLogoutAlertDialog = builder.create();
                //alertDialog.setTitle("Note");
                successfulLogoutAlertDialog.setMessage("Successfully logged out");


                //show confirmation alert
                final AlertDialog alertDialog;
                builder = new AlertDialog.Builder(getContext());
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        //user confirmed they want to logout

                        successfulLogoutAlertDialog.show();

                        //end session
                        SessionPlayer.getInstance(getContext()).endSession();

                        //start join team activity
                        Intent intent = new Intent(getActivity(), JoinActivity.class);
                        startActivity(intent);
                    }
                });

                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //do not logout, dismiss alert
                        dialog.dismiss();
                    }
                });

                //show confirmation dialog
                alertDialog = builder.create();
                alertDialog.setTitle("Confirm");
                alertDialog.setMessage("Are you sure you want to logout? You will not be able to log back in as the same player once you have left.");

                alertDialog.show();

                break;
            case R.id.collectTreasureButton:
                collectTreasure("test");
                break;
        }
    }

    private void collectTreasure(String qrCode){
        LatLng latLng;
        //get current coordinates of user
        Location currentLocation = GoogleApiHelperSingleton.getInstance(getContext()).getGoogleApiHelper().getLastKnownLocation();
        if(currentLocation == null){
            //do not have location permissions
            Log.d("STATE", "no api client");
            showFailedToCollectTreasureDialog("Could not determine your location, please try again in a bit!");
            return;
        }else {
            latLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
        }



        collectingAlertDialog = new AlertDialog.Builder(getContext()).create();
        //alertDialog.setTitle("Success!");
        collectingAlertDialog.setMessage("attempting to collect treasure...");
        collectingAlertDialog.show();

        ApiClient.collectTreasure(latLng, qrCode, getActivity() , new Response.Listener<CollectableTreasure>() {

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
        if(treasure.getOrder() == (SessionPlayer.getInstance(getContext()).getNumberOfTreasuresToCollection()) - 1){
            //team has found their last treasure
            message = "Congratulations you've found your last location! Keep an eye on the leaderboard to track your competition, or logout to join a new hunt!";
        }else {
            message = treasure.getFoundTime();
        }

        AlertDialog alertDialog;
        alertDialog = new AlertDialog.Builder(getContext()).create();
        alertDialog.setTitle("Success!");
        alertDialog.setMessage(message);
        alertDialog.show();
    }
    private void showFailedToCollectTreasureDialog(String message){
        collectingAlertDialog.dismiss();

        AlertDialog alertDialog;
        alertDialog = new AlertDialog.Builder(getContext()).create();
        alertDialog.setTitle("Fail!");
        alertDialog.setMessage(message);
        alertDialog.show();

    }
}
