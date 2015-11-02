package com.smartapps.mlara.gymbuddy.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.Tab;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.smartapps.mlara.gymbuddy.R;
import com.smartapps.mlara.gymbuddy.com.smartapps.mlara.gymbuddy.listeners.EditProfileTabListener;
import com.smartapps.mlara.gymbuddy.fragments.ProfileTabAboutFragment;
import com.smartapps.mlara.gymbuddy.fragments.ProfileTabMySportsFragment;
import com.smartapps.mlara.gymbuddy.fragments.ProfileTabPhotosFragment;
import com.smartapps.mlara.gymbuddy.interfaces.OnDataPass;

import java.util.Date;
import java.util.HashMap;

import util.Constants;
import util.Utilities;

/**
 * Created by mlara on 10/7/2015.
 */
public class EditProfileActivity extends ActionBarActivity implements OnDataPass {

    Tab aboutTab,mySportsTab,photosTab;
    Fragment profileTabAboutFragment = new ProfileTabAboutFragment();
    Fragment profileTabMySportsFragment=new ProfileTabMySportsFragment();
    Fragment profileTabPhotosFragment=new ProfileTabPhotosFragment();
    Bitmap profilePicture;
    //Fields from ProfileTabAboutFragment to be saved in Activity if there is a change
    private String firstName="";
    private String lastName="";
    private String email="";
    private ParseFile profilePicFile;
    private Date dateOfBirth;
    private String gender="";
    //elements of save progress bar
    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.v("CHECK", "IN EDIT PROFILE ACTIVITY");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        profilePicFile=null;
        profilePicture=null;
        dateOfBirth=new Date();
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        aboutTab=actionBar.newTab().setText(R.string.about_tab);
        mySportsTab=actionBar.newTab().setText(R.string.mySports_tab);
        photosTab=actionBar.newTab().setText(R.string.photos_tab);

        aboutTab.setTabListener(new EditProfileTabListener(profileTabAboutFragment));
        mySportsTab.setTabListener(new EditProfileTabListener<>(profileTabMySportsFragment));
        photosTab.setTabListener(new EditProfileTabListener<>(profileTabPhotosFragment));

        actionBar.addTab(aboutTab);
        actionBar.addTab(mySportsTab);
        actionBar.addTab(photosTab);

        progressDialog=new ProgressDialog(EditProfileActivity.this);
        progressDialog.setTitle(Constants.pleaseWait);
        progressDialog.setMessage(Constants.updatingProfile);
        progressDialog.setCancelable(false);


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

        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_save:
                progressDialog.show();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            ParseUser currentUser = ParseUser.getCurrentUser();
                            if (currentUser != null) {
                                if(firstName!="") {
                                    currentUser.put(Constants.firstName, firstName);
                                }
                                if(lastName!=""){
                                    currentUser.put(Constants.lastName,lastName);
                                }
                                if(email!=""){
                                    currentUser.setUsername(email);
                                    currentUser.setEmail(email);
                                }
                                if(dateOfBirth!=null){
                                    currentUser.put(Constants.dateOfBirth,dateOfBirth);
                                }
                                if(gender!=""){
                                    currentUser.put(Constants.gender,gender);
                                }
                                if(profilePicture!=null){
                                    Log.v(Constants.profilePic,"CAME TO THIS CHECK #1");
                                    //first Delete the old profile Picture from database and then save the new one coming in from Fragment
                                    ParseQuery<ParseObject> query = ParseQuery.getQuery(Constants.pictureTable);
                                    query.whereEqualTo(Constants.userName, currentUser.getUsername());
                                    query.whereEqualTo(Constants.isItMainProfilePicture,true);
                                    query.getFirstInBackground(new GetCallback<ParseObject>() {
                                        public void done(ParseObject mainProfilePicture, ParseException e) {
                                            if (mainProfilePicture == null) {
                                                Log.d(Constants.pictureTable, "Main picture not found");
                                            } else {
                                                Log.d(Constants.pictureTable, "Retrieved the object.About to delete main profile picture to update it with new one");
                                                //about to delete if found
                                                try {
                                                    mainProfilePicture.delete();
                                                } catch (ParseException e1) {
                                                    e1.printStackTrace();
                                                    Log.d(Constants.pictureTable, "Error deleting main profile picture");
                                                }
                                            }
                                        }
                                    });

                                    //convert ImageView bitMap to Bytes before Saving to parse
                                    profilePicFile = new ParseFile(Constants.profilePic, Utilities.bitMapToBytes(profilePicture));
                                    profilePicFile.saveInBackground(new SaveCallback() {
                                        @Override
                                        public void done(ParseException e) {
                                            if(e==null){
                                                Log.v("PROFILEPIC","Profile picture file saved to parse");
                                                //about to save it to the Picture Table
                                                ParseObject picture = new ParseObject(Constants.pictureTable);
                                                picture.put(Constants.userName, ParseUser.getCurrentUser().getUsername());
                                                picture.put(Constants.isItMainProfilePicture, true);
                                                picture.put(Constants.pictureFile, profilePicFile);
                                                picture.saveInBackground(new SaveCallback() {
                                                    @Override
                                                    public void done(ParseException e) {
                                                        if (e == null) {
                                                            Log.v("ProfilePic", "New picture object created in database");
                                                        } else {
                                                            Log.v("ProfilePic", "Picture object failed to persist to database with error "+ e.toString());
                                                        }
                                                    }
                                                });
                                                picture.pinInBackground(new SaveCallback() {
                                                    @Override
                                                    public void done(ParseException e) {
                                                        if(e==null ){
                                                            Log.v("ProfilePic","New picture object pinned to local datastore");
                                                        } else{
                                                            Log.v("ProfilePic","Failed to pin picture File to local datastore with error "+e.toString());
                                                        }
                                                    }
                                                });
                                            }
                                            else{
                                                Log.v("ProfilePic","Picture File could not be saved to Parse with error "+e.toString());
                                            }
                                        }
                                    });
                                }
                                //keep adding fields to be saved here
                                //save changes made on profile
                                currentUser.saveInBackground(new SaveCallback() {
                                    @Override
                                    public void done(ParseException e) {
                                        if(e==null){
                                          // progressDialog.dismiss();
                                            Toast.makeText(getApplicationContext(),
                                                    "Profile Updated"
                                                    , Toast.LENGTH_LONG).show();
                                        }else{
                                          //  progressDialog.dismiss();
                                            Toast.makeText(getApplicationContext(),"Profile update failed, please try again",
                                                    Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });
                            }
                            // saveChanges()
                            // showProgress(false);
                           // Thread.sleep(5000);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        progressDialog.dismiss();
                    }
                }).start();
                return true;

            case R.id.action_cancel:
               //the user canceled ..going back to main screen
                EditProfileActivity.this.startActivity(new Intent(EditProfileActivity.this, MainMenuActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    //Assigns values stored in Fragment to variables created in this activity , to be saved later on by the Action bar save button
    @Override
    public void saveChangesFromEditProfileAboutFragment(HashMap<Object, Object> dataToBeSaved) {
      if(dataToBeSaved.containsKey(Constants.firstName)){
          firstName=(String)dataToBeSaved.get(Constants.firstName);
      }
      if(dataToBeSaved.containsKey(Constants.lastName)){
          lastName=(String)dataToBeSaved.get(Constants.lastName);
      }
      if(dataToBeSaved.containsKey(Constants.email)){
          email=(String)dataToBeSaved.get(Constants.email);
      }
     if(dataToBeSaved.containsKey((Constants.profilePic))){
            profilePicture=(Bitmap)dataToBeSaved.get(Constants.profilePic);
     }
     if(dataToBeSaved.containsKey(Constants.dateOfBirth)){
         dateOfBirth=(Date)dataToBeSaved.get(Constants.dateOfBirth);
     }
     if(dataToBeSaved.containsKey(Constants.gender)){
         gender=(String)dataToBeSaved.get(Constants.gender);
     }
    }



}
