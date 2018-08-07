
package com.example.team9.treasurehunt.ApiResources;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * class representing the Team resource from the api
 * see the api documentation for resource definitions
 */
public class Team {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("current_treasure_index")
    @Expose
    private Integer currentTreasureIndex;
    @SerializedName("score")
    @Expose
    private Integer score;
    @SerializedName("public_team_code")
    @Expose
    private String publicTeamCode;
    @SerializedName("max_players")
    @Expose
    private Integer maxPlayers;
    @SerializedName("active_treasure_hunt_id")
    @Expose
    private Integer activeTreasureHuntId;
    @SerializedName("players")
    @Expose
    private List<Player> players = null;
    @SerializedName("collectable_treasures")
    @Expose
    private List<CollectableTreasure> collectableTreasures = null;

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

    public Integer getCurrentTreasureIndex() {
        return currentTreasureIndex;
    }

    public void setCurrentTreasureIndex(Integer currentTreasureIndex) {
        this.currentTreasureIndex = currentTreasureIndex;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public String getPublicTeamCode() {
        return publicTeamCode;
    }

    public void setPublicTeamCode(String publicTeamCode) {
        this.publicTeamCode = publicTeamCode;
    }

    public Integer getMaxPlayers() {
        return maxPlayers;
    }

    public void setMaxPlayers(Integer maxPlayers) {
        this.maxPlayers = maxPlayers;
    }

    public Integer getActiveTreasureHuntId() {
        return activeTreasureHuntId;
    }

    public void setActiveTreasureHuntId(Integer activeTreasureHuntId) {
        this.activeTreasureHuntId = activeTreasureHuntId;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    public List<CollectableTreasure> getCollectableTreasures() {
        return collectableTreasures;
    }

    public void setCollectableTreasures(List<CollectableTreasure> collectableTreasures) {
        this.collectableTreasures = collectableTreasures;
    }

}
