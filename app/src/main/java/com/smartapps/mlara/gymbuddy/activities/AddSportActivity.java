package com.smartapps.mlara.gymbuddy.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.parse.ParseObject;
import com.parse.ParseUser;
import com.smartapps.mlara.gymbuddy.R;

import java.util.ArrayList;
import java.util.Arrays;

import util.Constants;

/**
 * Created by mlara on 10/26/2015.
 */
public class AddSportActivity extends ActionBarActivity {


    private ProgressDialog progressDialog;
    private Spinner sportNameSpinner;
    private EditText otherSportEditText;
    private ImageButton addPositionButton;
    private EditText positionInSportEditText;
    private Spinner yearsOfExperienceSpinner;
    private Spinner levelOfExpertiseSpinner;
    private CheckBox mondayCheckBox, tuesdayCheckBox, wednesdayCheckBox, thursdayCheckBox, fridayCheckBox, saturdayCheckBox, sundayCheckBox;
    private ArrayList<Integer> mSelectedItems;
    private String[] positionsInSoccer;
    private String[] positionsInBaseball;
    boolean[] checkedPositionValues;
    boolean cancelSportSave;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_sport);
        cancelSportSave = false;
        positionInSportEditText = (EditText) findViewById(R.id.positionInSport);
        mSelectedItems = new ArrayList();
        checkedPositionValues=new boolean[0];
        sportNameSpinner = (Spinner) findViewById(R.id.sportName);
        yearsOfExperienceSpinner = (Spinner) findViewById(R.id.yearsSpinner);
        levelOfExpertiseSpinner = (Spinner) findViewById(R.id.experienceSpinner);
        otherSportEditText = (EditText) findViewById(R.id.otherSport);
        addPositionButton = (ImageButton) findViewById(R.id.addPositionButton);
        Resources resources = getResources();
        positionsInSoccer = resources.getStringArray(R.array.positionSoccerArray);
        positionsInBaseball = resources.getStringArray(R.array.positionBaseballArray);
        //setting up the add position dialog
        addPositionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(AddSportActivity.this);
                // Set the dialog title
                builder.setTitle(R.string.selectPosition);
                //get the name of the sport to filter the list of positions by it.
                final String sportName = sportNameSpinner.getSelectedItem().toString();
                //soccer positions
                if (sportName.equals(Constants.soccer)) {
                    checkedPositionValues = new boolean[positionsInSoccer.length];
                        //this for loop will check the positions on the dialog that the user had already chosen.
                        for (int k = 0; k < mSelectedItems.size(); k++) {
                        checkedPositionValues[mSelectedItems.get(k)] = true;
                    }
                    builder.setMultiChoiceItems(R.array.positionSoccerArray, checkedPositionValues,
                            new DialogInterface.OnMultiChoiceClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which,
                                                    boolean isChecked) {
                                    if (isChecked) {
                                        // If the user checked the item, add it to the selected items
                                        mSelectedItems.add(which);
                                    } else if (mSelectedItems.contains(which)) {
                                        // Else, if the item is already in the array, remove it
                                        mSelectedItems.remove(Integer.valueOf(which));
                                    }
                                }
                            });
               /* }*/
                //baseball positions
                } else if (sportName.equals(Constants.baseball)) {
                    checkedPositionValues = new boolean[positionsInBaseball.length];
                    for (int k = 0; k < mSelectedItems.size(); k++) {
                        checkedPositionValues[mSelectedItems.get(k)] = true;
                    }
                    builder.setMultiChoiceItems(R.array.positionBaseballArray, checkedPositionValues,
                            new DialogInterface.OnMultiChoiceClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which,
                                                    boolean isChecked) {
                                    if (isChecked) {
                                        // If the user checked the item, add it to the selected items
                                        mSelectedItems.add(which);
                                    } else if (mSelectedItems.contains(which)) {
                                        // Else, if the item is already in the array, remove it
                                        mSelectedItems.remove(Integer.valueOf(which));
                                    }
                                }
                            });
               /* }*/
                }
                //add other sports positions here

                // Set the action buttons
                builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // User clicked OK, so save the mSelectedItems results somewhere
                        // or return them to the component that opened the dialog

                        //first delete the previous values from the position edit text
                        positionInSportEditText.setText("");
                        for (int i = 0; i < mSelectedItems.size(); i++) {
                            if (i == 0) {
                                if(sportName.equals(Constants.soccer)){
                                    positionInSportEditText.append(positionsInSoccer[mSelectedItems.get(i)]);
                                }
                                else if(sportName.equals(Constants.baseball)){
                                    positionInSportEditText.append(positionsInBaseball[mSelectedItems.get(i)]);
                                }
                            } else if (i > 0) {
                                if(sportName.equals(Constants.soccer)){
                                    positionInSportEditText.append(" , " + positionsInSoccer[mSelectedItems.get(i)]);
                                } else if(sportName.equals(Constants.baseball)){
                                    positionInSportEditText.append(" , " + positionsInBaseball[mSelectedItems.get(i)]);
                                }
                            }
                        }

                    }
                });
                builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });
                builder.create().show();
            }

        });

        sportNameSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedSport = parent.getSelectedItem().toString();

                //clear out the values of the position because the sport has changed.
                positionInSportEditText.setText("");
                mSelectedItems.clear();
                //reset the chosen values of the array because sport has changed
                Arrays.fill(checkedPositionValues, false);

                if (selectedSport.equals(Constants.otherSport)) {
                    otherSportEditText.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        //elements of ActionBar
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setTitle(Constants.addSport);

        //elements of save progress bar
        progressDialog = new ProgressDialog(AddSportActivity.this);
        progressDialog.setTitle(Constants.pleaseWait);
        progressDialog.setMessage(Constants.updatingProfile);
        progressDialog.setCancelable(false);

        //availability
        mondayCheckBox = (CheckBox) findViewById(R.id.mondayCheckBox);
        tuesdayCheckBox = (CheckBox) findViewById(R.id.tuesdayCheckBox);
        wednesdayCheckBox = (CheckBox) findViewById(R.id.wednesdayCheckBox);
        thursdayCheckBox = (CheckBox) findViewById(R.id.thursdayCheckBox);
        fridayCheckBox = (CheckBox) findViewById(R.id.fridayCheckBox);
        saturdayCheckBox = (CheckBox) findViewById(R.id.satCheckBox);
        sundayCheckBox = (CheckBox) findViewById(R.id.sunCheckBox);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.edit_profile_actionbar_actions, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            //save new sport to profile and go back to my sport tab
            //save availibilty to class
            case R.id.action_save:
                progressDialog.show();
                cancelSportSave = validateSportSave();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            if(cancelSportSave==false){
                                ParseUser currentUser = ParseUser.getCurrentUser();
                                if (currentUser != null) {
                                    //probably need to check if that user does not have that sport added before saving or removing the sport
                                    //from the spinner options if found in the sport table!

                                    //saving the sport first
                                    ParseObject sportToBeAdded=new ParseObject((Constants.sportTable));
                                    sportToBeAdded.put(Constants.userName,currentUser.getUsername());
                                    sportToBeAdded.put(Constants.sportName,sportNameSpinner.getSelectedItem().toString());
                                    sportToBeAdded.put(Constants.levelOfExpertise,levelOfExpertiseSpinner.getSelectedItem().toString());
                                    sportToBeAdded.put(Constants.yearsOfExperience, yearsOfExperienceSpinner.getSelectedItem().toString());
                                    //sportToBeAdded.saveInBackground();

                                    //saving the position for that sport if a position has been added to the text view
                                    if(!TextUtils.equals(positionInSportEditText.getText().toString(),Constants.addPosition)){
                                    /*  String[]positionsArray=TextUtils.split(" , ",positionInSportEditText.getText().toString().trim());*/
                                        String[]positionsArray=positionInSportEditText.getText().toString().trim().split(",");
                                        Log.v("CHECK","positionInSportEditText is "+positionInSportEditText.getText().toString().trim());
                                        for (int k=0;k<positionsArray.length;k++){
                                            Log.v("CHECK","Position at index " + k + " is " + positionsArray[k].trim());
                                        }
                                    }
                                    //go back to sport list

                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        progressDialog.dismiss();
                    }
                }).start();
                return true;


            //Cancel add of new sport and go back to MYsports fragment
            case R.id.action_cancel:


                return true;

            default:
                return super.onOptionsItemSelected(item);
        }


    }

    public boolean validateSportSave(){
        boolean cancelSave=false;
        View focusView = null;
        //checking if a sport has been selected
        if(TextUtils.equals(sportNameSpinner.getSelectedItem().toString(),Constants.selectSport)){
            TextView errorText = (TextView)sportNameSpinner.getSelectedView();
            errorText.setError("");
            errorText.setTextColor(Color.RED);//just to highlight that this is an error
            cancelSave=true;
            focusView=errorText;
            focusView.requestFocus();
        }

        return cancelSave;
    }


}




