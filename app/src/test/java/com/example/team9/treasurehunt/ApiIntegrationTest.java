package com.example.team9.treasurehunt;

import android.content.Context;
import android.os.AsyncTask;
import android.test.AndroidTestCase;
import android.test.mock.MockContext;
import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.RequestFuture;
import com.example.team9.treasurehunt.ApiClient.ApiClient;
import com.example.team9.treasurehunt.ApiClient.VolleyErrorHelper;
import com.example.team9.treasurehunt.ApiResources.ActiveTreasureHunt;
import com.example.team9.treasurehunt.ApiResources.CollectableTreasure;
import com.example.team9.treasurehunt.ApiResources.Player;
import com.example.team9.treasurehunt.ApiResources.Team;


import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;


import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Timer;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static org.junit.Assert.*;

/**
 * Created by Edward on 29/04/2017.
 * We're using Robolectric test runner as it makes it easy to test volley requests to our api
 * inside our development environment without requiring an emulator.#
 *
 */



 /*
 * THESE TESTS AREN'T WRITTEN CORRECTLY, WILL ALWAYS PASS, PLEASE IGNORE!
 * I struggled to test volley, for some reason the onResponse callback never got called
 * I think its a problem with robolectrics RuntimeEnvironment.application context
 */

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class ApiIntegrationTest {


    Context context;


    @Before
    public void setUp() throws Exception{
        context = new MockContext();

        assertNotNull(context);
    }

    /**
     * helper functions
     **/

    private class AsyncVolleyRequestFuture extends AsyncTask<Object, Player, Player> {

        @Override
        protected Player doInBackground(Object... params) {

            final String playerName = "test_name3";
            String teamCode = "wyWt";

            Context context = (Context)params[0];

            Player player = null;
            RequestFuture<Player> future = RequestFuture.newFuture();

            ApiClient.joinTeam(playerName, teamCode, context, future, future);

            try {
                player = future.get(20, TimeUnit.SECONDS); // this will block
            } catch (InterruptedException e) {
                // exception handling
            } catch (ExecutionException e) {
                // exception handling
            }catch (TimeoutException e) {
                // exception handling
                System.out.println("nope");

            }

            return player;
        }

//        @Override
//        protected void onPostExecute(String result) {
//
//        }
//
//        @Override
//        protected void onPreExecute() {}
//
//        @Override
//        protected void onProgressUpdate(Void... values) {}
    }

    /**
     * will join a player of given name to team identified by team code
     * will then get the team the player joined and execute either
     * teamResponseListener on success or teamErrorListener on fail
     */
    public void getTeamFromPlayer(String playerName, String teamCode, final Response.Listener<Team> teamResponseListener){

        //executes when get team request fails
        final Response.ErrorListener teamErrorListener = new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                //request failed
                //log the reason for failure
                Log.d("Request_Fail", VolleyErrorHelper.getServerConnectionError(error, true));

                //test fails
                fail("get team request failed");

            }
        };



        //we use a team code to join a team as a player
        //and use the information returned as part of the player resource
        //to get the team resource the player belongs to
        ApiClient.joinTeam(playerName, teamCode, RuntimeEnvironment.application, new Response.Listener<Player>() {

            @Override
            public void onResponse(Player player) {
                //request was successful


                //get the players team
                ApiClient.getTeam(player.getTeamId(), RuntimeEnvironment.application,  teamResponseListener, teamErrorListener);



            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                //request failed
                //log the reason for failure
                Log.d("Request_Fail", VolleyErrorHelper.getServerConnectionError(error, true));

                //test fails
                fail("failed to join team");
            }
        });
    }

    /**
     * attempt to get the active hunt the player is participating in.
     * execute the activeHuntResponseListener on success.
     * we have to join team as player, get the team this player belongs to
     * then get the active hunt the team belongs too
     */

    public void getActiveHuntFromPlayer(String playerName, String teamCode, final Response.Listener<ActiveTreasureHunt> activeHuntResponseListener){


        //executes when get active hunt request fails, we will want to just fail the test
        //at this point as there is nothing else to do
        final Response.ErrorListener activeHuntErrorListener = new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                //request failed
                //log the reason for failure
                Log.d("Request_Fail", VolleyErrorHelper.getServerConnectionError(error, true));

                //test fails
                fail("get active treasure hunt request failed");

            }
        };



        //executes when get team request is successful
        final Response.Listener<Team> teamResponseListener = new Response.Listener<Team>() {

            @Override
            public void onResponse(Team team) {
                //get team request was successful
                //get the active hunt this team belongs too
                ApiClient.getActiveTreasureHunt(team.getActiveTreasureHuntId(), RuntimeEnvironment.application, activeHuntResponseListener, activeHuntErrorListener );

            }
        };

        //execute the get team request and use the team response listener on success
        getTeamFromPlayer(playerName, teamCode, teamResponseListener);

    }

    /**
     * TESTS
     */



    @Test
    /**
     * can use a valid team code to join a team and get back the player resource created
     */
    public void canJoinTeamWithValidCode() throws Exception {


        final String playerName = "test_name3";
        String teamCode = "wyWt";
        final int timeout = 20;

        //final CountDownLatch countDownLatch = new CountDownLatch(1);
        final Object[] responseHolder = new Object[1];

        Player player = null;
        System.out.println("TEST");


        try {
            player = new AsyncVolleyRequestFuture().execute(RuntimeEnvironment.application.getApplicationContext()).get(); // this will block
        } catch (InterruptedException e) {
            // exception handling
        } catch (ExecutionException e) {
            // exception handling
//        }catch (TimeoutException e) {
//            // exception handling
//            fail("API request timed out");
//        }

            System.out.println(player.getName());

            if (player != null) {
                assertEquals(player.getName(), playerName);
            }

            fail("request failed, player was null");

            fail("request did not return player resource");
//


//        ApiClient.joinTeam(playerName, teamCode, RuntimeEnvironment.application.getApplicationContext(), new Response.Listener<Player>() {
//
//            @Override
//            public void onResponse(Player player) {
//                //request was successful
//                //check player is not null, and the returned player resource name
//                //should be the same as the one we set
//                System.out.println("TEST");
//                responseHolder[0] = player;
//                countDownLatch.countDown();
//
//                if (player != null) {
//                    assertEquals(player.getName(), playerName);
//                }
//
//                fail("request did not return player resource");
//
//
//            }
//        }, new Response.ErrorListener() {
//
//            @Override
//            public void onErrorResponse(VolleyError error) {
//
//                responseHolder[0] = error;
//                countDownLatch.countDown();
//
//                //request failed
//                //log the reason for failure
//                Log.d("Request_Fail", VolleyErrorHelper.getServerConnectionError(error, true));
//
//                //test fails
//                fail("join team request failed");
//            }
//        });
//
//        try {
//            countDownLatch.await();
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }
//        if (responseHolder[0] instanceof VolleyError) {
//
//            final VolleyError volleyError = (VolleyError) responseHolder[0];
//            fail("failed to join team");
//            //TODO: Handle error...
//        } else {
//            final Player player = (Player) responseHolder[0];
//
//            if (player != null) {
//                assertEquals(player.getName(), playerName);
//            }
//
//            fail("request did not return player resource");
//
//            //TODO: Handle response...
//        }


//
//        fail("request timed out after " + timeout + " seconds");
        }
    }


    /**
     * can use a valid team code to join a team and get back the player resource created
     * as a player object
     */
    @Test
    public void cannotJoinTeamWithInvalidCode() throws Exception {


        final String playerName = "test_name";
        //not a valid code
        String teamCode = "YYYYY";



        ApiClient.joinTeam(playerName, teamCode, RuntimeEnvironment.application, new Response.Listener<Player>() {

            @Override
            public void onResponse(Player player) {
                //request was successful but should not have been
                //should have returned some 400 error

                fail("join team request was successful with invalid code");


            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                System.out.println("TEST");
                //request failed as code was not a correct code
                if(VolleyErrorHelper.isServerProblem(error)){
                    //request failed due to some 400 error, means code was not a correct code
                    assert true;
                }

                //if it failed for some other reason it means the server was unreachable
                //which should not be the case. check your internet connection!

                fail("request failed for some non server related reason:" + VolleyErrorHelper.getServerConnectionError(error, true));

            }
        });
    }

    @Test
    /**
     * test we can get the team the player belongs to
     * we have to join team as player, then get the team this player belongs to
     */
    public void canGetTeam(){
        Player userPlayer;

        final String PLAYER_NAME = "test_name";
        //I know this code matches to a team with name "team01"
        final String EXPECTED_TEAM_NAME = "team01";
        final String TEAM_CODE = "wyWt";

        //we have to join team as player, then get the team this player belongs to


        //executes when get team request is successful
        final Response.Listener<Team> teamResponseListener = new Response.Listener<Team>() {

            @Override
            public void onResponse(Team team) {
                //get team request was successful
                //team name should be what we expected
                System.out.println("TEST");

                assertEquals(EXPECTED_TEAM_NAME, team.getName() );

            }
        };

        getTeamFromPlayer(PLAYER_NAME, TEAM_CODE, teamResponseListener);


    }


    @Test
    /**
     * test we can get the active hunt the player is participating in
     * we have to join team as player, get the team this player belongs to
     * then get the active hunt the team belongs too
     */

    public void canGetActiveTreasureHunt(){
        final String PLAYER_NAME = "test_name";

        //I know the active hunt the team participates in has this name (see test_active_hunt_newcastle.json)
        final String EXPECTED_ACTIVE_HUNT_NAME = "App Test Hunt";
        final String TEAM_CODE = "wyWt";


        //executes when get active hunt request is successful
        final Response.Listener<ActiveTreasureHunt> activeHuntResponseListener = new Response.Listener<ActiveTreasureHunt>() {

            @Override
            public void onResponse(ActiveTreasureHunt activeTreasureHunt) {
                //get team request was successful
                //active hunt name should be what we expected
                assertEquals(EXPECTED_ACTIVE_HUNT_NAME, activeTreasureHunt.getName() );

            }
        };

        //attempt to get active hunt from player and execute out activeHuntResponseListener on a successful request
        getActiveHuntFromPlayer(PLAYER_NAME, TEAM_CODE, activeHuntResponseListener);



    }

    /**
     * we should be able to see limited information about all treasures the team has to find
     * test the team contains the expected number of collectable_treasures
     * we have to join team as player, then get the team this player belongs to
     * then go through the collectable_treasures.
     */

    @Test
    public void canGetCollectableTreasure(){
        final String PLAYER_NAME = "test_name";
        //I know this code matches to a team with name "team01"

        final int EXPECTED_NUMBER_OF_TREASURES = 5; //see test_active_hunt_newcastle.json
        final String TEAM_CODE = "wyWt";

        //executes when get team request is successful
        final Response.Listener<Team> teamResponseListener = new Response.Listener<Team>() {

            @Override
            public void onResponse(Team team) {
                //get team request was successful
                //should have expected number of treasure
                assertEquals(EXPECTED_NUMBER_OF_TREASURES, team.getCollectableTreasures().size() );

            }
        };


        getTeamFromPlayer(PLAYER_NAME, TEAM_CODE, teamResponseListener);
    }
    /**
     * test we can see the clues the player should be able to see
     * we have to join team as player, then get the team this player belongs to
     * then go through the collectable_treasures of the team and read the clues of each treasure
     */

    public void userCanSeeClues(){

    }

    /**
     * team has been set up so no treasures have been found yet
     * meaning team should be able to see one clue (their first clue)
     * and no more
     */
    @Test
    public void userCanSeeCurrentClue(){
        final String PLAYER_NAME = "test_name";
        //I know this code matches to a team with name "team01"

        final int EXPECTED_NUMBER_OF_ClUES = 1; //see test_active_hunt_newcastle.json
        final String EXPECTED_CLUE_TEXT = "Claremont Tower";
        final String TEAM_CODE = "wyWt";

        //executes when get team request is successful
        final Response.Listener<Team> teamResponseListener = new Response.Listener<Team>() {

            @Override
            public void onResponse(Team team) {
                //get team request was successful
                //should have expected number of treasure
                int numberOfClues = 0;
                for(CollectableTreasure treasure : team.getCollectableTreasures()){
                    if(treasure.getClue() != null){
                        numberOfClues++;
                    }
                }


                if(numberOfClues != EXPECTED_NUMBER_OF_ClUES){
                    //this will fail
                    assertEquals(EXPECTED_NUMBER_OF_ClUES, numberOfClues);
                }

                //the one clue we can see should have the expected text
                assertEquals(EXPECTED_CLUE_TEXT, team.getCollectableTreasures().get(0) );

            }
        };

        getTeamFromPlayer(PLAYER_NAME, TEAM_CODE, teamResponseListener);
    }

    /**
     * team has been set up so 2 treasures have been found
     * meaning team should be able to see 3 clues (the two they have found, and one they are looking for)
     * and no more
     */
    @Test
    public void userCanSeePastClues(){
        final String PLAYER_NAME = "test_name";
        //I know this code matches to a team with name "team01"

        final int EXPECTED_NUMBER_OF_ClUES = 1; //see test_active_hunt_newcastle.json
        final String EXPECTED_CLUE1_TEXT = "Claremont Tower";
        final String EXPECTED_CLUE2_TEXT = "Library";
        final String EXPECTED_CLUE3_TEXT = "Students Union";
        final String[] EXPECTED_CLUES_TEXT = {"Claremont Tower", "Library", "Students Union" };
        //final String TEAM_CODE = "ghRp";
        final String TEAM_CODE = "wyWt";

        System.out.println("TEST");

        //executes when get team request is successful
        final Response.Listener<Team> teamResponseListener = new Response.Listener<Team>() {

            @Override
            public void onResponse(Team team) {
                //get team request was successful
                //should have expected number of treasure

                System.out.println("TEST");

                List<CollectableTreasure> treasures = team.getCollectableTreasures();

                //the api doesn't guarantee any sort of order so
                //sort in ascending order based on treasure order in hunt
                //first clue to be found at start of list 2nd clue at 2nd etc

                Collections.sort(treasures, new Comparator<CollectableTreasure>() {
                    public int compare(CollectableTreasure t1, CollectableTreasure t2) {
                        return t1.getOrder().compareTo(t2.getOrder());
                    }
                });

                int numberOfClues = 0;

                String[] clues = new String[5];
                for(CollectableTreasure treasure : team.getCollectableTreasures()){

                    clues[numberOfClues] = treasure.getClue();
                    Log.d("STATE", clues[numberOfClues]);
                    System.out.println(clues[numberOfClues]);
                    if(treasure.getClue() != null){

                    }
                    numberOfClues++;
                }

//                if(numberOfClues != EXPECTED_NUMBER_OF_ClUES){
//                    //this will fail
//                    assertEquals(EXPECTED_NUMBER_OF_ClUES, numberOfClues);
//                }


                //the 3 clues we can see should have the expected text
                assertArrayEquals(EXPECTED_CLUES_TEXT, clues);


            }
        };

        getTeamFromPlayer(PLAYER_NAME, TEAM_CODE, teamResponseListener);
    }

    /**
     * team has been set up so 0 treasures have been found
     * meaning team should NOT be able to see clues for treasure 2,3,4 and 5
     *
     */
    public void userCannotSeeFutureClues(){

    }

    /**
     * players should be able to view the score and name of other teams in the same
     * treasure hunt as them
     *
     */
    public void userCanSeeHuntTeams(){

    }

    /**
     * players should NOT be able to view the collectable treasures of other teams in the same
     * treasure hunt as them
     *
     */
    public void userCannotSeeHuntTeamsTreasures(){

    }
}
