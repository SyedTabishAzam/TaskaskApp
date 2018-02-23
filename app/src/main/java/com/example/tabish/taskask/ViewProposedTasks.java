package com.example.tabish.taskask;

import java.util.ArrayList;
import java.util.List;


import android.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
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

public class ViewProposedTasks extends AppCompatActivity {

    private static String url_view_task = "get_task_details.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_proposed_tasks);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String id = getIntent().getStringExtra("TaskID");
        new viewTask().execute(id);

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
        Button propose = (Button) findViewById(R.id.propose);
        propose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
        TextView fullname = (TextView) findViewById(R.id.fullname);
        TextView desc = (TextView) findViewById(R.id.desc);
        TextView urgency = (TextView) findViewById(R.id.urgencyTell);
        TextView critical = (TextView) findViewById(R.id.criticalTell);
        TextView total = (TextView) findViewById(R.id.total);

        fullname.setText(currentTask.getUser());
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

        fullname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),ViewUser.class);
                i.putExtra("UserName",currentTask.getUsername());
                startActivity(i);
            }
        });


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
                    String pby = c.getString("PostedBy");
                    String username = c.getString("Username");
                    String vcnty = c.getString("Vicinity");
                    String uLevel = c.getString("UrgencyLevel");
                    //String poT  = c.getString(PostedTime);
                    // String tl  = c.getString(TimeLimit);
                    //String acT = c.getString(AcceptedTime);
                    // String ct = c.getString(CompletedTime);
                    //String fees  = c.getString(Fee);
                    //String rt = c.getString(Rate);
                    String amnt  = c.getString("Amount");
                    //String apaid = c.getString(AmountPaid);
                    // String rc = c.getString(RatingCustomer);
                    //String re = c.getString(RatingErrand);





                    // adding HashList to ArrayList
                    viewTask = new Tasks(tag,pby,amnt,uLevel,clevel,ucolor,"10",id,descript,username);

                    int checklist = json.getInt("checkItems");
                    if(checklist == 1)
                    {

                        JSONArray checkElements = json.getJSONArray("checklist");

                        for(int i =0;i<checkElements.length();i++)
                        {

                            JSONObject e = checkElements.getJSONObject(i);
                            String type = e.getString("type");
                            String desc = e.getString("Description");
//                            String lat = e.getString("Latitude");
//                            String lon = e.getString("Latitude");

//                            if(lat!="null" && lon!="null")
//                            {
//                                desc= lat + "," + lon;
//                            }
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


}


