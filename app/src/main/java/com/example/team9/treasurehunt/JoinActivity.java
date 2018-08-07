package com.example.team9.treasurehunt;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.team9.treasurehunt.ApiClient.ApiClient;
import com.example.team9.treasurehunt.ApiClient.VolleyErrorHelper;
import com.example.team9.treasurehunt.ApiResources.ActiveTreasureHunt;
import com.example.team9.treasurehunt.ApiResources.Player;
import com.example.team9.treasurehunt.ApiResources.Team;
import com.example.team9.treasurehunt.Runtime.SessionPlayer;

/**
 * Created by Sam/Mantas
 * Styled by Mantas
 * Api Integration - Edward
 */
public class JoinActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String EXTRA_PLAYERID = "com.example.myfirstapp.PLAYERID";
    Button join;
    EditText name;
    EditText teamCode;
    Team team;
    int numberOfTreasuresToCollect;
    View line;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().hide(); // hide action bar

        setContentView(R.layout.activity_join);
        line = (View) findViewById(R.id.lineLogin);
        join = (Button) findViewById(R.id.join);
        name = (EditText) findViewById(R.id.nametxt);
        teamCode = (EditText) findViewById(R.id.teamCodetxt);
        line.bringToFront();

        join.setOnClickListener(JoinActivity.this);

    }


    private int getPlayerId(){
        return 1;
    }

    /**
     * Called when the user taps the Send button
     */

    @Override
    public void onClick(View v) {
        //pull inputs
        EditText editText = (EditText) findViewById(R.id.nametxt);
        String name = editText.getText().toString();
        editText = (EditText) findViewById(R.id.teamCodetxt);
        String teamCode = editText.getText().toString();

        // basic validation
        if (name.equals("") || teamCode.equals("")) {
            Toast.makeText(getApplicationContext(),
                    "Enter name and team code", Toast.LENGTH_SHORT).show();
        } else {

            ApiClient.joinTeam(name, teamCode, getApplicationContext(), new Response.Listener<Player>() {

                @Override
                public void onResponse(Player player) {
                    //successfully joined team

                    //set player we just received from api as our SessionPlayer
                    SessionPlayer sessionPlayer = SessionPlayer.getInstance(JoinActivity.this);
                    sessionPlayer.setPlayer(player);

                    //
                    checkSessionActiveTreasureHuntId(player);

                    String token = player.getToken();

                    //start main activity
                    launchMainApp();
                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {

                    //if it's a server problem means the given code wasn't valid
                    if(VolleyErrorHelper.isServerProblem(error)) {
                        teamNotFoundReset();
                    }else{
                        //otherwise some kind of other error, e.g no internet
                        showServerConnectionError(error);
                    }
                }
            });


        }
    }

    private void checkSessionActiveTreasureHuntId(Player player){
        //get the team the player just joined
        ApiClient.getTeam(player.getTeamId(), getApplicationContext(), new Response.Listener<Team>() {

            @Override
            public void onResponse(Team foundTeam) {
                //successfully found team player belongs to


                //store the players team for later use
                team = foundTeam;
                numberOfTreasuresToCollect = foundTeam.getCollectableTreasures().size();

                //now we need to check whether it is still joinable
                //e.g not finished
                checkHuntIsActive(foundTeam.getActiveTreasureHuntId());

            }
        },new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                //some connection erro
                showServerConnectionError(error);
                teamNotFoundReset();
            }
        });
    }

    private void checkHuntIsActive(final int activeTreasureHuntId) {

        //check treasure hunt is still in progress
        ApiClient.getActiveTreasureHunt(activeTreasureHuntId, getApplicationContext(), new Response.Listener<ActiveTreasureHunt>() {

            @Override
            public void onResponse(ActiveTreasureHunt activeTreasureHunt) {
                //successfully found the treasure hunt the player belongs to

                if (activeTreasureHunt.getIsFinished() == 0) {

                    //set activeTreasureHuntId that this team belongs to
                    SessionPlayer.getInstance(getApplicationContext()).setActiveTreasureHuntId(activeTreasureHunt.getId());
                    SessionPlayer.getInstance(getApplicationContext()).setNumberOfTreasureToCollect(numberOfTreasuresToCollect);
                    //treasure hunt is active so launch app and let user keep playing!
                    launchMainApp();

                } else {
                    //treasure hunt is not active
                    //player can't join a hunt which has ended


                    //show alert and reset
                    //join team page on pressing ok
                    AlertDialog alertDialog;
                    AlertDialog.Builder builder = new AlertDialog.Builder(JoinActivity.this);
                    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // Handle Ok
                            teamNotFoundReset();
                        }
                    });
                    alertDialog = builder.create();
                    alertDialog.setTitle("Fail");
                    alertDialog.setMessage("treasure hunt has ended");

                    alertDialog.show();

                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                //couldn't find the active treasure hunt
                //something wrong with connceting to api
                showServerConnectionError(error);
            }
        });
    }

    private void launchMainApp(){
        if(team != null && team.getName() != null) {
            Toast.makeText(getApplicationContext(), "Joined team: " + team.getName(), Toast.LENGTH_SHORT).show();
        }
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
    private void  teamNotFoundReset(){

        Toast.makeText(getApplicationContext(), "Team not found", Toast.LENGTH_SHORT).show();
        teamCode.setText("");

    }

    private void showServerConnectionError(VolleyError volleyError) {


        String message;
        message = ApiClient.getServerConnectionError(volleyError);

        Toast.makeText(getApplicationContext(),
                message,
                Toast.LENGTH_SHORT).show();
    }
}
