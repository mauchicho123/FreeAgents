package com.smartapps.mlara.gymbuddy.com.smartapps.mlara.gymbuddy.listeners;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;

import com.smartapps.mlara.gymbuddy.R;

/**
 * Created by mlara on 10/8/2015.
 */
public class EditProfileTabListener<T extends Fragment> implements  ActionBar.TabListener {

   private  Fragment fragment;

    public EditProfileTabListener(Fragment fragment){
        this.fragment=fragment;
    }


    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        fragmentTransaction.replace(R.id.fragment_container, fragment);
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        fragmentTransaction.remove(fragment);
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {

    }
}
