package com.smartapps.mlara.gymbuddy.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.smartapps.mlara.gymbuddy.R;
import com.smartapps.mlara.gymbuddy.activities.AddSportActivity;
import com.smartapps.mlara.gymbuddy.activities.EditSportActivity;
import com.smartapps.mlara.gymbuddy.adapters.MySportListAdapter;
import com.smartapps.mlara.gymbuddy.pojos.Sport;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import util.Constants;

/**
 * Created by mlara on 10/8/2015.
 */
public class ProfileTabMySportsFragment extends Fragment{

    private Button addSportButton;
    private ListView listOfSportsView;
    private Context context;
    private List<Sport> sportList = new ArrayList<Sport>();
    private HashMap<String,String> positionPerSport;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_editprofile_mysports_tab, container, false);
        addSportButton = (Button) view.findViewById(R.id.newSportButton);
        addSportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddSportActivity.class);
                startActivity(intent);
            }
        });

        listOfSportsView = (ListView) view.findViewById(R.id.mySportsList);

        sportList.clear();
        positionPerSport=new HashMap<>();
        String userName = ParseUser.getCurrentUser().getUsername();
        //getting all the sports for that user.
        ParseQuery<ParseObject> query = ParseQuery.getQuery(Constants.sportTable);
        query.whereEqualTo(Constants.userName, userName);
        final String finalUserName = userName;
        Log.v("CHECK", "finalUserName is " + finalUserName);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                Log.v("CHECK", "The number of sports found is " + list.size());
                processSportsFound(finalUserName, list);
                Log.v("CHECK", "Size of sport list after calling processSportsFound is " + sportList.size());

                //getting all the positions for the player in that sport
                for (int k = 0; k < sportList.size(); k++) {
                    Log.v("CHECK", "Processing positions");
                    ParseQuery<ParseObject> query2 = ParseQuery.getQuery(Constants.positionTable);
                    query2.whereEqualTo(Constants.userName, finalUserName);
                    query2.whereEqualTo(Constants.sportName, sportList.get(k).getSportName());
                    query2.findInBackground(new FindCallback<ParseObject>() {
                        @Override
                        public void done(List<ParseObject> list, ParseException e) {
                            Log.v("CHECK", "Number of positions found " + list.size());
                            processPositionsFoundForThatSport(list);
                            Log.v("CHECK", "Size of sport list after calling processSportsFound1 and positionSportFound is " + sportList.size());
                            listOfSportsView.setAdapter(new MySportListAdapter(context, R.layout.sport_list, sportList));
                            printSportsOfUser(sportList);
                        }
                    });
                }
            }
        });


/*       listOfSportsView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
           @Override
           public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
               //Intent intent =new Intent(context, EditSportActivity.class);

               Sport sport = (Sport) listOfSportsView.getItemAtPosition(position);
               String sportSelectedName = sport.getSportName();
               Toast.makeText(context,
                       "The sport selected is " + sportSelectedName,
                       Toast.LENGTH_LONG).show();

           }

           @Override
           public void onNothingSelected(AdapterView<?> parent) {

           }
       });*/

        listOfSportsView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Sport sport = (Sport) listOfSportsView.getItemAtPosition(position);
                Intent intent =new Intent(context, EditSportActivity.class);
                intent.putExtra(Constants.selectedSport,sport.getSportName());
                startActivity(intent);
                //String sportSelectedName = sport.getSportName();
               // Log.v("CHECK","Inside OnItemClick of list with sport "+sportSelectedName);
              /*  Toast.makeText(context,
                        "The sport selected is " + sportSelectedName,
                        Toast.LENGTH_LONG).show();*/

            }
        });
        return view;
    }


    public void processSportsFound(String finalUserName, List<ParseObject> listOfSports) {
        for (int count = 0; count < listOfSports.size(); count++) {
            Log.v("CHECK","Inside process sports found for loop ");
            Sport sportToBeAdded = new Sport(finalUserName, listOfSports.get(count).getString(Constants.sportName),
                    listOfSports.get(count).getString(Constants.yearsOfExperience), listOfSports.get(count).getString(Constants.levelOfExpertise));
            sportList.add(sportToBeAdded);
        }
    }

    public void processPositionsFoundForThatSport( List<ParseObject> listOfPositions){
        for(int count=0;count<sportList.size();count++){
           for(int index=0;index<listOfPositions.size();index++){
               if(listOfPositions.get(index).get(Constants.sportName).equals(sportList.get(count).getSportName())){
                   Log.v("CHECK","Adding position to sport");
                   sportList.get(count).getPositions().add((String) listOfPositions.get(index).get(Constants.positionShortDescription));
               }
           }
        }
        Log.v("CHECK","Done processing position found for that sport");
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity().getApplicationContext();
       /* ProgressBar progressBar = new ProgressBar(context);
        progressBar.setLayoutParams(new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        progressBar.setIndeterminate(true);
        getListView().setEmptyView(progressBar);

        // Must add the progress bar to the root of the layout
        ViewGroup root = (ViewGroup) getListView().findViewById(android.R.id.content);
        root.addView(progressBar);*/
    }



   /* public ArrayList<Sport> getAllSportsForUser() {
         ArrayList<Sport> sportList = new ArrayList<Sport>();
        // first getting all the sports that the user has
        String userName = "";
        userName = ParseUser.getCurrentUser().getUsername();
        ParseQuery<ParseObject> query = ParseQuery.getQuery(Constants.sportTable);
        query.whereEqualTo(Constants.userName, userName);
        final String finalUserName = userName;
        Log.v("CHECK","finalUserName is "+finalUserName);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                for (int k = 0; k < list.size(); k++) {
                    Log.v("CHECK", "sport at position k is " + list.get(k).getString(Constants.sportName));
                    final Sport sportToBeAdded = new Sport(finalUserName, list.get(k).getString(Constants.sportName),
                            list.get(k).getString(Constants.yearsOfExperience), list.get(k).getString(Constants.levelOfExpertise));
                    //getting all the positions for the player in that sport
                    ParseQuery<ParseObject> query2 = ParseQuery.getQuery(Constants.positionTable);
                    query2.whereEqualTo(Constants.userName, finalUserName);
                    query2.whereEqualTo(Constants.sportName, sportToBeAdded.getSportName());
                    query2.findInBackground(new FindCallback<ParseObject>() {
                        @Override
                        public void done(List<ParseObject> list, ParseException e) {
                            for (int j = 0; j < list.size(); j++) {
                                sportToBeAdded.getPositions().add((String) list.get(j).get(Constants.positionShortDescription));
                            }
                        }
                    });
                    sportList.add(sportToBeAdded);
                }
            }

        });

        Log.v("CHECK", "sportList.sportToBeAdded name"+ sportList.get(0).getSportName());
        return sportList;
    }
*/

    public void printSportsOfUser(List<Sport> sportList) {
        Log.v("CHECK", "Inside printSportsOfUser about to print This number of sports:" + sportList.size());
        for (int i = 0; i < sportList.size(); i++) {
            Log.v("CHECK", "sport at position i is" + (String) sportList.get(i).getSportName());
        }
    }

}