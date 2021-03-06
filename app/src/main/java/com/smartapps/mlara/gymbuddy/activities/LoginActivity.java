package com.smartapps.mlara.gymbuddy.activities;

/**
 * Created by mlara on 9/18/2015.
 */

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.text.util.Linkify;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.smartapps.mlara.gymbuddy.R;

import java.util.ArrayList;
import java.util.List;

import util.Constants;


/**
 * Android login screen Activity
 */
public class LoginActivity extends Activity implements LoaderCallbacks<Cursor> {

    private static final String DUMMY_CREDENTIALS = "user@test.com:hello";

    /*   private UserLoginTask userLoginTask = null;*/
    private View loginFormView;
    private View progressView;
    private AutoCompleteTextView emailTextView;
    private EditText passwordTextView;
    private TextView signUpTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser != null) {
            // user is logged in already..redirecting to main screen
            LoginActivity.this.startActivity(new Intent(LoginActivity.this, MainMenuActivity.class));
        } else {
            // show the signup or login screen


            setContentView(R.layout.activity_login);
            emailTextView = (AutoCompleteTextView) findViewById(R.id.email);
            loadAutoComplete();

            passwordTextView = (EditText) findViewById(R.id.password);
            passwordTextView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                    if (id == EditorInfo.IME_NULL) {
                        initLogin();
                        return true;
                    }
                    return false;
                }
            });

            Button loginButton = (Button) findViewById(R.id.email_sign_in_button);
            loginButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    initLogin();
                }
            });

            loginFormView = findViewById(R.id.login_form);
            progressView = findViewById(R.id.login_progress);

            //adding underline and link to signup textview
            signUpTextView = (TextView) findViewById(R.id.signUpTextView);
            signUpTextView.setPaintFlags(signUpTextView.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
            Linkify.addLinks(signUpTextView, Linkify.ALL);

            signUpTextView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i("LoginActivity", "Sign Up Activity activated.");
                    //start the signup Activity
                    LoginActivity.this.startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
                }
            });
        }
    }

    private void loadAutoComplete() {
        getLoaderManager().initLoader(0, null, this);
    }


    /**
     * Validate Login form and authenticate.
     */
    public void initLogin() {
      /*  if (userLoginTask != null) {
            return;
        }
*/
        emailTextView.setError(null);
        passwordTextView.setError(null);

        String email = emailTextView.getText().toString();
        String password = passwordTextView.getText().toString();

        boolean cancelLogin = false;
        View focusView = null;

        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            passwordTextView.setError(Constants.passwordIsTooShort);
            focusView = passwordTextView;
            cancelLogin = true;
        }

        if (TextUtils.isEmpty(email)) {
            emailTextView.setError(Constants.fieldRequired);
            focusView = emailTextView;
            cancelLogin = true;
        } else if (!isEmailValid(email)) {
            emailTextView.setError(Constants.emailAddressInvalid);
            focusView = emailTextView;
            cancelLogin = true;
        }

        if (cancelLogin) {
            // error in login
            focusView.requestFocus();
        } else {
            // show progress spinner, and start background task to login
            showProgress(true);
           /* userLoginTask = new UserLoginTask(email, password);
            userLoginTask.execute((Void) null);*/
            ParseUser.logInInBackground(email, password, new LogInCallback() {
                @Override
                public void done(ParseUser parseUser, ParseException e) {
                    if (parseUser != null) {
                        //succesfully logged in
                        Toast.makeText(getApplicationContext(),
                                "Welcome " + parseUser.getUsername(),
                                Toast.LENGTH_LONG).show();
                        boolean basicProfileCompleted = (boolean) parseUser.get(Constants.firstLogin);
                        if (basicProfileCompleted == false) {
                            //redirect to main screen
                            LoginActivity.this.startActivity(new Intent(LoginActivity.this, MainMenuActivity.class));
                        } else {
                            //redirect to complete Basic Profile screen
                            LoginActivity.this.startActivity(new Intent(LoginActivity.this, CompleteRegistrationActivity.class));
                        }
                    } else {
                        Toast.makeText(getApplicationContext(),
                                e.getMessage(),
                                Toast.LENGTH_LONG).show();
                        showProgress(false);
                        LoginActivity.this.startActivity(new Intent(LoginActivity.this, LoginActivity.class));

                    }
                }
            });
        }
    }

    private boolean isEmailValid(String email) {
        //add your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //add your own logic
        return password.length() > 4;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    public void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            loginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            loginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    loginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            progressView.setVisibility(show ? View.VISIBLE : View.GONE);
            progressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    progressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            progressView.setVisibility(show ? View.VISIBLE : View.GONE);
            loginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }


    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
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
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> emails = new ArrayList<String>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }

        addEmailsToAutoComplete(emails);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(LoginActivity.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        emailTextView.setAdapter(adapter);
    }


    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }

}