package com.example.tabish.taskask;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tabish on 06-Jan-18.
 */

public class ChecklistAdapter extends ArrayAdapter<myDict> {


    Context mContext;
    public ChecklistAdapter(Activity context, ArrayList<myDict> checklist)
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
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.checklist_list,parent,false);
        }

        //Set uname field in list to current username
        TextView type = (TextView) listItemView.findViewById(R.id.checktype);
        type.setText(currentChecklist.getName());

        TextView desc = (TextView) listItemView.findViewById(R.id.checkdesc);
        desc.setText(currentChecklist.getValue());

        TextView numb = (TextView) listItemView.findViewById(R.id.checklistNo);
        numb.setText(Integer.toString(position+1));


        return listItemView;
    }


}
