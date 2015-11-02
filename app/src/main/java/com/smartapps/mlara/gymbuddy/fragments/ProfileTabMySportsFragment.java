package com.smartapps.mlara.gymbuddy.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.smartapps.mlara.gymbuddy.R;
import com.smartapps.mlara.gymbuddy.activities.AddSportActivity;

/**
 * Created by mlara on 10/8/2015.
 */
public class ProfileTabMySportsFragment extends Fragment {

    private Button addSportButton;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view= inflater.inflate(R.layout.fragment_editprofile_mysports_tab,container,false);
        addSportButton=(Button)view.findViewById(R.id.newSportButton);
        addSportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddSportActivity.class);
                startActivity(intent);
            }
        });
        return view;

    }

}
