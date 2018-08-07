package com.example.team9.treasurehunt;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.team9.treasurehunt.ApiClient.ApiClient;
import com.example.team9.treasurehunt.ApiClient.VolleyErrorHelper;
import com.example.team9.treasurehunt.ApiResources.CollectableTreasure;
import com.example.team9.treasurehunt.ApiResources.Player;
import com.example.team9.treasurehunt.ApiResources.Team;
import com.example.team9.treasurehunt.Runtime.SessionPlayer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Edward, Mantas
 *
 *
 *
 * Api Integration - Edward
 */

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CluesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CluesFragment extends Fragment {
    List<String> clueHolder = new ArrayList<String>();
    ArrayAdapter arrayAdapter;
    ClueAdapter adapterClue;
    SwipeRefreshLayout swipeRefreshLayout;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;



    public CluesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CluesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CluesFragment newInstance(String param1, String param2) {
        CluesFragment fragment = new CluesFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.getActivity().setTitle("Clues"); // Title change

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        setHasOptionsMenu(true);
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            //pressing the action bar refreshes button also refreshes the page
            case R.id.action_refresh:
                // signal SwipeRefreshLayout to start the refresh progress indicator
                setCluesFromAPI();
                swipeRefreshLayout.setRefreshing(true);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }



    @Override
    public void onStart(){
        super.onStart();

        //setCluesFromAPI();
        //ApiClient.setClueList(this.getActivity().getApplicationContext(), arrayAdapter);
        setCluesFromAPI();
    }

    @Override
    public void onStop() {
        super.onStop();

        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout containing a title and body text.

        ViewGroup rootView = (ViewGroup) inflater
                .inflate(R.layout.fragment_clues, container, false);



        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //setContentView(R.layout.list_item);
        ListView clueView = (ListView) view.findViewById(R.id.listViewClues);

//        arrayAdapter = new ArrayAdapter<>
//                (this.getActivity(), R.layout.cluelist_item, list_clue_item, clueHolder);
//        clueView.setAdapter(arrayAdapter);

        adapterClue = new ClueAdapter(this.getActivity(), R.layout.cluelist_item, clueHolder);
        clueView.setAdapter(adapterClue);


        //setup our swipe to refresh
        swipeRefreshLayout = (SwipeRefreshLayout) getView().findViewById(R.id.clues_swipe_refresh);


        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                //update clues from api on refresh
                setCluesFromAPI();
            }
        });

        //update clues on first opening fragment
        swipeRefreshLayout.setRefreshing(true);
        setCluesFromAPI();
    }



    //sets the clue list from treasure the team this player belongs to needs to find
    private void setCluesFromAPI(){

        //swipe refresh indicator will show untill the clue request ends
        //either fails or succeeds

        Player player = SessionPlayer.getInstance(getContext()).getPlayer();
        ApiClient.getTeam(player.getTeamId(), getContext(), new Response.Listener<Team>() {

            @Override
            public void onResponse(Team team) {
                //successfully got team from api


                //update clues list with any new clues
                populateCluesListFromTreasures(team.getCollectableTreasures());


            }
        },new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                //couldn't get clues from server for some reason

                NetworkResponse response = error.networkResponse;

                //
                swipeRefreshLayout.setRefreshing(false);

                //display some error text
                showServerConnectionError(error);
            }
        });

    }


    private void showServerConnectionError(VolleyError volleyError){
        //if the api calls ends whilst this fragment is not opened then we
        //have some problems where the context is null, getActivity()
        //also doesn't fix the problem but i feel like it should...
        if(getActivity() != null) {
            String message;
            message = VolleyErrorHelper.getServerConnectionError(volleyError, true);

            Toast.makeText(getActivity(),
                    message,
                    Toast.LENGTH_SHORT).show();
        }

        //request finished so stop refresh icon
        swipeRefreshLayout.setRefreshing(false);

    }
    //populates clue list with clues from an array of collectable treasures
    private void populateCluesListFromTreasures(List<CollectableTreasure> treasures){

        List<String> latestClues = new ArrayList<String>();
        //arrayAdapter.clear();

        //add current clues to dataholder in descending order

        //make sure treasures are in order
        //latest to oldest, aka descending order based on order field

        Collections.sort(treasures, new Comparator<CollectableTreasure>() {
            public int compare(CollectableTreasure t1, CollectableTreasure t2) {
                return t2.getOrder().compareTo(t1.getOrder());
            }
        });

        for(CollectableTreasure treasure : treasures){
            //can only see clues for treasures the players team have already found
            //or the treasure they currently looking for
            if(treasure.getClue() != null) {
                Log.d("STATE", "clue:" + treasure.getClue());
                latestClues.add(treasure.getClue());
            }
        }

        Log.d("STATE", "number of clues:" + latestClues.size());
        Log.d("STATE", "current number of clues:" + clueHolder.size());
        //check if we have new clues
        int numberOfNewClues = latestClues.size() - clueHolder.size();
        Log.d("STATE", "number of new clues:" + numberOfNewClues);
        if(numberOfNewClues > 0) {
            //if we do at the new ones to the clues list
            for(int i = 0 ; i < numberOfNewClues; i++){
                Log.d("STATE", "add to list" + latestClues.get(i));
                clueHolder.add(i, latestClues.get(i));
                //arrayAdapter.add(clueHolder.get(i));
            }
            adapterClue.notifyDataSetChanged();
        }

        //request successfully so finish refreshing icon
        swipeRefreshLayout.setRefreshing(false);
    }
}
