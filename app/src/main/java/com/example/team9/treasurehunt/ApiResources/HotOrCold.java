package com.example.team9.treasurehunt.ApiResources;

/**
 * Created by Edward on 30/04/2017.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * class representing the player resource from the api
 * see the api documentation for resource definitions
 */

public class HotOrCold {

    @SerializedName("hot_or_cold")
    @Expose
    private double hotOrCold;

    public double getHotOrCold() {
        return hotOrCold;
    }

    public void setHotOrCold(double hotOrCold) {
        this.hotOrCold = hotOrCold;
    }
}
