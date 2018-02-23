package com.example.tabish.taskask;

import java.util.ArrayList;
import java.util.List;


import android.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.MenuItem;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.support.v4.app.NavUtils;
import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import com.example.tabish.taskask.Pair;
import com.google.android.gms.maps.MapFragment;

import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class ViewTask extends AppCompatActivity {

    private static String url_view_task = "get_task_details.php";
    String id;
    String nature;
    SessionManager session;
    int isProposed=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_task);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        session = new SessionManager(getApplicationContext());
        String uname = session.getUserDetails().get("username");
        id = getIntent().getStringExtra("TaskID");
        nature = getIntent().getStringExtra("nature");
        new viewTask().execute(id,uname,nature);

        ScrollView outerScrollView = (ScrollView) findViewById(R.id.outerScrollView);
        ListView innerScrollView = (ListView) findViewById(R.id.createChecklist);
        outerScrollView.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                findViewById(R.id.createChecklist).getParent()
                        .requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

        innerScrollView.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                v.getParent().requestDisallowInterceptTouchEvent(false);
                return false;
            }
        });
        final TextView userName = (TextView) findViewById(R.id.fullname);




    };

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null)
            return;

        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, listView);
            if (i == 0)
                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, android.support.v7.app.ActionBar.LayoutParams.WRAP_CONTENT));

            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

    private void display(Pair<Tasks, ArrayList<myDict>> pair)
    {
        final Tasks currentTask = pair.getFirst();

        TextView desc = (TextView) findViewById(R.id.desc);
        TextView urgency = (TextView) findViewById(R.id.urgencyTell);
        TextView critical = (TextView) findViewById(R.id.criticalTell);
        TextView total = (TextView) findViewById(R.id.total);
        String username = currentTask.getUsername();
        String uname = session.getUserDetails().get("username");
        boolean myTask = uname.equals(username);


        desc.setText(currentTask.getDesc());
        urgency.setText(currentTask.getUrgency());
        critical.setText(currentTask.getCritical());
        total.setText(currentTask.getFee());

        ArrayList<myDict> checkList = pair.getSecond();
        /*if(checkList.size()>0)
        {*/
        ListView listview = (ListView) findViewById(R.id.createChecklist);
        ChecklistAdapter checklistAdapter = new ChecklistAdapter(this,checkList);
        listview.setAdapter(checklistAdapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {


                myDict entry = (myDict) parent.getItemAtPosition(position);

                if(entry.getName().equals("Go"))
                {
                    String latLon = entry.getValue();
                    final String lat = latLon.split(",")[0];
                    final String lon = latLon.split(",")[1];
                    Intent viewIn = new Intent(getApplicationContext(),ViewCheckMap.class);
                    viewIn.putExtra("Latitude",lat);
                    viewIn.putExtra("Longitude",lon);
                    startActivity(viewIn);
                }

            }
        });
        setListViewHeightBasedOnChildren(listview);
        if(nature.equals("Posted Tasks"))
        {

        }




        if(!(username=="null") & !myTask)
        {

        }
        else
        {

        }
        String taskStatus = "Open";
        TackleLayoutDisplay(myTask,taskStatus,currentTask.getUser(),currentTask.getUsername());
        TackleOptions(myTask);





    }

    private void TackleLayoutDisplay(boolean myTask,String tstatus, String nameToDisplay, final String userWhoPosted)
    {
        TextView fullname = (TextView) findViewById(R.id.fullname);
        boolean listenToClick = false;

        if(myTask)
        {
            LinearLayout panel = (LinearLayout) findViewById(R.id.userPanel);
            panel.setGravity(Gravity.END);
            //if its my task and its completed, inProgress, i want to see who completed it - right
            if(tstatus.equals("Completed") | tstatus.equals("InProgress"));
            {
                listenToClick=true;
            }
            //if its my task and its open,closed i want to see  "Not completed yet" - right
            if(tstatus.equals("Closed") | tstatus.equals("Open"))
            {
                fullname.setText("Not completed yet.");
            }

            //if its my tasks and it was proposed, i want to see all those who proposed it - right
            if(tstatus.equals("Proposed"))
            {
                //Add code for viewing proposers
            }
        }
        //if its not my tasks and someone posted it - i want to see fullname of that user and should be able to click it too
        else
        {
            listenToClick=true;
        }

        if(listenToClick)
        {
            fullname.setText(nameToDisplay);
            fullname.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(getApplicationContext(),ViewUser.class);
                    i.putExtra("UserName",userWhoPosted);
                    startActivity(i);
                }
            });
        }



    }
    private void TackleOptions(boolean myTask)
    {
        Button propose = (Button) findViewById(R.id.propose);
        if(myTask)
        {
            propose.setText("Delete Task");
            propose.setBackgroundResource(R.drawable.red);
        }
        else
        {

            if(nature.equals("ProposableTasks") & isProposed != 1)
            {
                propose.setText("Propose Task");
                propose.setBackgroundResource(R.drawable.post_button);
                propose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String status="Proposed";
                        String uname = session.getUserDetails().get("username");
                        new ProposeTask().execute(id,status,uname);
                    }
                });
            }
            else if(isProposed==1)
            {
                propose.setText("Cancel Proposal");
                propose.setBackgroundResource(R.drawable.red);
            }

        }

    }

     class viewTask extends AsyncTask<String,Void,Pair<Tasks, ArrayList<myDict>>>{

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected Pair<Tasks, ArrayList<myDict>> doInBackground(String... args)
        {
            JSONParser jParser = new JSONParser();

            List<myDict> params = new ArrayList<myDict>();

            params.add(new myDict("taskID",args[0]));   //Get task based on TaskID
            params.add(new myDict("username",args[1]));
            params.add(new myDict("nature",args[2]));
            JSONObject json = jParser.makeHttpRequest(url_view_task, "GET", params);

            // Check your log cat for JSON reponse
            Log.d("All Tasks: ", json.toString());
            Tasks viewTask = null;
            ArrayList<myDict> checklistList = new ArrayList<myDict>();
            try {
                // Checking for SUCCESS TAG
                int success = json.getInt("success");
                if (success == 1) {
                    // Tasks found
                    // Getting Array of Tasks
                    JSONArray tasks = json.getJSONArray("tasks");

                    // looping through All Tasks

                        JSONObject c = tasks.getJSONObject(0);

                        // Storing each json item in variable
                        String id = c.getString("idTask");
                        String tag = c.getString("tag");
                        String descript = c.getString("description");
                        String pstatus = c.getString("PaymentStatus");
                        String ts = c.getString("tstatus");
                        String clevel = c.getString("CriticalLevel");
                        String ucolor = c.getString("uColor");
/*                        String pby = c.getString("PostedBy");
                        String username = c.getString("Username");*/
                        String vcnty = c.getString("Vicinity");
                        String uLevel = c.getString("UrgencyLevel");


                        String displayName = c.getString("displayName");
                        String hiddenUser = c.getString("displayUser");

                        String amnt  = c.getString("Amount");
                        isProposed = c.getInt("isProposed");







                        // adding HashList to ArrayList
                        viewTask = new Tasks(tag,displayName,amnt,uLevel,clevel,ucolor,"10",id,descript,hiddenUser);


                    int checklist = json.getInt("checkItems");
                    if(checklist == 1)
                    {

                        JSONArray checkElements = json.getJSONArray("checklist");

                        for(int i =0;i<checkElements.length();i++)
                        {

                            JSONObject e = checkElements.getJSONObject(i);
                            String type = e.getString("type");
                            String desc = e.getString("Description");

                            checklistList.add(new myDict(type,desc));
                        }


                    }

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return (new Pair<Tasks,ArrayList<myDict>>(viewTask,checklistList));
        }

        protected void onPostExecute(Pair<Tasks, ArrayList<myDict>> currentTask) {
            // dismiss the dialog after getting all products

            display(currentTask);



        }
    }

    class ProposeTask extends AsyncTask<String,Void,Boolean>
    {

        @Override
        protected Boolean doInBackground(String... args) {
            JSONParser jParser = new JSONParser();
            String url_update_task = "update_task.php";
            List<myDict> params = new ArrayList<myDict>();
            params.add(new myDict("idTask",args[0]));   //Get task based on TaskID
            params.add(new myDict("Status",args[1]));   //Get task based on TaskID
            params.add(new myDict("Uname",args[2]));
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

        protected void onPostExecute(Boolean success) {
            if(success)
            {
                Button propose = (Button) findViewById(R.id.propose);
                propose.setText("Cancel proposal");
                propose.setEnabled(false);
                Toast.makeText(getApplicationContext(), "Proposal sent.",Toast.LENGTH_SHORT).show();
            }

        }
    }


}


