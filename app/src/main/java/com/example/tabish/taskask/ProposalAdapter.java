package com.example.tabish.taskask;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Tabish on 18-Feb-18.
 */

public class ProposalAdapter extends ArrayAdapter<UserDetail>  {

    Context mContext;

    public ProposalAdapter(Activity context, ArrayList<UserDetail> checklist) {

        super(context, 0, checklist);
        mContext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // Get the {@link Tasks} object located at this position in list
        final UserDetail currentProposer = getItem(position);

        //check if the existing view is being reused, otherwise inflate the view
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.runners_list, parent, false);
        }

        //Set uname field in list to current username
        TextView nm = (TextView) listItemView.findViewById(R.id.nameOfUser);
        nm.setText(currentProposer.getFullname());

        nm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String username = currentProposer.getUsername();
                Intent i = new Intent(mContext,ViewUser.class);
                i.putExtra("UserName",username);
                mContext.startActivity(i);
            }
        });


        TextView stat = (TextView) listItemView.findViewById(R.id.statusUser);
        stat.setText(currentProposer.getStatus());

        TextView goLocation = (TextView) listItemView.findViewById(R.id.goLocation);
        String sMaker = Double.toString(currentProposer.getLatitude()) + "," + Double.toString(currentProposer.getLongitude());
       goLocation.setText(sMaker);

        TextView viewLocation = (TextView) listItemView.findViewById(R.id.viewLocation);
        viewLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String lat = Double.toString(currentProposer.getLatitude());
                final String lon = Double.toString(currentProposer.getLongitude());
                Intent viewIn = new Intent(mContext,ViewCheckMap.class);
                viewIn.putExtra("Latitude",lat);
                viewIn.putExtra("Longitude",lon);
                mContext.startActivity(viewIn);
            }
        });
        Button accept = (Button) listItemView.findViewById(R.id.acceptUser);
        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("Accepted",currentProposer.getUsername());
            }
        });

        RatingBar rating = (RatingBar) listItemView.findViewById(R.id.userRating);
        float eRatingBarFloat = currentProposer.geteRating();
        rating.setRating(eRatingBarFloat);

        return listItemView;
    }
}

