package util;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by mlara on 9/29/2015.
 */
public final class Constants {

    //database keywords
    //user table
    public static final String firstLogin="firstLogin";
    public static final String email="email";
    public static final String userName="username";
    public static final String firstName="firstName";
    public static final String lastName="lastName";
    public static final String gender="gender";
    public static final String dateOfBirth="dateOfBirth";

    //picture Table
    public static final String pictureFile="pictureFile";
    public static final String isItMainProfilePicture="isItMainProfilePic";
    public static final String description="pictureDescription";


    //database Parse tables
    public static final String userTable="USER";
    public static final String pictureTable="Picture";


    //util constants
    public static final String male="male";
    public static final String female="female";
    public static final String ddmmyyyy = "DDMMYYYY";
    public static int RESULT_LOAD_IMAGE = 1;
    public static final String profilePic="ProfilePic";
    public static final String year="year";
    public static final String month="month";
    public static final String day="day";


    //menu items
    public static final List<String> leftMenuItems =
            Collections.unmodifiableList(Arrays.asList("Profile", "Log out"));

    //Progress bar
    public static final String pleaseWait="Please Wait...";
    public static final String updatingProfile="Updating Profile";


  /*  //Sport List
    public static final List<String> sportList =
            Collections.unmodifiableList(Arrays.asList("Soccer", "Yoga","Tennis","Running","Basketball","Weight Lifting"));*/


    //add sport constants
    public static final String addSport="Add Sport";
}
