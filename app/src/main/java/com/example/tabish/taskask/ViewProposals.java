package com.example.tabish.taskask;

import android.os.AsyncTask;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ViewProposals extends AppCompatActivity {
    JSONParser jsonParser;
    SessionManager session;
    SwipeRefreshLayout mSwipeRefreshLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        jsonParser = new JSONParser();
        setContentView(R.layout.activity_view_proposals);
        ArrayList<UserDetail> users = new ArrayList<UserDetail>();
        //(String fullname,String username, String number, float cRating, float eRating, String address, String cnic,int postedTasks,int completedTasks,Double latitude, Double longitude)

        String taskId = getIntent().getStringExtra("TaskID");
        new LoadProposers().execute(taskId);

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener(){
            @Override
            public void onRefresh() {
                refreshContent();
            }
        });


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

    private void refreshContent()
    {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                new LoadProposers().execute("60");
                mSwipeRefreshLayout.setRefreshing(false);
            };
        },0);
    }

    void display(ArrayList<UserDetail> users)
    {

        ListView lv = (ListView) findViewById(android.R.id.list);
        ProposalAdapter ua = new ProposalAdapter(this,users);
        lv.setAdapter(ua);
    }

    class LoadProposers extends AsyncTask<String ,Void,ArrayList<UserDetail>>
    {

        @Override
        protected ArrayList<UserDetail> doInBackground(String... args) {
            String url_view_proposers = "get_proposers.php";
            ArrayList<UserDetail> proposedUsersList = new ArrayList<UserDetail>();
            List<myDict> params = new ArrayList<myDict>();
            params.add(new myDict("TaskID",args[0]));
            JSONObject jObj = jsonParser.makeHttpRequest(url_view_proposers,"GET",params);
            try {
                int success = jObj.getInt("success");
                if(success==1)
                {
                    JSONArray proposers = jObj.getJSONArray("proposers");
                    for (int i = 0; i < proposers.length(); i++) {
                        JSONObject currentProp = proposers.getJSONObject(i);
                        String fullname = currentProp.getString("Fullname");
                        String status = currentProp.getString("Status");
                        float rating = BigDecimal.valueOf(currentProp.getDouble("eRating")).floatValue();
                        Double latitude = currentProp.getDouble("Latitude");
                        Double longitude = currentProp.getDouble("Longitude");
                        String username = currentProp.getString("Username");

                        UserDetail ud = new UserDetail();
                        ud.setFullname(fullname);
                        ud.setStatus(status);
                        ud.seteRating(rating);
                        ud.setLatitude(latitude);
                        ud.setLongitude(longitude);
                        ud.setUsername(username);

                        proposedUsersList.add(ud);
                    }
                    return proposedUsersList;
                }
            }
            catch (JSONException e)
            {

            }
            return null;
        }

        protected void onPostExecute(ArrayList<UserDetail> list)
        {
            if(list!=null)
            {
                display(list);

            }
            else
            {
                Toast.makeText(getApplicationContext(),"Empty list",Toast.LENGTH_SHORT).show();
            }
        }
    }
}
