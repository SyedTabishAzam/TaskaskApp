package com.example.tabish.taskask;

import java.util.ArrayList;
import java.util.List;


import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class Register extends Activity {

    // Progress Dialog
    private ProgressDialog pDialog;


    JSONParser jsonParser = new JSONParser();
    EditText nam;
    EditText gender;
    EditText cnic;
    EditText pass;
    EditText numb;
    EditText city;
    EditText address;
    EditText vehicle;



    // url to create new product
    private static String url_create_user = "add_user.php";

    // JSON Node names
    private static final String TAG_SUCCESS = "success";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        // Edit Text
        nam = (EditText) findViewById(R.id.nam);
        gender = (EditText) findViewById(R.id.gender);
        vehicle = (EditText) findViewById(R.id.vehicle);
        cnic = (EditText) findViewById(R.id.cnic);
        address = (EditText) findViewById(R.id.address);
        city = (EditText) findViewById(R.id.city);
        numb = (EditText) findViewById(R.id.numb);
        pass = (EditText) findViewById(R.id.pass);

        // register button
        Button registerBtn = (Button) findViewById(R.id.registerUser);

        // button click event
        registerBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                // creating new product in background thread
                new CreateNewUser().execute();
            }
        });
    }

    /**
     * Background Async Task to Create new product
     * */
    class CreateNewUser extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Register.this);
            pDialog.setMessage("Creating User..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        /**
         * Creating product
         * */
        protected String doInBackground(String... args) {

            String _name = nam.getText().toString();
            String _cnic = cnic.getText().toString();
            String _vehicle = vehicle.getText().toString();
            String _gender = gender.getText().toString();
            String _city = city.getText().toString();
            String _password = pass.getText().toString();
            String _address = address.getText().toString();
            String _number = numb.getText().toString();

            // Building Parameters
            List<myDict> params = new ArrayList<myDict>();
            params.add(new myDict("name", _name));
            params.add(new myDict("gender", _gender));
            params.add(new myDict("cnic", _cnic));
            params.add(new myDict("address", _address));
            params.add(new myDict("city", _city));
            params.add(new myDict("number", _number));
            params.add(new myDict("vehicle", _vehicle));
            params.add(new myDict("password", _password));


            // getting JSON Object
            // Note that create product url accepts POST method

            JSONObject json = jsonParser.makeHttpRequest(url_create_user,"POST", params);

            // check log cat fro response
            Log.d("Create Response", json.toString());

            // check for success tag
            try {

                int success = json.getInt(TAG_SUCCESS);

                if (success == 1) {
                    // successfully created product
                    Intent i = new Intent(getApplicationContext(), MainScreenActivity.class);
                    startActivity(i);

                    // closing this screen
                    finish();
                } else {
                    // failed to create product

                }
            } catch (JSONException e) {

                e.printStackTrace();
            }

            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog once done
            pDialog.dismiss();
        }

    }
}