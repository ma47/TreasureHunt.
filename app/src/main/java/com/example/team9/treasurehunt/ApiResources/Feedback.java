package com.example.team9.treasurehunt.ApiResources;

/**
 * Created by Edward on 01/05/2017.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * class representing the data of the feedback form
 */


public class Feedback {
    @SerializedName("id")
    @Expose
    private Integer id;

    @SerializedName("submit_time")
    @Expose
    private String submitTime;

    @SerializedName("active_treasure_hunt_id")
    @Expose
    private Integer activeTreasureHuntId;

    @SerializedName("player_token")
    @Expose
    private String playerToken;

    public String getPlayerToken() {
        return playerToken;
    }

    public void setPlayerToken(String playerToken) {
        this.playerToken = playerToken;
    }

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("question_1")
    @Expose
    private String question1;

    @SerializedName("question_2")
    @Expose
    private String question2;

    @SerializedName("question_3")
    @Expose
    private String question3;

    @SerializedName("question_4")
    @Expose
    private String question4;

    @SerializedName("question_5")
    @Expose
    private String question5;

    @SerializedName("question_6")
    @Expose
    private String question6;

    @SerializedName("question_7")
    @Expose
    private String question7;

    @SerializedName("question_8")
    @Expose
    private String question8;

    @SerializedName("question_9")
    @Expose
    private String question9;

    @SerializedName("question_10")
    @Expose
    private String question10;

    @SerializedName("question_11")
    @Expose
    private String question11;

    @SerializedName("question_12")
    @Expose
    private String question12;

    @SerializedName("question_13")
    @Expose
    private String question13;

    public String getQuestion11() {
        return question11;
    }

    public void setQuestion11(String question11) {
        this.question11 = question11;
    }

    @SerializedName("question_14")
    @Expose
    private String question14;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSubmitTime() {
        return submitTime;
    }

    public void setSubmitTime(String submitTime) {
        this.submitTime = submitTime;
    }

    public Integer getActiveTreasureHuntId() {
        return activeTreasureHuntId;
    }

    public void setActiveTreasureHuntId(Integer activeTreasureHuntId) {
        this.activeTreasureHuntId = activeTreasureHuntId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getQuestion1() {
        return question1;
    }

    public void setQuestion1(String question1) {
        this.question1 = question1;
    }

    public String getQuestion2() {
        return question2;
    }

    public void setQuestion2(String question2) {
        this.question2 = question2;
    }

    public String getQuestion3() {
        return question3;
    }

    public void setQuestion3(String question3) {
        this.question3 = question3;
    }

    public String getQuestion4() {
        return question4;
    }

    public void setQuestion4(String question4) {
        this.question4 = question4;
    }

    public String getQuestion5() {
        return question5;
    }

    public void setQuestion5(String question5) {
        this.question5 = question5;
    }

    public String getQuestion6() {
        return question6;
    }

    public void setQuestion6(String question6) {
        this.question6 = question6;
    }

    public String getQuestion7() {
        return question7;
    }

    public void setQuestion7(String question7) {
        this.question7 = question7;
    }

    public String getQuestion8() {
        return question8;
    }

    public void setQuestion8(String question8) {
        this.question8 = question8;
    }

    public String getQuestion9() {
        return question9;
    }

    public void setQuestion9(String question9) {
        this.question9 = question9;
    }

    public String getQuestion10() {
        return question10;
    }

    public void setQuestion10(String question10) {
        this.question10 = question10;
    }

    public String getQuestion12() {
        return question12;
    }

    public void setQuestion12(String question12) {
        this.question12 = question12;
    }

    public String getQuestion13() {
        return question13;
    }

    public void setQuestion13(String question13) {
        this.question13 = question13;
    }

    public String getQuestion14() {
        return question14;
    }

    public void setQuestion14(String question14) {
        this.question14 = question14;
    }
}
