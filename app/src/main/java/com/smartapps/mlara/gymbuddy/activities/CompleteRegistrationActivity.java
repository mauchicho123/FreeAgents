package com.smartapps.mlara.gymbuddy.activities;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.parse.ParseUser;
import com.smartapps.mlara.gymbuddy.R;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

import util.Constants;
import util.Utilities;

/**
 * Created by mlara on 9/29/2015.
 */
public class CompleteRegistrationActivity extends Activity {

    private ImageButton calendarButton;
    private Calendar calendar;
    private int day;
    private int month;
    private int year;
    private EditText firstName;
    private EditText lastName;
    private EditText dateOfBirth;
    private RadioGroup genderRadioGroup;
    String gender;
    private Button saveButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_registration);
        gender=Constants.male;
        calendarButton = (ImageButton) findViewById(R.id.calendarButton);
        calendar = Calendar.getInstance();
        day = calendar.get(Calendar.DAY_OF_MONTH);
        month = calendar.get(Calendar.MONTH);
        year = calendar.get(Calendar.YEAR);
        dateOfBirth = (EditText) findViewById(R.id.dateOfBirth);
        dateOfBirth.addTextChangedListener(tw);
        calendarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(0);
            }
        });

        firstName=(EditText) findViewById(R.id.firstName);
        lastName=(EditText) findViewById(R.id.lastName);

        genderRadioGroup=(RadioGroup)findViewById(R.id.genderRadioGroup);
        genderRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId==R.id.radioMale){
                    gender= Constants.male;
                }else if(checkedId==R.id.radioFemale){
                    gender= Constants.female;
                }
            }
        });

        saveButton=(Button)findViewById(R.id.save);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean cancelProfileCompletion = false;
                View focusView = null;

                if (TextUtils.isEmpty(firstName.getText().toString().trim())) {
                    firstName.setError(getString(R.string.field_required));
                    focusView = firstName;
                    cancelProfileCompletion = true;
                }
                if (TextUtils.isEmpty(lastName.getText().toString().trim())) {
                    lastName.setError(getString(R.string.field_required));
                    focusView = lastName;
                    cancelProfileCompletion = true;

                } else if (TextUtils.isEmpty(gender)) {
                    focusView=genderRadioGroup;
                    Toast.makeText(getApplicationContext(),
                            "Please specify a gender"
                            , Toast.LENGTH_LONG).show();
                    cancelProfileCompletion = true;
                } else if(TextUtils.isEmpty((dateOfBirth.getText().toString().trim()))){
                    dateOfBirth.setError(getString(R.string.field_required));
                    focusView = dateOfBirth;
                    Toast.makeText(getApplicationContext(),
                            "Came in here"
                            , Toast.LENGTH_LONG).show();
                    cancelProfileCompletion = true;
                }

                if (cancelProfileCompletion) {
                    // one of the fields is Missing
                    focusView.requestFocus();
                } else {
                    ParseUser currentUser = ParseUser.getCurrentUser();
                    Date date=new Date();
                    if (currentUser != null) {
                       try{
                            date= Utilities.convertStringToDate(dateOfBirth.getText().toString().trim());
                       }catch(ParseException e){
                           Log.e("Parse Exception ", "An error has ocurred: "+e);
                       }

                        //passed all validations and code is about to complete basic information profile
                        currentUser.put(Constants.firstName,firstName.getText().toString().trim());
                        currentUser.put(Constants.lastName,lastName.getText().toString().trim());
                        currentUser.put(Constants.gender,gender);
                        currentUser.put(Constants.dateOfBirth,date);
                        currentUser.put(Constants.firstLogin,false);

                        currentUser.saveInBackground();
                        Toast.makeText(getApplicationContext(),
                                "Profile Updated"
                                , Toast.LENGTH_LONG).show();
                        CompleteRegistrationActivity.this.startActivity(new Intent(CompleteRegistrationActivity.this, MainMenuActivity.class));
                    } else {
                        Toast.makeText(getApplicationContext(),
                                "Lost Session"
                                , Toast.LENGTH_LONG).show();
                    }
                }
            }
        });



    }

    @Override
    @Deprecated
    protected Dialog onCreateDialog(int id) {
        return new DatePickerDialog(this, datePickerListener, year, month, day);
    }

    private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {
            int lenghtSelectedDay=String.valueOf(selectedDay).length();
            int lengthSelectedMonth = String.valueOf(selectedMonth).length();
            String selectedDayFormatted=String.valueOf(selectedDay);
            String selectedMonthFormatted=String.valueOf(selectedMonth+1);
            if (lenghtSelectedDay==1){
                selectedDayFormatted= "0"+selectedDay;
            }
            if(lengthSelectedMonth==1){
                selectedMonthFormatted= "0"+selectedMonthFormatted;
            }
            dateOfBirth.setText(selectedDayFormatted + " / " + (selectedMonthFormatted) + " / "
                    + selectedYear);
        }
    };



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
