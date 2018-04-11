package com.example.tabish.taskask;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class TaskComplete extends AppCompatActivity {
    String fullName;
    String otherUser;
    String taskID;
    String userRated;
    String nature;
    String action;
    String taskAmount;
    Button finishButton;
    EditText amount;
    JSONParser jsonParser;
    SessionManager session;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_complete);
        jsonParser = new JSONParser();
        finishButton = (Button) findViewById(R.id.finishButton);
        amount = (EditText) findViewById(R.id.amountText);
        fullName = getIntent().getStringExtra("DisplayName");
        taskID =getIntent().getStringExtra("TaskID");
        otherUser = getIntent().getStringExtra("OtherUser");
        nature = getIntent().getStringExtra("nature");
        action = getIntent().getStringExtra("action");
        taskAmount = getIntent().getStringExtra("amount");
        session = new SessionManager(getApplicationContext());
        TextView displayName = findViewById(R.id.displayName);
        displayName.setText(fullName);

        RatingBar rateUser = (RatingBar) findViewById(R.id.rateUser);
        rateUser.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {

                finishButton.setVisibility(View.VISIBLE);
                userRated = Float.toString(rating);
                Toast.makeText(getApplicationContext(),userRated,Toast.LENGTH_SHORT).show();
                //Intent homeScreen = new Intent(getApplicationContext(),AllTasksActivity.class);
                //startActivity(homeScreen);
                //finish();
            }
        });
        amount.setHint(taskAmount);
        if(nature.equals("SprintAsCustomer"))
        {
            amount.setEnabled(false);

        }
        else
        {
            amount.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    taskAmount = s.toString();
                }
            });
        }
        taskAmount = amount.getText().toString();

        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(taskAmount.equals(""))
                {
                    taskAmount="0";
                }
               new RateUser().execute(otherUser,taskID,userRated,nature,action,taskAmount);
            }
        });



    }

    class RateUser extends AsyncTask<String,Void,Boolean>
    {

        @Override
        protected Boolean doInBackground(String... args)
        {
            String url_rateUser = "rate_user.php";
            List<myDict> params = new ArrayList<myDict>();
            params.add(new myDict("username",args[0]));
            params.add(new myDict("taskID",args[1]));
            params.add(new myDict("rating",args[2]));
            params.add(new myDict("nature",args[3]));
            params.add(new myDict("action",args[4]));
            params.add(new myDict("amount",args[5]));
            JSONObject jObj = jsonParser.makeHttpRequest(url_rateUser,"POST",params);

            try
            {
                int success = jObj.getInt("success");
                if(success==1)
                {
                    return Boolean.TRUE;
                }
            }
            catch(JSONException e)
            {
                Log.e("RatingError",e.toString());
            }

            return Boolean.FALSE;
        }

        @Override
        protected void onPostExecute(Boolean success)
        {
            if(success==Boolean.TRUE)
            {
                Toast.makeText(getApplicationContext(),"Thankyou!",Toast.LENGTH_SHORT).show();

            }
            else
            {
                Toast.makeText(getApplicationContext(),"Rating Failed - Try again later",Toast.LENGTH_SHORT).show();

            }
            if(nature.equals("SprintAsCustomer"))
            {
                session.endSprint();
                Intent mainScreen = new Intent(getApplicationContext(),AllTasksActivity.class);
                mainScreen.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(mainScreen);
            }
            else
            {
                finish();
            }

        }
    }
}
