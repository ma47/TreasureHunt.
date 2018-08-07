package com.example.team9.treasurehunt.ApiClient;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONObject;

/**
 * Created by Edward on 27/04/2017.
 */

public class VolleyErrorHelper {

    private static final String serverProblemMessage = "The server could not be found. Please try again after some time!!";
    private static final String networkProblemMessage = "Cannot connect to Internet...Please check your connection!";
    private static final String timeoutProblemMessage = "Connection TimeOut! Please check your internet connection.";
    private static final String genericProblemMessage = "Oops! Something went wrong!";

    public static String getServerConnectionError(VolleyError volleyError, Boolean showApiErrorMessage){

        if(isServerProblem(volleyError)){
            //I want to run handle server anyway because it logs the api error which is useful
            String messsage = handleServerError(volleyError);
            if(showApiErrorMessage){
                return messsage;
            }else{
                return serverProblemMessage;
            }

        }else if(isNetworkProblem(volleyError)){
            return networkProblemMessage;
        }else if(isTimoeutProblem(volleyError)){
            return timeoutProblemMessage;
        }

        return genericProblemMessage;

    }

    private static String handleServerError(VolleyError error) {

        //VolleyError error = (VolleyError) error;
        NetworkResponse response = error.networkResponse;
        if (response != null) {
            switch (response.statusCode) {


                case 404:   //Not Found
                case 409:   //Duplicate resource
                case 403:   //Forbidden resource
                case 422:   //unprocessable resource
                case 401:   //unauthorised
                    try {
                        // server will return error like this { "message": "Some error occured" }
                        // Use Gson to parse the result

                        String jsonString = new String(response.data);
                        JSONObject jsonObject = new JSONObject(jsonString);

                        // server will return error like this { "message": "Some error occured" }
                        // parse the result and return just the message value

                        String errorMessage = jsonObject.getString("message");

                        if (errorMessage != null) {
                            Log.d("API_ERROR", errorMessage);
                            return errorMessage;
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    // invalid request
                    return ((VolleyError) error).getMessage();

                default:
                    return serverProblemMessage;
            }
        }
        return genericProblemMessage;
    }

    public static boolean isServerProblem(VolleyError error) {
        return (error instanceof ServerError || error instanceof AuthFailureError);
    }

    public static boolean isNetworkProblem (VolleyError error){
        return (error instanceof NetworkError || error instanceof NoConnectionError);
    }

    public static boolean isTimoeutProblem (VolleyError error){
        return (error instanceof TimeoutError);
    }
}
