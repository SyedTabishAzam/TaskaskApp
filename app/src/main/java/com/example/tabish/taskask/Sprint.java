package com.example.tabish.taskask;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Sprint extends Activity {
    Button sprintFinish,sprintCancel;
    SessionManager session;
    String taskID;
    String myUsername;
    String nature;
    boolean refreshView,firstTime;
    JSONParser jsonParser;
    boolean taskDetailsAltered;
    Handler myHandle;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sprint);
        taskDetailsAltered = false;
        refreshView = false;
        firstTime = true;
        session = new SessionManager(getApplicationContext());
        myHandle = new Handler();
        taskID = session.getSprintDetail().get("taskID");
        myUsername = session.getUserDetails().get("username");
        nature = "SprintAsCustomer";
        sprintFinish = findViewById(R.id.sprintFinish);
        sprintCancel = findViewById(R.id.sprintCancel);

        String taskStatus = "InProgress";
        new SprintData().execute(taskID,myUsername,nature);



        sprintFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CompleteSprint();
            }
        });
        sprintCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CancelSprint();
            }
        });

        Toast.makeText(this,taskID, Toast.LENGTH_SHORT).show();

    }

    void CompleteSprint()
    {

        Intent i = new Intent(getApplicationContext(),TaskComplete.class);
        i.putExtra("DisplayName",session.getSprintDetail().get("otherUserId"));
        session.endSprint();
        startActivity(i);
        finish();
    }

    void CancelSprint()
    {
        session.endSprint();
        Intent i = new Intent(getApplicationContext(),TaskComplete.class);
        startActivity(i);
        finish();
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        refresh(false);
    }

    class SprintData extends AsyncTask<String,Void,Pair<Tasks, ArrayList<CheckListForSprint>>>
    {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            if(!refreshView)
            {
                findViewById(R.id.loadingPanel).setVisibility(View.VISIBLE);
                float alpha = 0.5f;
                findViewById(R.id.loadingPanel).setAlpha(alpha);
            }


        }

        @Override
        protected Pair<Tasks, ArrayList<CheckListForSprint>> doInBackground(String... args)
        {
            String url_view_task = "get_task_details.php";
            JSONParser jParser = new JSONParser();

            List<myDict> params = new ArrayList<myDict>();

            params.add(new myDict("taskID",args[0]));   //Get task based on TaskID
            params.add(new myDict("username",args[1]));
            params.add(new myDict("nature",args[2]));
            JSONObject json = jParser.makeHttpRequest(url_view_task, "GET", params);

            // Check your log cat for JSON reponse
            Log.d("All Tasks: ", json.toString());
            Tasks viewTask = null;
            //TODO:ChangeHere7
            ArrayList<CheckListForSprint> checklistList = new ArrayList<CheckListForSprint>();
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
                    String clevel = c.getString("CriticalLevel");
                    String ucolor = c.getString("uColor");
                    String vcnty = c.getString("Vicinity");
                    String uLevel = c.getString("UrgencyLevel");
                    String displayName = c.getString("displayName");
                    String hiddenUser = c.getString("displayUser");
                    String amnt  = c.getString("Amount");








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
                            String checklistItemID = e.getString("checklistItemID");
                            String completed = e.getString("Completed");

                            boolean boolVal;
                            if(completed.equals("true"))
                            {
                                Log.e("YES","TRUE");
                                boolVal = true;
                            }
                            else
                            {
                                Log.e("no",completed);
                                boolVal = false;
                            }
                            //TODO:ChangeHere5
                            checklistList.add(new CheckListForSprint(type,desc,checklistItemID,boolVal));
                        }


                    }
                    return (new Pair<Tasks,ArrayList<CheckListForSprint>>(viewTask,checklistList));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            //TODO:ChangeHere4
            return null;
        }
        //TODO:ChangeHere3
        protected void onPostExecute(Pair<Tasks, ArrayList<CheckListForSprint>> currentTask) {
            // dismiss the dialog after getting all products
            findViewById(R.id.loadingPanel).setVisibility(View.GONE);
            if(currentTask!=null)
            {

                display(currentTask);
            }
            else
            {
                Log.e("Error","Success was false");
            }




        }
    }
    //TODO:ChangeHere2
    private void display(Pair<Tasks, ArrayList<CheckListForSprint>> pair)
    {
        final Tasks currentTask = pair.getFirst();

        TextView desc = (TextView) findViewById(R.id.desc);
        TextView fullname = (TextView) findViewById(R.id.fullname);

        String username = currentTask.getUsername();

        boolean myTask = username.equals(myUsername);
        Log.e(username,myUsername);

        desc.setText(currentTask.getDesc());

        if(!myTask)
        {
            //if it is my task, display user who accepted it
            //Make accept button change the task status

            refreshView = false;
        }
        else
        {

            refreshView = true;
        }
        fullname.setText(currentTask.getUser());


//TODO:ChangeHere1
        ArrayList<CheckListForSprint> checkList = pair.getSecond();
        /*if(checkList.size()>0)
        {*/
        ListView listview = (ListView) findViewById(R.id.createChecklist);
        SprintCheckListAdapter sprintCheckListAdapter = new SprintCheckListAdapter(this,checkList,myTask);
        listview.setAdapter(sprintCheckListAdapter);

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

        if(firstTime)
        {
            refresh(refreshView);
            firstTime = false;
        }
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

    void refresh(boolean b){
        Log.e("Refresh",Boolean.toString(b));
        if(b){
            myHandle.postDelayed(new Runnable() {
                @Override
                public void run() {
                    new SprintData().execute(taskID,myUsername,nature);
                    refresh(refreshView);
                }
            }, 1*1000);
        }
        else
        {
            myHandle.removeCallbacksAndMessages(null);
            new SprintData().execute(taskID,myUsername,nature);
        }
    }


}
