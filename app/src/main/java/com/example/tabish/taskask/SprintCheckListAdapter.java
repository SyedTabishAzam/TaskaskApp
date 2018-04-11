package com.example.tabish.taskask;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.app.Activity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tabish on 06-Jan-18.
 */

public class SprintCheckListAdapter extends ArrayAdapter<CheckListForSprint> {


    Context mContext;
    boolean myTask;
    String idOfChecklist;
    public SprintCheckListAdapter(Activity context, ArrayList<CheckListForSprint> checklist,boolean _myTask)
    {

        super(context,0,checklist);
        mContext = context;
        myTask = _myTask;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {

        // Get the {@link Tasks} object located at this position in list
        CheckListForSprint currentChecklist = getItem(position);
        idOfChecklist = currentChecklist.getId();
        //check if the existing view is being reused, otherwise inflate the view
        View listItemView = convertView;
        if (listItemView == null)
        {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.sprint_checklist,parent,false);
        }

        //Set uname field in list to current username
        TextView type = (TextView) listItemView.findViewById(R.id.checktype);
        type.setText(currentChecklist.getName());

        TextView desc = (TextView) listItemView.findViewById(R.id.checkdesc);

        if(type.getText().equals("Go"))
        {
            final String latLon = currentChecklist.getValue();
            desc.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    final String lat = latLon.split(",")[0];
                    final String lon = latLon.split(",")[1];
                    Intent viewIn = new Intent(mContext,ViewCheckMap.class);
                    viewIn.putExtra("Latitude",lat);
                    viewIn.putExtra("Longitude",lon);
                    mContext.startActivity(viewIn);
                }
            });

        }
        desc.setText(currentChecklist.getValue());


        CheckBox numb = (CheckBox) listItemView.findViewById(R.id.checklistNo);
        numb.setText(Integer.toString(position+1));


        //If it is my task, then I want the checkboxes greyed out
        if(myTask)
        {
            numb.setClickable(false);
            numb.setBackgroundResource(R.drawable.gray);
            numb.setChecked(currentChecklist.isCompleted());


        }
        else
        {
            numb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    String TrueOrFalse;
                    if(isChecked)
                    {
                        TrueOrFalse = "True";
                    }
                    else
                    {
                        TrueOrFalse = "False";
                    }
                    new TickSprintCheckbox().execute(idOfChecklist,TrueOrFalse);
                }
            });
        }


        return listItemView;
    }

    class TickSprintCheckbox extends AsyncTask<String,Void,Boolean>
    {

        @Override
        protected Boolean doInBackground(String... args) {
            String url_update_checklist = "update_checklist_item.php";
            JSONParser jParser = new JSONParser();

            List<myDict> params = new ArrayList<myDict>();
            params.add(new myDict("idCheckList",args[0]));   //Get task based on TaskID
            params.add(new myDict("Checked",args[1]));   //Get task based on TaskID

            JSONObject json = jParser.makeHttpRequest(url_update_checklist, "GET", params);

            // Check your log cat for JSON reponse
            Log.d("Update Checklist: ", json.toString());

            try {
                // Checking for SUCCESS TAG
                int success = json.getInt("success");
                if (success == 1)
                {
                    return Boolean.TRUE;
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return Boolean.FALSE;
        }

        protected void onPostExecute(Boolean success)
        {

            if(success==Boolean.TRUE)
            {
                Log.e("SprintCheckList","True");
            }
            else
            {
                Log.e("SprintCheckList","False");
            }


        }
    }


}
