package com.example.tabish.taskask;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NavUtils;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.location.FusedLocationProviderApi;

import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;


import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


public class ViewUser extends AppCompatActivity {

    JSONParser jsonParser = new JSONParser();
    private FusedLocationProviderApi mFusedLocationClient;
    Handler myHandle;
    boolean mapReady=false;
    ScrollView mScrollView;
    GoogleMap m_map;
    Marker mark;
    boolean firstRef = true;
    float zoom;
    boolean re = false;
    String userName= "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_user);

        zoom = 15;
        myHandle = new Handler();
        Switch live = (Switch) findViewById(R.id.switchLive);
        live.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    re=true;
                    refresh(re);
                }
                else
                {
                    re=false;
                    refresh(re);

                }

            }
        });
        userName = getIntent().getStringExtra("UserName");

        new LoadUser().execute(userName);


        if (m_map == null) {
            SupportMapFragment spm = (WorkaroundMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
            spm.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap googleMap) {
                    mapReady = true;
                    m_map = googleMap;
                    m_map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                    m_map.getUiSettings().setZoomControlsEnabled(true);
                    mScrollView = (ScrollView) findViewById(R.id.outerScrollView); //parent scrollview in xml, give your scrollview id value

                    ((WorkaroundMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                            .setListener(new WorkaroundMapFragment.OnTouchListener() {
                                @Override
                                public void onTouch() {
                                    mScrollView.requestDisallowInterceptTouchEvent(true);
                                }
                            });
                }
            });


        }

    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        refresh(false);
    }
    void refresh(boolean b){
        if(b){
            myHandle.postDelayed(new Runnable() {
                @Override
                public void run() {
                    new FetchLatLong().execute(userName);
                    refresh(re);
                }
            }, 1*1000);
        }
        else
        {
            myHandle.removeCallbacksAndMessages(null);
            new FetchLatLong().execute(userName);
        }
    }


    public void setupMap(Double latitude,Double longitude)
    {

        LatLng targetPos = new LatLng(latitude,longitude);
        CameraPosition target = CameraPosition.builder().target(targetPos).zoom(zoom).build();
        if(!firstRef)
        {

            mark.setPosition(targetPos);
            m_map.moveCamera(CameraUpdateFactory.newCameraPosition(target));


        }
        if(firstRef)
        {


            m_map.moveCamera(CameraUpdateFactory.newCameraPosition(target));
            mark = m_map.addMarker(new MarkerOptions()
                .position(targetPos));
            mark.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.markerx));
            m_map.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
                @Override
                public void onCameraChange(CameraPosition cameraPosition) {
                    zoom = m_map.getCameraPosition().zoom;
                }
            });


        }

        if(re)
        {
            m_map.getUiSettings().setAllGesturesEnabled(false);
        }
        else
        {
            m_map.getUiSettings().setAllGesturesEnabled(true);
        }

        firstRef=false;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                refresh(false);
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void displayDetails(UserDetail arg)
    {
        //Toast.makeText(getApplicationContext(), "Success finding user.",Toast.LENGTH_SHORT).show();
        TextView username = (TextView) findViewById(R.id.username);
        TextView postedTasks = (TextView) findViewById(R.id.postedTasks);
        TextView completedTasks = (TextView) findViewById(R.id.completedTasks);
        RatingBar eRating = (RatingBar) findViewById(R.id.errandRunnerRating);
        RatingBar cRating = (RatingBar) findViewById(R.id.customerRating);
        TextView status = (TextView) findViewById(R.id.statusOfUser);
        username.setText(arg.getFullname());
        status.setText(arg.getStatus());
        int pTasksInt = arg.getPostedTasks();
        postedTasks.setText(Integer.toString(pTasksInt));

        int cTasksInt = arg.getCompletedTasks();
        completedTasks.setText(Integer.toString(cTasksInt));

        float eRatingBarFloat = arg.geteRating();
        eRating.setRating(eRatingBarFloat);

        float cRatingBarFloat = arg.getcRating();
        cRating.setRating(cRatingBarFloat);

        Double latitude = arg.getLatitude();
        Double longitude = arg.getLongitude();
        if(mapReady)
        {
            setupMap(latitude,longitude);
        }
    }

    class LoadUser extends AsyncTask<String,Void,UserDetail>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        protected UserDetail doInBackground(String... args)
        {
            UserDetail details = null;
            String urlx = "get_user_details.php";
            List<myDict> params = new ArrayList<myDict>();
            params.add(new myDict("Username",args[0]));

            JSONObject jobj = jsonParser.makeHttpRequest(urlx,"GET",params);
            int success = 0;
            try
            {

                success = jobj.getInt("success");
            }
            catch (JSONException e)
            {
                Log.e("View User",e.toString());
            }

            if(success==1)
            {
                try
                {
                    String fullname = jobj.getString("Fullname");
                    String status = jobj.getString("Status");
                    String username = jobj.getString("Fullname");
                    BigDecimal.valueOf(jobj.getDouble("eRating")).floatValue();
                    float erating = BigDecimal.valueOf(jobj.getDouble("eRating")).floatValue();
                    float crating = BigDecimal.valueOf(jobj.getDouble("cRating")).floatValue();
                    int postedTasks = jobj.getInt("postedTasks");
                    int completedTasks = jobj.getInt("completedTasks");
                    Log.e("Showing int",Integer.toString(completedTasks));
                    String number = jobj.getString("NUMBER");
                    Double latitude = jobj.getDouble("Latitude");
                    Double longitude = jobj.getDouble("Longitude");
                    details = new UserDetail(fullname,username,number,status,crating,erating,"","",postedTasks,completedTasks,latitude,longitude);
                    return details;

                }
                catch (JSONException e)
                {
                    Log.e("ViewUser",e.toString());
                }

            }
            return details;

        }

        protected void onPostExecute(UserDetail arg)
        {
            if(arg!=null)
            {
                displayDetails(arg);

            }
            else
            {
               // Toast.makeText(getApplicationContext(), "No or Bad user found! Try again",Toast.LENGTH_SHORT).show();
            }
        }
    }


    class FetchLatLong extends AsyncTask<String,Void,LatLng>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        protected LatLng doInBackground(String... args)
        {
            LatLng pos = null;
            String urlx = "get_pos.php";
            List<myDict> params = new ArrayList<myDict>();
            params.add(new myDict("Username",args[0]));

            JSONObject jobj = jsonParser.makeHttpRequest(urlx,"GET",params);
            int success = 0;
            try
            {

                success = jobj.getInt("success");
            }
            catch (JSONException e)
            {
                Log.e("View Latlong",e.toString());
            }

            if(success==1)
            {
                try
                {

                    Double latitude = jobj.getDouble("Latitude");
                    Double longitude = jobj.getDouble("Longitude");
                    pos = new LatLng(latitude,longitude);
                    return pos;

                }
                catch (JSONException e)
                {
                    Log.e("ViewUser",e.toString());
                }

            }
            return pos;

        }

        protected void onPostExecute(LatLng arg)
        {
            if(arg!=null)
            {
                if(mapReady)
                {
                    Log.e("RefreshAs",Double.toString(arg.latitude));
                    setupMap(arg.latitude,arg.longitude);
                }

            }
            else
            {
                // Toast.makeText(getApplicationContext(), "No or Bad user found! Try again",Toast.LENGTH_SHORT).show();
            }
        }
    }
}
