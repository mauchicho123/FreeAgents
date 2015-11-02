package com.smartapps.mlara.gymbuddy.fragments;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.smartapps.mlara.gymbuddy.R;
import com.smartapps.mlara.gymbuddy.interfaces.OnDataPass;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import util.Constants;
import util.Utilities;

/**
 * Created by mlara on 10/8/2015.
 */
public class ProfileTabAboutFragment extends Fragment  {
    private ImageView profilePicture;
    OnDataPass mCallback;
    private HashMap<Object,Object> dataToBeSaved;
    private TextView firstName;
    private TextView lastName;
    private TextView email;
    private EditText dateOfBirth;
    boolean profilePictureFoundInLocalDataStore;
    private int day;
    private int month;
    private int year;
    private ImageButton calendarButton;
    private Calendar calendar;
    private  DatePickerDialog datePickerDialog;
    private RadioGroup genderRadioGroup;
    private RadioButton maleRadioButton;
    private RadioButton femaleRadioButton;
    private String gender;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_editprofile_about_tab, container, false);
        dataToBeSaved=new HashMap<Object,Object>();
        firstName = (TextView) view.findViewById(R.id.edittext_firstname);
        lastName = (TextView) view.findViewById(R.id.edittext_lastname);
        email = (TextView) view.findViewById(R.id.edittext_email);
        dateOfBirth = (EditText) view.findViewById(R.id.edittext_dateOfBirth);
        dateOfBirth.addTextChangedListener(tw);
        profilePicture = (ImageView) view.findViewById(R.id.imageview_profile);
        genderRadioGroup=(RadioGroup)view.findViewById(R.id.radiogroup_gender);
        maleRadioButton=(RadioButton)view.findViewById(R.id.radiobutton_male);
        femaleRadioButton=(RadioButton)view.findViewById(R.id.radiobutton_female);
        //calendar picker fields
        calendar = Calendar.getInstance();
        day = calendar.get(Calendar.DAY_OF_MONTH);
        month = calendar.get(Calendar.MONTH);
        year = calendar.get(Calendar.YEAR);

        calendarButton = (ImageButton)view.findViewById(R.id.calendarButton);
        DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int selectedYear,
                                  int selectedMonth, int selectedDay) {
                int lenghtSelectedDay=String.valueOf(selectedDay).length();
                int lengthSelectedMonth = String.valueOf(selectedMonth+1).length();
                String selectedDayFormatted=String.valueOf(selectedDay);
                String selectedMonthFormatted=String.valueOf(selectedMonth+1);
                if (lenghtSelectedDay==1){
                    selectedDayFormatted= "0"+selectedDay;
                }
                if(lengthSelectedMonth==1){
                    selectedMonthFormatted= "0"+(selectedMonth+1);
                }
                dateOfBirth.setText(selectedDayFormatted + " / " + selectedMonthFormatted + " / "
                        + selectedYear);

                //Send new date to be saved by the EditProfile activity
                Date newBirthDate=new Date();
                Calendar calendar;
                calendar=Calendar.getInstance();
                calendar.set(year,Integer.parseInt(selectedMonthFormatted)-1,Integer.parseInt(selectedDayFormatted));
                newBirthDate=calendar.getTime();
                dataToBeSaved.put(Constants.dateOfBirth,newBirthDate);
                mCallback.saveChangesFromEditProfileAboutFragment(dataToBeSaved);
            }

        };

        datePickerDialog= new DatePickerDialog(getActivity(),
                mDateSetListener, year, month, day);

        calendarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog.show();
            }
        });

        //setting inforomation from user on screen
        ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser != null) {
            firstName.setText(currentUser.get(Constants.firstName).toString());
            lastName.setText(currentUser.get(Constants.lastName).toString());
            email.setText(currentUser.getUsername());
            Date dateBirthFromParse=currentUser.getDate(Constants.dateOfBirth);
            HashMap<String,String> dateInParts;
            dateInParts=Utilities.dateToDayMonthYear(dateBirthFromParse);
            dateOfBirth.setText(dateInParts.get(Constants.day) + "/" + dateInParts.get(Constants.month) + "/" + dateInParts.get(Constants.year));

            //gender set
            gender=currentUser.get(Constants.gender).toString();
            if(gender.equals(Constants.male)){
                maleRadioButton.setChecked(true);
            }else if(gender.equals(Constants.female)){
                femaleRadioButton.setChecked(true);
            }
        }

        //profile picture setup

        //setting profile picture in profilePicture ImageView
        //first find it on local datastore ..if cant be found go to database
        ParseQuery<ParseObject> query = ParseQuery.getQuery(Constants.pictureTable);
        query.whereEqualTo(Constants.userName, currentUser.getUsername());
        query.whereEqualTo(Constants.isItMainProfilePicture, true);
        query.fromLocalDatastore();
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            public void done(ParseObject mainProfilePicture, ParseException e) {
                if (mainProfilePicture == null) {
                    Log.v(Constants.pictureTable, "Main picture not found in local datastore");
                    profilePictureFoundInLocalDataStore=false;
                } else {
                    Log.v(Constants.pictureTable, "Main picture found from  local datastore");
                    profilePictureFoundInLocalDataStore = true;
                    //convert file found to bitMap
                    ParseFile image = (ParseFile) mainProfilePicture.get(Constants.pictureFile);
                    image.getDataInBackground(new GetDataCallback() {
                        @Override
                        public void done(byte[] bytes, ParseException e) {
                            Bitmap profilePicBitMap = Utilities.bytesToBitMap(bytes);
                            profilePicture.setImageBitmap(profilePicBitMap);
                        }
                    });
                }
            }
        });
        Log.v("ProfilePic","Value of the profilePictureFoundInLocalDataStore is "+profilePictureFoundInLocalDataStore);

        if(profilePictureFoundInLocalDataStore==false){
        //picture not found in local datastore..go to the database and find it
        ParseQuery<ParseObject> queryFromDatabase = ParseQuery.getQuery(Constants.pictureTable);
        queryFromDatabase.whereEqualTo(Constants.userName, currentUser.getUsername());
        queryFromDatabase.whereEqualTo(Constants.isItMainProfilePicture, true);
            queryFromDatabase.getFirstInBackground(new GetCallback<ParseObject>() {
                public void done(ParseObject mainProfilePicture, ParseException e) {
                    if (mainProfilePicture == null) {
                        Log.v(Constants.pictureTable, "Main picture not found in database");
                    } else {
                        Log.v(Constants.pictureTable, "Main picture found from Database");
                        //convert file found to bitMap
                        ParseFile image = (ParseFile) mainProfilePicture.get(Constants.pictureFile);
                        image.getDataInBackground(new GetDataCallback() {
                            @Override
                            public void done(byte[] bytes, ParseException e) {
                                Bitmap profilePicBitMap=Utilities.bytesToBitMap(bytes);
                                profilePicture.setImageBitmap(profilePicBitMap );
                            }
                        });
                    }
                }
            });
        }

        //adding text watcher to send value to parent activity "EditProfileActivity" when changed
        firstName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //nothing to do before textChanged
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // nothing to do while text is changing
            }

            @Override
            public void afterTextChanged(Editable s) {
                //when text has changed send information to parent activity here
                dataToBeSaved.put(Constants.firstName, firstName.getText().toString());
                mCallback.saveChangesFromEditProfileAboutFragment(dataToBeSaved);
            }
        });

        lastName.addTextChangedListener(new TextWatcher() {
                                            @Override
                                            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                                            }

                                            @Override
                                            public void onTextChanged(CharSequence s, int start, int before, int count) {

                                            }

                                            @Override
                                            public void afterTextChanged(Editable s) {
                                                dataToBeSaved.put(Constants.lastName, lastName.getText().toString());
                                                mCallback.saveChangesFromEditProfileAboutFragment(dataToBeSaved);
                                            }
                                        }

        );

            email.addTextChangedListener(new

            TextWatcher() {
                @Override
                public void beforeTextChanged (CharSequence s,int start, int count, int after){

                }

                @Override
                public void onTextChanged (CharSequence s,int start, int before, int count){

                }

                @Override
                public void afterTextChanged (Editable s){
                    dataToBeSaved.put(Constants.email, email.getText().toString());
                    mCallback.saveChangesFromEditProfileAboutFragment(dataToBeSaved);
            }
        });


        profilePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(
                        Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, Constants.RESULT_LOAD_IMAGE);
            }
        });

        //send gender changes to save in Edit Profile
        genderRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId==R.id.radiobutton_male){
                    gender=Constants.male;
                }else if(checkedId==R.id.radiobutton_female){
                    gender= Constants.female;
                }
                dataToBeSaved.put(Constants.gender,gender);
                mCallback.saveChangesFromEditProfileAboutFragment(dataToBeSaved);
            }
        });


       /* TextView textview=(TextView) view.findViewById(R.id.tabtextview);
        textview.setText(R.string.about_tab);*/
        return view;

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == Constants.RESULT_LOAD_IMAGE && null != data) {
                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                Cursor cursor = getActivity().getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String picturePath = cursor.getString(columnIndex);
                cursor.close();
                Bitmap imageBitMap = BitmapFactory.decodeFile(picturePath);
                imageBitMap = Utilities.getclip(imageBitMap);
                profilePicture.setImageBitmap(imageBitMap);
                dataToBeSaved.put(Constants.profilePic, imageBitMap);
                mCallback.saveChangesFromEditProfileAboutFragment(dataToBeSaved);
            }
        } catch (Exception e) {
            Log.v("Error uploading picture", "" + e);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (OnDataPass) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }

    //dateOfBIrth text watcher methods

    //date formatter input
    TextWatcher tw = new TextWatcher() {
        private String current = "";
        private String ddmmyyyy = Constants.ddmmyyyy;
        private Calendar cal = Calendar.getInstance();

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (!s.toString().equals(current)) {
                String clean = s.toString().replaceAll("[^\\d.]", "");
                String cleanC = current.replaceAll("[^\\d.]", "");

                int cl = clean.length();
                int sel = cl;
                for (int i = 2; i <= cl && i < 6; i += 2) {
                    sel++;
                }
                //Fix for pressing delete next to a forward slash
                if (clean.equals(cleanC)) sel--;

                if (clean.length() < 8){
                    clean = clean + ddmmyyyy.substring(clean.length());
                }else{
                    //This part makes sure that when we finish entering numbers
                    //the date is correct, fixing it otherwise
                    int day  = Integer.parseInt(clean.substring(0,2));
                    int mon  = Integer.parseInt(clean.substring(2,4));
                    int year = Integer.parseInt(clean.substring(4,8));

                    if(mon > 12) mon = 12;
                    cal.set(Calendar.MONTH, mon-1);
                    year = (year<1900)?1900:(year>2100)?2100:year;
                    cal.set(Calendar.YEAR, year);
                    // ^ first set year for the line below to work correctly
                    //with leap years - otherwise, date e.g. 29/02/2012
                    //would be automatically corrected to 28/02/2012

                    day = (day > cal.getActualMaximum(Calendar.DATE))? cal.getActualMaximum(Calendar.DATE):day;
                    clean = String.format("%02d%02d%02d",day, mon, year);
                }

                clean = String.format("%s/%s/%s", clean.substring(0, 2),
                        clean.substring(2, 4),
                        clean.substring(4, 8));

                sel = sel < 0 ? 0 : sel;
                current = clean;
                dateOfBirth.setText(current);

                dateOfBirth.setSelection(sel < current.length() ? sel : current.length());
            }
        }
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        @Override
        public void afterTextChanged(Editable s) {}
    };




}
