package com.example.tabish.taskask;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Tabish on 17-Feb-18.
 */

public class UserOptionsAdapter extends ArrayAdapter<myDict>{

    Context mContext;
    public UserOptionsAdapter(Activity context, ArrayList<myDict> checklist)
    {

        super(context,0,checklist);
        mContext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {

        // Get the {@link Tasks} object located at this position in list
        myDict currentChecklist = getItem(position);

        //check if the existing view is being reused, otherwise inflate the view
        View listItemView = convertView;
        if (listItemView == null)
        {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.options_list,parent,false);
        }

        //Set uname field in list to current username
        TextView desc = (TextView) listItemView.findViewById(R.id.itemDesc);
        desc.setText(currentChecklist.getName());

        ImageView img = (ImageView) listItemView.findViewById(R.id.imgView);
        img.setImageResource(Integer.parseInt(currentChecklist.getValue()));


        return listItemView;
    }
}
