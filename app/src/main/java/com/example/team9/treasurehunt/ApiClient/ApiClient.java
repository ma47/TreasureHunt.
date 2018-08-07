package com.example.team9.treasurehunt.ApiClient;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.util.ArrayMap;
import android.util.Log;
import android.widget.ArrayAdapter;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.team9.treasurehunt.ApiResources.ActiveTreasureHunt;
import com.example.team9.treasurehunt.ApiResources.CollectableTreasure;
import com.example.team9.treasurehunt.ApiResources.Feedback;
import com.example.team9.treasurehunt.ApiResources.HotOrCold;
import com.example.team9.treasurehunt.ApiResources.Player;
import com.example.team9.treasurehunt.ApiResources.Team;
import com.example.team9.treasurehunt.Runtime.SessionPlayer;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Edward on 14/02/2017.
 */

public class ApiClient {

   final static String urlRoot = "http://homepages.cs.ncl.ac.uk/2016-17/csc2022_team09/api/api.php/v1";

    public static List<String> setClueList(Context context, final ArrayAdapter arrayAdapter) {
        final List<String> clueList = new ArrayList<String>();

        //RequestQueue queue = ClientRequestQueue.getInstance(this.getApplicationContext()).
        //       getRequestQueue();
        String url = "http://homepages.cs.ncl.ac.uk/2016-17/csc2022_team09/api/api.php/db/clues";
        Log.d("STATE", "TEST ");
        JsonArrayRequest jsArrayRequest = new JsonArrayRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
                        for (int i = 0; i < response.length(); i++) {

                            try {
                                String clueText = ((JSONObject) response.get(i)).get("content").toString();
                                arrayAdapter.add(clueText);
                                //clueList.add(clueText);
                                arrayAdapter.notifyDataSetChanged();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }


                        Log.d("STATE", "HTTP REQUEST SUCCESS: " + response.toString());

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {


                        Log.d("STATE", "HTTP REQUEST FAILED ");

                    }
                });

        ClientRequestQueue.getInstance(context).addToRequestQueue(jsArrayRequest);
        //queue.add(jsArrayRequest);


        return clueList;


    }

    /**
     * creates a new player belonging to team specified by the public_team_code
     * and returns the player just created as a Player object in the listenerResponse
     */
    public static void joinTeam(String name, String publicTeamCode, Context context, Response.Listener<Player> listenerResponse,  Response.ErrorListener listenerError){
        //from url we use to join team
        String url = urlRoot + "/teams/jointeam";

        JsonObject jsonRequestBody = new JsonObject();
        //JSONObject jsonRequestBody = new JSONObject();

        //form the correct request body
        //this can be found in the documentation
        //for the jointeam endpoint
        jsonRequestBody.addProperty("name", name);
        jsonRequestBody.addProperty("public_team_code", publicTeamCode);

        GsonPostRequest<Player> playerObjectRequest = new GsonPostRequest<>(url, jsonRequestBody.toString(), Player.class, null, listenerResponse, listenerError);

        //JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonRequestBody, listenerResponse, listenerError);

        ClientRequestQueue.getInstance(context).addToRequestQueue(playerObjectRequest);

    }

    /**
     * creates a new player belonging to team specified by the public_team_code
     * and returns the player just created as a Player object in the listenerResponse
     */
    public static void getHotOrCold(LatLng latLng, Context context, Response.Listener<HotOrCold> listenerResponse,  Response.ErrorListener listenerError){

        int teamId = SessionPlayer.getInstance(context).getPlayer().getTeamId();
        int activeTreasureHuntId =  SessionPlayer.getInstance(context).getActiveTreasureHuntId();

        //from url we use to join team
        String url = urlRoot + "/active_treasure_hunts/" + activeTreasureHuntId + "/teams/" + teamId + "/hot_or_cold";

        JsonObject jsonRequestBody = new JsonObject();
        //JSONObject jsonRequestBody = new JSONObject();

        //set auth header
        Map<String, String> headers = getAuthHeader(context);


        //form the correct request body
        //this can be found in the documentation
        //for the hot_or_cold endpoint
        jsonRequestBody.addProperty("latitude", latLng.latitude );
        jsonRequestBody.addProperty("longitude", latLng.longitude);

        GsonPostRequest<HotOrCold> playerObjectRequest = new GsonPostRequest<>(url, jsonRequestBody.toString(), HotOrCold.class, headers, listenerResponse, listenerError);

        //JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonRequestBody, listenerResponse, listenerError);

        ClientRequestQueue.getInstance(context).addToRequestQueue(playerObjectRequest);

    }

    private static  Map<String, String> getAuthHeader(Context context){
        String AUTH_HEADER_NAME = "Php-Auth-Token";
        Map<String, String> headers = new ArrayMap<String, String>();

        Player userPlayer = SessionPlayer.getInstance(context).getPlayer();

        headers.put(AUTH_HEADER_NAME, userPlayer.getToken());
        return headers;
    }

    public static void getActiveTreasureHunt(int activeTreasureHuntId, Context context, Response.Listener<ActiveTreasureHunt> listenerResponse, Response.ErrorListener listenerError){
        String url = urlRoot + "/active_treasure_hunts/" + activeTreasureHuntId;

        Map<String, String> headers = getAuthHeader(context);

        GsonGetRequest<ActiveTreasureHunt> playerObjectRequest = new GsonGetRequest<>(url, ActiveTreasureHunt.class, headers, listenerResponse, listenerError);

        ClientRequestQueue.getInstance(context).addToRequestQueue(playerObjectRequest);
    }

    public static void getTeamsInHunt(int activeTreasureHuntId, Context context){
        String url = urlRoot + "/teams/";
    }

    public static void getTeam(int teamId, Context context, Response.Listener<Team> listenerResponse,  Response.ErrorListener listenerError){
        String url = urlRoot + "/teams/" + teamId;

        Map<String, String> headers = getAuthHeader(context);

        GsonGetRequest<Team> playerObjectRequest = new GsonGetRequest<>(url, Team.class, headers, listenerResponse, listenerError);

        ClientRequestQueue.getInstance(context).addToRequestQueue(playerObjectRequest);
    }

    public static void getTreasures(){
        int playerId = 0;
        int playerToken = 0;

    }

    public static void collectTreasure(LatLng latLng, String qrCode, Context context,
                                       Response.Listener<CollectableTreasure> listenerResponse, Response.ErrorListener listenerError){
        SessionPlayer sessionPlayer = SessionPlayer.getInstance(context);
        int teamId = sessionPlayer.getPlayer().getTeamId();
        int activeTreasureHuntId = sessionPlayer.getActiveTreasureHuntId();

        String url = urlRoot + "/active_treasure_hunts/" + activeTreasureHuntId +  "/teams/" + teamId + "/collect_treasure";

        Map<String, String> headers = getAuthHeader(context);

        JsonObject jsonRequestBody = new JsonObject();
        //JSONObject jsonRequestBody = new JSONObject();

        //form the correct request body
        //this can be found in the documentation
        //for the collect_treasure endpoint
        jsonRequestBody.addProperty("latitude", latLng.latitude );
        jsonRequestBody.addProperty("longitude", latLng.longitude);
        jsonRequestBody.addProperty("qr_code", qrCode);

        GsonPostRequest<CollectableTreasure> collectTreasureRequest = new GsonPostRequest<>(url, jsonRequestBody.toString(), CollectableTreasure.class, headers, listenerResponse, listenerError);


        DefaultRetryPolicy retryPolicy = new DefaultRetryPolicy(10000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        collectTreasureRequest.setRetryPolicy(retryPolicy);
        //JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonRequestBody, listenerResponse, listenerError);

        Log.d("STATE", "collect treasure request");
        ClientRequestQueue.getInstance(context).addToRequestQueue(collectTreasureRequest);

    }



    public static String getServerConnectionError(VolleyError volleyError){
        String message;

        if (volleyError instanceof NetworkError) {
            message = "Cannot connect to Internet...Please check your connection!";
        } else if (volleyError instanceof ServerError) {
            message = "The server could not be found. Please try again after some time!!";
        } else if (volleyError instanceof AuthFailureError) {
            message = "Cannot connect to Internet...Please check your connection!";
        } else if (volleyError instanceof ParseError) {
            message = "Parsing error! Please try again after some time!!";
        } else if (volleyError instanceof NoConnectionError) {
            message = "Cannot connect to Internet...Please check your connection!";
        } else if (volleyError instanceof TimeoutError) {
            message = "Connection TimeOut! Please check your internet connection.";
        }else
        {
            message = "Oops! Something went wrong!";
        }

        return message;
    }

    public static void sendFeedback(Feedback feedback, Context context,
                                    Response.Listener<Feedback> listenerResponse, Response.ErrorListener listenerError){

        SessionPlayer sessionPlayer = SessionPlayer.getInstance(context);
        int teamId = sessionPlayer.getPlayer().getTeamId();
        int activeTreasureHuntId = sessionPlayer.getActiveTreasureHuntId();

        feedback.setActiveTreasureHuntId(activeTreasureHuntId);
        feedback.setPlayerToken(sessionPlayer.getPlayer().getToken());

        String url = urlRoot + "/feedback";

        Map<String, String> headers = getAuthHeader(context);

        Gson gson = new Gson();

        String feedbackAsJsonString = gson.toJson(feedback);


        GsonPostRequest<Feedback> collectTreasureRequest = new GsonPostRequest<>(url, feedbackAsJsonString, Feedback.class, headers, listenerResponse, listenerError);


        Log.d("STATE", "collect treasure request");
        ClientRequestQueue.getInstance(context).addToRequestQueue(collectTreasureRequest);

    }

    //
//
//        url ="http://homepages.cs.ncl.ac.uk/2016-17/csc2022_team09/api/api.php/db/clues/1";
//
//        JsonObjectRequest jsObjRequest = new JsonObjectRequest
//                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
//
//                    @Override
//                    public void onResponse(JSONObject response) {
//
//
//                        clueHolder.add(response.toString());
//                        arrayAdapter.notifyDataSetChanged();
//                        Log.d("STATE", "HTTP REQUEST SUCCESS: " + response.toString() );
//
//                    }
//                }, new Response.ErrorListener() {
//
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//
//
//                        Log.d("STATE", "HTTP REQUEST FAILED ");
//
//                    }
//                });
//        queue.add(jsObjRequest);

}
