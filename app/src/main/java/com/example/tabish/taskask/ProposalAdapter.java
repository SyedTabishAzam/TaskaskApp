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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tabish on 18-Feb-18.
 */

public class ProposalAdapter extends ArrayAdapter<UserDetail>  {

    Context mContext;
    SessionManager session;
    String taskID;
    public ProposalAdapter(Activity context, ArrayList<UserDetail> checklist,String _taskId) {

        super(context, 0, checklist);
        mContext = context;
        session = new SessionManager(mContext);
        taskID = _taskId;
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

                session.createSprintSession(taskID,currentProposer.getUsername());
                String status = "InProgress";
                String proposer = currentProposer.getUsername();
                Log.e("whoPropo",proposer);
                new ChangeTaskStatus().execute(taskID,proposer,status);
            }
        });

        RatingBar rating = (RatingBar) listItemView.findViewById(R.id.userRating);
        float eRatingBarFloat = currentProposer.geteRating();
        rating.setRating(eRatingBarFloat);

        return listItemView;
    }

    class ChangeTaskStatus extends AsyncTask<String,Void,Boolean>
    {


        @Override
        protected Boolean doInBackground(String... args) {
            String url_update_task = "change_task_to_accept.php";
            JSONParser jParser = new JSONParser();

            List<myDict> params = new ArrayList<myDict>();
            params.add(new myDict("idTask",args[0]));   //Get task based on TaskID
            params.add(new myDict("proposer",args[1]));
            params.add(new myDict("Status",args[2]));
            JSONObject json = jParser.makeHttpRequest(url_update_task, "POST", params);

            // Check your log cat for JSON reponse
            Log.d("Update Taks: ", json.toString());
            Tasks viewTask = null;
            ArrayList<myDict> checklistList = new ArrayList<myDict>();
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
            // dismiss the dialog once done

            if(success==Boolean.TRUE)
            {

                FurtherAction();
            }
            else
            {
                session.endSprint();
                Toast.makeText(mContext,"Some error occured.",Toast.LENGTH_SHORT).show();
            }
        }
    }

    void FurtherAction()
    {
        Log.e("Accepted","F.ACTION");


        // After logout redirect user to Loing Activity
        Intent sprintActivity = new Intent(mContext, Sprint.class);

        // Add new Flag to start new Activity
        sprintActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        // Staring Login Activity
        mContext.startActivity(sprintActivity);

    }
}

