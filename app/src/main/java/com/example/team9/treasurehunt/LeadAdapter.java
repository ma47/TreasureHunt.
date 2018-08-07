package com.example.team9.treasurehunt;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.team9.treasurehunt.ApiResources.Player;
import com.example.team9.treasurehunt.ApiResources.Team;
import com.example.team9.treasurehunt.Runtime.SessionPlayer;

import java.util.List;

/**
 * Created by Mantas
 * Modified by Edward( Team resource integration) - Edward
 */

public class LeadAdapter extends ArrayAdapter<Team>{

    private Context context;
    private int resource;
    private String response;
    private int DEFAULT_TEAM_TEXT_COLOR = Color.BLACK;
    private int PLAYERS_TEAM_TEXT_COLOR = Color.RED;



    public LeadAdapter(Context context, int resource, List<Team> teamObjects) {
        super(context, resource, teamObjects);
        this.resource=resource;
    }


    @Override
    public View getView(int position, View scoreView, ViewGroup parent) {

        LinearLayout teamView;
        Team team = getItem(position);

        if (scoreView == null) {
            teamView = new LinearLayout(getContext());
            String inflater = Context.LAYOUT_INFLATER_SERVICE;
            LayoutInflater inf;
            inf = (LayoutInflater) getContext().getSystemService(inflater);
            inf.inflate(resource, teamView, true);
        } else {
            teamView = (LinearLayout) scoreView;
        }

        TextView teamName = (TextView) teamView.findViewById(R.id.list_team);
        TextView score = (TextView) teamView.findViewById(R.id.list_score);


        teamName.setText(team.getName());
        score.setText(team.getScore().toString());


        Player userPlayer = SessionPlayer.getInstance(getContext()).getPlayer();


        if(userPlayer.getTeamId().equals(team.getId())){
            //this is the current players team
            //change their font colour
            teamName.setTextColor(PLAYERS_TEAM_TEXT_COLOR);
            score.setTextColor(PLAYERS_TEAM_TEXT_COLOR);
        }else{
            teamName.setTextColor(DEFAULT_TEAM_TEXT_COLOR);
            score.setTextColor(DEFAULT_TEAM_TEXT_COLOR);
        }

        return teamView;
    }


}
