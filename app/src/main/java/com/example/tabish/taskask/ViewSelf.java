package com.example.tabish.taskask;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.RatingBar;
import android.widget.TextView;


import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ViewSelf extends AppCompatActivity {
    SessionManager session;
    JSONParser jsonParser = new JSONParser();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_self);
        session = new SessionManager(getApplicationContext());
        String userName = session.getUserDetails().get("username");
        new LoadSelf().execute(userName);
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

    private void displayDetails(UserDetail arg)
    {
        //Toast.makeText(getApplicationContext(), "Success finding user.",Toast.LENGTH_SHORT).show();
        TextView username = (TextView) findViewById(R.id.username);
        TextView fullname = (TextView) findViewById(R.id.fullname);
        TextView postedTasks = (TextView) findViewById(R.id.postedTasks);
        TextView completedTasks = (TextView) findViewById(R.id.completedTasks);
        TextView totalEarnings = (TextView) findViewById(R.id.totalEarnings);
        TextView totalSpendings = (TextView) findViewById(R.id.totalSpendings);
        TextView currentDebt = (TextView) findViewById(R.id.currentDebt);
        TextView status = (TextView) findViewById(R.id.statusUser);
        TextView number = (TextView) findViewById(R.id.phoneNumber);
        TextView vehicle = (TextView) findViewById(R.id.vehicle);
        RatingBar eRating = (RatingBar) findViewById(R.id.errandRunnerRating);
        RatingBar cRating = (RatingBar) findViewById(R.id.customerRating);

        username.setText(arg.getUsername());
        fullname.setText(arg.getFullname());
        status.setText(arg.getStatus());
        number.setText(arg.getNumber());
        vehicle.setText(arg.getVehicle());

        int pTasksInt = arg.getPostedTasks();
        postedTasks.setText(Integer.toString(pTasksInt));

        int cTasksInt = arg.getCompletedTasks();
        completedTasks.setText(Integer.toString(cTasksInt));


        //TODO: Get eanrning and spending from query
        int earning = 0;
        totalEarnings.setText(Integer.toString(earning));

        int spending = 0;
        totalSpendings.setText(Integer.toString(spending));

        int debt = arg.getDebt();
        currentDebt.setText(Integer.toString(debt));

        float eRatingBarFloat = arg.geteRating();
        eRating.setRating(eRatingBarFloat);

        float cRatingBarFloat = arg.getcRating();
        cRating.setRating(cRatingBarFloat);



    }

    class LoadSelf extends AsyncTask<String,Void,UserDetail>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        protected UserDetail doInBackground(String... args)
        {
            UserDetail details = null;
            String urlx = "get_self_details.php";
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
                    String username = jobj.getString("Username");
                    float eRating = BigDecimal.valueOf(jobj.getDouble("eRating")).floatValue();
                    float cRating = BigDecimal.valueOf(jobj.getDouble("cRating")).floatValue();
                    int postedTasks = jobj.getInt("postedTasks");
                    int completedTasks = jobj.getInt("completedTasks");
                    int debt = jobj.getInt("debt");
                    String vehicle = jobj.getString("Vehicle");
                    String status = jobj.getString("Status");
                    String number = jobj.getString("NUMBER");

                    details = new UserDetail( username, fullname, number, status, postedTasks, completedTasks,  vehicle, cRating, eRating, debt);
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



}
