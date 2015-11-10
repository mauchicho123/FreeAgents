package com.smartapps.mlara.gymbuddy.pojos;

/**
 * Created by mlara on 11/5/2015.
 */
public class Position {

    private String username;
    private String sportName;
    private String position;

    public Position(String username, String sportName, String position){
        this.username=username;
        this.sportName=sportName;
        this.position=position;
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

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }
}
