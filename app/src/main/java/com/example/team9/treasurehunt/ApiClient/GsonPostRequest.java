package com.example.team9.treasurehunt.ApiClient;

/**
 * Created by Edward on 23/04/2017.
 */

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonRequest;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.Map;

/**
 * Convert a JsonElement into a list of objects or an object with Google Gson.
 *
 * The JsonElement is the response object for a {@link com.android.volley.Request.Method} POST call.
 *
 * adapted from: https://developer.android.com/training/volley/request-custom.html
 */
public class GsonPostRequest<T> extends JsonRequest<T>
{
    private final Gson gson = new Gson();
    private final Type type;
    private final Response.Listener<T> listener;
    private final Map<String, String> headers;

    /**
     * Make a POST request and return a parsed object from JSON.
     *
     * @param url URL of the request to make
     * @param type is the type of the object to be returned
     * @param headers Map of request headers
     * @param listener is the listener for the right answer
     * @param errorListener  is the listener for the wrong answer
     */
    public GsonPostRequest
    (String url, String body, Type type, Map<String, String> headers,
     Response.Listener<T> listener, Response.ErrorListener errorListener)
    {
        //could ask for the body as a JsonObject, then convert it to string here
        //put tbh the request body doesn't have to be json i guess...
        //its just our api is entirely json so kind off makes sense for us
        super(Method.POST, url, body, listener, errorListener);


        this.headers = headers;
        this.type = type;
        this.listener = listener;
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        return headers != null ? headers : super.getHeaders();
    }

    @Override
    protected void deliverResponse(T response)
    {
        listener.onResponse(response);
    }

    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response)
    {
        Log.d("STATE", "PARSING");
        try
        {
            String json = new String(response.data, HttpHeaderParser.parseCharset(response.headers));

            return (Response<T>) Response.success
                    (
                            gson.fromJson(json, type),
                            HttpHeaderParser.parseCacheHeaders(response)
                    );
        }
        catch (UnsupportedEncodingException e)
        {
            return Response.error(new ParseError(e));
        }
        catch (JsonSyntaxException e)
        {
            return Response.error(new ParseError(e));
        }
    }
}
