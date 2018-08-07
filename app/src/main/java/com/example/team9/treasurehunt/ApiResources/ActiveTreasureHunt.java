
package com.example.team9.treasurehunt.ApiResources;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


/**
 * class representing the active_treasure_hunt resource from the api
 * see the api documentation for resource definitions
 */
public class ActiveTreasureHunt {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("is_started")
    @Expose
    private Integer isStarted;
    @SerializedName("is_finished")
    @Expose
    private Integer isFinished;
    @SerializedName("start_time")
    @Expose
    private String startTime;
    @SerializedName("finish_time")
    @Expose
    private String finishTime;
    @SerializedName("organiser_id")
    @Expose
    private Integer organiserId;
    @SerializedName("treasure_hunt_template_id")
    @Expose
    private Integer treasureHuntTemplateId;
    @SerializedName("teams")
    @Expose

    private List<Team> teams = null;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName(){ return name;}

    public void setName(String name){ this.name = name;}

    public Integer getIsStarted() {
        return isStarted;
    }

    public void setIsStarted(Integer isStarted) {
        this.isStarted = isStarted;
    }

    public Integer getIsFinished() {
        return isFinished;
    }

    public void setIsFinished(Integer isFinished) {
        this.isFinished = isFinished;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(String finishTime) {
        this.finishTime = finishTime;
    }

    public Integer getOrganiserId() {
        return organiserId;
    }

    public void setOrganiserId(Integer organiserId) {
        this.organiserId = organiserId;
    }

    public Integer getTreasureHuntTemplateId() {
        return treasureHuntTemplateId;
    }

    public void setTreasureHuntTemplateId(Integer treasureHuntTemplateId) {
        this.treasureHuntTemplateId = treasureHuntTemplateId;
    }

    public List<Team> getTeams() {
        return teams;
    }

    public void setTeams(List<Team> teams) {
        this.teams = teams;
    }

}
