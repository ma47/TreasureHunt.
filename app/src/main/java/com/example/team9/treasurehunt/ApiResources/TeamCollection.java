
package com.example.team9.treasurehunt.ApiResources;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * class representing a collection of team resources from the api
 * see the api documentation for resource definitions
 */
public class TeamCollection {

    @SerializedName("teams")
    @Expose
    private List<Team> teams = null;

    public List<Team> getTeams() {
        return teams;
    }

    public void setActiveTreasureHunts(List<Team> teams) {
        this.teams = teams;
    }

}
