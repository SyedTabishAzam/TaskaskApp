
package com.example.tabish.taskask;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class MainScreenActivity extends Activity{



    JSONParser jsonParser = new JSONParser();

    EditText uname;
    EditText pswd;
    Button registerBtn;
    Button loginBtn;
    SessionManager session;
    private static String url_login = "login.php";
    private static final String TAG_SUCCESS = "success";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        session = new SessionManager(getApplicationContext());

        // Buttons
        registerBtn = (Button) findViewById(R.id.registerBtn);
        loginBtn = (Button) findViewById(R.id.login);
        //Edit text
        uname = (EditText) findViewById(R.id.uname);
        pswd = (EditText) findViewById(R.id.pswd);

        registerBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                // Launching register Activity
                Intent i = new Intent(getApplicationContext(), Register.class);
                startActivity(i);

            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                String _name = uname.getText().toString();
                String _pswd = pswd.getText().toString();
                new Login().execute(_name,_pswd);
            }
        });
    }

    class Login extends AsyncTask<String, Void, List<String>> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            findViewById(R.id.loadingPanel).setVisibility(View.VISIBLE);
            float alpha = 0.5f;
            findViewById(R.id.loadingPanel).setAlpha(alpha);


        }

        /**
         * Creating product
         * */
        protected List<String> doInBackground(String... args) {



            // Building Parameters.
            List<String> arr = new ArrayList<String>();
            List<myDict> params = new ArrayList<myDict>();
            params.add(new myDict("uname", args[0])); //Custom arraylist can be created
            params.add(new myDict("pwd", args[1]));
            //params.add(new BasicNameValuePair("pswd", _pswd));





            JSONObject json = jsonParser.makeHttpRequest(url_login,"POST", params);


            Log.d("Create Response", json.toString());

            // check for success tag
            try {

                int success = json.getInt(TAG_SUCCESS);
                if (success == 1) {

                    try {
                        String username = json.getString("username");
                        String fullname = json.getString("fullname");
                        arr.add(username);
                        arr.add(fullname);
                        return arr;
                    }
                    catch (JSONException e)
                    {
                        Log.e("MainScreenActivity","Login Error");
                    }

                } else if (success == 2) {


                    Log.e("MainScreenActivity","Invalid Password");
                    arr.add("incorrectPassword");

                }
                else if (success == 3) {


                    Log.e("MainScreenActivity","Invalid User");
                    arr.add("incorrectUser");

                }
            } catch (JSONException e) {
                Log.e("MainScreenActivity","Not Successful");
                e.printStackTrace();
            }

            return arr;
        }

        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(List<String> names) {
            // dismiss the dialog once done

            if(names.size()==2)
            {
                session.createLoginSession(names.get(1), names.get(0));
                Intent i = new Intent(getApplicationContext(), AllTasksActivity.class);
                startActivity(i);

                // closing this screen
                finish();
            }
            else if(names.size()==1)
            {
                if(names.get(0).equals("incorrectPassword"))
                    Toast.makeText(getApplicationContext(), "Incorrect Password. Please retry!",Toast.LENGTH_SHORT).show();
                if(names.get(0).equals("incorrectUser"))
                    Toast.makeText(getApplicationContext(), "Incorrect Username. Please retry!",Toast.LENGTH_SHORT).show();
            }
            else
            {
                Toast.makeText(getApplicationContext(), "Connection not available. Please retry!",Toast.LENGTH_SHORT).show();
            }

            if(names.size()!=2)
            {
                findViewById(R.id.loadingPanel).setVisibility(View.GONE);
            }

        }

    }
}