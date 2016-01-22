package com.smartapps.mlara.gymbuddy.parse.DAOImplementations;

import android.util.Log;

import com.parse.FindCallback;
import com.parse.LogInCallback;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.smartapps.mlara.gymbuddy.interfaces.DAO.UserDAO;
import com.smartapps.mlara.gymbuddy.pojos.User;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

import util.Constants;

/**
 * Created by mlara on 11/5/2015.
 */
public class UserDAOImpl implements UserDAO {


    //Saves the user to parse on the first time registering
    public void saveUserToParseFirstLogin(User user) throws ParseException {

        ParseUser userToBeSaved = new ParseUser();
        userToBeSaved.setUsername(user.getUsername());
        userToBeSaved.setPassword(user.getPassword());
        userToBeSaved.setEmail(user.getUsername());
        userToBeSaved.put(Constants.firstLogin, true);
        userToBeSaved.saveInBackground();

    }

    //Completes registration details of user after first sign up
    public void completeUserRegistrationInformation(User user) {
        ParseUser currentUser = ParseUser.getCurrentUser();
        currentUser.put(Constants.firstName, user.getFirstName().trim());
        currentUser.put(Constants.lastName, user.getLastName().trim());
        currentUser.put(Constants.gender, user.getGender());
        currentUser.put(Constants.dateOfBirth, user.getDateOfBirth());
        currentUser.put(Constants.firstLogin, false);
        currentUser.saveInBackground();
    }

    //Check if user already exist on database. If it does returns true, otherwise false.
    public boolean checkIfUserAlreadyExist(String email) {
        final boolean[] userExists = {false};
        ParseQuery<ParseObject> query = ParseQuery.getQuery(Constants.userTable);
        query.whereEqualTo(Constants.userName, email);
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> userList, com.parse.ParseException e) {
                if (userList.size() > 0) {
                    // found one user with same email
                    userExists[0] = true;
                } else {
                    if (e != null) {
                        Log.e("Error", "The exception that ocurred checking for the user existance is: " + e);
                    }
                }
            }
        });
        return userExists[0];
    }


    //logs the user in background , returns a parse User
    public User logInInBackground(String email, String password) {
        final User[] userLoggedIn = {null};
        Log.v("INFO", "About to log in user with email: " + email + " and password " + password);
        ParseUser.logInInBackground(email, password, new LogInCallback() {
            @Override
            public void done(ParseUser parseUser, com.parse.ParseException e) {
                if (parseUser != null) {

                    Log.v("INFO", "Logged in succcesfull");
                    userLoggedIn[0] = convertParseUserToLocalUser(parseUser);
                } else {
                    Log.e("Invalid Log In", "Not able to log in user");
                }
            }
        });
        return userLoggedIn[0];
    }


    public void logInInBackgroundNoCallback(String email, String password) throws com.parse.ParseException {
        Log.v("INFO", "About to log in user with email: " + email + " and password " + password);
        ParseUser.logIn(email, password);
    }

    //Checks if user is logged in . If user == null no user was logged in, if user!= null ..user returned successfullly
    public User checkIfUserIsLoggedInAndReturnIt() {
        ParseUser currentUser = ParseUser.getCurrentUser();
        User user = null;
        if (currentUser != null) {
            user = convertParseUserToLocalUser(currentUser);
        }
        return user;
    }

    public User convertParseUserToLocalUser(ParseUser parseUser) {
        User user = null;
        if (parseUser != null) {
            String username = parseUser.getUsername();
            String email = parseUser.getEmail();
            boolean firstLogin = parseUser.getBoolean(Constants.firstLogin);
            user = new User(username);
            user.setEmail(email);
            user.setUsername(username);
            //check if its not the user first time login to the app to retrieve the fields from the complete registration
            if (firstLogin == false) {
                String firstName = (String) parseUser.get(Constants.firstName);
                String lastName = (String) parseUser.get(Constants.lastName);
                String gender = (String) parseUser.get(Constants.gender);
                Date dateOfBirth = parseUser.getDate(Constants.dateOfBirth);
                user.setFirstName(firstName);
                user.setLastName(lastName);
                user.setGender(gender);
                user.setDateOfBirth(dateOfBirth);
                user.setFirstLogin(false);
            }
        }
        return user;
    }


}
