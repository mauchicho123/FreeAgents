package com.smartapps.mlara.gymbuddy.pojos;

import android.graphics.Bitmap;

/**
 * Created by mlara on 10/13/2015.
 */
public class Picture {

    private Bitmap bitMap;

    public Picture(Bitmap bitMap, boolean isItMainProfilePicture){
        this.bitMap=bitMap;
        this.isItMainProfilePicture=isItMainProfilePicture;
    }

    public boolean isItMainProfilePicture() {
        return isItMainProfilePicture;
    }

    public void setIsItMainProfilePicture(boolean isItMainProfilePicture) {
        this.isItMainProfilePicture = isItMainProfilePicture;
    }

    public Bitmap getBitMap() {
        return bitMap;
    }

    public void setBitMap(Bitmap bitMap) {
        this.bitMap = bitMap;
    }

    private boolean isItMainProfilePicture;


}
