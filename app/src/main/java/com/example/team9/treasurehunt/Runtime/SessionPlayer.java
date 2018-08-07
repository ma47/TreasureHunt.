package com.example.team9.treasurehunt.Runtime;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.example.team9.treasurehunt.ApiResources.Player;
import com.google.gson.Gson;

/**
 * Created by Mantas
 *  -modified by Edward
 */

/**
 * SessionPlayer singleton
 * handles getting the player the user is currently
 * logged in as and getting/storing this information to and from
 * shared preferences
 */
public class SessionPlayer {

    private static SessionPlayer sessionPlayerInstance;

    private SharedPreferences playerPref;
    private static final String PLAYER_PREF = "PLAYER_PREF";
    private final String SESSION_PLAYER_STORAGE_NAME = "SESSION_PLAYER";
    private final String ACTIVE_HUNT_ID_STORAGE_NAME = "SESSION_ACTIVE_HUNT_ID";
    private final String NUMBER_OF_TREASURES_STORAGE_NAME = "SESSION_NUMBER_OF_TREASURE";


    //represents the player the user is currently logged in as /
    //participating in the hunt as
    private Player player;
    private int activeTreasureHuntId = -1;
    private int numberOfTreasureToCollect = -1;


    public static SessionPlayer  getInstance(Context context) {
        if(sessionPlayerInstance == null){
            sessionPlayerInstance = new SessionPlayer(context);
        }
        return sessionPlayerInstance;

    }

    private SessionPlayer(Context context) {

        playerPref = context.getSharedPreferences(PLAYER_PREF, Activity.MODE_PRIVATE);



        player = readPlayer();


        activeTreasureHuntId = read(ACTIVE_HUNT_ID_STORAGE_NAME, -1);

        numberOfTreasureToCollect = read(NUMBER_OF_TREASURES_STORAGE_NAME, -1);

    }

    public Player getPlayer(){
        return player;
    }

    public void setPlayer(Player player){
        this.player = player;
        writePlayer();
    }

    public void setActiveTreasureHuntId(int activeTreasureHuntId){
        this.activeTreasureHuntId = activeTreasureHuntId;
        write(ACTIVE_HUNT_ID_STORAGE_NAME, this.activeTreasureHuntId);
    }

    public int getActiveTreasureHuntId(){
        return activeTreasureHuntId;
    }

    public void setNumberOfTreasureToCollect(int numberOfTreasureToCollect){
        this.numberOfTreasureToCollect = numberOfTreasureToCollect;
        write(NUMBER_OF_TREASURES_STORAGE_NAME, this.numberOfTreasureToCollect);
    }

    public int getNumberOfTreasuresToCollection(){ return numberOfTreasureToCollect; }

    //use to end the session, if the player logs out
    //or the hunt ends
    public void endSession(){

        removePlayer();
        deletePref(ACTIVE_HUNT_ID_STORAGE_NAME);
        deletePref(NUMBER_OF_TREASURES_STORAGE_NAME);
    }

    //writes player into shared preferences storage
    private void writePlayer(){
        Gson gson = new Gson();
        //convert Player object to json to store as string
        String sessionPlayerAsJson = gson.toJson(player);

        //write player json string into shared preferences
        write(SESSION_PLAYER_STORAGE_NAME, sessionPlayerAsJson);
    }

    //gets player object from shared preferences storage
    private Player readPlayer(){
        Gson gson = new Gson();
        //get player stored as json string from shared preferences
        String playerJson =  read(SESSION_PLAYER_STORAGE_NAME, "DEFAULT");

        if(playerJson.equals("DEFAULT")){
            //player is not set
            //this means no session is active
            //user has not joined a team
            return null;
        }

        //convert player stored as json to Player object
        Player player = gson.fromJson(playerJson, Player.class);
        return player;
    }

    //remove player object from shared preferences storage
    private void removePlayer(){
       deletePref(SESSION_PLAYER_STORAGE_NAME);
    }


    //get string value by key from shared prefs
    private String read(String key, String defaultValue) {
        return playerPref.getString(key, defaultValue);
    }

    //write key with string value into shared prefs
    private  void write(String key, String value) {
        SharedPreferences.Editor editor = playerPref.edit();
        editor.putString(key, value);
        editor.apply();
    }
//
//
    private Integer read(String key, int defaultValue) {
        return playerPref.getInt(key, defaultValue);
    }

    private void write(String key, Integer value) {
        SharedPreferences.Editor editor = playerPref.edit();
        editor.putInt(key, value);
        editor.apply();
    }
//
    private void deletePref(String key) {
        SharedPreferences.Editor editor = playerPref.edit();
        editor.remove(key);
        editor.apply();
    }

}
