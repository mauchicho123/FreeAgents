package util;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * Created by mlara on 9/29/2015.
 */
public final class Constants {

    //database Parse tables
    public static final String userTable="User";
    public static final String pictureTable="Picture";
    public static final String sportTable="Sport";
    public static final String positionTable="Position";
    public static final String availabilityTable="Availability";

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

    //sport Table
    public static final String sportName="sportName";
    public static final String yearsOfExperience="yearsOfExperience";
    public static final String levelOfExpertise="levelOfExpertise";
    public static final String positionShortDescription="positionShortDesc";

    //position Table
    public static final String position="position";

    //availability Table
    public static final String monday="monday";
    public static final String tuesday="tuesday";
    public static final String wednesday="wednesday";
    public static final String thursday="thursday";
    public static final String friday="friday";
    public static final String saturday="saturday";
    public static final String sunday="sunday";




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
   //action Bar Edit Profile strings
    public static final String about="About";
    public static final String mySports="My Sports";
    public static final String photos="Photos";

    //add sport constants
    public static final String addSport="Add Sport";
    public static final String otherSport="Other";
    public static final String soccer="Soccer";
    public static final String baseball="Baseball";
    public static final String selectSport="Select Sport";
    public static final String addPosition="Add Position";
    public static final String selectedSport="Selected Sport";

    //error messages
    public static final String passwordIsTooShort="Password is too Short";
    public static final String fieldRequired="This field is required";
    public static final String emailAddressInvalid="This email address is invalid";
    public static final String invalidLoginCredentials="Invalid Login, please check your credentials";

    //position short description
    //soccer
    public static final HashMap<String, String> soccerPositionShortDescriptionMap = new HashMap<String, String>();
    static {
        soccerPositionShortDescriptionMap.put("GoalKeeper",  "GK" );
        soccerPositionShortDescriptionMap.put("Left Back",  "LB" );
        soccerPositionShortDescriptionMap.put("Center Back",  "CB" );
        soccerPositionShortDescriptionMap.put("Right Back",  "RB" );
        soccerPositionShortDescriptionMap.put("Defensive Midfielder", "DMF");
        soccerPositionShortDescriptionMap.put("Attacking Midfielder", "AMF" );
        soccerPositionShortDescriptionMap.put("Winger","WIN" );
        soccerPositionShortDescriptionMap.put("Forward",  "FWD");

    }
}
