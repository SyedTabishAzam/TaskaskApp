
package com.example.tabish.taskask;
import java.sql.Time;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Handler;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.ActivityCompat;
import android.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tabish.taskask.R;
import com.google.android.gms.maps.model.LatLng;

import static android.view.Gravity.START;

public class AllTasksActivity extends ListActivity {

    // Progress Dialog
    //private ProgressDialog pDialog;

    // Creating JSON Parser object
    JSONParser jParser = new JSONParser();
    LocationManager mLocationManager;


    // url to get all products list
    private static String url_all_products = "get_all_details.php";

    // JSON Node names
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_tasks = "tasks";
    private static final String UrgencyColor = "uColor";
    private static final String TAG_tid = "idTask";
    private static final String TAG_NAME = "tag";
    private static final String description = "description";
    private static final String PaymentStatus = "PaymentStatus";
    private static final String CriticalLevel = "CriticalLevel";
    private static final String PostedBy = "PostedBy";
    private static final String TaskStatus = "tstatus";
    private static final String Vicinity = "Vicinity";
    private static final String UrgencyLevel = "UrgencyLevel";
    private static final String PostedTime  = "PostedTime";
    private static final String TimeLimit  = "TimeLimit";
    private static final String AcceptedTime = "AcceptedTime";
    private static final String CompletedTime = "CompletedTime";
    private static final String Fee  = "Fee";
    private static final String Rate = "Rate";
    private static final String Amount  = "Amount";
    private static final String AmountPaid = "AmountPaid";
    private static final String RatingCustomer = "RatingCustomer";
    private static final String RatingErrand = "RatingErrand";

    // tasks JSONArray
    JSONArray tasks = null;
    SwipeRefreshLayout mSwipeRefreshLayout;
    SessionManager session;
    //button for creating task
    Handler myHandle;
    Button createTask;
    Button filter;
    boolean re = true;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.all_products);
        //getLoaderManager().initLoader(0,null,this);

        session = new SessionManager(getApplicationContext());
        if(!session.isLoggedIn())
        {
            session.checkLogin();
            re = false;
            this.finish();
        }



        HashMap<String, String> user = session.getUserDetails();
        String username = user.get(SessionManager.KEY_USERNAME);
        myHandle = new Handler();
        mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        try
        {
            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, new android.location.LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    Log.e("ListenToChange","try");
                    new UpdateLocation().execute((session.getUserDetails().get(SessionManager.KEY_USERNAME)),Double.toString(location.getLatitude()),Double.toString(location.getLongitude()));
                }
                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {            }
                @Override
                public void onProviderEnabled(String provider) {            }
                @Override
                public void onProviderDisabled(String provider) {            }
            });
        }
        catch (SecurityException e)
        {

        }

        // Loading products in Background Thread
        new LoadAllTasks().execute();

        createTask = (Button) findViewById(R.id.createButton);
        filter = (Button) findViewById(R.id.filterButton);
        createTask.setOnClickListener(new View.OnClickListener(){
              @Override
              public void onClick(View view) {
                  // creating new task in background thread
                 Intent i = new Intent(getApplicationContext(), CreateTask.class);
                 startActivityForResult(i,3);

        }
        });

        ImageView userOptions = (ImageView) findViewById(R.id.userOption);
        userOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getApplicationContext(),UserOptions.class);
                startActivity(i);

            }
        });
        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.e("AllTask","Registering to filter");
            }
        });



        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener(){
            @Override
            public void onRefresh() {
                refreshContent();
            }
        });


    };


    private void refreshContent(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                new LoadAllTasks().execute();
                mSwipeRefreshLayout.setRefreshing(false);
            };
        },0);
    }



    private void displayTasks(ArrayList<Tasks> tasklist)
    {   TextView noData = (TextView) findViewById(R.id.noData);
        ListView listview = (ListView) findViewById(android.R.id.list);
       if (tasklist.size()>0)
       {
        noData.setVisibility(View.GONE);
        TasksAdapter taskadapter = new TasksAdapter(this,tasklist);
        listview.setAdapter(taskadapter);
        listview.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                Tasks entry = (Tasks) parent.getItemAtPosition(position);
                Intent viewTask = new Intent(getApplicationContext(),ViewTask.class);
                viewTask.putExtra("TaskID",entry.getID());
                viewTask.putExtra("nature","ProposableTasks");
                startActivityForResult(viewTask,4);

            }
        });
       }else
       {

           noData.setVisibility(View.VISIBLE);
       }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if ((requestCode == 3) || (requestCode == 4)) {
            refreshContent();
        }
    }

    class LoadAllTasks extends AsyncTask<String, Void, ArrayList<Tasks>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        /**
         * getting All Tasks from url
         * */
        protected ArrayList<Tasks> doInBackground(String... args) {

            List<myDict> params = new ArrayList<myDict>();
           params.add(new myDict("true","true"));
           JSONObject json = jParser.makeHttpRequest(url_all_products, "GET", params);

            // Check your log cat for JSON reponse
            Log.d("All Tasks: ", json.toString());
            ArrayList<Tasks> tasklist = new ArrayList<Tasks>();
            try {
                // Checking for SUCCESS TAG
                int success = json.getInt(TAG_SUCCESS);
                if (success == 1) {
                    // Tasks found
                    // Getting Array of Tasks
                    tasks = json.getJSONArray(TAG_tasks);

                    // looping through All Tasks
                    for (int i = 0; i < tasks.length(); i++) {
                        JSONObject c = tasks.getJSONObject(i);

                        // Storing each json item in variable
                        String id = c.getString(TAG_tid);
                        String tag = c.getString(TAG_NAME);
                        String descript = c.getString(description);
                        String pstatus = c.getString(PaymentStatus);
                        String ts = c.getString(TaskStatus);
                        String clevel = c.getString(CriticalLevel);
                        String ucolor = c.getString(UrgencyColor);
                        String pby = c.getString(PostedBy);
                        String taskStatus = c.getString(PaymentStatus);
                        String vcnty = c.getString(Vicinity);
                        String uLevel = c.getString(UrgencyLevel);
                        String poT  = c.getString(PostedTime);
                        String tl  = c.getString(TimeLimit);
                        String acT = c.getString(AcceptedTime);
                        String ct = c.getString(CompletedTime);
                        String fees  = c.getString(Fee);
                        String rt = c.getString(Rate);
                        String amnt  = c.getString(Amount);
                        String apaid = c.getString(AmountPaid);
                        String rc = c.getString(RatingCustomer);
                        String re = c.getString(RatingErrand);





                        // adding HashList to ArrayList
                        tasklist.add(new Tasks(tag,pby,fees,uLevel,clevel,ucolor,tl,id));
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
            // dismiss the dialog after getting all products
//            pDialog.dismiss();
            displayTasks(tasklist);




        }

    }

    class UpdateLocation extends AsyncTask<String,Void,Boolean>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        protected Boolean doInBackground(String... args)
        {
            String urlx = "update_pos.php";
            List<myDict> params = new ArrayList<myDict>();
            params.add(new myDict("Username",args[0]));
            params.add(new myDict("Latitude",args[1]));
            params.add(new myDict("Longitude",args[2]));
            JSONObject jobj = jParser.makeHttpRequest(urlx,"POST",params);
            int success = 0;
            try
            {

                success = jobj.getInt("success");
            }
            catch (JSONException e)
            {
                Log.e("Add Latlong",e.toString());
            }

            if(success==1)
            {
                return Boolean.TRUE;


            }

            return Boolean.FALSE;
        }

        protected void onPostExecute(Boolean success)
        {
            if(success==Boolean.TRUE)
            {
                Log.e("UpdatePost","Success");

            }
            else
            {
                Log.e("UpdatePost","Failed");
            }
        }
    }
}