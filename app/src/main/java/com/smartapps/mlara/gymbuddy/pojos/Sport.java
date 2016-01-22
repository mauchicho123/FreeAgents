package com.smartapps.mlara.gymbuddy.pojos;

import android.os.Parcel;

import java.util.ArrayList;

/**
 * Created by mlara on 11/5/2015.
 */
public class Sport  {

    private String username;
    private String sportName;
    private String yearsOfExperience;
    private String levelOfExpertise;
    private Availability availability;
    private ArrayList<String> positions;
    private String sportImageName;

    public Sport(String userName, String sportName, String yearsOfExperience, String levelOfExpertise  )  {
        this.username=userName;
        this.sportName=sportName;
        this.yearsOfExperience=yearsOfExperience;
        this.levelOfExpertise=levelOfExpertise;
        positions=new ArrayList<String>();

    }

    private Sport(Parcel in) {
        username = in.readString();
        sportName = in.readString();
        yearsOfExperience= in.readString();
        levelOfExpertise=in.readString();


    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getSportName() {
        return sportName;
    }

    public void setSportName(String sportName) {
        this.sportName = sportName;
    }

    public String getYearsOfExperience() {
        return yearsOfExperience;
    }

    public void setYearsOfExperience(String yearsOfExperience) {
        this.yearsOfExperience = yearsOfExperience;
    }

    public String getLevelOfExpertise() {
        return levelOfExpertise;
    }

    public void setLevelOfExpertise(String levelOfExpertise) {
        this.levelOfExpertise = levelOfExpertise;
    }

    public Availability getAvailability() {
        return availability;
    }

    public void setAvailability(Availability availability) {
        this.availability = availability;
    }

    public ArrayList<String> getPositions() {
        return positions;
    }

    public void setPositions(ArrayList<String> positions) {
        this.positions = positions;
    }

    public String getSportImage() {
        return sportImageName;
    }

    public void setSportImage(String sportImage) {
        this.sportImageName = sportImage;
    }

//    @Override
//    public int describeContents() {
//        return 0;
//    }




/*
    @Override
    public void writeToParcel(Parcel dest, int flags) {
       dest.writeString(username);
       dest.writeString(sportName);
       dest.writeString(yearsOfExperience);
       dest.writeString(levelOfExpertise);
       dest.writeStringList(positions);
    }

    // Creator
    public static final Parcelable.Creator<Sport> CREATOR
            = new Parcelable.Creator<Sport>() {
        public Sport createFromParcel(Parcel in) {
            return new Sport(in);
        }

        public Sport[] newArray(int size) {
            return new Sport[size];
        }
    };*/

}
