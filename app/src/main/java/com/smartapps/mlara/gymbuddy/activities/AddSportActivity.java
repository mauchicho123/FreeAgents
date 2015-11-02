package com.smartapps.mlara.gymbuddy.activities;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.Spinner;

import com.parse.ParseUser;
import com.smartapps.mlara.gymbuddy.R;

import util.Constants;

/**
 * Created by mlara on 10/26/2015.
 */
public class AddSportActivity extends ActionBarActivity {

    private AutoCompleteTextView sportNameTextView;
    private ProgressDialog progressDialog;
    private Spinner yearsOfExperience;
    private Spinner levelOfExpertise;
    private CheckBox monday,tuesday,wednesday,thursday,friday,saturday,sunday;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_sport);


        yearsOfExperience=(Spinner)findViewById(R.id.yearsSpinner);
        levelOfExpertise=(Spinner)findViewById(R.id.experienceSpinner);


        //elements of ActionBar
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setTitle(Constants.addSport);

        //elements of save progress bar
        progressDialog=new ProgressDialog(AddSportActivity.this);
        progressDialog.setTitle(Constants.pleaseWait);
        progressDialog.setMessage(Constants.updatingProfile);
        progressDialog.setCancelable(false);

        //availability
        monday=(CheckBox)findViewById(R.id.mondayCheckBox);
        tuesday=(CheckBox)findViewById(R.id.tuesdayCheckBox);
        wednesday=(CheckBox)findViewById(R.id.wednesdayCheckBox);
        thursday=(CheckBox)findViewById(R.id.thursdayCheckBox);
        friday=(CheckBox)findViewById(R.id.fridayCheckBox);
        saturday=(CheckBox)findViewById(R.id.satCheckBox);
        sunday=(CheckBox)findViewById(R.id.sunCheckBox);




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
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            ParseUser currentUser = ParseUser.getCurrentUser();
                            if (currentUser != null) {

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


}