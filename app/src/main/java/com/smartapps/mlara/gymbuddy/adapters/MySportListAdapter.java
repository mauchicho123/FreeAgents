package com.smartapps.mlara.gymbuddy.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.smartapps.mlara.gymbuddy.R;
import com.smartapps.mlara.gymbuddy.pojos.Sport;

import java.util.List;

/**
 * Created by mlara on 12/9/2015.
 */
public class MySportListAdapter extends ArrayAdapter<Sport> {

    private int resource;
    private LayoutInflater inflater;
    private Context context;

    public MySportListAdapter(Context context, int resource, List<Sport> objects) {
        super(context, resource, objects);
        this.resource = resource;
        inflater = LayoutInflater.from( context );
        this.context=context;
    }

    @Override
    public View getView ( int position, View convertView, ViewGroup parent ) {
        Log.v("CHECK", " Inside MySportListAdapter getView");
        convertView = (RelativeLayout) inflater.inflate( resource, null );
        Sport sport= getItem( position );
        TextView sportName = (TextView) convertView.findViewById(R.id.editSportName);
        sportName.setText(sport.getSportName());

        TextView levelOfExpertise = (TextView) convertView.findViewById(R.id.levelOfExpertise);
        levelOfExpertise.setText(sport.getLevelOfExpertise());

        ImageView sportImage = (ImageView) convertView.findViewById(R.id.sportImage);
        String uri = "drawable/" + sport.getSportName().toLowerCase();
        int imageResource = context.getResources().getIdentifier(uri, null, context.getPackageName());
        Drawable image = context.getResources().getDrawable(imageResource);
        sportImage.setImageDrawable(image);

        /*ImageButton trashCanButton = (ImageButton) convertView.findViewById(R.id.deleteSport);
        uri = "drawable/trashcan";
        imageResource = context.getResources().getIdentifier(uri, null, context.getPackageName());
        image = context.getResources().getDrawable(imageResource);
        trashCanButton.setImageDrawable(image);*/
        return convertView;
    }
}
