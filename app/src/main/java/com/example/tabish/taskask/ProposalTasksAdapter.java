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

public class ProposalTasksAdapter extends ArrayAdapter<Tasks>  {

    Context mContext;

    public ProposalTasksAdapter(Activity context, ArrayList<Tasks> checklist) {

        super(context, 0, checklist);
        mContext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // Get the {@link Tasks} object located at this position in list
        final Tasks currentTask = getItem(position);

        //check if the existing view is being reused, otherwise inflate the view
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }

        TextView tag = (TextView) listItemView.findViewById(R.id.tag) ;
        tag.setText(currentTask.getTag() + "...");

        TextView taskNumber = (TextView) listItemView.findViewById(R.id.uname);
        taskNumber.setVisibility(View.GONE);

        TextView ulevel = (TextView) listItemView.findViewById(R.id.ulevel);
        ulevel.setVisibility(View.GONE);

        TextView clevel = (TextView) listItemView.findViewById(R.id.clevel);
        clevel.setVisibility(View.GONE);

        return listItemView;
    }
}

