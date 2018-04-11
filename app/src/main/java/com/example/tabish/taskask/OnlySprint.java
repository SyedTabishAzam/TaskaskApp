package com.example.tabish.taskask;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class OnlySprint extends AppCompatActivity {
    JSONParser jParser = new JSONParser();
    JSONArray sprintTask = null;
    SessionManager sessionManager;

    Button emergencyExit;
    Button safeExit;
    String username;
    SwipeRefreshLayout mSwipeRefreshLayout;
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_tasks = "tasks";
    private static final String TAG_tid = "idTask";
    private static final String TAG_NAME = "tag";
    private static final String PostedBy = "PostedBy";
    private static final String TaskStatus = "tstatus";
    private static final String TimeLimit  = "TimeLimit";
    private static final String AcceptedTime = "AcceptedTime";
    private static final String CompletedTime = "CompletedTime";
    private static final String UrgencyLevel = "UrgencyLevel";
    private static final String CriticalLevel = "CriticalLevel";
    private static final String UrgencyColor = "uColor";
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_only_sprint);
        sessionManager = new SessionManager(this);
        username = sessionManager.getUserDetails().get("username");
        refreshTasks();

        safeExit = (Button) findViewById(R.id.safeExit);
        safeExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sessionManager.endSprint();
                Intent mainScreen = new Intent(getApplicationContext(),AllTasksActivity.class);
                startActivity(mainScreen);
                finish();
            }
        });

        emergencyExit =  (Button) findViewById(R.id.emergencyExit);

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener(){
            @Override
            public void onRefresh() {
                Log.d("RunOnlyOnce", "NOW");
                refreshTasks();

            }
        });
        Log.d("OnlySprin","Starting");
    }
    private void refreshTasks()
    {
        new LoadSprintTasks().execute(username);
    }
    class LoadSprintTasks extends AsyncTask<String, Void, ArrayList<Tasks>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        /**
         * getting All Tasks from url
         * */
        protected ArrayList<Tasks> doInBackground(String... args)
        {

            List<myDict> params = new ArrayList<myDict>();

            String url_my_sprint = "get_sprints.php";
            params.add(new myDict("username",args[0]));


            JSONObject json = jParser.makeHttpRequest(url_my_sprint, "GET", params);

            // Check your log cat for JSON reponse
            Log.d("Sprint Tasks: ", json.toString());
            ArrayList<Tasks> tasklist = new ArrayList<Tasks>();
            try {
                // Checking for SUCCESS TAG
                int success = json.getInt(TAG_SUCCESS);
                if (success == 1) {
                    // Tasks found
                    // Getting Array of Tasks
                    sprintTask = json.getJSONArray(TAG_tasks);

                    // looping through All Tasks
                    for (int i = 0; i < sprintTask.length(); i++) {
                        JSONObject c = sprintTask.getJSONObject(i);

                        // Storing each json item in variable
                        String id = c.getString(TAG_tid);
                        String tag = c.getString(TAG_NAME);
                        String ts = c.getString(TaskStatus);
                        String clevel = c.getString(CriticalLevel);
                        String ucolor = c.getString(UrgencyColor);
                        String pby = c.getString(PostedBy);
                        String taskStatus = c.getString(TaskStatus);
                        String uLevel = c.getString(UrgencyLevel);
                        String tl  = c.getString(TimeLimit);
                        String acT = c.getString(AcceptedTime);
                        String ct = c.getString(CompletedTime);

                        Tasks iterativeTask = new Tasks();
                        iterativeTask.setId_(id);
                        iterativeTask.setTag_(tag);
                        iterativeTask.setTaskStatus(ts);
                        iterativeTask.setCrticialColor_("Black");
                        iterativeTask.setCritical_(clevel);
                        iterativeTask.setUrgency_(uLevel);
                        iterativeTask.setTime_(tl);
                        iterativeTask.setPostedByname(pby);
                        iterativeTask.setTaskStatus(taskStatus);
                        iterativeTask.setUrgencyColor_(ucolor);





                        // adding HashList to ArrayList
                        tasklist.add(iterativeTask);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return tasklist;
        }

        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(ArrayList<Tasks> tasklist) {
            displayTasks(tasklist);
            mSwipeRefreshLayout.setRefreshing(false);
        }

    }

    private void displayTasks(ArrayList<Tasks> tasklist)
    {   TextView noData = (TextView) findViewById(R.id.noData);
        ListView listview = (ListView) findViewById(android.R.id.list);
        if (tasklist.size()>0)
        {
            noData.setVisibility(View.GONE);
            SprintTasksAdapter taskadapter = new SprintTasksAdapter(this,tasklist);
            listview.setAdapter(taskadapter);
            listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position,
                                        long id) {
                    Tasks entry = (Tasks) parent.getItemAtPosition(position);
                    Intent sprintActivity = new Intent(getApplicationContext(), Sprint.class);
                    sprintActivity.putExtra("TaskID",entry.getID());
                    startActivity(sprintActivity);

                }
            });
        }else
        {

            noData.setVisibility(View.VISIBLE);
        }
    }
}
