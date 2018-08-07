package com.example.team9.treasurehunt;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.team9.treasurehunt.ApiClient.ApiClient;
import com.example.team9.treasurehunt.ApiClient.VolleyErrorHelper;
import com.example.team9.treasurehunt.ApiResources.ActiveTreasureHunt;
import com.example.team9.treasurehunt.ApiResources.Player;
import com.example.team9.treasurehunt.Runtime.SessionPlayer;

/**
 * Created by Edward
 * Api Integration - Edward
 */
public class StartActivity extends AppCompatActivity implements View.OnClickListener {

    private Button retryButton;
    private TextView startingTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        getSupportActionBar().hide();

        startingTextView = (TextView) findViewById(R.id.startingTextView);

        retryButton = (Button) findViewById(R.id.retryButton);
        retryButton.setOnClickListener(this);

        loginUser();
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.retryButton:
                loginUser();
                break;
        }
    }

    /**
     * attempts to log in the user using saved SessionPlayer details if they exist
     * if successful will start the main app
     * if unsuccessful will bring user to join team screen
     */
    private void loginUser(){


        //hide retry button if visible
        retryButton.setVisibility(View.GONE);
        //showing loading text
        startingTextView.setVisibility(View.VISIBLE);

        SessionPlayer sessionPlayer = SessionPlayer.getInstance(this);
        Player player = sessionPlayer.getPlayer();

        if(player == null){

            //allow them to join a team
            startJoinTeamActivity();

        }else{
            //check the hunt the player is participating in is still active
            int activeHuntId = sessionPlayer.getActiveTreasureHuntId();

            //if user succesfully joined team and set player in SessionPlayer but failed to set the activeHuntId
            //this state could occur where activeHuntId is -1
            if(activeHuntId == -1){
                startJoinTeamActivity();
            }else {

                checkPlayerHuntIsActive(activeHuntId);
            }
        }
    }

    private void checkPlayerHuntIsActive(final int activeTreasureHuntId) {
        Log.d("STATE", "testStart");
        //check treasure hunt is still in progress
        ApiClient.getActiveTreasureHunt(activeTreasureHuntId, getApplicationContext(), new Response.Listener<ActiveTreasureHunt>() {

            @Override
            public void onResponse(ActiveTreasureHunt activeTreasureHunt) {
                //successfully found the treasure hunt the player belongs to

                if (activeTreasureHunt.getIsFinished() == 0) {
                    //treasure hunt is active so launch app and let user keep playing!

                    startMainAppActivity();

                } else {
                    //treasure hunt is not active

                    //end the previous player session as its no longer valid
                    SessionPlayer.getInstance(StartActivity.this).endSession();

                    showHuntEndedDialog();


                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                //couldn't find the active treasure hunt
                //something wrong with connceting to api


                //404 / server error = active_treasure_hunt deleted
                //should go to join hunt screen
                if(VolleyErrorHelper.isServerProblem(error)){
                    //if the active treasure hunt the user is trying to rejoin has been deleted this case will occur
                    //delete the users session
                    SessionPlayer.getInstance(StartActivity.this).endSession();
                    //bring them to join team screen
                    showHuntEndedDialog();
                }else {

                    //anything else, should have option to try to connect again
                    connectionFailed();
                }

            }
        });
    }

    private void connectionFailed(){
        AlertDialog alertDialog;
        Builder builder = new AlertDialog.Builder(StartActivity.this);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // Handle Ok
                //hide starting text
                startingTextView.setVisibility(View.GONE);

                //show retry button
                retryButton = (Button) findViewById(R.id.retryButton);
                retryButton.setVisibility(View.VISIBLE);
            }
        });

        alertDialog = builder.create();
        alertDialog.setTitle("Error");
        alertDialog.setMessage("could not connect to server");

        alertDialog.show();

    }

    private void showHuntEndedDialog(){
        //show alert and bring
        //to join team page on pressing ok

        AlertDialog alertDialog;
        Builder builder = new AlertDialog.Builder(StartActivity.this);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // Handle Ok
                startJoinTeamActivity();
            }
        });
        alertDialog = builder.create();
        alertDialog.setTitle("Note");
        alertDialog.setMessage("treasure hunt has ended");

        alertDialog.show();
    }
    private void startJoinTeamActivity(){
        this.finish();
        Intent intent = new Intent(this, JoinActivity.class);
        startActivity(intent);
    }

    private void startMainAppActivity(){
        this.finish();

        //show the name of the player the user just joined the hunt as
        String playerName = SessionPlayer.getInstance(getApplicationContext()).getPlayer().getName();
        Toast.makeText(getApplicationContext(), "Joined as: " + playerName, Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
