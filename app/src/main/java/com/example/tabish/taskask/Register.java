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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;


public class Register extends Activity {

    // Progress Dialog



    JSONParser jsonParser = new JSONParser();
    EditText username;
    EditText fullname;
    EditText cnic;
    EditText pass;
    EditText numb;
    EditText city;
    EditText address;
    EditText vehicle;
    RadioGroup rg;


    // url to create new product
    private static String url_create_user = "add_user.php";

    // JSON Node names
    private static final String TAG_SUCCESS = "success";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        // Edit Text
        username = (EditText) findViewById(R.id.userName);
        fullname = (EditText) findViewById(R.id.fullName);
        vehicle = (EditText) findViewById(R.id.vehicle);
        cnic = (EditText) findViewById(R.id.cnic);
        address = (EditText) findViewById(R.id.address);
        city = (EditText) findViewById(R.id.city);
        numb = (EditText) findViewById(R.id.numb);
        pass = (EditText) findViewById(R.id.pass);
        rg = (RadioGroup) findViewById(R.id.radioGroup);



        // register button
        Button registerBtn = (Button) findViewById(R.id.registerUser);

        // button click event
        registerBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                // creating new product in background thread
                String _username = username.getText().toString();
                String _fullname = fullname.getText().toString();
                String _cnic = cnic.getText().toString();
                String _vehicle = vehicle.getText().toString();

                String _city = city.getText().toString();
                String _password = pass.getText().toString();
                String _address = address.getText().toString();
                String _number = numb.getText().toString();


                int genderId = rg.getCheckedRadioButtonId();

                boolean valid = validate(_username,_cnic,_vehicle,genderId,_city,_password,_address,_number,_fullname);

                if(valid)
                {

                    RadioButton selectedRadioButton = (RadioButton) findViewById(genderId);
                    String _gender = selectedRadioButton.getText().toString();
                    new CreateNewUser().execute(_username,_number,_address,_cnic,_vehicle,_city,_gender,_password,_fullname);
                }
                else
                {
                    ScrollView sv = (ScrollView) findViewById(R.id.sv);
                    sv.scrollTo(0, sv.getTop());
                }

            }
        });
    }



    private boolean validate(String _username,String _cnic,String _vehicle,int _gender,String _city,String _password,String _address,String _number,String _fullname)
    {
        String checker = "true";
        if(_fullname.equals("") || _fullname.matches(".*\\d+.*"))
        {
            TextView error = (TextView) findViewById(R.id.errorFullname);
            error.setVisibility(View.VISIBLE);
            checker = "false";
        }
        else
        {
            TextView error = (TextView) findViewById(R.id.errorFullname);
            error.setVisibility(View.GONE);
        }

        if(_username.matches("^.*[^a-zA-Z0-9 ].*$") || _username.equals(""))
        {
            TextView error = (TextView) findViewById(R.id.errorUsername);
            error.setText("*Username not valid. Allowed characters: A-Z,a-z,0-9");
            error.setVisibility(View.VISIBLE);

            checker = "false";
        }
        else if (_username.length()<5)
        {
            TextView error = (TextView) findViewById(R.id.errorUsername);
            error.setText("*Username not valid. Minimum 5 characters required!");
            error.setVisibility(View.VISIBLE);
            checker = "false";
        }
        else
        {
            TextView error = (TextView) findViewById(R.id.errorUsername);
            error.setVisibility(View.GONE);
        }

        if(_cnic.length()!=13 )
        {
            TextView error = (TextView) findViewById(R.id.errorCNIC);
            error.setVisibility(View.VISIBLE);
            checker = "false";
        }else
        {
            TextView error = (TextView) findViewById(R.id.errorCNIC);
            error.setVisibility(View.GONE);
        }

        if(_password.equals(""))
        {
            TextView error = (TextView) findViewById(R.id.errorPassword);
            error.setVisibility(View.VISIBLE);
            checker = "false";
        }
        else
        {
            TextView error = (TextView) findViewById(R.id.errorPassword);
            error.setVisibility(View.GONE);
        }

        if(_vehicle.equals(""))
        {
            TextView error = (TextView) findViewById(R.id.errorVehicle);
            error.setVisibility(View.VISIBLE);
            checker = "false";
        }
        else
        {
            TextView error = (TextView) findViewById(R.id.errorVehicle);
            error.setVisibility(View.GONE);
        }

        if(_city.equals(""))
        {
            TextView error = (TextView) findViewById(R.id.errorCity);
            error.setVisibility(View.VISIBLE);
            checker = "false";
        }
        else
        {
            TextView error = (TextView) findViewById(R.id.errorCity);
            error.setVisibility(View.GONE);
        }

        if(_address.equals(""))
        {
            TextView error = (TextView) findViewById(R.id.errorAddress);
            error.setVisibility(View.VISIBLE);
            checker = "false";
        }
        else
        {
            TextView error = (TextView) findViewById(R.id.errorAddress);
            error.setVisibility(View.GONE);
        }

        if(_number.length()!=11)
        {
            TextView error = (TextView) findViewById(R.id.errorNumber);
            error.setVisibility(View.VISIBLE);
            checker = "false";
        }
        else
        {
            TextView error = (TextView) findViewById(R.id.errorNumber);
            error.setVisibility(View.GONE);
        }



        // If nothing is selected from Radio Group, then it return -1
        if (_gender == -1)
        {
            TextView error = (TextView) findViewById(R.id.errorGender);
            error.setVisibility(View.VISIBLE);
            checker = "false";
        }
        else
        {
            TextView error = (TextView) findViewById(R.id.errorGender);
            error.setVisibility(View.GONE);
        }

        if(checker.equals("false"))
            return false;
        return true;
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

        }

        /**
         * Creating product
         * */
        protected String doInBackground(String... args) {



            // Building Parameters

            List<myDict> params = new ArrayList<myDict>();
            params.add(new myDict("username", args[0]));
            params.add(new myDict("number", args[1]));
            params.add(new myDict("address", args[2]));
            params.add(new myDict("cnic", args[3]));
            params.add(new myDict("vehicle", args[4]));
            params.add(new myDict("city", args[5]));
            params.add(new myDict("gender", args[6]));
            params.add(new myDict("password", args[7]));
            params.add(new myDict("fullname", args[8]));

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


                    // closing this screen
                    finish();
                } else {
                    // failed to create user

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

        }

    }
}