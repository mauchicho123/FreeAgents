package com.smartapps.mlara.gymbuddy.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.smartapps.mlara.gymbuddy.R;

import java.util.ArrayList;
import java.util.List;

import util.Constants;

/**
 * Created by mlara on 1/7/2016.
 */
public class EditSportActivity extends ActionBarActivity {

    private TextView editSportTextView;
    private EditText editPositionInSportEditText;
    private String[] positionsInSoccer;
    private String[] positionsInBaseball;
    private ImageButton addPositionButton;
    private ArrayList<String> positionsFound;
    boolean[] checkedPositionValues;
    private ArrayList<Integer> mSelectedItems;
    private Spinner editSportYearsSpinner;
    private Spinner editSportExperienceSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_sport);
        Resources resources = getResources();
        Intent intent = getIntent();


        //assigning the sport name to the text field
        final String sportSelected = intent.getStringExtra(Constants.selectedSport);
        editSportTextView = (TextView) findViewById(R.id.editSportTextView);
        editSportTextView.setText(sportSelected);
        editPositionInSportEditText = (EditText) findViewById(R.id.editPositionInSport);
        addPositionButton = (ImageButton) findViewById((R.id.editAddPositionButton));
        editSportYearsSpinner=(Spinner) findViewById(R.id.editSportYearsSpinner);
        editSportExperienceSpinner=(Spinner) findViewById(R.id.editSportExperienceSpinner);
        mSelectedItems = new ArrayList();
        checkedPositionValues = new boolean[0];

        positionsFound = new ArrayList<>();

        positionsInSoccer = resources.getStringArray(R.array.positionSoccerArray);
        positionsInBaseball = resources.getStringArray(R.array.positionBaseballArray);

        //first getting logged user
        ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser != null) {
            //get all position to the sport
            ParseQuery<ParseObject> query = ParseQuery.getQuery(Constants.positionTable);
            query.whereEqualTo(Constants.userName, currentUser.getUsername());
            query.whereEqualTo(Constants.sportName, sportSelected);
            query.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> list, ParseException e) {
                    for (int i = 0; i < list.size(); i++) {
                        positionsFound.add((String) list.get(i).get(Constants.position));
                        //adding the existing positions to the position text field
                        if (i == 0) {
                            editPositionInSportEditText.append((String) list.get(i).get(Constants.position));
                        } else {
                            editPositionInSportEditText.append(" , " + list.get(i).get(Constants.position));
                        }
                    }
                }
            });

            //get the level of expertise and years of experience
            ParseQuery<ParseObject> querySportTable= ParseQuery.getQuery(Constants.sportTable);
            querySportTable.whereEqualTo(Constants.userName,currentUser.getUsername());
            querySportTable.whereEqualTo(Constants.sportName,sportSelected);
            querySportTable.getFirstInBackground(new GetCallback<ParseObject>() {
                @Override
                public void done(ParseObject parseObject, ParseException e) {
                    Log.v("CHECK","THE parseObject.get(Constants.levelOfExpertise) is "+parseObject.get(Constants.levelOfExpertise));
                    Log.v("CHECK","parseObject.get(Constants.yearsOfExperience) is "+parseObject.get(Constants.yearsOfExperience));
                    Log.v("CHECK","THE editSportYearsSpinner.getAdapter()).getPosition(parseObject.get(Constants.levelOfExpertise) is "+((ArrayAdapter) editSportYearsSpinner.getAdapter()).getPosition(parseObject.get(Constants.levelOfExpertise).toString()));
                    Log.v("CHECK", "parseObject.get(Constants.yearsOfExperience) is " + ((ArrayAdapter) editSportExperienceSpinner.getAdapter()).getPosition(parseObject.get(Constants.yearsOfExperience)));



                    editSportYearsSpinner.setSelection(((ArrayAdapter) editSportYearsSpinner.getAdapter()).getPosition(parseObject.get(Constants.levelOfExpertise)));
                    editSportExperienceSpinner.setSelection(((ArrayAdapter) editSportExperienceSpinner.getAdapter()).getPosition(parseObject.get(Constants.yearsOfExperience)));
                }
            });

            //setting up the add position dialog
            addPositionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(EditSportActivity.this);
                    // Set the dialog title
                    builder.setTitle(R.string.selectPosition);

                    //soccer positions
                    if (sportSelected.equals(Constants.soccer)) {
                        checkedPositionValues = new boolean[positionsInSoccer.length];

                    }
                    //baseball positions
                    else if (sportSelected.equals(Constants.baseball)) {
                        checkedPositionValues = new boolean[positionsInBaseball.length];
                    }
                    //add other sport positions here


                    //this for loop will check the positions on the dialog that the user had already chosen.
                    for (int k = 0; k < mSelectedItems.size(); k++) {
                        checkedPositionValues[mSelectedItems.get(k)] = true;
                    }

                    //if it's the first time using the dialog..mark the entries of choices from the database info


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


                    //add other sports positions here

                    // Set the action buttons
                    builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            // User clicked OK, so save the mSelectedItems results somewhere
                            // or return them to the component that opened the dialog

                            //first delete the previous values from the position edit text
                            editPositionInSportEditText.setText("");
                            for (int i = 0; i < mSelectedItems.size(); i++) {
                                if (i == 0) {
                                    if (sportSelected.equals(Constants.soccer)) {
                                        editPositionInSportEditText.append(positionsInSoccer[mSelectedItems.get(i)]);
                                    } else if (sportSelected.equals(Constants.baseball)) {
                                        editPositionInSportEditText.append(positionsInBaseball[mSelectedItems.get(i)]);
                                    }
                                } else if (i > 0) {
                                    if (sportSelected.equals(Constants.soccer)) {
                                        editPositionInSportEditText.append(" , " + positionsInSoccer[mSelectedItems.get(i)]);
                                    } else if (sportSelected.equals(Constants.baseball)) {
                                        editPositionInSportEditText.append(" , " + positionsInBaseball[mSelectedItems.get(i)]);
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


        }


    }
}
