package com.smartapps.mlara.gymbuddy.pojos;

/**
 * Created by mlara on 11/5/2015.
 */
public class Sport {

    private String username;
    private String sportName;
    private String yearsOfExperience;
    private String levelOfExpertise;

    public Sport(String userName, String sportName, String yearsOfExperience, String levelOfExpertise ){
        this.username=userName;
        this.sportName=sportName;
        this.yearsOfExperience=yearsOfExperience;
        this.levelOfExpertise=levelOfExpertise;

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
}
