package com.smartapps.mlara.gymbuddy.interfaces.DAO;

import com.parse.ParseUser;
import com.smartapps.mlara.gymbuddy.pojos.User;

import java.text.ParseException;

/**
 * Created by mlara on 11/6/2015.
 */
public interface UserDAO {

    public void saveUserToParseFirstLogin(User user) throws ParseException;
    public void completeUserRegistrationInformation(User user);
    public boolean checkIfUserAlreadyExist(String email);
    public User checkIfUserIsLoggedInAndReturnIt( );
    public User logInInBackground(String email, String password);
    public void logInInBackgroundNoCallback(String email, String password) throws com.parse.ParseException;
    public User convertParseUserToLocalUser(ParseUser parseUser);

}
