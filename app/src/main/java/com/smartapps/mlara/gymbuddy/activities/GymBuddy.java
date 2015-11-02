package com.smartapps.mlara.gymbuddy.activities;

import android.app.Application;

import com.parse.Parse;

/**
 * Created by mlara on 9/28/2015.
 */
public class GymBuddy extends Application{

    @Override
    public void onCreate() {
        super.onCreate();
        // Enable Local Datastore.
        Parse.enableLocalDatastore(this);
        Parse.initialize(this, "Z6BxU54pDaTjCMaXH42BCcFphegyhSZkH8RSjFS8", "06A14AED7WS5pLtVnTfYxS1A4tZbuSo87IViIMpf");

    }
}
