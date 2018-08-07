
package com.example.team9.treasurehunt.ApiResources;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * class representing the player resource from the api
 * see the api documentation for resource definitions
 */
public class Player {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("team_id")
    @Expose
    private Integer teamId;
    @SerializedName("token")
    @Expose
    private String token;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getTeamId() {
        return teamId;
    }

    public void setTeamId(Integer teamId) {
        this.teamId = teamId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

}
