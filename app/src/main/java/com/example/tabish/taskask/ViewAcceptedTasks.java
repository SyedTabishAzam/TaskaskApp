package com.example.tabish.taskask;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ViewAcceptedTasks extends AppCompatActivity {

    SessionManager session;
    SwipeRefreshLayout mSwipeRefreshLayout;
    JSONParser jParser = new JSONParser();
    Handler myHandle;
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_tasks = "tasks";
    private static final String UrgencyColor = "uColor";
    private static final String TAG_tid = "idTask";
    private static final String TAG_NAME = "tag";
    private static final String CriticalLevel = "CriticalLevel";
    private static final String PostedBy = "PostedBy";
    private static final String UrgencyLevel = "UrgencyLevel";
    private static final String TimeLimit  = "TimeLimit";
    private static final String Fee  = "Fee";
    private String nature;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_accepted_tasks);

        session = new SessionManager(getApplicationContext());
        String uname = session.getUserDetails().get("username");
        nature = getIntent().getStringExtra("nature");
        setTitle(nature);
        new LoadAcceptedTasks().execute(uname,nature);

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener(){
            @Override
            public void onRefresh() {
                refreshContent();
            }
        });
    }

    private void refreshContent(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                String uname = session.getUserDetails().get("username");
                new LoadAcceptedTasks().execute(uname,nature);
                mSwipeRefreshLayout.setRefreshing(false);
            };
        },0);
    }

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

    class LoadAcceptedTasks extends AsyncTask<String,Void,ArrayList<Tasks>>
    {

        @Override
        protected ArrayList<Tasks> doInBackground(String... args) {
            List<myDict> params = new ArrayList<myDict>();
            params.add(new myDict("username",args[0]));
            params.add(new myDict("nature",args[1]));
            String url_accepted_tasks = "get_accepted_task_details.php";
            JSONObject json = jParser.makeHttpRequest(url_accepted_tasks, "GET", params);

            // Check your log cat for JSON reponse
            Log.d("Accepted Tasks: ", json.toString());
            ArrayList<Tasks> tasklist = new ArrayList<Tasks>();
            try {
                // Checking for SUCCESS TAG
                int success = json.getInt(TAG_SUCCESS);
                if (success == 1) {
                    // Tasks found
                    // Getting Array of Tasks
                    JSONArray tasks = null;
                    tasks = json.getJSONArray(TAG_tasks);

                    // looping through All Tasks
                    for (int i = 0; i < tasks.length(); i++) {
                        JSONObject c = tasks.getJSONObject(i);

                        // Storing each json item in variable
                        String id = c.getString(TAG_tid);
                        String tag = c.getString(TAG_NAME);
                        String clevel = c.getString(CriticalLevel);
                        String ucolor = c.getString(UrgencyColor);
                        String pby = c.getString(PostedBy);
                        String uLevel = c.getString(UrgencyLevel);
                        String tl  = c.getString(TimeLimit);
                        String fees  = c.getString(Fee);






                        // adding HashList to ArrayList
                        tasklist.add(new Tasks(tag,pby,fees,uLevel,clevel,ucolor,tl,id));
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return tasklist;
        }

        protected void onPostExecute(ArrayList<Tasks> tasklist) {

            displayTasks(tasklist);


        }
    }

    private void displayTasks(ArrayList<Tasks> tasklist)
    {   TextView noData = (TextView) findViewById(R.id.noData);
        ListView listview = (ListView) findViewById(android.R.id.list);
        if (tasklist.size()>0)
        {
            noData.setVisibility(View.GONE);

            if(nature.equals("Proposals"))
            {

                ProposalTasksAdapter hackedProposalAdapter = new ProposalTasksAdapter(this,tasklist);
                listview.setAdapter(hackedProposalAdapter);

                listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position,
                                            long id) {
                        Tasks entry = (Tasks) parent.getItemAtPosition(position);
                        String TaskId = entry.getID();

                        Intent viewProposers = new Intent(getApplicationContext(),ViewProposals.class);
                        viewProposers.putExtra("TaskID",TaskId);
                        startActivity(viewProposers);


                    }
                });

            }
            else
            {

                TasksAdapter taskadapter = new TasksAdapter(this,tasklist);
                listview.setAdapter(taskadapter);

                listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position,
                                            long id) {
                        Tasks entry = (Tasks) parent.getItemAtPosition(position);
                        String TaskId = entry.getID();

                        Intent viewTask = new Intent(getApplicationContext(),ViewTask.class);
                        viewTask.putExtra("TaskID",TaskId);
                        viewTask.putExtra("nature",nature);
                        startActivityForResult(viewTask,4);


                    }
                });
            }
        }else
        {

            noData.setVisibility(View.VISIBLE);
        }
    }
}
