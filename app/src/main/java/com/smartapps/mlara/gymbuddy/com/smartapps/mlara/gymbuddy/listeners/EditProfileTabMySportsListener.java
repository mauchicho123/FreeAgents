package com.smartapps.mlara.gymbuddy.com.smartapps.mlara.gymbuddy.listeners;

import android.app.ListFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;

/**
 * Created by mlara on 12/14/2015.
 */
public class EditProfileTabMySportsListener  extends FragmentActivity implements  ActionBar.TabListener {


    private ListFragment listFragment;

    public EditProfileTabMySportsListener(ListFragment fragment) {
        this.listFragment = fragment;
    }
/*
    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        fragmentTransaction.replace(R.id.fragment_container, listFragment);

    }*/

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {

    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {

    }


    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
/*

        */
/**
 * On first tab we will show our list
 *//*

        if (tab.getPosition() == 0) {
            ProfileTabMySportsFragment simpleListFragment = new ProfileTabMySportsFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, listFragment).commit();
        }
        else if (tab.getPosition() == 1) {
            AndroidList androidlidt = new AndroidList();
            getSupportFragmentManager().beginTransaction().replace(R.id.container, androidlidt).commit();
        }
*/

    }
}