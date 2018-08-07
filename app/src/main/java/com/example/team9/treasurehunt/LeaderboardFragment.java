package com.example.team9.treasurehunt;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.team9.treasurehunt.ApiClient.ApiClient;
import com.example.team9.treasurehunt.ApiClient.VolleyErrorHelper;
import com.example.team9.treasurehunt.ApiResources.ActiveTreasureHunt;
import com.example.team9.treasurehunt.ApiResources.Player;
import com.example.team9.treasurehunt.ApiResources.Team;
import com.example.team9.treasurehunt.Runtime.SessionPlayer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by: Mantas
 *
 * Api integration: Edward
 *
 * Styling - Mantas(ha, so much style)
 */

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LeaderboardFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LeaderboardFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private List<Team> leaderboardTeams = new ArrayList<Team>();
    private LeadAdapter leaderboardAdapater;
    SwipeRefreshLayout swipeRefreshLayout;


    public LeaderboardFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LeaderboardFragment.
     */


    // TODO: Rename and change types and number of parameters
    public static LeaderboardFragment newInstance(String param1, String param2) {
        LeaderboardFragment fragment = new LeaderboardFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getActivity().setTitle("Leaderboard");
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        setHasOptionsMenu(true);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            //pressing the action bar refreshes button also refreshes the page
            case R.id.action_refresh:
                // signal SwipeRefreshLayout to start the refresh progress indicator
                setLeaderboardFromAPI();
                swipeRefreshLayout.setRefreshing(true);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_leaderboard, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ListView teamView = (ListView) view.findViewById(R.id.list);

        leaderboardAdapater = new LeadAdapter(this.getActivity(),R.layout.listrow_item, leaderboardTeams);

        teamView.setAdapter(leaderboardAdapater);

        //setup our swipe to refresh
        swipeRefreshLayout = (SwipeRefreshLayout) getView().findViewById(R.id.leaderboard_swipe_refresh);


        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                //update clues from api on refresh
                setLeaderboardFromAPI();
            }
        });


        //update list on first opening the fragment
        swipeRefreshLayout.setRefreshing(true);
        setLeaderboardFromAPI();

    }

    public void onStart(){
        super.onStart();

        //setLeaderboardFromAPI();
    }


    @Override
    public void onStop() {
        super.onStop();

        swipeRefreshLayout.setRefreshing(false);
    }

    //sets the clue list from treasure the team this player belongs to needs to find
    private void setLeaderboardFromAPI(){

        Log.d("STATE", "SET LEADERBOARD");
        Player player = SessionPlayer.getInstance(getContext()).getPlayer();
        int activeTreasureHuntId = SessionPlayer.getInstance(getContext()).getActiveTreasureHuntId();

        ApiClient.getActiveTreasureHunt(activeTreasureHuntId, getContext(), new Response.Listener<ActiveTreasureHunt>() {

            @Override
            public void onResponse(ActiveTreasureHunt activeTreasureHunt) {
                //successfully got team from api

                populateLeaderboardListFromTeams(activeTreasureHunt.getTeams());

            }
        },new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                //couldn't get teams from server for some reason
                //display some error text
                showServerConnectionError(error);

            }
        });
    }

    private void showServerConnectionError(VolleyError volleyError){
        //update leaderbaord list request succeeded so stop refresh icon
        swipeRefreshLayout.setRefreshing(false);

        //if the api calls ends whilst this fragment is not opened then we
        //have some problems where the context is null, getActivity()
        //also doesn't fix the problem but i feel like it should...
        if(getContext() != null) {
            String message;
            message = VolleyErrorHelper.getServerConnectionError(volleyError, true);

            Toast.makeText(getContext(),
                    message,
                    Toast.LENGTH_SHORT).show();
        }

    }

    //populates clue list with clues from an array of collectable treasures
    private void populateLeaderboardListFromTeams(List<Team> teams){

        List<Team> latestLeaderboardTeams = new ArrayList<Team>();
        Log.d("STATE", "populate leaderboard");
        leaderboardAdapater.clear();

        //make sure treasures are in order
        //newest to oldest, aka descending order based on order field
        Collections.sort(teams, new Comparator<Team>() {
            public int compare(Team team1, Team team2) {
                return team2.getScore().compareTo(team1.getScore());
            }
        });

        for(Team team : teams){
            //can only see clues for treasures the players team have already found
            //or the treasure they currently looking for
            Log.d("STATE", "populate leaderboard with:" + team.getName());
            //leaderboardAdapater.add(team);
            latestLeaderboardTeams.add(team);
        }

        //update leaderboard with new teams
        leaderboardTeams.addAll(latestLeaderboardTeams);

        leaderboardAdapater.notifyDataSetChanged();

        //update leaderbaord list request succeeded so stop refresh icon
        swipeRefreshLayout.setRefreshing(false);

    }

}
