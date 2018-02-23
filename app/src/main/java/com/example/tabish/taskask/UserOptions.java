package com.example.tabish.taskask;

import android.app.ActivityOptions;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;


public class UserOptions extends AppCompatActivity {
    SessionManager session;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_options);
        session = new SessionManager(getApplicationContext());
        ListView lv = (ListView) findViewById(android.R.id.list);
        ArrayList<myDict> arList = new ArrayList<myDict>();
        arList.add(new myDict("Profile",Integer.toString(R.drawable.profleicon)));
        arList.add(new myDict("Current Proposals",Integer.toString(R.drawable.proposalsicon)));
        arList.add(new myDict("Proposed Tasks",Integer.toString(R.drawable.acceptedicon)));
        arList.add(new myDict("Posted Tasks",Integer.toString(R.drawable.postedicon)));
        arList.add(new myDict("Logout",Integer.toString(R.drawable.logouticon)));
        UserOptionsAdapter uvAdapter = new UserOptionsAdapter(this,arList);
        lv.setAdapter(uvAdapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {


                myDict entry = (myDict) parent.getItemAtPosition(position);

                if(entry.getName().equals("Profile"))
                {
                    Intent i = new Intent(getApplicationContext(),ViewSelf.class);
                    Log.e("User options","Reaching");
                    startActivity(i);
                }

                if(entry.getName().equals("Current Proposals"))
                {
                    Intent i = new Intent(getApplicationContext(),ViewAcceptedTasks.class);
                    i.putExtra("nature","Proposals");
                    startActivity(i);


                }

                if(entry.getName().equals("Proposed Tasks"))
                {
                    Intent i = new Intent(getApplicationContext(),ViewAcceptedTasks.class);
                    i.putExtra("nature","Proposed Tasks");
                    startActivity(i);

                }

                if(entry.getName().equals("Posted Tasks"))
                {
                    Intent i = new Intent(getApplicationContext(),ViewAcceptedTasks.class);
                    i.putExtra("nature","Posted Tasks");
                    startActivity(i);
                }

                if(entry.getName().equals("Logout"))
                {
                    session.logoutUser();
                    finish();
                }

            }
        });
    }
}
