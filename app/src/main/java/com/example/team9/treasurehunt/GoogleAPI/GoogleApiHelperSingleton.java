package com.example.team9.treasurehunt.GoogleAPI;

import android.app.Activity;
import android.content.Context;

import com.google.android.gms.location.LocationListener;

/**
 * Created by Edward on 25/04/2017.
 */

public class GoogleApiHelperSingleton {
    private static GoogleApiHelperSingleton instance;
    private GoogleApiHelper googleApiHelper;

    private GoogleApiHelperSingleton(Context context){
        googleApiHelper = new GoogleApiHelper(context);
    }

    public static GoogleApiHelperSingleton getInstance(Context context){
        if(instance == null){
            instance = new GoogleApiHelperSingleton(context);
        }
        return instance;
    }

    public GoogleApiHelper getGoogleApiHelper(){
        return googleApiHelper;
    }




}
