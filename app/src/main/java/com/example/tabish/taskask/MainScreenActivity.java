
package com.example.tabish.taskask;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
import android.content.Intent;
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

    private static String url_login = "login.php";
    private static final String TAG_SUCCESS = "success";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Buttons
        registerBtn = (Button) findViewById(R.id.registerBtn);
        loginBtn = (Button) findViewById(R.id.login);
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

                new Login().execute();
            }
        });
    }

    class Login extends AsyncTask<String, Void, Boolean> {

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
        protected Boolean doInBackground(String... args) {

            String _name = uname.getText().toString();
            _name = "\"" + _name + "\"";

            String _pswd = pswd.getText().toString();


            // Building Parameters.

            List<myDict> params = new ArrayList<myDict>();
            params.add(new myDict("uname", _name)); //Custom arraylist can be created
            //params.add(new BasicNameValuePair("pswd", _pswd));





            JSONObject json = jsonParser.makeHttpRequest(url_login,"GET", params);


            Log.d("Create Response", json.toString());

            // check for success tag
            try {

                int success = json.getInt(TAG_SUCCESS);


                if (success == 1) {

                    Intent i = new Intent(getApplicationContext(), AllTasksActivity.class);
                    startActivity(i);

                    // closing this screen
                    finish();
                } else {


                   return Boolean.FALSE;


                }
            } catch (JSONException e) {

                e.printStackTrace();
            }

            return Boolean.TRUE;
        }

        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(Boolean nexScreen) {
            // dismiss the dialog once done

            if(nexScreen==Boolean.TRUE)
            {

            }
            else
            {
                findViewById(R.id.loadingPanel).setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), "Connection to server failed! Please retry.",Toast.LENGTH_SHORT).show();
            }

        }

    }
}