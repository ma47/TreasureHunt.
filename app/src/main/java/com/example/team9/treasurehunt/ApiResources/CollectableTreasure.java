
package com.example.team9.treasurehunt.ApiResources;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * class representing the collectable_treasure resource from the api
 * see the api documentation for resource definitions
 */
public class CollectableTreasure {

    @SerializedName("team_id")
    @Expose
    private Integer teamId;
    @SerializedName("clue")
    @Expose
    private String clue;
    @SerializedName("latitude")
    @Expose
    private Double latitude;
    @SerializedName("longitude")
    @Expose
    private Double longitude;
    @SerializedName("has_been_found")
    @Expose
    private Integer hasBeenFound;
    @SerializedName("order")
    @Expose
    private Integer order;
    @SerializedName("score")
    @Expose
    private Integer score;
    @SerializedName("found_time")
    @Expose
    private String foundTime;
    @SerializedName("qr_code")
    @Expose
    private String qrCode;

    public Integer getTeamId() {
        return teamId;
    }

    public void setTeamId(Integer teamId) {
        this.teamId = teamId;
    }

    public String getClue() {
        return clue;
    }

    public void setClue(String clue) {
        this.clue = clue;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Integer getHasBeenFound() {
        return hasBeenFound;
    }

    public void setHasBeenFound(Integer hasBeenFound) {
        this.hasBeenFound = hasBeenFound;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public String getFoundTime() {
        return foundTime;
    }

    public void setFoundTime(String foundTime) {
        this.foundTime = foundTime;
    }

    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }

}
