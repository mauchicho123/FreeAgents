package com.smartapps.mlara.gymbuddy.activities;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SignUpCallback;
import com.smartapps.mlara.gymbuddy.R;

import java.util.ArrayList;
import java.util.List;

import util.Constants;

/**
 * Created by mlara on 9/28/2015.
 */
public class SignUpActivity extends Activity implements LoaderManager.LoaderCallbacks<Cursor> {

    private AutoCompleteTextView emailTextView;
    private EditText passwordTextView;
    private Button signUpButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_email);
        emailTextView = (AutoCompleteTextView) findViewById(R.id.email);
        passwordTextView=(EditText)findViewById(R.id.password);
        loadAutoComplete();

        signUpButton = (Button) findViewById(R.id.email_sign_in_button);
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View focusView = null;
                String email = emailTextView.getText().toString().trim();
                String password = passwordTextView.getText().toString().trim();
                boolean cancelSignUp = false;

                //validating input data before signing up user.
                if (TextUtils.isEmpty(email)) {
                    emailTextView.setError(getString(R.string.field_required));
                    focusView = emailTextView;
                    cancelSignUp = true;
                }
                if (TextUtils.isEmpty(password)) {
                    passwordTextView.setError(getString(R.string.field_required));
                    cancelSignUp = true;
                }
                if (!isEmailValid(email)) {
                    emailTextView.setError(getString(R.string.invalid_email));
                    focusView = emailTextView;
                    cancelSignUp = true;
                }
                if (!isPasswordValid(password)) {
                    passwordTextView.setError(getString(R.string.invalid_password));
                    focusView = passwordTextView;
                    cancelSignUp = true;
                }
                if (cancelSignUp) {
                    focusView.requestFocus();
                } else {
                    // check if email is unique
                    ParseQuery<ParseObject> query = ParseQuery.getQuery(Constants.userTable );
                    query.whereEqualTo(Constants.userName,email);
                    query.findInBackground(new FindCallback<ParseObject>() {
                        public void done(List<ParseObject> userList, ParseException e) {
                            if (userList.size()>0) {
                                // found one user with same email
                                emailTextView.setError(getString(R.string.email_taken));
                            } else {
                                //create new user
                                String email = emailTextView.getText().toString().trim();
                                String password = passwordTextView.getText().toString().trim();
                                ParseUser user = new ParseUser();
                                user.setUsername(email);
                                user.setPassword(password);
                                user.setEmail(email);
                                user.put(Constants.firstLogin, true);
                                user.saveInBackground();
                                user.signUpInBackground(new SignUpCallback() {
                                    @Override
                                    public void done(ParseException e) {
                                        if (e == null) {
                                            //sign up succesfull
                                            Toast.makeText(getApplicationContext(),
                                                    "User created successfully."
                                                    , Toast.LENGTH_LONG).show();
                                            SignUpActivity.this.startActivity(new Intent(SignUpActivity.this, CompleteRegistrationActivity.class));
                                        } else {
                                            Toast.makeText(getApplicationContext(),
                                                    e.getMessage()
                                                    , Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });
                            }
                        }
                    });


                }
            }
        });

    }


    private boolean isEmailValid(String email) {
        //add your own logic
        return email.contains("@");
    }



    private boolean isPasswordValid(String password) {
        //add your own logic
        return password.length() > 4;
    }


    //part of the autcomplete feature for the email textbox

    private void loadAutoComplete() {
        getLoaderManager().initLoader(0, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE},

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        List<String> emails = new ArrayList<String>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }

        addEmailsToAutoComplete(emails);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }


    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }

    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(SignUpActivity.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        emailTextView.setAdapter(adapter);
    }

    //part of the autcomplete feature for the email textbox


}
