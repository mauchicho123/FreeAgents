package com.smartapps.mlara.gymbuddy.fragments;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.smartapps.mlara.gymbuddy.R;

/**
 * Created by mlara on 10/8/2015.
 */
public class ProfileTabPhotosFragment extends Fragment {
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view= inflater.inflate(R.layout.tab,container,false);
        TextView textview=(TextView) view.findViewById(R.id.tabtextview);
        textview.setText(R.string.photos_tab);
        return view;

    }
}
