
package com.example.tabish.taskask;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.sql.Time;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.Manifest;
import android.app.Dialog;
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
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
    SQLiteDatabaseHandler db;

    // url to get all products list
    private static String url_all_products = "get_all_details.php";
    private static String url_create_task = "create_task.php";
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
    private View layoutFilterbox;
    // tasks JSONArray
    JSONArray tasks = null;
    SwipeRefreshLayout mSwipeRefreshLayout;
    SessionManager session;
    //button for creating task
    Handler myHandle;
    Button createTask;
    Button filter;

    boolean re = true;
    Double globalRate;
    String startingLatitude;
    String startingLongitude;
    String mRadius;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.all_products);
        //getLoaderManager().initLoader(0,null,this);
        globalRate = 1.0;
        session = new SessionManager(getApplicationContext());
        //session.ClearAll();
        startingLatitude = "24.905294";
        startingLongitude = "67.138026";
        session.checkLogin();

        if(!session.isLoggedIn())
        {

            re = false;
            this.finish();
        }

        session.checkSprint();
        if(session.isInSprint())
        {
            this.finish();
        }

        TextView usernamePlaceholder = (TextView) findViewById(R.id.usernamePlaceholder);
        usernamePlaceholder.setText(session.getUserDetails().get("username"));

        HashMap<String, String> user = session.getUserDetails();
        String username = user.get(SessionManager.KEY_USERNAME);
        myHandle = new Handler();
        mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        try
        {
            //startingLatitude = Double.toString(mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER).getLatitude());
           // startingLongitude = Double.toString(mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER).getLongitude());
            Log.e("AllTaskActivity","Success last known location");
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
            Log.e("AllTaskActivity",e.toString());
           // startingLatitude = "24.905294";
            //startingLongitude = "67.138026";
        }


        // Loading products in Background Thread
        String applyFilter = "default";
        new LoadAllTasks().execute(applyFilter);

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
                final Dialog yourDialog = new Dialog(AllTasksActivity.this);
                LayoutInflater inflater = (LayoutInflater)AllTasksActivity.this.getSystemService(LAYOUT_INFLATER_SERVICE);
                layoutFilterbox = inflater.inflate(R.layout.activity_filter_box, (RelativeLayout) findViewById(R.id.layout_dialog));
                yourDialog.setContentView(layoutFilterbox);
                yourDialog.setTitle("Filter Tasks");
                yourDialog.show();

                final SeekBar vicinityBar = (SeekBar) layoutFilterbox.findViewById(R.id.vicinityBar);
                final TextView vicinityText = (TextView) layoutFilterbox.findViewById(R.id.Vicinity);
                final SeekBar feeBar = (SeekBar) layoutFilterbox.findViewById(R.id.feeBar);
                final TextView FeeText = (TextView) layoutFilterbox.findViewById(R.id.Fee);
                Button update = (Button) layoutFilterbox.findViewById(R.id.updateSearch);

                vicinityText.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(AllTasksActivity.this,VicinityMap.class);
                        i.putExtra("radiusInMeters",vicinityBar.getProgress());
                        startActivityForResult(i, 1);
                    }
                });
                vicinityBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        vicinityText.setText("Vicinity : " + Integer.toString(progress)+ " Meters");
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                });

                feeBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        FeeText.setText("Minimum Fee : PKR " + Integer.toString(progress) );
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                });

                update.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String applyFilter = "filter";

                        String minFee = Double.toString(feeBar.getProgress());
                        mRadius = Integer.toString(vicinityBar.getProgress());
                        new LoadAllTasks().execute(applyFilter,mRadius,startingLatitude,startingLongitude,minFee);
                        yourDialog.dismiss();
                    }
                });
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

                new LoadAllTasks().execute("default");

                new InternetCheck().execute();
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

        if (requestCode == 1) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                Double lat = data.getDoubleExtra("startingLatitude",0);
                Double lon = data.getDoubleExtra("startingLongitude",0);
                startingLatitude = lat.toString();
                startingLongitude = lon.toString();
                Log.e("latlongFromFilter",startingLatitude+','+startingLongitude);
                int radiusInM = data.getIntExtra("Radius",0);
                SeekBar vicinitiyBar = (SeekBar) layoutFilterbox.findViewById(R.id.vicinityBar);
                vicinitiyBar.setProgress(radiusInM);


            }
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
            String filter = args[0];
            if(filter.equals("filter"))
            {
                params.add(new myDict("filter",filter));
                params.add(new myDict("radius",args[1]));
                params.add(new myDict("startingLatitude",args[2]));
                params.add(new myDict("startingLongitude",args[3]));
                params.add(new myDict("minFee",args[4]));

            }
            else
            {
                params.add(new myDict("true","true"));
            }

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

     class InternetCheck extends AsyncTask<Void,Void,Boolean> {




        @Override protected Boolean doInBackground(Void... voids) { try {
            Socket sock = new Socket();
            sock.connect(new InetSocketAddress("192.168.0.108", 80), 1500);
            sock.close();
            return true;
        } catch (IOException e) { return false; } }

        @Override protected void onPostExecute(Boolean internet)
        {
            if(internet==Boolean.TRUE)
            {
                Log.e("Internet Check","Connection Found!");
                AddTaskFromDatabase();
            }
            else
            {
                Log.e("Internet Check","Connection Failed!");
            }
        }
    }

    void AddTaskFromDatabase()
    {
        db = new SQLiteDatabaseHandler(this);
        List<Tasks> taskList = db.allTasks();
        db.deleteAll();
        int idx = 0;
        for (Tasks t : taskList) {
            String _rate = "";
            //Double amount = Double.parseDouble(t.getFee()) * globalRate;
            String _amount = "";
            new postTask().execute(t.getUrgency(),t.getDesc(),t.getFee(),t.getTag(),t.getCritical(),t.getUsername(),t.getTime(),_rate,_amount);
            idx++;
        }
        db.close();
    }

    class postTask extends AsyncTask<String, Void, Integer> {


        /*Initialize JSONarrays for use within async class*/
        JSONArray checklist = new JSONArray();
        JSONArray nature = new JSONArray();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        /**
         * Creating task
         * */
        protected Integer doInBackground(String... args) {

            Double rate = 1.0;

            // Building Parameters
            List<myDict> params1 = new ArrayList<myDict>();
            params1.add(new myDict("true", "true"));


            //Passing built arguments to JSON parser class//
            String url_get_rate = "get_rate.php";
            JSONObject json = jParser.makeHttpRequest(url_get_rate,"GET", params1);

            // check log cat fro response


            // check for success tag - show success message if task created, otherwise show error
            try {
                Log.d("Create Response ", json.toString());
                int success = json.getInt(TAG_SUCCESS);
                if (success == 1) {
                    // closing this screen
                    rate = json.getDouble("rate");

                } else {
                    rate = 1.0;


                }
            } catch (JSONException e) {
                rate = 1.0;
                e.printStackTrace();
            }




            Integer successful;

            // Building Parameters
            Double amount = Double.parseDouble(args[2]) * rate;

            List<myDict> params = new ArrayList<myDict>();
            params.add(new myDict("urgencylevel", args[0]));
            params.add(new myDict("description", args[1]));
            params.add(new myDict("fees", args[2]));
            params.add(new myDict("tag", args[3]));
            params.add(new myDict("clevel", args[4]));
            params.add(new myDict("postedby", args[5]));
            params.add(new myDict("timelimit", args[6]));
            params.add(new myDict("rate", Double.toString(rate)));
            params.add(new myDict("amount", amount.toString()));


            //------------------------------------------//


            //--Adding checklist items as JSON ARRays---P.S The ordering here matters--//
            String checklistStringed = checklist.toString();
            params.add(new myDict("checklistItems", checklistStringed));


            String natureString = nature.toString();
            params.add(new myDict("natureOfElement", natureString));


            //Passing built arguments to JSON parser class//
            json = jParser.makeHttpRequest(url_create_task,"POST", params);

            // check log cat fro response
            Log.d("Create Response ", json.toString());

            // check for success tag - show success message if task created, otherwise show error
            try {

                int success = json.getInt(TAG_SUCCESS);
                if (success == 1) {
                    // closing this screen
                    successful = 1;

                }
                else if(success==4)
                {
                    //SAVETASKTODISK

                    Tasks saveTask = new Tasks();

                    saveTask.setUrgency_(args[0]);
                    saveTask.setDesc_(args[1]);
                    saveTask.setFee_(args[2]);
                    saveTask.setTag_(args[3]);
                    saveTask.setCritical_(args[4]);
                    saveTask.setUsername_(args[5]);
                    saveTask.setTime_(args[6]);

                    db.addTask(saveTask);
                    successful = 4;
                    finish();

                }
                else {
                    successful = 2;
                    Log.e("Create TASK","Nothing Created!");

                }
            } catch (JSONException e) {
                successful = 3;
                e.printStackTrace();
            }

            return successful;
        }

        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(Integer success) {
            // dismiss the dialog once done

            if(success==1)
            {
                Toast.makeText(getApplicationContext(), "Saved tasks posted.",Toast.LENGTH_SHORT).show();
            }
            else if(success==2)
            {
                Toast.makeText(getApplicationContext(), "Saved tasks not Created! Try again.",Toast.LENGTH_SHORT).show();
            }
            else if(success == 3)
            {
                Toast.makeText(getApplicationContext(), "PHP Error.",Toast.LENGTH_SHORT).show();
            }
            else if(success == 4)
            {
                Toast.makeText(getApplicationContext(), "Task saved locally and will be posted when network is available.",Toast.LENGTH_SHORT).show();

            }
        }

    }




}